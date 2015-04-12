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
	private static String[] currentSortType = new String[1];

	/**This constructor prevent creating a new instance of Logic 
	 * **/
	private Logic(){}
	
	/**This method is to get the current instance of Logic
	 * return the instance of Logic
	 * **/
	public static Logic getInstance(){
		if(mStorage == null){
			initStorage();
		}
		if(mPrefs == null){
			initPreference();
		}
		if(mLog == null){
			initLogging();
		}
		return mLogic;
	}

	/**This method is to initiate the Preference class
	 * return true if done successfully
	 * return false otherwise
	 * **/
	public static boolean initPreference(){
		try{
			mPrefs = CustomPreferences.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}

	/**This method is to initiate the Storage class
	 * return true if done successfully
	 * return false otherwise
	 * **/
	public static boolean initStorage(){
		currentSortType[0] = Constants.SORT_OVERVIEW;
		try{
			mStorage = Storage.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
	/**This method is to initiate the Logging class
	 * return true if done successfully
	 * return false otherwise
	 * **/
	public static boolean initLogging(){
		try{
			mLog = CustomLogging.getInstance(Storage.class.getName());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	/**This method is the main controller of Logic class, from here all function can be called. UIController will access 
	 * Logic mainly through this method. Return a String regarding the status of operation
	 * Parameters:	command - the command that the user is calling
	 * 				tokens - information provided by the user
	 * Return: 		output - String that reflects the status of the operation
	 * **/
	public String runCommand(String command, String[] tokens) {
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
		case Constants.VALUE_SORT:
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
		Constants.HISTORY_INDEX += 1;
		return output;
	}

	/**This method is to load the Storage 
	 * return the String to indicate if the load is successful or fail
	 * **/
	public String load(){
		if(mStorage.load()){
			return Constants.LOGIC_SUCCESS_LOAD_XML;
		}else{
			return Constants.LOGIC_FAIL_LOAD_XML;
		}
	}
	
	/**This method is to get the Storage list containing all the task
	 * return an ArrayList<Task> 
	 * **/
	public ArrayList<Task> getStorageList(){
		return mStorage.getTaskList();
	}
	
	public void reorderStorageList(){
		list(currentSortType);
		reorderID();
	}
	
	/**This method is to check the file before saving to the file
	 * return false if the file is non-exist, empty or invalid
	 * return true otherwise.
	 * Parameters:	newFilePath - the path of the file 
	 * Return:		boolean
	 * **/
	public boolean checkFileBeforeSave(String newFilePath){
		if(!validateIsFileExist(newFilePath)){
			return false;
		}

		if(!validateIsFileEmpty(newFilePath) && !validateFileFormat(newFilePath)){
			return false;
		}

		return true;	
	}
	
	/**This method is to check the file during the saving operation
	 * if the file is empty, overwrite the file
	 * if the file is not empty, load the file
	 * Parameters:	newFilePath - the path of the file
	 * Return:		boolean - status if the file is empty or not
	 * **/
	public boolean checkFileDuringSave(String newFilePath){
		boolean isCreated = false;

		if(mStorage.isFileEmpty(newFilePath)){ //if empty, overwrite new xml format 
			isCreated = overwriteFile(newFilePath);
		}else{
			isCreated = loadExistingFile(newFilePath); //not empty and format is correct
		}

		return isCreated;
	}
	
	/**This method is to copy file from source file to destination file
	 * Parameters:	src - source file path
	 * Return:		dest - destination file path
	 * **/
	public boolean copyFile(String src, String dest){
		boolean isCopied = false;
		isCopied = mStorage.copyFile(src, dest);
		return isCopied;
	}
	
	/**This method is to process the information of a Task and construct a string
	 * based on that. Return the string when it is done. This is for autocomplete during
	 * editting.
	 * Parameters:	index - the index of the task
	 * Return:		finalStr - the String that is constructed using the information of the given Task
	 * **/
	public String processTaskInfo(int index){
		Task t = getTaskByIndex(index);
		String finalStr = "";
		if ( t!= null ){
			String title = t.getTitle();
			String startDate = t.getStartDate();
			String startTime = t.getStartTime();
			String endDate = t.getEndDate();
			String endTime = t.getEndTime();
			String category = t.getCategory();

			if(category.equalsIgnoreCase("timed")){
				finalStr += " -"+title+" -"+startDate+" -"+startTime+" -"+endDate+" -"+endTime;
			}
			else if(category.equalsIgnoreCase("deadline")){
				finalStr += " -"+title+" -"+endDate+" -"+endTime;
			}else{
				finalStr += " -"+title;
			}
		}
		return finalStr ;
	}

	/**This method is to add the command executed by the user to the history
	 * Parameters:	s - the command executed by the user
	 * **/
	public void storeToHistory(String s){
		mStorage.getHistoryList().add(s);
	}

	/**This method is to add a new Task to the program using the given information
	 * Parameters:	tokens - an Array that contains information of the new Task
	 * Return:		String - the status of the operation
	 * **/
	private String add(String[] tokens){
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

	/**This method is to edit an existing Task with new information provided by the user.
	 * Editing will be done differently depends on the type of Task the user is updating.
	 * Parameters:	tokens - an Array that contains new information to be updated
	 * Return:		String - the status of the operation
	 * **/
	private String edit(String[] tokens){
		ArrayList<Task> currentState = getCurrentState();
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

	/**This method is to delete a specific Task or all of Tasks in the system
	 * Parameters:	tokens - an Array that contains the ID of the Task need to be deleted or "a" means all
	 * Return:		String - the status of the operation
	 * **/
	private String delete(String[] tokens){
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
	
	/**This method is to undo the last action done by the user
	 * Return:		String - the status of the operation
	 * **/
	private String undo(){
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

	/**This method is to redo the last undone action
	 * Return:		String - the status of the operation
	 * **/
	private String redo(){
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
	
	/**This method is to mark a certain Task as done or undone
	 * Parameters:	tokens - an Array that contains the ID of the Task and the status you want to mark it with
	 * Return:		String - the status of the operation
	 * **/
	private String mark(String[] tokens){ 
		ArrayList<Task> currentState = getCurrentState();
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
			updateRedoAndUndo(currentState);
			return Constants.LOGIC_SUCCESS_MARK_TASK;
		}
		return Constants.LOGIC_FAIL_MARK_NOT_FOUND_TASK;
	}//end mark
	
	/**This method is to sort the Storage using the default sort
	 * **/
	private void sortOverView(){
		ComparatorTask comparatorTask = new ComparatorTask();
		Collections.sort(mStorage.getTaskList(), comparatorTask);
	}
	
	/**This method is to sort the Storage by ascending title of Task
	 * **/
	private void sortTitleAscending(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.titleComparator);
	}

	/**This method is to sort the Storage by the type of Task
	 * **/
	private void sortTypeOfTask(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.taskCategoryComparator);
	}

	/**This method is to sort the Storage by descending title of ask
	 * **/
	private void sortTitleDescending(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.titleComparator));
	}

	/**This method is to sort the Storage by the Done or Undone status. Undone before Done
	 * **/
	private void sortIsDoneUndone(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.isDoneComparator);
	}

	/**This method is to sort the Storage by the Done or Undone status. Done before Undone
	 * **/
	private void sortIsDoneCompleted(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.isDoneComparator));
	}

	/**This method is to sort the Storage by ascending start date of Task
	 * **/
	private void sortAscendingStartDate(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.startDateComparator);
	}

	/**This method is to sort the Storage by descending start date of Task
	 * **/
	private void sortDescendingStartDate(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.descendingStartDateComparator));
	}
	
	/**This method is to sort the Storage by ascending end date of Task
	 * **/
	private void sortAscendingEndDate(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.endDateComparator);
	}

	/**This method is to sort the Storage by descending end date of Task
	 * **/
	private void sortDescendingEndDate(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.endDateComparator));
	}

	/**This method is to sort the Storage by ascending priority of Task
	 * **/
	private void sortAscendingPriority(){
		Collections.sort(mStorage.getTaskList(), Collections.reverseOrder(MiscComparator.priorityComparator));
	}

	/**This method is to sort the Storage by descending priority of Task
	 * **/
	private void sortDescendingPriority(){
		Collections.sort(mStorage.getTaskList(), MiscComparator.priorityComparator);
	}
	
	/**This method is to open help guide
	 * **/
	private String help(){
		return Constants.LOGIC_SUCCESS_HELP;
	}

	/**This method is to sort the listview in a certain sorting option
	 * Parameters:	tokens - an Array contains type of sort the user want to sort the view by
	 * Return:		message - the message to show the type of sort has been implemented
	 * **/
	private String list(String[] tokens){ 
		String sortType=tokens[0];
		String message=String.format(Constants.LOGIC_SUCESS_SORTED, sortType);
		/*if(Constants.LIST_ASCENDING_END_DATE.equalsIgnoreCase(sortType)){
			sortAscendingEndDate();
		}phased out since overview is sorted in isDone, priority, ascending end date, title*/
		if(Constants.SORT_DESCENDING_END_DATE.equalsIgnoreCase(sortType)){
			sortDescendingEndDate();
		}else if(Constants.SORT_ASCENDING_START_DATE.equalsIgnoreCase(sortType)){
			sortAscendingStartDate();
		}else if(Constants.SORT_DESCENDING_START_DATE.equalsIgnoreCase(sortType) ){
			sortDescendingStartDate();
		}else if(Constants.SORT_ASCENDING_TITLE.equalsIgnoreCase(sortType)){
			sortTitleAscending();
		}else if(Constants.SORT_DESCENDING_TITLE.equals(sortType)){
			sortTitleDescending();
		}else if(Constants.SORT_ISDONE_DONE.equalsIgnoreCase(sortType) ){
			sortIsDoneCompleted();
		}else if(Constants.SORT_ISDONE_UNDONE.equalsIgnoreCase(sortType) ){
			sortIsDoneUndone();
		}else if(Constants.SORT_OVERVIEW.equalsIgnoreCase(sortType)){
			sortOverView();
		}else if(Constants.SORT_TYPE.equalsIgnoreCase(sortType)){
			sortTypeOfTask();
		}else if (Constants.SORT_ASCENDING_PRIORITY.equalsIgnoreCase(sortType)){
			sortAscendingPriority();
		}else if (Constants.SORT_DESCENDING_PRIORITY.equalsIgnoreCase(sortType)){
			sortDescendingPriority();
		}
		currentSortType[0] = sortType;
		mStorage.save();
		return message;
	}
	
	/**This method is to set priority to a Task .
	 * Parameters:	tokens - an Array that contains the ID of the Task and the level of priority the user want to set to that Task
	 * Returns:		String - the status of the operation
	 * **/
	private String priority(String[] tokens){
		ArrayList<Task> currentState = getCurrentState();
		int taskIndex = findTaskIndex(Integer.parseInt(tokens[0]));
		if(taskIndex != -1){
			Task extractedTask = mStorage.getTaskList().get(taskIndex);
			int prorityScore = Integer.parseInt(tokens[Constants.TOKEN_NUM_PRIORITY_SCORE]);
			if(prorityScore >=0 && prorityScore <=3) {
				extractedTask.setPriority(tokens[Constants.TOKEN_NUM_PRIORITY_SCORE]);
				reInsertTaskInToList(taskIndex, extractedTask);
				updateRedoAndUndo(currentState);
				return Constants.LOGIC_SUCCESS_PRIORITY_TASK;
			}
		}
		return Constants.LOGIC_FAIL_PRIORITY_NOT_FOUND_TASK;
	}
	
	/**This method is set the reminder to a certain Task
	 * Parameters:	tokens - an Array that contains the ID of the task the user want to set the reminder to
	 * Return:		String - a status of the operation
	 * **/
	private String reminder(String[] tokens){
		ArrayList<Task> currentState = getCurrentState();
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
					updateRedoAndUndo(currentState);
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
						updateRedoAndUndo(currentState);
						return Constants.LOGIC_SUCCESS_REMIND_TASK;
					}else{
						return Constants.LOGIC_FAIL_REMIND_LATER_THAN_ENDDATE_TASK;
					}
				}
			}
		}
		return Constants.LOGIC_FAIL_REMIND_NOT_FOUND_TASK;
	}
	
	/**This method is to set the file location that the data will be saved
	 * Parameters:	tokens - an Array that contains the file location
	 * Returns:		String - status of the operation
	 * **/
	private String set(String[] tokens){
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

	/**This method is to find the Task index in the Storage, given its id
	 * Parameters:	id - the ID of the Task 
	 * Return:		Integer - the Task index in the Storage or -1 if not found
	 * **/
	private Integer findTaskIndex(int id){
		for (int i = 0;i < mStorage.getTaskList().size();i++){
			if(mStorage.getTaskList().get(i).getID() == id){
				return i;
			}
		}
		return -1;
	}
	
	/**This method is to clear everything in the Storage
	 * **/
	private void clearList(){
		mStorage.getTaskList().clear();
	}

	/**This method is to add the Task to the Storage
	 * Parameters:	task - the Task to add into the Storage
	 * **/
	private boolean addTaskToTaskArrayList(Task task){
		if(mStorage.getTaskList().add(task)){
			return true;
		}else{
			assert false:"unable to add task to arraylist";
		}
		return false;
	}
	
	/**This method is to format the Array that contains the information of the Task based on the 
	 * type of Task it is.
	 * Parameters:	inputArray - an Array that contains information of the Task
	 * 				category - the type of Task 
	 * Return:		inputArray - the formated Array
	 * **/
	private String[] formatArray(String[] inputArray, String category){
		if(category.equals("timed")){
			inputArray[Constants.ARRAY_INDEX_START_MILLISECONDS]=String.valueOf(convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_START_DATE], inputArray[Constants.ARRAY_INDEX_START_TIME]));
			inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_END_DATE], inputArray[Constants.ARRAY_INDEX_END_TIME]));
		}
		else{
			inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_END_DATE], inputArray[Constants.ARRAY_INDEX_END_TIME]));
		}


		return inputArray;
	}

	/**This method is to construct a new Task based on given information
	 * Parameters:	inputArray - the information of the new Task
	 * Return:		newTask - the new Task that has been constructed using the information
	 * **/
	private Task constructNewTask(String[] inputArray){
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

	/**This method is to edit a Task if the Task is of the type timed
	 * Parameters:	extractedTask - the Task that need to be edited
	 * 				tokens - the new information to be updated
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task editTimedTask(Task extractedTask, String[] tokens) {
		Long remindMS = extractedTask.getReminder();
		Long endMS = extractedTask.getEndMilliseconds();
		Long differenceReminer = endMS - remindMS;
		Long updatedRemindMS = 0L;
		Long updatedEndMS = 0L;
		if(!Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TITLE])){
			extractedTask.setTitle(tokens[Constants.EDIT_TOKEN_TITLE]);//0
		}
		extractedTask = setFieldsInTimed(extractedTask, tokens);
		updatedEndMS = extractedTask.getEndMilliseconds();
		extractedTask =changeReminderBasedOnDifference(extractedTask, remindMS, endMS,
				differenceReminer, updatedEndMS);

		return extractedTask;
	}

	/**This method is to set the fields of the timed Task. Use to update new information to the Task
	 * Parameters:	extractTask - the Task to be updated
	 * 				tokens - an Array that contains the new information
	 * Return:		extractedTask - the Task with updated information
	 * **/
	private Task setFieldsInTimed(Task extractedTask, String[] tokens) {
		extractedTask.setStartDate(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE]);//1
		extractedTask.setStartTime(tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME]);//2
		extractedTask.setEndDate(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE]);//3	
		extractedTask.setEndTime(tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME]);//4
		extractedTask.setStartMilliseconds(Long.parseLong("0"));
		extractedTask.setEndMilliseconds(Long.parseLong("0"));
		extractedTask.setStartMilliseconds(MainParser.convertDateToMillisecond(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE]
				, tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME]));
		extractedTask.setEndMilliseconds(MainParser.convertDateToMillisecond(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE]
				, tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME]));
		extractedTask.setCategory(Constants.TASK_TIMED);
		return extractedTask;
	}
	
	/**This method is to edit a Task if the Task is of the type deadline
	 * Parameters:	extractedTask - the Task that need to be edited
	 * 				tokens - the new information to be updated	
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task editDeadlineTask(Task extractedTask, String[] tokens) {
		String taskCategory = extractedTask.getCategory();
		Long remindMS = extractedTask.getReminder();
		Long endMS = extractedTask.getEndMilliseconds();
		Long differenceReminer = endMS - remindMS;
		Long updatedRemindMS = 0L;
		Long updatedEndMS = 0L;
		if(!Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(tokens[Constants.EDIT_TOKEN_TITLE])){
			extractedTask.setTitle(tokens[Constants.EDIT_TOKEN_TITLE]);//0
		}
		extractedTask = setFieldsInDeadline(extractedTask, tokens);
		extractedTask = resetFieldsFromTimedToDeadline(extractedTask, taskCategory);
		updatedEndMS = extractedTask.getEndMilliseconds();
		extractedTask =changeReminderBasedOnDifference(extractedTask, remindMS, endMS,
				differenceReminer, updatedEndMS);

		extractedTask.setCategory(Constants.TASK_DEADLINE);
		return extractedTask;
	}
	
	/**	This method edits the reminder of a Task
	 * 	if the newly edited enddate and end time is earlier than the previous, a new reminder will be set.
	 * 	new reminder is obtained by subracting the difference from the initial endMS.
	 * difference is obtained from initial endMS - initial remindMS 
	 * Parameters:	Task - the Task that need to be edited
	 * 				Long - remindMS, endMS, differenceReminder, updatedEndMS
	 * 				tokens - the new information to be updated	
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task changeReminderBasedOnDifference(Task extractedTask,
			Long remindMS, Long endMS, Long differenceReminer, Long updatedEndMS) {
		Long updatedRemindMS = 0L;
		if(updatedEndMS != endMS){
			updatedRemindMS = updatedEndMS - differenceReminer;
			if(remindMS >= updatedEndMS) {
				extractedTask.setReminder(updatedRemindMS);
			}
		}
		return extractedTask;
	}

	/**	This method edits the information of a deadline Task
	 * Parameters:	Task - the Task that need to be edited
	 * 				String array - parameters
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task setFieldsInDeadline(Task extractedTask, String[] tokens) {
		extractedTask.setEndDate(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE]);
		extractedTask.setEndTime(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME]);
		extractedTask.setEndMilliseconds(MainParser.convertDateToMillisecond(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE]
				, tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME]));
		return extractedTask;
	}

	/**	This method does the conversion of a Timed task to Deadline task
	 * by resetting the necessary fields 
	 * Parameters:	Task - the Task that need to be edited
	 * 				String - task's category
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task resetFieldsFromTimedToDeadline(Task extractedTask,
			String taskCategory) {
		if(taskCategory.equalsIgnoreCase(Constants.TASK_TIMED)){
			extractedTask.setStartDate("");
			extractedTask.setStartTime("");
			extractedTask.setStartMilliseconds(Long.parseLong("0"));
		}
		return extractedTask;
	}

	/**	This method edits the information of a deadline Task
	 * Parameters:	Task - the Task that need to be edited
	 * 				String array - parameters
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task editFloatingTask(Task extractedTask, String title) {
		String taskCategory = extractedTask.getCategory();
		if(!Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(title) ){
			extractedTask.setTitle(title);
		}
		extractedTask = resetFieldsFloating(extractedTask, taskCategory);		
		extractedTask.setCategory(Constants.TASK_FLOATED);
		return extractedTask;
	}

	/**	This method does the conversion of a Timed/Deadline task to floating task
	 * by resetting the necessary fields 
	 * Parameters:	Task - the Task that need to be edited
	 * 				String - task's category
	 * Return:		extractedTask - the Task with information updated
	 * **/
	private Task resetFieldsFloating(Task extractedTask,
			String taskCategory) {
		if(taskCategory.equalsIgnoreCase(Constants.TASK_DEADLINE) 
				|| taskCategory.equalsIgnoreCase(Constants.TASK_TIMED)){
			extractedTask.setStartDate("");
			extractedTask.setStartTime("");
			extractedTask.setEndDate("");
			extractedTask.setEndTime("");
			extractedTask.setStartMilliseconds(Long.parseLong("0"));
			extractedTask.setEndMilliseconds(Long.parseLong("0"));
			extractedTask.setReminder(Long.parseLong("0"));

		}
		return extractedTask;
	}
	
	/**This method is to reinsert a Task to the Storage
	 * Parameters:	taskIndex - the index in the Storage that the Task will be added to
	 * 				extractedTask - the Task that will be insert inside the Storage
	 * **/
	private void reInsertTaskInToList(int taskIndex, Task extractedTask) {
		mStorage.getTaskList().remove(taskIndex);
		mStorage.getTaskList().add(extractedTask);
		mStorage.save();
	}
	
	/**This method is to check if a Task is in the Storage based on its ID
	 * Parameters:	taskId - the ID of the Task
	 * Return:		boolean - true if found, false otherwise
	 * **/
	private boolean isTaskInList(int taskId){
		for(Task t: mStorage.getTaskList()){
			if(t.getID()==taskId){
				return true;
			}
		}
		return false;
	}
	
	/**This method is to convert a Date to its corresponding Millisecond value
	 * Parameters:	date - the date value that need to be converted
	 * 				time - the time value that need to be converted
	 * **/
	private long convertDateToMillisecond(String date, String time){
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
	
	/**This method is to return a Task in the Storage based on its id
	 * Parameters:	id - the ID of the Task
	 * Return:		Task - the Task that matched the ID , return null if not found
	 * **/
	private Task getTaskByIndex(int id){
		int index = findTaskIndex(id);
		if(isTaskInList(id)){
			return mStorage.getTaskList().get(index);
		}else{
			return null;
		}
	}

	//============= API FOR SETTING PAGE ======================
	/** This method is to validate if the File in the given path is existed
	 * Parameters:	newFilePath - the file path of the file
	 * Return:		boolean - true if the file exists, false otherwise
	 * **/
	private boolean validateIsFileExist(String newFilePath){
		if(!mStorage.isFileExist(newFilePath)){ //not exist
			return false; //maybe file is deleted after choosing
		}
		return true;
	}

	/** This method is to validate if the File in the given path is empty
	 * Parameters:	newFilePath - the file path of the file
	 * Return:		boolean - true if the file is emptys, false otherwise
	 * **/
	private boolean validateIsFileEmpty(String newFilePath){
		if(mStorage.isFileEmpty(newFilePath)){
			//create xml nodes and format into the file
			return true;
		}
		return false;
	}

	/** This method is to validate if the File in the given path is in the correct format
	 * Parameters:	newFilePath - the file path of the file
	 * Return:		boolean - true if the file's format is correct, false otherwise
	 * **/
	private boolean validateFileFormat(String newFilePath){
		if(mStorage.isFileFormatValid(newFilePath)){ //if valid
			return true;
		}
		return false;
	}

	/** This method is to overwrite the File at the given file path
	 * Parameters:	newFilePath - the file path of the file to be overwritten
	 * Return:		boolean - true if the file is overwritten, false otherwise
	 * **/
	private boolean overwriteFile(String newFilePath){
		mStorage.initTaskList();
		mStorage.setDataFolderLocation(newFilePath);
		boolean isOverwrite = mStorage.overwriteNewXmlFile(newFilePath);
		return isOverwrite;
	}
	
	/** This method is to load the existing file at the given file path to the Storage
	 * Parameters:	newFilePath - the file path of the file
	 * Return:		boolean - true if the file loaded successfully, false otherwise
	 * **/
	private boolean loadExistingFile(String newFilePath){
		//read file
		mStorage.setDataFolderLocation(newFilePath);
		boolean isLoaded = mStorage.load(); //load the file into tasklist	
		return isLoaded;
	}

	//================================= UNDO & REDO =================================
	/** This method is to save the current state of the Storage the undoLIst
	 * Parameters:	currState - the current state of the Storage
	 * **/
	private void saveToUndo(ArrayList<Task> currState){
		if(undoList.size() < 3){
			undoList.add(currState);
		}else{
			undoList.remove(0);
			undoList.add(currState);
		}
	}

	/** This method is to save the current state of the Storage the redoLIst
	 * Parameters:	currState - the current state of the Storage
	 * **/
	private void saveToRedo(ArrayList<Task> currState){
		if(redoList.size() < 3){
			redoList.add(currState);
		}else{
			redoList.remove(0);
			redoList.add(currState);
		}
	}
	
	/** This method is to clear the redoList
	 * **/
	private void clearRedo(){
		redoList.clear();
	}

	/** This method is to update the redoList and undoList when new action is performed
	 * Parameters:	s - the current state of the Storage
	 * **/
	private void updateRedoAndUndo(ArrayList<Task> s){
		clearRedo();
		saveToUndo(s);
	}

	/** This method is to get the current state of the Storage
	 * return currentState - the current state of the Storage
	 * **/
	private ArrayList<Task> getCurrentState() {
		ArrayList<Task> currentState = new ArrayList<Task>(getStorageList());
		return currentState;
	}
	
	/** This method is to reorder the ID of the Task in the Storage to make the ID in a correct sequence based on
	 * how the Task is being shown
	 * **/
	private void reorderID() {
		for (int i = 0; i < mStorage.getTaskList().size(); i++) {
			mStorage.getTaskList().get(i).setID(i+1);
		}
		mStorage.save();
	}

}
