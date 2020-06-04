package com.dorsa.UITestingScript;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import com.dorsa.LogFormatter.GlobalVariableinUse;
import com.dorsa.LogFormatter.LogFormatterClass;
import com.dorsa.LogFormatter.MyHtmlFormatter;

public class LoggerFile {

	public LoggerFile() {
		// TODO Auto-generated constructor stub
	}

	//public final static Logger LOGGER = Logger.getLogger(LoggerFile.class.getName());
	public static FileHandler filetext;
	public static LogFormatterClass formatlogs;
	static private FileHandler fileHTML;
	static private MyHtmlFormatter formatterHTML;

	
	
	public static void setuplogger() {
		try {
			//Get Global logger
			@SuppressWarnings("static-access")
			Logger loggerglobal = GlobalVariableinUse.LOGGER.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
			//Suppress the logging output to the console
			
			//@SuppressWarnings("static-access")
			//Logger rootlogger = GlobalVariableinUse.LOGGER.getLogger("");
			//Handler[] handlers = rootlogger.getHandlers();
			
			filetext = new FileHandler("Logging.txt");
			fileHTML = new FileHandler("Logging.html");
			
			//create the text file logger
			formatlogs = new LogFormatterClass();
			filetext.setFormatter(formatlogs);
			loggerglobal.addHandler(filetext);
			
			formatterHTML = new MyHtmlFormatter();
	        fileHTML.setFormatter(formatterHTML);
	        loggerglobal.addHandler(fileHTML);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
	
}
