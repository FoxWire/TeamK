
/*
The class represents the computer players, the user class exends it.
The class is basically just an array and some methods to help implement the 
deck of cards as a queue.
*/

public class Player{

	public static final int MAX_DECK_SIZE = 40;

	private int playerId;
	private Card[] deck;
	private int topDeck;
	private int bottomDeck;
	private boolean wonLastRound;
	private int roundsWon;


	public Player(int playerId){
		this.playerId = playerId;
		topDeck = 0;
		bottomDeck = 0;
		deck = new Card[MAX_DECK_SIZE];
		wonLastRound = false;
		roundsWon = 0;
	}

	// Adds cards always at the bottom of the array.
	public void addCard(Card newCard){

		if (bottomDeck >= MAX_DECK_SIZE){ // implement the circular array
			deck[bottomDeck % MAX_DECK_SIZE] = newCard;
		} else {
			deck[bottomDeck] = newCard;	
		}
		bottomDeck ++;
	}

	/*
	Returns the top card without removing it from the players deck.
	This might be necessary for the user/ gui objects
	*/
	public Card peekCard(){
		return deck[topDeck % MAX_DECK_SIZE];		
	}

	/*
	Returns the top card and removes it from the deck.
	This will be called when the player submits his card 
	for the round to the game class.
	*/
	public Card getTopCard(){
		topDeck++;
		return deck[(topDeck - 1) % MAX_DECK_SIZE];
	}

	// Used to return the number of cards in the deck.
	public int getCardCount(){
		return bottomDeck - topDeck;
	}

	public boolean stillInGame(){
		return getCardCount() > 0;
	}

	public void setWonLastRound(boolean wonLastRound){
		this.wonLastRound = wonLastRound;
	}

	public boolean getWonLastRound(){
		return wonLastRound;
	}

	/*
	This returns the index of highest value for the top card, not the value itself
	*/
	public int getCategory(){
		return peekCard().getHighestCategory();
	}

	public void incrementRoundsWon(){
		roundsWon++;
	}

	public int getRoundsWon(){
		return roundsWon;
	}

	public void printDeck(){
		for (int i = topDeck; i < bottomDeck; i++){
			if (bottomDeck < MAX_DECK_SIZE){
				System.out.println(deck[i]);
			} else {
				System.out.println(deck[i % MAX_DECK_SIZE]);
			}
		}


	}

	public String toString(){
		return "Player " + playerId;
	}
} // close class