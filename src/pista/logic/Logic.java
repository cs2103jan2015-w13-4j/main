package pista.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pista.Constants;
import pista.log.CustomLogging;
import pista.storage.Storage;

public class Logic {
	private static CustomLogging mLog = null;
	private static Storage mStorage = null;
	private static ArrayList<Storage> undoList = new ArrayList<Storage>();
	private static ArrayList<Storage> redoList = new ArrayList<Storage>();
	
	public static boolean initStorage(){
		try{
			mStorage = Storage.getInstance();
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
	public static boolean initLogging(){
		try{
			mLog = CustomLogging.getInstance(Storage.class.getName());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
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
		case Constants.VALUE_UNDO:
			output = undo() ;
			break;
		default:
			assert false:"invalid comand in runCommand: "+command;
		break;
		}
		return output;
	}

	public static String load(){
		if(mStorage.load()){
			return Constants.LOGIC_SUCCESS_LOAD_XML;
		}else{
			return Constants.LOGIC_FAIL_LOAD_XML;
		}
	}

	public static ArrayList<Task> getStorageList(){
		return mStorage.getTaskList();
	}

	public static String add(String[] tokens){
		Storage currentState = mStorage;
		boolean isAddedToStorage = false;
		boolean isAddedToArray = false;

		Task newTask = null;

		newTask = constructNewTask(tokens);
		newTask.setID(mStorage.getNextAvailableID());
		isAddedToArray = addTaskToTaskArrayList(newTask);
		isAddedToStorage = mStorage.save();

		if(isAddedToArray && isAddedToStorage){
			mLog.logInfo(String.format(Constants.LOG_LOGIC_SUCCESS_ADD_TASK, newTask.getTitle(), newTask.getCategory()));
			updateRedoAndUndo(currentState);
			return Constants.LOGIC_SUCCESS_ADD_TASK;
		}else{
			mLog.logInfo(Constants.LOG_LOGIC_FAIL_ADD_TASK);
			return Constants.LOGIC_FAIL_ADD_TASK;
		}
	}

	public static String edit(String[] tokens){
		Storage currentState = mStorage;
		int taskId=Integer.parseInt(tokens[0]);
		if(isTaskInList(taskId)){
			int taskIndex = findTaskIndex(taskId);
			Task extractedTask = mStorage.getTaskList().get(taskIndex);

			if(tokens.length==Constants.TOKEN_NUM_EDIT_TWO){
				extractedTask=editFloatingTask(extractedTask, tokens[1]);

			}else if(tokens.length==Constants.TOKEN_NUM_EDIT_FOUR){
				extractedTask=editDeadlineTask(extractedTask, tokens);

			}else if(tokens.length==Constants.TOKEN_NUM_EDIT_SIX){
				extractedTask=editTimedTask(extractedTask, tokens);

			}

			//for deadline and timed tasks, additionally convert date and time to millisecond
			if(tokens.length==4){//deadline task
				extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
			}
			else if(tokens.length==6){//timed task
				extractedTask.setStartMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
				extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[4], tokens[5]));
			}

			mStorage.getTaskList().remove(taskIndex);
			mStorage.getTaskList().add(extractedTask);
			mStorage.save();
			updateRedoAndUndo(currentState);
			
			mLog.logInfo(String.format(Constants.LOG_LOGIC_SUCCESS_EDIT_TASK, extractedTask.getTitle(), extractedTask.getCategory()));
			return Constants.LOGIC_SUCCESS_EDIT_TASK;
		}
		else{
			mLog.logInfo(Constants.LOG_LOGIC_FAIL_EDIT_TASK);
			return Constants.LOGIC_EDIT_TASK_NOT_FOUND;
		}
	}

	//delete tasks by id or all
	public static String delete(String[] tokens){
		Storage currentState = mStorage;
		String input = tokens[0];
		if(input.equalsIgnoreCase("a")){
			clearList();
			if(mStorage.save()){
				updateRedoAndUndo(currentState);
				mLog.logInfo(Constants.LOGIC_SUCCESS_DELETE_ALL_TASKS);
				return Constants.LOGIC_SUCCESS_DELETE_ALL_TASKS;
			}else{
				mLog.logInfo(Constants.LOGIC_FAIL_DELETE_ALL_TASKS);
				return Constants.LOGIC_FAIL_DELETE_ALL_TASKS;
			}
		}else {
			int id = Integer.parseInt(input);
			int index = findTaskIndex(id);

			try{
				if( index > -1){
					assert index < mStorage.getTaskList().size() : "Index out of bound";
					//Task temp = mStorage.getTaskList().get(index);
					mStorage.getTaskList().remove(index);
					//mLog.logInfo(String.format(Constants.LOG_LOGIC_SUCCESS_DELETE_TASK, temp.getTitle()));
				}else{
					//mLog.logInfo(Constants.LOGIC_DELETE_TASK_NOT_FOUND);
					return Constants.LOGIC_DELETE_TASK_NOT_FOUND;
				}
			}catch(AssertionError e){
				mLog.logSevere(e.getMessage());
				e.printStackTrace();
			}

			if (mStorage.save()){
				updateRedoAndUndo(currentState);
				mLog.logInfo(Constants.LOGIC_SUCCESS_DELETE_TASK);
				return Constants.LOGIC_SUCCESS_DELETE_TASK;
			}else{
				mLog.logInfo(Constants.LOGIC_FAIL_DELETE_TASK);
				return Constants.LOGIC_FAIL_DELETE_TASK;
			}
		}
	}

