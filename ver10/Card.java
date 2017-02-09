/*
This holds the data and methods for each card in the game.
*/

import java.util.Arrays;

public class Card{

	private String description;
	private int[] values;
	private int highestCategory; // The index of the category with the highest value.
	
	/*
	 * The constructor takes a string that is read in (by the game class)
	 * from the .txt file. 
	 */
	public Card(String valString){

		// Split the string
		String[] temp = valString.split(" ");
		// Get the description
		description = temp[0];

		// Put the value into a new array.
		int[] array = new int[temp.length - 1];

		for (int i = 0; i < temp.length - 1; i++){
			array[i] = Integer.parseInt(temp[i + 1]);
		}
		values = array;	

		calculateHighestCategory();
	}

	/*
	 * Finds the index of the highest value on the card
	 */
	private void calculateHighestCategory(){
		int highestValue = 0;
		for (int i = 0; i < values.length; i++){
			if (values[i] > highestValue){
				highestValue = values[i];
				highestCategory = i;
			}
		}
	}

	public String getDescription(){
		return description;
	}
	/*
	* Returns the integer value of a category in the card. 
	* The category is specified by i which is the index. 
	* ie 0 would return the value for 'height'for this card.
	*/
	public int getValueAt(int i){
		return values[i];
	}

	public int[] getValues(){
		return values;
	}
	/*
	* Returns the index of the highest category, not the value of the category.
	*/
	public int getHighestCategory(){
		return highestCategory;
	}

	public String toString(){
		return description + " " + Arrays.toString(values);
	}
} // close class