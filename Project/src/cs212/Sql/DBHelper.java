package cs212.Sql;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mysql.jdbc.PreparedStatement;
public class DBHelper {
	
	static DBConfig dbconfig;
	
	public static void setUpConfig(){
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get("dbConfig.json"));
			JSONParser parser = new JSONParser();
			JSONObject object = (JSONObject) parser.parse(reader);
			String username = (String) object.get("username");
			String password = (String) object.get("password");
			String db = (String) object.get("db");
			String host = (String) object.get("host");
			String port = (String) object.get("port");

			dbconfig = new DBConfig(username, password, db, host, port);				
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * A helper method that returns a database connection.
	 * A calling method is responsible for closing the connection when finished.
	 * @param dbconfig
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {

		if(dbconfig==null){
			setUpConfig();
		}
		String username  = dbconfig.getUsername();
		String password  = dbconfig.getPassword();
		String db  = dbconfig.getDb();

		try {
			// load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
			System.exit(1);
		}

		String host = dbconfig.getHost();
		String port = dbconfig.getPort();
		String urlString = "jdbc:mysql://" + host + ":" + port + "/"+db;
		Connection con = DriverManager.getConnection(urlString,
				username,
				password);

		return con;
	}
	
	
	public static DBConfig getDBConfig(){
		return dbconfig;
	}
	
	
	
	/**
	 * method that adds a new user
	 * to the database
	 * @throws SQLException 
	 */
	public static void addUser(String name, String username, String password) throws SQLException{
		Connection con = null;
		
		try {
			con = getConnection();
			if(!tableExists(con, "user")){
				createTable("user");
			}
			PreparedStatement updateStmt = (PreparedStatement) con.prepareStatement("INSERT INTO user (name, username, password) VALUES (?, ?, ?)");
			updateStmt.setString(1, name);
			updateStmt.setString(2, username);
			updateStmt.setString(3, password);
			updateStmt.execute();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			con.close();
		}
	}
	
	/**
	 * check if the user is allowed to login
	 * check that they are in database and username/password match
	 * @param user
	 * @param pwd
	 * @return
	 * @throws SQLException
	 */
	public static boolean checkUser(String user, String pwd) throws SQLException{
		Connection con = null;
		try {
			con = getConnection();
			String selectStmt = "SELECT * FROM user;"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			
			ResultSet result = stmt.executeQuery();
			String u = null;
			String pwds = null;
			while (result.next()) {
				String name = result.getString("username");
				if(name.equals(user)){
					u = name;
					pwds = result.getString("password");
					
				}
			}
			if(u==null || !pwds.equals(pwd)){
				return false;
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			con.close();
		}
		
		
	}
	
	/**
	 * method that returns true if there
	 * are no other users with the same
	 * username in the database, false otherwise
	 * @param user
	 * @return boolean
	 * @throws SQLException 
	 */
	public static boolean noDuplicates(String user) throws SQLException{
		Connection con = null;
		try {
			con = getConnection();
			String selectStmt = "SELECT * FROM user;"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				String name = result.getString("username");
				if(name.equals(user)){
					return false;
					
				}
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			con.close();
		}
		
		return true;
	}
	
	/**
	 * Creates an arraylist of a users favs
	 * to use to display to the client 
	 *
	 * @param username
	 * @return arraylist ids
	 * @throws SQLException
	 */
	public static ArrayList<String> getFavs(String username) throws SQLException{
		Connection con = null;
		try {
			ArrayList<String> ids = new ArrayList<>();
			con = getConnection();
			String selectStmt = "SELECT * FROM favs where username=\""+username+"\";"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()){
				String id = result.getString("trackID");
				ids.add(id);
			}
			return ids;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			con.close();
		}
		return null;
	}
	
	/**
	 * adds a song to the favs database 
	 * for a specific user
	 * @param username
	 * @param songID
	 * @throws SQLException
	 */
	public static void addfav(String username, String songID) throws SQLException{
		Connection con = null;
		try{
			con = getConnection();
			if(!tableExists(con, "favs")){
				createTable("favs");
			}
			PreparedStatement updateStmt = (PreparedStatement) con.prepareStatement("INSERT INTO favs (username, trackID) VALUES (?, ?)");
			updateStmt.setString(1, username);
			updateStmt.setString(2, songID);
			updateStmt.execute();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			con.close();
		}
		
	}
	
	
	
	
	/**
	 * method that is used to create appropriate table
	 * if it does not yet exist for whatever reason
	 * @throws SQLException 
	 */
	public static void createTable(String table) throws SQLException{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
		if(table.equals("user")){
			
			
			stmt.executeUpdate (
					"CREATE TABLE user" + 
					"(name VARCHAR(100) not null, username VARCHAR(100) not null PRIMARY KEY, password VARCHAR(50) not null);");
			
			
		}
		else if(table.equals("favs")){
			stmt.executeUpdate (
					"CREATE TABLE favs" + 
					"(username VARCHAR(100) not null, trackID VARCHAR(100) not null);");
			
		}
		con.close();
	}
	
	
	/**
	 * Helper method that determines whether a table exists in the database.
	 * @param con
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	private static boolean tableExists(Connection con, String table) throws SQLException {

		DatabaseMetaData metadata = con.getMetaData();
		ResultSet resultSet;
		resultSet = metadata.getTables(null, null, table, null);

		if(resultSet.next()) {
			// Table exists
			return true;
		}		
		return false;
	}
	
	

}
