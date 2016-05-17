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
		String content="<div class=\"userInfo\"><font-color:white>~~~"+
				username+"~~~</font>"+
				"<p><a href=\"/favs\">My Favorites</a></p>"+
				"<p> <a href=\"/history\">History</a></p>"+
				"<p> <a href=\"/settings\">Account Settings</a></p>"+
				"<p> <a href=\"/logout\">Logout</a></p></div>";
				
		return content;
	}
	
	protected String style(String title, String header){
		String content = "<html" + 
				"<head><title>"+ title +"</title><style>"+
		"table{ background-color: #8B008B; } "+
		"table, th, td { border: 5px solid black; border-color: black; border-collapse: collapse;}" +
		"th, td { padding: 10px;text-align: center; color: white} " +
		"body{background-color: black; color: white}"+
		"h1{color: #8B008B; font: italic bold 30px Georgia, serif; }"+
		".sel {background-color: white; }" +
		"a:link, a:visited {background-color: black; color: #8B008B;"+
	    "  text-decoration: none;  }"+
	    "a.s:link, a.s:visited { background-color: #8B008B; color: white;"+
	    "  text-decoration: none;  }"+
	    "a.a:link, a.a:visited { background-color:black; color: white;"+
	    "  text-decoration: none;  }"+
	    "a.f:link, a.f:visited { background-color: #8B008B; color: black;"+
	    "  text-decoration: none;  }"+
		".userInfo { color: white; font: italic bold 15px Georgia, serif; position:absolute; "+
		"top:10px; right:10px; } "+
		".p{text-align: center; font: italic bold 15px Georgia, serif; color:white}"+
		".songInfo h2{color: #8B008B; text-align: center; font: italic bold 15px Georgia, serif;}"+
		".songInfo p{color: white; text-align: center; }"+
		"hr{border-color:#8B008B; width: 100px;}"+
		"</style></head>" +
		"<body><h1>"+ header +"<img align=\"left\" src=\"http://sheldon-conrich.sheldonconrich.netdna-cdn.com/wp-content/uploads/2013/06/notes_Inverted_Web.jpg\" alt=\"logo\" style=\"width:100px;height:75px; \"></h1>";
		
		return content;
		
	}
	
	
	protected String searchHTML(String status){
		String content="<a href=\"/artists?sort=alpha\"> View All Artists </a><p></p>"+
				"<form action=\"songs?status="+ status +"\" method=\"post\">" +
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
