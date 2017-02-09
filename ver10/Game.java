/*
 * This is the main part of the model in the MVC.
 * It controls most of the game logic.
 */

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Random;

public class Game{

	public final static int MAX_DECK_SIZE = 40;
	private int totalPlayers; // this does not change once the game has started.
	private int numberOfPlayers; // keeps track of active players in the game, needed to know if game over
	private int currentRound;
	private int numberOfDraws;
	private int winnerOfGame;

	private User user;
	private DBInterface database;

	private Player[] players;
	private Card[] communalDeck;
	private int communalDeckCount;
	
	private Card[] cacheDeck;		// means the deck only needs to be loaded once
	private int cacheDeckCount;

	private Card[] auxDeck;	// deck used to store the cards for each round
	private Card[] cardsFromLastRound; // saves the cards from the last round for the display
	private String[] categoryHeadings;

	private int currentPlayerIndex; // The index of the player whose turn it is.
	private int selectedCategory;
	private Player currentPlayer;
	private int winnerIndex;
	private Player winnerOfRound;
	
	private Random rand;

	public Game(User user){
		this.user = user;
		rand = new Random();
		database= new DBInterface();
		communalDeck = new Card[MAX_DECK_SIZE];
		cacheDeck = new Card[MAX_DECK_SIZE];	
	}

	/*
	 * This method is called each time a new game is started.
	 * It avoid having to recreate a new game object each time. 
	 */
	public void resetGame(int numberOfOpponents){
		totalPlayers = numberOfOpponents + 1;
		numberOfPlayers = numberOfOpponents + 1;
		numberOfDraws = 0;
		winnerOfGame = 0;
		
		// Make sure the user has no cards left over from last game
		while (user.getCardCount() > 0){
			user.getTopCard();
		}

		players = new Player[totalPlayers];
		
		cardsFromLastRound = new Card[totalPlayers];
		currentRound = 0;
		numberOfDraws = 0;
		winnerOfRound = null;

		createPlayers();
		createDeck();
		shuffleDeck();
		dealDeck();

		currentPlayerIndex = rand.nextInt(numberOfPlayers);
		currentPlayer = players[currentPlayerIndex];
		selectedCategory = currentPlayer.getCategory();
		currentPlayer.setWonLastRound(true);
	} // close method 

	/*
	 * Reads the data from the text file when the program starts and 
	 * creates the cards. Otherwise just copies the cards from the 
	 * cache deck.
	 */
	private void createDeck(){
		
		if (cacheDeckCount == 0){ // The deck has not been loaded yet
			Scanner scan;
			try {
				System.out.println("loading deck");
				scan = new Scanner(new FileReader("/Users/stuartmiller/java/workspaces/Test/src/deck.txt"));
				categoryHeadings = scan.nextLine().split(" ");
		
				for (int i = 0; scan.hasNextLine(); i++){
					cacheDeck[i] = new Card(scan.nextLine());
					cacheDeckCount++;	
				}	
				scan.close();
			} catch (FileNotFoundException ex){ ex.printStackTrace(); }
	
		} 
		
		// Copy the cache to the communal deck, to reset the deck for each new game.
		for (int i = 0; i < MAX_DECK_SIZE; i++){
			communalDeck[i] = cacheDeck[i];
			communalDeckCount++;
		}
		
		// For Testing:
		System.out.println("The contents of the complete deck after"
				+ " being read in:");
		Game.printDeck(communalDeck);
	
	} // close method
	
	
	private void createPlayers(){
		players[0] = user;
		for (int i = 1; i < numberOfPlayers; i++){
			players[i] = new Player(i);
		}
	} // close method

	
	private void shuffleDeck(){
		// Create a new array of cards.
		Card[] shuffledDeck = new Card[communalDeckCount];
		int r = 0;
		// For each card in the original array, pick new random
		// position in the new array. If this new position is
		// occupied, cycle through to find the first free space.
		for (int i = 0; i < communalDeckCount; i++){
			r = rand.nextInt(communalDeckCount);
			while (shuffledDeck[r] != null){
				r++;
				r %= communalDeckCount;
			}
			shuffledDeck[r] = communalDeck[i];
		}
		communalDeck = shuffledDeck;
		
		// For testing:
		System.out.println("The contents of the deck after"
				+ " shuffling:"); 
		Game.printDeck(communalDeck);
		
	} // close method

	
	private void dealDeck(){
		for (int i = 0; i < MAX_DECK_SIZE; i++){
			players[i % numberOfPlayers].addCard(communalDeck[i]);

			// Make sure the communal deck is empty after dealing cards
			communalDeck[i] = null;
			communalDeckCount--;
		}
		
		// For testing:
		System.out.println("The contents of the players decks"
				+ " when they have been allocated:");
		System.out.println("_________________________");
		for (int i = 0; i < players.length; i++){
			System.out.println("The contents of " + players[i] +"'s deck:");
			players[i].printDeck();
		}
	} // close method


