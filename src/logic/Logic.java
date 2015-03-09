package logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.storage.Storage;


public class Logic {
	
	private String[] parameterToken = null;
	
	public static ArrayList<Task> taskList = Storage.XmltoTable(Constants.XML_FILE_PATH);
	public static int totalNumberOfTasks =  Storage.getMaxNumberOfTasks();

	/*
    public static String add(String parameters) throws IOException {
    	
    	String[] inputArray = parameters.split("-");
    	
    	LogicAddTask.add(inputArray);
 
    	Storage.setMaxNumberOfTasks(Storage.getMaxNumberOfTasks() + 1);
    	Storage.tableToXml(Constants.XML_FILE_PATH, taskList);
    	
    	
//    	FileWriter fw = new FileWriter(Constants.FILENAME, true);
//    	BufferedWriter bw = new BufferedWriter(fw);
//    	
//    	bw.write(parameters + "\n");
//    	bw.close();
//    	
    	return "[ADD]Parameters entered are: " + parameters;
    }*/
    
	/*
	 * @param is correct 
	 * */
    public static String add(String param){
    	boolean isAddedToStorage = false;
    	boolean isAddedToArray = false;
    	
    	String[] inputArray = param.split("-");
    	Task newTask = null;
    	
    	newTask = LogicAddTask.constructNewTask(inputArray);
    	
    	isAddedToArray = addTaskToArrayTask(newTask);
    	isAddedToStorage = Storage.XmlAddTask(Constants.XML_FILE_PATH, newTask);
    	
    	if(isAddedToArray && isAddedToStorage){
    		return Constants.LOGIC_SUCCESS_ADD_TASK;
    		
    	}else{
    		return Constants.LOGIC_FAIL_ADD_TASK;
    	}
    	
    }//end add
    
    public static boolean addTaskToArrayTask(Task task){
    	taskList.add(task);
    	return true;
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
    
    
    public static String validateString(String input){
    	boolean isValid = validateParameter(input);
    	if(isValid){
    		return Constants.LOGIC_VALID_PARAMETER_MESSAGE;
    	}else{
    		return Constants.LOGIC_INVALID_PARAMETER_MESSAGE;
    	}
    	
    }//end validateString
    
    /*
     * Valid incoming parameter
     * */
    private static boolean validateParameter(String param){
    	String[] inputArray = param.split("-");
    	
    	if(inputArray.length > 0){
    		return true;
    	}
    	
    	return false;
    }
    
    
}
