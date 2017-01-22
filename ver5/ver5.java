/*
This just does some intial set-up and starts the game 
*/

class ver5{
	public static void main(String[] args){

		User user = new User();
		// The game object has a reference to the user
		Game game = new Game(user);
		UI ui = new UI();

		// The ui has a reference to the user and to the games
		ui.setUser(user); // might not need this
		ui.setGame(game); 

		ui.runUi();

	} // close main method 

} // close main 










