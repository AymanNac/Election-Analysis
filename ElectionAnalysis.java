package election;


/* 
 * Election Analysis class which parses past election data for the house/senate
 * in csv format, and implements methods which can return information about candidates
 * and nationwide election results. 
 * 
 * It stores the election data by year, state, then election using nested linked structures.
 * 
 * The years field is a Singly linked list of YearNodes.
 * 
 * Each YearNode has a states Circularly linked list of StateNodes
 * 
 * Each StateNode has its own singly linked list of ElectionNodes, which are elections
 * that occured in that state, in that year.
 * 
 * This structure allows information about elections to be stored, by year and state.
 * 
 * @author Colin Sullivan
 */
public class ElectionAnalysis {

    // Reference to the front of the Years SLL
    private YearNode years;

    public YearNode years() {
        return years;
    }

    /*
     * Read through the lines in the given elections CSV file
     * 
     * Loop Though lines with StdIn.hasNextLine()
     * 
     * Split each line with:
     * String[] split = StdIn.readLine().split(",");
     * Then access the Year Name with split[4]
     * 
     * For each year you read, search the years Linked List
     * -If it is null, insert a new YearNode with the read year
     * -If you find the target year, skip (since it's already inserted)
     * 
     * If you don't find the read year:
     * -Insert a new YearNode at the end of the years list with the corresponding year.
     * 
     * @param file String filename to parse, in csv format.
     */
    public void readYears(String file) {
		// WRITE YOUR CODE HERE
        StdIn.setFile(file);
        while(StdIn.hasNextLine()){
          String[] split = StdIn.readLine().split(",");

          int currentyear = Integer.parseInt(split[4]);
          while(years == null){
           years = new YearNode(currentyear);

            }
            if(years != null){
              YearNode ptr = years;
              boolean exist =false;
              while (ptr != null) {
                if(ptr.getYear()== currentyear){
                  exist = true;
                  break;
                }
                if(ptr.getNext() == null){
                  break;
                }
                ptr = ptr.getNext();
              }
              if(!exist){
                YearNode newNode = new YearNode(currentyear);
                ptr.setNext(newNode);
          
              }


            }
          }
        }
        
  
    

    /*
     * Read through the lines in the given elections CSV file
     * 
     * Loop Though lines with StdIn.hasNextLine()
     * 
     * Split each line with:
     * String[] split = StdIn.readLine().split(",");
     * Then access the State Name with split[1] and the year with split[4]
     * 
     * For each line you read, search the years Linked List for the given year.
     * 
     * In that year, search the states list. If the target state exists, continue
     * onto the next csv line. Else, insert a new state node at the END of that year's
     * states list (aka that years "states" reference will now point to that new node).
     * Remember the states list is circularly linked.
     * 
     * @param file String filename to parse, in csv format.
     */
    public void readStates(String file) {
		// WRITE YOUR CODE HERE
        StdIn.setFile(file);

        while (StdIn.hasNextLine()) {
            String[] split = StdIn.readLine().split(",");
            int year = Integer.parseInt(split[4]);
            String state = split[1];
    
            YearNode curr = years;
            while (curr != null && curr.getYear() != year) {
                curr = curr.getNext();
            }
    
            if (curr != null) {
                StateNode stateHead = curr.getStates();
                if (stateHead == null) {
                    StateNode newState = new StateNode(state, null);
                    curr.setStates(newState);
                    newState.setNext(newState);  
                } else {
                    StateNode temp = stateHead;
                    boolean found = false;
    
                    do {
                        if (temp.getStateName().equals(state)) {
                            found = true;
                            break;
                        }
                        temp = temp.getNext();
                    } while (temp != stateHead);
    
                    if (!found) {
                        StateNode newState = new StateNode(state, curr.getStates().getNext());
                      curr.getStates().setNext(newState);
                      curr.setStates(newState);
                       
                    }
                }
            }
        }
  }




        
    

