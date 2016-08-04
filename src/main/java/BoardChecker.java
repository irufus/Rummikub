/**
* This class checks the validity of groups on the board
*/
public class BoardChecker
{
	int boardHeight, boardWidth;

/**
* BoardChecker constructor
* @param height the height of the board
* @param width the width of the board
*/
	public BoardChecker(int height, int width)
	{
		boardHeight = height;
		boardWidth = width;
	}

/**
* Checks that all groups on the board are valid
* @return a <code>boolean</code> whether the board is valid or not
*/
	public boolean check()
	{
		boolean validBoard = true;
		int[][] boardState = CanvasPanel.getBoard();

		// first extract groups
		boolean inGroup = false;
		int numGroups = 0;
		Group groups[] = new Group[106];

		for(int i=0; i<boardHeight; i++)
		{
			inGroup = false;
			for(int j=0; j<boardWidth; j++)
			{
				if(boardState[i][j]==-1)
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
						groups[numGroups].addItem(boardState[i][j]);
					}
					else
					{
						inGroup = true;
						groups[numGroups] = new Group();
						//initialise new group
						groups[numGroups].addItem(boardState[i][j]);
					}
				}
			}
		}// END of setting up groups

		boolean validGroup;

		for(int i=0; i<numGroups; i++)
		{
			validGroup = groups[i].isValidGroup();
			if(!validGroup)
			{
				validBoard = validGroup;
			}
		}
		return validBoard;
	}

}// END of class BoardChecker
