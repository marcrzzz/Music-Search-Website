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
import java.util.TreeSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mysql.jdbc.PreparedStatement;

import cs212.api.FetchInfo;
import cs212.comparators.ByArtistComparator;
import cs212.comparators.ByPlayCountComparator;
import cs212.data.ConcurrentMusicLibrary;
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
	 * Removes a song as a favorite for a user
	 * @param username
	 * @throws SQLException
	 */
	public static void deleteFav(String username, String songID) throws SQLException{
		Connection con = null;
		try {
			con = getConnection();
			String selectStmt = "DELETE FROM favs WHERE username=? AND trackID=?;"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			stmt.setString(1, username);
			stmt.setString(2, songID);
			stmt.execute();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			con.close();
		}
		
		
	}
	
	/**
	 * uses library to add artist info from lastfm api
	 * @param lib
	 * @throws SQLException 
	 */
	public static void addInfo(ConcurrentMusicLibrary lib) throws SQLException{
		Connection con=null;
		try{
			con = getConnection();
			if(!tableExists(con, "artistInfo")){
				createTable("artistInfo");
				TreeSet<String> artists = lib.getArtists();
				System.out.println(artists.size());
				for(String a: artists){
					
					FetchInfo info = new FetchInfo(a, "artist.getInfo");
					JSONObject o = info.getInfo();
					if(o==null){
						DBHelper.updateArtistInfo(con, a, 0, 0, null);
						continue;
					}
					JSONObject innerJSON =(JSONObject) o.get("artist");
					if(innerJSON==null){
						DBHelper.updateArtistInfo(con, a, 0, 0, null);
						continue;
					}
					String name = (String) innerJSON.get("name");
					JSONObject bio =(JSONObject) innerJSON.get("bio");
					JSONObject stats =(JSONObject) innerJSON.get("stats");
					if(bio==null){
						if(stats==null){
							DBHelper.updateArtistInfo(con, name, 0, 0, null);
							continue;
						}
						String listeners = (String) stats.get("listeners");
						String playcount = (String) stats.get("playcount");
						int l = Integer.parseInt(listeners);
						int p = Integer.parseInt(playcount);
						DBHelper.updateArtistInfo(con, name, l, p, null);
						return;
						
					}
					if(stats==null){
						String summary = (String) bio.get("summary");
						DBHelper.updateArtistInfo(con, name, 0, 0, summary);
						continue;
					}
					String listeners = (String) stats.get("listeners");
					String playcount = (String) stats.get("playcount");
					String summary = (String) bio.get("summary");
					int l = 0;
					int p = 0;
					if(listeners==null || playcount==null){
						if(playcount==null){
							l = Integer.parseInt(listeners);
						}
						if(listeners==null){
							p = Integer.parseInt(playcount);
						}
					}
					else{
						l = Integer.parseInt(listeners);
						p = Integer.parseInt(playcount);
					}
					
					DBHelper.updateArtistInfo(con, name, l, p, summary);
				}
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			con.close();
		}
		
		
	}
	
	
	
	/**
	 * helper method to add artist to userbase
	 * @param con
	 * @param name
	 * @param l
	 * @param p
	 * @param summary
	 */
	public static void updateArtistInfo(Connection con, String name, int l, int p, String summary){
		PreparedStatement updateStmt;
		try {
			updateStmt = (PreparedStatement) con.prepareStatement("INSERT INTO artistInfo (name, listeners, playcount, bio) VALUES (?, ?, ?, ?)");
			updateStmt.setString(1, name);
			updateStmt.setInt(2, l);
			updateStmt.setInt(3, p);
			updateStmt.setString(4, summary);
			updateStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * gets an artist from database and returns json obj with all its info
	 * @param a
	 * @throws SQLException 
	 */
	public static JSONObject getArtist(String a) throws SQLException{
		Connection con = null;
		try{
			con = getConnection(); 
			String selectStmt = "SELECT * FROM artistInfo WHERE name=? ;"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			stmt.setString(1, a);
			ResultSet result = stmt.executeQuery();
			JSONObject o = new JSONObject();
			while(result.next()){
				String n = result.getString("name");
				String bio = result.getString("bio");
				int l = result.getInt("listeners");
				int c = result.getInt("playcount");
				o.put("listeners", l);
				o.put("bio", bio);
				o.put("artist", a);
				o.put("playcount", c);
				
			}
			return o;
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
	 * Gets artist info sorted by playcount 
	 * @return
	 * @throws SQLException
	 */
	public static TreeSet<JSONObject> getArtists() throws SQLException{
		Connection con = null;
		TreeSet<JSONObject> counts = new TreeSet<JSONObject>(new ByPlayCountComparator());
		try {
			con = getConnection();
			String selectStmt = "SELECT * FROM artistInfo;"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()){
				JSONObject o = new JSONObject();
				int c = result.getInt("playcount");
				String a = result.getString("name");
				o.put("artist", a);
				o.put("playcount", c);
				counts.add(o);
				
			}
			
			return counts;
			
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
	 * updates database to have user's new password
	 * @param user
	 * @param newPwd
	 * @throws SQLException
	 */
	public static void changePwd(String user, String newPwd) throws SQLException{
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement updateStmt = (PreparedStatement) con.prepareStatement("UPDATE user SET password = ? WHERE username = ?;");
			updateStmt.setString(1, newPwd);
			updateStmt.setString(2, user);
			updateStmt.execute();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			con.close();
		}
		
	}
	
	/**
	 * returns a list of all songs in the user's history
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<String> showHistory(String username, String type) throws SQLException{
		Connection con = null;
		try {
			ArrayList<String> searches = new ArrayList<>();
			con = getConnection();
			if(!tableExists(con, "history")){
				createTable("history");
			}
			String selectStmt = "SELECT * FROM history where name=\""+username+"\" AND searchType=\""+type+"\";"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()){
				String s = result.getString("search");
				searches.add(s);
			}
			return searches;
			
			
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
	 * adds a song search to user's history
	 * @param user
	 * @param id
	 * @throws SQLException
	 */
	public static void addToHistory(String user, String search, String type) throws SQLException{
		
		Connection con = null;
		try{
			con = getConnection();
			if(!tableExists(con, "history")){
				createTable("history");
			}
			PreparedStatement updateStmt = (PreparedStatement) con.prepareStatement("INSERT INTO history (name, search, searchType) VALUES (?, ?, ?)");
			updateStmt.setString(1, user);
			updateStmt.setString(2, search);
			updateStmt.setString(3, type);
			updateStmt.execute();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			con.close();
		}
		
		
	}
	
	/**
	 * Removes a users total history
	 * @param username
	 * @throws SQLException
	 */
	public static void deleteHistory(String username) throws SQLException{
		Connection con = null;
		try {
			con = getConnection();
			String selectStmt = "DELETE FROM history WHERE name=?;"; 
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(selectStmt);
			stmt.setString(1, username);
			stmt.execute();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		else if(table.equals("artistInfo")){
			stmt.executeUpdate (
					"CREATE TABLE artistInfo" + 
					"(name VARCHAR(250) not null , listeners INTEGER, playcount INTEGER, bio TEXT);");
		}

		else if(table.equals("history")){
			stmt.executeUpdate (
					"CREATE TABLE history" + 
					"(name VARCHAR(100) not null, search VARCHAR(100) not null, searchType VARCHAR(100) not null);");
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
