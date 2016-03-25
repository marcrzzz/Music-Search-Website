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

public class ConcurrentMusicLibrary extends MusicLibrary {
	//override ALL methods in Music library
	
	private ReentrantLock lock;
	
	public ConcurrentMusicLibrary() {
		super();
		lock = new ReentrantLock();
	}
	@Override
	public void addSong(Song song) {//read&write, but just need write lock
		//get write lock
		lock.lockWrite();
		super.addSong(song);
		lock.unlockWrite();
		//release lock
		
		
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
	public void orderSong(Path file, TreeMap<String, TreeSet<Song>> map){//read
		lock.lockRead();
		super.orderSong(file, map);
		lock.unlockRead();
	}
	
	/**
	 * order by the tag and print
	 * all trackIds with that tag
	 * @param file
	 */
	public void orderByTag(Path file) {//read
		lock.lockRead();
		super.orderByTag(file);
		lock.unlockRead();
	}
	
}
