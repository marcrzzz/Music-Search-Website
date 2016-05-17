package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import cs212.Sql.DBHelper;
import cs212.api.FetchInfo;
import cs212.comparators.ByPlayCountComparator;
import cs212.comparators.ByTrackIdComparator;
import cs212.data.ConcurrentMusicLibrary;
import cs212.data.Song;

public class ArtistsServlet extends BaseServlet{
	
	
	/**
	 * GET /artists returns a list of all artist sorted alphabetically or by play count
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String style = style("Artists", "Artists");
		String sortBy = request.getParameter("sort");
		ConcurrentMusicLibrary lib = (ConcurrentMusicLibrary) request.getServletContext().getAttribute("musiclib");
		StringBuilder html = new StringBuilder();
		if(sortBy==null || sortBy.equals("alpha")){
			html.append("<a href=\" /artists?sort=count \" > Sort by Playcount </a>");
			html.append("<p> </p><a href=\" /search \" > Go back to Search </a>");
			TreeSet<String> a = lib.getArtists();
			html.append("<div class=\"songInfo\">");
			for(String s : a){
				html.append("<p> <a class=\"a\" href=\"artist?name="+ s +"\" >"+ s +"</a> </p><hr>");
				
			}
			
		} 
		
		else if(sortBy.equals("count")){
			html.append("<a href=\" /artists?sort=alpha \" > Sort Alphabetically </a>");
			html.append("<p> </p><a href=\" /search \" > Go back to Search </a>");
			try {
				TreeSet<JSONObject> byCounts = DBHelper.getArtists();
				html.append("<div class=\"songInfo\">");
				for(JSONObject o :  byCounts){
					html.append("<p> <a class=\"a\" href=\"artist?name="+  o.get("artist") +"\" >"+ o.get("artist") +"</a></p><hr>");
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			html.append("</body></html>");
		}
		
		
		String htmlContent = html.toString();
		PrintWriter writer = prepareResponse(response);
		if((String) session.getAttribute("name")==null){
			writer.println(style+htmlContent);
		}
		else{
			String name = (String) session.getAttribute("name");
			String userInfo = userInfo(name);
			writer.println(style+userInfo+htmlContent);
		}
	}
}
