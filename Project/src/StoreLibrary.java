import java.util.HashMap;
import java.util.TreeSet;

public class StoreLibrary{
	private HashMap<String, TreeSet<StoreSong>> songMap;
	
	/**
	 * constructor
	 */
	public StoreLibrary(){
		this.songMap = new HashMap<>();
	}
	
	/**
	 * Add a song to the library.
	 * Make sure to add a reference to the song object to all 
	 * appropriate data structures.
	 * @param song
	 */
	public void addSong(StoreSong song) {
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