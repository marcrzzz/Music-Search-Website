package cs212.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs212.data.ConcurrentMusicLibrary;
import cs212.utils.BuildLibrary;

public class MusicServer {

	public static void main(String[] args) throws Exception{
		int port = 0;
		try{
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.err.println("port must be a number: "+e);
			return;
		}
		Server server = new Server(port);
		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
		server.setHandler(servhandler);
		
		servhandler.addEventListener(new ServletContextListener() {

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				//Do nothing when server shut down.
			}

			@Override
			public void contextInitialized(ServletContextEvent sce) {

				Path path = Paths.get("input/lastfm_subset");
				BuildLibrary lib = new BuildLibrary(path, 10);
				ConcurrentMusicLibrary library = lib.getMusicLibrary();
				
				sce.getServletContext().setAttribute("musiclib", library);
			}

		});
		
		servhandler.addServlet(SearchServlet.class, "/search");
		servhandler.addServlet(SongsServlet.class, "/songs");
		
		server.setHandler(servhandler);

		server.start();
		server.join();
		
	}
}
