package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs212.Sql.DBHelper;
import cs212.data.ConcurrentMusicLibrary;
import cs212.data.Song;

public class HistoryServlet extends BaseServlet{
	
	/**
	 * get method that displays 
	 * the history of searches for the user
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		
		
		
		if(name==null){
			response.sendRedirect(response.encodeRedirectURL("/login?status=notLoggedIn"));
			return;
		}
		
		String style = style("History", "History");
		
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"userInfo\"><font-color:white>~~~"+
				name+"~~~</font><p><a href=\"/search\">Search</a></p>"+
				"<p><a href=\"/favs\">My Favorites</a></p>"+
				"<p> <a href=\"/settings\">Account Settings</a></p>"+
				"<p> <a href=\"/clear\">Clear History</a></p>"+
				"<p><a href=\"/logout\">Logout</a></p>  </p></div>");
		
		
		ArrayList<String> resultsTitle;
		ArrayList<String> resultsArtist;
		ArrayList<String> resultsTag;
		
		try {
			resultsTitle = (ArrayList<String>) DBHelper.showHistory(name, "title");
		
			html.append("<table  style=\"float:left;\">");
			html.append("<tr><th> By Title </th></tr>");
			if(resultsTitle!=null){
			for(String t: resultsTitle){
				if(t!=null){
				html.append("<tr><td>"+ t +"</td></tr>");
				}
			}
			}
			html.append("</table>");
			resultsArtist = (ArrayList<String>) DBHelper.showHistory(name, "artist");
			html.append("<table  style=\"float:left;\">");
			html.append("<tr><th> By Artist </th></tr>");
			if(resultsArtist!=null){
			for(String a: resultsArtist){
				if(a!=null){
				html.append("<tr><td>"+ a +"</td></tr>");
				}
			}
			}
			html.append("</table>");
			resultsTag = (ArrayList<String>) DBHelper.showHistory(name, "tag");
			html.append("<table  style=\"float:left;\">");
			html.append("<tr><th> By Tag </th></tr>");
			if(resultsTag!=null){
			for(String t: resultsTag){
				html.append("<tr><td>"+ t +"</td></tr>");

			}
			}
			html.append("</table>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		html.append("</table>"+
				"</body></html>");
		
		
		
		
		String responseHtmlContent = html.toString();
		PrintWriter writer = prepareResponse(response);
		
		writer.println(style+responseHtmlContent);
		
		
	}
	
	
}