    /*
     * Read in Elections from a given CSV file, and insert them in the
     * correct states list, inside the correct year node.
     * 
     * Each election has a unique ID, so multiple people (lines) can be inserted
     * into the same ElectionNode in a single year & state.
     * 
     * Before we insert the candidate, we should check that they dont exist already.
     * If they do exist, instead modify their information new data.
     * 
     * The ElectionNode class contains addCandidate() and modifyCandidate() methods for you to use.
     * 
     * @param file String filename of CSV to read from
     */
      public void readElections(String file) {
		  // WRITE YOUR CODE HERE

          StdIn.setFile(file);

          while (StdIn.hasNextLine()) {
              String line = StdIn.readLine();
              String[] split = line.split(",");
      
              int raceID = Integer.parseInt(split[0]);
              String stateName = split[1];
              int officeID = Integer.parseInt(split[2]);
              boolean senate = split[3].equals("U.S. Senate");
              int year = Integer.parseInt(split[4]);
              String canName = split[5];
              String party = split[6];
              int votes = Integer.parseInt(split[7]);
              boolean winner = split[8].toLowerCase().equals("true");
    
              YearNode curr = years;
              while (curr != null && curr.getYear() != year) {
                  curr = curr.getNext();
              }
              if (curr != null) {
                  StateNode statePtr = curr.getStates();
                  boolean found = false;
                  StateNode front = statePtr;
      
                  if (statePtr != null) {
                      do {
                          if (statePtr.getStateName().equals(stateName)) {
                              found = true;
                              break;
                          }
                          statePtr = statePtr.getNext();
                      } while (statePtr != front);
                  }
      
                  if (found) {
                      ElectionNode election = statePtr.getElections();
      
                      if (election == null) {
                          election = new ElectionNode();
                          election.setRaceID(raceID);
                          election.setoOfficeID(officeID);
                          election.setSenate(senate);
                          statePtr.setElections(election);
                      } else {
                          ElectionNode prev = null;
                          while (election != null && election.getRaceID() != raceID) {
                              prev = election;
                              election = election.getNext();
                          }
                          if (election == null) {
                              election = new ElectionNode();
                              election.setRaceID(raceID);
                              election.setoOfficeID(officeID);
                              election.setSenate(senate);
                              if (prev != null) {
                                  prev.setNext(election);  
                              }
                          }
                      }
                      if (!election.isCandidate(canName)) {
                          election.addCandidate(canName, votes, party, winner);
                      } else {
                          election.modifyCandidate(canName, votes, party);
                      }
                  }
              }
          }
          return;
        
    }

    /*
     * DO NOT EDIT
     * 
     * Calls the next method to get the difference in voter turnout between two
     * years
     * 
     * @param int firstYear First year to track
     * 
     * @param int secondYear Second year to track
     * 
     * @param String state State name to track elections in
     * 
     * @return int Change in voter turnout between two years in that state
     */
    public int changeInTurnout(int firstYear, int secondYear, String state) {
        // DO NOT EDIT
        int last = totalVotes(firstYear, state);
        int first = totalVotes(secondYear, state);
        return last - first;
    }

    /*
     * Given a state name, find the total number of votes cast
     * in all elections in that state in the given year and return that number
     * 
     * If no elections occured in that state in that year, return 0
     * 
     * Use the ElectionNode method getVotes() to get the total votes for any single
     * election
     * 
     * @param year The year to track votes in
     * 
     * @param stateName The state to track votes for
     * 
     * @return avg number of votes this state in this year
     */
    public int totalVotes(int year, String stateName) {
      	// WRITE YOUR CODE HERE
          int total = 0;
          YearNode curr = years;
      
          while (curr != null) {
              if (curr.getYear() == year) {
                  StateNode statePtr = curr.getStates();
                  do {
                      if (statePtr.getStateName().equals(stateName)) {
                          ElectionNode election = statePtr.getElections();
                          while (election != null) {
                              total += election.getVotes();
                              election = election.getNext();
                          }
                          break;
                      }
                      statePtr = statePtr.getNext();
                  } while (statePtr != curr.getStates());
                  break;
              }
              curr = curr.getNext();
          }
          return total;
      }

    /*
     * Given a state name and a year, find the average number of votes in that
     * state's elections in the given year
     * 
     * @param year The year to track votes in
     * 
     * @param stateName The state to track votes for
     * 
     * @return avg number of votes this state in this year
     */
    public int averageVotes(int year, String stateName) {
      	// WRITE YOUR CODE HERE
        int totalVotes = 0;
        int electionCount = 0;
        YearNode currentYear = years;
    
        while (currentYear != null) {
            if (currentYear.getYear() == year) {
                StateNode statePtr = currentYear.getStates();
                if(statePtr != null){
                    StateNode start = statePtr;
                    boolean first = true;
                    while (first || statePtr != start) {
                        first = false;
                        if(statePtr.getStateName().equals(stateName)){
                            ElectionNode election = statePtr.getElections();
                            while(election != null){
                            totalVotes += election.getVotes();
                            electionCount++;
                            election = election.getNext();

                            }
                            break;
                        }
                        statePtr = statePtr.getNext();
                        
                    }
                }
                break;
            }
            currentYear = currentYear.getNext();
            }
            return (electionCount == 0) ? 0 : totalVotes / electionCount;
        }
    
    /*
     * Given a candidate name, return the party they FIRST ran with
     * 
     * Search each year node for elections with the given candidate
     * name. Update that party each time you see the candidates name and
     * return the party they FIRST ran with
     * 
     * @param candidateName name to find
     * 
     * @return String party abbreviation
     */
    public String candidatesParty(String candidateName) {
		  // WRITE YOUR CODE HERE
          YearNode curr = years;

    while (curr != null) {
        StateNode statePtr = curr.getStates();

        if (statePtr != null) {
            StateNode start = statePtr;
            boolean first= true;

            while (first || statePtr != start) {
                first = false;
                ElectionNode election = statePtr.getElections();
                while (election != null) {
                    if (election.isCandidate(candidateName)) {
                        return election.getParty(candidateName);
                    }
                    election = election.getNext();
                }

                statePtr = statePtr.getNext();
            }
        }

        curr = curr.getNext();
    }
    return null;
    }
      
         
          
    }