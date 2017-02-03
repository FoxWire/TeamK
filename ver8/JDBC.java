import java.sql.*;
public class JDBC{

	private String DB_URL = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/m_16_2286354s";
	private String USER = "m_16_2286354s";
	private String PASS = "2286354s";
	private Connection conn = null;
	private Statement statement = null;

//this is how we connect to the database and then we can call this method in every following one 
//since it is static	
	public static void connect(){
		// try{
		// 	conn = DriverManager.getConnection(DB_URL, USER, PASS);
		// 	statement = conn.createStatement();
		// }catch (SQLException x){
		// 	System.out.println("Connection error");
		// }
	}
//this is the method when they want to output the results of a single game to the database
//we have to see how we will get the values	
//maybe we can have some getter in the gui or wherever we are recording stuff and then just say
//getID() rather than id
	public void insertResults(){
		// connect();
		// String sqlQuery = "insert into Game(gameID, numOfDraws, winner, numOfRounds, playerWins,
		// 					comp1Wins, comp2Wins, comp3Wins, comp4Wins) VALUES ("+id+","+draws+",
		// 					"+winner+","+rounds+","+playerWins+","+comp1+","+comp2+","+comp3+","+comp4+")"; 
		// ResultSet result = statement.executeQuery(sqlQuery); 
	}
//so this method is basically if the user wants to see the overall statistics of the game
//these are all the queries, I havent tested them but I think they should work since it is
//not something complicated
	public int[] overallStatistics(){
		// connect();
		// String sqlQuery = "select count(gameID) from Game";
		// ResultSet result = statement.executeQuery(sqlQuery);
		// System.out.println("Overall games played: " +result);

		// String sqlQuery2 = "select count(winner) from Game where winner<>Player";
		// ResultSet result = statement.executeQuery(sqlQuery2);
		// System.out.println("Computer has won "+result+" games");

		// String sqlQuery3 = "select count(winner) from Game where winner=Player";
		// ResultSet result = statement.executeQuery(sqlQuery3);
		// System.out.println("The player has won "+result+" games");

		// String sqlQuery4 = "select AVG(numOfDraws) from Game";
		// ResultSet result = statement.executeQuery(sqlQuery4);
		// System.out.println("Average number of draws: "+result);

		// String sqlQuery5 = "select MAX(numOfRounds) from Game";
		// ResultSet result = statement.executeQuery(sqlQuery5);
		// System.out.println("Largest number of rounds in a game: "+result);
		return new int[5];
	}
}