package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cs212.Sql.DBHelper;
import cs212.api.FetchInfo;

public class ArtistInfoServlet extends BaseServlet{
	
	/**
	 * GET /artist returns a web page containing information about a specific artist.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		
		String artist = request.getParameter("name");
		
		if(artist == null){
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		
		
		String style = style("ArtistInfo", artist);
		String htmlContent = "";
		try {
			JSONObject obj = DBHelper.getArtist(artist);
			if(obj==null){
				htmlContent+="<div class=\"songInfo\"> <h2> Artist: </h2> <p>"+ artist + "</p>"+ 
						"<h2> Listeners: </h2> <p> N/A </p> <h2> PlayCount: </h2><p> N/A </p>"+
						"<h2> Bio: </h2><p> bio not available </p>";
			}
			else if(obj.get("bio")!=null && !obj.get("listeners").equals(0)&& !obj.get("playcount").equals(0)){
				FetchInfo f = new FetchInfo(artist, "artist.getInfo");
				FetchInfo e = new FetchInfo(artist, "artist.getTopTracks");
				JSONObject i = e.getInfo();
				JSONObject i1 = (JSONObject) i.get("toptracks");
				JSONArray i2 = (JSONArray) i1.get("track");
				if(i2.size()!=0){
					JSONObject i3 = (JSONObject) i2.get(0);
					JSONObject o = f.getInfo();
					JSONObject inner = (JSONObject) o.get("artist");
					JSONArray ar = (JSONArray) inner.get("image");
					JSONObject smallImg = (JSONObject) ar.get(1);
					htmlContent+="<div class=\"songInfo\"> <img src=\""+ smallImg.get("#text") +"\" alt=\"Artist Photo\">"+
							 "<h2> Artist: </h2> <p>"+ artist + "</p>"+ 
							 "<h2>Top Track:</h2> <p>"+ i3.get("name") +"</p ><p><a   href=\""+ i3.get("url") +"  \" >"+i3.get("url")+"</a></p>"+
							"<h2> Listeners: </h2> <p>"+ obj.get("listeners") + "</p> <h2> PlayCount: </h2><p>"+ obj.get("playcount") + "</p>"+
							"<h2> Bio: </h2><p> "+ obj.get("bio") + " </p>";
				}
				else{
					JSONObject o = f.getInfo();
					JSONObject inner = (JSONObject) o.get("artist");
					JSONArray ar = (JSONArray) inner.get("image");
					JSONObject smallImg = (JSONObject) ar.get(1);
					htmlContent+="<div class=\"songInfo\"><img src=\""+ smallImg.get("#text") +"\" alt=\"Artist Photo\"> <h2> Artist: </h2> <p>"+ artist + "</p>"+
							"<h2> Listeners: </h2> <p>"+ obj.get("listeners") + "</p> <h2> PlayCount: </h2><p>"+ obj.get("playcount") + "</p>"+
							"<h2> Bio: </h2><p> bio not available </p>";
				}
			}
			else if(obj.get("listeners")==null || obj.get("playcount")==null){
					htmlContent+="<div class=\"songInfo\"> <h2> Artist: </h2> <p>"+ artist + "</p>"+
							"<h2> Listeners: </h2> <p>0</p> <h2> PlayCount: </h2><p>0</p>"+
							"<h2> Bio: </h2><p> bio not available </p>";
				
			}
			else{
				htmlContent+="<div class=\"songInfo\"> <h2> Artist: </h2> <p>"+ artist + "</p>"+
						"<h2> Listeners: </h2> <p>"+ obj.get("listeners") + "</p> <h2> PlayCount: </h2><p>"+ obj.get("playcount") + "</p>"+
						"<h2> Bio: </h2><p> bio not available </p>";
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		htmlContent+="</div><a href=\"/artists\" > Go back to all artists </a></body></html>";
		
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
