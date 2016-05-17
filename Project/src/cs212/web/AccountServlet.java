package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs212.Sql.DBHelper;

public class AccountServlet extends BaseServlet{
	/**
	 * Allows user to change something of their account
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		
		if((String) session.getAttribute("name")==null){
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		String status = request.getParameter("status");
		String name = (String) session.getAttribute("name");
		String style = style("Account", "Account Settings");
		String userInfo = userInfo(name);
		
		
		String error = "";
		if(status!=null){
			if(status.equals("error")){
				error+="<p><font color=#8B008B>Entered current password wrong</font></p>";
			}
			if(status.equals("nomatch")){
				error+="<p><font color=#8B008B>New passwords don't match</font></p>";
			}
			
			
		}
		
		String content="<form action=\"settings\" method=\"post\">" +
				"Current Password: "+
				"<input type=\"password\" name=\"curr\">" +
				"<p> </p>"+
				"New Password: "+
				"<input type=\"password\" name=\"new1\">" +
				"<p> </p>"+
				"New Password Again: "+
				"<input type=\"password\" name=\"new2\">" +
				"<input type=\"submit\" value=\"Enter\"><br/>" +
				"</form></body></html>";
		PrintWriter writer = prepareResponse(response);
		writer.println(style+error+content+userInfo);
		
		
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String currPwd = request.getParameter("curr");
		String newPwd = request.getParameter("new1");
		String newPwd2 = request.getParameter("new2");
		
		String name = (String) session.getAttribute("name");
		String userInfo = userInfo(name);
		try {
			if(!DBHelper.checkUser(name, currPwd)){
				response.sendRedirect(response.encodeRedirectURL("/settings?status=error"));
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!newPwd.equals(newPwd2)){
			response.sendRedirect(response.encodeRedirectURL("/settings?status=nomatch"));
			return;
		}
		
		
		String style = style("Account","Password Change Successful");
		String content = "<a class=\"a\" href=\"/search\" > Go back to Search <a></body></html>";
		try {
			DBHelper.changePwd(name, newPwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter writer = prepareResponse(response);
		writer.println(style+content+userInfo);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
