package logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.storage.Storage;


public class Logic {
	static String[] temp=new String[8];
	private String[] parameterToken = null;
	
	public static ArrayList<Task> taskList = null;
	public static int totalNumberOfTasks =  Storage.getMaxNumberOfTasks();

	public static ArrayList<Task> initTaskFromXML(){
		taskList = Storage.XmltoTable(Constants.XML_FILE_PATH);
		return taskList;
	}
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
	 * add meeting -10/03/2015 -14:36 -11/03/2015 -15:10
	 * */
    public static String add(String param){
    	boolean isAddedToStorage = false;
    	boolean isAddedToArray = false;
    	
    	temp = param.split("-");
    	temp=trimLeadingTrailingSpacesInArray(temp);
    	Task newTask = null;
    	
   
    	newTask = LogicAddTask.constructNewTask(temp);
    	
    	isAddedToArray = addTaskToTaskArrayList(newTask);
    	isAddedToStorage = Storage.XmlAddTask(Constants.XML_FILE_PATH, newTask);
    	
    	if(isAddedToArray && isAddedToStorage){
    		return Constants.LOGIC_SUCCESS_ADD_TASK;
    		
    	}else{
    		return Constants.LOGIC_FAIL_ADD_TASK;
    	}
    	
    }//end add
    
    public static String mark(String param){ 
    	temp = param.split("-");
    	temp=trimLeadingTrailingSpacesInArray(temp);
    	Task newTask = null;
    	int taskId=Integer.parseInt(temp[0]);
    	String status=temp[1];
    	int taskIndex=getTaskIndexInList(taskId);
    	
    	newTask =taskList.get(taskIndex);
    	if(status.equalsIgnoreCase("done")){
    		newTask.setIsDone(true);
    	}
    	else if(status.equalsIgnoreCase("undone")){
    		newTask.setIsDone(false);
    	}
    	
		return Constants.LOGIC_SUCCESS_MARK_TASK;
    }//end mark
    
    public static boolean addTaskToTaskArrayList(Task task){
    	taskList.add(task);
    	return true;
    }
    
    public static String edit(String parameters) {
    	return "[EDIT]Parameters entered are: " + parameters;
    }
    
    public static boolean delete(String parameters) {
    	taskList.clear();
    	return true;
    }
    

    
    public static String list(String parameters) {
    	return ListTask.listTask(taskList).toString();
//    	return "[LIST]Parameters entered are: " + parameters;
    }
    
    
    public static String validateString(String command, String input){
 
    	boolean isValid = validateParameter(command, input);
    	if(isValid){
    		return Constants.LOGIC_VALID_PARAMETER_MESSAGE;
    	}else{
    		return Constants.LOGIC_INVALID_PARAMETER_MESSAGE;
    	}
    	
    }//end validateString
    
    /*
     * Valid incoming parameter
     * */
    private static boolean validateParameter(String command, String param){
    	String[] inputArray = param.split("-");
    	
    	if(inputArray.length > 0){
    		if(command.equalsIgnoreCase("add")){
    			return validateAddParameters(param);
    		}
    		else if(command.equalsIgnoreCase("edit")){
    			return validateEditParameters(param);
    		}
    		else if(command.equalsIgnoreCase("mark")){
    			return validateEditParameters(param);
    		}
    	}
    	
    	return false;
    }
    
    private static boolean validateMarkParameters(String param){
    	String[] inputArray = param.split("-");
    	inputArray=trimLeadingTrailingSpacesInArray(inputArray);
    	int taskId=Integer.parseInt(inputArray[0]);
    	boolean isTaskExists= isTaskInList(taskId);
    	if(isTaskExists){
    		if(inputArray[1].equalsIgnoreCase("done") || inputArray[1].equalsIgnoreCase("undone")){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private static boolean isTaskInList(int taskId){
    	for(Task t: taskList){
    		if(t.getID()==taskId){
    			return true;
    		}
    	}
    	return false;
    }
    
    private static int getTaskIndexInList(int taskId){
    	for (int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getID()==taskId)
				return i;
		}
    	return -1;
    }
    
    private static boolean validateEditParameters(String param){
    	
    	return false;
    }
    
    private static boolean validateAddParameters(String param){
    	String[] inputArray = param.split("-");
    	inputArray=trimLeadingTrailingSpacesInArray(inputArray);
   
    	
    	if(inputArray.length==1){
    		return TokenValidation.isTitleValid((inputArray[Constants.ARRAY_INDEX_TITLE]));
    	}
    	
    	else if(inputArray.length==3){
    		if(TokenValidation.isTitleValid(inputArray[Constants.ARRAY_INDEX_TITLE]) && (TokenValidation.isDateValid(inputArray[1])) && (TokenValidation.isTimeValid(inputArray[2]))){
    			return true;
    		}
    	}
    	else if(inputArray.length==5){
    		if(TokenValidation.isTitleValid(inputArray[Constants.ARRAY_INDEX_TITLE]) && (TokenValidation.isDateValid(inputArray[Constants.ARRAY_INDEX_END_DATE])) && (TokenValidation.isTimeValid(inputArray[Constants.ARRAY_INDEX_END_TIME])) && (TokenValidation.isDateValid(inputArray[Constants.ARRAY_INDEX_START_DATE])) && (TokenValidation.isTimeValid(inputArray[Constants.ARRAY_INDEX_START_TIME]))){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private static String[] trimLeadingTrailingSpacesInArray(String[] temp){
    	for (int i = 0; i < temp.length; i++) {
			temp[i]=temp[i].trim();
		}
    	return temp;
    }

}
