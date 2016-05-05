package cs212.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cs212.data.ConcurrentMusicLibrary;
import cs212.utils.BuildLibrary;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class MusicServer {

	public static void main(String[] args) throws Exception{
		int port = 0;
		try{
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.err.println("port must be a number: "+e);
			return;
		}
		
		Configurator.setLevel(SearchServlet.class.getName(), Level.TRACE);
		Configurator.setLevel(SongsServlet.class.getName(), Level.TRACE);
		Configurator.setLevel(AddFavServlet.class.getName(), Level.TRACE);
		Configurator.setLevel(SearchServlet.class.getName(), Level.ERROR);
		Configurator.setLevel(SongsServlet.class.getName(), Level.ERROR);
		Configurator.setLevel(AddFavServlet.class.getName(), Level.ERROR);
		Configurator.setLevel(FavsServlet.class.getName(), Level.ERROR);
		Configurator.setLevel(LoginServlet.class.getName(), Level.TRACE);
		Configurator.setLevel(LoginServlet.class.getName(), Level.ERROR);
		Configurator.setLevel(SignUpServlet.class.getName(), Level.TRACE);
		Configurator.setLevel(SignUpServlet.class.getName(), Level.ERROR);
		
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
					BufferedReader reader;
					try {
						reader = Files.newBufferedReader(Paths.get("dbConfig.json"));
						JSONParser parser = new JSONParser();
						JSONObject object = (JSONObject) parser.parse(reader);
						String pathObj = (String) object.get("path");
						Path path = Paths.get(pathObj);
						BuildLibrary lib = new BuildLibrary(path, 10);
						ConcurrentMusicLibrary library = lib.getMusicLibrary();
						
						sce.getServletContext().setAttribute("musiclib", library);
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					
				
				
			}

		});
		
		servhandler.addServlet(SearchServlet.class, "/search");
		servhandler.addServlet(SongsServlet.class, "/songs");
		servhandler.addServlet(LoginServlet.class, "/login");
		servhandler.addServlet(SignUpServlet.class, "/signup");
		servhandler.addServlet(LogoutServlet.class, "/logout");
		servhandler.addServlet(FavsServlet.class, "/favs");
		servhandler.addServlet(AddFavServlet.class, "/addfav");
		
		server.setHandler(servhandler);

		server.start();
		server.join();
		
	}
}
