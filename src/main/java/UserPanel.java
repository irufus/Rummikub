import javax.swing.*;
import java.awt.*;


/**
* This class is the Panel that the player's tiles appear on
*/
public class UserPanel extends JPanel
{
	Rack playersRack;
	Image originalRack, blankImage;
	Graphics tilesGraphics;
	String pathSep;
	Image theTileImage;

	/**
	* UserPanel Constructor
	* @param the player's rack <code>Rack</code>
	*/
	public UserPanel(Rack playRack)
	{
		super();
		playersRack = playRack;

		pathSep = System.getProperty("file.separator");
		if(pathSep.equals("\\"))
		{
			pathSep = "\\\\";
		}
		blankImage = (new ImageIcon("pics" + pathSep + "blank.gif")).getImage();
	}

	/*
	* draw a given tile on the panel
	* @param i the index of the tile in the <code>Rack</code>
	*/
	public void drawTile(int i)
	{
		if(originalRack == null)
		{
			originalRack = createImage(getWidth(), getHeight());
			tilesGraphics = originalRack.getGraphics();
			tilesGraphics.setColor(new Color(0,156,0));
			tilesGraphics.fillRect(0, 0, getWidth(), getHeight());
		}

		if(i<playersRack.getSize())
		{
			int tileId = playersRack.getTileNumber(i);
			theTileImage = CompleteTileSet.getTile(tileId).getImageIcon().getImage();
		}
		else
		{
			theTileImage = blankImage;
		}

		tilesGraphics = originalRack.getGraphics();
		tilesGraphics.drawImage(theTileImage,i*45,0,this);
	}

	/**
	* paints the Panel when called
	* @param the <code>Graphics</code> object associated with the Panel
	*/
	public void paint(Graphics g)
	{
		if(originalRack == null)
		{
			originalRack = createImage(getWidth(), getHeight());
			tilesGraphics = originalRack.getGraphics();
			tilesGraphics.setColor(new Color(0,156,0));
			tilesGraphics.fillRect(0, 0, getWidth(), getHeight());
		}

		g.drawImage(originalRack,0,0,this);
	}
}// END of class UserPanel
