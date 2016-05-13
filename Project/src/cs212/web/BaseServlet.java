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
	
	protected String style(String title, String header){
		String content = "<html" + 
				"<head><title>"+ title +"</title><style>"+
//		"table, th, td {border: 3px solid black;"+
//		"border-collapse: collapse; border-color: #8B008B;}"+
		"table{ background-color: #8B008B; } "+
		"table, th, td { border: 5px solid black; border-color: black; border-collapse: collapse;}" +
		"th, td { padding: 10px;text-align: center; color: white} " +
		"body{background-color: black; color: white}"+
		"h1{color: #8B008B; font: italic bold 30px Georgia, serif; }"+
		".sel {background-color: white; }" +
		"a:link, a:visited {background-color: black; color: #8B008B;"+
	    "  text-decoration: none;  }"+
		".userInfo{ color: #8B008B; font: italic bold 15px Georgia, serif; position:absolute; "+
		"top:10px; right:10px; } "+
		".p{text-align: center; font: italic bold 15px Georgia, serif;}"+
		"</style></head>" +
		"<body><h1>"+ header +"</h1>";
		
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