	/*
	 * Advances the game into the first half of the round.
	 */
	public void runTick(){
		if (!gameOver()){
			currentRound++;		
			currentPlayer = getNextPlayer(currentPlayer);
			selectedCategory = currentPlayer.getCategory();
		}	
	} // close method

	/*
	 * Runs the logic for the second half of the round.
	 */
	public void runTock(){
		
		winnerOfRound = null;

		// Get the users chosen category.
		if (currentPlayer == user){
			selectedCategory = currentPlayer.getCategory();
		}

		// Get the cards from the all players and put them in auxDeck
		// AuxDeck does contain some nulls.
		auxDeck = new Card[totalPlayers]; // do i need this here?
		for (int i = 0; i < totalPlayers; i++){
			if (players[i].stillInGame()){
				auxDeck[i] = players[i].getTopCard();
			}
		}
		
		// For Testing:
		System.out.println("The current cards in play:");
		Game.printDeck(auxDeck);
		
		// Find the two highest values and the index (in the players array) 
		// of the winner. The two highest values are used to determine if there 
		// is a draw.
		int highest = 0;
		int second = 0;
		int val = 0;
		winnerIndex = 0;
		for (int i = 0; i < auxDeck.length; i++){
			if (auxDeck[i] != null){
				val = auxDeck[i].getValueAt(selectedCategory);
				if (val >= highest){
					second = highest;
					highest = val;										
					winnerIndex = i;
				}
			}
		}

		//winnerOfRound = players[winnerIndex];

		// Put auxDeck into cards from last round
		for (int i = 0; i < cardsFromLastRound.length; i++){
			cardsFromLastRound[i] = auxDeck[i];
		}

		// Decide who gets the cards after the first round.
		if (highest == second){ // Last round was a draw
			numberOfDraws++;
			for (int i = 0; i < auxDeck.length; i++){
				// Put the cards from the round in the communal deck
				if (auxDeck[i] != null){
					communalDeck[communalDeckCount] = auxDeck[i];
					communalDeckCount++;
				}
			}
			
			// For testing:
			System.out.println("The contents of the communal pile when cards added:");
			Game.printDeck(communalDeck);
			
		} else {
			winnerOfRound = players[winnerIndex];
			// If there is a clear winner, he gets the cards from the auxDeck ...
			players[winnerIndex].incrementRoundsWon();
			for (int i = 0; i < auxDeck.length; i++){
				if (auxDeck[i] != null){
					players[winnerIndex].addCard(auxDeck[i]);
					
				}
			}
			// ... along with anything in the communal deck.
			int x = communalDeckCount;
			for (int i = 0; i < x; i++){
				players[winnerIndex].addCard(communalDeck[i]);
				communalDeckCount--;
			}
			
			// For testing:
			System.out.println("The contents of the communal pile when cards removed:");
			// Note: cards are not actually removed from the communal deck:
			// the communalDeckCount is decremented and the old cards are 
			// overwritten when new cards are added.
			Game.printDeck(communalDeck);
		}
		
		
		
		
		// Loop over all players and update who won the last round.
		for (int i = 0 ; i < players.length; i++){
			players[i].setWonLastRound((players[i] == winnerOfRound));
		}
		
		
		// Check if any players have no cards left.
		updateNumberOfPlayers();
	} // close method

