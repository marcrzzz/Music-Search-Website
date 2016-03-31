package cs212.utils;
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

import cs212.data.ConcurrentMusicLibrary;
import cs212.data.MusicLibrary;
import cs212.data.Song;

public class BuildLibrary {
	
	private Path path;
	private WorkQueue queue;
	private int threadNum;
	private ConcurrentMusicLibrary library;
	
	/**
	 * constructor
	 * @param path, number of threads
	 */
	public BuildLibrary(Path path, int threadNum){
		this.path = path;
		this.threadNum = threadNum;
		this.queue = new WorkQueue(this.threadNum);
		this.library = new ConcurrentMusicLibrary();
		findFiles(this.path, ".json");
		
		
	}
	
	
	/**
	 * creates arraylist of found JSON files
	 * @param path
	 * @param extension
	 * @return arraylist
	 */
	private ArrayList<String> findFiles(Path path, String extension) {
		  
		ArrayList<String> result = new ArrayList<>();
		findFiles(path, extension, result);
		queue.shutdown();
		queue.awaitTermination();
		return result;
		
	}
	
	/**
	 * helper method for above findFiles method
	 * @param path
	 * @param extension
	 * @param files
	 */
	private void findFiles(Path path, String extension, ArrayList<String> files){
		
		if(Files.isDirectory(path)){
			try(DirectoryStream<Path> dir = Files.newDirectoryStream(path)){
				
				for(Path entry: dir){
					findFiles(entry, extension, files);
					}
				
			}catch (IOException e) {
				
				e.printStackTrace();
				
			}
		}
		else{
			queue.execute(new ParseFile(path,extension));
		}
		
		
		}
	
	
	
	/**
	 * get method for the music library that we added songs to
	 * @return MusicLibrary library
	 */
	public ConcurrentMusicLibrary getMusicLibrary(){
		return this.library;
	}
	
	
	
	/**
	 * inner class of BuildLibrary that is runnable
	 * allows concurrency in parsing files
	 * @author Marcrzzz
	 *
	 */
	public class ParseFile implements Runnable{

		private Path path;
		private String extension;
		
		public ParseFile(Path path, String extension) {
			this.path = path;
			this.extension = extension;
		}

		@Override
		public void run() {
			parseFile(this.path, this.extension);
			
		}
		
		/**
		 * parses each file and extracts the necessary
		 * information to create song and add it to
		 * the library
		 * @param file
		 */
		private void parseFile(Path file,String extension){
			JSONParser parser = new JSONParser();
			if(!path.toString().toLowerCase().endsWith(extension)){
				System.err.println("Failed to open file on path: " + file.toString());
				return;
			}
			try (BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF-8"))){
				
				
				JSONObject contents = (JSONObject) parser.parse(reader);
				String artist = (String) contents.get("artist");
				String title = (String) contents.get("title");
				String track_id = (String) contents.get("track_id");
				JSONArray similars = (JSONArray) contents.get("similars");
				JSONArray tags = (JSONArray) contents.get("tags");
				ArrayList<String> simArray = new ArrayList<String>();
				ArrayList<String> tagArray = new ArrayList<String>();
				
				for (Object t: tags){
					if (t instanceof JSONArray){
						tagArray.add((String) ((ArrayList<?>) t).get(0));
					}
				}
				for (Object s: similars){
					if (s instanceof JSONArray){
						simArray.add((String) ((ArrayList<?>) s).get(0));
					}
				}
				
				Song song = new Song(artist, track_id, title, simArray, tagArray);
				library.addSong(song);
				
			}catch (IOException e) {
				System.err.println("Failed to open file on path: " + file.toString());
				
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}
		
		}
	
	}
	
}
