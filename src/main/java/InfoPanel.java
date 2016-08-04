import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
* This class is the Panel that contains all buttons and information given to user
* @see javax.swing.JPanel
*/
public class InfoPanel extends JPanel
{
	JButton quitButton;
	JButton takeTileButton;
	JButton endTurnButton;
	JButton sortTilesButton;
	JButton resetBoardButton;
	JPanel buttonPanel;
	JButton titleButton;
	Image titlepic;
	JTextArea infoArea;
	Rack the_pool, players_rack;
	Rummikub gameFr;

	/**
	* Constructor InfoPanel
	* @param pool the <code>Rack</code> coresponding to the tiles in the pool
	* @param pool the <code>Rack</code> coresponding to the tiles in the human player's hand
	*/
	public InfoPanel(Rack pool, Rack playersRack)
	{
		super();

		String pathSep = System.getProperty("file.separator");
		if(pathSep.equals("\\"))
		{
			pathSep = "\\\\";
		}

		titleButton = new JButton(new ImageIcon("pics" + pathSep + "titlepic.gif"));

		setPreferredSize(new Dimension(200,625));
		setBackground(new Color(0,0,0));

		the_pool = pool;
		players_rack = playersRack;
		buttonPanel = new JPanel(new GridLayout(5,1));
		quitButton = new JButton("QUIT");
		takeTileButton = new JButton("TAKE TILE");
		endTurnButton = new JButton("END TURN");
		sortTilesButton = new JButton("SORT TILES");
		resetBoardButton = new JButton("RESET BOARD");
		infoArea = new JTextArea(10,10);
		Font f = new Font("Courier", Font.BOLD, 12);
		infoArea.setFont(f);
		infoArea.setWrapStyleWord(true);
		infoArea.setLineWrap(true);

		buttonPanel.add(takeTileButton);
		buttonPanel.add(endTurnButton);
		buttonPanel.add(sortTilesButton);
		buttonPanel.add(resetBoardButton);
		buttonPanel.add(quitButton);

		titleButton.setBackground(new Color(0,0,0));
		titleButton.setForeground(new Color(0,0,0));
		//titleButton.setEnabled(false);
		this.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(infoArea, BorderLayout.NORTH);
		this.add(titleButton, BorderLayout.CENTER);

		ActionEventHandler handler = new ActionEventHandler();

		quitButton.addActionListener(handler);
		takeTileButton.addActionListener(handler);
		endTurnButton.addActionListener(handler);
		sortTilesButton.addActionListener(handler);
		resetBoardButton.addActionListener(handler);

		infoArea.setText("Tiles dealt. Your turn. Choose a tile to start a new group, or take a tile.");
	}

	/**
	* sets the <code>Rummikub</code> object associated with this object
	* @param gf the <code>Rummikub</code> corresponding to this
	*/
	public void setGameFrame(Rummikub gf)
	{
		gameFr = gf;
		return;
	}

	private void takeTile()
	{
		if(Human.isTilePlaced())
		{
			JOptionPane.showMessageDialog(null, "You have placed a tile, you may not take one.", "Rummikub", JOptionPane.ERROR_MESSAGE);
		}
		else if(Human.isTileTaken())
		{
			JOptionPane.showMessageDialog(null, "You have already taken a tile. You may take another one.", "Rummikub", JOptionPane.ERROR_MESSAGE);
		}
		else if(!Human.isTurn())
		{
			JOptionPane.showMessageDialog(null, "It is not your turn","Rummikub",JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			Human.setTileTaken(true);
			int newTile = the_pool.removeRandomTile();

			if(newTile!=-1)
			{
				players_rack.addTile(newTile);
				gameFr.showPlayersTiles(players_rack);
				infoArea.setText("Tile taken.");
			}
			else
			{
				// pop up dialog box to quit...
				JOptionPane.showMessageDialog(null, "No more tiles in pool.\nQuiting...", "Rummikub", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}
	}


	private void sortTiles()
	{
		players_rack.sort();
		gameFr.showPlayersTiles(players_rack);
	}

	private boolean checkBoard()
	{
		return CanvasPanel.checkBoardValidity();
	}

	void output(String outStr)
	{
		infoArea.setText(outStr);
	}

	private class ActionEventHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource()==takeTileButton)
			{
				takeTile();
			}
			else if(event.getSource()==endTurnButton)
			{
				if(Human.isTilePlaced() || Human.isTileTaken())
				{
					infoArea.setText("Checking board");
					boolean valid = checkBoard();

					if(valid)
					{
						if(Rummikub.getPlayersRack().getSize()==0)
						{
							JOptionPane.showMessageDialog(null, "Well done! You won!", "Rummikub", JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						}
						else
						{
							Rummikub.getPlayersRack().setNewRack();
							infoArea.setText("Computer's turn");
							Human.setTurn(false);
							Human.setTilePlaced(false);
							Human.setTileTaken(false);
							Computer.takeTurn();
						}
					}
				}
				else if(Human.isTurn())
				{
					JOptionPane.showMessageDialog(null, "You may not end you turn. You have not placed a tile, or taken one.", "Rummiub", JOptionPane.ERROR_MESSAGE);
					infoArea.setText("You must place a\ntile or take one.");
				}
				else if(!Human.isTurn())
				{
					JOptionPane.showMessageDialog(null, "It is not your turn to end!", "Rummikub", JOptionPane.ERROR_MESSAGE);
				}

			}
			else if(event.getSource()==quitButton)
			{
				System.exit(0);
			}
			else if(event.getSource()==sortTilesButton)
			{
				infoArea.setText("Tiles sorted.");
				sortTiles();
			}
			else if(event.getSource()==resetBoardButton)
			{
				if(Human.isTurn())
				{
					CanvasPanel.resetBoard();
					infoArea.setText("Board Reset");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "It is not your turn", "Rummikub", JOptionPane.ERROR_MESSAGE);
				}

			}
		}
	}//END of class ActionEventHandler

}//END of class InfoPanel
