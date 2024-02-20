import java.util.*;

class Point {
    int x, y, keys;

    public Point(int x, int y, int keys) {
        this.x = x;
        this.y = y;
        this.keys = keys;
    }
}

public class Question4a{
    public int minStepsToCollectAllKeys(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int allKeys = 0; // Bitmask to track collected keys
        int startX = -1, startY = -1; // Starting position

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i][j];
                if (cell == 'S') {
                    startX = i;
                    startY = j;
                } else if (cell >= 'a' && cell <= 'f') {
                    allKeys |= (1 << (cell - 'a')); // Set the bit for the key
                }
            }
        }

        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point(startX, startY, 0));
        boolean[][][] visited = new boolean[m][n][64]; // 64 possible keys (2^6)

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int steps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Point curr = queue.poll();
                int x = curr.x, y = curr.y, keys = curr.keys;

                if (keys == allKeys) {
                    return steps;
                }

                for (int[] dir : dirs) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];

                    if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] != '#') {
                        char nextCell = grid[newX][newY];
                        int newKeys = keys;

                        if (nextCell >= 'A' && nextCell <= 'F') {
                            int door = nextCell - 'A';
                            if ((keys & (1 << door)) == 0) {
                                continue; // Missing key for this door
                            }
                        } else if (nextCell >= 'a' && nextCell <= 'f') {
                            newKeys |= (1 << (nextCell - 'a')); // Collect the key
                        }

                        if (!visited[newX][newY][newKeys]) {
                            visited[newX][newY][newKeys] = true;
                            queue.offer(new Point(newX, newY, newKeys));
                        }
                    }
                }
            }
            steps++;
        }

        return -1; // Cannot collect all keys
    }

    public static void main(String[] args) {
        char[][] grid = {
            {'S', 'P', 'q', 'P', 'P'},
            {'W', 'W', 'W', 'P', 'W'},
            {'r', 'P', 'Q', 'P', 'R'}
        };

        Question4a solver = new Question4a();
        System.out.println("Minimum steps to collect all keys: " + solver.minStepsToCollectAllKeys(grid));
    }
}