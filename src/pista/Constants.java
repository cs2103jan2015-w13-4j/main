package pista;

import java.nio.file.Paths;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;


//contains all the constants used by the program
public class Constants {
	/*============================= UI ========================================== */
	public static final String PRODUCT_NAME = "Pista";
	public static final String BUILD_PATH =  Paths.get("").toAbsolutePath().toString();
	
	/*----- FXML file paths-----*/
	public static final String MAIN_UI_LAYOUT_PATH = "ui/MainUI.fxml";
	public static final String ROOT_LAYOUT_PATH = "ui/RootLayout.fxml";
	public static final String LISTVIEW_LAYOUT_PATH = "ui/customListView.fxml";
	public static final String SETTING_LAYOUT_PATH = "SettingLayout.fxml";
	
	//User Interface (UI) - controls(button), css, popover, messages, tooltip
	public static final double UI_BUTTON_IMAGE_TOP_WIDTH = 75.0;
	public static final double UI_BUTTON_TOP_WIDTH = 40.0;
	public static final double UI_BUTTON_TOP_HEIGHT = 40.0;
	public static final double UI_IMG_INSIDE_BUTTON_WIDTH = 20.0;
	public static final double UI_IMG_INSIDE_BUTTON_HEIGHT = 20.0;
	
	/*----- Key Combination -----*/
	public static final KeyCombination KEY_COMBINATION_HELP = new KeyCodeCombination(KeyCode.F1);
	public static final KeyCombination KEY_COMBINATION_START_GUIDE = new KeyCodeCombination(KeyCode.F2);
	public static final KeyCombination KEY_COMBINATION_AUTO_COMPLETE = new KeyCodeCombination(KeyCode.SPACE,KeyCombination.CONTROL_DOWN);
	public static final KeyCombination KEY_COMBINATION_UP = new KeyCodeCombination(KeyCode.UP); //press up to see latest used command
	public static final KeyCombination KEY_COMBINATION_DOWN = new KeyCodeCombination(KeyCode.DOWN); //press down to see previous used command
	
	/*----- CSS ------*/
	public static final String UI_CSS_IMAGE_BACKGROUND = "image-background";
	public static final String UI_CSS_TEXT_BACKGROUND  = "text-background";
	public static final String UI_CSS_TRANSPARENT_BACKGROUND = "transparent-background";
	public static final String UI_CSS_LIST_VIEW = "list-view-style";
	public static final String UI_CSS_TEXT_BOX = "text-box-style";
	public static final String UI_CSS_TEXT_STATUS = "text-status-style";
	public static final String UI_CSS_TEXT_CLOCK = "text-clock-style";
	public static final String UI_CSS_BUTTON = "button-style";
	public static final String UI_CSS_BUTTON_IMAGE = "button-image-style";
	public static final String UI_CSS_BUTTON_SETTING = "button-setting-style";
	public static final String UI_CSS_BUTTON_ADD = "button-add-style";
	public static final String UI_CSS_BUTTON_HELP = "button-help-style";
	public static final String UI_CSS_BUTTON_REFRESH = "button-refresh-style";
	public static final String UI_CSS_BUTTON_REDO = "button-redo-style";		
	public static final String UI_CSS_BUTTON_UNDO = "button-undo-style";
	public static final String UI_CSS_START_GUIDE_PANE = "start-guide-pane";
	public static final String UI_CSS_START_GUIDE_BUTTON = "start-guide-button";
	public static final String UI_CSS_POP_OVER_TEXTAREA = "pop-over-text-area";
	public static final String UI_CSS_POP_OVER_TOOLTIP = "pop-label-time-tip";
	public static final String UI_CSS_POP_OVER_CONTENT_AREA = "pop-content-area";
	public static final String UI_CSS_POP_OVER_TITLE= "pop-label-title";
	public static final String UI_CSS_POP_OVER_BUTTON = "pop-btn";
	public static final String UI_CSS_POP_OVER_ERROR_MESSAGE = "pop-label-error-message";
	public static final String UI_CSS_POP_OVER_CORRECT_MESSAGE = "pop-label-correct-message";
	
