/*
This will be replaced by the GUI in the final version. At the moment the ui class needs
a reference to the user and the game, so that it can retreive data from both. Once we see how everything 
works together with the gui we can see if it is feasible to get all the data to and from the gui via 
only the game reference. This would be a lot cleaner than having two references.

As it stands at the moment, any gui that we want to use with the game has to have the game and 
user references and the methods to set them.

We probably won't need the runUi() method whenwe have the full gui.

Bye the way, player 0 is the user. To enter the catogory enter the numbers 
0 - 4 to correspond to the categories: height weight length ferocity intelligence.
*/

import java.util.Scanner;
import java.util.Arrays;

public class UI{

	private User user;
	private Game game;

	public UI(){
		// Make an new instance of user.
		user = new User();
		// Both the Game and the UI instances have a reference to user.
		game = new Game(user);
		runUi();
	}

	public void runUi(){
		
		game.resetGame(4);

		while(!game.gameOver()){
			Scanner scan = new Scanner(System.in);
			System.out.println("----------------------------------");
			System.out.println("Press enter to continue: ");
			scan.nextLine();
			game.runTick();

			if (game.getCurrentPlayer() == user){
				System.out.println("It is currently your turn.");
				System.out.println("Round: " + game.getCurrentRound());
				System.out.println("Here is your card: ");
				System.out.println(Arrays.toString(game.getCategoryHeadings()));
				System.out.println(user.peekCard());
				System.out.println("Enter the int of the cateogory: ");
				user.setChosenCategory(scan.nextInt());
			} else {
				System.out.println("It is currently " + game.getCurrentPlayer() + "'s turn");
				System.out.println("Round: " + game.getCurrentRound());
				System.out.println("The current catetogory for the round is: " + game.getSelectedCategory());
				System.out.println();
				System.out.println("Here is your card: ");
				System.out.println(Arrays.toString(game.getCategoryHeadings()));
				System.out.println(user.peekCard());
				System.out.print("Press enter to submit your card...");
				scan.nextLine();
			}
			
			game.runTock();
		

			System.out.println(game.getWinnerOfRound() + " won the round");
			System.out.println("Here are the cards from the round: ");
			for (Card c: game.getCardsFromRound()){
				System.out.println("\t" + c);
			}

			System.out.println("Here are the current card counts for each player: ");
			String[] names = game.getPlayerNames();
			int[] cardCounts = game.getPlayerCardCounts();
			for (int i = 0; i < cardCounts.length; i++){
				System.out.println("\t" + names[i] + " has " + cardCounts[i] + " cards left");
			}
			System.out.println("Here is the card count for the communal deck: ");
			System.out.println("The communal deck contains " + game.getCommunalDeckCardCount() + " cards");
			System.out.println("----------------------------------");
		}

		System.out.println("Game over!");

	}
} // close class