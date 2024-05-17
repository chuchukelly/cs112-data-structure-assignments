package spiderman;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import spiderman.Collider.Person;

class Bag<T> extends ArrayList<T> { }

public class ClusterHashTable {
    private List<List<Dimension>> clusters;
    private int size;
    private double capacityThreshold;
    private int dimensionsAdded;
    
    
    public ClusterHashTable(int initialSize, double capacityThreshold) {
        this.size = initialSize;
        this.capacityThreshold = capacityThreshold;
        this.dimensionsAdded = 0;
        this.clusters = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            clusters.add(new LinkedList<>());
        }
        
    }

    private void rehash() {
        int newSize = size * 2;
        List<List<Dimension>> newClusters = new ArrayList<>(newSize);
        for (int i = 0; i < newSize; i++) {
            newClusters.add(new LinkedList<>());
        }
        for (List<Dimension> cluster : clusters) {
            for (Dimension dimension : cluster) {
                int newIndex = dimension.getDimensionNumber() % newSize;
                newClusters.get(newIndex).add(0,dimension);
            }
        }
        clusters = newClusters;
        size = newSize;
    }

    public void addDimension(Dimension dimension) {
        int index = dimension.getDimensionNumber() % size;
        clusters.get(index).add(0,dimension); // Add to the front of the list
        dimensionsAdded++;
        double loadFactor = (double) dimensionsAdded / size;
        if (loadFactor >= capacityThreshold) {
            rehash();
        }
    }

    public Dimension getDimensionByNumber(int dimensionNumber) {
        for (List<Dimension> cluster : clusters) {
            for (Dimension dimension : cluster) {
                if (dimension.getDimensionNumber() == dimensionNumber) {
                    return dimension;
                }
            }
        }
        return null;
    }


    public void connectClusters() {
        for (int i = 0; i < size; i++) {
            List<Dimension> currentCluster = clusters.get(i);
            List<Dimension> prev1Cluster = clusters.get((i - 1 + size) % size);
            List<Dimension> prev2Cluster = clusters.get((i - 2 + size) % size);

            if (!prev1Cluster.isEmpty()) {
                Dimension firstPrev1 = prev1Cluster.get(0);
                currentCluster.add(firstPrev1);
            }
            if (!prev2Cluster.isEmpty()) {
                Dimension firstPrev2 = prev2Cluster.get(0);
                currentCluster.add(firstPrev2);
            }
        }
    }

    public List<List<Dimension>> getClusters() {
        return clusters;
    }
    public List<Dimension> getDimensions() {
        List<Dimension> dimensions=new ArrayList<>();
        for (List<Dimension> cluster : clusters) {
            dimensions.add(cluster.get(0));
        }
        return dimensions;
    }
    

    public List<List<Integer>> generateAdjacencyList() {
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (List<Dimension> cluster : clusters) {
            if (!cluster.isEmpty()) {
                List<Integer> dimensionNumbers = new ArrayList<>();
                for (Dimension dimension : cluster) {
                    dimensionNumbers.add(dimension.getDimensionNumber());
                }
                int firstDimension = dimensionNumbers.get(0);
                for (int i = 1; i < dimensionNumbers.size(); i++) {
                    int currentDimension = dimensionNumbers.get(i);
                    // Add edge from first dimension to current dimension
                    List<Integer> edge = new ArrayList<>();
                    edge.add(firstDimension);
                    edge.add(currentDimension);
                    adjacencyList.add(edge);
                    // Add edge from current dimension to first dimension (undirected)
                    List<Integer> reverseEdge = new ArrayList<>();
                    reverseEdge.add(currentDimension);
                    reverseEdge.add(firstDimension);
                    adjacencyList.add(reverseEdge);
                }
            }
        }
        return adjacencyList;
    }
    public static Map<Dimension, Dimension> dijkstra(ClusterHashTable clusterHashTable, int sourceint) {
        Dimension source = clusterHashTable.getDimensionByNumber(sourceint);
        Map<Dimension, Integer> d = new HashMap<>();
        Set<Dimension> fringe = new HashSet<>();
        Map<Dimension, Dimension> pred = new HashMap<>();
        Set<Dimension> done = new HashSet<>();

        // Initialize distances
        for (List<Dimension> cluster : clusterHashTable.getClusters()) {
            for (Dimension v : cluster) {
                d.put(v, Integer.MAX_VALUE);
                pred.put(v, null);
            }
        }

        // Set distance for source
        d.put(source, 0);
        pred.put(source, null);
        fringe.add(source);

        while (!fringe.isEmpty()) {
            int min = Integer.MAX_VALUE;
            Dimension minDimension = source;
            for(Dimension x : fringe) {
                int distance = d.get(x);
                if (distance < min) {
                    min = distance;
                    minDimension = x;
                }
            }
            Dimension m = minDimension;
            done.add(m);
            fringe.remove(minDimension);

            // Go through all the neighbors of current
            for (Integer neighborint : getNeighbors(clusterHashTable, m)) {
                Dimension w = clusterHashTable.getDimensionByNumber(neighborint);
                if (!done.contains(w)) {
                    int edgeWeight = m.getDimensionWeight() + w.getDimensionWeight();
                    int newDistance = d.get(m) + edgeWeight;
                    if(d.get(w)==Integer.MAX_VALUE){
                        d.put(w, newDistance);
                        fringe.add(w);
                        pred.put(w, m);
                    }
                    else if ( d.get(w)>newDistance ) {
                        d.put(w, newDistance);
                        pred.put(w, m);
                    }
                }
            }
        }
        return pred;
    }
    public static Map<Dimension, Integer> dijkstradis(ClusterHashTable clusterHashTable, int sourceint) {
        Dimension source = clusterHashTable.getDimensionByNumber(sourceint);
        Map<Dimension, Integer> d = new HashMap<>();
        Set<Dimension> fringe = new HashSet<>();
        Map<Dimension, Dimension> pred = new HashMap<>();
        Set<Dimension> done = new HashSet<>();

        // Initialize distances
        for (List<Dimension> cluster : clusterHashTable.getClusters()) {
            for (Dimension v : cluster) {
                d.put(v, Integer.MAX_VALUE);
                pred.put(v, null);
            }
        }

        // Set distance for source
        d.put(source, 0);
        pred.put(source, null);
        fringe.add(source);

        while (!fringe.isEmpty()) {
            int min = Integer.MAX_VALUE;
            Dimension minDimension = source;
            for(Dimension x : fringe) {
                int distance = d.get(x);
                if (distance < min) {
                    min = distance;
                    minDimension = x;
                }
            }
            Dimension m = minDimension;
            done.add(m);
            fringe.remove(minDimension);

            // Go through all the neighbors of current
            for (Integer neighborint : getNeighbors(clusterHashTable, m)) {
                Dimension w = clusterHashTable.getDimensionByNumber(neighborint);
                if (!done.contains(w)) {
                    int edgeWeight = m.getDimensionWeight() + w.getDimensionWeight();
                    int newDistance = d.get(m) + edgeWeight;
                    if(d.get(w)==Integer.MAX_VALUE){
                        d.put(w, newDistance);
                        fringe.add(w);
                        pred.put(w, m);
                    }
                    else if ( d.get(w)>newDistance ) {
                        d.put(w, newDistance);
                        pred.put(w, m);
                    }
                }
            }
        }
        return d;
    }


    private static List<Integer> getNeighbors(ClusterHashTable hashTable, Dimension dimension) {
        Map<Integer, List<Integer>> map = new LinkedHashMap<>();
        // 填充映射
        List<List<Integer>> adjacencyList =hashTable.generateAdjacencyList();
        for (List<Integer> pair : adjacencyList) {
            int first = pair.get(0);
            int second = pair.get(1);
            // 如果映射中还没有第一个数字的键，就创建一个
            if (!map.containsKey(first)) {
                map.put(first, new ArrayList<>());
            }
            // 添加与第一个数字关联的第二个数字
            map.get(first).add(second);
        }
        return map.get(dimension.getDimensionNumber());
        
    }

    


    
    
    


    public static class Dimension {
        private int dimensionNumber;
        private int numCanonEvents;
        private int dimensionWeight;
        private List<Person> people = new ArrayList<>();

        public Dimension(int dimensionNumber, int numCanonEvents, int dimensionWeight) {
            this.dimensionNumber = dimensionNumber;
            this.numCanonEvents = numCanonEvents;
            this.dimensionWeight = dimensionWeight;
            this.people = new ArrayList<>();
        }

        public int getDimensionNumber() {
            return dimensionNumber;
        }

        public int getNumCanonEvents() {
            return numCanonEvents;
        }

        public int getDimensionWeight() {
            return dimensionWeight;
        }

        public void addPerson(Person person) {
            people.add(person);
        }
    
        public List<Person> getPeople() {
            return people;
        }
    }

}
