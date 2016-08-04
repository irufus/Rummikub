/**
* This class holds the complete set of tiles
*/

public class CompleteTileSet
{

	static CompleteTileSet FULL_SET;
	Tile[] tileSet;
	final int setSize = 106;

	final int red = 0;
	final int blue = 1;
	final int yellow = 2;
	final int green = 3;
	final int joker = 4;

	/**
	* CompleteTileSet Constructor creates the complete set of tiles
	*/
	public CompleteTileSet()
	{
		tileSet = new Tile[setSize];
		int times = 0;
		int tileNum = 0;

		for(int colour=red; colour<green+1; colour++)
		{
			for(int value=1; value<14; value++)
			{
				times = 0;
				do{
					tileSet[tileNum] = new Tile(value, colour, tileNum);
					tileNum++;
					times++;
				}while(times<2);
			}
		}

		for(int i=1; i<3; i++)
		{
			tileSet[tileNum] = new Tile(i, joker, tileNum);
			tileNum++;
		}
		FULL_SET = this;
	}

	/**
	* gets a tile of a given index
	* @param index number of the tile to be returned
	* @return <code>Tile</code> the tile corresponding to the index
	*/
	public static Tile getTile(int number)
	{
		return FULL_SET.tileSet[number];
	}


}// END of class CompleteTileSet
