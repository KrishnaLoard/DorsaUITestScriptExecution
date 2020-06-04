package com.dorsa.LogFormatter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatterClass extends Formatter {

	public LogFormatterClass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		try {
			StringBuffer buf = new StringBuffer(1000);
			buf.append("<tr>\n");

			// colorize any levels >= WARNING in red
			if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
				buf.append("\t<td style=\"color:red\">");
				buf.append("<b>");
				buf.append(record.getLevel());
				buf.append("</b>");
			} else {
				buf.append("\t<td>");
				buf.append(record.getLevel());
			}

			buf.append("</td>\n");
			buf.append("\t<td>");
			buf.append(calcDate(record.getMillis()));
			buf.append("</td>\n");
			buf.append("\t<td>");
			buf.append(formatMessage(record));
			buf.append("</td>\n");
			buf.append("</tr>\n");

			return buf.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "Something wrong with Logger";
	}

	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

	public String getHead(Handler h) {
		return "<!DOCTYPE html>\n<head>\n<style>\n" + "table { width: 100% }\n" + "th { font:bold 10pt Tahoma; }\n"
				+ "td { font:normal 10pt Tahoma; }\n" + "h1 {font:normal 11pt Tahoma;}\n" + "</style>\n" + "</head>\n"
				+ "<body>\n" + "<h1>" + (new java.util.Date()) + "</h1>\n"
				+ "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n" + "<tr align=\"left\">\n"
				+ "\t<th style=\"width:10%\">Loglevel</th>\n" + "\t<th style=\"width:15%\">Time</th>\n"
				+ "\t<th style=\"width:75%\">Log Message</th>\n" + "</tr>\n";
	}
	public String getTail(Handler h) {
        return "</table>\n</body>\n</html>";
    }

}
