package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] data = inputLine.split(",");
        String stateName = data[2];
        StateNode currentState = firstState;
        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
                return;
            }
            currentState = currentState.getNext();
        }
        // If the state does not exist, create a new StateNode instance
        StateNode newState = new StateNode();
        newState.setName(stateName);
        if(firstState==null){firstState=newState;}
        else{
            currentState = firstState;
            while (currentState.getNext() != null) {
                currentState = currentState.getNext();
            }
            currentState.setNext(newState);                 
        }
        
        
    }
        


    

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] data = inputLine.split(",");
        String countyName = data[1];
        String stateName = data[2];
        StateNode currentState = firstState;
        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
                CountyNode currentCounty = currentState.getDown();
                while (currentCounty != null) {
                    if (currentCounty.getName().equals(countyName)) {
                        return;
                    }
                    currentCounty = currentCounty.getNext();
                }

                CountyNode newCounty = new CountyNode();
                newCounty.setName(countyName);
                if (currentState.getDown() == null) {
                    currentState.setDown(newCounty);
                } 
                else{
                    currentCounty = currentState.getDown();
                    while (currentCounty.getNext() != null) {
                        currentCounty = currentCounty.getNext();
                    }
                    currentCounty.setNext(newCounty);
                }
                
                return;
            }
            currentState = currentState.getNext();
        }
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] data = inputLine.split(",");
        String countyName = data[1];
        String stateName = data[2];
        String communityName = data[0];
        double percentAfricanAmerican = Double.parseDouble(data[3]);
        double percentNative = Double.parseDouble(data[4]);
        double percentAsian = Double.parseDouble(data[5]);
        double percentWhite = Double.parseDouble(data[8]);
        double percentHispanic = Double.parseDouble(data[9]);
        String disadvantaged = data[19];
        double PMlevel = Double.parseDouble(data[49]);
        double chanceOfFlood = Double.parseDouble(data[37]);
        double povertyLine = Double.parseDouble(data[121]);
        Data communityData = new Data(percentAfricanAmerican, percentNative, percentAsian, percentWhite, percentHispanic, disadvantaged, PMlevel, chanceOfFlood, povertyLine);
        StateNode currentState = firstState;
        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
                CountyNode currentCounty = currentState.getDown();
                while (currentCounty != null) {
                    if (currentCounty.getName().equals(countyName)) {
                        CommunityNode currentCommunity = currentCounty.getDown();
                        while (currentCommunity != null) {
                            if (currentCommunity.getName().equals(communityName)) {
                                return;
                            }
                            currentCommunity = currentCommunity.getNext();
                        }
                        CommunityNode newCommunity = new CommunityNode();
                        newCommunity.setName(communityName);
                        if (currentCounty.getDown() == null) {
                            currentCounty.setDown(new CommunityNode(communityName, null, communityData));
                        } 
                        else{
                            currentCommunity = currentCounty.getDown();
                            while (currentCommunity.getNext() != null) {
                                currentCommunity = currentCommunity.getNext();
                            }
                            currentCommunity.setNext(new CommunityNode(communityName, null, communityData));
                        }             
                        return;
                    }
                    currentCounty = currentCounty.getNext();
                }
                return;
            }
            currentState = currentState.getNext();
        }
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {
        // WRITE YOUR CODE HERE

        int count = 0;
        // Traverse the linked list structure layer by layer
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                    // Check if the community has the specified racial demographic percentage or higher
                    Data communityData = currentCommunity.getInfo();
                    double racePercentage = 0.0;
                    switch (race) {
                        case "African American":
                            racePercentage = communityData.getPrcntAfricanAmerican();
                            break;
                        case "Native American":
                            racePercentage = communityData.getPrcntNative();
                            break;
                        case "Asian American":
                            racePercentage = communityData.getPrcntAsian();
                            break;
                        case "White American":
                            racePercentage = communityData.getPrcntWhite();
                            break;
                        case "Hispanic American":
                            racePercentage = communityData.getPrcntHispanic();
                            break;
                    }
                    // Check if the community is disadvantaged and meets the racial demographic percentage threshold
                    if (communityData.getAdvantageStatus().equals("True") && (racePercentage*100) >= (userPrcntage)) {
                        count++;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return count;// replace this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        //WRITE YOUR CODE HERE

        int count = 0;
        // Traverse the linked list structure layer by layer
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                    Data communityData = currentCommunity.getInfo();
                    double racePercentage = 0.0;
                    switch (race) {
                        case "African American":
                            racePercentage = communityData.getPrcntAfricanAmerican();
                            break;
                        case "Native American":
                            racePercentage = communityData.getPrcntNative();
                            break;
                        case "Asian American":
                            racePercentage = communityData.getPrcntAsian();
                            break;
                        case "White American":
                            racePercentage = communityData.getPrcntWhite();
                            break;
                        case "Hispanic American":
                            racePercentage = communityData.getPrcntHispanic();
                            break;
                    }
                    // Check if the community is disadvantaged and meets the racial demographic percentage threshold
                    if (communityData.getAdvantageStatus().equals("False") && (racePercentage*100) >= (userPrcntage)) {
                        count++;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return count;// replace this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        // WRITE YOUR METHOD HERE
        ArrayList<StateNode> statesWithHighPMLevels = new ArrayList<>();

        // Traverse the linked list structure layer by layer
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                    // Check if the pollution level of the community exceeds the threshold
                    if (currentCommunity.getInfo().getPMlevel() >= PMlevel) {
                        // Add the state to the list if it's not already present
                        if (!statesWithHighPMLevels.contains(currentState)) {
                            statesWithHighPMLevels.add(currentState);
                        }
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return statesWithHighPMLevels;// replace this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        // WRITE YOUR METHOD HERE

        int count = 0;

        // Traverse the linked list structure layer by layer
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                    // Check if the chance of flood of the community meets the user percentage or higher
                    if (currentCommunity.getInfo().getChanceOfFlood() >= userPercntage) {
                        count++;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }
    
        return count;// replace this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        //WRITE YOUR METHOD HERE
        ArrayList<CommunityNode> lowestIncomeCommunities = new ArrayList<>();
    
        // Traverse the linked list structure until you reach the specified state
        StateNode currentState = firstState;
        while (currentState != null && !currentState.getName().equals(stateName)) {
            currentState = currentState.getNext();
        }
        
        // If state not found, return empty list
        if (currentState == null) {
            return lowestIncomeCommunities;
        }
        
        // Traverse the linked list connected to that state until you reach the community layer
        CountyNode currentCounty = currentState.getDown();
        while (currentCounty != null) {
            CommunityNode currentCommunity = currentCounty.getDown();
            while (currentCommunity != null) {
                // Check if the list of lowest income communities is less than 10
                if (lowestIncomeCommunities.size() < 10) {
                    lowestIncomeCommunities.add(currentCommunity);
                } else {
                    // Find the lowest income community in the list
                    CommunityNode lowestIncomeCommunity = lowestIncomeCommunities.get(0);
                    for (int i = 1; i < lowestIncomeCommunities.size(); i++) {
                        CommunityNode community = lowestIncomeCommunities.get(i);
                        if (community.getInfo().getPercentPovertyLine() < lowestIncomeCommunity.getInfo().getPercentPovertyLine()) {
                            lowestIncomeCommunity = community;
                        }
                    }
                    // Replace the lowest income community if the current community has a higher income
                    if (currentCommunity.getInfo().getPercentPovertyLine() > lowestIncomeCommunity.getInfo().getPercentPovertyLine()) {
                        lowestIncomeCommunities.set(lowestIncomeCommunities.indexOf(lowestIncomeCommunity), currentCommunity);
                    }
                }
                currentCommunity = currentCommunity.getNext();
            }
            currentCounty = currentCounty.getNext();
        }
        
        return lowestIncomeCommunities;// replace this line
    }
    
}
