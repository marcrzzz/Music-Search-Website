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
				error+="<p><font color=#8B008B>Must be logged in to see Favs</font></p>";
			}
			
		}
		String responseHtml = "<html" + 
				"<head><title>Login</title><style> form {align:center; } h1{color: #8B008B; "
				+ "font: italic bold 30px Georgia, serif; } </style></head>" +
				"<body>" +
				"<h1>Login below</h1> ";
		
		
		String responseContent=
					"<form action=\"login?status=loggedin\" method=\"post\">" +
					
					
					"Username: "+
					"<input type=\"text\" name=\"userName\">" +
					"Password: "+
					"<input type=\"password\" name=\"pwd1\">" +
					"<p></p>"+
					"<input type=\"submit\" value=\"Submit\"><br/>" +
					"</form>" +
					"</body>" +
					"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml+error+responseContent);
		
		
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
		
		String responseHtml = "<html" + 
				"<head><title>Logged in</title><style>.sel {background-color: white; } h1{color: #8B008B; "
				+ "font: italic bold 30px Georgia, serif; } "+
				"a:link, a:visited {background-color: white; color: #8B008B;"+
				" text-decoration: none; }"+
				 ".userInfo{ color: #8B008B; font: italic bold 15px Georgia, serif; position:absolute; "+
				"top:10px; right:10px; } "+"</style></head>" +
				"<body>" +
				"<h1>Discover Music</h1> ";
		
		String userInfo = userInfo(userName);
		String searchStuff = searchHTML();
		
		String responseContent=
					
					searchStuff+
					userInfo+
					"</body>" +
					"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml+responseContent);
		
		
	}

}
