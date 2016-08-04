import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.util.GregorianCalendar;


/**
* This class is the computer player
*/
public class Computer
{
	Rack rack;
	static Computer THE_COMPUTER;
	int boardHeight = 10;
	int boardWidth = 20;
	int startingBoard[][] = new int[boardHeight][boardWidth];
	int manipBoard[][] = new int[boardHeight][boardWidth];
	int [] boardTiles = new int[104];
	int numOnBoard = 0;

	Group[] sequences = new Group[130656];
	Group[] sets = new Group[416];
	Group[] allGroups;
	Solution bestSoln;
	boolean solnFound;
	boolean used[];
	boolean anyPlaced;
	int numAvailable;
	int numSequences;
	int numSets;
	int numGroups;
	int numSolns;
	int leftOnRack;
	int bestLeftOnRack;
	
	GregorianCalendar currentCal;
	int startSec;

	boolean setBreak;


	/**
	* Computer Constructor creates a computer player instance
	* @ param compsRack the <code>Rack</code> containing the tiles of the computer player
	*/
	public Computer(Rack compsRack)
	{
		rack = compsRack;
		//set up vars
		THE_COMPUTER = this;
	}

	/**
	* invoked to make computer player take a turn
	*/
	public static void takeTurn()
	{
		THE_COMPUTER.takeIntTurn();
	}

