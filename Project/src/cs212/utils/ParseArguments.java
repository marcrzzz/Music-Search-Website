package cs212.utils;



public class ParseArguments{
	
	private String[] args;
	private String arg;
	private String outputPath;
	private String inputPath;
	private String order;
	private int threads;
	private String searchInput;
	private String searchOutput;
	
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
		this.threads = 0;
		this.searchInput = null;
		this.searchOutput = null;
	}
	
	/**
	 * method to get the arguments and set the
	 * paths and order
	 */
	public Boolean getArguments(){
		
		if(args.length < 6){
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
				else if(arg.equals("-threads")){
					try{
						this.threads = Integer.parseInt(args[i+1]);
					
					}
					catch(NumberFormatException e){
						this.threads = 10;
					}
				}
				else if(arg.equals("-searchInput")){
					this.searchInput = args[i+1];
				}
				else if(arg.equals("-searchOutput")){
					this.searchOutput = args[i+1];
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
	
	public String getSearchIn(){
		return this.searchInput;
	}
	
	public String getSearchOut(){
		return this.searchOutput;
	}
	
	public int getThreads(){
		if(this.threads < 1 || this.threads > 1000 ){
			this.threads = 10;
		}
		return this.threads;
	}
		
	
}
