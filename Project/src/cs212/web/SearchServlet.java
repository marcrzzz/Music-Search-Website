package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends BaseServlet {

	
	/**
	 * GET /search returns a web page containing a search box where you can enter a query.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseHtml = "<html" + 
				"<head><title>Song Finder</title><style>.sel {background-color: white; } h1{color: #8B008B; "
				+ "font: italic bold 30px Georgia, serif; } </style></head>" +
				"<body>" +
				"<h1>Discover Music</h1> ";

		String responseContent=
					"<form action=\"songs\" method=\"post\">" +
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
					"</form>" +
					"</body>" +
					"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml+responseContent);
		
		
	}
}
