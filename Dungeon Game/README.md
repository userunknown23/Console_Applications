# Dungeon Game

A small console-based grid dungeon crawler in Java.

## Objective
- Find and reach the Exit tile (E) to win.
- Survive against enemies (X). If they land on you or you bump into them, you take 1 damage.
- Collect potions (P) to heal along the way.

## Legend
- `#` Wall — cannot pass
- `.` Floor — you can move here
- `@` You — the player
- `E` Exit — reach this to win
- `X` Enemy — moves each turn after you
- `P` Potion — pick up to add to your inventory

## Controls
- W — Move up
- A — Move left
- S — Move down
- D — Move right
- U — Use a potion (+3 HP up to 5)
- Q — Quit the game

## Rules
- You start with 5 HP and 0 potions.
- Moving onto `P` picks it up; it’s removed from the map and added to your inventory.
- Using a potion heals 3 HP (up to a maximum of 5).
- Enemies move one step after every action you take, often drifting towards you.
- If you occupy the same tile as an enemy (either you move into them, or they move onto you), you take 1 damage.
- If your HP reaches 0, you die and the game ends.

## How to Run
From a terminal with Java installed:

Compile (if needed):
```powershell
javac "...\Console_Applications\Dungeon Game\DungeonGame.java"
```
Run:
```powershell
java -cp "...\Console_Applications\Dungeon Game" DungeonGame
```

## Tips
- Use corridors to funnel enemies.
- Don’t hoard potions if you’re low; healing early can prevent getting one-shot.
- Listen for when an enemy is close; after each of your moves, they move too.
