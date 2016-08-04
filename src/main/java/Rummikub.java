import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* This class is the main class which sets up all others
* @see javax.swing.JFrame
*/
public class Rummikub extends JFrame
{

	static Rummikub GAME_FRAME;
	PlayingCanvasPane board;
	UserPane userPane;
	InfoPanel infoPanel;
	JPanel top;

	final int startRackSize = 14;
	CompleteTileSet allTiles;
	Rack pool;
	Rack players_rack;
	Rack comps_rack;

	Computer compPlayer;
	Human humanPlayer;

	boolean addTile = false;
	String OSused="";

	/**
	* Rummikub Constructor
	* @param title the title of the frame
	*/
	public Rummikub(String title)
	{
		super(title);

		OSused = System.getProperty("file.separator");
		if(OSused.equals("\\"))
		{
			OSused = "\\\\";
		}

		allTiles = new CompleteTileSet();

		pool = new Rack();
		players_rack = new Rack(pool, startRackSize);
		comps_rack = new Rack(pool, startRackSize);
		players_rack.setNewRack();

		compPlayer = new Computer(comps_rack);
		humanPlayer = new Human();

		board = new PlayingCanvasPane();
		userPane = new UserPane(players_rack);
		infoPanel = new InfoPanel(pool, players_rack);
		top = new JPanel();

		setSize(1000,710);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());

		top.setLayout(new BorderLayout());
		top.add(board, BorderLayout.WEST);
		top.add(infoPanel, BorderLayout.EAST);
		top.setPreferredSize(new Dimension(1000, 625));
		getContentPane().add(top, BorderLayout.NORTH);
		getContentPane().add(userPane, BorderLayout.SOUTH);

		pack();
		setVisible(true);
		infoPanel.setGameFrame(this);
		GAME_FRAME = this;

		showPlayersTiles(players_rack);
	}


	/*
	* checks if a tile is currently being placed on the board
	* @return <code>boolean</code> whether there is a tile being placed or not
	*/
	public static boolean adding()
	{
		return GAME_FRAME.addTile;
	}

	/**
	* Determines the <code>Rack</code> belonging to the human player
	* @return the <code>Rack</code> object of the player
	*/
	public static Rack getPlayersRack()
	{
		return GAME_FRAME.players_rack;
	}

	/**
	* Determines the <code>Rack</code> corresponding to the pool of tiles
	* @return the <code>Rack</code> object of the pool
	*/
	public static Rack getPool()
	{
		return GAME_FRAME.pool;
	}

	/**
	* Determines the computer's <code>Rack</code>
	* @return the <code>Rack</code> corresponding to the tiles in the computer's hand
	*/
	public static Rack getCompsRack()
	{
		return GAME_FRAME.comps_rack;
	}

	/**
	* set whether a tile is being added or not
	* @param isAdding whether a tile is being added or not
	*/
	public static void setAdding(boolean isAdding)
	{
		GAME_FRAME.setInternalAdd(isAdding);
	}

	private void setInternalAdd(boolean isIt)
	{
		addTile = isIt;
	}


	/**
	* places a given tile on the board
	* @param tileIndex the index of the tile on the player's <code>Rack</code> to put down
	* @param who <code>String</code> ("Comp" if computer) is placing the tile
	*/
	public static void putTileDown(int tileIndex, String who)
	{
		GAME_FRAME.internalPutTile(tileIndex, who);
	}

	private void internalPutTile(int tileIndex, String who)
	{
		if(who.equals("Comp"))
		{
			int whichNumericalTile = comps_rack.getTileNumber(tileIndex);
			comps_rack.removeTile(tileIndex);
			board.setWhichTile(whichNumericalTile);
		}
		else
		{
			int whichNumericalTile = players_rack.getTileNumber(tileIndex);
			Tile whichTile = CompleteTileSet.getTile(whichNumericalTile);
			players_rack.removeTile(tileIndex);
			players_rack.display(userPane.getPanel());
			// where to remove last tile...
			int blankIndex = players_rack.getSize();
			userPane.getPanel().drawTile(blankIndex);
			userPane.getPanel().repaint();
			board.setWhichTile(whichNumericalTile);
		}
	}

	/**
	* outputs a message to the user in the info area of the <code>InfoPanel</code>
	* @param infoStr the <code>String</code> to be outputted
	*/
	public static void tellUser(String infoStr)
	{
		GAME_FRAME.infoPanel.output(infoStr);
	}

	public static void main(String args[])
	{
		Rummikub gameView = new Rummikub("Rummikub");
		gameView.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}

	/**
	* displays the players tiles on the <code>UserPanel</code> associated with it
	* @param playersRack the <code>Rack</code> corresponding to the tiles in the player's hand
	*/
	public void showPlayersTiles(Rack playersRack)
	{
		playersRack.display(userPane.getPanel());
	}
}
