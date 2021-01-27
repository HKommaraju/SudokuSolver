package defaultPackage;

import static org.junit.Assert.assertTrue;

import java.io.*;
import java.util.Random;

/**
 * HillClimber: A program to take in a 4 sudoku board with empty spaces and use the Hill- Climbing algorithm to produce a solution state
 *
 * @author Harshitha Kommaraju
 * Created on 20/11/2020
 * @version 1.1
 *
 */

public class HillClimber {
  

	/**
	 * A function to take in the initial given input and fill in the blanks with random valid values   
	 * @param gb - The gameBoard object holding the state vector and given state board to be filled in
	 * 
	 */
	private static void generateinitial(GameBoard gb) {
		
		//A Random object used to generate random numbers 
		Random rand = new Random();
		
		//Looping through every blank space and filling it with a valid random number
		for(int i=0;i<gb.returnBlankCount();i++)
		{
			int random = rand.nextInt(3)+1;//generate a valid random number between 1 and 4
			char fill='0';
			if(random == 1) {fill = '1';}
			else if(random == 2) {fill = '2';}
			else if(random == 3) {fill = '3';}
			else if(random == 4) {fill = '4';}
			gb.setGivenState(fill, gb.blankmap[i]);//fill the position
		}
	}
	
	/**
	 * testHarness program to take in the filename, sets up the initial state and calls hillClimberLoop till a solution is found
	 * @param filename - the file containing the input
	 * @param gb - The Game board object containing the problem
	 */
	private static void testHarness(String filename,GameBoard gb) {
		
    	BufferedReader myFile;//To read in the input file
    	
    	int size= 0;//to store the size of the sudoku board in the file
    	int blankcounter=0;//to keep track of how many blanks are present in the state vector
    	try {
    	    myFile=new BufferedReader(new FileReader(filename)); 
    	    size = myFile.readLine().charAt(0);
    	    assertTrue(size-48==4);//make sure we are dealing with a 4 sudoku problem
    	    int i=0;//count of array index being read into
    	    
    	    int current = myFile.read();//the current character read
    	    while(current>-1)//while it is a valid char
    	    {
    	    	char cur = (char)current;//cast into character
    	    	if((current>48 && current<53)||cur == '*')//check appropriate char value
    	    	{
    	    		if(cur=='*')
    	    		{
    	    			blankcounter++;//increment blank space if a '*' is found
    	    		}
    	    		
    	    		gb.setGivenState(cur, i);//input the character into the state vector
    	    		i++;//increment counter
    	    	}
    	    	current = myFile.read();//read in the next character
    	    }
    	    
    	} catch (Exception e) {
    		//If there were problems in reading the file
    	    System.err.println("Ooops!  I can't seem to load the file \""+filename+"\", do you have the file in the correct place?");
    	    System.exit(1);
    	}

    	
    	gb.setBlankCount(blankcounter);//Set the blankcounter as calculated
    	gb.makearrays(gb.returnBlankCount());//make the array to hold the indexes of the blanks 


    	//fill the made array with the indexes of the blanks
    	int j=0;
    	for(int i=0;i<16;i++)
    	{
    		if(gb.returnstateAt(i)=='*')
    		{
    			gb.blankmap[j] = i;
    			j++;
    		}
    	}
    	
    	boolean stuck = true;//to keep track off whether the Hill Climbing is stuck at local minima
    	int restart = 1;//to keep track of how many random restarts have been done
    	while(stuck)//until the algorithm is not stuck
    	{
    		System.out.println("Random restart number: "+restart);
    		generateinitial(gb);//generate a random state vector by filling in blanks
    		stuck=HillClimberLoop(gb);//run HillClimbing until it is not stuck
    		restart++;
    	}
    	
    	//print the finished output board
    	System.out.println("The solved 4 sudoku board ");
    	for(int i=0;i<16;i++)
    	{
    		System.out.print(gb.returnstateAt(i));
    		if(i== 3 || i ==7 || i==11)
    				{
    					System.out.print("\n");
    				}
    	}
		
	}
	
