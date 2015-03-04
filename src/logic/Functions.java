package logic;

import java.io.*;

public class Functions {
    public static String add(String parameters) throws IOException {
    	FileWriter fw = new FileWriter(Constants.FILENAME, true);
    	BufferedWriter bw = new BufferedWriter(fw);
    	
    	bw.write(parameters + "\n");
    	bw.close();
    	
    	return "[ADD]Parameters entered are: " + parameters;
    }
    
    public static String edit(String parameters) {
    	return "[EDIT]Parameters entered are: " + parameters;
    }
    
    public static String delete(String parameters) {
    	return "[DELETE]Parameters entered are: " + parameters;
    }
    
    public static String list(String parameters) {
    	return "[LIST]Parameters entered are: " + parameters;
    }
}
