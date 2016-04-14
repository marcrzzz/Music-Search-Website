package cs212.comparators;

import java.util.Comparator;

import org.json.simple.JSONObject;

public class ByTrackIdComparator implements Comparator<JSONObject>{
	public int compare(JSONObject o1, JSONObject o2) {
		String id1 = (String) o1.get("trackId");
		String id2 = (String) o2.get("trackId");
		return id1.compareTo(id2);
			
	}

}
