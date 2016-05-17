package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import cs212.data.ConcurrentMusicLibrary;
import cs212.data.Song;

public class ViewSongServlet extends BaseServlet{
	
	/**
	 * GET /song returns a web page containing information about a specific song.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		String songId = request.getParameter("songId");
		
		
		if(songId == null){
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		ConcurrentMusicLibrary lib = (ConcurrentMusicLibrary) request.getServletContext().getAttribute("musiclib");
		Song s = lib.getSongById(songId);
		String style = style("Song", s.getTitle());
		ArrayList<String> sims = s.getSimilars();
		StringBuilder html = new StringBuilder();
		html.append("<p> </p><a href:\"/search\" > Go back to Search </a><div class=\"songInfo\"> <h2> Title: </h2> <p>"+ s.getTitle() +"</p> <h2> Artist: </h2> <p>"+ s.getArtist() +"</p> <h2> Songs Similar: </h2>");
		

		if(sims==null){
			html.append("<p> no similar songs </p>");
		}
		else{
			for(String id: sims){
				if(lib.getSongById(id) != null){
					html.append("<p>"+ lib.getSongById(id).getTitle() +"</p>");
				}
				
			}
		}
		html.append("</div></body></html>");	
			
			
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
