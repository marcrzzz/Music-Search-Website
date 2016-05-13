package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cs212.Sql.DBHelper;
import cs212.data.*;

public class SongsServlet extends BaseServlet{

	
	private static final Logger logger = LogManager.getLogger(SongsServlet.class.getName());
	
	/**
	 * if GET request done on /songs it redirects to search for songs
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		logger.trace("get request done on songs url, redirecting to search");
		response.sendRedirect(response.encodeRedirectURL("/search"));
	}
	
	
	/**
	 * POST Shows similar songs based on the query from /search
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		
		String q = request.getParameter("query");
		String type = request.getParameter("t");
		ConcurrentMusicLibrary lib = (ConcurrentMusicLibrary) request.getServletContext().getAttribute("musiclib");
		JSONObject songs = null;
		if(type.equals("artist")){
			songs = lib.getSongsByArtist(q);
		}
		else if(type.equals("title")){
			songs = lib.getSongsByTitle(q);
		}
		else if(type.equals("tag")){
			songs = lib.getSongsByTag(q);
		}
		
		JSONArray similars = (JSONArray) songs.get("similars");
		String search = searchHTML();
		String userInfo = userInfo(name);
		String style = style("Songs", "Discover Music");
		
		String responseNoSimilarsHtml = 
				"<div class=\"p\"><p>Sorry there are no similars to "+ q +"! Try another search!</p></div>";
		
		
		StringBuilder responseHtmlContent = new StringBuilder();
		responseHtmlContent.append("<div class=\"p\"><p>If you like "+ q  +" then you might also like...</p></div>"+
				"<table  width=\"50%\" align=\"center\">" +				
				"<tr><td><strong>Artist</strong></td><td><strong>Song Title</strong></td>");
		
		String img = "<img id=\"myImage\" onclick=\"changeImage(this)\" src=\"http://www.clker.com/cliparts/T/y/k/o/D/E/white-star-md.png\" alt=\"fav?\" style=\"width:20px;height:20px;\">";
		String img2 = "<img id=\"myImage2\"  src=\"http://pngimg.com/upload/star_PNG1595.png\" alt=\"fav\" style=\"width:20px;height:20px;\">";
		ArrayList<String> favs = null;
		try {
			favs = DBHelper.getFavs(name);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		if(name != null){
			responseHtmlContent.append("<td><strong>Add to Favs</strong></td></tr>");
		for(Object s: similars){
			JSONObject o = (JSONObject) s;
			String id = (String) o.get("trackId");
			
			if(favs.contains(id)){
				responseHtmlContent.append("<tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td><td>  "+img2+"</td></tr>");
				
			}
			else{
				responseHtmlContent.append("<tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td><td><a href=\"/addfav?trackID="+o.get("trackId")+"\">"+img+"</a></td></tr>");
			}
		}
		
		}
		else{
			for(Object s: similars){
				JSONObject o = (JSONObject) s;
				
				responseHtmlContent.append("</tr><tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td></tr>");
						
			}
		}
		responseHtmlContent.append("</table>"+
						"<script>"+
						"function changeImage(img){"+
						"   img.src = \"http://pngimg.com/upload/star_PNG1595.png\";}"+
						
						
						"</script>;"+
							"</body></html>");
		
		String htmlString = responseHtmlContent.toString();
		PrintWriter writer = prepareResponse(response);
		String allResponses = style+search;
		String login = "<div class=\"userInfo\"><p><a href=\"/signup?status=null\">Create Account</a> </p></div>";
		if(name != null){
			logger.trace("user logged in using search");
			if(similars.size()!=0){
				writer.println(allResponses+htmlString+userInfo);
			}
			else{
				writer.println(allResponses+responseNoSimilarsHtml+userInfo);
			}
		}
		else{
			logger.trace("No user logged in using search");
			if(similars.size()!=0){
				writer.println(allResponses+login+htmlString);
			}
			else{
				writer.println(allResponses+login+responseNoSimilarsHtml);
			}
		}
		
		
		
	}
}
