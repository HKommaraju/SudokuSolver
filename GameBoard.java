package defaultPackage;

/**
 * GameBoard: A class to represent the 4 sudoku tile using char arrays and a an integer array to map the empty index to the values  
 *  *
 * @author Harshitha Kommaraju
 * Created on 20/11/2020
 */
public class GameBoard {
	private int blankscount;//the count of the number of black spaces in the input board
	private char[] givenstate;//the entire board vector holding possible solutions
	public int[] blankmap;//the array containing the indexes of the blank spaces
	public int proposed_loc;//the location/index in givenstate that needs to be changed to get the least clashing successor 
	public char proposed_val;//the value to be put in givenstate at propsed_loc to get the least clashing successor 
	private int currentclash;//the current value of the evaluation function on givenstate
	private int successorclash;//the result of evaluation function on the successor state
	public int totaliterations;//the total number of times HillClimbing has run irrespective of restart
	
   
    
    /**
     * Create a GameBoard object
     */
    public GameBoard() {
    	givenstate = new char[16];
    	totaliterations=0;
    }

    /**
     * Make the blank map with the same size as the number of blanks
     * @param blankcount - the number of blanks in the input state
     */
   public void makearrays(int blankcount)
   {
	   blankmap = new int[blankcount];
   }
    
  
   /**
    * Set the value @param index in givenstate to @param value
    * @param value - the value to be set to
    * @param index - the index to be set value to 
    */
    public void setGivenState(char value,int index)
    {
    	 givenstate[index] = value;
    	
    }
    
    /**
     * Return the value of givenstate at particular index
     * @param index - the index whose value in givenstate is to be returned
     * @return the value of givenstate at index
     */
    public char returnstateAt(int index)
    {
    	return givenstate[index];
    }
    
    /**
     * Set the value of blankscount to given parameter
     * @param x - the value blankscount is to be set to
     */
    public void setBlankCount(int x)
    {
    	blankscount = x;
    }
    
    /**
     * returns current clash
     * @return current clash value
     */
    public int currentclash()
    {
    	return currentclash;
    }
    
    /**
     * returns successor clash
     * @return successor clash value
     */
    public int successorclash()
    {
    	return successorclash;
    }
    
    /**
     * returns blank count value
     * @return blankscount value
     */
    public int returnBlankCount()
    {
    	return blankscount;
    }
    
    /**
     * Set the value of current clash to passed in parameter
     * @param x - the value current clash is to be set to
     */
    public void setcurrentclash(int x)
    {
    	currentclash = x;
    }
    
    /**
     * Set the value of successor clash to passed in parameter
     * @param x - the value successor clash is to be set to
     */
    public void setsuccessorclash(int x)
    {
    	successorclash = x;
    }
    
    /**
     * Return the entire character array of givenstate
     * @return the char[] givenstate
     */
    public char[] returnGivenState()
    {
    	return givenstate;
    }
}

