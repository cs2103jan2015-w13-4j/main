//@author A0111884E
package pista.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pista.Constants;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class MainParser {

	String command="";
	String[] tokens;
	String message="";
	boolean validTokens=false;


	public MainParser(){
		tokens=null;
	}

	public String getCommand(){return command;}
	public String[] getTokens(){return tokens;}
	public String getItemInTokenIndex(int index){return tokens[index];}
	public String getMessage(){return message;}
	private void setCommand(String command){this.command = command;}
	private void setTokens(String[] tokens){this.tokens = tokens;}
	private void setMessage(String message){this.message = message;}
	private void setIndexInToken(int index, String input){this.tokens[index] = input;}	
	//@author A0111721Y
	/*
	 * tokens
	 * [0] - command type
	 * [1] - parameter in string 
	 * */
	
	/**This method is the main method that use to validate the input provided by the user
	 * Parameters: String - a string that consists of command + parameters
	 * Return:		mp - a MainParser object that contains the message of validity.
	 * **/
	public static MainParser validateInput(String input) {
		MainParser mp = new MainParser();
		if (isEmptyString(input)) {
			mp.setMessage(Constants.PARSER_MESSAGE_EMPTY_STRING);
			return mp;
		} else {
			String command = getCommand(input);
			if (!isCommandValid(command)) {
				mp.setMessage(Constants.PARSER_MESSAGE_WRONG_COMMAND);
				return mp;
			} else {
				if (command.equalsIgnoreCase(Constants.VALUE_HELP)) {
					mp.setCommand(command);
					mp.setMessage(Constants.PARSER_MESSAGE_VALID_INPUT);
					return mp;
				} else {
					String[] tokens = getTokens(input);

					if (!mp.isTokensValid(mp, command, tokens)) {
						//mp.setMessage(Constants.MESSAGE_WRONG_PARAMETERS);
						return mp;
					} else {
						mp.setCommand(command);
						mp.setMessage(Constants.PARSER_MESSAGE_VALID_INPUT);
						return mp;
					}
				}
			}
		}
	}
	//@author A0112522Y
	/**This method is checks if a string is empty
	 * Parameters: String - a string that consists of command + parameters
	 * Return:		Boolean
	 * 				True if empty
	 * 				false if it is not
	 * **/
	public static boolean isEmptyString(String input){
		if(input.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**This method extracts the user command from a string
	 * Parameters: String - a string that consists of command + parameters
	 * Return:		String - which consists only the command
	 * **/
	public static String getCommand(String input){
		String command=input.split(" ",Constants.SPLIT_INTO_TWO)[Constants.INDEX_ZERO];
		return command;
	}
	
	//@author A0112522Y
	/**This method checks if the command entered is supported by the program
	 * Parameters: String - a string that contains the user's command
	 * Return:		True if it is a valid command
	 * 				False if it is not
	 * **/
	private static boolean isCommandValid(String command){
		if(command.equalsIgnoreCase(Constants.VALUE_ADD) || command.equalsIgnoreCase(Constants.VALUE_EDIT) || 
				command.equalsIgnoreCase(Constants.VALUE_DELETE) || command.equalsIgnoreCase(Constants.VALUE_REDO) || 
				command.equalsIgnoreCase(Constants.VALUE_UNDO) || command.equalsIgnoreCase(Constants.VALUE_MARK) ||
				command.equalsIgnoreCase(Constants.VALUE_HELP) || command.equalsIgnoreCase(Constants.VALUE_SORT) || 
				command.equalsIgnoreCase(Constants.VALUE_SET) ||
				command.equalsIgnoreCase(Constants.VALUE_REMINDER) ||
				command.equalsIgnoreCase(Constants.VALUE_PRIORITY) ||
				command.equalsIgnoreCase(Constants.VALUE_SEARCH)){ //check for command type		
			return true;
		}else{
			assert false:"unacceptable command typed: "+command;
		return false;
		}//end if

	}//end checkCommand

	//@author A0111884E
	/**This method extracts parameters from a string and put them inside a String array
	 * Parameters: String - a string that consists of command + parameters
	 * Return:		String array - Provided that all parameters have been entered
	 * 				null if no parameters are entered after the command
	 * **/
	public static String[] getTokens(String input){
		String[] temp = input.split(" ",Constants.SPLIT_INTO_TWO);

		if(temp.length > 1){
			String[] arr = temp[Constants.INDEX_ONE].split("-");
			trimWhiteSpace(arr);
			return arr;
		}else{
			return null;
		}
	}

	//@author A0111721Y
	/**This method checks if the tokens/parameters supplied by the user
	 * Parameters: Object of MainParser -  
	 * 				String - contains the user's command
	 * 				String array - contains all parameters  
	 * Return:		Boolean -
	 * 				True if all tokens/parameters are correct
	 * 				False if the tokens/parameters are wrong
	 * **/
	private boolean isTokensValid(MainParser mp, String command, String[] tokens){
		switch(command) {
		case Constants.VALUE_ADD:
			return checkAddTokens(mp, tokens);
		case Constants.VALUE_EDIT:
			return checkEditTokens(mp, tokens);
		case Constants.VALUE_DELETE:
			return checkDeleteTokens(mp, tokens);
		case Constants.VALUE_MARK:
			return checkMarkTokens(mp, tokens);
		case Constants.VALUE_REDO:
			return true;
		case Constants.VALUE_UNDO:
			return true;
		case Constants.VALUE_REMINDER:
			return checkReminderTokens(mp, tokens);
		case Constants.VALUE_PRIORITY:
			return checkPriorityTokens(mp, tokens);
		case Constants.VALUE_SET:
			return checkSetTokens(mp, tokens);
		case Constants.VALUE_SEARCH:
			return checkSearchTokens(mp, tokens);
		case Constants.VALUE_SORT:
			return checkSortTokens(mp, tokens);
		default:
			return false;
		}
	}
	
	//@author A0111884E
	/**This method checks the tokens for the Add command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkAddTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length == Constants.TOKEN_NUM_ADD_ONE){
			if(TokenValidation.isTitleValid((tokens[Constants.ADD_TOKEN_TITLE]))){
				return true;
			}
			mp.setMessage(Constants.PARSER_MESSAGE_EMPTY_TITLE);
			return false;
		}
		else if(tokens.length == Constants.TOKEN_NUM_ADD_THREE){
			if(TokenValidation.isTitleValid(tokens[Constants.ADD_TOKEN_TITLE])){
				if(!isDateAndTimeValid(mp, tokens, Constants.ADD_TOKEN_DEADLINE_ENDDATE, 
						Constants.ADD_TOKEN_DEADLINE_ENDTIME)){
					return false;
				}
				return true;
			}
			return false;
		}
		else if(tokens.length == Constants.TOKEN_NUM_ADD_FIVE){
			if(TokenValidation.isTitleValid(tokens[Constants.ADD_TOKEN_TITLE])) {
				boolean startDateTimeValid = isDateAndTimeValid(mp, tokens, Constants.ADD_TOKEN_TIMED_STARTDATE, Constants.ADD_TOKEN_TIMED_STARTTIME);
				boolean endDateTimeValid = isDateAndTimeValid(mp, tokens, Constants.ADD_TOKEN_TIMED_ENDDATE, Constants.ADD_TOKEN_TIMED_ENDTIME);
				if(startDateTimeValid && endDateTimeValid){
					if(TokenValidation.isStartDateBeforeThanEndDate(mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTDATE),
							mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDDATE), 
							mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTTIME), 
							mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDTIME))){
						return true;
					}else{
						mp.setMessage(Constants.PARSER_MESSAGE_STARTDATE_GREATER_THAN_ENDDATE);
						return false;
					}
				}
				return false;
			}
			return false;
		}

		assert false:"Tokens number in add function are "+tokens.length +" allowed length are 1,3,5";
		mp.setMessage(Constants.PARSER_MESSAGE_ADD_EMPTY_TOKENS);
		return false;
	}
	
	/**This method checks the tokens for the Edit command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkEditTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length==Constants.TOKEN_NUM_EDIT_TWO){
			if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE])){	//edit id -title 
				return true;
			}
		}

		else if(tokens.length==Constants.TOKEN_NUM_EDIT_FOUR){
			if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE])) {
				if(!isDateAndTimeValid(mp, tokens, Constants.EDIT_TOKEN_DEADLINE_ENDDATE, 
						Constants.EDIT_TOKEN_DEADLINE_ENDTIME)){
					return false;
				}
				return true;
			}			
			return false;
		}

		else if(tokens.length==Constants.TOKEN_NUM_EDIT_SIX){
			if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE])){
				boolean startDateTimeValid = isDateAndTimeValid(mp, tokens, 
						Constants.EDIT_TOKEN_TIMED_STARTDATE, Constants.EDIT_TOKEN_TIMED_STARTTIME);
				boolean endDateTimeValid = isDateAndTimeValid(mp, tokens, 
						Constants.EDIT_TOKEN_TIMED_ENDDATE, Constants.EDIT_TOKEN_TIMED_ENDTIME);
					if(startDateTimeValid && endDateTimeValid){
						if(TokenValidation.isStartDateBeforeThanEndDate(mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_STARTDATE),
								mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_ENDDATE), 
								mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_STARTTIME), 
								mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_ENDTIME))){						
							return true;
						}else{
							mp.setMessage(Constants.PARSER_MESSAGE_STARTDATE_GREATER_THAN_ENDDATE);
							return false;
						}
					}
					return false;
			
			}
			return false;
		}
		assert false:"Tokens number in edit function are "+tokens.length +"allowed length is 2,4,6";
		mp.setMessage(Constants.PARSER_MESSAGE_EDIT_EMPTY_TOKENS);
		return false;
	}
	
	//@author A0112522Y
	/**This method checks the tokens for the Delete command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private static boolean checkDeleteTokens(MainParser mp, String[] tokens) {
		mp.setTokens(tokens);
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}

		String input = tokens[Constants.INDEX_ZERO];
		try { 
			int id = Integer.parseInt(input); 
			// if it is a number
			if( id < 1 ){
				mp.setMessage(Constants.PARSER_MESSAGE_ID_LESS_THAN_ONE);
				return false;
			}
			return true;

		} catch(NumberFormatException e) { //if it is not a number
			if(!input.equalsIgnoreCase("a")){
				mp.setMessage(Constants.PARSER_MESSAGE_INVALID_SHORTHAND);
				return false;
			}
			return true;
		}
	}
	
	//@author A0111884E
	/**This method checks the tokens for the Mark command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkMarkTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		String status = tokens[Constants.INDEX_ONE];
		if(Constants.PARSER_MARK_DONE.equalsIgnoreCase(status) || Constants.PARSER_MARK_UNDONE.equalsIgnoreCase(status)){
			return true;
		}
		mp.setMessage(Constants.PARSER_INVALID_MARK);
		return false;
	}
	
	/**This method checks the tokens for the Reminder command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkReminderTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length == Constants.TOKEN_NUM_REMINDER_TWO){
			if(tokens[Constants.TOKEN_NUM_REMINDER_OFF].equalsIgnoreCase(Constants.PARSER_REMINDER_OFF)){
				return true;
			}
			mp.setMessage(Constants.PARSER_REMINDER_INVALID_STATUS);
			return false;
		}else if(tokens.length == Constants.TOKEN_NUM_REMINDER_THREE){
			//String taskId = tokens[TOKEN_REMINDER_ID];
			if(!isDateAndTimeValid(mp, tokens, Constants.REMINDER_DATE, 
					Constants.REMINDER_TIME)){
				return false;
			}
			return true;
		}

		mp.setMessage(Constants.PARSER_INVALID_REMINDER);
		return false;
	}
	
	/**This method checks the tokens for the Priority command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkPriorityTokens (MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length == Constants.LENGTH_TWO){
			int priorityScore = Integer.parseInt(tokens[Constants.TOKEN_NUM_PRIORITY_SCORE]);
			if(priorityScore >= Constants.DEFAULT_PRIORITY && priorityScore <= Constants.HIGH_PRIORITY){
				return true;
			}else {
				mp.setMessage(Constants.PARSER_INVALID_PRIORITY_SCORE);
				return false;
			}
		}
		mp.setMessage(Constants.PARSER_INVALID_PRIORITY_SCORE);
		return false;
	}
	
	//@author A0125474E
	/**This method checks the tokens for the Set file location command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkSetTokens(MainParser mp, String[] tokens){
		String getType = "";
		String getFileLocation = "";

		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}

		if(tokens.length <= Constants.LENGTH_ONE){ //must have at least 2
			mp.setMessage(Constants.LOGIC_INVALID_SET_COMMAND_LENGTH);
			return false;
		}

		getType = tokens[Constants.SET_TYPE_INDEX]; //index of type is 0
		if(!getType.equalsIgnoreCase(Constants.PARSER_SET_TYPE_FILE_LOCATION)){
			mp.setMessage(Constants.LOGIC_INVALID_SET_COMMAND_TYPE);	
			return false;
		}

		getFileLocation = tokens[Constants.SET_VALUE_INDEX];
		if(!TokenValidation.isFileNameValid(getFileLocation)){
			mp.setMessage(Constants.LOGIC_INVALID_SET_FILE_NAME);
			return false;
		}

		mp.setTokens(tokens);
		return true;

	}
	
	//@author A0111721Y-reused
	/**This method checks the tokens for the Search command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are co
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkSearchTokens (MainParser mp, String[] tokens) {
		Parser parser = new Parser();
		String requiredNattyInputFormat = "";
		String interpretOutputDateFromNatty = "";
		List<DateGroup> groups;
		
		SimpleDateFormat nattySDF = new SimpleDateFormat("[EEE MMM dd HH:mm:ss z yyyy]");
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		
		if(tokens == null || tokens.length != Constants.LENGTH_ONE){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		
		if (tokens[Constants.INDEX_ZERO].equals("")) {
			mp.setMessage(Constants.PARSER_MESSAGE_EMPTY_STRING);
			return false;
		}
		
		String[] possibleKeywords = new String[Constants.SEARCH_POSSIBLE_PARAMETERS];
		mp.setTokens(possibleKeywords);
		mp.setIndexInToken(Constants.SEARCH_KEYWORD_STRING, tokens[Constants.SEARCH_TOKEN_KEYWORD]);
		
		//checks if keyword is a date
		if(!TokenValidation.isDateValid(tokens[Constants.SEARCH_TOKEN_KEYWORD])){
			requiredNattyInputFormat = nattyInputFormat(tokens, Constants.SEARCH_TOKEN_KEYWORD);
			
			if(!requiredNattyInputFormat.isEmpty()){
				groups = parser.parse(requiredNattyInputFormat);
			}else{
				groups = parser.parse(tokens[Constants.SEARCH_TOKEN_KEYWORD]);
			}

			try {
				interpretOutputDateFromNatty = sdf.format(nattySDF.parse(interpretInputNatty(groups)));
			} catch (ParseException e1) {
				interpretOutputDateFromNatty = "";
			}
			
			if(TokenValidation.isDateValid(interpretOutputDateFromNatty) && !interpretOutputDateFromNatty.isEmpty()){
				mp.setIndexInToken(Constants.SEARCH_KEYWORD_DATE, interpretOutputDateFromNatty);
			}
		}
				
		//checks if keyword is a time
		String interpretOutputTimeFromNatty="";
		SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm");
		
		if(!TokenValidation.isTimeValid(tokens[Constants.SEARCH_TOKEN_KEYWORD])){
			groups = parser.parse(tokens[Constants.SEARCH_TOKEN_KEYWORD]);
			
			try {
				interpretOutputTimeFromNatty = sdf2.format(nattySDF.parse(interpretInputNatty(groups)));
			} catch (ParseException e1) {
				interpretOutputTimeFromNatty = "";
			}
			
			if(TokenValidation.isTimeValid(interpretOutputTimeFromNatty) && !interpretOutputTimeFromNatty.isEmpty()){
				mp.setIndexInToken(Constants.SEARCH_KEYWORD_TIME, interpretOutputTimeFromNatty);
			}
		}
		
		System.out.println(mp.getTokens()[0] + mp.getTokens()[1] + mp.getTokens()[2]);
		return true;
	}
	
	//@author A0111884E
	/**This method checks the tokens for the Sort command
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * Return:		Boolean 
	 * 				True if the tokens are correct
	 * 				False if the tokens are wrong 
	 * **/
	private boolean checkSortTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		String sortType = tokens[Constants.INDEX_ZERO];
		if(Constants.PARSER_SORT_ASCENDING_END_DATE.equalsIgnoreCase(sortType) || 
				Constants.PARSER_SORT_ASCENDING_START_DATE.equalsIgnoreCase(sortType) ||
				Constants.PARSER_SORT_ASCENDING_TITLE.equalsIgnoreCase(sortType) ||
				Constants.PARSER_SORT_DESCENDING_END_DATE.equalsIgnoreCase(sortType)|| 
				Constants.PARSER_SORT_DESCENDING_START_DATE.equalsIgnoreCase(sortType) || 
				Constants.PARSER_SORT_DESCENDING_TITLE.equals(sortType) || 
				Constants.PARSER_SORT_ISDONE_DONE.equals(sortType) || 
				Constants.PARSER_SORT_ISDONE_UNDONE.equalsIgnoreCase(sortType) || 
				Constants.PARSER_SORT_OVERVIEW.equalsIgnoreCase(sortType) || 
				Constants.PARSER_SORT_TYPE.equalsIgnoreCase(sortType) 
				|| Constants.PARSER_SORT_ASCENDING_PRIORITY.equalsIgnoreCase(sortType) ||
				Constants.PARSER_SORT_DESCENDING_PRIORITY.equalsIgnoreCase(sortType)){
			return true;
		}
		mp.setMessage(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION);
		return false;
	}
	
	/**This method validates both the Date and Time Tokens using inbuilt validation. 
	 * If the inbuilt validation does not recognizes, it will be passed on to Natty to interpret it 
	 * before going through the inbuilt validation again
	 * Parameters: Object of MainParser
	 * 				String array - contains all parameters
	 * 				int - contains the index of both date and time found in the array
	 * Return:		Boolean 
	 * 				True if both the Date and Time tokens are correct
	 * 				False if both the Date and Time tokens are wrong 
	 * **/
	private static boolean isDateAndTimeValid(MainParser mp, String[] tokens, int dateIndex, int timeIndex) {
		Parser parser = new Parser();
		List<DateGroup> groups;
		String requiredNattyInputFormat = ""; 
		String interpretOutputDateFromNatty="";
		String interpretOutputTimeFromNatty="";
		
		SimpleDateFormat nattySDF = new SimpleDateFormat("[EEE MMM dd HH:mm:ss z yyyy]");
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm");
		
		tokens[dateIndex]=flexibleDateFormat(tokens[dateIndex]);
		
		if(!TokenValidation.isDateValid(tokens[dateIndex])){
			requiredNattyInputFormat = nattyInputFormat(tokens, dateIndex);
			if(!requiredNattyInputFormat.isEmpty()){
				groups = parser.parse(requiredNattyInputFormat);
			}else{
				groups = parser.parse(tokens[dateIndex]);
			}

			try {
				interpretOutputDateFromNatty = sdf.format(nattySDF.parse(interpretInputNatty(groups)));
			} catch (ParseException e1) {
				mp.setMessage(Constants.PARSER_MESSAGE_INVALD_NATTY_DATE);
				return false;
			}
			if(!TokenValidation.isDateValid(interpretOutputDateFromNatty) && !interpretOutputDateFromNatty.isEmpty()){
				mp.setMessage(Constants.PARSER_MESSAGE_INVALID_DATE_INPUT);
				return false;
			}else{
				mp.setIndexInToken(dateIndex, interpretOutputDateFromNatty);
			}
		}
		
		if(!TokenValidation.isTimeValid(tokens[timeIndex])){
			groups = parser.parse(tokens[timeIndex]);
			try {
				interpretOutputTimeFromNatty = sdf2.format(nattySDF.parse(interpretInputNatty(groups)));
			} catch (ParseException e1) {
				mp.setMessage(Constants.PARSER_MESSAGE_INVALD_NATTY_TIME);
				return false;
			}
			if(!TokenValidation.isTimeValid(interpretOutputTimeFromNatty) && !interpretOutputTimeFromNatty.isEmpty()){
				mp.setMessage(Constants.PARSER_MESSAGE_INVALID_TIME_INPUT);
				return false;
			}else{
				mp.setIndexInToken(timeIndex, interpretOutputTimeFromNatty);
			}
		}
		return true;
	}

	/**This method reorder of data format for natty as it recognizes mm/dd/yyyy instead of dd/mm/yyyy
	 * Parameters: String array - contains all parameters
	 * 				int - contains the index of a date in the array 
	 * Return:		String - in the order which natty recognizes
	 * **/
	private static String nattyInputFormat(String[] tokens, int dateIndex) {
		String requiredNattyInputDateFormat = "";
		String[] temp = tokens[dateIndex].split("/");
		if(StringUtils.countMatches(tokens[dateIndex], "/") == Constants.LENGTH_TWO){
			requiredNattyInputDateFormat = temp[Constants.INDEX_ONE] +"/"+ temp [Constants.INDEX_ZERO] 
					+"/"+ temp [Constants.INDEX_TWO];
		}else if (StringUtils.countMatches(tokens[dateIndex], "/") == Constants.LENGTH_ONE){
			requiredNattyInputDateFormat = temp[Constants.INDEX_ONE] +"/"+ temp [Constants.INDEX_ZERO];
		}else {
			assert StringUtils.countMatches(tokens[dateIndex], "/") > 3 ;
		}
		return requiredNattyInputDateFormat;
	}

	//@author A0111884E-reused
	/**This method lets the third party library Natty interprets date and time 
	 * which the inbuilt validator does not recognizes
	 * Parameters: List<DateGroup>
	 * Return:		String - if natty recognizes what it represents 
	 * 				else it returns an empty string
	 * **/
	private static String interpretInputNatty(List<DateGroup> groups) {
		List dates = null;
		for(DateGroup group: groups){
			dates = group.getDates();
			return dates.toString();
		}

		return "";

	}

	//@author A0111884E
	/**This method converts unix epoch into a String of Date and Time
	 * Parameters: Long - milliseconds
	 * Return:		String - date in the format dd/MM/yyyy
	 * **/
	public static String convertMillisecondToDate(long ms){
		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ms);	 
		return date.split(" ",Constants.SPLIT_INTO_TWO)[Constants.INDEX_ZERO];

	}//end /convertMillisecondToDate

	/**This method converts unix epoch into Time
	 * Parameters: Long - in milliseconds
	 * Return:		String - time in the format HH:mm
	 * **/
	public static String convertMillisecondToTime(long ms){
		//get time in HH:mm
		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ms);	 
		return date.split(" ",Constants.SPLIT_INTO_TWO)[Constants.INDEX_ONE];

	}

	/**This method converts Date and Time into unix epoch
	 * Parameters: String - contains date and time
	 * Return:		Long - time in milliseconds
	 * **/
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

	//@author A0125474E
	/**This method converts a string into Int
	 * Parameters: String - input
	 * Return:		Integer - converted string representation in int
	 * **/
	public static int stringToInt(String input){
		return Integer.parseInt(input.trim());
	}

	//@author A0111884E
	/**This method removes leading and trailing white spaces in an array
	 * Parameters: String array - contains parameters with whitespaces
	 * Return:		String array = parameters without whitespaces
	 * **/
	private static String[] trimWhiteSpace(String[] temp){
		for (int i = 0; i < temp.length; i++) {
			temp[i]=temp[i].trim();
		}
		return temp;
	}
	
	/**This method recognize dots and convert data to slash format
	 * removes leading zeros from date etc 000005/00011/2015
	 * Parameters: String - in the format dd/MM/yyyy or dd.MM.yyyy
	 * Return:		String - in the format dd/MM/yyyy
	 * **/
	private static String flexibleDateFormat(String myDate){
		StringBuffer sb=new StringBuffer();
		ArrayList<String> aL=new ArrayList<String>();
		aL.add(".");
		aL.add("/");
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		for(String pattern : aL){
			if(StringUtils.countMatches(myDate, pattern) == Constants.LENGTH_TWO ){
				String[] temp = null;
				if(pattern.equals(".")){
					temp = myDate.split("\\.");
				}else{
					temp = myDate.split("/");
				}
				for (int i = 0; i < temp.length-1; i++) {
					int extractedValue = Integer.parseInt(temp[i]);
					//to fix d/m/yyyy to dd/MM/yyyy
					if(extractedValue < 10 && extractedValue > 0){
						sb.append("0");
						sb.append(Integer.valueOf(temp[i])+"/");
					}else if(extractedValue >= 10 && extractedValue <=31){
						sb.append(Integer.valueOf(extractedValue)+"/");
					}else{
						assert extractedValue > 32;
					}
				}
				sb.append(temp[temp.length-1]);;
				return sb.toString();
			}
		}
		return myDate;
	}
}
