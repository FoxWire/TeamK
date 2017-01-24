/*
This class represents the user's data in the game. It extends the player class, and so has all the 
logic for managing the deck of cards etc. The only difference is that this class stores the user's 
choice of category.
*/

public class User extends Player{

	private int chosenCategory; 

	public User(){
		super(0);
	}
	// @Override
	public int getCategory(){
		return chosenCategory;
	}
	
	public void setChosenCategory(int chosenCategory){
	 	this.chosenCategory = chosenCategory;
	 }
} // close class