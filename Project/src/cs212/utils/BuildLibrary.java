package cs212.utils;
import java.io.BufferedReader;

import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cs212.data.MusicLibrary;
import cs212.data.Song;

public class BuildLibrary{
	
	private Path path;
	private MusicLibrary library;
	
	/**
	 * constructor
	 * @param path
	 */
	public BuildLibrary(Path path){
		this.path = path;
		this.library = new MusicLibrary();
		findFiles(this.path, ".json");
		
		
	}
	
	/**
	 * creates arraylist of found JSON files
	 * @param path
	 * @param extension
	 * @return arraylist
	 */
	private ArrayList<String> findFiles(Path path, String extension) {
		
		ArrayList<String> result = new ArrayList<>();
		findFiles(path, extension, result);
		return result;
		
	}
	
	/**
	 * helper method for above findFiles method
	 * @param path
	 * @param extension
	 * @param files
	 */
	private void findFiles(Path path, String extension, ArrayList<String> files){
		
		if(path.toString().toLowerCase().endsWith(extension)){	
			parseFile(path);
			
		}
		else if(Files.isDirectory(path)){
			
			try(DirectoryStream<Path> dir = Files.newDirectoryStream(path)){
					
					for(Path entry: dir){
						findFiles(entry, extension, files);
						}
					
			}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	/**
	 * parses each file and extracts the necessary
	 * information to create song and add it to
	 * the library
	 * @param file
	 */
	private void parseFile(Path file){
		JSONParser parser = new JSONParser();
		
		try (BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"))){
			
			String line = reader.readLine();			
			
			JSONObject contents = (JSONObject) parser.parse(line);
			String artist = (String) contents.get("artist");
			String title = (String) contents.get("title");
			String track_id = (String) contents.get("track_id");
			JSONArray similars = (JSONArray) contents.get("similars");
			JSONArray tags = (JSONArray) contents.get("tags");
			ArrayList<String> simArray = new ArrayList<String>();
			ArrayList<String> tagArray = new ArrayList<String>();
			
			for (Object t: tags){
				if (t instanceof JSONArray){
					tagArray.add((String) ((ArrayList<?>) t).get(0));
				}
			}
			for (Object s: similars){
				if (s instanceof JSONArray){
					simArray.add((String) ((ArrayList<?>) s).get(0));
				}
			}
			
			Song song = new Song(artist, track_id, title, simArray, tagArray);
			this.library.addSong(song);
			
		}catch (IOException e) {
			System.err.println("Failed to open file on path: " + file.toString());
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sends file path to the method that corresponds
	 * with the order 
	 * @param file
	 * @param order
	 */
//TODO: move this logic to Driver	 
	public void printData(Path file, String order){
		if (order.equals("artist")){
			this.library.orderByArtist(file);
		}
		else if(order.equals("title")){
			this.library.orderByTitle(file);
		}
		else if(order.equals("tag")){
			this.library.orderByTag(file);
		}
		else{
			System.err.println("No such order argument!");
		}
		
	}
		
	
	
	
	
}