	/**
	 * The HillClimbing algorithm that keeps going until either the goal is reached or is stuck
	 * @param gb - the game board containing the problem
	 * @return a boolean value representing whether or not it is stuck. It returns true when stuck.
	 */
	private static boolean HillClimberLoop(GameBoard gb) {
		
		boolean atGoal=false;//boolean to see if it is at goal
		boolean stuck=false;//boolean to see if it is stuck
		int iterationcount=0;//keeps track of the number of iterations in current cycle
		
		atGoal = goalTest(gb.returnGivenState());//checks whether or not the current state of the board is at goal
		while(!atGoal && !stuck)//as long as it is not in the goal state and is not stuck
		{
			//iterate and display iteration counters
			gb.totaliterations++;
			System.out.println("Iteration number for entire algorithm: "+gb.totaliterations);
			iterationcount++;
			System.out.println("Iteration number for current start state: "+iterationcount);
		
			//call the successor function which returns the least clashing next successor state
			//The least clashing successor state is loaded into the GameBoard
			Successor(gb);
		
			//if there is a lower clash
			if(gb.successorclash()<gb.currentclash())
			{
				gb.setGivenState(gb.proposed_val, gb.proposed_loc);//the game board is changed to the proposed successor 
			
				System.out.println("Evaluation function result: "+gb.successorclash());
			
				for(int i=0;i<gb.returnBlankCount();i++)//the proposed state vector is printed
				{
					System.out.print(gb.returnstateAt(gb.blankmap[i])+" ");
				}
				System.out.println("\n");
				gb.setcurrentclash(gb.successorclash());//the current clash is updated
				atGoal = goalTest(gb.returnGivenState());//this new state is checked for goal state
			}
		
			//if there is no lower clash
			else if(gb.successorclash()>=gb.currentclash())
			{
				if(!goalTest(gb.returnGivenState()))
				{
					stuck = true;//made true to stop the loop
				}
			}
		}
	
	return stuck;
	}

	/**
	 * To check if the state vector passed in is at goal state
	 * @param state - the character array containing the proposed state
	 * @return true if at goal state and false if not
	 */
    private static boolean goalTest(char[] state) {
		
    	boolean notfoundclash=true;//keeps track of clashes
    	
    	//check if there are any clashes in the 4 rows
    	for(int i=0;i<=12 && notfoundclash;i=i+4)
    	{
    		if(state[i]==state[i+1] || state[i]==state[i+2] || state[i]==state[i+3] 
    				|| state[i+1]==state[i+2] || state[i+1]==state[i+3] || state[i+2]==state[i+3])
    		{
    			notfoundclash=false;
    		}
    	}
    	
    	//check if there are no clashes in the columns
    	if(notfoundclash)
    	{
    		for(int i=0;i<=3 && notfoundclash;i++)
        	{
        		if(state[i]==state[i+4] || state[i]==state[i+8] || state[i]==state[i+12] 
        				|| state[i+4]==state[i+8] || state[i+4]==state[i+12] || state[i+8]==state[i+12])
        		{
        			notfoundclash=false;
        		}
        	}
    	}
    	
    	//check if there are no clashes in the 4 boxes
    	if(notfoundclash)
    	{
    		int[] starters= new int[4];
    		starters[0]=0;starters[1]=2;starters[2]=8;starters[3]=10;
    		int i;
    		for(int j=0;j<=3 && notfoundclash;j++)
        	{
    			i=starters[j];
        		if(state[i]==state[i+1] || state[i]==state[i+4] || state[i]==state[i+5] 
        				|| state[i+1]==state[i+4] || state[i+1]==state[i+5] || state[i+4]==state[i+5])
        		{
        			notfoundclash=false;
        		}
        	}
    	}
    	
    	//return the boolean 
		return notfoundclash;
	}

    /**
     * The successor function which evaluates all successor states of the state given in gb and finds the least clashing vector
     * @param gb - The game board containing the state vector and the proposed location and proposed value variables which will be 
     * updated by the function
     */
	private static void Successor(GameBoard gb) {
		
		int leastclash = Evaluateclashes(gb);//evaluate the current clash value of the vector and set it as the least
		gb.setcurrentclash(leastclash);//make that the current clash value of the board
		int newclash=0;//the variable to hold the clash value of the successor
		
         for(int i=0;i<gb.returnBlankCount();i++)//for every single blank spot in the vector
         {
        	 char existing = gb.returnstateAt(gb.blankmap[i]);//get the existing value
        	 int exist = (int)existing-48;
        	 for(int j=1;j<5;j++)//each black can be one of three values. Anything between 1 to 4 excluding its current value
        	 {
        		 char val='4';
				 if(j==1) {val='1';}
				 else if(j==2) {val = '2';}
				 else if(j==3) {val ='3';}
        		 if(exist != j)//when it is something not already present
        		 {
        			 gb.setGivenState(val, gb.blankmap[i]);//change the state vector to include that value
        			 newclash = Evaluateclashes(gb);//calculate the clash value for this state
        			 if(newclash<leastclash)//if this clash value is lesser than the least clash
        			 {
        				 gb.proposed_loc = gb.blankmap[i];//load the proposed values into the gameBoard
        				 gb.proposed_val = val;
        				 leastclash = newclash;//change leastclash to be newclash
        			 }
        			 gb.setGivenState(existing, gb.blankmap[i]);//revert the state vector back to the original
        		 }
        	 }
         }
         
        gb.setsuccessorclash(leastclash);//the last value of leastclash is the least clsah found between all successors 
	}

