import java.util.Comparator;

public class ByArtistComparator implements Comparator<StoreSong> {

	@Override
	public int compare(StoreSong o1, StoreSong o2) {
		if(o1.getArtist().equals(o2.getArtist())) {
			if(o1.getTitle().equals(o2.getTitle())){
				return o1.getTrackId().compareTo(o2.getTrackId());
			}
			return o1.getTitle().compareTo(o2.getTitle());
		}
		return o1.getArtist().compareTo(o2.getArtist());
		
	
	}
	
}
