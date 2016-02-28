import java.nio.file.Path;
import java.nio.file.Paths;

import cs212.data.MusicLibrary;
import cs212.utils.BuildLibrary;
import cs212.utils.ParseArguments;

public class Driver {
	
	public static void main(String[] args) {

		ParseArguments a = new ParseArguments(args);
		if(!a.getArguments()){
			System.err.println("Not enough Arugments");
			return;
		}
		Path inPath = Paths.get(a.getInput());
		Path outPath = Paths.get(a.getOutput());
		String order = a.getOrder();
		BuildLibrary lib = new BuildLibrary(inPath);
		MusicLibrary library = lib.getMusicLibrary();
		
		if (order.equals("artist")){
			library.orderByArtist(outPath);
		}
		else if(order.equals("title")){
			library.orderByTitle(outPath);
		}
		else if(order.equals("tag")){
			library.orderByTag(outPath);
		}
		else{
			System.err.println("No such order argument!");
		}
			
		
		
	}

}
