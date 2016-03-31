package cs212.data;
import java.util.ArrayList;


public class Song{
	
	private String artist;
	private String trackId;
	private String title;
	private ArrayList<String> similars;
	private ArrayList<String> tags;

	/**
	 * Constructor.
	 * @param artist
	 * @param trackId
	 * @param title
	 * @param similars
	 * @param tags
	 */
	public Song(String artist, String trackId, String title, ArrayList<String> similars, ArrayList<String> tags){
		this.artist = artist;
		this.trackId = trackId;
		this.title = title;
		this.similars = similars;
		this.tags = tags;
	}
	
	/**
	 * Return artist.
	 * @return
	 */
	public String getArtist() {
		return this.artist;
	}
	
	/**
	 * Return track ID.
	 * @return
	 */
	public String getTrackId() {
		return this.trackId;
	}

	/**
	 * Return title.
	 * @return 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Return a list of the track IDs of all similar tracks.
	 * @return 
	 */
	public ArrayList<String> getSimilars() {
		return this.similars;
	}

	/**
	 * Return a list of all tags for this track.
	 * @return 
	 */
	public ArrayList<String> getTags() {
		return this.tags;
	}
	
	public Song clone(){
		ArrayList<String> sims = (ArrayList<String>) this.similars.clone();
		ArrayList<String> tgs = (ArrayList<String>) this.tags.clone();
		
		Song clonedSong = new Song(this.artist, this.trackId, this.title, sims, tgs);
		return clonedSong;
	}
	/**
	 * toString method to debug
	 */
	public String toString() {
		return artist + " - " + title + " - " + trackId + " - " + similars + " - " +tags;
	}
//TODO: make sure to do a deep copy by creating new array lists for tags and similars.	
	
}
