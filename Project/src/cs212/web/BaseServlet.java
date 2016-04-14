package cs212.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet superclass with printWriter method for all servlets for this application.
 * 
 *
 */
public class BaseServlet extends HttpServlet{
	
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();
	}

}
