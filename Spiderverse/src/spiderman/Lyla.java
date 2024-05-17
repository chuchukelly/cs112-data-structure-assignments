package spiderman;
import java.util.*;

public class Lyla {
    
    private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
    private List<Integer> path = new ArrayList<>();
    private Set<Integer> visited = new HashSet<>();
    //private Map<Integer, Integer> predecessor = new HashMap<>();
    LinkedList<Integer> path3 = new LinkedList<>();//

    public Lyla(Map<Integer, List<Integer>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    // Method to find the shortest path from a to b using BFS
    /*public List<Integer> findShortestPath(int start, int end) {
        
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> predecessors = new HashMap<>();
        Set<Integer> visited2 = new HashSet<>();

        queue.add(start);
        visited2.add(start);
        predecessors.put(start, null);  // 起点没有前驱

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == end) {
                return reconstructPath(predecessors, end, start);
            }

            for (int neighbor : adjacencyList.get(current)) {
                if (!visited2.contains(neighbor)) {
                    visited2.add(neighbor);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return Collections.emptyList();  // 找不到路径
    }

    private List<Integer> reconstructPath(Map<Integer, Integer> predecessors, int end,int start) {
        //LinkedList<Integer> path2 = new LinkedList<>();
        for (Integer at = end; at != null; at = predecessors.get(at)) {
            path3.addFirst(at);
        }
        return path3;
    }*/

    // Method to perform DFS
    public boolean dfs(int current, int destination) {
        // Add current to path and mark as visited
        path.add(current);
        visited.add(current);

        // Check if destination is reached
        if (current == destination) {
            return true;
        }

        
        // Recur for all the vertices adjacent to current vertex
        for (int neighbor : adjacencyList.get(current)) {
            //path.add(neighbor);
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, destination)) {
                    return true; // If path is found, return true
                    
                }
            }
        }

        // If no path is found, remove current from path and backtrack
        //path.remove(path.size() - 1);
        return false;
    }

    public void bfs(int start) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print( current+" ");

            for (int neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    public ArrayList<Integer> shortestPath(int start, int end, int numDimensions) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        Map<Integer, Integer> predecessor = new HashMap<>(); // Store predecessors as a map
    
        queue.add(start);
        visited.add(start);
    
        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == end) break; // Found the destination vertex
    
            for (int neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    predecessor.put(neighbor, current); // Update predecessor of neighbor
                }
            }
        }
    
        // Reconstruct the shortest path
        ArrayList<Integer> path = new ArrayList<>();
        int current = end;
        while (current != start && predecessor.containsKey(current)) { // Iterate until reaching the start vertex
            path.add(current);
            current = predecessor.get(current);
        }
        path.add(start); // Add the start vertex to the path
        Collections.reverse(path); // Reverse the path to get it in the correct order
        return path;
    }

    

    // Utility method to print the path
    public void printPath(String trackSpotOutputFile) {
        StdOut.setFile(trackSpotOutputFile);
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) StdOut.print(" ");
            StdOut.print(path.get(i));
        }
        StdOut.println();
    }
    public void printPath2(String trackSpotOutputFile) {
        for (int i = 0; i < path3.size(); i++) {
            if (i > 0) StdOut.print(" ");
            StdOut.print(path3.get(i));
        }
        //StdOut.println();
    }
    public void printPath3(String trackSpotOutputFile) {
        for (int i = path3.size()-1; i >= 0; i--) {
            if (i < path3.size()-1) StdOut.print(" ");
            StdOut.print(path3.get(i));
        }
        //StdOut.println();
    }

    public void printvisit(String trackSpotOutputFile) {
        StdOut.setFile(trackSpotOutputFile);
        for (Integer node : visited) {
            StdOut.print(node);
            StdOut.print(" ");
        }
    }

    

    
}
