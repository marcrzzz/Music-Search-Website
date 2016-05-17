package cs212.api;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.PreparedStatement;

public class FetchInfo {
	
	private final String APIKEY = "6a63aa022b11b9ff9ebd3d124f440221";
	private String artist;
	private JSONObject artistInfo;
	private String pageInfo;
	
	/***
	 * Constructor to get the page information from the api
	 * @param artist
	 */
	public FetchInfo(String artist, String method){
		
		this.artist = artist;
		this.artistInfo = new JSONObject();
		try {
			this.pageInfo = HTTPFetcher.download("ws.audioscrobbler.com",
					"/2.0?artist=" + this.artist + 
					"&api_key=" + APIKEY + 
					"&method="+method+"&format=json");
		} catch (RequestException e) {
			this.pageInfo = null;
		}
	}
	
	
	
	/**
	 * 
	 * From the page info made in constructor, this method
	 * gets the needed information such as the jsonobject representation
	 * of the artist info
	 * @return JSONObject
	 */
	public JSONObject getInfo(){
		if(pageInfo == null){
			return null;
		}
		String[] pages = pageInfo.split("\n");
		String jsonInfo="";
		for(int i=0; i<pages.length; i++){
			
			if(pages[i].length()==0){
				jsonInfo = pages[i+1];
				break;
				
			}
		}
		
		JSONParser parser = new JSONParser();
		
		try {
			artistInfo = (JSONObject) parser.parse(jsonInfo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return artistInfo;
		
	}
	

}
