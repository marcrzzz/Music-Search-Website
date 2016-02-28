package cs212.utils;



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
	public Boolean getArguments(){
		
		if(args.length != 6){
			return false;
		}
		
		

		for (int i = 0; i < args.length-1; i+=2) {				
				this.arg = args[i];
				
				if (arg.equals("-output")){
					this.outputPath = args[i+1];
				}
				else if (arg.equals("-input")){
					this.inputPath = args[i+1];
				}
				else if (arg.equals("-order")){
					this.order = args[i+1];
				}
			}				
					
		return true;
	}
	
	
	public String getOutput(){
		return this.outputPath;
	}
	
	public String getInput(){
		return this.inputPath;
	}
	
	public String getOrder(){
		return this.order;
	}
		
	
}