	/*----- Pop Over (Pop-up) -----*/
	public static final String UI_POP_OVER_FAILED_SETTING_MESSAGE = "Setting Failed";
	public static final String UI_POP_OVER_INVALID_FILE_MESSAGE = "Invalid File";
	public static final String UI_POP_OVER_SUCCESS_SETTING_MESSAGE = "Updated";
	public static final String UI_POP_OVER_INVALID_TITLE_MESSAGE = "Task cannot be empty";
	public static final String UI_POP_OVER_INVALID_START_TIME_MESSAGE = "Invalid Start Time";
	public static final String UI_POP_OVER_INVALID_END_TIME_MESSAGE = "Invalid End Time";
	public static final String UI_POP_OVER_SUCCESS_ADD_MESSAGE = "Added Successfully";
	public static final String UI_POP_OVER_FAIL_ADD_MESSAGE = "Fail to add";
	public static final String UI_POP_OVER_ADD_TASK_COMMAND = "add [task_title] -[start_date] -[start_time] -[end_date] -[end_time]";
	public static final double UI_POP_OVER_SETTING_WIDTH = 500.0;
	public static final double UI_POP_OVER_SETTING_HEIGHT = 200.0;
	public static final double UI_POP_OVER_ADD_WIDTH = 400.0;
	public static final double UI_POP_OVER_ADD_HEIGHT = 380.0;
	public static final double UI_POP_OVER_LABEL_MESSAGE_HEIGHT = 30.0;
	public static final double UI_POP_OVER_LABEL_DATETIME_WIDTH = 80.0;
	
	/*----- ToolTip -----*/
	public static final String UI_TOOLTIP_HELP = "Help";
	public static final String UI_TOOLTIP_SETTING = "Setting";
	public static final String UI_TOOLTIP_REFRESH = "Refresh the list";
	public static final String UI_TOOLTIP_ADD = "Add New Task";
	public static final String UI_TOOLTIP_REDO = "Redo to last action";
	public static final String UI_TOOLTIP_UNDO = "Undo to previous action";
	
	/*----- Status Messages ------*/
	public static final String UI_STATUS_EMPTY_XML_FILE_PATH_MESSAGE = "Please provide a XML file to keep track of your data";
	public static final String UI_STATUS_INVALID_XML_FILE_PATH_MESSAGE  = "Either your file is not empty or invalid format.";
	public static final String UI_STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE = "Failed to load selected file.";
	public static final String UI_STATUS_FAIL_TO_SAVE_DUE_TO_INVALID_FILE_MESSAGE = "Please ensure your file is valid before saving.";
	public static final String UI_STATUS_FAIL_TO_SAVE = "Unable to save setting. Please try again.";
	public static final String UI_STATUS_APPLICATION_ERROR_MESSAGE = "Application error. Please contact the administrator";
	public static final String UI_STATUS_SUCCESS_FILE_CREATED_MESSAGE = "[new_file_path] is loaded.";
	
	/*----- Alarm -----*/ 
	public static final String UI_ALARM_LOCATION = "/bin/sounds/alarm.mp3";
	
	
	/*----- Setting -----*/
	public static final String SETTING_LAYOUT_TITLE = "Setting";
	
	/*----- Date format -----*/
	public static final String DATETIME_FORMAT_CLOCK = "dd MMM yyyy HH:mm:ss a"; //e.g. 18 Jan 2015 03:00:00 AM
	public static final String DATETIME_FORMAT_DATEPICKER = "dd MMMM yyyy"; //e.g. 18 January 2015
	public static final String DATETIME_FORMAT_STORAGE = "d/M/yyyy"; //e.g. 18/4/2015
	
	/*============================= End of UI =================================== */
	
