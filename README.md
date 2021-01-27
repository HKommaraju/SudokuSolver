# SudokuSolver
A 4x4 sudoku board solver. 
The project can take in any state of a valid 4x4 sudoku board and produce a correct and completely solved board as an answer.

The project has two files namely HillClimber and GameBoard. 
GameBoard is a class containing function defenitions and methods for GameBoard class objects.
HillClimber contains the code for the creation of GameBoard object and subsequent execution.

To run the project, import both files from the src folder into the same folder(This is essential since HillClimber declares objects of GameBoard class and if not present in the same folder will lead to exceptions). Run HillClimber.java file and specify the input file along with the path if the file is not present in the same local folder. This project was developed in eclipse IDE but can be run in any IDE/terminal that supports java projects.

The input for the board has to be in a separate file in the following format:

4\
1 * 3 2 \
3 * 4 *  \
4 * 2 * \
2 3 * * 

The first line contains the n from from the nxn board about to be inputed. The following lines are the state of the board with an '*' denoting an empty spot.
The output is displayed in the same formatting with a complete board on the console. 
