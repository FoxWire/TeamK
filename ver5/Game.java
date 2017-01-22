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
	public int numberOfPlayers; // this value changes as players are knocked out

	User user;
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
		numberOfPlayers = 4;
		players = new Player[numberOfPlayers];
		communalDeck = new Card[MAX_DECK_SIZE];
		rand = new Random();
		cardsFromLastRound = new Card[numberOfPlayers];

		createDeck();
		createPlayers();
		shuffleDeck();
		dealDeck();

		currentPlayerIndex = rand.nextInt(numberOfPlayers);
		currentPlayer = players[currentPlayerIndex];
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
				if (r >= communalDeckCount){
					r %= communalDeckCount;
				}
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
			if (i < numberOfPlayers){
				players[i].addCard(communalDeck[i]);
			} else {
				players[i % numberOfPlayers].addCard(communalDeck[i]);
			}
			// make sure the master deck is empty after dealing cards
			communalDeck[i] = null;
			communalDeckCount--;
		}
	} // close method


	public void runGame(){
		
		// Get the chosen category of the current player
		selectedCategory = currentPlayer.getCategory();
		// Get the cards from the all players and put them in auxDeck
		auxDeck = new Card[numberOfPlayers]; // This needs to change size depending on num of players
		for (int i = 0; i < numberOfPlayers; i++){
			auxDeck[i] = players[i].getTopCard();
		}

		// Find the winning card for the selected category
		int highest = 0;
		int second = 0;
		int val = 0;
		winnerIndex = 0;
		for (int i = 0; i < auxDeck.length; i++){
			val = auxDeck[i].getValueAt(selectedCategory);
			if (val >= highest){
				second = highest;
				highest = val;
				winnerIndex = i;
			}
		}

		/*
		Note: winnerIndex is the card index in aux deck and the index of the winner player
		in the players array
		*/

		winnerOfRound = players[winnerIndex];
		cardsFromLastRound = auxDeck;

		// Decide who gets the cards after the first round.
		if (highest == second){ // Last round was a draw
			for (int i = 0; i < auxDeck.length; i++){
				// Put the cards from the round in the communal deck
				communalDeck[communalDeckCount] = auxDeck[i];
				communalDeckCount++;
			}
		} else {
			// If there is a clear winner, he gets the cards from the aux deck ...
			for (int i = 0; i < auxDeck.length; i++){
				players[winnerIndex].addCard(auxDeck[i]);
			}
			// ... and anything in the communal deck.
			int x = communalDeckCount;
			for (int i = 0; i < x; i++){
				players[winnerIndex].addCard(communalDeck[i]);
				communalDeckCount--;
			}
		}

		// Check if current player won the last round and update accordingly
		currentPlayer.setWonLastRound((currentPlayer == winnerOfRound));

		// Check if any players have no cards left and remove them.
		removePlayers();

		// Move to the next player
		currentPlayer = getNextPlayer(currentPlayer);

	} // close method

	/*
	Works out who the next player should be.
	*/
	public Player getNextPlayer(Player previousPlayer){
		/*
		Find out if the previous player won the last round.
		If so return the player, to play again.
		Else, advance the player turn number and return that player.
		*/
		if (previousPlayer.getWonLastRound()){
			return previousPlayer;
		} else {
			currentPlayerIndex++;
			if (currentPlayerIndex < players.length){
				return players[currentPlayerIndex];
			} else {
				return players[currentPlayerIndex % numberOfPlayers];
			}
		}
	} // close method

	// returns true when there is only one player left.
	public boolean gameOver(){
		return (players.length == 1) ? true : false;
	}

	private void removePlayers(){
		//find out how many players have no cards left
		int counter = 0;
		for (int i = 0; i < players.length; i++){
			if (players[i].getCardCount() == 0){
				counter++;
			}
		}

		// copy the players that are staying into a new array
		Player[] p = new Player[players.length - counter];
		int index = 0;
		for (int i = 0; i < players.length; i++){
			if (players[i].getCardCount() > 0){
				p[index] = players[i];
				index++;
			}
		}
		// the ones that are not continuing are abandoned for now.
		// We might need them later though.

		numberOfPlayers = players.length - counter;
		players = p;
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

	// Returns an array of the computer players' card counts
	public int[] getPlayerCardCounts(){
		int[] temp = new int[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++){
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
} //close class