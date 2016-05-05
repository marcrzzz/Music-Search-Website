package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * A Servlet superclass with printWriter method for all servlets for this application.
 * 
 *
 */
public class BaseServlet extends HttpServlet{
	
	
	protected String userInfo(String username){
		String content="<div class=\"userInfo\">~~~"+
				username+"~~~<p><a href=\"/favs\">My Favorites</a></p>"+
				"<p> <a href=\"/logout\">Logout</a></p></div>";
				
		return content;
	}
	
	protected String searchHTML(){
		String content="<form action=\"songs\" method=\"post\">" +
				"Search by:"+
				"<select class=\"sel\" name=\"t\">"+
				  "<option value=\"artist\">Artist</option>"+
				 " <option value=\"title\">Title</option>"+
				 " <option value=\"tag\">Tag</option>"+
				"</select>"+
				"<p> </p>"+
				"Artist/Title/Tag: "+
				"<input type=\"text\" name=\"query\">" +
				"<input type=\"submit\" value=\"Search\"><br/>" +
				"</form>";
		
		return content;
	}
	
	
	
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();
	}

}
