package es.weso.util;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 * Initialises Log4J
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 05/04/2013
 */
public class Log4JInitServlet extends HttpServlet {

	private static final long serialVersionUID = -3851999887869228226L;

	public void init(ServletConfig config) throws ServletException {
		System.out.println("Log4JInitServlet is initializing log4j");
		String log4jLocation = config
				.getInitParameter("log4j-properties-location");
		ServletContext sc = config.getServletContext();
		if (log4jLocation == null) {
			System.err.println("No log4j-properties-location init param,"
					+ " so initialising log4j with BasicConfigurator");
			BasicConfigurator.configure();
		} else {
			String webAppPath = sc.getRealPath("/");
			String log4jProp = webAppPath + log4jLocation;
			File confFile = new File(log4jProp);
			if (confFile.exists()) {
				System.out.println("Initialising log4j with: " + log4jProp);
				PropertyConfigurator.configure(log4jProp);
			} else {
				System.err.println(log4jProp + " not found, "
						+ "initialising log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		super.init(config);
	}
}