	/*============================= Parser ====================================== */
	/*----- Parser Message -----*/
	public static final String MESSAGE_EMPTY_STRING = "No input detected!";
	public static final String MESSAGE_WRONG_COMMAND = "No/Wrong command detected!";
	public static final String MESSAGE_WRONG_PARAMETERS = "No/Wrong parameters detected!";
	public static final String MESSAGE_VALID_INPUT = "Valid";
	public static final String MESSAGE_STARTDATE_GREATER_THAN_ENDDATE = "Start date cannot be greater than end date";
	public static final String MESSAGE_INVALID_DATE_INPUT = "Invalid date input";
	public static final String MESSAGE_INVALID_TIME_INPUT = "Invalid time input";
	public static final String MESSAGE_INVALID_TOKEN_LENGTH = "Invalid token length";
	public static final String MESSAGE_ID_LESS_THAN_ONE = "ID cannot be less than one";
	public static final String MESSAGE_INVALID_SHORTHAND = "invalid shorthand for delete";
	public static final String MESSAGE_INVALD_NATTY_DATE = "Unable to interpret the date format you have typed";
  	public static final String MESSAGE_INVALD_NATTY_TIME = "Unable to interpret the time format you have typed";
  	public static final String MESSAGE_EMPTY_TITLE = "The title cannot be empty";
  	public static final String MESSAGE_EDIT_EMPTY_TOKENS = "Empty tokens detected for Edit function";
  	public static final String MESSAGE_INVALID_SORT_FUNCTION = "The sort function you have entered is invalid";
  	public static final String REMINDER_OFF = "off";
  	public static final String REMINDER_INVALID_STATUS = "only allowed to set reminder as off";
  	public static final String MARK_DONE = "done";
  	public static final String MARK_UNDONE = "undone";
	public static final String INVALID_MARK = "You can only mark task as done or undone";
	public static final String INVALID_REMINDER = "invalid reminder tokens paramter";
	public static final String INVALID_PRIORITY_SCORE = "Prority range can be only 0-3";
	public static final String SORT_TYPE = "type";
	public static final String SORT_OVERVIEW = "overview";
	public static final String SORT_ASCENDING_START_DATE = "ascending start date";
	public static final String SORT_DESCENDING_START_DATE = "descending start date";
	public static final String SORT_ASCENDING_END_DATE = "ascending end date";
	public static final String SORT_DESCENDING_END_DATE = "descending end date";
	public static final String SORT_ASCENDING_TITLE = "ascending title";
	public static final String SORT_DESCENDING_TITLE = "descending title";
	public static final String SORT_ASCENDING_PRIORITY = "ascending priority";
	public static final String SORT_DESCENDING_PRIORITY = "descending priority";
	public static final String SORT_ISDONE_UNDONE = "undone";
	public static final String SORT_ISDONE_DONE = "done";
	public static final String SORT_PRIORITY = "priority";
	public static final String SET_TYPE_FILE_LOCATION = "file location";
	public static final String SET_FILE_TYPE = ".xml";
	public static final String MESSAGE_EDIT_DEADLINE_INVALID_CLEAR_COUNT = "Invalid clear count. To change from deadline to floated task, there must be 2 clear count ";
	public static final String MESSAGE_EDIT_Timed_INVALID_CLEAR_COUNT = "Invalid clear count. To change from Timed to floated task, there must be 4 clear count. To deadline, 2 clear count  ";
	public static final String MESSAGE_EDIT_Timed_INVALID_CLEAR_COUNT_TIMED_TO_DEADLINE = "To convert timed to deadline, make sure the last 2 parameters contain c";
	public static final int TOKEN_NUM_PRIORITY_SCORE = 1;
	public static final int TOKEN_NUM_PRORITY_ID = 0;
	public static final int TOKEN_NUM_REMINDER_OFF = 1;
	public static final int TOKEN_NUM_REMINDER_TWO = 2;
	public static final int TOKEN_NUM_REMINDER_THREE = 3;
	public static final int REMINDER_ID = 0;
	public static final int REMINDER_DATE = 1;
	public static final int REMINDER_TIME = 2;
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
	public static final int SPLIT_INTO_TWO = 2;
	public static final int INDEX_ZERO = 0;
	public static final int INDEX_ONE = 1;
	public static final int INDEX_TWO = 2;
	public static final int LENGTH_ONE = 1;
	public static final int LENGTH_TWO = 2;
	public static final int DEFAULT_PRIORITY = 0;
	public static final int HIGH_PRIORITY = 3;
	
