package pista;


//contains all the constants used by the program
public class Constants {
	
	public static String PRODUCT_NAME = "Pista";
	
	public static String FILENAME = "Tasks.txt";
	public static String ADD_COMMAND = "add <Task Name> -<DD/MM/YYYY> -<HH:MM> -<DD/MM/YYYY> -<HH:MM>";
	public static String EDIT_COMMAND = "edit <ID> -<Task Name> -<DD/MM/YYYY> -<HH:MM> -<DD/MM/YYYY> -<HH:MM>";
	public static String DELETE_COMMAND = "delete -<TaskID>";
	
	//Parser Message 
	public static final String MESSAGE_EMPTY_STRING = "No input detected!";
	public static final String MESSAGE_WRONG_COMMAND = "No/Wrong command detected!";
	public static final String MESSAGE_WRONG_PARAMETERS = "No/Wrong parameters detected!";
	public static final String MESSAGE_VALID_INPUT = "Valid";
	public static final int TOKEN_NUM_ADD_ONE = 1;
	public static final int TOKEN_NUM_ADD_THREE = 3;
	public static final int TOKEN_NUM_ADD_FIVE = 5;
	public static final int TOKEN_NUM_EDIT_TWO = 2;
	public static final int TOKEN_NUM_EDIT_FOUR = 4;
	public static final int TOKEN_NUM_EDIT_SIX = 6;
	public static final int EDIT_TOKEN_TITLE = 1;
	public static final int EDIT_TOKEN_DEADLINE_ENDDATE = 2;
	public static final int EDIT_TOKEN_DEADLINE_ENDTIME = 3;
	public static final int EDIT_TOKEN_TIMED_STARTDATE = 2;
	public static final int EDIT_TOKEN_TIMED_STARTTIME = 3;
	public static final int EDIT_TOKEN_TIMED_ENDDATE = 4;
	public static final int EDIT_TOKEN_TIMED_ENDTIME = 5;
	public static final int ADD_TOKEN_TITLE = 0;
	public static final int ADD_TOKEN_DEADLINE_ENDDATE = 1;
	public static final int ADD_TOKEN_DEADLINE_ENDTIME = 2;
	public static final int ADD_TOKEN_TIMED_STARTDATE = 1;
	public static final int ADD_TOKEN_TIMED_STARTTIME = 2;
	public static final int ADD_TOKEN_TIMED_ENDDATE = 3;
	public static final int ADD_TOKEN_TIMED_ENDTIME = 4;
	
	//Logic Message
	public static String LOGIC_INVALID_PARAMETER_MESSAGE = "Invalid parameters detected!";
	public static String LOGIC_VALID_PARAMETER_MESSAGE = "valid";
	public static String LOGIC_SUCCESS_ADD_TASK = "Successfully add new task.";
	public static String LOGIC_SUCCESS_EDIT_TASK = "Successfully edit task.";
	public static String LOGIC_SUCCESS_MARK_TASK = "Successfully mark task.";
	public static String LOGIC_SUCCESS_DELETE_TASK = "Successfully delete task.";
	public static String LOGIC_SUCCESS_DELETE_ALL_TASKS = "Successfully delete all task.";
	public static String LOGIC_FAIL_ADD_TASK = "Failed to add new task.";
	public static String LOGIC_FAIL_EDIT_TASK = "Failed to edit task.";
	public static String LOGIC_FAIL_MARK_TASK = "Failed to mark task.";
	public static String LOGIC_FAIL_DELETE_TASK = "Failed to delete task.";
	public static String LOGIC_FAIL_DELETE_ALL_TASKS = "Failed to delete all tasks.";
	public static String LOGIC_DELETE_TASK_NOT_FOUND = "No such task existed.";
	public static String LOGIC_EDIT_TASK_NOT_FOUND = "No such task existed.";
	public static String LOGIC_SUCCESS_LOAD_XML = "Successfully read tasks from file.";
	public static String LOGIC_FAIL_LOAD_XML = "Failed to read tasks from file.";
	
	//public static String XML_FILE_NAME = "init_task.xml";
	//public static String XML_FILE_PATH = "";
	
	public static final String MAIN_UI_LAYOUT_PATH = "ui/MainUI.fxml";
	public static final String ROOT_LAYOUT_PATH = "ui/RootLayout.fxml";
	public static final String LISTVIEW_LAYOUT_PATH = "ui/customListView.fxml";
	public static final String SETTING_LAYOUT_PATH = "SettingLayout.fxml";
	
	public static final String SETTING_LAYOUT_TITLE = "Setting";
	
	
	public static String STATUS_DONE="done";
	public static String STATUS_UNDONE="undone";
	
	public static final int ARRAY_SIZE = 10;
	public static final int ARRAY_INDEX_TITLE = 0;
	public static final int ARRAY_INDEX_START_DATE = 1;
	public static final int ARRAY_INDEX_START_TIME = 2;
	public static final int ARRAY_INDEX_END_DATE = 3;
	public static final int ARRAY_INDEX_END_TIME = 4;
	public static final int ARRAY_INDEX_REPEAT = 5;
	public static final int ARRAY_INDEX_DELAYTYPE = 6;
	public static final int ARRAY_INDEX_RECUR = 7;
	public static int ARRAY_INDEX_START_MILLISECONDS = 8;
	public static int ARRAY_INDEX_END_MILLISECONDS = 9;
	public static final int ONE_DAY = 1;
	public static final int ONE_WEEK = 1;
	public static final int ONE_MONTH = 1;
	public static final int ONE_YEAR = 1;
	public static final int DELAY_DURATION_ONE = 1;
	public static final int FLOATING_TASK=1;
	public static final int DEADLINE_TASK=3;
	public static final int TIMED_TASK=5;
	public static final int EDIT_FLOATING_TASK=2;
	public static final int EDIT_DEADLINE_TASK=3;
	public static final int EDIT_TIMED_TASK=5;
	
	public static final String VALUE_ADD = "add";
	public static final String VALUE_EDIT = "edit";
	public static final String VALUE_DELETE = "delete";
	
	public static final String LISTVIEW_DATETIME_STRING_FORMAT = "Due on %s, %s";
	public static final String DEFAULT_VALUE="default";
	
	
	//========== LOGGING ===========
	public static final String LOG_FILE_NAME = "pista_log.txt";
	public static final String LOG_LOGIC_SUCCESS_ADD_TASK="Successfully added %s task of %s category into list";
	public static final String LOG_LOGIC_SUCCESS_DELETE_TASK = "Successfully delete %s task out of list";
	public static final String LOG_LOGIC_FAIL_ADD_TASK="Fail to add task into list";
	public static final String LOG_LOGIC_SUCCESS_EDIT_TASK="Successfully edited %s of category %s task";
	public static final String LOG_LOGIC_FAIL_EDIT_TASK="Fail to edit task it is not found in list";
	public static final String LOG_UI_RUN_ON_ENTER = "Executed user command: ";
	public static final String LOG_UI_SUCCESS_RUN_ON_ENTER = "Successfully executed user command: ";
	public static final String LOG_UI_FAIL_VALIDATE_INPUT = "Failed validateInput: ";
	public static final String LOG_UI_SUCCESS_VALIDATE_INPUT = "Successful Validate Input";

	
	
	
	//Setting
	public static final String SETTING_DEFAULT_FOLDER_PATH = System.getProperty("user.home") + "\\"+"Desktop";
}
