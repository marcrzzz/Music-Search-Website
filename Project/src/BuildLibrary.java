import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BuildLibrary{
	
	private Path path;
	private HashMap<String, TreeSet<StoreSong>> songMap;
	
	/**
	 * constructor
	 * @param path
	 */
	public BuildLibrary(Path path){
		this.path = path;
		this.songMap = new HashMap<>();
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
		
		if(path.toString().endsWith(extension.toLowerCase().trim())){
				
			parseFile(path);
			
		}
		
		else if(Files.isDirectory(path)){
			
			try(DirectoryStream<Path> dir = 
					Files.newDirectoryStream(path)) {
					
					for(Path entry: dir) {
						findFiles(entry, extension, files);
					}
					
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
			}
		
		
	}
	
	private void parseFile(Path file){
		
		JSONParser parser = new JSONParser();
		
		try (BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"))){
			
			String line = reader.readLine();			
			
			JSONObject contents = (JSONObject) parser.parse(line);
			JSONArray similars = (JSONArray) contents.get("similars");
			JSONArray tags = (JSONArray) contents.get("tags");
			String artist = (String) contents.get("artist");
			String title = (String) contents.get("title");
			String track_id = (String) contents.get("track_id");
			StoreSong song = new StoreSong(artist, track_id, title, similars, tags);	
			addSong(song);
			System.out.println();
				
		}catch (IOException e) {
			System.err.println("Failed to open file on path: " + file.toString());
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Add a song to the library.
	 * Make sure to add a reference to the song object to all 
	 * appropriate data structures.
	 * @param song
	 */
	private void addSong(StoreSong song) {
		String trackId = song.getTrackId();
		if(!this.songMap.containsKey(trackId)){
			this.songMap.put(trackId, new TreeSet<StoreSong>(new ByArtistComparator()));
		}
		this.songMap.get(trackId).add(song);
		
	}
	/**
	 * debug method
	 */
	public void debugByArtist() {
		for(String artist: this.songMap.keySet()){
			System.out.println(artist + ": " + this.songMap.get(artist));
		}
	}
		
	
	
	
	
}