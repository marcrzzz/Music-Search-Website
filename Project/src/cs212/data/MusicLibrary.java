package cs212.data;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

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
	public void addSong(Song song) {//read&write, but just need write lock
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
	 * Return a sorted set of all songs by a given artist.
	 * @param artist
	 * @return
	 */
//	//not thread safe need to change it
//	public TreeSet<Song> getSongsByArtist(String artist) {
//		return this.artistMap.get(artist);
//	}
//	//have them return json object that is representation of the data
//	public TreeSet<Song> getSongByTitle(String title){
//		return this.titleMap.get(title);
//		
//		//retrieve treeset of songs and converts it into jsonobj ex: {title:"songTitle", songs:[{artist:xxx, title:"songTitle",tags:ect},
//		//{all info},{more info of songs} ]}
//		//use get and put
//	}
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