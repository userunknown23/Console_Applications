import java.util.*;

public class ShortestPath {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Input matrix dimensions
        System.out.println("Enter matrix dimensions (n x n):");
        int n = sc.nextInt();
        
        char[][] matrix = new char[n][n];
        
        // Initialize matrix with '0'
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                matrix[i][j] = '0';
            }
        }
        
        // Input adventurer position
        System.out.println("Enter Adventurer position (row col):");
        int aRow = sc.nextInt();
        int aCol = sc.nextInt();
        matrix[aRow][aCol] = 'A';
        
        // Input destination position
        System.out.println("Enter Destination position (row col):");
        int dRow = sc.nextInt();
        int dCol = sc.nextInt();
        matrix[dRow][dCol] = 'D';
        
        // Print the matrix
        System.out.println("Matrix:");
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        
        // Calculate shortest path
        int shortestPath = findShortestPath(aRow, aCol, dRow, dCol);
        
        // Print result
        System.out.println("\nThe shortest path is : " + shortestPath);
        sc.close();  // Close the scanner to prevent resource leak
    }
    
    // Function to find shortest path considering diagonal movements
    static int findShortestPath(int startX, int startY, int endX, int endY) {
        // Using Chebyshev distance formula for 8-directional movement
        // This gives us the minimum number of moves needed
        return Math.max(Math.abs(endX - startX), Math.abs(endY - startY));
    }
}

/*
Sample Input 1:
Enter matrix dimensions (n x n):
5
Enter Adventurer position (row col):
1 1
Enter Destination position (row col):
3 3

Sample Output 1:
Matrix:
0 0 0 0 0
0 A 0 0 0
0 0 0 0 0
0 0 0 D 0
0 0 0 0 0

Answer : 2

Sample Input 2:
Enter matrix dimensions (n x n):
5
Enter Adventurer position (row col):
1 1
Enter Destination position (row col):
2 4

Sample Output 2:
Matrix:
0 0 0 0 0
0 A 0 0 0
0 0 0 0 D
0 0 0 0 0
0 0 0 0 0

Answer : 4
*/