import java.util.*;

/*
 A simple console-based Dungeon Game.
 - Use W/A/S/D to move, Q to quit.
 - Reach the Exit (E) to win.
 - Avoid enemies (X). If they touch you, you lose.
 - Pick up potions (P) to heal.
 Legend: 
   # = wall, . = floor, @ = you, E = exit, X = enemy, P = potion
*/
public class DungeonGame {
    // Game configuration
    private static final int WIDTH = 15;
    private static final int HEIGHT = 10;
    private static final double WALL_DENSITY = 0.16;  // fraction of walls
    private static final int MAX_GEN_ATTEMPTS = 60;   // attempts to make a reachable map
    private static final int ENEMY_COUNT = 4;
    private static final int POTION_COUNT = 3;
    private static final int PLAYER_MAX_HP = 5;

    // Tiles
    private static final char WALL = '#';
    private static final char FLOOR = '.';
    private static final char PLAYER = '@';
    private static final char ENEMY = 'X';
    private static final char EXIT = 'E';
    private static final char POTION = 'P';

    private static class Pos {
        int r, c;
        Pos(int r, int c) { this.r = r; this.c = c; }
        @Override public boolean equals(Object o) {
            if (!(o instanceof Pos)) return false;
            Pos p = (Pos) o; return r == p.r && c == p.c;
        }
        @Override public int hashCode() { return Objects.hash(r, c); }
    }

    private static class PlayerState {
        Pos pos;
        int hp = PLAYER_MAX_HP;
        int potions = 0;
    }

    private static class EnemyState {
        Pos pos;
        EnemyState(Pos p) { this.pos = p; }
    }

    private final Random rng = new Random();
    private char[][] map;
    private PlayerState player;
    private Pos exit;
    private final List<EnemyState> enemies = new ArrayList<>();

    public static void main(String[] args) {
        new DungeonGame().run();
    }

