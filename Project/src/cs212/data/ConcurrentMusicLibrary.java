package cs212.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONObject;

import cs212.comparators.ByArtistComparator;
import cs212.comparators.ByTitleComparator;
import cs212.utils.ReentrantLock;

public class ConcurrentMusicLibrary extends MusicLibrary {
	
	private ReentrantLock lock;
	
	public ConcurrentMusicLibrary() {
		super();
		lock = new ReentrantLock();
	}
	
	/**
	 * Acquires lock write before
	 * calling superclass method to add song
	 */
	@Override
	public void addSong(Song song) {
		lock.lockWrite();
		super.addSong(song);
		lock.unlockWrite();
	}
	
	/**
	 * Uses superclass method to 
	 * return songs by an artist
	 */
	@Override
	public JSONObject getSongsByArtist(String artist){
		lock.lockRead();
		JSONObject obj = super.getSongsByArtist(artist);
		lock.unlockRead();
		return obj;
	}
	
	
	/**
	 * Uses superclass method
	 * to return songs with a given
	 * title
	 */
	@Override
	public JSONObject getSongsByTitle(String title){
		lock.lockRead();
		JSONObject obj = super.getSongsByArtist(title);
		lock.unlockRead();
		return obj;
	}
	
	/**
	 *calls it's superclass methods 
	 *to allow concurrency for ordering 
	 *by specified order
	 * Order by artist
	 * @param file
	 * 
	 */
	public void orderByArtist(Path file) {
		lock.lockRead();
		super.orderByArtist(file);
		lock.unlockRead();
		
	}
	
	/**
	 * order by the title
	 * @param file
	 */
	public void orderByTitle(Path file) {
		lock.lockRead();
		super.orderByTitle(file);
		lock.unlockRead();
	}
	
	/**
	 * helper method for order by title and artist
	 * @param file
	 * @param map
	 */
	public void orderSong(Path file, TreeMap<String, TreeSet<Song>> map){
		lock.lockRead();
		super.orderSong(file, map);
		lock.unlockRead();
	}
	
	/**
	 * order by the tag and print
	 * all trackIds with that tag
	 * @param file
	 */
	public void orderByTag(Path file) {
		lock.lockRead();
		super.orderByTag(file);
		lock.unlockRead();
	}
	
}
