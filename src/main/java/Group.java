/**
* Corresponds to a set or sequence
*/

public class Group
{
	int size;
	int[] tiles = new int[20];

	/**
	* creates an empty <code>Group</code>
	*/
	public Group()
	{
		size = 0;
	}

	/**
	* Setermines the number of tiles in the group
	* @return size the number of tiles
	*/
	public int getSize()
	{
		return size;
	}

	/**
	* checks if the group is a valid set or sequence
	* @return <code>boolean</code> whether the group is valid or not
	*/
	public boolean isValidGroup()
	{
		boolean validGroup = (isSet() || isSequence());
		return validGroup;
	}

	/**
	* adds a tile
	* @param num the tile number
	*/
	public void addItem(int num)
	{
		tiles[size] = num;
		size++;
	}

	/**
	* Determines a tile
	* @param num the index of the tile
	*/
	public int getItem(int num)
	{
		return tiles[num];
	}

	/**
	* Determines if a given tile is present
	* @param tileNum the tile number
	* @return <code>boolean</code> whether the tile is a member or not
	*/
	boolean isMember(int tilenum)
	{
		for(int i=0; i<size; i++)
		{
			if(tiles[i] == tilenum)
				return true;
		}
		return false;
	}

	
	/**
	* prints the group information to the screen
	*/
	public void print()
	{
		for(int i=0; i<size; i++)
		{
			System.out.println(CompleteTileSet.getTile(tiles[i]).getColour() + " " + CompleteTileSet.getTile(tiles[i]).getValue());
		}
		System.out.println("");
	}

	// checks if the group is a valid set
	private boolean isSet()
	{
		boolean validSet = true;

		// test size of set
		if(size>4 || size<3)
		{
			validSet = false;
		}

		// check all tiles are the same value
		int val = 0;
		int setVal = 0;
		boolean setValueSet = false;
		boolean isJoker = false;
		Tile currTile;

		for(int i=0; i<size; i++)
		{
			currTile = CompleteTileSet.getTile(tiles[i]);
			val = currTile.getValue();
			isJoker = currTile.getColour().equals("joker");

			if(!isJoker)
			{
				if(setValueSet)
				{
					if(val!=setVal)
					{
						validSet = false;
					}
				}
				else
				{
					setVal = val;
					setValueSet = true;
				}
			}
		}

		// test tiles are different colours
		boolean redFlag = false;
		boolean blueFlag = false;
		boolean yellowFlag = false;
		boolean greenFlag = false;

		for(int i=0; i<size; i++)
		{
			String colour = CompleteTileSet.getTile(tiles[i]).getColour();

			if(colour.equals("red"))
			{
				if(redFlag==true)
				{
					validSet = false;
				}
				redFlag = true;
			}

			if(colour.equals("blue"))
			{
				if(blueFlag==true)
				{
					validSet = false;
				}
				blueFlag = true;
			}

			if(colour.equals("yellow"))
			{
				if(yellowFlag==true)
				{
					validSet = false;
				}
				yellowFlag = true;
			}

			if(colour.equals("green"))
			{
				if(greenFlag==true)
				{
					validSet = false;
				}
				greenFlag = true;
			}
		}

		return validSet;
	}


	// checks if the group is a valid sequence
	private boolean isSequence()
	{
		boolean validSequence = true;

		// check sequence size
		if(size<3 || size>13)
		{
			validSequence = false;
		}

		// check all one colour

		String col = "";
		String sequenceCol = "";
		boolean isJoker = false;
		boolean colourSet = false;
		Tile currTile;

		for(int i=0; i<size; i++)
		{
			currTile = CompleteTileSet.getTile(tiles[i]);
			col = currTile.getColour();
			isJoker = col.equals("joker");

			if(!isJoker)
			{
				if(colourSet)
				{
					if(col!=sequenceCol)
					{
						validSequence = false;
					}
				}
				else
				{
					sequenceCol = col;
					colourSet = true;
				}
			}
		}

		// test tiles are sequential
		int vals[] = new int[size];

		for(int i=0; i<size; i++)
		{
			currTile = CompleteTileSet.getTile(tiles[i]);
			vals[i] = currTile.getValue();

			if(currTile.getColour().equals("joker"))
			{
				vals[i] = -1;
			}
		}


		// test vals[]
		int joker_count = 0;
		boolean sequenceStarted = false;

		for(int i=0; i<size; i++)
		{
			if(vals[i] == -1)
			{
				if(sequenceStarted)
				{
					joker_count++;
				}
			}
			else
			{
				if(!sequenceStarted)
				{
					sequenceStarted = true;
					joker_count = 0;
				}
				else
				{
					if(vals[i-joker_count-1]==vals[i]-joker_count-1)
					{
						joker_count++;
					}
					else
					{
						validSequence = false;
					}
				}
			}

		}

		// only test if everything else fits
		if(validSequence)
		{
			int upper_bound = 0;
			int lower_bound = 0;
			// check bounds of sequence (<13 and >1)
			for(int i=0; i<size; i++)
			{
				upper_bound = vals[i] + size - 1 - i;

				if(upper_bound > 13)
				{
					validSequence = false;
				}
				lower_bound = vals[i] - i;

				if(lower_bound < 1 && vals[i] != -1)
				{
					validSequence = false;
				}
			}
		}
		return validSequence;
	}


	/**
	* tests one Group against another to see if they contain one or more of the same tiles
	* @param otherGroup the <code>Group</code> to test this one against
	* @return <code>boolean</code> whether the <code>Group</code> passed in clashes with the current one
	*/
	public boolean clashesWith(Group otherGroup)
	{
		boolean clash = false;

		for(int i=0; i<size; i++)
		{
			for(int j=0; j<otherGroup.size; j++)
			{
				if(getItem(i)==otherGroup.getItem(j))
				{
					clash = true;
				}
			}
		}
		return clash;
	}
}// END of class Group
