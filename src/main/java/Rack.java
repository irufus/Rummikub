import javax.swing.*;
import java.awt.*;

/**
* This class corresponds to a set of tiles
*/
public class Rack
{

	int size = 0;
	int previousSize = 0;
	int [] tileNumbers = new int[106];
	int [] previousRack = new int[106];
	int fred=0;
	Image blankRack;
	Graphics rackGraphics;
	UserPanel displayPlace = null;


	/**
	* Rack Constructor which sets up the pool rack
	*/
	public Rack()
	{
		size = 106;

		for(int i=0; i<size; i++)
		{
			tileNumbers[i] = i;
		}

	}

	/**
	* Rack Constructor which sets up a player's rack
	* @param the_pool the pool from which the tiles are to be taken
	* @param the initial size the <code>Rack</code> is to be
	*/
	public Rack(Rack the_pool, int initialSize)
	{
		size = 0;

		for(int i=0; i<initialSize; i++)
		{
			int addingTile = the_pool.removeRandomTile();
			this.addTile(addingTile);
		}
	}

	/**
	* displays the player's <code>Rack</code> on the given <code>UserPanel</code>
	* @param dispPlace the <code>UserPanel</code> object to display the <code>Rack</code> on
	*/
	public void display(UserPanel dispPlace)
	{
		displayPlace = dispPlace;

		for(int i=0; i<size; i++)
		{
			displayPlace.drawTile(i);
		}
		displayPlace.paint(displayPlace.getGraphics());
	}

	/**
	* sets the <code>Rack</code> as what it currently is
	*/
	public void setNewRack()
	{
		for(int i=0; i<size; i++)
		{
			previousRack[i] = tileNumbers[i];
		}
		previousSize = size;
	}

	/**
	* resets the <code>Rack</code> as what it previously was
	*/
	public void reset()
	{
		int i=0;

		for(i=0; i<previousSize; i++)
		{
			tileNumbers[i] = previousRack[i];
		}
		size = previousSize;
		display(displayPlace);
	}


	/**
	* outputs the <code>Rack</code> textually to the default output sink
	*/
	public void printRack()
	{
		System.out.println("\nRack size = " + size);

		for(int i=0; i<size; i++)
		{
			System.out.println("Tile " + i + " = " + tileNumbers[i]);
		}
	}

	/**
	* Determine the size of the rack
	* @return <code>int</code>the size of the rack
	*/
	public int getSize()
	{
		return size;
	}


	/**
	* adds a given tile to the <code>Rack</code>
	* @param tileNumber the tile to add
	*/
	public void addTile(int tileNumber)
	{
		size++;
		tileNumbers[size-1] = tileNumber;
		return;
	}

	/**
	* removes a given tile by index
	* @param tileIndex the index within the <code>Rack</code> of the tile to remove
	*/
	public void removeTile(int tileIndex)
	{
		for(int i=tileIndex; i<size-1; i++)
		{
			tileNumbers[i] = tileNumbers[i+1];
		}
		size--;
	}

	/**
	* removes a given tile by tile number
	* @param tileIndex the index within the <code>Rack</code> of the tile to remove
	*/
	public void removeTileNumber(int tileNum)
	{
		for(int i=0; i<size; i++)
		{
			if(getTileNumber(i)==tileNum)
			{
				removeTile(i);
			}
		}
	}

	/**
	* Determines the tile number of a tile given its index
	* @param tileIndex the index of the tile
	* @return <code>int</code> the tile number of the indexed tile
	*/
	public int getTileNumber(int tileIndex)
	{
		return tileNumbers[tileIndex];
	}

	/**
	* removes a random tile from the rack
	* @return <code>int</code>the tile number of the removed tile
	*/
	public int removeRandomTile()
	{
		if(size==0)
		{
			return -1;
		}
		else
		{
			int whichTile = (int)(Math.random()*size);
			int tileRemoved = tileNumbers[whichTile];
			this.removeTile(whichTile);
			return tileRemoved;
		}
	}

	/**
	* sorts the <code>Rack</code> by colour then number
	*/
	public void sort()
	{
		int tempTile = 0;
		for (int j=size-1; j>0; j--)
		{
			for (int i=0; i<j; i++)
			{
				if(tileNumbers[i]>tileNumbers[i+1])
				{
					tempTile = tileNumbers[i+1];
					tileNumbers[i+1] = tileNumbers[i];
					tileNumbers[i] = tempTile;
				}
			}
		}
	}
}// END of class Rack
