package cs212.comparators;

import java.util.Comparator;

import org.json.simple.JSONObject;

public class ByPlayCountComparator implements Comparator<JSONObject>{
	public int compare(JSONObject o1, JSONObject o2) {
		int c1 = (int) o1.get("playcount");
		int c2 = (int) o2.get("playcount");
		if(c1<c2){
			return 1;
		}
		else if(c1==c2){
			return 0;
		}
		else{
			return -1;
		}
			
	}
}
