package ch.sniffdatel.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLogger {
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	static public void setup() throws IOException {

		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.SEVERE);
		String path = "";

		if (System.getProperty("os.name").compareTo("Linux") == 0) {
			path = "/var/log/sniffdatel/error.log";
			createLogfile(path);
		} else {
			path = System.getProperty("user.home") + "\\sniffdatel\\error.log";
			createLogfile(path);
		}

		fileTxt = new FileHandler(path);

		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}

	private static void createLogfile(String path) throws IOException {
		File logfile = new File(path);
		logfile.getParentFile().mkdirs();
		logfile.createNewFile();
	}

}
