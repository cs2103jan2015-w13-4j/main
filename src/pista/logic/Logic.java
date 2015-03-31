package pista.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import pista.Constants;
import pista.CustomPreferences;
import pista.log.CustomLogging;
import pista.parser.MainParser;
import pista.storage.Storage;

public class Logic {
	private static final Logic mLogic = new Logic();
	private static CustomLogging mLog = null;
	private static Storage mStorage = null;
	private static CustomPreferences mPrefs = null;
	private static ArrayList<ArrayList<Task>> undoList = new ArrayList<ArrayList<Task>>();
	private static ArrayList<ArrayList<Task>> redoList = new ArrayList<ArrayList<Task>>();

	private Logic(){}

	public static Logic getInstance(){
		initStorage();
		initPreference();
		initLogging();
		return mLogic;
	}

	public static boolean initPreference(){
		try{
			mPrefs = CustomPreferences.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}
	
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
		case Constants.VALUE_REDO:
			output = redo() ;
			break;
		case Constants.VALUE_MARK:
			output = mark(tokens);
			break;
		case Constants.VALUE_HELP:
			output = help();
			break;
		case Constants.VALUE_LIST:
			output = list(tokens);
			break;
		case Constants.VALUE_REMINDER:
			output = reminder(tokens);
			break;
		case Constants.VALUE_PRIORITY:
			output = priority(tokens);
			break;
		case Constants.VALUE_SET:
			output = set(tokens);
			break;
		default:
			assert false:"invalid comand in runCommand: "+command;
		break;
		}
		return output;
	}
	
	public static String priority(String[] tokens){
		int taskIndex = findTaskIndex(Integer.parseInt(tokens[0]));
		if(taskIndex != -1){
			Task extractedTask = mStorage.getTaskList().get(taskIndex);
			int prorityScore = Integer.parseInt(tokens[Constants.TOKEN_NUM_PRIORITY_SCORE]);
			if(prorityScore >=0 && prorityScore <=3) {
				extractedTask.setPriority(tokens[Constants.TOKEN_NUM_PRIORITY_SCORE]);
				reInsertTaskInToList(taskIndex, extractedTask);
				return Constants.LOGIC_SUCCESS_PRIORITY_TASK;
			}
		}
		return Constants.LOGIC_FAIL_PRIORITY_NOT_FOUND_TASK;
	}
	public static String reminder(String[] tokens){
		int taskIndex = findTaskIndex(Integer.parseInt(tokens[0]));
		long reminderMS = 0L;
		long endMS = 0L;
		String taskCategory = "";
		if(taskIndex != -1){
			Task extractedTask = mStorage.getTaskList().get(taskIndex);
			taskCategory = extractedTask.getCategory();
			if(tokens.length == Constants.TOKEN_NUM_REMINDER_TWO){
				if(taskCategory.equalsIgnoreCase(Constants.TASK_FLOATED)){
					return Constants.LOGIC_FAIL_REMIND_FLOATING_TASK;
				}else{
					extractedTask.setReminder(0L);
					reInsertTaskInToList(taskIndex, extractedTask);
					return Constants.LOGIC_SUCCESS_REMIND_OFF_TASK;
				}
			}else if (tokens.length == Constants.TOKEN_NUM_REMINDER_THREE){
				endMS = extractedTask.getEndMilliseconds();
				reminderMS = MainParser.convertDateToMillisecond(tokens[Constants.REMINDER_DATE], tokens[Constants.REMINDER_TIME]);
				if(taskCategory.equalsIgnoreCase(Constants.TASK_FLOATED)){
					return Constants.LOGIC_FAIL_REMIND_FLOATING_TASK;
				}else{
					if(reminderMS <= endMS){
						extractedTask.setReminder(reminderMS);
						reInsertTaskInToList(taskIndex, extractedTask);
						return Constants.LOGIC_SUCCESS_REMIND_TASK;
					}else{
						return Constants.LOGIC_FAIL_REMIND_LATER_THAN_ENDDATE_TASK;
					}
				}
			}
		}
		return Constants.LOGIC_FAIL_REMIND_NOT_FOUND_TASK;
	}

	private static void reInsertTaskInToList(int taskIndex, Task extractedTask) {
		mStorage.getTaskList().remove(taskIndex);
		mStorage.getTaskList().add(extractedTask);
		mStorage.save();
	}

	public static String load(){
		if(mStorage.load()){
			return Constants.LOGIC_SUCCESS_LOAD_XML;
		}else{
			return Constants.LOGIC_FAIL_LOAD_XML;
		}
	}

	public static ArrayList<Task> getStorageList(){
		sortOverView();
		return mStorage.getTaskList();
	}

	public static void sortOverView(){
		ComparatorTask comparatorTask = new ComparatorTask();
		Collections.sort(mStorage.getTaskList(), comparatorTask);
	}
	
	public static void sortTitleAscending(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.titleComparator);
	}
	
	public static void sortTypeOfTask(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.taskCategoryComparator);
	}
	
	public static void sortTitleDescending(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.titleComparator));
	}
	
	public static void sortIsDoneUndone(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.isDoneComparator);
	}
	
	public static void sortIsDoneCompleted(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.isDoneComparator));
	}
	
	public static void sortAscendingStartDate(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.startDateComparator);
	}
	
	public static void sortDescendingStartDate(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.startDateComparator));
	}
	
	public static void sortAscendingEndDate(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.endDateComparator);
	}
	
	public static void sortDescendingEndDate(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.endDateComparator));
	}

	public static String help(){
		return Constants.LOGIC_SUCCESS_HELP;
	}
	
	private static String set(String[] tokens){
		boolean isValidFile = false;
		boolean isFileLoaded = false;
		boolean isPrefSave = false;
		
		String getFileName = "";
		
		getFileName = tokens[Constants.SET_VALUE_INDEX];
		isValidFile = checkFileBeforeSave(getFileName);
		if(!isValidFile){
			return Constants.LOGIC_INVALID_SET_INVALID_FILE_FORMAT;
		}
		
		isFileLoaded = checkFileDuringSave(getFileName); //will save location, load XML file in storage
		if(!isFileLoaded){
			return Constants.LOGIC_FAIL_SET_LOAD_FILE;
		}
		
		isPrefSave = mPrefs.setPreferenceFileLocation(getFileName); //save preferences
		if(!isPrefSave){ //unable to save
			return Constants.LOGIC_FAIL_SET_SAVE;
		}
		
		return Constants.LOGIC_SUCCESS_SET_SAVE;
		
	}
	
	
	public static String list(String[] tokens){ 
		String sortType=tokens[0];
		String message=String.format(Constants.LOGIC_SUCESS_SORTED, sortType);
		if(Constants.LIST_ASCENDING_END_DATE.equalsIgnoreCase(sortType)){
			sortAscendingEndDate();
		}else if(Constants.LIST_DESCENDING_END_DATE.equalsIgnoreCase(sortType)){
			sortDescendingEndDate();
		}else if(Constants.LIST_ASCENDING_START_DATE.equalsIgnoreCase(sortType)){
			sortAscendingStartDate();
		}else if(Constants.LIST_DESCENDING_START_DATE.equalsIgnoreCase(sortType) ){
			sortDescendingStartDate();
		}else if(Constants.LIST_ASCENDING_TITLE.equalsIgnoreCase(sortType)){
			sortTitleAscending();
		}else if(Constants.LIST_DESCENDING_TITLE.equals(sortType)){
			sortTitleDescending();
		}else if(Constants.LIST_ISDONE_COMPLETED.equalsIgnoreCase(sortType) ){
			sortIsDoneCompleted();
		}else if(Constants.LIST_ISDONE_UNDONE.equalsIgnoreCase(sortType) ){
			sortIsDoneUndone();
		}else if(Constants.LIST_OVERVIEW.equalsIgnoreCase(sortType)){
			sortOverView();
		}else if(Constants.LIST_TYPE.equalsIgnoreCase(sortType)){
			
		}
		mStorage.save();
		return message;
	}
	
	public static String mark(String[] tokens){ 
		int taskIndex = findTaskIndex(Integer.parseInt(tokens[0]));
		if(taskIndex != -1){
			Task extractedTask = mStorage.getTaskList().get(taskIndex);
			String status=tokens[1];

			if(Constants.STATUS_DONE.equalsIgnoreCase(status)){
				extractedTask.setIsDone(true);
			}
			else if(Constants.STATUS_UNDONE.equalsIgnoreCase(status)){
				extractedTask.setIsDone(false);
			}
			reInsertTaskInToList(taskIndex, extractedTask);
			return Constants.LOGIC_SUCCESS_MARK_TASK;
		}
		return Constants.LOGIC_FAIL_MARK_NOT_FOUND_TASK;
	}//end mark

	public static String add(String[] tokens){
		ArrayList<Task> currentState = getCurrentState();
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
		ArrayList<Task> currentState = getCurrentState();
		int taskId=Integer.parseInt(tokens[0]);
		int clearValue = 0;
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
			clearValue = getClearValue(tokens, clearValue);

			//for deadline and timed tasks, additionally convert date and time to millisecond
			if(tokens.length==4){//deadline task
				if(clearValue == 2){
					extractedTask.setCategory(Constants.TASK_FLOATED);
					extractedTask.setEndMilliseconds(Long.parseLong("0"));
				}else{
					extractedTask.setCategory(Constants.TASK_DEADLINE);
					extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
				}
			}
			else if(tokens.length==6){//timed task
				if(clearValue == 4){
					extractedTask.setCategory(Constants.TASK_FLOATED);
					extractedTask.setStartMilliseconds(Long.parseLong("0"));
					extractedTask.setEndMilliseconds(Long.parseLong("0"));
				}else if(clearValue == 2){
					extractedTask.setCategory(Constants.TASK_DEADLINE);
					extractedTask.setStartMilliseconds(Long.parseLong("0"));
					extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
				}else if(clearValue == 0){
				extractedTask.setCategory(Constants.TASK_TIMED);
				extractedTask.setStartMilliseconds(convertDateToMillisecond(tokens[2], tokens[3]));
				extractedTask.setEndMilliseconds(convertDateToMillisecond(tokens[4], tokens[5]));
				}
			}

			reInsertTaskInToList(taskIndex, extractedTask);
			updateRedoAndUndo(currentState);

			mLog.logInfo(String.format(Constants.LOG_LOGIC_SUCCESS_EDIT_TASK, extractedTask.getTitle(), extractedTask.getCategory()));
			return Constants.LOGIC_SUCCESS_EDIT_TASK;
		}
		else{
			mLog.logInfo(Constants.LOG_LOGIC_FAIL_EDIT_TASK);
			return Constants.LOGIC_EDIT_TASK_NOT_FOUND;
		}
	}

	private static int getClearValue(String[] tokens, int clearValue) {
		for(String temp : tokens){
			if(Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(temp)){
				clearValue++;
			}
		}
		return clearValue;
	}

	//delete tasks by id or all
	public static String delete(String[] tokens){
		ArrayList<Task> currentState = getCurrentState();
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
					return Constants.LOGIC_SUCCESS_DELETE_TASK;
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
		if(!Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TITLE])){
			extractedTask.setTitle(tokens[Constants.EDIT_TOKEN_TITLE]);//0
		}else{

		}if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE])){	//check startdate
			extractedTask.setStartDate(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE]);//1
		}else {
			extractedTask.setStartDate("");
		}if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME])){//check starttime
			extractedTask.setStartTime(tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME]);//2
		}else{
			extractedTask.setStartTime("");
		}if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE])){	//check enddate
			extractedTask.setEndDate(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE]);//3
		}else{
			extractedTask.setEndDate("");
		}if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME])){//check endtime
			extractedTask.setEndTime(tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME]);//4
		}else{
			extractedTask.setEndTime("");
		}

		return extractedTask;
	}

	private static Task editDeadlineTask(Task extractedTask, String[] tokens) {
		if(!Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TITLE])){
			extractedTask.setTitle(tokens[Constants.EDIT_TOKEN_TITLE]);//0
		}else{

		}
		if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE])){	//check enddate
			extractedTask.setEndDate(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE]);//3
		}else{
			extractedTask.setEndDate("");
		}
		if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME])){//check endtime
			extractedTask.setEndTime(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME]);//4
		}else{
			extractedTask.setEndTime("");
		}
		return extractedTask;
	}

	private static Task editFloatingTask(Task extractedTask, String title) {
		if(!Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(title)){
			extractedTask.setTitle(title);
		}
		return extractedTask;
	}

	private static String undo(){
		if(!undoList.isEmpty()){
			ArrayList<Task> currentState = getCurrentState();
			saveToRedo(currentState);
			mStorage.setTaskList(undoList.get(undoList.size()-1));
			undoList.remove(undoList.size()-1);
			mStorage.save();
			return Constants.LOGIC_SUCCESS_UNDO;
		}
		return Constants.LOGIC_FAIL_UNDO;
	}

	private static String redo(){
		if(!redoList.isEmpty()){
			ArrayList<Task> currentState = getCurrentState();
			saveToUndo(currentState);
			mStorage.setTaskList(redoList.get(redoList.size()-1));
			redoList.remove(redoList.size()-1);
			mStorage.save();
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


	//================================= UNDO & REDO =================================
	private static void saveToUndo(ArrayList<Task> currState){
		if(undoList.size() < 3){
			undoList.add(currState);
		}else{
			undoList.remove(0);
			undoList.add(currState);
		}
	}

	private static void saveToRedo(ArrayList<Task> currState){
		if(redoList.size() < 3){
			redoList.add(currState);
		}else{
			redoList.remove(0);
			redoList.add(currState);
		}
	}

	private static void clearRedo(){
		redoList.clear();
	}

	private static void updateRedoAndUndo(ArrayList<Task> s){
		clearRedo();
		saveToUndo(s);
	}

	private static ArrayList<Task> getCurrentState() {
		ArrayList<Task> currentState = new ArrayList<Task>(mStorage.getTaskList());
		return currentState;
	}


	//============== LISTVIEW CUSTOM CELL ===========
	public static boolean updateTaskIsDone(int id, boolean newDone){
		try{
			boolean isUpdated = false;
			for(Task t : mStorage.getTaskList()){
				if(t.getID() == id){
					t.setIsDone(newDone);
				}
			}

			isUpdated = mStorage.save();
			return isUpdated;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}
	
	
}
