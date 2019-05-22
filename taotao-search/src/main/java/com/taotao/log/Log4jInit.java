package com.taotao.log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * Servlet implementation class Log4jInit
 */

public class Log4jInit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Log4jInit() {
		super();
	}

	@Override
	public void init() throws ServletException {
		System.setProperty("taotaoLogRootDir",
				this.getInitParameter("taotaoLogRootDir"));
		System.setProperty("taotaoVersion",
				this.getInitParameter("taotaoVersion"));

		String file = this.getInitParameter("taotaoLog4j");
		file = this.getServletConfig().getServletContext().getRealPath("/")
				+ file;
		if (file != null) {
			DOMConfigurator.configure(file);
		}
	}

}
