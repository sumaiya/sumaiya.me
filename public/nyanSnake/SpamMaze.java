/*
 * class SpamMaze
 * Sumaiya Hashmi
 * represents and handles the model for the Spampede applet
 */

import java.lang.Math;
import java.util.LinkedList;
import java.util.Random;

class SpamMaze extends Maze
{
  // The data members representing the spam and the centipede
  public LinkedList<MazeCell> spamCells;
  public LinkedList<MazeCell> pedeCells;
  public static final char SPAM = 'D';
  public static final char HEAD = 'H'; //start
  public static final char BODY = 'B'; //pede
  public static final char NORTH = 'N';
  public static final char SOUTH = 'S';
  public static final char EAST = 'E';
  public static final char WEST = 'W';
  public static final char AI = 'A';
  public static final char WALL = '*';
  
  // you will want other data members representing various
  // constants that are important in the Spampede application...
  // 
  // remember to avoid magic numbers (or constants!)




public SpamMaze()
{
	super(); //call base class' zero-arg constr
	spamCells = new LinkedList<MazeCell>();
	pedeCells = new LinkedList<MazeCell>();
//	spamCells.addFirst(maze[10][5]);
//	spamCells.element().contents = SPAM;
	pedeCells.addFirst(maze[1][2]);
	pedeCells.addLast(maze[1][1]);
	MazeCell head = pedeCells.getFirst();
	pedeCells.element().contents = HEAD; //head is first in pedeCells LL
	for(int i=1; i<pedeCells.size(); i++) //body is everything else in pedeCells
	{
		pedeCells.get(i).contents = BODY;
	}
	//this.direction = 'East';

	
}

public int getRows()
{
	return mazeStrings.length;
}

public int getColumns()
{
	return mazeStrings[0].length();
}

public char getContents(int r, int c)
{
	return maze[r][c].contents;
}

public void setContents(int r, int c, char newcontents)
{
	maze[r][c].contents = newcontents;
}

public void addSpam() //add one can spam to environment
{
	Random myGenerator = new Random();
	int newr = myGenerator.nextInt(getRows());
	int newc = myGenerator.nextInt(getColumns());
	while(maze[newr][newc].isWall() || pedeCells.contains(maze[newr][newc]) || spamCells.contains(maze[newr][newc]))
	{
		newr = myGenerator.nextInt(getRows());
		newc = myGenerator.nextInt(getColumns());
	}
	spamCells.addLast(maze[newr][newc]);
	maze[newr][newc].contents = SPAM;
	/*for(int i=1; i<spamCells.size(); i++) 
	{	
		int spamRow = spamCells.get(i).row;
		int spamCol = spamCells.get(i).col;
		spamCells.get(i).contents = SPAM;
		maze[spamRow][spamCol].contents = SPAM;
	}
*/
	
}

public void removeSpam()
{
	if (spamCells.size()>0)
	{
	spamCells.getFirst().contents = ' ';
	spamCells.removeFirst(); //remove oldest spam
	}
}

public int advancePede(char direction)
{
	MazeCell newCell = null;
	int headRow = pedeCells.element().row;
	int headCol = pedeCells.element().col;
	//System.out.println("At top of advance, pede is "+ pedeCells);
//AI: run multiBFS start from head, end at SPAM
	if(direction == AI)
	{
			newCell = this.multiBFS(pedeCells.element(), SPAM);
	
				
		}

// set newCell depending on direction 
	else if(direction == NORTH)
	{
	//	newCell=maze[headRow-1][headCol];
		newCell=maze[headRow][headCol-1];
	}
	else if(direction == SOUTH)
	{
	//	newCell=maze[headRow+1][headCol];
		newCell=maze[headRow][headCol+1];
	}
	else if(direction == EAST)
	{
	//	newCell=maze[headRow][headCol+1];
		newCell=maze[headRow+1][headCol];
	
	}
	else if(direction == WEST)
	{
	//	newCell=maze[headRow][headCol-1];
		newCell=maze[headRow-1][headCol];
	}


	
	//if centipede hits wall or self
	if(newCell.isWall() || pedeCells.contains(newCell))
	{
		// reset pede to its initial state!
		/*for(int i = 0; i<pedeCells.size(); i++)
		{
			pedeCells.get(i).contents = ' ';
		}
		
		pedeCells.clear();
		pedeCells.add(0, maze[1][2]);
		pedeCells.add(1, maze[1][1]);
		pedeCells.element().contents = HEAD; 
		pedeCells.get(1).contents = BODY;
		*/
		//System.out.println("Wall? " + newCell.isWall());
		//System.out.println("Inside Pede? " + pedeCells.contains(newCell));
		//System.out.println("pede is " + pedeCells);
		//System.out.println("spam is " + spamCells);
		return 13;
	
	}
	//if pede doesn't hit spam, tail retracts

	//if pede eats spam
	else if(spamCells.contains(newCell))
	{
	spamCells.remove(newCell); //remove eaten spam	
	pedeCells.element().contents = BODY; //head becomes body
	//System.out.println("pede cells element is" + pedeCells.element());
	pedeCells.addFirst(newCell); //pede advances
	pedeCells.element().contents = HEAD; //front becomes head
	//System.out.println("Pede eats spam, pede is now " + pedeCells);
	return 0; //arbitrary
	}
	
	else 
	{	
		pedeCells.getLast().contents = ' ';
		pedeCells.removeLast();	
		pedeCells.element().contents = BODY; //head becomes body
		pedeCells.addFirst(newCell); //pede advances
		pedeCells.element().contents = HEAD; //front becomes head
		//System.out.println("Pede continues. Spam is " + spamCells);
		return 0; //arbitrary
	}

}

public char reversePede()
{
	//make new list, add reversed old list, rename to replace old
	LinkedList<MazeCell> pedeCellsrev; 
	pedeCellsrev = new LinkedList<MazeCell>();
	if (pedeCells.size()<2)
		{
		return EAST; //arbitrary. 
		}
	else
	{		
		while(pedeCells.size()>0) 
		{
			MazeCell oldElement = pedeCells.getLast(); 
			pedeCellsrev.add(oldElement);
			pedeCells.removeLast();
		//	System.out.println("In the loop!" + " pedeCells is " + pedeCells);
			//System.out.println("rev is " + pedeCellsrev + " pedeCells size: " + pedeCells.size());
		}
	//	System.out.println("below the loop");
	
	pedeCells = pedeCellsrev;
	pedeCells.element().contents = HEAD;
	for(int i=1; i<pedeCells.size(); i++) //body is everything else in pedeCells
	{
		pedeCells.get(i).contents = BODY;
	}
//	System.out.println("pedeCells is now " + pedeCells);

// find position of head relative to second cell to get dir
	int headRow = pedeCells.element().row;
	int headCol = pedeCells.element().col;
	int secondRow = pedeCells.get(1).row;
	int secondCol = pedeCells.get(1).col;
	if(headRow-secondRow == 0) //head and second on same row
	{
//		System.out.println("east or west?");
		if(headCol-secondCol == 1) // head is to right of second
		{ 
	//		System.out.println("should return east");
			return EAST; 
		}
		else // head is not to right of second. but still same row. ergo left.
		{
		//	System.out.println("should return west");
			return WEST;
		}
	}
	else if(headRow-secondRow == 1) // head is below second
	{
	//	System.out.println("should return south");
		return SOUTH;
	}
	else //not same row, not below. so head is above second.
	{
	//	System.out.println("should return north");
		return NORTH;
	}
	}
}





public static void main(String[] args)
{
  SpamMaze SM = new SpamMaze();

  System.out.println("SM is\n" + SM);
 /* MazeCell nextSpot = SM.multiBFS(SM.pedeCells.getFirst(), SPAM);
  System.out.println("nextSpot is\n" + nextSpot);
  System.out.println("SM is\n" + SM);
*/
  SM.advancePede(EAST);
  System.out.println("SM is\n" + SM);
  System.out.println("pedeCells is " + SM.pedeCells);

  SM.advancePede(EAST);
  System.out.println("SM is\n" + SM);
  System.out.println("pedeCells is " + SM.pedeCells);

  SM.advancePede(EAST);
  System.out.println("SM is\n" + SM);
  System.out.println("pedeCells is " + SM.pedeCells);

  //SM.reversePede();
  System.out.println("Reversed to " + SM.reversePede());
  System.out.println("pedeCells is " + SM.pedeCells);
  System.out.println("SM is\n" + SM);
 
  SM.advancePede(SOUTH);
  System.out.println("SM is\n" + SM);
  System.out.println("pedeCells is " + SM.pedeCells);

  SM.advancePede(SOUTH);
  System.out.println("SM is\n" + SM);
  System.out.println("pedeCells is " + SM.pedeCells);


 // SM.reversePede();
  System.out.println("Reversed to " + SM.reversePede());
  System.out.println("pedeCells is " + SM.pedeCells);
  System.out.println("SM is\n" + SM);
 
  
  /* 
  SM.addSpam();
  System.out.println("added. spamCells is " + SM.spamCells);
  System.out.println("SM is\n" + SM);
  
  SM.addSpam();
  System.out.println("added. spamCells is " + SM.spamCells);
  System.out.println("SM is\n" + SM);

  SM.removeSpam();
  System.out.println("added. spamCells is " + SM.spamCells);
  System.out.println("SM is\n" + SM);
*/
}


}