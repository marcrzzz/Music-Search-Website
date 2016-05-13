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
		
		String style = style("Favs", "Favorites");
		
	
		String responseHtmlContent = "<div class=\"userInfo\"><p>~~~"+
				name+"~~~<p><a href=\"/search\">Search</a></p>"+
				"<p><a href=\"/logout\">Logout</a></p>  </p></div>"+
				"<table  width=\"50%\" align=\"center\">";
		
		ArrayList<String> results;
		try {
			results = (ArrayList<String>) DBHelper.getFavs(name);
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
		
		writer.println(style+responseHtmlContent);
		
		
	}
	
	
	
}
