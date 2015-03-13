package logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import logic.storage.Storage;


public class Logic {
	public static String runCommand(String command, String[] tokens) {
		String output = "";
		switch(command) {
		case Constants.VALUE_ADD:
			output = add(tokens);
			break;
		case Constants.VALUE_EDIT:
			output = edit(tokens);
			break;
		case Constants.VALUE_DELETE:
			output = delete(tokens) ;
			break;
		default:
			break;
		}
		
		return output;
	}
	
	public static String load(){
		if(Storage.load()){
			return Constants.LOGIC_SUCCESS_LOAD_XML;
		}else{
			return Constants.LOGIC_FAIL_LOAD_XML;
		}
	}
	
	public static ArrayList<Task> getStorageList(){
		return Storage.getTaskList();
	}
	
	public static String add(String[] tokens){
    	boolean isAddedToStorage = false;
    	boolean isAddedToArray = false;
    	
    	Task newTask = null;
    	
    	newTask = constructNewTask(tokens);
    	
    	isAddedToArray = addTaskToTaskArrayList(newTask);
    	isAddedToStorage = Storage.save();
    	
    	if(isAddedToArray && isAddedToStorage){
    		return Constants.LOGIC_SUCCESS_ADD_TASK;
    	}else{
    		return Constants.LOGIC_FAIL_ADD_TASK;
    	}
    }
	
	public static String edit(String[] tokens){

		int taskId=Integer.parseInt(tokens[0]);
		if(isTaskInList(taskId)){
			int taskIndex = findTaskIndex(taskId);
			Task extractedTask = Storage.getTaskList().get(taskIndex);
			
			if(tokens.length==2){
				extractedTask=editFloatingTask(extractedTask, tokens[1]);
				
			}else if(tokens.length==4){
				extractedTask=editDeadlineTask(extractedTask, tokens);

			}else if(tokens.length==6){
				extractedTask=editTimedTask(extractedTask, tokens);

			}

			//for deadline and timed tasks, additionally convert date and time to millisecond
			if(tokens.length==4){//deadline task
				extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
			}
			else if(tokens.length==6){//timed task
				extractedTask.setStartMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
				extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[3], tokens[4]));
			}

			Storage.getTaskList().remove(taskIndex);
			Storage.getTaskList().add(extractedTask);
			Storage.save();

			return Constants.LOGIC_SUCCESS_EDIT_TASK;
		}
		else{
			return Constants.LOGIC_EDIT_TASK_NOT_FOUND;
		}
	}
	
    //delete tasks by id or all
    public static String delete(String[] tokens){  	
    	String input = tokens[0];
    	if(input.equalsIgnoreCase("a")){
    		clearList();
    		if(Storage.save()){
    			return Constants.LOGIC_SUCCESS_DELETE_ALL_TASKS;
    		}else{
    			return Constants.LOGIC_FAIL_DELETE_ALL_TASKS;
    		}
    	}else {
    		int id = Integer.parseInt(input);
    		int index = findTaskIndex(id);
    		
    		if( index > -1){
    			Storage.getTaskList().remove(index);
    		}else{
    			return Constants.LOGIC_DELETE_TASK_NOT_FOUND;
    		}	
    	}
    	
    	if (Storage.save()){
    		return Constants.LOGIC_SUCCESS_DELETE_TASK;
    	}else{
    		return Constants.LOGIC_FAIL_DELETE_TASK;
    	}
    }
    
    //find the index of the task by a certain parameter, can expands it further
    public static Integer findTaskIndex(int id){
		for (int i = 0;i < Storage.getTaskList().size();i++){
			if(Storage.getTaskList().get(i).getID() == id){
				return i;
			}
		}
		return -1;
    }
    
    public static void clearList(){
    	Storage.getTaskList().clear();
    }
    
    public static boolean addTaskToTaskArrayList(Task task){
		Storage.getTaskList().add(task);
		return true;
	}
    
    public static String[] formatArray(String[] inputArray, String category){
		
		if(category.equals("timed")){
			inputArray[Constants.ARRAY_INDEX_START_MILLISECONDS]=String.valueOf(convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_START_DATE], inputArray[Constants.ARRAY_INDEX_START_TIME]));
			inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_END_DATE], inputArray[Constants.ARRAY_INDEX_END_TIME]));	
		}
		else{
			inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_END_DATE], inputArray[Constants.ARRAY_INDEX_END_TIME]));
		}
		
		
		return inputArray;
	}
	
	public static Task constructNewTask(String[] inputArray){
		Task newTask = null;
		
		String[] newArray = new String[Constants.ARRAY_SIZE];
		if(inputArray.length==Constants.FLOATING_TASK 
				|| inputArray.length==Constants.TIMED_TASK){
			System.arraycopy(inputArray, 0, newArray, 0, inputArray.length); //copy inputArray into newArray
		}
		else if(inputArray.length==Constants.DEADLINE_TASK){
			System.arraycopy(inputArray, 0, newArray, 0, 1); //copy title of inputArray into newArray postition
			System.arraycopy(inputArray, 1, newArray, 3, inputArray.length-1); //copy end date and end time of inputArray into newArray postition
		}
		
		if(inputArray.length==Constants.DEADLINE_TASK){
			newArray = formatArray(newArray,"deadline"); //add in epoh time for parser
		}
		else if(inputArray.length==Constants.TIMED_TASK){
			newArray = formatArray(newArray,"timed"); //add in epoh time for parser
		}
		
		newTask = new Task(newArray); //id will auto generate inside Task class
		
		return newTask;	
	}//end constructNewTask
	
	private static boolean isTaskInList(int taskId){
		for(Task t: Storage.getTaskList()){
			if(t.getID()==taskId){
				return true;
			}
		}
		return false;
	}
	
	private static Task editTimedTask(Task extractedTask, String[] tokens) {
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[1])){
			extractedTask.setTitle(tokens[1]);//0
		}else{
			
		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[2])){	//check startdate
			extractedTask.setStartDate(tokens[2]);//1
		}else{
			
		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[3])){//check starttime
			extractedTask.setStartTime(tokens[3]);//2
		}else{
			
		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[4])){	//check enddate
			extractedTask.setEndDate(tokens[4]);//3
		}else{
			
		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[5])){//check endtime
			extractedTask.setEndTime(tokens[5]);//4
		}else{
			
		}
		
		return extractedTask;
	}

	private static Task editDeadlineTask(Task extractedTask, String[] tokens) {

		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[1])){
			extractedTask.setTitle(tokens[1]);//0
		}else{
			
		}
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[2])){	//check enddate
			extractedTask.setEndDate(tokens[2]);//3
		}else{
			
		}
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[3])){//check endtime
			extractedTask.setEndTime(tokens[3]);//4
		}else{
			
		}
		return extractedTask;
	}

	private static Task editFloatingTask(Task extractedTask, String title) {
		extractedTask.setTitle(title);
		return extractedTask;
	}
	
	public static long convertDateToMillisecond(String date, String time){
		//date format dd/mm/yyyy
		//time format HH:mm;	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm");
		String combined=date+" "+time;
		Date date1 = null;
		try {
			date1=sdf.parse(combined);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		
		return cal.getTimeInMillis();
	}
}
