package logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.storage.Storage;


public class Functions {
	
	
	
	public static ArrayList<Task> taskList = Storage.XmltoTable(Constants.XML_FILE_PATH);
	public static int totalNumberOfTasks =  Storage.getMaxNumberOfTasks();

	
    public static String add(String parameters) throws IOException {
    	
    	String[] inputArray = parameters.split("-");
    	
    	AddTask.add(inputArray);
 
    	Storage.setMaxNumberOfTasks(Storage.getMaxNumberOfTasks() + 1);
    	Storage.tableToXml(Constants.XML_FILE_PATH, taskList);
    	
    	
//    	FileWriter fw = new FileWriter(Constants.FILENAME, true);
//    	BufferedWriter bw = new BufferedWriter(fw);
//    	
//    	bw.write(parameters + "\n");
//    	bw.close();
//    	
    	return "[ADD]Parameters entered are: " + parameters;
    }
    
    public static String edit(String parameters) {
    	return "[EDIT]Parameters entered are: " + parameters;
    }
    
    public static String delete(String parameters) {
    	return "[DELETE]Parameters entered are: " + parameters;
    }
    
    public static String list(String parameters) {
    	return ListTask.listTask(taskList).toString();
//    	return "[LIST]Parameters entered are: " + parameters;
    }
    
    
    
}
