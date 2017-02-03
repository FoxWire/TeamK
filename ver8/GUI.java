import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class GUI extends JFrame {

	// This is the bold font used at several points in the gui
	private static final Font BOLD_FONT = new Font("Lucida Grande", Font.BOLD, 13);
	private static final int NUMBER_OF_CATEGORIES = 5;

	private boolean tickTock; // This boolean controls the two step display of the gui
	// These variables are needed to access the game and user data.
	private User user;
	private Game game;

	private int numberOfOpponents;
	private int totalPlayers;


	// Variables for section left one.
	private JLabel currentPlayerDisplay;
	private JLabel currentCategoryDisplay1;
	private JLabel currentCategoryDisplay2;
	private JLabel currentRoundDisplay;

	// Variables for section left two.	
	private JLabel cardName; // This is the name of the card ie 'TRex'.
	private JTextField[] categoryValues;
	private ButtonGroup buttonGroup;
	private JRadioButton[] radioButtons;
	private int userSelectedCategory;
	private JLabel selectLabel;

	// Variables for section left three 
	private JButton button;

	// Variables for section right one
	private JTextField[] cardDescriptions;
	private JTextField[] cardValues;
	private JLabel winningPlayer;

	// Variables for section right two
	private JTextField[] cardCountTextFields;

	// Variables for the dialog boxes
	private int initialOptionResult;
	private JButton databaseDisplayButton;
	private JDialog dia;

	// Variables menu dialog box
	JDialog menuDialog;
	JButton[] menuButtons;


	public GUI(){
		user = new User();
		game = new Game(user);
		menuDialog();
	} // close contstructor

	private void startNewGame(){
		getContentPane().removeAll();
		revalidate();
		repaint();

		numberOfOpponents = numberOfOpponentsDialog();
		totalPlayers = numberOfOpponents + 1;
		game.resetGame(numberOfOpponents);

		setLayout(new GridLayout(0, 2));	
		tickTock = true;
		buildPanels();
		setVisible(true);
		revalidate();
		repaint();
	} // close method

	/*
	Shows a dialog box asking for the number of computer players
	This should have a simple comb box
	*/

	private int numberOfOpponentsDialog(){
		String[] possibilities = {"4", "3", "2", "1"};
		String result = (String)JOptionPane.showInputDialog(null,
			"How many opponents do you want?",
           "Top Trumps",
           JOptionPane.CLOSED_OPTION,
           null,
           possibilities,
           "ham");

		if (result == null){
			return 4;
		} else {
			return Integer.parseInt(result);
		}
	} // close method

	/*
	Displays a label and a four buttons. This is displayed when the application is loaded and
	after each new game.
	*/
	private void menuDialog(){
		menuDialog = new JDialog();

		JPanel panel = new JPanel(new BorderLayout());
		JLabel menuLabel = new JLabel("What would you like to do?", SwingConstants.CENTER);
		panel.add(menuLabel, BorderLayout.CENTER);

		JPanel subpanel = new JPanel(new GridLayout(0, 4));
		ActionListener menuListener = new MenuListener();

		String[] buttonNames = {"Play game", "View game data", "Load to database", "Quit"};
		menuButtons = new JButton[buttonNames.length];

		for (int i = 0 ; i < buttonNames.length; i++){
			JButton menuButton = new JButton(buttonNames[i]);
			menuButton.addActionListener(menuListener);
			menuButtons[i] = menuButton;
			subpanel.add(menuButton);
		}

		panel.add(subpanel, BorderLayout.SOUTH);
		menuDialog.add(panel);
		menuDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		menuDialog.setTitle("Top Trumps: Menu");
		menuDialog.setSize(550, 150);
		menuDialog.setLocationRelativeTo(null);
		menuDialog.setVisible(true);
	} // close method

	/*
	This is what is displayed when the user asks to see the database
	*/
	private void buildDatabaseDisplay(){

		dia = new JDialog();
		int[] results = new int[5];
		results = game.readFromDatabase();


		JPanel panel = new JPanel(new BorderLayout());
		JPanel subpanel = new JPanel(new GridLayout(5, 0));
		JLabel label1 = new JLabel("Number of games played overall: " + results[0]);
		JLabel label2 = new JLabel("Number of computer wins: " + results[1]);
		JLabel label3 = new JLabel("Number of user wins: " + results[2]);
		JLabel label4 = new JLabel("Number of draws: " + results[3]);
		JLabel label5 = new JLabel("Most rounds in a single game: " + results[4]);
		subpanel.add(label1);
		subpanel.add(label2);
		subpanel.add(label3);
		subpanel.add(label4);
		subpanel.add(label5);


		panel.add(subpanel, BorderLayout.CENTER);
		
		JButton closeButton = new JButton("Return to menu");
		 closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				dia.dispose();
			}
		});
		panel.add(closeButton, BorderLayout.SOUTH);
		dia.add(panel);
		dia.setTitle("Game data");
		dia.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dia.setSize(300, 200);
		dia.setLocationRelativeTo(null);
		dia.setAutoRequestFocus(true);
		dia.toFront();
		dia.setVisible(true);
	}


	private void buildPanels(){
		// Build the left panel of the main game view
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 0));
		leftPanel.add(buildLeftOne());
		leftPanel.add(buildLeftTwo());
		leftPanel.add(buildLeftThree());
		add(leftPanel);

		// Build the right panel of the main game view
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(2, 0));
		rightPanel.add(buildRightOne());
		rightPanel.add(buildRightTwo());
		add(rightPanel);
	}

	private JPanel buildLeftOne(){

		JPanel leftOne = new JPanel(new GridLayout(3, 2));

		JLabel label = new JLabel("Current player: ");
		label.setFont(BOLD_FONT);
		leftOne.add(label);
		currentPlayerDisplay = new JLabel("");
		currentCategoryDisplay1 = new JLabel("");
		currentRoundDisplay = new JLabel("");
		leftOne.add(currentPlayerDisplay);

		JLabel label2 = new JLabel("Current category: ");
		label2.setFont(BOLD_FONT);
		leftOne.add(label2);
		leftOne.add(currentCategoryDisplay1);

		JLabel label3 = new JLabel("Current round: ");
		label3.setFont(BOLD_FONT);
		leftOne.add(label3);
		leftOne.add(currentRoundDisplay);
		
		leftOne.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 20));
		return leftOne;
	} // close method

	private JPanel buildLeftTwo(){
		JPanel leftTwo = new JPanel(new BorderLayout());

		// makes the second top left
		JPanel leftTwoSub = new JPanel(new GridLayout(0, 3));
		// in the second part goes the headings (also as labels)
		JPanel subpanel = new JPanel(new GridLayout(6, 0));
		JLabel label = new JLabel("Your card: ");
		label.setFont(BOLD_FONT);
		subpanel.add(label);

		// Add the headings, these don't change throughout the game.
		for (int i = 1 ; i < NUMBER_OF_CATEGORIES + 1; i++){
			subpanel.add(new JLabel(game.getCategoryHeadings()[i] + ":"));
		}

		leftTwoSub.add(subpanel);
		// in the third part goes the values
		JPanel subpanel2 = new JPanel(new GridLayout(6, 0));
		cardName = new JLabel("");
		cardName.setFont(BOLD_FONT);
		subpanel2.add(cardName);

		categoryValues = new JTextField[5];
		for (int i = 0; i < 5; i++){
			JTextField categoryValue = new JTextField();
			categoryValue.setEditable(false);
			categoryValue.setText("");
			categoryValues[i] = categoryValue;
			subpanel2.add(categoryValue);
		}

		leftTwoSub.add(subpanel2);

		// the radios at the end
		JPanel subpanel3 = new JPanel(new GridLayout(6, 0));
		buttonGroup = new ButtonGroup();
		radioButtons = new JRadioButton[NUMBER_OF_CATEGORIES];

		selectLabel = new JLabel("");
		subpanel3.add(selectLabel);

		// Create the radio buttons
		for (int i = 0; i < NUMBER_OF_CATEGORIES; i++){
			JRadioButton radioButton = new JRadioButton();
			radioButton.addActionListener(new RadioButtonListener());
			radioButton.setEnabled(false);
			buttonGroup.add(radioButton);
			subpanel3.add(radioButton);
			radioButtons[i] = radioButton;
		}

		leftTwoSub.add(subpanel3);

		leftTwo.add(leftTwoSub, BorderLayout.CENTER);
		leftTwo.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 20));

		return leftTwo;
	} // close method

	private JPanel buildLeftThree(){
		JPanel leftThree = new JPanel(new BorderLayout());
		leftThree.setBorder(BorderFactory.createEmptyBorder(35, 20, 35, 20));
		button = new JButton("Start game");
		button.addActionListener(new ButtonListener());
		leftThree.add(button, BorderLayout.CENTER);
		return leftThree;
	} // close method

	/*
	Builds the top panel on the right.
	*/
	private JPanel buildRightOne(){

		JPanel rightOne = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Cards from last round: ");
		label.setFont(BOLD_FONT);

		cardDescriptions = new JTextField[5];
		cardValues = new JTextField[5];

		// make a subpanel here 
		JPanel subpanel = new JPanel(new GridLayout(totalPlayers + 1, 3));
		subpanel.add(new JLabel());
		subpanel.add(new JLabel("description"));
		currentCategoryDisplay2 = new JLabel();
		subpanel.add(currentCategoryDisplay2);

		// Create the text fields
		for (int i = 0; i < totalPlayers; i++){
			subpanel.add(new JLabel((i == 0) ? "You: " : "Player " + i));
			JTextField playerDescription = new JTextField();
			playerDescription.setEditable(false);
			cardDescriptions[i] = playerDescription;
			subpanel.add(playerDescription);

			JTextField playerValue = new JTextField();
			playerValue.setEditable(false);
			cardValues[i] = playerValue;
			subpanel.add(playerValue);
		}

		JPanel subpanel2 = new JPanel(new GridLayout(0, 3));
		subpanel2.add(new JLabel("Winner: "));
		winningPlayer = new JLabel("");
		winningPlayer.setText("");
		winningPlayer.setFont(BOLD_FONT);
		subpanel2.add(winningPlayer);
		subpanel2.add(new JPanel());

		rightOne.add(label, BorderLayout.NORTH);
		rightOne.add(subpanel, BorderLayout.CENTER);
		rightOne.add(subpanel2, BorderLayout.SOUTH);
		rightOne.setBorder(BorderFactory.createEmptyBorder(35, 20, 15, 20));

		return rightOne;
	} // close method

	private JPanel buildRightTwo(){

		cardCountTextFields = new JTextField[6];
		// the second panel on the right
		JPanel rightTwo = new JPanel(new BorderLayout());
		JLabel label2 = new JLabel("Card counts: ");
		label2.setFont(BOLD_FONT);

		JPanel subpanel2 = new JPanel(new GridLayout(totalPlayers + 1, 2));

		// get the card counts
		int[] playerCardCounts = game.getPlayerCardCounts();

		for (int i = 0; i < totalPlayers + 1; i ++){
			String name;
			if (i == 0){
				name = "You: ";
			} else if (i == totalPlayers){
				name = "Communal deck: ";
			} else {
				name = "Player " + i + ":";
			}
			subpanel2.add(new Label(name));
			String content;
			if (i == totalPlayers){
				content = "" + game.getCommunalDeckCardCount();
			} else {
				content = "" + playerCardCounts[i];
			}
			JTextField textField = new JTextField(content);
			textField.setEditable(false);
			cardCountTextFields[i] = textField;
			subpanel2.add(textField);

		}

		rightTwo.add(label2, BorderLayout.NORTH);
		rightTwo.add(subpanel2, BorderLayout.CENTER);
		rightTwo.setBorder(BorderFactory.createEmptyBorder(15, 20, 35, 20));

		return rightTwo;
	} // close method

	private void updateOnTick(){
		// Update the current player display
		currentPlayerDisplay.setText((game.getCurrentPlayer() == user) ? "You" : "" + game.getCurrentPlayer());

		// update the display for the two current category labels
		currentCategoryDisplay2.setText("");
		currentCategoryDisplay1.setText(game.getSelectedCategory());

		// update current round display
		currentRoundDisplay.setText("Round " + game.getCurrentRound());

		// update the winning player label
		winningPlayer.setText("");	

		if (game.getCurrentPlayer() == user){

			currentCategoryDisplay1.setText("");
			// Change the button label
			button.setText("Select category");
			// Active the label for select
			selectLabel.setText("Select");

			// Enable the radio buttons
			for (int i = 0; i < NUMBER_OF_CATEGORIES; i++){
				radioButtons[i].setEnabled(true);
			}

		} else {
			button.setText("Continue");
			selectLabel.setText("");

		}

		// update own top card
		if(user.stillInGame()){

			// update the card title (dino name of own card)
			cardName.setText(user.peekCard().getDescription());

			int[] values = user.peekCard().getValues();
			for (int i = 0; i < 5; i++){
				categoryValues[i].setText("" + values[i]);
			} 
		} else {
			for (int i = 0; i < 5; i++){
				categoryValues[i].setText("");
			}
			cardName.setText("");
		}
		
		// Remove the data from last round on tick.	
		for (int i = 0; i < totalPlayers; i++){
			cardDescriptions[i].setText("");
			cardValues[i].setText("");
		}

		// update the button text
		if (game.getCurrentPlayer() == user && user.getCategory() == -1){
				button.setText("Select category");
		} else {
			button.setText("Continue");
		}

	} // close method

	private void updateOnTock(){

		//update the users selection for the category
		user.setChosenCategory(userSelectedCategory);

		// update the current category label
		currentCategoryDisplay2.setText(currentCategoryDisplay1.getText());
		
		//update cards names from last round
		String[] descriptions = game.getDescriptions();
		for (int i = 0; i < totalPlayers; i++){
			cardDescriptions[i].setText(descriptions[i]);
		}

		// update card values from last round
		int[] values = game.getCardValues();
		for (int i = 0; i < totalPlayers; i++){
			cardValues[i].setText((values[i] > 0)? "" + values[i]: "");
		}

		//update winning player
		String winnerName;
		if (game.getWinnerOfRound() == user){
			winnerName = "You";
		} else if (game.getDrawLastRound()){
			winnerName = "Draw";
		} else {
			winnerName = "" + game.getWinnerOfRound();
		}
		winningPlayer.setText(winnerName);	
		
		// update the card counts
		int[] cardCounts = game.getPlayerCardCounts();
		for (int i = 0; i < totalPlayers; i++){
			if (i == totalPlayers + 1){
				cardCountTextFields[i].setText("" + game.getCommunalDeckCardCount());
			} else {
				cardCountTextFields[i].setText("" + cardCounts[i]);
			}
		}

		// update the button text
		button.setText("Next round");

		// disable the radio buttons
		for (int i = 0; i < NUMBER_OF_CATEGORIES; i++){
			radioButtons[i].setEnabled(false);
		}
		buttonGroup.clearSelection();
		user.setChosenCategory(-1);
	} // close method

	/*
	When users turn, checks if the user has made a selection 
	and displays a warning if not.
	*/
	private boolean madeSelection(){

		// check if the user has selected a category
		for (int i = 0; i < radioButtons.length; i++){
			if (radioButtons[i].isSelected())
				user.setChosenCategory(i);
		}
		// If no category selected, display warning
		if (game.getCurrentPlayer() == user && user.getCategory() == -1){
			JOptionPane.showMessageDialog(null,
    			"You have not yet selected a category.",
   				 "Selection Error",
    			JOptionPane.ERROR_MESSAGE);
			return false;
		} else return true;
	} // close method

	class ButtonListener implements ActionListener{
		public void actionPerformed (ActionEvent event){
			if (tickTock){
				updateOnTick();
				tickTock = !tickTock;
			} else {
				// if they haven't selected anything and it is their turn 
				if (!madeSelection()){
					updateOnTick();
				} else {
					game.runGame();
					updateOnTock();	
					tickTock = !tickTock;	
				}				
			}
		
			if (game.gameOver()) menuDialog();	
		}
	} // close inner class

	class RadioButtonListener implements ActionListener{
		public void actionPerformed (ActionEvent event){
			userSelectedCategory = 0;
			for (int i = 0; i < radioButtons.length; i++){
				if (radioButtons[i] == event.getSource()){
					userSelectedCategory = i;
				}
			}
			currentCategoryDisplay1.setText(game.getCategoryHeadings()[userSelectedCategory + 1]);
			button.setText("Submit category");
		}
	} // close inner class

	class MenuListener implements ActionListener{
		public void actionPerformed (ActionEvent event){
			if (event.getSource() == menuButtons[0]){
				// play game
				menuDialog.dispose();
				startNewGame();	
			} else if (event.getSource() == menuButtons[1]){
				// view data
				buildDatabaseDisplay();
			} else if (event.getSource() == menuButtons[2]){
				// load to database
				game.writeToDatabase();
			} else System.exit(0);
		}
	} // close inner class

} // close GUI