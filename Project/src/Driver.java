import java.nio.file.Path;
import java.nio.file.Paths;

import cs212.data.ConcurrentMusicLibrary;
import cs212.data.MusicLibrary;
import cs212.utils.BuildLibrary;
import cs212.utils.ParseArguments;
import cs212.utils.WorkQueue;

public class Driver {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		ParseArguments a = new ParseArguments(args);
		if(!a.getArguments()){
			System.err.println("Not enough Arugments");
			return;
		}
		Path inPath = Paths.get(a.getInput());
		Path outPath = Paths.get(a.getOutput());
		String order = a.getOrder();
		int threadNum = a.getThreads();
		if(threadNum < 1 || threadNum > 1000 ){
			threadNum = 10;
		}
		if(!order.equals("artist")&&!order.equals("title")&&!order.equals("tag")){
			System.err.println("No such order argument!");
			return;
		}
		BuildLibrary lib = new BuildLibrary(inPath, threadNum);
		ConcurrentMusicLibrary library = lib.getMusicLibrary();
		
		if (order.equals("artist")){
			library.orderByArtist(outPath);
		}
		else if(order.equals("title")){
			library.orderByTitle(outPath);
		}
		else if(order.equals("tag")){
			library.orderByTag(outPath);
		}
		
		
		
	}

}
