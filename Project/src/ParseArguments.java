import java.nio.file.Path;
import java.nio.file.Paths;

public class ParseArguments{
	
	static String arg;
	static String outputPath;
	static String inputPath;
	static String order;
	
	public static void getArguments(String[] args){
		
	
		
		if(args.length == 6){
			try{
				//System.out.println(args.length);
				for (int i = 0; i < args.length; i++) {
						arg = args[i++];
						
						if (arg.equals("-output")){
							outputPath = args[i];
							
							//System.out.println(outputPath);
								
						}
						else if (arg.equals("-input")){
							inputPath = args[i];
							
							//System.out.println(inputPath);
							
						}
						else if (arg.equals("-order")){
							order = args[i];
							//System.out.println(order);
							
							
						}
						
						
			
			    }
			    
			     
			    }
			    catch(RuntimeException exception){
			    	System.out.println("runtime error");
			    }
			
			Path inPath = Paths.get(inputPath);
			BuildLibrary lib = new BuildLibrary(inPath);
			
			
			
			}
		else{
			
			System.err.println("not enough arguments");
			
			
			
			
		}
		
		}
		
	
}