package cs212.utils;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParseArguments{
	
	private String[] args;
	private String arg;
	private String outputPath;
	private String inputPath;
	private String order;
	
	/**
	 * constructor for ParseArguments
	 * @param args
	 */
	public ParseArguments(String[] args){
		this.arg = null;
		this.outputPath = null;
		this.inputPath = null;
		this.order = null;
		this.args = args;
		
	}
	
	/**
	 * method to get the arguments and set the
	 * paths and order
	 */
	public void getArguments(){
		//if this is false, print error 
		if(args.length != 6){
			System.err.println("not enough arguments!");
			return;
		}
		
		try{

			for (int i = 0; i < args.length; i++) {
					this.arg = args[i++];
					
					if (arg.equals("-output") && !args[i].equals("-")){
						this.outputPath = args[i];
					}
					else if (arg.equals("-input")){
						this.inputPath = args[i];
					}
					else if (arg.equals("-order")){
						this.order = args[i];
					}
				}
			}catch(RuntimeException exception){
		    	System.out.println("runtime error");
		    }
		
		Path inPath = Paths.get(this.inputPath);
		Path outPath = Paths.get(this.outputPath);
		BuildLibrary lib = new BuildLibrary(inPath);
		lib.printData(outPath, this.order);
	}
		
	
}