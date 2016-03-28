package cs212.data;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cs212.comparators.ByArtistComparator;
import cs212.comparators.ByTitleComparator;
import cs212.utils.ReentrantLock;

public class MusicLibrary {
	
	private TreeMap<String, TreeSet<Song>> artistMap;
	private TreeMap<String, TreeSet<Song>> titleMap;
	private TreeMap<String, TreeSet<String>> tagMap;
	
	
	/**
	 * constructor
	 */
	public MusicLibrary(){
		this.artistMap = new TreeMap<>();
		this.titleMap = new TreeMap<>();
		this.tagMap = new TreeMap<>();
		
	}
	
	/**
	 * Add a song to the library.
	 * Make sure to add a reference to the song object to all 
	 * appropriate data structures.
	 * @param song
	 */
	public void addSong(Song song) {
		String artist = song.getArtist();
		String title = song.getTitle();
		ArrayList<String> tags = song.getTags();
		
		if(!this.artistMap.containsKey(artist)){
			this.artistMap.put(artist, new TreeSet<Song>(new ByArtistComparator()));
		}
		this.artistMap.get(artist).add(song);
		
		if(!this.titleMap.containsKey(title)){
			this.titleMap.put(title, new TreeSet<Song>(new ByTitleComparator()));
		}
		this.titleMap.get(title).add(song);
		
		for (String t: tags){
			
			if(!this.tagMap.containsKey(t)){
				this.tagMap.put(t, new TreeSet<String>());
			}
			this.tagMap.get(t).add(song.getTrackId());
		}
		
		
	}
	
	
	/**
	 * Return a jsonobject representation of all songs by a given artist.
	 * @param artist
	 * @return
	 */
	
	public JSONObject getSongsByArtist(String artist) {
		JSONObject jsonArtist = new JSONObject();
		TreeSet<Song> set = this.artistMap.get(artist);
		jsonArtist.put("artist", artist);
		JSONArray songs = new JSONArray();
		for(Song s: set){
			JSONObject song = new JSONObject();
			song.put("artist", s.getArtist());
			song.put("title", s.getTitle());
			song.put("trackId", s.getTrackId());
			song.put("similars", s.getSimilars());
			song.put("tags", s.getTags());
			songs.add(song);
		}
		jsonArtist.put("songs", songs);
		return jsonArtist;
		
	}
	
	/**
	 * returns a jsonObject representation
	 * of all songs with a certain title
	 * @param title
	 * @return
	 */
	public JSONObject getSongsByTitle(String title){
		JSONObject jsonTitle = new JSONObject();
		TreeSet<Song> set = this.titleMap.get(title);
		jsonTitle.put("title", title);
		JSONArray songs = new JSONArray();
		for(Song s: set){
			JSONObject song = new JSONObject();
			song.put("artist", s.getArtist());
			song.put("title", s.getTitle());
			song.put("trackId", s.getTrackId());
			song.put("similars", s.getSimilars());
			song.put("tags", s.getTags());
			songs.add(song);
		}
		jsonTitle.put("songs", songs);
		return jsonTitle;
		
	}
	
	
	/**
	 * methods that are used to order 
	 * by the specified type(artist, title, or tag)
	 * and send to the specific path file
	 * Order by artist
	 * @param file
	 * 
	 */
	public void orderByArtist(Path file) {
		
		orderSong(file, artistMap);
		
	}
	
	/**
	 * order by the title
	 * @param file
	 */
	public void orderByTitle(Path file) {
		
		orderSong(file, titleMap);
		
	}
	
	/**
	 * helper method for order by title and artist
	 * @param file
	 * @param map
	 */
	public void orderSong(Path file, TreeMap<String, TreeSet<Song>> map){//read
		try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"))){
			
			for(String ele: map.navigableKeySet()){
				TreeSet<Song> songSet = map.get(ele);
				
				for (Song song: songSet){
					writer.write(song.getArtist() + " - " + song.getTitle());
					writer.newLine();
					}
				}
			
		}catch (IOException ex) {
	    	System.err.println("File output not available!");
	    }			
	}
	
	/**
	 * order by the tag and print
	 * all trackIds with that tag
	 * @param file
	 */
	public void orderByTag(Path file) {//read
		try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"))){
			
			for(String tag: this.tagMap.navigableKeySet()){
				writer.write(tag + ": ");
				TreeSet<String> trackIdSet = this.tagMap.get(tag);
				
				for(String id : trackIdSet){
					writer.write(id+" ");
					}
				writer.newLine();
				
			}
	        
	    }catch (IOException ex) {
	    	System.err.println("File output not available!");
	    }
	}
	
	
	
	
}