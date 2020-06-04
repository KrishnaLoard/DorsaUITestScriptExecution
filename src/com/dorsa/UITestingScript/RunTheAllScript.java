package com.dorsa.UITestingScript;

import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;

import com.dorsa.LogFormatter.GlobalVariableinUse;
import com.dorsa.ModelsEmail.EmailSenderModel;

public class RunTheAllScript {
	static {

		LoggerFile.setuplogger();

	}

	public RunTheAllScript() {
		System.out.println("Initilizing the Constructor");
	}

	public boolean runthescriptwithpath(String path) {
		try {

			GlobalVariableinUse.LOGGER.setLevel(Level.ALL);
			GlobalVariableinUse.LOGGER.log(Level.INFO, "Starting the Script Execution");
			GlobalVariableinUse.LOGGER.log(Level.INFO, "Creating Process Builder");
			File file = new File(path);
			Commandline commandline = new Commandline();
			commandline.setExecutable(file.getAbsolutePath());
			WriterStreamConsumer systemOut = new WriterStreamConsumer(new OutputStreamWriter(System.out));
			WriterStreamConsumer systemErr = new WriterStreamConsumer(new OutputStreamWriter(System.out));

			if (new File("newman").isDirectory()) {
				// Delete first reports first
				File pathnewman = new File("newman");
				String[] getlist = pathnewman.list();
				for (int i = 0; i < getlist.length; i++) {
					File file2 = new File(pathnewman, getlist[i]);
					GlobalVariableinUse.LOGGER.log(Level.INFO, "Deleting Previous record" + file2.getName());
					file2.delete();
				}

			}

			GlobalVariableinUse.LOGGER.log(Level.INFO, "Processbuiler Created");
			GlobalVariableinUse.LOGGER.log(Level.INFO, "Running The Scripts");

			int returnCode = CommandLineUtils.executeCommandLine(commandline, systemOut, systemErr);

			if (returnCode != 0) {
				GlobalVariableinUse.LOGGER.log(Level.INFO, "Command Executed Check the report " + systemOut);
				System.out.println("Something Bad Happened!");
			} else {

				System.out.println("Script is executed");
				GlobalVariableinUse.LOGGER.info("Script executed");
			}

			try (Stream<Path> walk = Files.walk(Paths.get("newman\\"))) {

				List<String> resultsofstring = walk.filter(Files::isRegularFile).map(x -> x.toString())
						.collect(Collectors.toList());
				resultsofstring.forEach(System.out::println);
				sendreportinemail("html", resultsofstring);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// GlobalVariableinUse.LOGGER.log(Level.INFO,"Script is Run Successfully");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			GlobalVariableinUse.LOGGER.log(Level.SEVERE, "Error While Running The Script");
			GlobalVariableinUse.LOGGER.log(Level.SEVERE, e.getMessage());
		}

		return false;
	}

	public static boolean sendreportinemail(String format, List<String> file) {
		try {

			Properties properties = new Properties();
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");

			List<EmailSenderModel> emailSenderModels = getemailsfromtheDB();

			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// TODO Auto-generated method stub
					return new PasswordAuthentication(GlobalVariableinUse.username, GlobalVariableinUse.password);
				}

			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(GlobalVariableinUse.username));
			
			
			//emailstring build email string from the database to whom the Report needs to be sent
			
			String emailstring = "";
			// Creating email by running loop from the list of the emails
			for (int i = 0; i < emailSenderModels.size(); i++) {
				EmailSenderModel condition = emailSenderModels.get(i);
				System.out.println("String Compare " + condition.getPropname());
				if (condition.getPropname().trim().equalsIgnoreCase("email")) {
					if (i == (emailSenderModels.size()-1)) {
						emailstring = emailstring.trim() + condition.getPropvalue().trim();
					}else {
						System.out.println(emailstring);
						emailstring = emailstring.trim() + condition.getPropvalue().trim() + ",";
					}
				}
			}
			
			//Printing the Email string intot the Logger
			GlobalVariableinUse.LOGGER.info(emailstring);
			
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("kishan.bheemajiyani@gmail.com"));
			message.setSubject("Report From the Automation Run");
			message.setText("System Generated Email for the Reporting the Report");

			
			//Creating Multiple Part of attachments into the email
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(message, "text/html");
			messageBodyPart.setText("System Generated Email for the Reporting the Report");
			multipart.addBodyPart(messageBodyPart);

			for (String string : file) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(new File(string));
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName(new File(string).getName());
				multipart.addBodyPart(attachmentBodyPart);
			}
			message.setContent(multipart);
			Transport.send(message);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}

	private static List<EmailSenderModel> getemailsfromtheDB() {
		// TODO Auto-generated method stub

		try {

			// Getting the email from the database
			String url = "jdbc:postgresql://localhost/KrishnasTestingDatabase";
			Properties properties2 = new Properties();
			properties2.setProperty("user", "postgres");
			properties2.setProperty("password", "kjbkishan");
			properties2.setProperty("ssl", "false");

			// String SQL Query
			String SQL_QUERY = "SELECT * FROM propertiesvalue";

			Connection conn = DriverManager.getConnection(url, properties2);
			List<EmailSenderModel> emailSenderModels = new ArrayList<EmailSenderModel>();

			PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				EmailSenderModel emailmodel = new EmailSenderModel();
				int getid = resultSet.getInt(1);
				emailmodel.setPropid(getid);

				String propname = resultSet.getString(2);
				emailmodel.setPropname(propname);

				String propvalue = resultSet.getString("propvalue");
				emailmodel.setPropvalue(propvalue);

				GlobalVariableinUse.LOGGER.info(getid + " " + propname + " " + propvalue);

				emailSenderModels.add(emailmodel);

			}
			GlobalVariableinUse.LOGGER.info("Number of emails " + emailSenderModels.size());
			return emailSenderModels;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
