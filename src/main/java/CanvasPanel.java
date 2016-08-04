import javax.swing.*;
import java.awt.*;

/**
* This class is the panel on which the board appears on the screen
* @see javax.swing.JPanel
*/

public class CanvasPanel extends JPanel
{
	static CanvasPanel THE_BOARD;
	BoardListener boardHandler;
	BoardChecker checker;

	Image originalBoard,blankImage;
	Graphics boardGraphics;
	PlayingCanvasPane parentPane;
	int numGroups = 0;
	final int boardWidth = 20;
	final int boardHeight = 10;
	int whichTile = -1;
	// virtual tile array of tiles on board.
	int[][] currentPlaced = new int[boardHeight][boardWidth];
	int[][] previousPlaced = new int[boardHeight][boardWidth];
	String pathSep;

	/**
	* CanvasPanel Constructor
	* @param parent the parent panel the CanvasPanel is to be associated
	*/
	public CanvasPanel(PlayingCanvasPane parent)
	{
		super();
		parentPane = parent;
		setLayout(new FlowLayout());
		setBackground(new Color(6, 152, 6));
		setPreferredSize(new Dimension(boardWidth*45,boardHeight*60));
		setVisible(true);

		boardHandler = new BoardListener(this);
		addMouseMotionListener(boardHandler);
		addMouseListener(boardHandler);

		pathSep = System.getProperty("file.separator");

		if(pathSep.equals("\\"))
		{
			pathSep = "\\\\";
		}

		blankImage = (new ImageIcon("pics" + pathSep + "blank.gif")).getImage();

		for(int i=0; i<boardHeight; i++)
		{
			for(int j=0; j<boardWidth; j++)
			{
				currentPlaced[i][j] = -1;
				previousPlaced[i][j] = -1;
			}
		}

		THE_BOARD = this;
		checker = new BoardChecker(boardHeight, boardWidth);
	}

	/**
	* @return a 2-dimensional array of integers representing the tiles on the canvas
	*/
	public static int[][] getBoard()
	{
		return THE_BOARD.getInternalBoard();
	}


	private int[][] getInternalBoard()
	{
		return currentPlaced;
	}

	/**
	* @return all Groups currently on the board
	*/
	static Group[] getGroups()
	{
		return THE_BOARD.getIntGroups();
	}

	private Group[] getIntGroups()
	{
		int numGroups = 0;
		Group groups[] = new Group[106];
		boolean inGroup;

		for(int i=0; i<boardHeight; i++)
		{
			inGroup = false;
			for(int j=0; j<boardWidth; j++)
			{
				if(currentPlaced[i][j]==-1)
				{
					if(inGroup)
					{
						inGroup = false;
						numGroups++;
					}
				}
				else
				{
					if(inGroup)
					{
						//add to group
						groups[numGroups].addItem(currentPlaced[i][j]);
					}
					else
					{
						inGroup = true;
						groups[numGroups] = new Group();
						//initialise new group
						groups[numGroups].addItem(currentPlaced[i][j]);
					}
				}
			}
		}// END of setting up groups

		setNumGroups(numGroups);
		return groups;
	}


	private void setNumGroups(int num)
	{
		numGroups = num;
	}

	/**
	* @return the number of groups currently placed on the canvas
	*/
	public static int getNumGroups()
	{
		return THE_BOARD.numGroups;
	}


	/**
	* @return whether the canvas is a valid board or not
	*/
	public static boolean checkBoardValidity()
	{
		return THE_BOARD.InternalBoardCheck();
	}

