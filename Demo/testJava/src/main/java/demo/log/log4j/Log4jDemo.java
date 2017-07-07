package demo.log.log4j;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jDemo {

	private static Logger logger = Logger.getLogger(Log4jDemo.class);

	public static void main(String[] args) {
		configuredProperties();
	}

	/**
	 * SimpleLayout和 FileAppender
	 */
	public static void fileAppender() {
		SimpleLayout layout = new SimpleLayout();
		FileAppender appender = null;

		try {
			appender = new FileAppender(layout, "d:/cache/log/output1.txt", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addAppender(appender);

		// logger.setLevel(Level.DEBUG);
		logger.setLevel(Level.INFO);
		// logger.setLevel(Level.ERROR);
		// logger.setLevel(Level.WARN);
		// logger.setLevel(Level.ALL);

		logger.debug("Here is some DEBUG");
		logger.info("Here is some INFO");
		logger.warn("Here is some WARN");
		logger.error("Here is some ERROR");
		logger.fatal("Here is some FATAL");
	}

	/**
	 * HTMLLayout和WriterAppender
	 */
	public static void writerAppender() {
		HTMLLayout layout = new HTMLLayout();
		WriterAppender appender = null;

		try {
			FileOutputStream output = new FileOutputStream("d:/cache/log/output2.html");
			appender = new WriterAppender(layout, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.addAppender(appender);
		logger.setLevel((Level) Level.DEBUG);

		logger.debug("Here is some DEBUG");
		logger.info("Here is some INFO");
		logger.warn("Here is some WARN");
		logger.error("Here is some ERROR");
		logger.fatal("Here is some FATAL");

	}

	/**
	 * PatternLayout和 ConsoleAppender
	 */
	public static void consoleAppender() {

		// Note, %n is newline
		String pattern = "Milliseconds since program start: %r %n";
		pattern += "Classname of caller: %C %n";
		pattern += "Date in ISO8601 format: %d{ISO8601} %n";
		pattern += "Location of log event: %l %n";
		pattern += "Message: %m %n %n";

		PatternLayout layout = new PatternLayout(pattern);
		ConsoleAppender appender = new ConsoleAppender(layout);

		logger.addAppender(appender);

		logger.setLevel((Level) Level.DEBUG);

		logger.debug("Here is some DEBUG");
		logger.info("Here is some INFO");
		logger.warn("Here is some WARN");
		logger.error("Here is some ERROR");
		logger.fatal("Here is some FATAL");
	}

	public static void configuredXML() {
		DOMConfigurator.configure("WebContent/WEB-INF/resource/log4j.xml");
		logger.debug("Here is some DEBUG");
		logger.info("Here is some INFO");
		logger.warn("Here is some WARN");
		logger.error("Here is some ERROR");
		logger.fatal("Here is some FATAL");
	}
	
	public static void configuredProperties(){
		PropertyConfigurator.configure("WebContent/WEB-INF/resource/log4j.properties");

		logger.debug("Here is some DEBUG");
		logger.info("Here is some INFO");
		logger.warn("Here is some WARN");
		logger.error("Here is some ERROR");
		logger.fatal("Here is some FATAL");
	}
}
