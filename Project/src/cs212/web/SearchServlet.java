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
		String style = style("Song Finder", "Discover Music");
		
		String all = style;
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
