package cs212.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cs212.data.ConcurrentMusicLibrary;

public class Search {

	
	private Path input;
	private JSONObject info;
	private ConcurrentMusicLibrary lib;
	
	/**
	 * constructor
	 * @param input
	 * @param lib
	 */
	public Search(Path input, ConcurrentMusicLibrary lib){
		this.input = input;
		this.info = new JSONObject();
		this.lib = lib;
		parseInput(this.input);
		
	}
	
	/**
	 * parses the input file to obtain the json object with the query you want to search by
	 * calls the addInfo method to add the data to our info object
	 * @param file
	 */
	public void parseInput(Path file){
		JSONParser parser = new JSONParser();
		
		try(BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"))){
			JSONObject obj = (JSONObject) parser.parse(reader);
			if(obj.containsKey("searchByArtist")){
				addInfo(obj, "searchByArtist");
				
				
			}
			if(obj.containsKey("searchByTitle")){
				addInfo(obj, "searchByTitle");
				
			}
			if(obj.containsKey("searchByTag")){
				addInfo(obj, "searchByTag");
				
				
			}
			
			
		} catch (IOException e) {
			System.err.println("Failed to open file on path: " + file.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * adds info to the info JSONObject. Gets the data from the appropriate 
	 * music library method.
	 * @param obj
	 * @param key
	 */
	public void addInfo(JSONObject obj, String key){
		JSONArray jsonArray = (JSONArray) obj.get(key);
		ArrayList<String> query = new ArrayList<String>();
		for (Object i: jsonArray){
			if (i instanceof String){
				query.add((String) i);
			}
		}
		
		JSONArray data = new JSONArray();
		for(String s : query){
			if(key=="searchByArtist"){
				JSONObject o = lib.getSongsByArtist(s);
				data.add(o);
			}
			if(key=="searchByTitle"){
				JSONObject o = lib.getSongsByTitle(s);
				data.add(o);
			}
			if(key=="searchByTag"){
				JSONObject o = lib.getSongsByTag(s);
				data.add(o);
			}
			
		}
		this.info.put(key, data);
	}
	
	
	/**
	 * writes info JSONObject to an output file
	 * @param file
	 */
	public void writeToOuput(Path file) {
		try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"))){
			writer.write(this.info.toString());
			
		}
		catch(IOException e){
			
			System.err.println("File output not available!");
		}
	}
	
	
	
	
	
	
	
}
