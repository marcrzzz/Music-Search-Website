import org.json.simple.JSONArray;

public class StoreData{
	
	public static void storeData(JSONArray similars, JSONArray tag, String artist, String title, String id ){
		
		System.out.println(similars);
		System.out.println(tag);
		System.out.println(artist);
		System.out.println(title);
		System.out.println(id);
		
		storeArrays(tag);
		//storeArrays(similars);
		
		
		
	}
	
	public static void storeArrays(JSONArray array){
		if (array.size() == 0){
			System.out.println("empty array");
			//tags is empty array, add exception/error
		}
		else {
			for(int i=0; i < array.size(); i++){
				JSONArray tags = (JSONArray) array.get(i);
				String genre = (String) tags.get(0);
				System.out.println(genre);
			}
			
			
		}
		
	}
	
	
}