// can a player object still give cards after it is empty?

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Arrays;

class test{
	public static void main(String[] args){

		String[] categoryHeadings;
		Card[] communalDeck = new Card[40];
		int communalDeckCount = 0;

		Scanner scan;
		Random rand = new Random();

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

		for (Card c: communalDeck){
			System.out.println(c);
		}

		//shuffle the deck 
		Card[] newDeck = new Card[communalDeckCount];
		int r = 0;
		for (int i = 0; i < communalDeckCount; i++){
			r = rand.nextInt(communalDeckCount);
			while (newDeck[r] != null){
				r++;
				if (r >= communalDeckCount){
					r %= communalDeckCount;
				}
			}
			newDeck[r] = communalDeck[i];
		}
		communalDeck = newDeck;



		System.out.println();

		for (Card c: communalDeck){
			System.out.println(c);
		}
		






	} // close method 
} // close class