	/**
	 * Returns the number of clashes in the current state vector in the game board object
	 * @param gb - The game Board containing the state vector
	 * @return the number of clashes found in the state vector
	 */
	private static int Evaluateclashes(GameBoard gb) {
		
		int clashes=0;//the total number of clashes
		int rownum=0;//the row of the current index being studies
		int start=0;//the start and end index of that row
		int end=0;
		int cube = 0;//will reflect if this index should look right or left in respect to the cubes/boxes.
					 //cube value of 1 means look to the right and -1 means look to the left.
		boolean lookup = false;//will reflect if this index needs to lookup or lookdown with regard to its cube
		
		for(int i=0;i<16;i++)
		{
			rownum = i/4;//divide by 4 and set the row num
			if(rownum == 0)
			{
				//set the appropriate values of cube,lookup,start and end
				if(i==0 || i == 2) {cube =1;}
				else {cube = -1;}
				start =0; end =3; lookup = false;
			}
			
			else if(rownum == 1)
			{
				//set the appropriate values of cube,lookup,start and end
				if(i==4 || i == 6) {cube =1;}
				else {cube = -1;}
				start =4; end =7; lookup = true;
			}
			
			else if(rownum == 2)
			{
				//set the appropriate values of cube,lookup,start and end
				if(i==8 || i == 10) {cube =1;}
				else {cube = -1;}
				start =8; end =11; lookup = false;
			}
			
			else if(rownum == 3)
			{
				//set the appropriate values of cube,lookup,start and end
				if(i==12 || i == 14) {cube =1;}
				else {cube = -1;}
				start =12; end =15; lookup = true;
			}
			
			//calculate clashes in trailing row values
			for(int j=1; i+j<=end;j++)
			{
				if(gb.returnstateAt(i) == gb.returnstateAt(i+j))
				{
					clashes++;
					if(j==1 && cube == 1) 
					{
						//if this index is also in its cube, add an additional clash
						clashes++;
					}
				}
			}
			
			//calculate clashes in leading row values
			for(int j=1; i-j>=start;j++)
			{
				if(gb.returnstateAt(i) == gb.returnstateAt(i-j))
				{
					clashes++;
					if(j==1 && cube == -1) 
					{
						//if this index is also in its cube, add an additional clash
						clashes++;
					}
				}
			}
				
			 //calculate clashes in trailing column values
			for(int j=1; i+4*j<16;j++)
			{
				if(gb.returnstateAt(i) == gb.returnstateAt(i+4*j))
				{
					clashes++;
					if(j==1 && !lookup) 
					{
						//if this index is also in its cube, add an additional clash
						clashes++;
					}
				}
			}
			
			//calculate clashes in leading column values
			for(int j=1; i-4*j>=0;j++)
			{
				if(gb.returnstateAt(i) == gb.returnstateAt(i-4*j))
				{
					clashes++;
					if(j==1 && lookup) 
					{
						//if this index is also in its cube, add an additional clash
						clashes++;
					}
				}
			}
				
			//checking the appropriate diagonal value in the cube
			int adjuster=0;//the number to be added to find the index to be checked diagonally
			if(cube == 1 && !lookup)//right and down
			{adjuster = 5;}
			
			else if(cube == -1 && !lookup)//left and down
			{adjuster = 3;}
				
			else if(cube == 1 && lookup)//right and up
			{adjuster = -3;}
			
			else if(cube == -1 && lookup)//left and up
			{adjuster = -5;}
				
			//check for clashes in that diagonal index
			if(gb.returnstateAt(i) == gb.returnstateAt(i+adjuster))
			{
				clashes++;
			}
		}
		
		//return the total number of clashes
		return clashes;
	}
	
    public static void main(String args[]) {
	
    	//make the new GameBoard object
    	GameBoard gb = new GameBoard();
    	//Set the filename from command line arguments
    	String filename = args[0];
    	//call the testHarness function
    	testHarness(filename,gb);
    }	

}
