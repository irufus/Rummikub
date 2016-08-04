import javax.swing.*;
import java.awt.*;

/**
* This class is the scroll pane that holds the canvas of the board
* @see javax.swing.JScrollPane
*/
public class PlayingCanvasPane extends JScrollPane
{
	CanvasPanel canvasPanel;
	int size;

	Image originalBoard;
	Image boardImageBuffer;
	Graphics boardGraphics;


	/**
	* PlayingCanvasPane Constructor
	*/
	public PlayingCanvasPane()
	{
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		size = 0;
		canvasPanel = new CanvasPanel(this);
		setViewportView(canvasPanel);
		setPreferredSize(new Dimension(800, 625));
	}

	/**
	* sets a tile which may be added to the canvas
	* @param which the tile to be set
	*/
	public void setWhichTile(int which)
	{
		canvasPanel.setWhichTile(which);
	}

	/**
	* gets the parent panel of this pane
	* @return the <code>CanvasPanel</code> which is the parent of this object
	*/
	public CanvasPanel getPanel()
	{
		return canvasPanel;
	}

}// END of class PlayingCanvasPane
