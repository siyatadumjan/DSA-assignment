import java.util.*;

class Maze {
    static final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

    public static int shortestPathAllKeys(String[] grid) {
        int m = grid.length;
        int n = grid[0].length();
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int startX = 0, startY = 0, totalKeys = 0;

        // Scan the grid to find the starting point and total number of keys
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i].charAt(j);
                if (cell == 'S') {
                    startX = i;
                    startY = j;
                } else if (cell >= 'a' && cell <= 'f') {
                    totalKeys |= (1 << (cell - 'a')); // Mark the key as needed to be collected
                }
            }
        }

        queue.offer(new Node(startX, startY, 0, 0)); // Position x, y, steps, keys collected
        visited.add(startX + "," + startY + ",0"); // Initial state

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.keys == totalKeys) return current.steps; // Found all keys

            for (int[] dir : directions) {
                int newX = current.x + dir[0], newY = current.y + dir[1];
                int newKeys = current.keys;
                if (newX >= 0 && newX < m && newY >= 0 && newY < n) {
                    char nextCell = grid[newX].charAt(newY);
                    if (nextCell == 'W') continue; // Wall
                    if (nextCell >= 'A' && nextCell <= 'F' && (newKeys & (1 << (nextCell - 'A'))) == 0) continue; // Locked door without key
                    if (nextCell >= 'a' && nextCell <= 'f') newKeys |= (1 << (nextCell - 'a')); // Collect key

                    String newState = newX + "," + newY + "," + newKeys;
                    if (!visited.contains(newState)) {
                        visited.add(newState);
                        queue.offer(new Node(newX, newY, current.steps + 1, newKeys));
                    }
                }
            }
        }

        return -1; // If not possible to collect all keys
    }

    static class Node {
        int x, y, steps, keys;

        Node(int x, int y, int steps, int keys) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.keys = keys;
        }
    }

    public static void main(String[] args) {
        String[] grid = {"SPaPP", "WWWPW", "bPAPB"};
        System.out.println("Minimum steps: " + shortestPathAllKeys(grid));
    }
}