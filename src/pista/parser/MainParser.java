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
	public boolean getValidTokens(){return validTokens;}
	private void setCommand(String command){this.command = command;}
	private void setTokens(String[] tokens){this.tokens = tokens;}
	private void setMessage(String message){this.message = message;}
	private void setValidToken(boolean status){this.validTokens = status;}
	private void setIndexInToken(int index, String input){this.tokens[index] = input;}	
	/*
	 * tokens
	 * [0] - command type
	 * [1] - parameter in string 
	 * */

	public static MainParser validateInput(String input) {
		MainParser mp = new MainParser();
		if (isEmptyString(input)) {
			mp.setMessage(Constants.MESSAGE_EMPTY_STRING);
			return mp;
		} else {
			String command = getCommand(input);
			if (!isCommandValid(command)) {
				mp.setMessage(Constants.MESSAGE_WRONG_COMMAND);
				return mp;
			} else {
				if (command.equalsIgnoreCase(Constants.VALUE_HELP)) {
					mp.setCommand(command);
					mp.setMessage(Constants.MESSAGE_VALID_INPUT);
					return mp;
				} else {
					String[] tokens = getTokens(input);

					if (!mp.isTokensValid(mp, command, tokens)) {
						//mp.setMessage(Constants.MESSAGE_WRONG_PARAMETERS);
						return mp;
					} else {
						mp.setCommand(command);
						mp.setMessage(Constants.MESSAGE_VALID_INPUT);
						return mp;
					}
				}
			}
		}
	}

	public static boolean isEmptyString(String input){
		if(input.isEmpty()){
			return true;
		}
		return false;
	}
	
	public static String getCommand(String input){
		String command=input.split(" ",2)[0];
		return command;
	}
	
	/*
	 * Grabs user command and verify if matches the supported commands
	 * Add, edit, delete*/
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

	public static String[] getTokens(String input){
		String[] temp = input.split(" ",2);

		if(temp.length > 1){
			String[] arr = temp[1].split("-");
			trimWhiteSpace(arr);
			return arr;
		}else{
			return null;
		}
	}

	/*
	 * check parameters
	 * true=parameter exists
	 * false=no parameters
	 */
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
	
	private boolean checkAddTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length == Constants.TOKEN_NUM_ADD_ONE){
			if(TokenValidation.isTitleValid((tokens[Constants.ADD_TOKEN_TITLE]))){
				return true;
			}
			mp.setMessage(Constants.MESSAGE_EMPTY_TITLE);
			return false;
		}


		else if(tokens.length == Constants.TOKEN_NUM_ADD_THREE){
			if(TokenValidation.isTitleValid(tokens[Constants.ADD_TOKEN_TITLE])){
				if(!isDateAndTimeValid(mp, tokens, Constants.ADD_TOKEN_DEADLINE_ENDDATE, 
						Constants.ADD_TOKEN_DEADLINE_ENDTIME)){
					//mp.setValidToken(false);
					return false;
				}
				//mp.setValidToken(true);
				return true;
			}
			//mp.setValidToken(false);
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
						//mp.setValidToken(true);
						return true;
					}else{
						mp.setMessage(Constants.MESSAGE_STARTDATE_GREATER_THAN_ENDDATE);
						return false;
					}
				}
				return false;
			}
			return false;
		}

		assert false:"Tokens number in add function are "+tokens.length +" allowed length are 1,3,5";
		//mp.setValidToken(false);
		return false;
	}
	
	private boolean checkEditTokens(MainParser mp, String[] tokens) {
		int clearValue = 0;
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		clearValue = getClearValue(tokens, clearValue);
		if(tokens.length==Constants.TOKEN_NUM_EDIT_TWO){
			if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE])){	//edit id -title 
				//mp.setValidToken(true);
				return true;
			}
		}

		else if(tokens.length==Constants.TOKEN_NUM_EDIT_FOUR){
			if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE])) {
				if(!isDateAndTimeValid(mp, tokens, Constants.EDIT_TOKEN_DEADLINE_ENDDATE, 
						Constants.EDIT_TOKEN_DEADLINE_ENDTIME)){
					return false;
				}
				if(clearValue != 0 && clearValue != 2){
					mp.setMessage(Constants.MESSAGE_EDIT_DEADLINE_INVALID_CLEAR_COUNT);
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
				if(clearValue == 4){
					return true;
				}else if (clearValue == 2){
					if(!Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_ENDDATE)) 
							&& !Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_ENDTIME))){
						mp.setMessage(Constants.MESSAGE_EDIT_Timed_INVALID_CLEAR_COUNT_TIMED_TO_DEADLINE);
						return false;
					}else{
						return true;
					}
				}else if (clearValue == 0){
					if(startDateTimeValid && endDateTimeValid){
						if(TokenValidation.isStartDateBeforeThanEndDate(mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_STARTDATE),
								mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_ENDDATE), 
								mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_STARTTIME), 
								mp.getItemInTokenIndex(Constants.EDIT_TOKEN_TIMED_ENDTIME))){						
							return true;
						}else{
							mp.setMessage(Constants.MESSAGE_STARTDATE_GREATER_THAN_ENDDATE);
							return false;
						}
					}
					return false;
				}else{
					mp.setMessage(Constants.MESSAGE_EDIT_Timed_INVALID_CLEAR_COUNT);
					return false;
				}
			}
			return false;
		}
		assert false:"Tokens number in edit function are "+tokens.length +"allowed length is 2,4,6";
		mp.setMessage(Constants.MESSAGE_EDIT_EMPTY_TOKENS);
		return false;
	}
	
	private static boolean checkDeleteTokens(MainParser mp, String[] tokens) {
		mp.setTokens(tokens);
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}

		String input = tokens[0];
		try { 
			int id = Integer.parseInt(input); 
			// if it is a number
			if( id < 1 ){
				mp.setMessage(Constants.MESSAGE_ID_LESS_THAN_ONE);
				return false;
			}
			return true;

		} catch(NumberFormatException e) { //if it is not a number
			if(!input.equalsIgnoreCase("a")){
				mp.setMessage(Constants.MESSAGE_INVALID_SHORTHAND);
				return false;
			}
			return true;
		}
	}
	
	private boolean checkMarkTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		String status = tokens[1];
		if(Constants.MARK_DONE.equalsIgnoreCase(status) || Constants.MARK_UNDONE.equalsIgnoreCase(status)){
			return true;
		}
		mp.setMessage(Constants.INVALID_MARK);
		return false;
	}
	
	private boolean checkReminderTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length == Constants.TOKEN_NUM_REMINDER_TWO){
			if(tokens[Constants.TOKEN_NUM_REMINDER_OFF].equalsIgnoreCase(Constants.REMINDER_OFF)){
				return true;
			}
			mp.setMessage(Constants.REMINDER_INVALID_STATUS);
			return false;
		}else if(tokens.length == Constants.TOKEN_NUM_REMINDER_THREE){
			//String taskId = tokens[TOKEN_REMINDER_ID];
			if(!isDateAndTimeValid(mp, tokens, Constants.REMINDER_DATE, 
					Constants.REMINDER_TIME)){
				return false;
			}
			return true;
		}

		mp.setMessage(Constants.INVALID_REMINDER);
		return false;
	}
	
	private boolean checkPriorityTokens (MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		if(tokens.length == 2){
			int priorityScore = Integer.parseInt(tokens[Constants.TOKEN_NUM_PRIORITY_SCORE]);
			if(priorityScore >=0 || priorityScore<=3){
				return true;
			}
		}
		mp.setMessage(Constants.INVALID_PRIORITY_SCORE);
		return false;
	}
	
	private boolean checkSetTokens(MainParser mp, String[] tokens){
		String getType = "";
		String getFileLocation = "";

		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}

		if(tokens.length <= 1){ //must have at least 2
			mp.setMessage(Constants.LOGIC_INVALID_SET_COMMAND_LENGTH);
			return false;
		}

		getType = tokens[Constants.SET_TYPE_INDEX]; //index of type is 0
		if(!getType.equalsIgnoreCase(Constants.SET_TYPE_FILE_LOCATION)){
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
	
	private boolean checkSearchTokens (MainParser mp, String[] tokens) {
		if(tokens == null || tokens.length != 1){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		
		if (tokens[0].equals("")) {
			mp.setMessage(Constants.MESSAGE_EMPTY_STRING);
			return false;
		}
		
		mp.setTokens(tokens);
		return true;
	}
	
	private boolean checkSortTokens(MainParser mp, String[] tokens) {
		if(tokens == null){
			mp.setMessage(Constants.MESSAGE_INVALID_TOKEN_LENGTH);
			return false;
		}
		mp.setTokens(tokens);
		String sortType = tokens[0];
		if(Constants.SORT_ASCENDING_END_DATE.equalsIgnoreCase(sortType) || 
				Constants.SORT_ASCENDING_START_DATE.equalsIgnoreCase(sortType) ||
				Constants.SORT_ASCENDING_TITLE.equalsIgnoreCase(sortType) ||
				Constants.SORT_DESCENDING_END_DATE.equalsIgnoreCase(sortType)|| 
				Constants.SORT_DESCENDING_START_DATE.equalsIgnoreCase(sortType) || 
				Constants.SORT_DESCENDING_TITLE.equals(sortType) || 
				Constants.SORT_ISDONE_DONE.equals(sortType) || 
				Constants.SORT_ISDONE_UNDONE.equalsIgnoreCase(sortType) || 
				Constants.SORT_OVERVIEW.equalsIgnoreCase(sortType) || 
				Constants.SORT_TYPE.equalsIgnoreCase(sortType) 
				|| Constants.SORT_ASCENDING_PRIORITY.equalsIgnoreCase(sortType) ||
				Constants.SORT_DESCENDING_PRIORITY.equalsIgnoreCase(sortType)){
			return true;
		}
		mp.setMessage(Constants.MESSAGE_INVALID_SORT_FUNCTION);
		return false;
	}
	
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
				interpretOutputDateFromNatty = sdf.format(nattySDF.parse(intrepretInputNatty(groups)));
			} catch (ParseException e1) {
				mp.setMessage(Constants.MESSAGE_INVALD_NATTY_DATE);
				return false;
			}
			if(!TokenValidation.isDateValid(interpretOutputDateFromNatty) && !interpretOutputDateFromNatty.isEmpty()){
				mp.setMessage(Constants.MESSAGE_INVALID_DATE_INPUT);
				return false;
			}else{
				mp.setIndexInToken(dateIndex, interpretOutputDateFromNatty);
			}
		}
		if(!TokenValidation.isTimeValid(tokens[timeIndex])){
			groups = parser.parse(tokens[timeIndex]);
			try {
				interpretOutputTimeFromNatty = sdf2.format(nattySDF.parse(intrepretInputNatty(groups)));
			} catch (ParseException e1) {
				mp.setMessage(Constants.MESSAGE_INVALD_NATTY_TIME);
				return false;
			}
			if(!TokenValidation.isTimeValid(interpretOutputTimeFromNatty) && !interpretOutputTimeFromNatty.isEmpty()){
				mp.setMessage(Constants.MESSAGE_INVALID_TIME_INPUT);
				return false;
			}else{
				mp.setIndexInToken(timeIndex, interpretOutputTimeFromNatty);
			}
		}
		return true;
	}

	private static String nattyInputFormat(String[] tokens, int dateIndex) {
		String requiredNattyInputDateFormat = "";
		String[] temp = tokens[dateIndex].split("/");
		if(StringUtils.countMatches(tokens[dateIndex], "/")==2){
			requiredNattyInputDateFormat = temp[1] +"/"+ temp [0] +"/"+ temp [2];
		}else if (StringUtils.countMatches(tokens[dateIndex], "/")==1){
			requiredNattyInputDateFormat = temp[1] +"/"+ temp [0];
		}
		return requiredNattyInputDateFormat;
	}

	private static String intrepretInputNatty(List<DateGroup> groups) {
		List dates = null;
		for(DateGroup group: groups){
			dates = group.getDates();
			return dates.toString();
		}

		return "";

	}

	public static String convertMillisecondToDate(long ms){
		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ms);	 
		return date.split(" ",2)[0];

	}//end /convertMillisecondToDate

	public static String convertMillisecondToTime(long ms){
		//get time in HH:mm
		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ms);	 
		return date.split(" ",2)[1];

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

	public static int stringToInt(String input){
		return Integer.parseInt(input.trim());
	}

	private static String[] trimWhiteSpace(String[] temp){
		for (int i = 0; i < temp.length; i++) {
			temp[i]=temp[i].trim();
		}
		return temp;
	}
	private static String flexibleDateFormat(String myDate){
		StringBuffer sb=new StringBuffer();
		ArrayList<String> aL=new ArrayList<String>();
		aL.add(".");
		aL.add("/");
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		for(String pattern : aL){
			if(StringUtils.countMatches(myDate, pattern)==2){
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
					}else {
						sb.append(Integer.valueOf(extractedValue)+"/");
					}
				}
				sb.append(temp[temp.length-1]);;
				return sb.toString();
			}
		}
		return myDate;
	}
	private static int getClearValue(String[] tokens, int clearValue) {
		for(String temp : tokens){
			if(Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(temp)){
				clearValue++;
			}
		}
		return clearValue;
	}
}
