package spiderman;
import java.util.*;

import spiderman.ClusterHashTable.Dimension;
import spiderman.Collider.Person;
/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String anomaliesInputFile = args[3];
        String reportOutputFile = args[4];
        StdOut.setFile(reportOutputFile);

        // Initialize cluster table from dimension data
        ClusterHashTable clusterHashTable = initializeClusterHashTable(dimensionInputFile);

        // Read and add people to dimensions
        List<Person> people = readPeopleFromFile(spiderverseInputFile);
        insertPeopleIntoDimensions(clusterHashTable, people);
        clusterHashTable.connectClusters();
        // Read startdimension
        StdIn.setFile(hubInputFile);
        int startdimension = StdIn.readInt();
        // Read AnomaliesInputFile
        StdIn.setFile(anomaliesInputFile);
        int numofanomalies = StdIn.readInt();
        List<Anomaly> anomaliestime = new ArrayList<>();
        for (int i = 0; i < numofanomalies; i++) {
            String nameofanomaly = StdIn.readString();
            //time allotted to return the anomaly home before a canon event is missed
            int time = StdIn.readInt();
            anomaliestime.add(new Anomaly(nameofanomaly, time));
        }

        //
        List<Person> anomalies = new ArrayList<>();
        List<Person> anomaliesinhub = new ArrayList<>();
        for(Person person:people){
            
            if(person.getCurrentDimension() != person.getDimensionalSignature()/*&&person.getCurrentDimension() !=startdimension*/){
                //System.out.println(person.getName());
                if(person.getCurrentDimension() !=startdimension){anomalies.add(person);}
                //anomalies.add(person);
               else{anomaliesinhub.add(person);}
            }
        }
        
        for(Person x:anomaliesinhub){
            //x.setCurrentDimension(startdimension);
            if(isAnomalyExists(anomaliestime, x.getName())){
            StdOut.print(clusterHashTable.getDimensionByNumber(x.getDimensionalSignature()).getNumCanonEvents());
                StdOut.print(" "+x.getName()+" SUCCESS ");
                Map<ClusterHashTable.Dimension,ClusterHashTable.Dimension> predecessors=clusterHashTable.dijkstra(clusterHashTable, startdimension);
                List<ClusterHashTable.Dimension> path=reconstructPath(predecessors,clusterHashTable.getDimensionByNumber(x.getDimensionalSignature()));
                for (ClusterHashTable.Dimension dim : path) {
                    StdOut.print(dim.getDimensionNumber() + " ");
                }
                StdOut.println();
            }
        }
        //insertPeopleIntoDimensions(clusterHashTable, anomaliesinhub);

        for(Person x:anomalies){
            int assigntime=getTimeForAnomaly(anomaliestime,x.getName());
            ClusterHashTable.Dimension dimension=clusterHashTable.getDimensionByNumber(x.getDimensionalSignature());
            Map<ClusterHashTable.Dimension, Integer> distance=clusterHashTable.dijkstradis(clusterHashTable, startdimension);
            Integer intValue = distance.get(dimension);
            if (intValue.intValue() > assigntime) {
                StdOut.print(clusterHashTable.getDimensionByNumber(x.getDimensionalSignature()).getNumCanonEvents()-1);
                StdOut.print(" "+x.getName()+" FAILED ");
                Map<ClusterHashTable.Dimension,ClusterHashTable.Dimension> predecessors=clusterHashTable.dijkstra(clusterHashTable, startdimension);
                List<ClusterHashTable.Dimension> path=reconstructPath(predecessors,clusterHashTable.getDimensionByNumber(x.getDimensionalSignature()));
                for (ClusterHashTable.Dimension dim : path) {
                    StdOut.print(dim.getDimensionNumber() + " ");
                }
                StdOut.println();
            }
            else{
                StdOut.print(clusterHashTable.getDimensionByNumber(x.getDimensionalSignature()).getNumCanonEvents());
                StdOut.print(" "+x.getName()+" SUCCESS ");
                Map<ClusterHashTable.Dimension,ClusterHashTable.Dimension> predecessors=clusterHashTable.dijkstra(clusterHashTable, startdimension);
                List<ClusterHashTable.Dimension> path=reconstructPath(predecessors,clusterHashTable.getDimensionByNumber(x.getDimensionalSignature()));
                for (ClusterHashTable.Dimension dim : path) {
                    StdOut.print(dim.getDimensionNumber() + " ");
                }
                StdOut.println();
            }

        }
        
        /*Map<ClusterHashTable.Dimension,ClusterHashTable.Dimension> predecessors=clusterHashTable.dijkstra(clusterHashTable, startdimension);
        
        List<ClusterHashTable.Dimension> path=buildPath(predecessors, /*clusterHashTable.getDimensionByNumber(42),clusterHashTable.getDimensionByNumber(startdimension));
        for (Map.Entry<ClusterHashTable.Dimension, ClusterHashTable.Dimension> entry : predecessors.entrySet()) {
            ClusterHashTable.Dimension current = entry.getKey();
            ClusterHashTable.Dimension predecessor = entry.getValue();
            
            if (predecessor != null) {
                System.out.println("Dimension " + current.getDimensionNumber() + " is reached from Dimension " + predecessor.getDimensionNumber());
            } else {
                System.out.println("Dimension " + current.getDimensionNumber() + " is the starting point or has no predecessor.");
            }
        }

        List<ClusterHashTable.Dimension> path=reconstructPath(predecessors,clusterHashTable.getDimensionByNumber(42));
        for (ClusterHashTable.Dimension dim : path) {
            System.out.print(dim.getDimensionNumber() + " ");
        }
        System.out.println("END");*/

        
        
    }


    public static List<ClusterHashTable.Dimension> reconstructPath(Map<ClusterHashTable.Dimension, ClusterHashTable.Dimension> predecessors, ClusterHashTable.Dimension target) {
        LinkedList<ClusterHashTable.Dimension> path = new LinkedList<>();
        for (ClusterHashTable.Dimension at = target; at != null; at = predecessors.get(at)) {
            path.addFirst(at);
        }
        return path;
    }

    /*public static  Map<ClusterHashTable.Dimension, ClusterHashTable.Dimension> dijkstra(ClusterHashTable clusterHashTable,int sourceint) {
        ClusterHashTable.Dimension source=clusterHashTable.getDimensionByNumber(sourceint);
        Map<ClusterHashTable.Dimension, Integer> d = new HashMap<>();
        PriorityQueue<ClusterHashTable.Dimension> fringe = new PriorityQueue<>(Comparator.comparing(d::get));
        Map<ClusterHashTable.Dimension, ClusterHashTable.Dimension> pred = new HashMap<>();
        Set<ClusterHashTable.Dimension> done = new HashSet<>();
    
        // Initialize distances
        for (List<ClusterHashTable.Dimension> cluster : clusterHashTable.getClusters()) {
            for (ClusterHashTable.Dimension v : cluster) {
                d.put(v, Integer.MAX_VALUE);
                pred.put(v, null);
            }
        }
    
        // Set distance for source
        d.put(source, 0);
        pred.put(source, null);
        fringe.add(source);
    
        while (!fringe.isEmpty()) {
            ClusterHashTable.Dimension m = fringe.poll();
            done.add(m);
    
            // Go through all the neighbors of current
            for (Integer neighborint : getNeighbors(clusterHashTable,m)) {
                ClusterHashTable.Dimension w=clusterHashTable.getDimensionByNumber(neighborint);

                if (!done.contains(w)) {
                    if(d.get(w)==Integer.MAX_VALUE){
                        int edgeWeight = m.getDimensionWeight() + w.getDimensionWeight();
                        int newDistance = d.get(m) + edgeWeight;
                        d.put(w, newDistance);
                        fringe.add(w);
                    }
                    
                    else if (d.get(w)> m.getDimensionWeight() + w.getDimensionWeight()) {
                        d.put(w, d.get(m) + m.getDimensionWeight() + w.getDimensionWeight());
                        pred.put(w, m);
                        
                    }
                }
            }
        }
    
        return pred;
    }
    public static List<ClusterHashTable.Dimension> buildPath(
        Map<ClusterHashTable.Dimension, ClusterHashTable.Dimension> pred,
        ClusterHashTable.Dimension target) {
        
        LinkedList<ClusterHashTable.Dimension> path = new LinkedList<>();
        ClusterHashTable.Dimension step = target;

        // Check if a path exists
        if (!pred.containsKey(step)) {
            return path; // Empty list signifies no path found
        }
        
        for (; step != null; step = pred.get(step)) {
            path.add(step);
        }
        
        Collections.reverse(path); // Reverse the path to get it from source to target
        return path;
    }

    public static void printPath(List<ClusterHashTable.Dimension> path) {
        if (path.isEmpty()) {
            System.out.println("No path exists.");
            return;
        }
        
        System.out.println("Shortest path:");
        for (ClusterHashTable.Dimension dimension : path) {
            System.out.print(dimension.getDimensionNumber() + " -> ");
        }
        System.out.println("END");
    }

    /*public static List<ClusterHashTable.Dimension> reconstructPath(Map<ClusterHashTable.Dimension, ClusterHashTable.Dimension> predecessors, ClusterHashTable.Dimension target,ClusterHashTable.Dimension source) {
        LinkedList<ClusterHashTable.Dimension> path = new LinkedList<>();
        ClusterHashTable.Dimension current = target;

        // Check if a path exists
        if (predecessors.get(current) == null && !current.equals(source)) {
            return path; // empty list signifies no path found
        }

        while (current != null) {
            path.addFirst(current);
            current = predecessors.get(current);
        }

        return path; // The list contains the shortest path from source to target
    }

    public static List<Integer> getNeighbors(ClusterHashTable hashTable, ClusterHashTable.Dimension dimension) {
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
        
    }*/

    private static ClusterHashTable initializeClusterHashTable(String dimensionInputFile) {
        StdIn.setFile(dimensionInputFile);
        int numDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();
        ClusterHashTable hashTable = new ClusterHashTable(initialSize, threshold);

        for (int i = 0; i < numDimensions; i++) {
            int dimensionNumber = StdIn.readInt();
            int numCanonEvents = StdIn.readInt();
            int dimensionWeight = StdIn.readInt();

            ClusterHashTable.Dimension dimension = new ClusterHashTable.Dimension(dimensionNumber, numCanonEvents, dimensionWeight);
            hashTable.addDimension(dimension);
        }

        return hashTable;
    }

    private static List<Person> readPeopleFromFile(String spiderverseInputFile) {
        StdIn.setFile(spiderverseInputFile);
        int numPeople = StdIn.readInt();
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < numPeople; i++) {
            int currentDimension = StdIn.readInt();
            String name = StdIn.readString();
            int dimensionalSignature = StdIn.readInt();
            people.add(new Person(currentDimension, name, dimensionalSignature));
        }

        return people;
    }
    private static void insertPeopleIntoDimensions(ClusterHashTable clusterHashTable, List<Person> people) {
        for (Person person : people) {
            int dimensionNumber = person.getCurrentDimension();
            ClusterHashTable.Dimension dimension = clusterHashTable.getDimensionByNumber(dimensionNumber);
            if (dimension != null) {
                dimension.addPerson(person);
            } else {
                // Optionally handle the case where no dimension matches the person's current dimension
                System.err.println("No dimension found for person " + person.getName() + " in dimension " + dimensionNumber);
            }
        }
    }


    public static class Anomaly{
        private String name;
        private int time;

        public Anomaly(String name, int time) {
            this.name = name;
            this.time = time;
        }

        public String getName() {return name;}
        public int getTime() {return time;}

    }
    public static int getTimeForAnomaly(List<Anomaly> anomalies, String nameofanomaly) {
        for (Anomaly anomaly : anomalies) {
            if (anomaly.getName().equals(nameofanomaly)) {
                return anomaly.getTime();
            }
        }
        return 0;
    }
    public static boolean isAnomalyExists(List<Anomaly> anomalies, String x) {
        for (Anomaly anomaly : anomalies) {
            if (anomaly.getName().equals(x)) {
                return true; // 找到了匹配的异常名称
            }
        }
        return false; // 没有找到匹配的异常名称
    }
}
