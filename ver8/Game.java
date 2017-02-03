/*
This was previously called the dealer. 
The class does a number of tasks:
- reads in card data from the .txt file
- creates the cards from the data.
- shuffles the deck.
- creates the players and deals the cards to them.
- controls the flow of the game. ie who is next, who has won the round etc
*/
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Arrays;


public class Game{

	public final static int MAX_DECK_SIZE = 40;
	private int totalPlayers; // this does not change once the game has started.
	private int numberOfPlayers; // this value changes as players are knocked out
	private int currentRound;
	private int numberOfDraws;

	private User user;
	private JDBC jdbc;

	private Player[] players;
	private Card[] communalDeck;
	private int communalDeckCount;

	private Card[] auxDeck;	// deck used to store the cards for each round
	private Card[] cardsFromLastRound; // saves the cards from the last round for ui display
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
		jdbc = new JDBC();
	}

	/*
	This method is called when a new game is started
	Avoids contructing a new game object for every new game.
	*/
	public void resetGame(int numberOfOpponents){
		totalPlayers = numberOfOpponents + 1;
		numberOfPlayers = numberOfOpponents + 1;
		numberOfDraws = 0;

		players = new Player[totalPlayers];
		communalDeck = new Card[MAX_DECK_SIZE];
		
		cardsFromLastRound = new Card[totalPlayers];
		currentRound = 1;

		createDeck(); // don't need to do this every time.
		createPlayers();
		shuffleDeck();
		dealDeck();

		currentPlayerIndex = rand.nextInt(numberOfPlayers);
		currentPlayer = players[currentPlayerIndex];
		selectedCategory = currentPlayer.getCategory();
		currentPlayer.setWonLastRound(true);
	}

	/*
	Read information in from the text file 
	and populate the communal deck
	*/
	private void createDeck(){
		
		Scanner scan;

		try {
			scan = new Scanner(new FileReader("deck.txt"));
			categoryHeadings = scan.nextLine().split(" ");

			for (int i = 0; scan.hasNextLine(); i++){
				communalDeck[i] = new Card(scan.nextLine());
				communalDeckCount++;
			}	
		} catch (FileNotFoundException ex){
			ex.printStackTrace();
		}
	} // close method

	/*
	Populates the array of players.
	*/
	private void createPlayers(){
		players[0] = user;
		for (int i = 1; i < numberOfPlayers; i++){
			players[i] = new Player(i);
		}
	} // close method

	
	private void shuffleDeck(){

		Card[] shuffledDeck = new Card[communalDeckCount];
		int r = 0;
		for (int i = 0; i < communalDeckCount; i++){
			r = rand.nextInt(communalDeckCount);
			while (shuffledDeck[r] != null){
				r++;
				r %= communalDeckCount;
			}
			shuffledDeck[r] = communalDeck[i];
		}
		communalDeck = shuffledDeck;

	} // close method

	/*
	Deals the cards out to the players
	*/
	private void dealDeck(){
		for (int i = 0; i < MAX_DECK_SIZE; i++){

			players[i % numberOfPlayers].addCard(communalDeck[i]);

			// make sure the master deck is empty after dealing cards
			communalDeck[i] = null;
			communalDeckCount--;
		}
	} // close method


	public void runGame(){

		currentRound++;
		
		// Get the chosen category of the current player
		selectedCategory = currentPlayer.getCategory();

		// Get the cards from the all players and put them in auxDeck
		// auxDeck does contain some nulls
		auxDeck = new Card[totalPlayers]; // do i need this here?
		for (int i = 0; i < totalPlayers; i++){
			if (players[i].stillInGame()){
				auxDeck[i] = players[i].getTopCard();
			}
		}
		// Find the two highest values and the index of the winner
		// The two highest values are used to determine if there is a draw
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

		/*
		Note: winnerIndex is the card index in aux deck and the index of the winner player
		in the players array
		*/

		winnerOfRound = players[winnerIndex];

		// put aux deck into card from last round
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
		} else {
			// If there is a clear winner, he gets the cards from the aux deck ...
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
		}

		// Check if current player won the last round and update accordingly
		currentPlayer.setWonLastRound((currentPlayer == winnerOfRound));

		// Check if any players have no cards left and remove them.
		updateNumberOfPlayers();

		// Move to the next player
		currentPlayer = getNextPlayer(currentPlayer);

		

	} // close method

	/*
	Works out who the next player should be.
	*/
	public Player getNextPlayer(Player previousPlayer){
		/*
		Check if the previous player won the last round
		Then loop through the other players in the list
		until we find a non-null, return this.
		*/

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

	// returns true when there is only one player left.
	public boolean gameOver(){
		return numberOfPlayers < 2 || currentRound > 5; // game ends after 5 rounds
		//return numberOfPlayers < 2; unlimited rounds
	}

	public void updateNumberOfPlayers(){
		int counter = 0;
		for (int i = 0 ; i < players.length; i++){
			if (players[i].stillInGame()){
				counter++;
			}
		}
		numberOfPlayers = counter;
	}

	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	public String getSelectedCategory(){
		return categoryHeadings[selectedCategory + 1];
	}

	public Player getWinnerOfRound(){
		return winnerOfRound;
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
	}

	// returns a array of the values for the current category
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

	// Returns an array of the computer players' card counts
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

	public void writeToDatabase(){
		// the number of draws 
		System.out.println("Number of draws in this game: " + numberOfDraws);

		//who won the game, get the player with the max cards
		int max = 0;
		int playerIndex = 0;
		for (int i = 0; i < players.length; i++){
			if (players[i].getCardCount() > max){
				max = players[i].getCardCount();
				playerIndex = i;
			}
		}
		System.out.println("The winning player is: " + players[playerIndex]);

		// how many rounds?
		System.out.println("number of rounds in this game: " + (currentRound - 1));

		//how many rounds did each player win?
		for (int i = 0; i < totalPlayers; i++){
			System.out.println("Player " + i + " won " + players[i].getRoundsWon() + "rounds in this game.");
		}
		/*
		This would be the call to write to the database. It's pretty messy at the moment, I'll try to 
		tidy it up though.

		jdbc.insertResults(10, numberOfDraws, "" + players[winnerIndex], 
							currentRound - 1, players[0].getRoundsWon(), 
							players[1].getRoundsWon(), players[2].getRoundsWon(), 
							players[3].getRoundsWon(), players[4].getRoundsWon());
		*/

	}


	public int[] readFromDatabase(){
		int[] results = jdbc.overallStatistics();
		return results;

	}

} //close class