	//find the index of the task by a certain parameter, can expands it further
	public static Integer findTaskIndex(int id){
		for (int i = 0;i < mStorage.getTaskList().size();i++){
			if(mStorage.getTaskList().get(i).getID() == id){
				return i;
			}
		}
		return -1;
	}

	public static void clearList(){
		mStorage.getTaskList().clear();
	}

	public static boolean addTaskToTaskArrayList(Task task){
		if(mStorage.getTaskList().add(task)){
			return true;
		}else{
			assert false:"unable to add task to arraylist";
		}
		return false;
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
		for(Task t: mStorage.getTaskList()){
			if(t.getID()==taskId){
				return true;
			}
		}
		return false;
	}

	private static Task editTimedTask(Task extractedTask, String[] tokens) {
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TITLE])){
			extractedTask.setTitle(tokens[Constants.EDIT_TOKEN_TITLE]);//0
		}else{

		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE])){	//check startdate
			extractedTask.setStartDate(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE]);//1
		}else{

		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME])){//check starttime
			extractedTask.setStartTime(tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME]);//2
		}else{

		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE])){	//check enddate
			extractedTask.setEndDate(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE]);//3
		}else{

		}if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME])){//check endtime
			extractedTask.setEndTime(tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME]);//4
		}else{

		}

		return extractedTask;
	}

	private static Task editDeadlineTask(Task extractedTask, String[] tokens) {
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TITLE])){
			extractedTask.setTitle(tokens[Constants.EDIT_TOKEN_TITLE]);//0
		}else{

		}
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE])){	//check enddate
			extractedTask.setEndDate(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE]);//3
		}else{

		}
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME])){//check endtime
			extractedTask.setEndTime(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME]);//4
		}else{

		}
		return extractedTask;
	}

	private static Task editFloatingTask(Task extractedTask, String title) {
		if(!Constants.DEFAULT_VALUE.equalsIgnoreCase(title)){
			extractedTask.setTitle(title);
		}
		return extractedTask;
	}

	private static String undo(){
		if(!undoList.isEmpty()){
			saveToRedo();
			mStorage = undoList.get(undoList.size()-1);
			undoList.remove(undoList.size()-1);
			return Constants.LOGIC_SUCCESS_UNDO;
		}
		return Constants.LOGIC_FAIL_UNDO;
	}
	
	private static String redo(){
		if(!redoList.isEmpty()){
			saveToUndo(mStorage);
			mStorage = redoList.get(redoList.size()-1);
			undoList.remove(redoList.size()-1);
			return Constants.LOGIC_SUCCESS_REDO;
		}
		return Constants.LOGIC_SUCCESS_REDO;
	}
	
	public static long convertDateToMillisecond(String date, String time){
		//date format dd/mm/yyyy
		//time format HH:mm;	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm");
		String combined=date+" "+time;
		Date date1 = null;
		try {
			date1=sdf.parse(combined);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date1);

			return cal.getTimeInMillis();
			
		} catch (ParseException e) {
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return 0L;
		}

		
	}
	
	
	//============= API FOR SETTING PAGE ======================
	
	public static boolean checkFileBeforeSave(String newFilePath){
		if(!validateIsFileExist(newFilePath)){
			return false;
		}
		
		if(!validateIsFileEmpty(newFilePath) && !validateFileFormat(newFilePath)){
			return false;
		}
		
		return true;	
	}
	
	public static boolean checkFileDuringSave(String newFilePath){
		boolean isCreated = false;
		
		if(mStorage.isFileEmpty(newFilePath)){ //if empty, overwrite new xml format 
			isCreated = overwriteFile(newFilePath);
		}else{
			isCreated = loadExistingFile(newFilePath); //not empty and format is correct
		}
		
		return isCreated;
	}
	
	private static boolean validateIsFileExist(String newFilePath){
		if(!mStorage.isFileExist(newFilePath)){ //not exist
			return false; //maybe file is deleted after choosing
		}
		return true;
	}
	
	private static boolean validateIsFileEmpty(String newFilePath){
		if(mStorage.isFileEmpty(newFilePath)){
			//create xml nodes and format into the file
			return true;
		}
		return false;
	}
	
	private static boolean validateFileFormat(String newFilePath){
		if(mStorage.isFileFormatValid(newFilePath)){ //if valid
			return true;
		}
		return false;
	}
	
	private static boolean overwriteFile(String newFilePath){
		mStorage.initTaskList();
		mStorage.setDataFolderLocation(newFilePath);
		boolean isOverwrite = mStorage.overwriteNewXmlFile(newFilePath);
		return isOverwrite;
	}
	
	private static boolean loadExistingFile(String newFilePath){
		//read file
		mStorage.setDataFolderLocation(newFilePath);
		boolean isLoaded = mStorage.load(); //load the file into tasklist	
		return isLoaded;
	}
	
	private static void saveToUndo(Storage currState){
		if(undoList.size() < 3){
			undoList.add(currState);
		}else{
			undoList.remove(0);
			undoList.add(currState);
		}
	}
	
	private static void saveToRedo(){
		if(redoList.size() < 3){
			redoList.add(undoList.get(undoList.size()-1));
		}else{
			redoList.remove(0);
			redoList.add(undoList.get(undoList.size()-1));
		}
	}
	
	private static void clearRedo(){
		redoList.clear();
	}
	
	private static void updateRedoAndUndo(Storage s){
		clearRedo();
		saveToUndo(s);
	}
}
