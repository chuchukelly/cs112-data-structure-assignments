package restaurant;
/**
 * Use this class to test your Menu method. 
 * This class takes in two arguments:
 * - args[0] is the menu input file
 * - args[1] is the output file
 * 
 * This class:
 * - Reads the input and output file names from args
 * - Instantiates a new RUHungry object
 * - Calls the menu() method 
 * - Sets standard output to the output and prints the restaurant
 *   to that file
 * 
 * To run: java -cp bin restaurant.Menu menu.in menu.out
 * 
 */
public class Menu {
    public static void main(String[] args) {

	// 1. Read input files
	// Option to hardcode these values if you don't want to use the command line arguments
	   
        String inputFile = args[0];
        String outputFile = args[1];
        String inputFile2 = args[2];
        String inputFile3 = args[3];
        String inputFile4 = args[4];
        String inputFile5 = args[5];
        String inputFile6 = args[6];
        
        StdOut.println("INput file: "+ inputFile);
	StdOut.println("OUTput file: "+ outputFile);
        StdOut.println("INput file2: "+ inputFile2);
        StdOut.println("INput file3: "+ inputFile3);
        StdOut.println("INput file4: "+ inputFile4);
	StdOut.println("INput file5: "+ inputFile5);
        StdOut.println("INput file6: "+ inputFile6);


        // 2. Instantiate an RUHungry object
        RUHungry rh = new RUHungry();

	// 3. Call the menu() method to read the menu
        rh.menu(inputFile);
        rh.createStockHashTable(inputFile2);
        rh.updatePriceAndProfit();

        StdIn.setFile(inputFile3);
        int numOrders = StdIn.readInt();
        StdIn.readLine(); // Move to the next line after reading the number of orders
        // Process each order
        for (int i = 0; i < numOrders; i++) {
                // Read the order quantity
                int amount = StdIn.readInt();
                StdIn.readChar(); // Read the space between quantity and dish name
                
                // Read the dish name
                String dishName = StdIn.readLine(); // Trim to remove leading/trailing spaces
                
                // Call the order method for the current order
                rh.order(dishName, amount);
        }
     
        /*StdIn.setFile(inputFile4);
        int numdonate = StdIn.readInt();
        StdIn.readLine();
        for (int i = 0; i < numdonate; i++) {
                // Read the order quantity
                int quantity = StdIn.readInt();
                StdIn.readChar(); // Read the space between quantity and dish name
                        
                // Read the dish name
                String ingredientName = StdIn.readLine(); // Trim to remove leading/trailing spaces
                        
                // Call the order method for the current order
                rh.donation(ingredientName, quantity);
        }*/
        StdIn.setFile(inputFile5);
        int numrestock = StdIn.readInt();
        StdIn.readLine();
        for (int i = 0; i < numrestock; i++) {
                // Read the order quantity
                int quantity = StdIn.readInt();
                StdIn.readChar(); // Read the space between quantity and dish name
                        
                // Read the dish name
                String ingredientName = StdIn.readLine(); // Trim to remove leading/trailing spaces
                        
                // Call the order method for the current order
                rh.restock(ingredientName, quantity);
        }
        /*StdIn.setFile(inputFile6);
        int nums = StdIn.readInt();
        StdIn.readLine(); // Move to the next line after reading the number of orders
        // Process each order
        for (int i = 0; i < nums; i++) {
                String cllass=StdIn.readString();
                // Read the order quantity
                int amount = StdIn.readInt();
                StdIn.readChar(); // Read the space between quantity and dish name
                
                // Read the dish name
                String dishName = StdIn.readLine(); // Trim to remove leading/trailing spaces
                if(cllass=="order"){
                rh.order(dishName, amount);
                }
                if(cllass=="restock"){
                        rh.restock(dishName, amount);
                }
                if(cllass=="donation"){
                        rh.donation(dishName, amount);
                }
        }*/

        StdOut.println("Called menu: "+ inputFile);
        StdOut.println("Called stock: "+ inputFile2);

	// 4. Set output file
	// Option to remove this line if you want to print directly to the screen
        StdOut.setFile(outputFile);

	// 5. Print restaurant
        rh.printRestaurant();
    }
}