	/*
	Works out who the next player should be.
	*/
	public Player getNextPlayer(Player previousPlayer){
	
		//Check if the previous player won the last round
		//Then loop through the other players in the list
		//until we find a non-null, return this.
		
		Player nextPlayer = null;
		while(nextPlayer == null){
			if (previousPlayer.getWonLastRound()){
				nextPlayer = previousPlayer;
			} else {
				currentPlayerIndex++;
				Player player = players[currentPlayerIndex % totalPlayers];
				if (player.stillInGame()){
					nextPlayer = player;
				} 
			}
		}
		return nextPlayer;
	} // close method

	
	public boolean gameOver(){
		
		if (numberOfPlayers < 2 || currentRound > 5){ // game ends after 5 rounds
		//if (numberOfPlayers < 2){                  //unlimited rounds
			
			// If the game is over, find the winning player
			int maxCount = 0;
			int index = 0;
			for (int i = 0; i < totalPlayers; i++){
				int count = players[i].getCardCount();
				if (count > maxCount){
					maxCount = count;																												// doesn't check every time.
					index = i;
				}
			}
			// now index should be the index of the winning player
			winnerOfGame = index;
			return true;
		} else { return false; }
	} // close method
	
	/*
	 * Finds out how many players are still in the game
	 * i.e. have not been knocked out.
	 */
	public void updateNumberOfPlayers(){
		int counter = 0;
		for (int i = 0 ; i < players.length; i++){
			if (players[i].stillInGame()){
				counter++;
			}
		}
		numberOfPlayers = counter;
	} // close method

	public Player getCurrentPlayer(){
		return currentPlayer;
	} 

	public String getSelectedCategory(){
		return categoryHeadings[selectedCategory + 1];
	}

	public Player getWinnerOfRound(){
		return winnerOfRound;
	}
	
	public Player getWinnerOfGame(){
		return players[winnerOfGame];
	}

	public Card[] getCardsFromRound(){
		// maybe empty the contents of the array at this point?
		return cardsFromLastRound;
	}

	// returns the names of the cards in an array
	public String[] getDescriptions(){
		String[] s = new String[totalPlayers];
		for (int i = 0; i < totalPlayers; i++){
			if (cardsFromLastRound[i] == null){
				s[i] = "";
			} else {
				s[i] = cardsFromLastRound[i].getDescription();
			}
		}
		return s;
	} // close method 

	/*
	 * Gets an array of values for the cards from the 
	 * last round for the selected category.
	 */
	public int[] getCardValues(){
		int[] values = new int[totalPlayers];
		for (int i = 0; i < totalPlayers; i++){
			if (cardsFromLastRound[i] == null){
				values[i] = 0;
			} else {
				values[i] = cardsFromLastRound[i].getValueAt(selectedCategory);
			}
		}
		return values;
	}

	/*
	 * Returns an array of the computer players' card counts
	 */
	public int[] getPlayerCardCounts(){
		int[] temp = new int[totalPlayers];
		for (int i = 0; i < totalPlayers; i++){
			temp[i] = players[i].getCardCount();
		}
		return temp;
	}

	public int getCommunalDeckCardCount(){
		return communalDeckCount;
	}

	public String[] getPlayerNames(){
		String[] temp = new String[players.length];
		for (int i = 0; i < players.length; i++){
			temp[i] = players[i].toString();
		}
		return temp;
	}

	public String[] getCategoryHeadings(){
		return categoryHeadings;
	}

	public int getCurrentRound(){
		return currentRound;
	}

	public boolean getDrawLastRound(){
		return communalDeckCount > 0;
	}

	public boolean writeToDatabase(){
		
		// Check if a game has been played
		if (winnerOfRound == null && numberOfDraws == 0){ return false; }
	
		int[] data = new int[7];
		data[0]= numberOfDraws;
		data[1] = currentRound - 1;
		
		// Get the number of rounds won for each player
		for (int i = 0; i < totalPlayers; i++){
			data[i + 2] = players[i].getRoundsWon();
		}	
		database.insertResults("" + players[winnerOfGame], data);
		
		// Make sure that the same game data cannot 
		// be written more that once to the database
		winnerOfRound = null;
		numberOfDraws = 0;
		
		return true;
	} // close method

	public int[] readFromDatabase(){
		return database.getOverallStatistics();
	} // close method
	
	
	public static void printDeck(Card[] deck){
		System.out.println("____________________________");
		for (int i = 0; i < deck.length; i++){
			System.out.println(deck[i]);
		}
		System.out.println("____________________________");
	}

} //close class