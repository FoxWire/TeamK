/*
 * This class represents the user's data in the game.
 */

public class User extends Player{

	private int chosenCategory; 


	public User(){
		super(0);
		chosenCategory = -1; // This should signify that the user has nothing selected
	}

	public String getCardDescription(){
		return peekCard().getDescription();
	}

	// @Override
	public int getCategory(){
		return chosenCategory;
	}
	
	public void setChosenCategory(int chosenCategory){
	 	this.chosenCategory = chosenCategory;
	 }
	
	public String toString(){
		return "User";
	}
} // close class