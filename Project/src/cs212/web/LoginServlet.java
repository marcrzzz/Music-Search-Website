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

public class LoginServlet extends BaseServlet{
	private static final Logger logger = LogManager.getLogger(LoginServlet.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		//if user is logged in, redirect
		if(session.getAttribute("name") != null) {
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		
		String status = request.getParameter("status");
		
		String error = "";
		if(status!=null){
			if(status.equals("error")){
				logger.trace("User inserted wrong Username/Password");
				error+="<p><font color=#8B008B>Username/Password is wrong</font></p>";
			}
			else if (status.equals("notLoggedIn")){
				logger.trace("User tried favs url without being signed in");
				error+="<p><font color=#8B008B>Must be logged in</font></p>";
			}
			
		}
		String style = style("Login", "Login Below");
		
		
		String responseContent=
					"<form action=\"login?status=loggedin\" method=\"post\">" +
					
					
					"Username: "+
					"<input type=\"text\" name=\"userName\">" +
					"Password: "+
					"<input type=\"password\" name=\"pwd1\">" +
					"<input type=\"submit\" value=\"Submit\"><br/>" +
					"</form>" +
					"</body>" +
					"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(style+error+responseContent);
		
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String pwd1 = request.getParameter("pwd1");
		String userName = request.getParameter("userName");
		
		try {
			if(!DBHelper.checkUser(userName, pwd1)){
				response.sendRedirect(response.encodeRedirectURL("/login?status=error"));
				return;
			}
		} catch (SQLException e) {
			logger.error("SQL exception");
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("name", userName);
		String style = style("Logged in", "Discover Music");
	
		
		
		String userInfo = userInfo(userName);
		String searchStuff = searchHTML("none");
		
		String responseContent=
					
					searchStuff+
					"<p><a  href=\"/search?status=private\"> go private </p>"+
					userInfo+
					"</body>" +
					"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(style+responseContent);
		
		
	}

}
