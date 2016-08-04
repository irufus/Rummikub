import java.awt.event.*;
import java.awt.*;
import javax.swing.JOptionPane;

/**
* This class listens for events on the panel that display a <code>Rack</code> object
* @see java.awt.event.MouseListener
*/
public class RackListener implements MouseListener
{
	Rack rack;

	/**
	* RackListener Constructor
	* @param playersRack the <code>Rack</code> corresponsing to the tiles in the players's hand
	*/
	public RackListener(Rack playersRack)
	{
		rack = playersRack;
	}

	/**
	* registers a mouse press on the panel
	*/
	public void mousePressed(MouseEvent event)
	{
		if(!Rummikub.adding() && Human.isTurn() && !Human.isTileTaken())
		{
			int x_pos = event.getX();
			int tileIndex = x_pos/45;

			// check if its a tile
			if(tileIndex < rack.getSize())
			{
				String tileColour = CompleteTileSet.getTile(rack.getTileNumber(tileIndex)).getColour();
				int faceValue = CompleteTileSet.getTile(rack.getTileNumber(tileIndex)).faceValue;
				Rummikub.tellUser("Place " + tileColour + " " + faceValue + ".");
				Rummikub.setAdding(true);
				Rummikub.putTileDown(tileIndex, "");
			}
		}
		else if(Human.isTileTaken())
		{
			JOptionPane.showMessageDialog(null, "You have taken a tile, you may not now place one on this turn.", "Rummikub", JOptionPane.ERROR_MESSAGE);
		}
	}


	public void mouseClicked(MouseEvent event)
	{
	}

	public void mouseReleased(MouseEvent event)
	{
	}

	public void mouseEntered(MouseEvent event)
	{
	}

	public void mouseExited(MouseEvent event)
	{
	}
}// END of class RackListener