    private void run() {
        // Generate a valid dungeon
        generateReachableDungeon();
        // Place player, enemies, potions, exit already done in generation

        try (Scanner sc = new Scanner(System.in)) {
            printIntro();
            boolean running = true;
            boolean won = false;

            while (running) {
                render();
                if (player.hp <= 0) { System.out.println("You died! Game over."); break; }
                if (player.pos.equals(exit)) { System.out.println("You found the exit! You win!"); won = true; break; }

                System.out.print("Move (W/A/S/D), U=Use Potion, Q=Quit: ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                char cmd = Character.toUpperCase(line.charAt(0));

                switch (cmd) {
                    case 'W': attemptMove(-1, 0); break;
                    case 'S': attemptMove(1, 0); break;
                    case 'A': attemptMove(0, -1); break;
                    case 'D': attemptMove(0, 1); break;
                    case 'U': usePotion(); break;
                    case 'Q': running = false; continue;
                    default:
                        System.out.println("Unknown command.");
                        continue;
                }

                // Enemies take their turn after player moves/acts
                moveEnemies();

                // Check collisions after enemy moves
                for (EnemyState e : enemies) {
                    if (e.pos.equals(player.pos)) {
                        System.out.println("An enemy hits you for 1 damage!");
                        player.hp -= 1;
                        break;
                    }
                }
            }

            if (!won && player.hp > 0) {
                System.out.println("You gave up. Maybe next time!");
            }
        }
    }

    private void printIntro() {
        System.out.println("\n=== How to Play ===");
        System.out.println("Objective: Reach the Exit (E) without dying.");
        System.out.println("Legend: # wall, . floor, @ you, E exit, X enemy, P potion");
        System.out.println("Controls: W/A/S/D to move, U to use potion, Q to quit");
        System.out.println("Rules: Enemies move after you; colliding with them deals 1 damage. Potions heal 3 HP up to 5.");
        System.out.println();
    }

    private void render() {
        // Build a fresh view-layer to include dynamic entities
        char[][] view = new char[HEIGHT][WIDTH];
        for (int r = 0; r < HEIGHT; r++) {
            System.arraycopy(map[r], 0, view[r], 0, WIDTH);
        }
        // Place potions already baked into map
        // Place exit visual
        view[exit.r][exit.c] = EXIT;
        // Place enemies
        for (EnemyState e : enemies) view[e.pos.r][e.pos.c] = ENEMY;
        // Place player last
        view[player.pos.r][player.pos.c] = PLAYER;

        // Clear-ish screen
        System.out.println("\n\n\n\n\n");
        System.out.println("=== Dungeon ===");
        for (int r = 0; r < HEIGHT; r++) {
            StringBuilder sb = new StringBuilder(WIDTH);
            for (int c = 0; c < WIDTH; c++) sb.append(view[r][c]);
            System.out.println(sb);
        }
        System.out.println("HP: " + player.hp + "/" + PLAYER_MAX_HP + "  Potions: " + player.potions);
    }

    private void attemptMove(int dr, int dc) {
        int nr = player.pos.r + dr;
        int nc = player.pos.c + dc;
        if (!inBounds(nr, nc)) return;
        char t = map[nr][nc];
        if (t == WALL) return; // blocked

        // If stepping onto potion, pick it up
        if (t == POTION) {
            player.potions += 1;
            System.out.println("You picked up a potion!");
            map[nr][nc] = FLOOR; // remove from map
        }

        player.pos = new Pos(nr, nc);
        // If stepped onto enemy tile (enemy might be here due to rendering difference), check collision
        for (EnemyState e : enemies) {
            if (e.pos.equals(player.pos)) {
                System.out.println("You bump into an enemy and take 1 damage!");
                player.hp -= 1;
                break;
            }
        }
    }

    private void usePotion() {
        if (player.potions <= 0) {
            System.out.println("No potions to use.");
            return;
        }
        if (player.hp >= PLAYER_MAX_HP) {
            System.out.println("You're already at full health.");
            return;
        }
        player.potions -= 1;
        player.hp = Math.min(PLAYER_MAX_HP, player.hp + 3);
        System.out.println("You drink a potion and heal up!");
    }

    private void moveEnemies() {
        // Enemies move at most 1 tile in a random cardinal direction, preferring moves toward player occasionally
        for (EnemyState e : enemies) {
            int bestDr = 0, bestDc = 0;
            if (rng.nextDouble() < 0.6) {
                // biased towards chasing player
                int dr = Integer.compare(player.pos.r, e.pos.r);
                int dc = Integer.compare(player.pos.c, e.pos.c);
                // randomly choose axis to move on
                if (rng.nextBoolean()) { bestDr = dr; bestDc = 0; } else { bestDr = 0; bestDc = dc; }
            } else {
                // random move
                int[][] dirs = { {1,0},{-1,0},{0,1},{0,-1},{0,0} }; // including staying sometimes
                int[] d = dirs[rng.nextInt(dirs.length)];
                bestDr = d[0]; bestDc = d[1];
            }
            int nr = e.pos.r + bestDr;
            int nc = e.pos.c + bestDc;
            if (inBounds(nr, nc) && map[nr][nc] != WALL) {
                // avoid stacking too many enemies on one cell if possible
                boolean occupiedByEnemy = false;
                for (EnemyState other : enemies) {
                    if (other != e && other.pos.r == nr && other.pos.c == nc) { occupiedByEnemy = true; break; }
                }
                if (!occupiedByEnemy) {
                    e.pos = new Pos(nr, nc);
                }
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < HEIGHT && c >= 0 && c < WIDTH;
    }

    private void generateReachableDungeon() {
        // We'll try multiple times until we get a start->exit reachable map
        for (int attempt = 0; attempt < MAX_GEN_ATTEMPTS; attempt++) {
            // Initialize empty map with floors
            map = new char[HEIGHT][WIDTH];
            for (int r = 0; r < HEIGHT; r++) Arrays.fill(map[r], FLOOR);

            // Place walls randomly with some density, leaving a border of floors for fairness
            for (int r = 1; r < HEIGHT - 1; r++) {
                for (int c = 1; c < WIDTH - 1; c++) {
                    if (rng.nextDouble() < WALL_DENSITY) map[r][c] = WALL;
                }
            }

            // Choose start (top-left-ish) and exit (bottom-right-ish)
            Pos start = findFirstOpen(new int[][] { {1,1}, {0,0}, {0,1}, {1,0} }, new Pos(0,0));
            Pos maybeExit = findFirstOpen(new int[][] { {HEIGHT-2, WIDTH-2}, {HEIGHT-1, WIDTH-1}, {HEIGHT-2, WIDTH-1}, {HEIGHT-1, WIDTH-2} }, new Pos(HEIGHT-1, WIDTH-1));
            if (start == null || maybeExit == null) continue;

            // Ensure a path by carving a simple drunk-walk from start to exit, then sprinkle more walls
            carvePath(start, maybeExit);

            // Validate reachability using BFS
            if (!reachable(start, maybeExit)) continue;

            // Place player and exit
            this.player = new PlayerState();
            this.player.pos = start;
            this.exit = maybeExit;

            // Place potions on floor tiles
            placeObjects(POTION, POTION_COUNT, Set.of(start, maybeExit));

            // Place enemies on floor tiles, not on player/exit/potions
            placeEnemies(ENEMY_COUNT, Set.of(start, maybeExit));

            // Final validation: ensure enemies are not immediately overlapping player
            boolean ok = true;
            for (EnemyState e : enemies) if (e.pos.equals(player.pos)) { ok = false; break; }
            if (!ok) { enemies.clear(); continue; }

            // Good dungeon generated
            return;
        }
        // Fallback: if generation fails, create a simple corridor map
        System.out.println("Note: Using fallback simple map generation.");
        map = new char[HEIGHT][WIDTH];
        for (int r = 0; r < HEIGHT; r++) Arrays.fill(map[r], FLOOR);
        for (int r = 0; r < HEIGHT; r++) { map[r][0] = WALL; map[r][WIDTH-1] = WALL; }
        for (int c = 0; c < WIDTH; c++) { map[0][c] = WALL; map[HEIGHT-1][c] = WALL; }
        this.player = new PlayerState();
        this.player.pos = new Pos(1,1);
        this.exit = new Pos(HEIGHT-2, WIDTH-2);
        placeObjects(POTION, POTION_COUNT, Set.of(player.pos, exit));
        placeEnemies(ENEMY_COUNT, Set.of(player.pos, exit));
    }

    private Pos findFirstOpen(int[][] candidates, Pos fallback) {
        for (int[] rc : candidates) {
            int r = clamp(rc[0], 0, HEIGHT-1);
            int c = clamp(rc[1], 0, WIDTH-1);
            if (map[r][c] != WALL) return new Pos(r, c);
        }
        return (map[fallback.r][fallback.c] != WALL) ? fallback : null;
    }

    private int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }

    private void carvePath(Pos from, Pos to) {
        // Drunkard walk from 'from' to 'to', carving floors
        int r = from.r, c = from.c;
        map[r][c] = FLOOR;
        int safety = WIDTH * HEIGHT * 10;
        while ((r != to.r || c != to.c) && safety-- > 0) {
            int dr = Integer.compare(to.r, r);
            int dc = Integer.compare(to.c, c);
            // bias towards moving closer but allow random variation
            if (rng.nextBoolean()) r += dr; else c += dc;
            r = clamp(r, 1, HEIGHT-2);
            c = clamp(c, 1, WIDTH-2);
            map[r][c] = FLOOR;
        }
        // Lightly add walls elsewhere to create some structure
        for (int i = 0; i < (WIDTH*HEIGHT)/6; i++) {
            int rr = 1 + rng.nextInt(HEIGHT-2);
            int cc = 1 + rng.nextInt(WIDTH-2);
            if (!(rr == from.r && cc == from.c) && !(rr == to.r && cc == to.c)) {
                if (rng.nextDouble() < 0.35) map[rr][cc] = WALL; else map[rr][cc] = FLOOR;
            }
        }
    }

    private boolean reachable(Pos start, Pos goal) {
        boolean[][] seen = new boolean[HEIGHT][WIDTH];
        ArrayDeque<Pos> q = new ArrayDeque<>();
        q.add(start); seen[start.r][start.c] = true;
        int[][] dirs = { {1,0},{-1,0},{0,1},{0,-1} };
        while (!q.isEmpty()) {
            Pos p = q.poll();
            if (p.equals(goal)) return true;
            for (int[] d : dirs) {
                int nr = p.r + d[0], nc = p.c + d[1];
                if (inBounds(nr, nc) && !seen[nr][nc] && map[nr][nc] != WALL) {
                    seen[nr][nc] = true; q.add(new Pos(nr, nc));
                }
            }
        }
        return false;
    }

    private void placeObjects(char tile, int count, Set<Pos> forbidden) {
        int placed = 0;
        int guard = WIDTH * HEIGHT * 10;
        while (placed < count && guard-- > 0) {
            int r = 1 + rng.nextInt(HEIGHT - 2);
            int c = 1 + rng.nextInt(WIDTH - 2);
            if (map[r][c] == FLOOR) {
                Pos p = new Pos(r, c);
                if (!contains(forbidden, p)) {
                    map[r][c] = tile;
                    placed++;
                }
            }
        }
    }

    private void placeEnemies(int count, Set<Pos> forbidden) {
        enemies.clear();
        int placed = 0;
        int guard = WIDTH * HEIGHT * 10;
        while (placed < count && guard-- > 0) {
            int r = 1 + rng.nextInt(HEIGHT - 2);
            int c = 1 + rng.nextInt(WIDTH - 2);
            if (map[r][c] != WALL) {
                Pos p = new Pos(r, c);
                if (!contains(forbidden, p) && !posHasEnemy(p)) {
                    enemies.add(new EnemyState(p));
                    placed++;
                }
            }
        }
    }

    private boolean posHasEnemy(Pos p) {
        for (EnemyState e : enemies) if (e.pos.equals(p)) return true;
        return false;
    }

    private boolean contains(Set<Pos> set, Pos p) {
        for (Pos s : set) if (s.equals(p)) return true;
        return false;
    }
}
