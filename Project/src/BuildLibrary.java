import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BuildLibrary{
	
	public static ArrayList<String> findFiles(Path path, String extension) {
		
		ArrayList<String> result = new ArrayList<>();
		findFiles(path, extension, result);
		return result;
	}
	
	public static void findFiles(Path path, String extension, ArrayList<String> files){
		
		if(path.toString().endsWith(extension.toLowerCase().trim())){
				
			parseFile(path);
			
		}
		
		else if(Files.isDirectory(path)){
			
			try(DirectoryStream<Path> dir = 
					Files.newDirectoryStream(path)) {
					
					for(Path entry: dir) {
						findFiles(entry, extension, files);
					}
					
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
			}
		
		
	}
	
	private static void parseFile(Path file){
		
		JSONParser parser = new JSONParser();
		
		try (BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"))){
			
			String line = reader.readLine();			
			
			while(line != null){

				JSONObject contents = (JSONObject) parser.parse(line);
				JSONArray similars = (JSONArray) contents.get("similars");
				JSONArray tags = (JSONArray) contents.get("tags");
				String artist = (String) contents.get("artist");
				String title = (String) contents.get("title");
				String track_id = (String) contents.get("track_id");
				StoreData.storeData(similars, tags, artist, title, track_id);
				line = reader.readLine();
			}
		}catch (IOException e) {
			System.err.println("Failed to open file on path: " + file.toString());
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
		
	
	
	
	
}