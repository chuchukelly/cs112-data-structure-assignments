package spiderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String colliderOutputFile = args[2];

        // Initialize cluster table from dimension data
        ClusterHashTable clusterHashTable = initializeClusterHashTable(dimensionInputFile);

        // Read and add people to dimensions
        List<Person> people = readPeopleFromFile(spiderverseInputFile);
        insertPeopleIntoDimensions(clusterHashTable, people);

        // Connect clusters and generate adjacency list
        clusterHashTable.connectClusters();
        List<List<Integer>> adjacencyList=clusterHashTable.generateAdjacencyList();
        //for (List<Integer> pair : adjacencyList) {System.out.println(pair);}

        // Write adjacency list to output file
        writeAdjacencyListToFile(colliderOutputFile, adjacencyList);
    }

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

    private static void writeAdjacencyListToFile(String outputFile, List<List<Integer>> adjacencyList) {
        StdOut.setFile(outputFile);
        /*Map<Integer, Set<Integer>> linkMap = new HashMap<>();
        for (List<Integer> pair : adjacencyList) {
            // Each pair is bidirectional
            linkMap.computeIfAbsent(pair.get(1), k -> new HashSet<>()).add(pair.get(0));
        }

        // Output the map
        for (Map.Entry<Integer, Set<Integer>> entry : linkMap.entrySet()) {
            StdOut.print(entry.getKey() + " ");
            for (Integer link : entry.getValue()) {
                StdOut.print(link + " ");
            }
            StdOut.println();
        }*/

        Map<Integer, List<Integer>> map = new LinkedHashMap<>();
        // 填充映射
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

        // 输出映射
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            StdOut.print(entry.getKey());
            for (Integer linked : entry.getValue()) {
                StdOut.print(" " + linked);
            }
            StdOut.println(); // 换行符，新的一行
        }
    
    
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
    



    public static class Person {
        private int currentDimension;
        private String name;
        private int dimensionalSignature;
    
        public Person(int currentDimension, String name, int dimensionalSignature) {
            this.currentDimension = currentDimension;
            this.name = name;
            this.dimensionalSignature = dimensionalSignature;
        }
    
        public int getCurrentDimension() {return currentDimension;}
    
        public void setCurrentDimension(int currentDimension) {this.currentDimension = currentDimension;}
    
        public String getName() {return name;}
    
        public int getDimensionalSignature() {return dimensionalSignature;}
    }
}