package cs212.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs212.Sql.DBHelper;
import cs212.web.BaseServlet;

public class SignUpServlet extends BaseServlet{
	
	private static final Logger logger = LogManager.getLogger(SignUpServlet.class.getName());
	
	/**
	 * get method for signing up for an account
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.getAttribute("name") != null) {
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		
		String status = request.getParameter("status");
		String style = style("Sign Up", "Sign Up Below");
		
		String error = "";
		if(status!=null){
			if(status.equals("not_matched")){
				logger.trace("user's passwords not match");
				error+="<p><font color=#8B008B>***Passwords don't match***</font></p>";
			}
			else if (status.equals("username_taken")){
				logger.trace("user's username is not unique");
				error+="<p><font color=#8B008B>***Username is already taken***</font></p>";
			}
			else if(status.equals("error")){
				logger.trace("user left parts blank/empty");
				error+="<p><font color=#8B008B>***All spaces are required! None must be left blank***</font></p>";
			}
		}
		String responseContent=
				"<p>Already have an account? Click <a href=\"/login?status=login\">HERE</a> to sign in!</p>"+
					"<form action=\"signup\" method=\"post\">" +
					
					"Full Name: "+
					"<input type=\"text\" name=\"name\">" +
					"<p></p>"+
					"Username: "+
					"<input type=\"text\" name=\"userName\">" +
					"<p></p>Password: "+
					"<input type=\"password\" name=\"pwd1\">" +
					"<p></p>"+
					"Password again: "+
					"<input type=\"password\" name=\"pwd2\">" +
					"<p></p>"+
					"<input type=\"submit\" value=\"Submit\"><br/>" +
					"</form>" +
					"</body>" +
					"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(style+error+responseContent);
	}
	
	
	/**
	 * checks all info is correct and if so, adds to 
	 * the user database. Directs to sign up page if info not 
	 * good. Directs to search if sign up is successful
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			
			String pwd1 = request.getParameter("pwd1");
			String pwd2 = request.getParameter("pwd2");
			String name = request.getParameter("name");
			String userName = request.getParameter("userName");
			if(!pwd1.equals(pwd2) ){
				response.sendRedirect(response.encodeRedirectURL("/signup?status=not_matched"));
				return;
			}
			try {
				if(!DBHelper.noDuplicates(userName)){
					response.sendRedirect(response.encodeRedirectURL("/signup?status=username_taken"));
					return;
				}
			} catch (SQLException e1) {
				logger.error("SQL EXCEPTION");
				e1.printStackTrace();
			}
			if(pwd1.equals("") || name.equals("")  || userName.equals("") ){
				response.sendRedirect(response.encodeRedirectURL("/signup?status=error"));
				return;
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("name", userName);
			String style = style("Song Finder", "Discover Music");
	
			
			
			try {
				DBHelper.addUser(name, userName, pwd1);
			} catch (SQLException e) {
				logger.error("SQL EXCEPTION");
				e.printStackTrace();
			}
			
			String userInfo = userInfo(userName);
			String searchStuff = searchHTML("none");
			
			String responseContent=
						
						searchStuff+
						"<p><a class=\"a\" href=\"/search?status=private\"> go private </p>"+
						userInfo+
						"</body>" +
						"</html>";
			
			PrintWriter writer = prepareResponse(response);
			writer.println(style+responseContent);
			
			
		}

}
