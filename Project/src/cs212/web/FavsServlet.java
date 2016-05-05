package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import cs212.Sql.DBHelper;
import cs212.data.ConcurrentMusicLibrary;
import cs212.data.Song;

public class  FavsServlet extends BaseServlet {
	
	private static final Logger logger = LogManager.getLogger(FavsServlet.class.getName());
	
	/**
	 * get method that displays a 
	 * saved favorites list from database
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		
		ConcurrentMusicLibrary lib = (ConcurrentMusicLibrary) request.getServletContext().getAttribute("musiclib");
		
		
		if(name==null){
			response.sendRedirect(response.encodeRedirectURL("/login?status=notLoggedIn"));
			return;
		}
		
		
		String responseHtml = "<html" + 
				"<head><title>Favs</title><style>"+
				"h1{color: #8B008B; font: italic bold 30px Georgia, serif; align:center; }"+
				"table{ background-color: #8B008B; } "+
						"table, th, td { border: 5px solid black; border-color: white; border-collapse: collapse;}" +
						"th, td { padding: 10px;text-align: center; color: white} " +
				"a:link, a:visited {background-color: white; color: #8B008B;"+
			    " text-decoration: none; }"+
				".userInfo{ color: #8B008B; font: italic bold 15px Georgia, serif; position:absolute; "+
				"top:10px; right:10px; } "+
				"</style></head>" +
				"<body><h1>Favorites</h1>";
		
		
		
		String responseHtmlContent = "<div class=\"userInfo\"><p>~~~"+
				name+"~~~<p><a href=\"/search\">Search</a></p>"+
				"<p><a href=\"/logout\">Logout</a></p>  </p></div>"+
				"<table  width=\"50%\" align=\"center\">";
		
		ArrayList<String> results;
		try {
			results = (ArrayList<String>) DBHelper.showFavs(name);
			for(String id: results){
				Song s = lib.getSongById(id);
				responseHtmlContent+="<tr><td>"+s.getTitle() +"</td></tr>";
			}
		} catch (SQLException e) {
			logger.error("SQL Exception");
			e.printStackTrace();
		}
		
		
		
		responseHtmlContent+="</table>"+
				"</body></html>";
		
		
		
		
		
		PrintWriter writer = prepareResponse(response);
		
		writer.println(responseHtml+responseHtmlContent);
		
		
	}
	
	
	
}
