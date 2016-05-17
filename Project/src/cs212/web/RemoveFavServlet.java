package cs212.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs212.Sql.DBHelper;

public class RemoveFavServlet extends BaseServlet{
	/**
	 * removes a song from favs for a user
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		if(name==null){
			response.sendRedirect(response.encodeRedirectURL("/login?status=login"));
			return;
		}
		String id = request.getParameter("trackId");
		
		try {
			DBHelper.deleteFav(name, id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.sendRedirect(response.encodeRedirectURL("/favs"));
		
		
		
	}
}
