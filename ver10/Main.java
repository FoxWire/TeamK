import javax.swing.*;

class Main{
	public static void main(String[] args){
		JFrame frame = new GUI();

		frame.setSize(700, 400);
		frame.setTitle("Top Trumps");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setAutoRequestFocus(false);
		frame.setVisible(false);
		frame.toBack();
	} // close main method 
} // close main 