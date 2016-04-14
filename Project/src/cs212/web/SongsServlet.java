package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cs212.data.*;

public class SongsServlet extends BaseServlet{

	/**
	 * POST Shows similar songs based on the query from /search
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
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
		
		
		String responseHtml = "<html" + 
				"<head><title>Songs</title><style>"+
				"table, th, td {border: 3px solid black;"+
				"border-collapse: collapse; border-color: #8B008B;}"+
				".sel {background-color: white; }" +
				"p{text-align: center; font: italic bold 15px Georgia, serif;}"+
				"</style></head>" +
				"<body>";
		
		String responseNoSimilarsHtml = 
				"<body><p>Sorry there are no similars to "+ q +"! Try another search!</p>";
		
		String responseSearchHtml ="<form action=\"songs\" method=\"post\">" +
				"Search by:"+
				"<select name=\"t\" class=\"sel\">"+
				  "<option value=\"artist\">Artist</option>"+
				 " <option value=\"title\">Title</option>"+
				 " <option value=\"tag\">Tag</option>"+
				"</select>"+
				"<p> </p>"+
				"Artist/Title/Tag: "+
				"<input type=\"text\" name=\"query\">" +
				"<input type=\"submit\" value=\"Search\"><br/>" +
				"</form>";
		
		
		String responseHtmlContent = "<p>If you like "+ q  +" then you might also like...</p>"+
				"<table  width=\"50%\" align=\"center\">" +				
				"<tr><td><strong>Artist</strong></td><td><strong>Song Title</strong></td></tr>";
		
		
		for(Object s: similars){
			JSONObject o = (JSONObject) s;
			
			responseHtmlContent+="<tr><td>"+o.get("artist") +"</td><td>"+ o.get("title") +"</td></tr>";
					
		}
		responseHtmlContent+="</table>"+
							"</body></html>";
		
		
		
		
		PrintWriter writer = prepareResponse(response);
		
		if(similars.size()!=0){
			writer.println(responseHtml+responseSearchHtml+responseHtmlContent);
		}
		else{
			writer.println(responseHtml+responseSearchHtml+responseNoSimilarsHtml);
		}
		
		
	}
}
