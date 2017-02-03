/*
Main class just starts the game.
*/

import javax.swing.*;

class ver8{
	public static void main(String[] args){
		//UI ui = new UI();
		runGui();
	} // close main method 

	public static void runGui(){
		JFrame frame = new GUI();

		frame.setSize(700, 400);
		frame.setTitle("Top Trumps");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setAutoRequestFocus(false);
		frame.setVisible(false);
		frame.toBack();
	}
} // close main 