	/*============================= End of Parser =============================== */
	
	/*============================= Logic ======================================= */
	/*----- Logic Message -----*/
	public static final String LOGIC_INVALID_PARAMETER_MESSAGE = "Invalid parameters detected!";
	public static final String LOGIC_VALID_PARAMETER_MESSAGE = "valid";
	public static final String LOGIC_SUCCESS_ADD_TASK = "Successfully add new task.";
	public static final String LOGIC_SUCCESS_EDIT_TASK = "Successfully edit task.";
	public static final String LOGIC_SUCCESS_MARK_TASK = "Successfully mark task.";
	public static final String LOGIC_SUCCESS_HELP = "Open help";
	public static final String LOGIC_SUCCESS_SEARCH = "Searching tasks for the keyword: ";
	public static final String LOGIC_SUCCESS_DELETE_TASK = "Successfully delete task.";
	public static final String LOGIC_SUCCESS_DELETE_ALL_TASKS = "Successfully delete all task.";
	public static final String LOGIC_SUCCESS_UNDO = "Successfully undo the last action";
	public static final String LOGIC_SUCCESS_REDO = "Successfully redo";
	public static final String LOGIC_FAIL_ADD_TASK = "Failed to add new task.";
	public static final String LOGIC_FAIL_EDIT_TIMED_TO_DEADLINE_TASK = "To edit timed to deadline, use edit id -d-ed-et-c-c";
	public static final String LOGIC_FAIL_EDIT_TASK = "Failed to edit task.";
	public static final String LOGIC_SUCCESS_PRIORITY_TASK = "Successfully set priority for task";
	public static final String LOGIC_FAIL_PRIORITY_NOT_FOUND_TASK = "Failed to set priority for task as it is not found.";
	public static final String LOGIC_SUCCESS_REMIND_TASK = "Successfully set reminder for task";
	public static final String LOGIC_SUCCESS_REMIND_OFF_TASK = "Successfully off reminder for task";
	public static final String LOGIC_FAIL_REMIND_LATER_THAN_ENDDATE_TASK = "unable to set reminder as it is later than the end date";
	public static final String LOGIC_FAIL_REMIND_NOT_FOUND_TASK = "Failed to remind task as it is not found.";
	public static final String LOGIC_FAIL_REMIND_FLOATING_TASK = "you cannot set a reminder for floating task";
	public static final String LOGIC_FAIL_REMIND_off_FLOATING_TASK = "No reminder can be off for floating task";
	public static final String LOGIC_FAIL_MARK_NOT_FOUND_TASK = "Failed to mark task as it is not found.";
	public static final String LOGIC_FAIL_DELETE_TASK = "Failed to delete task.";
	public static final String LOGIC_FAIL_DELETE_ALL_TASKS = "Failed to delete all tasks.";
	public static final String LOGIC_DELETE_TASK_NOT_FOUND = "No such task existed.";
	public static final String LOGIC_EDIT_TASK_NOT_FOUND = "No such task existed.";
	public static final String LOGIC_MARK_TASK_NOT_FOUND = "Unable to mark as no such task existed.";
	public static final String LOGIC_SUCCESS_LOAD_XML = "Successfully read tasks from file.";
	public static final String LOGIC_FAIL_LOAD_XML = "Failed to read tasks from file.";
	public static final String LOGIC_FAIL_UNDO = "Failed to undo the last action";
	public static final String LOGIC_FAIL_REDO = "Failed to redo";
	public static final String LOGIC_SUCESS_SORTED = "List is successfully sorted in %s";
	public static final String LOGIC_INVALID_SET_COMMAND_TYPE = "Invalid type for set command";
	public static final String LOGIC_INVALID_SET_COMMAND_LENGTH = "Insufficient commands.";
	public static final String LOGIC_INVALID_SET_FILE_NAME = "Incorrect file name.";
	public static final String LOGIC_INVALID_SET_INVALID_FILE_FORMAT = "Either your file is not empty or invalid format.";
	public static final String LOGIC_FAIL_SET_LOAD_FILE =  "Failed to load selected file.";
	public static final String LOGIC_FAIL_SET_SAVE = "Fail to save setting.";
	public static final String LOGIC_SUCCESS_SET_SAVE = "Sucessfully save setting.";
		
