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


	public Player(int playerId){
		this.playerId = playerId;
		topDeck = 0;
		bottomDeck = 0;
		deck = new Card[MAX_DECK_SIZE];
		wonLastRound = false;
	}

	// Adds cards always at the bottom of the array.
	public void addCard(Card newCard){
		if (getCardCount() < MAX_DECK_SIZE){

			if (bottomDeck >= MAX_DECK_SIZE){ // implement the circular array
				deck[bottomDeck % MAX_DECK_SIZE] = newCard;
			} else {
				deck[bottomDeck] = newCard;	
			}
			bottomDeck ++;
		}
	}

	/*
	Returns the top card without removing it from the players deck.
	This might be necessary for the user/ gui objects
	*/
	public Card peekCard(){
		if (topDeck < MAX_DECK_SIZE){
			return deck[topDeck];
		} else {
			return deck[topDeck % MAX_DECK_SIZE];
		}	
	}

	/*
	Returns the top card and removes it from the deck.
	This will be called when the player submits his card 
	for the round to the game class.
	*/
	public Card getTopCard(){
		if (topDeck < MAX_DECK_SIZE){
			topDeck ++;
			return deck[topDeck - 1];
		} else {
			topDeck++;
			return deck[(topDeck - 1) % MAX_DECK_SIZE];
		}
	}

	// Used to return the number of cards in the deck.
	public int getCardCount(){
		return bottomDeck - topDeck;
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

	public void printDeck(){
		for (int i = topDeck; i < getCardCount(); i++){
			System.out.println(deck[i].toString());
		}
	}

	public String toString(){
		return "Player " + playerId;
	}
} // close class