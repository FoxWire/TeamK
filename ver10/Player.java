/*
 * This class represents the computer players in the game.
 * The class holds a deck of cards and the methods needed to 
 * manage the deck. The user class extends it.
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
 
		deck[bottomDeck % MAX_DECK_SIZE] = newCard;
		bottomDeck ++;
	}

	/*
	 * Returns the top card without actually removing it 
	 * from the deck.
	 */
	public Card peekCard(){
		return deck[topDeck % MAX_DECK_SIZE];		
	}

	/*
	 * Removes and returns the top card on the deck.
	 */
	public Card getTopCard(){
		topDeck++;
		return deck[(topDeck - 1) % MAX_DECK_SIZE];
	}

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
	 * This returns the index of the highest category for the 
	 * top card, not the card itself. The computer players 
	 * automatically select the highest possible value.
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
		
		System.out.println("_________________________");
	}

	public String toString(){
		return "Player " + playerId;
	}
} // close class