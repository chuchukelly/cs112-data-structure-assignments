package spiderman;


import java.util.*;
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
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        StdIn.setFile(dimensionInputFile);
        int numDimensions = StdIn.readInt();
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String collectAnomaliesOutputFile = args[3];
        StdOut.setFile(collectAnomaliesOutputFile);

        // Initialize cluster table from dimension data
        ClusterHashTable clusterHashTable = initializeClusterHashTable(dimensionInputFile);

        // Read and add people to dimensions
        List<Person> people = readPeopleFromFile(spiderverseInputFile);
        insertPeopleIntoDimensions(clusterHashTable, people);

        // Connect clusters and generate adjacency list
        clusterHashTable.connectClusters();
        List<List<Integer>> adjacencyList=clusterHashTable.generateAdjacencyList();
        Map<Integer, List<Integer>> adjacencyLists=setAdjacencyList(adjacencyList);
        Lyla a =new Lyla(adjacencyLists);
        // Read startdimension
        StdIn.setFile(hubInputFile);
        int startdimension = StdIn.readInt();
        //System.out.println(startdimension);
        //a.bfs(startdimension);
        
        List<Person> anomalies = new ArrayList<>();
        List<Person> spiders = new ArrayList<>();
        for(Person person:people){
            
            if(person.getCurrentDimension() != person.getDimensionalSignature()&&person.getCurrentDimension() !=startdimension){
                //System.out.println(person.getName());
                anomalies.add(person);
                for(Person person1:people){
                    if(person1.getCurrentDimension() == person1.getDimensionalSignature()&&person1.getCurrentDimension()==person.getCurrentDimension()){
                        spiders.add(person1);
                        //System.out.println(person1.getName());
                    }

                }
            }
            
        }

        //
        
        //StdOut.setFile(collectAnomaliesOutputFile);
        for(Person person:anomalies){
            //System.out.print(person.getName()+" ");//
            StdOut.print(person.getName()+" ");
            boolean n=false;
            for(Person person1:spiders){
                if(person1.getCurrentDimension()==person.getCurrentDimension()){
                    //System.out.print(person1.getName()+" ");//
                    StdOut.print(person1.getName()+" ");
                    n=true;
                }
            }
            if(n){
                ArrayList<Integer> path=a.shortestPath(startdimension,person.getCurrentDimension(),numDimensions);
                printReversedArrayList(path);
                //StdOut.print("*");//
                StdOut.println();
                //System.out.println();//
            }
            else if(!n){
                ArrayList<Integer> path=a.shortestPath(startdimension,person.getCurrentDimension(),numDimensions);
                printArrayList2(path);
                ArrayList<Integer> path2=a.shortestPath(startdimension,person.getCurrentDimension(),numDimensions);
                printReversedArrayList(path2);
                StdOut.println();
                //System.out.println();
            }
        }

        //
        for(Person person:spiders){
            person.setCurrentDimension(startdimension);
        }
        for(Person person:anomalies){
            person.setCurrentDimension(startdimension);
        }
        insertPeopleIntoDimensions(clusterHashTable, people);

    }
    public static List<String>name(List<Person> people){
        List<String>name=new ArrayList<>();
        return name;
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
    private static Map<Integer, List<Integer>> setAdjacencyList(List<List<Integer>> adjacencyList) {
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
        return map;
    }
    public static void printArrayList(ArrayList<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) { // 不是最后一个元素则添加空格
                sb.append(" ");
            }
        }
        StdOut.print(sb.toString());
    }
    public static void printArrayList2(ArrayList<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) { // 不是最后一个元素则添加空格
                sb.append(" ");
            }
        }
        StdOut.print(sb.toString());
    }
    public static void printReversedArrayList(ArrayList<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = list.size() - 1; i >= 0; i--) {
            sb.append(list.get(i));
            if (i > 0) { // 不是第一个元素则添加空格
                sb.append(" ");
            }
        }
        StdOut.print(sb.toString());
    }
}