	private void takeIntTurn()
	{
		Rummikub.tellUser("Computer's turn");
		boardTiles = new int[104];
		numOnBoard = 0;
		anyPlaced = false;
		setBreak = false;
		int availableTiles[] = getAvailableTiles();

		numAvailable = availableTiles.length;
		bestLeftOnRack = rack.getSize();
		//GregorianCalendar startCal = new GregorianCalendar();
		
		startSec = (new GregorianCalendar()).get(GregorianCalendar.MINUTE)*60 + (new GregorianCalendar()).get(GregorianCalendar.SECOND);
		System.out.println("Start time: " + startSec);
		
		// first pull out sequences...
		//sequences = getAllSequences(availableTiles);
		//System.out.println("num Sequences: " + numSequences);
		numSequences=0;


		// then pull out sets...
		sets = getAllSets(availableTiles);
		//System.out.println("num Sets: " + numSets);

		numGroups = numSets + numSequences;
		allGroups = new Group[numGroups];

		for(int i=0; i<numGroups; i++)
		{
			if(i<numSequences)
			{
				allGroups[i] = sequences[i];
			}
			else
			{
				allGroups[i] = sets[i-numSequences];
			}

			for(int j=0; j<allGroups[i].getSize(); j++)
			{
				System.out.println(CompleteTileSet.getTile(allGroups[i].getItem(j)).getColour() + " " + CompleteTileSet.getTile(allGroups[i].getItem(j)).getValue() + " (" + allGroups[i].getItem(j) + ")");
			}
			System.out.println("==========================================================");
		}

		System.out.println("numGroups: " +  numGroups);
		// method to decide what to place on board
		if(numGroups!=0)
		{
			System.out.println("About to try to find a solution");
			used = new boolean[numGroups];

			for(int i=0; i<numGroups; i++)
			{
				used[i] = false;
			}
			numSolns = 0;
			leftOnRack = 0;
			//System.out.println("Best left on rack: " + bestLeftOnRack);
			//findSolution(0);
		}

		// if nothing was placed
		if(!anyPlaced)
		{
			int newTile = Rummikub.getPool().removeRandomTile();

			if(newTile!=-1)
			{
				rack.addTile(newTile);
	 		}
		 	else
			{
				// pop up dialog box to quit...
				JOptionPane.showMessageDialog(null, "No more tiles in pool.\nQuiting...", "Rummikub", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
	 		}
			JOptionPane.showMessageDialog(null, "Computer player took a tile. Your turn.", "Rummikub", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			// place the tiles from the best solution
			System.out.println("About to start placing groups");
			System.out.println("Num groups to place: " + bestSoln.getSize());

			System.out.println("Setting board to blank");
			CanvasPanel.whichTile(-1);

			for(int i=0; i<boardHeight; i++)
			{
				for(int j=0; j<boardWidth; j++)
				{
					CanvasPanel.addaTile(i, j);
				}
			}

			Group toPlace[] = bestSoln.getGroups();

			for(int i=0; i<bestSoln.getSize(); i++)
			{
				Dimension putWhere = new Dimension(0, 0);
				int groupSize = toPlace[i].getSize();
				putWhere = getSpace(groupSize);
	
				if(putWhere.getHeight()==-1)
				{
					System.out.println("No space was found");
				}
				else
				{
					int tileNums[] = new int[groupSize];
					Tile tileToPlace[] = new Tile[groupSize];
					int x = (int)putWhere.getHeight();
					int y = (int)putWhere.getWidth();
					System.out.println("Starting place = " + x + ", " + y);
	
					for(int j=0; j<groupSize; j++)
					{
						tileNums[j] = toPlace[i].getItem(j);
						tileToPlace[j] = CompleteTileSet.getTile(tileNums[j]);
						CanvasPanel.whichTile(tileNums[j]);
	
						// test if on board or rack
						boolean onBoard = false;
						for(int k=0; k<numOnBoard; k++)
						{
							if(boardTiles[k] == tileNums[j])
							{
								onBoard = true;
							}
						}
	
						CanvasPanel.addaTile(x, y+j);
						//System.out.println("Adding a tile to place: " + x + ", " +  (y+j));
	
						// remove from rack if necessary
						if(!onBoard)
						{
							rack.removeTileNumber(tileNums[j]);
						}
					}
				}
			}
			JOptionPane.showMessageDialog(null, "Computer player is finished placing tiles. Your turn.", "Rummikub", JOptionPane.INFORMATION_MESSAGE);
		}

		if(rack.getSize()==0)
		{
			JOptionPane.showMessageDialog(null, "Hard Luck. The computer beat you.", "Rummikub", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}

		Rummikub.tellUser("End of computer's turn.\nYour go.");
		Human.setTurn(true);
	}



	/*-------------------------------------------
	---------------------------------------------
	-------------------------------------------*/
	private void findSolution(int x)
	{
		// compare cuurent date and start date
		currentCal = new GregorianCalendar();
		int currSec = currentCal.get(GregorianCalendar.MINUTE)*60 + currentCal.get(GregorianCalendar.SECOND);

		if((currSec - startSec) == 30)
		{
			//System.out.println("Start time: " + startSec);
			//System.out.println("Current time: " + currSec);
			//System.out.println("returning");
			//return;
		}

		if(setBreak)
		{
			System.out.println("Returning");
			return;
		}

		boolean clash = false;
		//System.out.println("in find solution: x = " + x);
		for(int i=x; i<numGroups; i++)
		{
	
			for(int j=0; j<x+1; j++)
			{
				if(used[j]==true)
				{
					if(allGroups[i].clashesWith(allGroups[j]))
					{
						clash = true;
					}
				}
			}

			if(!clash)
			{
				used[i] = true;
				findSolution(i+1);
				used[i] = false;
			}
		}

		// set up temporary solution
		Solution tempSoln = new Solution();
		for(int j=0; j<numGroups; j++)
		{
			if(used[j]==true)
			{
				tempSoln.addGroup(allGroups[j]);
			}
		}

		// now test that solution meets criteria
		boolean keepSoln = true;

		int numTilesUsed = 0;
		for(int j=0; j<numGroups; j++)
		{
			if(used[j]==true)
			{
				numTilesUsed += allGroups[j].getSize();
				System.out.println("Size of group" + j + ": " + allGroups[j].getSize());
				System.out.println("numTilesUsed: " + numTilesUsed);
			}
		}

		leftOnRack = numAvailable - numTilesUsed;
		System.out.println("Left on Rack: " + leftOnRack);

		if(leftOnRack == 0)
		{
			System.out.println("Best solution possible like ever foudn!");
			setBreak = true;
		}
		boolean equalSoln = false;

		if(bestLeftOnRack>leftOnRack)
		{
			// check all tiles in are in solution
			for(int i=0; i<numOnBoard; i++)
			{
				if(!tempSoln.isMember(boardTiles[i]))
				{
					keepSoln =false;
				}
			}

			if(keepSoln)
			{
				//equalSoln = true;
				System.out.println("This is the best solution yet!!!");
				System.out.println("bestLeftOnRack: " + bestLeftOnRack + ". leftOnRack: " + leftOnRack);
				bestLeftOnRack = leftOnRack;
			}

		}
		else
		{
			keepSoln = false;
		}

		// now store tempsolution if necessary
		if(keepSoln)
		{
			/*if(equalSoln)
			{
				// decide which to keep
			}
			else
			{
				bestSoln = new Solution();
			}*/
			bestSoln = new Solution();
			Group tempGroups[] = tempSoln.getGroups();
			System.out.println("Keeping solution");
			System.out.println("tempSoln size: " + tempSoln.getSize());
			anyPlaced = true;

			for(int i=0; i<tempSoln.getSize(); i++)
			{
				bestSoln.addGroup(tempGroups[i]);
			}

			solnFound = true;
		}
		else
		{
			solnFound = false;
		}

	}



	/*-------------------------------------------
	---------------------------------------------
	-------------------------------------------*/
	// sorts the tiles to do search for sequences
	private void sortForSequence(int[] availableTiles)
	{
		int numAvailable = availableTiles.length;
		// sort tiles by colour then number
 		int tempTile = 0;

		for (int j=numAvailable-1; j>0; j--)
		{
			for (int i=0; i<j; i++)
			{
				if(availableTiles[i]>availableTiles[i+1])
				{
					tempTile = availableTiles[i+1];
					availableTiles[i+1] = availableTiles[i];
					availableTiles[i] = tempTile;
				}
			}
		}
	}



	/*--------------------------------------------------------------
	----------------------------------------------------------------
	----------------------------------------------------------------
	--------------------------------------------------------------*/
	// method that returns all possible sequences from all available tiles
	private Group[] getAllSequences(int[] availableTiles)
	{
		numSequences = 0;
		int numThisTile = 0;
		int currentGroups = 0;
		int val = 0;
		int lastVal = 0;
		String col = "";
		String lastCol = "";
		int numAvailable = availableTiles.length;

		// sort array of available tiles for extracting sequences
		sortForSequence(availableTiles);

		Tile sequenceTiles[] = new Tile[numAvailable];
		for(int i=0; i<numAvailable; i++)
		{
			sequenceTiles[i] = CompleteTileSet.getTile(availableTiles[i]);
		}

		//for all tiles upto and including the 3rd last
		for(int i=0; i<numAvailable-2; i++)
		{
			Group allSequences[] = new Group[17000];
			numThisTile = 0;
			lastVal = sequenceTiles[i].getValue();
			lastCol = sequenceTiles[i].getColour();
			allSequences[numThisTile] = new Group();
			numThisTile++;
			allSequences[numThisTile-1].addItem(availableTiles[i]);

			currentGroups = 1;

			// for all subsequent tiles after i
			for(int j=i+1; j<numAvailable; j++)
			{
				val = sequenceTiles[j].getValue();
				col = sequenceTiles[j].getColour();

				if(lastCol.equals(col))
				{
					//if its next in a set
					if(val==lastVal+1)
					{
						// check size of group. If its greater than 3 make new group with this tile added
						if(allSequences[numThisTile-1].size>2)
						{
							// doing it from index 0, but we want to do it from current groups' indices
							int currNumThisTile = numThisTile;

							for(int q=currNumThisTile-currentGroups; q<currNumThisTile; q++)
							{
								numThisTile++;
								allSequences[numThisTile-1] = new Group();
								// add to group all from previous group....
								for(int z=0; z<allSequences[q].size; z++)
								{
						 			allSequences[numThisTile-1].addItem(allSequences[q].getItem(z));
								}
								allSequences[numThisTile-1].addItem(availableTiles[j]);
							}
						}
						else
						{
						 	for(int q=0; q<currentGroups; q++)
							{
								// just add current to tile current groups
						 		allSequences[q].addItem(availableTiles[j]);
							}
						}
					}
					else if(val==lastVal)
					{
						if(allSequences[numThisTile-1].size>1)
						{
							// for all previous groups redo them, then add this tile to new ones instead of old ones
							for(int q=0; q<currentGroups; q++)
							{
								numThisTile++;
								// set up a new group for every current group that exists
								allSequences[numThisTile-1] = new Group();

								// add all tiles from its previous group except last
								for(int z=0; z<allSequences[numThisTile-1-currentGroups].size-1; z++)
								{
									allSequences[numThisTile-1].addItem(allSequences[numThisTile-1-currentGroups].getItem(z));
								}
								// then add new tile instead
								allSequences[numThisTile-1].addItem(availableTiles[j]);
							}
							currentGroups*=2;
						}
					}
					else // then it will be end of sequences for this tile, so sort them out
					{
						for(int k=0; k<numThisTile; k++)
						{
							if(allSequences[k].size<3)
							{
							}
							else
							{
								sequences[numSequences] = new Group();
								for(int m=0; m<allSequences[k].size; m++)
								{
									// put into all sequences place
									sequences[numSequences].addItem(allSequences[k].getItem(m));
								}
								numSequences++;
							}
						}
						lastCol = col;
						lastVal = val;
						break;
					}


					if(j==numAvailable-1) // then it will be end of sets for this tile, so sort them out
					{
						for(int k=0; k<numThisTile; k++)
						{
							if(allSequences[k].size<3)
							{
							}
							else
							{
								sequences[numSequences] = new Group();
								for(int m=0; m<allSequences[k].size; m++)
								{
									// put into all sets place
									sequences[numSequences].addItem(allSequences[k].getItem(m));
								}
								numSequences++;
							}
						}
						lastCol = col;
						lastVal = val;
						break;
					}

					lastVal = val;
					lastCol = col;
				}
				else // it its not the same color its the end of sequences for that tile, so sort them out
				{
					for(int k=0; k<numThisTile; k++)
					{
						if(allSequences[k].size<3)
						{
						}
						else
						{
							sequences[numSequences] = new Group();
							for(int m=0; m<allSequences[k].size; m++)
							{
								sequences[numSequences].addItem(allSequences[k].getItem(m));
							}
							numSequences++;
						}
					}
					lastVal = val;
					lastCol = col;
					break;
				}
			}
		}
		return sequences;
	}


	private void sortForSets(int[] availableTiles)
	{
 		int tempTile = 0;
		int numAvailable = availableTiles.length;

		// sort by colour then number
		for (int j=numAvailable-1; j>0; j--)
		{
			for (int i=0; i<j; i++)
			{
				if(availableTiles[i]>availableTiles[i+1])
				{
					tempTile = availableTiles[i+1];
					availableTiles[i+1] = availableTiles[i];
					availableTiles[i] = tempTile;
				}
			}
		}

		// sort number but not within number
		for (int j=numAvailable-1; j>0; j--)
		{
			for (int i=0; i<j; i++)
			{
				// leave jokers out of ordering - make them be at end....
				if(availableTiles[i]%26>availableTiles[i+1]%26 && availableTiles[i+1]<104)
				{
					tempTile = availableTiles[i+1];
					availableTiles[i+1] = availableTiles[i];
					availableTiles[i] = tempTile;
				}
			}
		}

		// finish sorting by numbers
		boolean someChanged = false;
		do
		{
			someChanged = false;

			// sort tiles by colour then number
			for (int i=0; i<numAvailable-1; i++)
			{
				// leave jokers out of ordering - make them be at end....
				int val_1 = CompleteTileSet.getTile(availableTiles[i]).getValue();
				int val_2 = CompleteTileSet.getTile(availableTiles[i+1]).getValue();
				if(val_1==val_2 && availableTiles[i]>availableTiles[i+1])
				{
					tempTile = availableTiles[i+1];
					availableTiles[i+1] = availableTiles[i];
					availableTiles[i] = tempTile;
					someChanged = true;
				}
			}
		}while(someChanged);

	}

	/*--------------------------------------------------------------
	----------------------------------------------------------------
	----------------------------------------------------------------
	--------------------------------------------------------------*/
	// method that returns all possible sets from all available tiles
	private Group[] getAllSets(int[] availableTiles)
	{
		int numAvailable = availableTiles.length;
		int numThisTile = 0;
		int currentGroups = 0;
		int val = 0;
		int lastVal = 0;
		String col = "";
		String lastCol = "";
		Group allSets[] = new Group[250];
		Tile setTiles[] = new Tile[numAvailable];
		numSets = 0;

		// sort array of available tiles for extracting sets
		sortForSets(availableTiles);

		// set up tiles sorted by number
		for(int i=0; i<numAvailable; i++)
		{
			setTiles[i] = CompleteTileSet.getTile(availableTiles[i]);
		}


		//for all tiles upto and including the 3rd last
		for(int i=0; i<numAvailable-2; i++)
		{
			numThisTile = 0;
			lastVal = setTiles[i].getValue();
			lastCol = setTiles[i].getColour();
			allSets[numThisTile] = new Group();
			numThisTile++;
			allSets[numThisTile-1].addItem(availableTiles[i]);

			currentGroups = 1;

			// for all subsequent tiles after i
			for(int j=i+1; j<numAvailable; j++)
			{
				val = setTiles[j].getValue();
				col = setTiles[j].getColour();

				if(val==lastVal)
				{
					//if its sequential
					if(!col.equals(lastCol))
					{
						// do for all current groups
						// check size of group. If its greater than 3 make new group with this tile added
						if(allSets[numThisTile-1].size>2)
						{
							// doing it from index 0, but we want to do it from current groups' indices
							int currNumThisTile = numThisTile;

							for(int q=currNumThisTile-currentGroups; q<currNumThisTile; q++)
							{
								numThisTile++;
								allSets[numThisTile-1] = new Group();

								// add to group all from previous group....
								for(int z=0; z<allSets[q].size; z++)
								{
						 			allSets[numThisTile-1].addItem(allSets[q].getItem(z));
								}

								// also add current tile to each group
								allSets[numThisTile-1].addItem(availableTiles[j]);
							}
						}
						else // if its a set smaller than 3
						{
						 	for(int q=0; q<currentGroups; q++)
							{
								// just add current to tile current groups
						 		allSets[q].addItem(availableTiles[j]);
							}
						}
					}
					else // if the colours are equal
					{
						if(allSets[numThisTile-1].size>1)
						{
							// for all previous groups redo them, then add this tile to new ones instead of old ones
							for(int q=0; q<currentGroups; q++)
							{
								numThisTile++;
								// set up a new group for every current group that exists
								allSets[numThisTile-1] = new Group();

								// add all tiles from its previous group except last
								for(int z=0; z<allSets[numThisTile-1-currentGroups].size-1; z++)
								{
									allSets[numThisTile-1].addItem(allSets[numThisTile-1-currentGroups].getItem(z));
								}
								// then add new tile instead
								allSets[numThisTile-1].addItem(availableTiles[j]);
							}
							currentGroups*=2;
						}
					}

					if(j==numAvailable-1) // then it will be end of sets for this tile, so sort them out
					{
						for(int k=0; k<numThisTile; k++)
						{
							if(allSets[k].size<3)
							{
							}
							else
							{
								sets[numSets] = new Group();
								for(int m=0; m<allSets[k].size; m++)
								{
									// put into all sets place
									sets[numSets].addItem(allSets[k].getItem(m));
								}
								numSets++;
							}
						}
						break;
					}

					lastVal = val;
					lastCol = col;
				}
				else // it its not the same color its the end of sets for that tile, so sort them out
				{
					for(int k=0; k<numThisTile; k++)
					{
						if(allSets[k].size<3)
						{
						}
						else
						{
							sets[numSets] = new Group();
							for(int m=0; m<allSets[k].size; m++)
							{
								sets[numSets].addItem(allSets[k].getItem(m));
							}
							numSets++;
						}
					}
					break;
				}
			}
		}
		return sets;
	}


	/*--------------------------------------------------------------
	----------------------------------------------------------------
	----------------------------------------------------------------
	--------------------------------------------------------------*/
	//finds a space on the board for a group of size amount
	private Dimension getSpace(int amount)
	{
		for(int i=0; i<boardHeight; i++)
		{
			for(int j=0; j<boardWidth-amount; j++)
			{
				if(j==0)
				{
					boolean placeFound = true;

					for(int k=0; k<amount+1; k++)
					{
						if(!CanvasPanel.placeEmpty(i, j+k))
						{
							placeFound = false;
						}
					}

					if(placeFound)
					{
						return new Dimension(j, i);
					}
				}
				else if(j==boardWidth-amount)
				{
					boolean placeFound = true;

					for(int k=0; k<amount+1; k++)
					{
						if(!CanvasPanel.placeEmpty(i, j+k-1))
						{
							placeFound = false;
						}
					}
					if(placeFound)
					{
						return new Dimension(j, i);
					}
				}
				else
				{
					boolean placeFound = true;

					for(int k=-1; k<amount+1; k++)
					{
						if(!CanvasPanel.placeEmpty(i, j+k))
						{
							placeFound = false;
						}
					}
					if(placeFound)
					{
						return new Dimension(j, i);
					}

				}
			}
		}
		return new Dimension(-1, -1);
	}


	/*--------------------------------------------------------------
	----------------------------------------------------------------
	----------------------------------------------------------------
	--------------------------------------------------------------*/
	private int[] getAvailableTiles()
	{
		int rackNum = rack.getSize();
		int numTiles;
		int availIndex = 0;

		/*startingBoard = CanvasPanel.getBoard();
		for(int i=0; i<boardHeight; i++)
		{
			for(int j=0; j<boardWidth; j++)
			{
				manipBoard[i][j] = startingBoard[i][j];
				if(startingBoard[i][j]!=-1)
				{
					boardTiles[numOnBoard] = startingBoard[i][j];
					numOnBoard++;
				}
			}
		}

		numTiles = numOnBoard + rackNum;
		int availableTiles[] = new int[numTiles];

		// set up array of all available tiles...
		for(int i=0; i<boardHeight; i++)
		{
			for(int j=0; j<boardWidth; j++)
			{
				if(manipBoard[i][j]!=-1)
				{
					availableTiles[availIndex] = manipBoard[i][j];
					availIndex++;
				}
			}
		}

		System.out.println("Computer's rack");
		for(int i=0; i<rackNum; i++)
		{
			availableTiles[availIndex] = rack.getTileNumber(i);
			System.out.println(CompleteTileSet.getTile(availableTiles[availIndex]).getColour() + " " + CompleteTileSet.getTile(availableTiles[availIndex]).getValue());
			availIndex++;
		}*/
		
		int availableTiles[] = new int[104];

		for(int i=0; i<104; i++)
		{
			availableTiles[i] = i;
		}

		return availableTiles;
	}// END of getAvailableTiles

}// END of Computer class