	private boolean InternalBoardCheck()
	{
		boolean isValid = checker.check();

		if(!isValid)
		{
			JOptionPane.showMessageDialog(null, "The board is not valid. You must either reset the board, move tiles around the board, or add more tiles.", "Rummikub", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			//set current board as previous valid board
			for(int i=0; i<boardHeight; i++)
			{
				for(int j=0; j<boardWidth; j++)
				{
					previousPlaced[i][j] = currentPlaced[i][j];
				}
			}
		
			//Rummikub.tellUser("Board is valid");
		}
		return isValid;
	}

	static void resetBoard()
	{
		THE_BOARD.resetInternalBoard();
	}

	void resetInternalBoard()
	{
		//int numToRemove = 0;
		//int [] rackTilesToAdd = new int[92];

		for(int i=0; i<boardHeight; i++)
		{
			for(int j=0; j<boardWidth; j++)
			{
				currentPlaced[i][j] = previousPlaced[i][j];
				drawTile(i,j);
			}
		}

		Rummikub.setAdding(false);
		Rummikub.getPlayersRack().reset();
		Rummikub.getPlayersRack().display(Rummikub.getPlayersRack().displayPlace);
		Human.setTilePlaced(false);
		repaint();
	}

	static void moveTile(int fromW, int toW, int fromH, int toH)
	{
		THE_BOARD.moveIntTile(fromW, toW, fromH, toH);
	}

	void moveIntTile(int fromW, int toW, int fromH, int toH)
	{
		if((currentPlaced[fromH][fromW]!=-1) && currentPlaced[toH][toW]==-1)
		{
			setWhichTile(currentPlaced[fromH][fromW]);
			currentPlaced[fromH][fromW] = -1;
			//previousPlaced[fromH][fromW] = -1;
			addTile(toH, toW);
			drawTile(fromH, fromW);
			repaint();
		}
	}

	/**
	* Gets the tile at a given position
	* @param hIndex - the height co-ordinate of the position
	* @param wIndex - the width co-ordinate of the position
	* @return <code>int</code> - the tile number at the given co-ordinates
	* @return -1 if the place is empty
	*/
	public static int getTileVal(int hIndex, int wIndex)
	{
		return THE_BOARD.getTileValue(hIndex, wIndex);
	}

	private int getTileValue(int hIndex, int wIndex)
	{
		return currentPlaced[hIndex][wIndex];
	}

	/**
	* Tells whether a given position holds a tile or not
	* @param hIndex - the height co-ordinate of the position
	* @param wIndex - the width co-ordinate of the position
	* @return <code>boolean</code> true if the the given position is empty
	*/
	public static boolean placeEmpty(int hIndex, int wIndex)
	{
		return THE_BOARD.placeIsEmpty(hIndex, wIndex);
	}

	private boolean placeIsEmpty(int i, int j)
	{
		return currentPlaced[i][j]==-1;
	}

	/**
	* sets a tile flag to the tile value that may be next placed
	* @param which the tile to be placed
	*/
	public static void whichTile(int which)
	{
		THE_BOARD.setWhichTile(which);
	}

	/**
	* sets a tile flag to the tile value that may be next placed
	* @param which the tile to be placed
	*/
	public void setWhichTile(int which)
	{
		whichTile = which;
	}

	/**
	* adds the tile set by setWhichTile(int)
	* @param hIndex - the height co-ordinate of the position
	* @param wIndex - the width co-ordinate of the position
	*/
	public static void addaTile(int hIndex, int wIndex)
	{
		THE_BOARD.addTile(hIndex, wIndex);
	}

	private void addTile(int i, int j)
	{
		currentPlaced[i][j] = whichTile;
		drawTile(i,j);
		repaint();
	}

	private void drawTile(int i, int j)
	{
		Image theTileImage;
		int tileId = currentPlaced[i][j];

		if(tileId == -1)
		{
			theTileImage = blankImage;
		}
		else
		{
			theTileImage = CompleteTileSet.getTile(tileId).getImageIcon().getImage();
		}

		boardGraphics = originalBoard.getGraphics();
		boardGraphics.drawImage(theTileImage,j*45,i*60,this);
	}

	/**
	* gets the parent of the canvas
	* @see PlayingCanvasPane
	* @return <code>PlayingCanvasPane</code> the parent of the canvas
	*/
	public PlayingCanvasPane getParentPane()
	{
		return parentPane;
	}

	/**
	* refreshes the panels graphics
	* @param g the <code>Graphics</code> object associated with the canvas
	*/
	public void paint(Graphics g)
	{
		if(originalBoard == null)
		{
			originalBoard = createImage(boardWidth*45, boardHeight*60);
			boardGraphics = originalBoard.getGraphics();
			boardGraphics.setColor(new Color(0,156,0));
			boardGraphics.fillRect(0, 0, getWidth(), getHeight());

		}

		for(int i=0; i<boardHeight; i++)
		{
			for(int j=0; j<boardWidth; j++)
			{
				drawTile(i,j);
			}
		}

		g.drawImage(originalBoard,0,0,this);
	}

}// END of class CanvasPanel
