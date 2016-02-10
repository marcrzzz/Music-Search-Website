import java.util.ArrayList;


public class StoreSong{
	
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
	public StoreSong(String artist, String trackId, String title, ArrayList<String> similars, ArrayList<String> tags) {
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
	/**
	 * toString method to debug
	 */
	public String toString() {
		return artist + " - " + title + " - " + trackId + " - " + similars + " - " +tags;
	}
	
	
}