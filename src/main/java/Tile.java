import java.awt.*;
import javax.swing.*;

/**
* This class represents a tile of the game
*/
public class Tile
{

	final int red = 0;
	final int blue = 1;
	final int yellow = 2;
	final int green = 3;
	final int joker = 4;

	int tileNumber;
	int faceValue;
	int tileColour;
	Image tileImage;
	ImageIcon im;

	String pathSep = "";

	/**
	* Tile Constructor makes a new
	* @param value the <code>int</code> value of the tile
	* @param colour the <code>int</code> colour of the tile
	* @param number the <code>int</code> number of the tile
	*/
	public Tile(int value, int colour, int number)
	{
		tileNumber = number;
		faceValue = value;
		tileColour = colour;

		pathSep = System.getProperty("file.separator");

		if(pathSep.equals("\\"))
			pathSep = "\\\\";

		String ImagePath = "pics" + pathSep + this.getColour() + faceValue + ".gif";
		im = new ImageIcon(ImagePath);
	}

	/*
	* Determines the colour of the tile
	* @return a <code>String</code> ("red", "yellow", "blue", "green")
	*/
	public String getColour()
	{
		if(tileColour==red)
			return "red";
		else if(tileColour==blue)
			return "blue";
		else if(tileColour==yellow)
			return "yellow";
		else if(tileColour==green)
			return "green";
		else if(tileColour==joker)
			return "joker";
		else
			System.out.println("TileColour: " + tileColour);

			return "";
	}

	/**
	* Determines the face value of the tile
	* @return <code>int</code> the face value
	*/
	public int getValue()
	{
		return faceValue;
	}

	/**
	* Determines the <code>ImageIcon</code> associated with the tile
	* @return the <code>ImageIcon</code> of the tile
	*/
	public ImageIcon getImageIcon()
	{
		return im;
	}

}// END of class Tile
