package cs212.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends BaseServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(request.getSession()!=null){
		request.getSession().invalidate();
		}
		response.sendRedirect(response.encodeRedirectURL("/login?status=login"));
		
	}
}
