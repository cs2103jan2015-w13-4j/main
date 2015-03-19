package pista.log;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
	
	private static Logger logger = null;
	private static FileHandler fh = null;
	
	public Logging(String className, String logFileName){
	}
	
	public static boolean initLog(String className, String logFileName){
		logger = Logger.getLogger(className);
		try {
			if(fh != null){
				fh.flush();
				fh.close();
				fh = null;
			}
			
			fh = new FileHandler(logFileName, true);
			logger.addHandler(fh);
			
			SimpleFormatter formatter = new SimpleFormatter(); 
			fh.setFormatter(formatter); 

			return true;
		} catch (SecurityException e) {
			logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		} catch (IOException e) {
			logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}
		
	}
	
	public static boolean closeLog(){
		try{
			if(fh != null){
				fh.close();
				fh = null;
			}
			return true;
		}catch (Exception e) {
			logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean logInfo(String msg){
		if(logger != null){
			logger.info(msg);
			return true;
		}
		return false;
	}
	
	public static boolean logSevere(String msg){
		if(logger != null){
			logger.severe(msg);
			return true;
		}
		return false;
	}
	
	public static boolean logWarning(String msg){
		if(logger != null){
			logger.warning(msg);
			return true;
		}
		return false;
	}
	
	public static boolean logFine(String msg){
		if(logger != null){
			logger.fine(msg);
			return true;
		}
		return false;
	}
	
	public static boolean logConfig(String msg){
		if(logger != null){
			logger.config(msg);
			return true;
		}
		return false;
	}
	
	
}
