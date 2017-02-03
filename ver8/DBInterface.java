/*
 * This class has deals with the interaction with the database
 */

import java.sql.*;


public class DBInterface{

	private Connection connection = null;

	public boolean establishConnection(){

		String dbname = "top_trumps";
		String username = "postgres"; 
		String password = "kitten";

		try {
			connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
			dbname, username, password);
		} catch(SQLException ex){
			System.out.println("Connection Failed");
			ex.printStackTrace();
			return false;
		}

		if (connection != null){
			System.out.println("Connection Successful");
			return true;
		} else {
			System.out.println("Connection Failed");
			return false;
		}
	} // close establishConnection


	public void closeConnection(){
		try{
			connection.close();
			System.out.println("Connection closed");
		} catch (SQLException ex){
			ex.printStackTrace();
			System.out.println("Connection could not be closed - SQL Exception");
		}
	}


	public void insertResults(int gameID, int draws, String winner, int rounds, 
							int playerWins, int comp1, int comp2, int comp3, 
							int comp4){

		String query = 
		String.format("INSERT INTO Game VALUES (%d, %d, %s, %d, %d, %d, %d, %d, %d)", 
		gameID, draws, winner, rounds, playerWins, comp1, comp2, comp3, comp4);

		Statement statement;
		ResultSet resultSet = null;
		try{
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex){
			ex.printStackTrace();
			System.out.println("Some kind of error somewhere");
		}
	} 
	


	// /*
	//  * This methods gets a list of course names and their corresponding ids 
	//  * and puts them into records, a list of which is returned
	//  * @ return - an array of Records
	//  */
	// public Record[] getCourseList(){
	// 	String query = "SELECT name, courseid FROM course";
	// 	Record[] returnArray = {};

	// 	ResultSet resultSet =  executeQuery(query);
	// 	try {
	// 		while(resultSet.next()){
	// 			String courseName = resultSet.getString("name");
	// 			String courseid = resultSet.getString("courseid");
	// 			Record rec = new Record(courseid, courseName);
	// 			returnArray = addToArray(returnArray, rec);
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	} 

	// 	return returnArray;
	// }

	// /*
	//  * This method gets a list of the members first and second names along with 
	//  * their corresponding ids and puts them into records.
	//  * @return - an array of records
	//  */
	// public Record[] getMemberList(){
	// 	String query = "SELECT memberid, fname, lname FROM member";
	// 	String name;
	// 	String memberid;
	// 	Record[] returnArray = {};


	// 	ResultSet resultSet =  executeQuery(query);
	// 	try {
	// 		while (resultSet.next()){
			
	// 			// get the member id 
	// 			memberid = resultSet.getString("memberid");
	// 			// combine the first and last names into a string
	// 			name = resultSet.getString("fname") + " " +  resultSet.getString("lname");
	// 			// create a new record and add to the array.
	// 			Record rec = new Record(memberid, name);
	// 			returnArray = addToArray(returnArray, rec);
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}
	// 	return returnArray;
	// }

	// /*
	//  * The methods get an instructor name based on the name of a course
	//  * @oaram = String courseName
	//  * @return - String the instructors first and last name as one string
	//  */
	// public String getInstructorName(String courseName){

	// 	String query = String.format("SELECT i.fname, i.sname FROM instructor AS i " + 
	// 					"INNER JOIN course AS c ON c.instructorid = i.instructorid " +
	// 					"WHERE c.name = '%s'", courseName);

	// 	String returnString = "";


	// 	ResultSet resultSet = executeQuery(query);
	// 	try {
	// 		while (resultSet.next()){
	// 			returnString = resultSet.getString("fname") + " " + resultSet.getString("sname");			
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}

	// 	return returnString;
	// }
	

	// /*
	//  * This method count the number of bookings for a course name
	//  * @param - String the name of the course
	//  * @return - String the level of the course
	//  */
		
	// public String getCurrentCourseLevel(String courseName){

	// 	String query = String.format("SELECT COUNT(coursebooking.courseid) " +
	// 					"FROM coursebooking " +
	// 					"INNER JOIN course " +
	// 					"ON course.courseid = coursebooking.courseid " +
	// 					"WHERE course.name = '%s'", courseName);

	// 	String returnString = "";

	// 	ResultSet resultSet = executeQuery(query);
	// 	try {
	// 		while (resultSet.next()){
	// 			returnString = resultSet.getString("count");
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}
	// 	return returnString;
	// }


	// /*
	//  * This method gets the max course capacity based on the course name
	//  * @param - String name of the course
	//  * @return - String max capacity
	//  */
	
	// public String getMaxCapacity(String courseName){

	// 	String query = String.format("SELECT maxplaces FROM course " +
	// 	  				"WHERE name = '%s'", courseName);

	// 	String returnString = "";

	// 	ResultSet resultSet = executeQuery(query);
	// 	try {
	// 		while (resultSet.next()){
	// 			returnString = resultSet.getString("maxplaces");
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}

	// 	return returnString;

	// }
	

	// /*
	//  * This gets a list of the members on a course based on the course name
	//  * @param - String the name of the course 
	//  * @return - String[] the list of members names
	//  */
	// public String[] getMembersOnCourse(String courseName){
	// 	String query = String.format("SELECT m.memberid, m.fname, m.lname " +
	// 							"FROM coursebooking AS cb " +
	// 							"INNER JOIN course AS c " +
	// 							"ON c.courseid = cb.courseid " +
	// 							"INNER JOIN member AS m " +
	// 							"ON m.memberid = cb.memberid " +
	// 							"WHERE c.name = '%s'", courseName);
	// 	String fname;
	// 	String lname;
	// 	String memberid;
	// 	String[] returnArray = {};

	// 	ResultSet resultSet = executeQuery(query);
		
	// 	try {
	// 		while (resultSet.next()){
	// 			fname = resultSet.getString("fname");
	// 			lname = resultSet.getString("lname");
	// 			memberid = resultSet.getString("memberid");
	// 			String s = memberid + " " + fname + " " + lname;
	// 			returnArray = addToArray(returnArray, s); 	
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}
	// 	return returnArray;	
	// }


	// /*
	//  * This method will use the member id number to get the
	//  * members' telephone number
	//  * @param - String member id
	//  * @return - String contact number as a string
	//  */
	// public String getMemberContactNumber(String memberid){
	// 	String query = String.format("SELECT phonenumber FROM member WHERE memberid = '%s'", memberid); 
		
	// 	String returnString = "";
	// 	ResultSet resultSet = executeQuery(query);
	// 	try {
	// 		while (resultSet.next()){
	// 			returnString = resultSet.getString("phonenumber");
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}
	// 	return returnString;
	// }
	
	// /*
	//  * The method gets a list of the courses that a particular member 
	//  * is booked on.
	//  * @param - the memberid
	//  * @return - an array of course records
	//  */
	// public Record[] getCoursesOfMember(String memberid){

	// 	String query = String.format("SELECT c.courseid, c.name FROM course AS c " + 
	// 						"INNER JOIN coursebooking AS cb " +
	// 						"ON c.courseid = cb.courseid " +
	// 						"WHERE cb.memberid = '%s'", memberid);
	// 	Record[] returnArray = {};
	// 	ResultSet resultSet = executeQuery(query);
	// 	try {
	// 		while (resultSet.next()){
	// 			Record rec = new Record(resultSet.getString("courseid"), resultSet.getString("name"));
	// 			returnArray = addToArray(returnArray, rec);
	// 		}
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 	}
	// 	return returnArray;	
	// }
	
	// /*
	//  * This method enters a new booking into the database. Method is only used to
	//  * execute updates to the database.
	//  * @param - memeberid and courseid as strings
	//  * @return - boolean depending on exceptions being thrown
	//  */
	// public boolean makeBooking(String memberid, String courseid, String courseName){
		
	// 	// Run the first query to find out what the most recent booking number was.
	// 	int maxBookingNumber = 0;
	// 	String query1 = "SELECT MAX(bookingnumber) FROM coursebooking";
		
	// 	// Get the maximum course capacity and the current level
	// 	int currLevel = Integer.parseInt(getCurrentCourseLevel(courseName)); 
	// 	int maxCap = Integer.parseInt(getMaxCapacity(courseName));
		
	// 	// Check if the current level exceeds the max capacity
	// 	if (currLevel < maxCap){
	// 		ResultSet resultSet = executeQuery(query1);
	// 		try {
	// 			while (resultSet.next()){
	// 				maxBookingNumber = Integer.parseInt(resultSet.getString("max"));
	// 				// Increase by one to get the next booking number
	// 				maxBookingNumber++;
	// 			}
	// 		} catch (SQLException ex){
	// 			ex.printStackTrace();
	// 		}
		
	// 		// Enter the new booking row into the table
	// 		String query2 = String.format("INSERT INTO coursebooking VALUES(%s, %s, %s)", 
	// 				maxBookingNumber, memberid, courseid);
	// 		return executeUpdate(query2);
	// 	} else {
	// 		return false;
	// 	}
	// }
	

	// /*
	//  * This method is used to execute the queries that return a result set
	//  * @param - the query as a string 
	//  * @return - the result set
	//  */
	// private ResultSet executeQuery(String query){

	// 	Statement statement;
	// 	ResultSet resultSet = null;
	// 	try{
	// 		statement = connection.createStatement();
	// 		resultSet = statement.executeQuery(query);
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 		System.out.println("Some kind of error somewhere");		
	// 	}
	// 	 return resultSet;
	// } 
	
	
	
	// private boolean executeUpdate(String query){

	// 	Statement statement;
	// 	ResultSet resultSet = null;
	// 	try{
	// 		statement = connection.createStatement();
	// 		statement.executeUpdate(query);
	// 		return true;
	// 	} catch (SQLException ex){
	// 		ex.printStackTrace();
	// 		System.out.println("Some kind of error somewhere");
	// 		return false;		
	// 	}
	// } 
} // close class
