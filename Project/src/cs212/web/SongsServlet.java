package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
		
		String responseHtml = "<html" + 
				"<head><title>Songs</title><style>"+
				"table, th, td {border: 3px solid black;"+
				"border-collapse: collapse; border-color: #8B008B;}"+
				"h1{color: #8B008B; font: italic bold 30px Georgia, serif; }"+
				".sel {background-color: white; }" +
				"a:link, a:visited {background-color: white; color: #8B008B;"+
			    "  text-decoration: none;  }"+
				".userInfo{ color: #8B008B; font: italic bold 15px Georgia, serif; position:absolute; "+
				"top:10px; right:10px; } "+
				".p{text-align: center; font: italic bold 15px Georgia, serif;}"+
				"</style></head>" +
				"<body><h1>Discover Music</h1>";
		
		String responseNoSimilarsHtml = 
				"<div class=\"p\"><p>Sorry there are no similars to "+ q +"! Try another search!</p></div>";
		
		
		
		String responseHtmlContent = "<div class=\"p\"><p>If you like "+ q  +" then you might also like...</p></div>"+
				"<table  width=\"50%\" align=\"center\">" +				
				"<tr><td><strong>Artist</strong></td><td><strong>Song Title</strong></td>";
		

		String img = "<img id=\"myImage\" onclick=\"changeImage(this)\" src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/White_Stars_1.svg/2000px-White_Stars_1.svg.png\" alt=\"fav?\" style=\"width:20px;height:20px;\">";
		String img2 = "<img id=\"myImage2\"  src=\"http://pngimg.com/upload/star_PNG1595.png\" alt=\"fav\" style=\"width:20px;height:20px;\">";
		if(name != null){
			responseHtmlContent+="<td><strong>Add to Favs</strong></td></tr>";
		for(Object s: similars){
			JSONObject o = (JSONObject) s;
			String id = (String) o.get("trackId");
			try {
				if(DBHelper.checkFav(name, id)){
					responseHtmlContent+="<tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td><td>  "+img2+"</td></tr>";
					
				}
				else{
					responseHtmlContent+="<tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td><td><a href=\"/addfav?trackID="+o.get("trackId")+"\">"+img+"</a></td></tr>";
					
				}
				
			} catch (SQLException e) {
				logger.error("SQL EXCEPTION");
				e.printStackTrace();
			}
			
		}
		}
		else{
			for(Object s: similars){
				JSONObject o = (JSONObject) s;
				
				responseHtmlContent+="</tr><tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td></tr>";
						
			}
		}
		responseHtmlContent+="</table>"+
						"<script>"+
						"function changeImage(img){"+
						"   img.src = \"http://pngimg.com/upload/star_PNG1595.png\";}"+
						
						
						"</script>;"+
							"</body></html>";
		
		
		
		
		PrintWriter writer = prepareResponse(response);
		String allResponses = responseHtml+search;
		String login = "<div class=\"userInfo\"><p><a href=\"/signup?status=null\">Create Account</a> </p></div>";
		if(name != null){
			logger.trace("user logged in using search");
			if(similars.size()!=0){
				writer.println(allResponses+responseHtmlContent+userInfo);
			}
			else{
				writer.println(allResponses+responseNoSimilarsHtml+userInfo);
			}
		}
		else{
			logger.trace("No user logged in using search");
			if(similars.size()!=0){
				writer.println(allResponses+login+responseHtmlContent);
			}
			else{
				writer.println(allResponses+login+responseNoSimilarsHtml);
			}
		}
		
		
		
	}
}
