package cs212.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs212.Sql.DBHelper;

public class ClearHistoryServlet extends BaseServlet {
	
	
	/**
	 * clears all history for user
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		if(name==null){
			response.sendRedirect(response.encodeRedirectURL("/login?status=login"));
			return;
		}
		
		try {
			DBHelper.deleteHistory(name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect(response.encodeRedirectURL("/history"));
		
	}
}
