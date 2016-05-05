package cs212.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs212.Sql.DBHelper;

public class AddFavServlet extends BaseServlet{
	
	private static final Logger logger = LogManager.getLogger(AddFavServlet.class.getName());

	/**
	 * saves a song to database as a favorite
	 * for the current user
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		if(name==null){
			logger.trace("User tried addFav url without signing in");
			response.sendRedirect(response.encodeRedirectURL("/login?status=login"));
			return;
		}
		String id = request.getParameter("trackID");
		if(id!=null){
			try {
				DBHelper.addfav(name, id);
				logger.trace("user adding song to favs");
			} catch (SQLException e) {
				logger.error("SQL exception");
				e.printStackTrace();
			}
		}
		
		response.sendRedirect(response.encodeRedirectURL("/favs"));
		
	}
}
