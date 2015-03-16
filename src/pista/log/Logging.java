package pista.log;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
	
	private Logger logger = null;
	private FileHandler fh = null;
	
	public Logging(String className, String logFileName){
		logger = Logger.getLogger(className);
		try {
			fh = new FileHandler(logFileName,true);
			logger.addHandler(fh);
			
			SimpleFormatter formatter = new SimpleFormatter(); 
			fh.setFormatter(formatter); 
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public boolean logInfo(String msg){
		if(logger != null){
			logger.info(msg);
			return true;
		}
		return false;
	}
	
	public boolean logSevere(String msg){
		if(logger != null){
			logger.severe(msg);
			return true;
		}
		return false;
	}
	
	public boolean logWarning(String msg){
		if(logger != null){
			logger.warning(msg);
			return true;
		}
		return false;
	}
	
	public boolean logFine(String msg){
		if(logger != null){
			logger.fine(msg);
			return true;
		}
		return false;
	}
	
	public boolean logConfig(String msg){
		if(logger != null){
			logger.config(msg);
			return true;
		}
		return false;
	}
	
	
}
