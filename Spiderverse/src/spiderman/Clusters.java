package spiderman;

import java.util.ArrayList;
import java.util.List;

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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String colliderOutputFile = args[1];

        // Set input and output files
        StdIn.setFile(dimensionInputFile);
        StdOut.setFile(colliderOutputFile);

        // Step 1: Read from the dimension input file
        int numDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();

        // Initialize cluster table
        ClusterHashTable clusterHashTable = new ClusterHashTable(initialSize, threshold);

        // Read dimensions and add to clusters
        for (int i = 0; i < numDimensions; i++) {
            int dimensionNumber = StdIn.readInt();
            int numCanonEvents = StdIn.readInt();
            int dimensionWeight = StdIn.readInt();

            ClusterHashTable.Dimension dimension = new ClusterHashTable.Dimension(dimensionNumber, numCanonEvents, dimensionWeight);
            clusterHashTable.addDimension(dimension);
        }

        // Step 2: Write to the collider output file
        clusterHashTable.connectClusters();
        List<List<ClusterHashTable.Dimension>> clusters = clusterHashTable.getClusters();
        for (List<ClusterHashTable.Dimension> cluster : clusters) {
            for (ClusterHashTable.Dimension dimension : cluster) {
                StdOut.print(dimension.getDimensionNumber() + " ");
            }
            StdOut.println();
        }
    }
    
}
