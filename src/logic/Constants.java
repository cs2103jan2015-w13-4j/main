package logic;


//contains all the constants used by the program
public class Constants {
	
	public static String PRODUCT_NAME = "Pista";
	
	public static String FILENAME = "Tasks.txt";
	public static String ADD_COMMAND = "add para1 para2";
	public static String EDIT_COMMAND = "edit para1 para2";
	public static String DELETE_COMMAND = "delete para1 para2";
	public static String LIST_COMMAND = "list para1 para2";
	
	//Parser Message 
	public static String WRONG_CMD_MESSAGE = "No/Wrong command detected!";
	public static String WRONG_PARAMETER_MESSAGE = "No/Wrong parameters detected!";
	public static String CORRECT_INPUT_MESSAGE = "valid";
	
	//Logic Message
	public static String LOGIC_INVALID_PARAMETER_MESSAGE = "Invalid parameters detected!";
	public static String LOGIC_VALID_PARAMETER_MESSAGE = "valid";
	public static String LOGIC_SUCCESS_ADD_TASK = "Successfully add new task.";
	public static String LOGIC_FAIL_ADD_TASK = "Failed to add new task.";
	
	public static String XML_FILE_PATH = "init_task.xml";
	
	public static String MAIN_UI_LAYOUT_PAH = "view/MainUI.fxml";
	public static String ROOT_LAYOUT_PATH = "view/RootLayout.fxml";
	public static String LISTVIEW_LAYOUT_PATH = "view/customListView.fxml";
	
	
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
	
	
	public static final String ADD_COMMAND_VALUE = "add";
	public static final String EDIT_COMMAND_VALUE = "edit";
	public static final String DELETE_COMMAND_VALUE = "delete";
}
