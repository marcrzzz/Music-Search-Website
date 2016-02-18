package cs212.comparators;
import java.util.Comparator;

import cs212.data.Song;

public class ByTitleComparator implements Comparator<Song> {

	@Override
	public int compare(Song o1, Song o2) {
		if(o1.getTitle().equals(o2.getTitle())) {
			if(o1.getArtist().equals(o2.getArtist())){
				return o1.getTrackId().compareTo(o2.getTrackId());
			}
			return o1.getArtist().compareTo(o2.getArtist());
		}
		return o1.getTitle().compareTo(o2.getTitle());
		
	
	}

}
