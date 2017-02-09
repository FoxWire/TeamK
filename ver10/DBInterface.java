/*
 * This class encapsulates the logic for the program to interface with 
 * the database.
 */
import java.sql.*;

public class DBInterface {
	
	private Connection connection = null;

	private boolean establishConnection(){
		
		String dbname = "";
		String username = ""; 
		String password = "";
		
		if (connection == null){
			try {
				connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
				dbname, username, password);
			} catch(SQLException ex){
				System.out.println("Connection Failed");
				ex.printStackTrace();
				return false;
			}
		}

		if (connection != null){
			System.out.println("Connection Successful");
			return true;
		} else {
			System.out.println("Connection Failed");
			return false;
		}
	} // close establish connection 
	
	
	public void closeConnection(){
		try{
			connection.close();
			connection = null;
			System.out.println("Connection closed");
		} catch (SQLException ex){
			ex.printStackTrace();
			System.out.println("Connection could not be closed - SQL Exception");
		}
	} // close close connection
	
	/*
	 * Inserts the results from each game into the database.
	 * @ param - the name of the winner as a string and the 
	 * remaining data as an array of ints
	 */
	public void insertResults(String winner, int[] data){
		
		establishConnection();
			
		int gameID = executeQuery("SELECT COUNT(gameid) FROM game", "count");
		gameID++;

		String query = 
		String.format("INSERT INTO Game (gameid, numofdraws, winner, numofrounds, playerwins,"
				+ " comp1wins, comp2wins, comp3wins, comp4wins) "
				+ "VALUES (%d, %d, '%s', %d, %d, %d, %d, %d, %d)", 
					gameID, data[0], winner, data[1], data[2], data[3], data[4], data[5], data[6]);
	
		Statement statement;
		try{
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex){
			ex.printStackTrace();
			System.out.println("Some kind of error somewhere");
		}
		
		closeConnection();
	} // close insert results 
	
	/*
	 * Runs the queries in the database.
	 * @return an int array of the query results
	 */
	public int[] getOverallStatistics(){
		
		establishConnection();
		
		int[] returnArray = new int[5];
		// get the number of games
		returnArray[0] = executeQuery("SELECT COUNT(gameid) FROM game", "count");
		
		// How many times the computer has won
		returnArray[1] = executeQuery("SELECT COUNT (winner) FROM Game WHERE winner<> 'you'", "count");
		
		//How many times the human has won
		returnArray[2] = executeQuery("SELECT COUNT(winner) FROM game WHERE winner='you'", "count");
		
		//The average number of draws
		returnArray[3] = executeQuery("SELECT AVG(numOfDraws) FROM game", "avg");
		
		//The largest number of rounds played in a single game
		returnArray[4] = executeQuery("SELECT MAX(numOfRounds) FROM game", "max");
		
		closeConnection();
		return returnArray;
		
	} // close overall statistics 
	
	/*
	 * Carries out the query.
	 * @param - the query and column name 
	 * as strings
	 */
	private int executeQuery(String sqlQuery, String column){
		
		Statement statement;
		ResultSet resultSet;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
			int count = 0;
			while (resultSet.next()){
				count = resultSet.getInt(column);
				return count;
			}		
		} catch (SQLException ex){
			ex.printStackTrace();
			System.out.println("Query failed");
		}	
		return 0;	
	} // close method
		
} // close class