	public static final String TASK_FLOATED = "floating";
	public static final String TASK_DEADLINE = "deadline";
	public static final String TASK_TIMED = "timed";
	
	public static final String STATUS_DONE = "done";
	public static final String STATUS_UNDONE = "undone";
	
	public static final int ARRAY_SIZE = 10;
	public static final int ARRAY_INDEX_TITLE = 0;
	public static final int ARRAY_INDEX_START_DATE = 1;
	public static final int ARRAY_INDEX_START_TIME = 2;
	public static final int ARRAY_INDEX_END_DATE = 3;
	public static final int ARRAY_INDEX_END_TIME = 4;
	public static final int ARRAY_INDEX_REPEAT = 5;
	public static final int ARRAY_INDEX_DELAYTYPE = 6;
	public static final int ARRAY_INDEX_RECUR = 7;
	public static final int ARRAY_INDEX_START_MILLISECONDS = 8;
	public static final int ARRAY_INDEX_END_MILLISECONDS = 9;
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
	public static final int SET_TYPE_INDEX = 0;
	public static final int SET_VALUE_INDEX = 1;
	
	public static int HISTORY_INDEX = 0;
	
	public static final String VALUE_ADD = "add";
	public static final String VALUE_EDIT = "edit";
	public static final String VALUE_DELETE = "delete";
	public static final String VALUE_UNDO = "undo";
	public static final String VALUE_REDO = "redo";
	public static final String VALUE_MARK = "mark";
	public static final String VALUE_HELP = "help";
	public static final String VALUE_SORT = "sort";
	public static final String VALUE_SET = "set";
	public static final String VALUE_REMINDER = "remind";
	public static final String VALUE_PRIORITY = "priority";
	public static final String VALUE_SEARCH = "search";

	//public static final String LISTVIEW_DATETIME_STRING_FORMAT = "Due on %s, %s";
	public static final String DEFAULT_IGNORE_VALUE="d";
	public static final String DEFAULT_CLEAR_VALUE="c";
	
	/*============================= End of Logic ================================ */
	
	/*============================= Storage ===================================== */
	/*============================= End of Storage ============================== */
	
	/*============================= Logging ===================================== */
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

	/*============================= End of Logging ============================== */
	
	/*============================= Setting ===================================== */
	public static final String SETTING_DEFAULT_FOLDER_PATH = System.getProperty("user.home") + "\\"+"Desktop";
		
	/*============================= End of Setting ============================== */
	
	/*============================= Preferences ===================================== */
	public static final String PREFERENCE_URL_PATH = "pista/preference";
	public static final String PREFERENCE_XML_DEFAULT_FILE_PATH = "preferences.xml";
	public static final String PREFERENCE_XML_ROOT_NODE = "preferences";
	public static final String PREFERENCE_XML_SETTING_NODE = "setting";
	public static final String PREFERENCE_XML_FILE_LOCATION_NODE = "file_location";
	public static final String PREFERENCE_XML_DEFAULT_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><preferences><setting><file_location></file_location></setting></preferences>";
	public static final String PREFERENCE_PISTA_FLAG_NODE = "flag";
	public static final int PREFERENCE_FIRST_LAUNCH_VALUE = 0;
	public static final int PREFERENCE_SUBSQUENT_LAUNCH_VALUE = 1;
	public static final int PREFERENCE_ERROR_LAUNCH_VALUE = -1;
	/*============================= End of Prefences ============================== */


	/*============================= Help ===================================== */
	public static final String HELP_TITLE = "Help";
	public static final String HELP_STAGE_HEIGHT = "700";
	/*============================= End of Help ============================== */



	
	
}
