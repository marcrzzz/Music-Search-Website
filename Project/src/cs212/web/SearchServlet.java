package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchServlet extends BaseServlet {

	
	/**
	 * GET /search returns a web page containing a search box where you can enter a query.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		

		String responseHtml = "<html" + 
				"<head><title>Song Finder</title><style>.sel {background-color: white; } h1{color: #8B008B; "
				+ "font: italic bold 30px Georgia, serif; } a:link, a:visited {background-color: white; color: #8B008B;"+
			    " text-decoration: none; }"+
				".userInfo{ color: #8B008B; font: italic bold 15px Georgia, serif; position:absolute; "+
				"top:10px; right:10px; } "+
				".p{text-align: center; font: italic bold 15px Georgia, serif;}"+
				"</style></head>" +
				"<body>" +
				"<h1>Discover Music</h1> ";
		
		String all = responseHtml;
		String search = searchHTML();
		
		
		if(session.getAttribute("name") == null){
			all+="<p><a href=\"/signup?status=null\">Create Account</a> </p>";
					
		}	
					
		
		if(session.getAttribute("name") != null){
			String userInfo = userInfo((String)session.getAttribute("name"));
			all += search + userInfo + "</body>" +
					"</html>";
			
			
		}
		else{
			all +=search +  "</body>" +
					"</html>";
		}
		PrintWriter writer = prepareResponse(response);
		writer.println(all);
		
		
	}
}
