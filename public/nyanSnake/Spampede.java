//  Spampede.java
//  Written by: Sumaiya Hashmi
//  Date:
/* NOTE: Keypresses to use: up,down,left,right arrows to move. 
   Control to reverse. Space to start. Enter to pause */

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import Maze.MazeCell;

import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Spampede extends JApplet
  implements ActionListener, KeyListener, Runnable
{

  Image image;                // off-screen buffer
  Graphics g;                 // that buffer's graphical tools

  int sleepTime = 50;         // 50 milliseconds between updates
  int cycleNum;               // number of update cycles so far
  int topScore;
  private SpamMaze themaze;   // the model for our Spampede game
  private char dir;           // the direction we're moving now...
  String message;             // A String that will be printed on screen

  // DEFINE CONSTANTS FOR YOUR PROGRAM HERE TO AVOID MAGIC VALUES!
  public static final int STRINGX = 15;
  public static final int STRINGY = 25;
  public static final int GAMEBOARDHEIGHT = 490; 
                               // Recommended values: 
                               // 490 with both menu bar and buttons
                               // 525 with only the menu bar
                               // 515 with only buttons
  public static final Color BGCOLOR = Color.white;

  // BELOW ARE DEFINITIONS OF BUTTONS AND MENU ITEMS WHICH WILL APPEAR
  private JButton newGameButton;
  private JButton pauseButton;
  private JButton startButton;

  private JMenu gameMenu;
  private JMenuItem newGameItem;
  private JMenuItem pauseItem;
  private JMenuItem startItem;

  // Here are other data members you might like to use (optional)...
  private AudioClip audioSpam;    // This is for playing a sound
  private AudioClip audioCrunch;  // This is for playing a sound
  private AudioClip audioNyan;
  private Image imageNyan;
  private Image     imageSpam;    // This is for loading an image

  private Color     currentColor; // This is for the big square


  // Initialize the applet.  This is called once when the applet starts.
  public void init()
  {

    // set up the maze here
    this.themaze = new SpamMaze();
    this.dir = SpamMaze.AI;
    // you may want other game-based set up to be in reset(), so that it 
    //   will be redone each time the spampede crashes...

    this.addKeyListener(this);                // listen for key events
    this.setLayout(new BorderLayout());       //set up layout on the form

    // BEGINNING OF BUTTON CODE

    // Add a panel for buttons
    JPanel buttonPane = new JPanel(new FlowLayout());
    buttonPane.setBackground(BGCOLOR);
    add(buttonPane, BorderLayout.PAGE_START);


    newGameButton = new JButton("New Game"); // the text in the button
    newGameButton.addActionListener(this);   // watch for button presses
    newGameButton.addKeyListener(this);      // listen for key presses here
    buttonPane.add(newGameButton);           // add button to the panel

    pauseButton = new JButton("Pause");      // a second button
    pauseButton.addActionListener(this);
    pauseButton.addKeyListener(this);
    buttonPane.add(pauseButton);

    startButton = new JButton("Start");      // a third button
    startButton.addActionListener(this);
    startButton.addKeyListener(this);
    buttonPane.add(startButton);
    // END OF BUTTON CODE

    // BEGINNING OF MENU BAR CODE

    // Set up the menu bar
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);

    // Add a menu to contain items
    gameMenu = new JMenu("Game");            // The menu name
    menuBar.add(gameMenu);                   // Add the menu to menu bar
    
    newGameItem = new JMenuItem("New Game"); // the text in the menu item
    newGameItem.addActionListener(this);     // Watch for button presses
    newGameItem.addKeyListener(this);        // Listen for key presses here
    gameMenu.add(newGameItem);               // Add the item to the menu

    pauseItem = new JMenuItem("Pause");      // A second menu item
    pauseItem.addActionListener(this);
    pauseItem.addKeyListener(this);
    gameMenu.add(pauseItem);

    startItem = new JMenuItem("Start");      // A third menu item
    startItem.addActionListener(this);
    startItem.addKeyListener(this);
    gameMenu.add(startItem);

    // END OF MENU BAR CODE

    // If you choose not to use either menus or buttons, comment out the
    // related code, and adjust GAMEBOARDHEIGHT to account for the 
    // increased amount of space available to the game board

    // Sets up the back (off-screen) buffer for drawing, named image
    image = createImage(getSize().width, GAMEBOARDHEIGHT);
    g = image.getGraphics();                 // g holds drawing routines
    this.clear();                            // clears the screen
//    this.reset();                            // Set up the game internals!

    //add a central panel which holds the buffer (the game board)
    add(new ImagePanel(image), BorderLayout.CENTER);

    // This is an example of loading in an image and a sound file.
    // You can play with this if you like, but it's not required.
    // So, you can also just comment it out (perhaps until later...)
    try
    {
      URL url = getCodeBase();
      this.audioSpam = getAudioClip(url,"Spam.au");
      this.audioCrunch = getAudioClip(url,"crunch.au");
      this.audioNyan = getAudioClip(url,"nyan.wav");
      this.imageSpam = getImage(url,"spam.gif");
      this.imageNyan = getImage(url,"nyan.jpg");

      System.out.println("successful loading of audio/images!");
    }
    catch (Exception e)
    {
      System.out.println("problem loading audio/images!");
      this.audioSpam = null;
      this.audioCrunch = null;
      this.audioNyan = null;
      this.imageSpam = null;
      this.imageNyan = null;
    }

    this.drawEnvironment(); // update our offscreen image buffer
    repaint();              // tell Java to update the screen
  }

  // Each time you start a new game, you will want to reset the
  // internal representation of the game.  Here's a good place to do it!
  // Remember, the applet will be initialized just once, but you may
  // play the game many times within that run of the applet!
  void reset()
  {
	//  System.out.println("Resetting!");
	  this.themaze = new SpamMaze();
	  threadSuspended = true;
	  dir = SpamMaze.AI;
	  
	/*  themaze.spamCells.clear();
	  themaze.pedeCells.clear();
	 */
    // you should update this string each time you try
    // the applet in a browser, so that you know it has
    // leaded the correct one!!
    message = "Hit Space to begin!";
    currentColor = Color.pink;   // initialize data members
    if (audioNyan != null)        // Example of playing an audioclip
      audioNyan.play();
  }

  // This is where you will draw your 2D array of colored squares
  // Notice that all drawing occurs in the off-screen buffer "image".
  //   and that the drawing commands themselves are held in the Graphics g
  // Later, repaint() will copy the image to the screen.
  void drawEnvironment()
  {
    this.clear();                  // first, clear out our image buffer
    
    for(int r=0; r<Maze.mazeStrings.length;r++)
    {
    	for(int c=0; c<Maze.mazeStrings[0].length(); c++) //C++ !!
    	{
    		if (themaze.maze[r][c].contents == '*') //walls
    		{
    				g.setColor(Color.lightGray);
    				g.fillRect(10*r,10*c,10,10);
    		}
    		
    		else if (themaze.maze[r][c].contents == 'H') //head
    		{
    				g.setColor(Color.pink);
    				g.fillOval(10*r,10*c,10,10);
    		}
    		else if (themaze.maze[r][c].contents == 'B') //body
    		{
    				g.setColor(Color.blue);
    				g.fillRect(10*r,10*c,10,10);
    				
    		}
    		else if (themaze.maze[r][c].contents == 'D') //spam
    		{
    				g.setColor(Color.pink);
    				g.fillRect(10*r,10*c,10,10);
    		}
    		else //if (themaze.maze[r][c].contents == ' ') //empty space
    		{
    				g.setColor(Color.black);
    				g.fillRect(10*r,10*c,10,10);
    		}
    	}
    }
    for(int i=1; i<themaze.pedeCells.size(); i++)
    {
    	int n = 7;
    	int r = themaze.pedeCells.get(i).row;
    	int c = themaze.pedeCells.get(i).col;
    	if ((i+n)%n == 1)
    	{
    		g.setColor(Color.red);
    		g.fillRect(10*r,10*c,10,10);
    	}
    	else if ((i+n)%n == 2)
    	{
		g.setColor(Color.orange);
		g.fillRect(10*r,10*c,10,10);
    	}
    	else if ((i+n)%n == 3)
    	{
		g.setColor(Color.yellow);
		g.fillRect(10*r,10*c,10,10);
    	}
    	else if ((i+n)%n == 4)
    	{
		g.setColor(Color.green);
		g.fillRect(10*r,10*c,10,10);
    	}
    	else if ((i+n)%n == 5)
    	{
		g.setColor(Color.cyan);
		g.fillRect(10*r,10*c,10,10);
    	}
    	else if ((i+n)%n == 6)
    	{
		g.setColor(Color.blue);
		g.fillRect(10*r,10*c,10,10);
    	}
    	else
    	{
		g.setColor(Color.magenta);
		g.fillRect(10*r,10*c,10,10);
    	}
    }
  
    


    
    
   /* g.setColor(Color.red);         // ACK! Magic Numbers!
    g.fillRect(100,100,10,10);

    g.setColor(currentColor);
    g.fillRect(150,100,142,142);

    // this draws an image which we've loaded in, if we have one to draw
    */if (this.imageNyan != null)
      g.drawImage(imageNyan,300,10,null);
/*
    // You won't need to have this kind of if statement in your
    //   code, but it shows you how you could use cycleNum, 
    //   if you wanted to...
    if (this.cycleNum % 42 == 0)
      currentColor = Color.magenta;
  
*/
  }
  // You might use this method to move the centipede one square
  // Also, this method can check if the centipede runs
  // into a can of spam, a wall, itself, etc. and act appropriately.

  
  void updateCentipede()
  {
	  if (this.cycleNum % 2 == 0)
	  {  
	  themaze.advancePede(dir);
	  if (themaze.advancePede(dir) == 13)
	  {
		  reset();
	  }
	  }
  }
   
  

  // You might use this method to add/delete spam cans periodically
  void updateSpam()
  {
	    /*if (this.cycleNum % 5 == 0)
	    {
	    	themaze.removeSpam();
	    }

	    else */if (this.cycleNum % 25 == 0)
	    {
	    	themaze.addSpam();
	    }
  }

  // You might use this method to draw a status String on the screen
  void displayMessage()
  {
	String message2 = "\nPoptarts¨ eaten: " +  (themaze.pedeCells.size() - 2) + " | ";  
    g.setColor(Color.white);
    g.drawString(message2 + this.message, STRINGX, STRINGY);
	String messageScore = "Last score: " + this.topScore + " Poptarts¨ eaten!";  
    g.setColor(Color.darkGray);
    g.drawString(messageScore, STRINGX + 400, STRINGY + 350);

  }

  // Things you want to happen at each update step
  // should be placed in this method. It's called from run().
  void cycle()
  {
    this.updateCentipede();  // update the Spampede deque
    this.updateSpam();       // update the Spam deque
    this.drawEnvironment();  // draw things to buffer
    this.displayMessage();   // display messages
    repaint();               // send buffer to the screen
    this.cycleNum++;         // One cycle just elapsed
    this.topScore = themaze.pedeCells.size() - 2; //keep track of spam eaten
  }

  // Here is how buttons and menu items work...
  public void actionPerformed(ActionEvent evt)
  {
    Object source = evt.getSource();

    if (source == newGameButton || source == newGameItem)  
      this.reset();

    if (source == pauseButton || source == pauseItem)  
      this.pause();

    if (source == startButton || source == startItem)  
      this.go();

    this.requestFocus(); // make sure the Applet keeps kbd focus
  }

  // Here's how keyboard events are handled...
  public void keyPressed(KeyEvent evt)
  {
    int ch = evt.getKeyCode(); // method saying what key was pressed

    switch(ch)  // Do different things depending on the character
    {
     /* case ' ':   // magic values!
        message = "You pressed a space...";
        currentColor = Color.white;
        break;
        */
      case KeyEvent.VK_UP: 
        message = "Going up!";
        dir = SpamMaze.NORTH;
        break;
      case KeyEvent.VK_DOWN: 
        message = "Downward!";
        dir = SpamMaze.SOUTH;
        break;
      case KeyEvent.VK_RIGHT: 
    	message = "Heading right!";
        dir = SpamMaze.EAST;
        break;
      case KeyEvent.VK_LEFT: 
        message = "To the left!";
        dir = SpamMaze.WEST;
        break;
      case KeyEvent.VK_CONTROL:
        message = "Reverse!";
        dir = this.themaze.reversePede();
        break;
      case KeyEvent.VK_SPACE:
          message = "Ready. Set. Go!";
          this.go();
          break;
      case KeyEvent.VK_ENTER:
          message = "Paused.";
          this.pause();
          break;
      case KeyEvent.VK_1:
          message = "New Game! Press Space to start.";
          this.reset();
          break;
    /*  case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      */
      case KeyEvent.VK_ALT:
        message = "You pressed Alt. AI";
        dir = SpamMaze.AI;
        //  currentColor = Color.blue;
        break;
   /*   case 'S':
        if (audioCrunch != null)    // Example of playing a sound
          audioCrunch.play();
     */
      default:
        message = "You pressed a " + ch + "...";
        currentColor = Color.cyan;
    }
  }

  // NO NEED TO CHANGE ANYTHING AFTER THIS!

  public void keyReleased(KeyEvent evt) {
      // We don't care if use *stops* pressing a key.
      // So, we do nothing when it happens.
      return;
  }
  public void keyTyped(KeyEvent evt) {
      // another keyboard-related event we don't care about
      return;
  }

  /*
   * A handy method to clear the applet's drawing area
   */
  void clear()
  {
    g.setColor(BGCOLOR);
    g.fillRect(0, 0, getSize().width, getSize().height);
    g.setColor(Color.blue);
    g.drawRect(0, 0, getSize().width-1, GAMEBOARDHEIGHT-1);
  }

  /*
   * The following methods and data members are used
   *   to implement the Runnable interface and to
   *   support pausing and resuming the applet.
   *
   */
  Thread thread;           // the thread controlling the updates
  boolean threadSuspended; // whether or not the thread is suspended
  boolean running;         // whether or not the thread is stopped

  /*
   * This is the method that calls the "cycle()"
   * method every so often (every sleepTime milliseconds).
   */
  public void run()
  {
    while (running) {
      try {
        if (thread != null) {
          thread.sleep(sleepTime);
          synchronized(this) {
            while (threadSuspended)
              wait(); // sleeps until notify() wakes it up
          }
        }
      }
      catch (InterruptedException e) { ; }

      cycle();  // this represents 1 update cycle for the environment
    }
    thread = null;
  }

  /* This is the method attached to the "Start" button
   */
  public synchronized void go()
  {
/*	  g.setColor(Color.yellow);
	  g.drawString("in the go loop", STRINGX, STRINGY);
	*/
	if (audioNyan != null)        // Example of playing an audioclip
	        audioNyan.play();
    if (thread == null)  {
      thread = new Thread(this);
      running = true;
      thread.start();
      threadSuspended = false;
    } else {
      threadSuspended = false;
    }
    notify(); // wakes up the call to wait(), above
  }

  /*
   * This is the method attached to the "Pause" button
   */
  void pause()
  {
    if (thread == null)
      ;
    else
      threadSuspended = true;
  }

  /*
   * This is a method called when you leave the page
   *   that contains the applet. It stops the thread altogether.
   */
  public synchronized void stop()
  {
    running = false;
    notify();
  }

  /* This is the end of the Spampede class */
}
