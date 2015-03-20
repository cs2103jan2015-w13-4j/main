package pista.log;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import pista.Constants;

public class CustomLogging {
	
	private static CustomLogging mLogging = null;
	private static Logger mLogger = null;
	private static FileHandler fh = null;
	
	private CustomLogging(){ //make sure this class cannot be instantiate
	}
	
	//Implement singleton pattern
	public static CustomLogging getInstance(String className){
		mLogging = new CustomLogging();
		mLogging.initLog(className, Constants.LOG_FILE_NAME);
		return mLogging;
	}
	
	private boolean initLog(String className, String logFileName){
		mLogger = Logger.getLogger(className);
		try {
			/*
			if(fh != null){
				fh.flush();
				fh.close();
				fh = null;
			}
			*/
			
			fh = new FileHandler(logFileName, true);
			mLogger.addHandler(fh);
			
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
	
	public boolean closeLog(){
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
	
	public boolean logInfo(String msg){
		if(mLogger != null){
			mLogger.info(msg);
			return true;
		}
		return false;
	}
	
	public boolean logSevere(String msg){
		if(mLogger != null){
			mLogger.severe(msg);
			return true;
		}
		return false;
	}
	
	public boolean logWarning(String msg){
		if(mLogger != null){
			mLogger.warning(msg);
			return true;
		}
		return false;
	}
	
	public boolean logFine(String msg){
		if(mLogger != null){
			mLogger.fine(msg);
			return true;
		}
		return false;
	}
	
	public boolean logConfig(String msg){
		if(mLogger != null){
			mLogger.config(msg);
			return true;
		}
		return false;
	}
	
	
}
