package pista.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pista.Constants;

public class Parser {
	
	/*
	 * tokens
	 * [0] - command type
	 * [1] - parameter in string 
	 * */
	
	public static String validateInput(String input) {
		if (isEmptyString(input)) {
			return Constants.MESSAGE_EMPTY_STRING;
		} else {
			String command = getCommand(input);
			if (!isCommandValid(command)) {
				return Constants.MESSAGE_WRONG_COMMAND;
			} else {
				String[] tokens = getTokens(input);
				if (!isTokensValid(command, tokens)) {
					return Constants.MESSAGE_WRONG_PARAMETERS;
				} else {
					return Constants.MESSAGE_VALID_INPUT;
				}
			}
		}
	}
	
	public static String getCommand(String input){
		String command=input.split(" ",2)[0];
		return command;
	}
	
	public static String[] getTokens(String input){
		String[] temp = input.split(" ",2);
		String[] arr = temp[1].split("-");
		trimWhiteSpace(arr);
		return arr;
	}
	
	public static boolean isEmptyString(String input){
		if(input.isEmpty()){
			return true;
		}
		return false;
	}
	
	/*
	 * Grabs user command and verify if matches the supported commands
	 * Add, edit, delete*/
	private static boolean isCommandValid(String command){
		if(command.equalsIgnoreCase(Constants.VALUE_ADD) || command.equalsIgnoreCase(Constants.VALUE_EDIT) || 
				command.equalsIgnoreCase(Constants.VALUE_DELETE)){ //check for command type		
				return true;
			}else{
				return false;
			}//end if

	}//end checkCommand
	
	/*
	 * check parameters
	 * true=parameter exists
	 * false=no parameters
	 */
	private static boolean isTokensValid(String command, String[] tokens){
		switch(command) {
			case Constants.VALUE_ADD:
				return checkAddTokens(tokens);
			case Constants.VALUE_EDIT:
				return checkEditTokens(tokens);
			case Constants.VALUE_DELETE:
				return checkDeleteTokens(tokens);
			default:
				return false;
		}
	}
	
	private static boolean checkAddTokens(String[] tokens) {
		if(tokens.length == Constants.TOKEN_NUM_ADD_ONE){
			return TokenValidation.isTitleValid((tokens[Constants.ADD_TOKEN_TITLE]));
		}
		
		else if(tokens.length == Constants.TOKEN_NUM_ADD_THREE){
			if(TokenValidation.isTitleValid(tokens[Constants.ADD_TOKEN_TITLE]) 
					&& (TokenValidation.isDateValid(tokens[Constants.ADD_TOKEN_DEADLINE_ENDDATE])) 
					&& (TokenValidation.isTimeValid(tokens[Constants.ADD_TOKEN_DEADLINE_ENDTIME]))){
				return true;
			}
		}
		
		else if(tokens.length == Constants.TOKEN_NUM_ADD_FIVE){
			if(TokenValidation.isTitleValid(tokens[Constants.ADD_TOKEN_TITLE]) 
					&& (TokenValidation.isDateValid(tokens[Constants.ADD_TOKEN_TIMED_STARTDATE])) 
					&& (TokenValidation.isTimeValid(tokens[Constants.ADD_TOKEN_TIMED_STARTTIME])) 
					&& (TokenValidation.isDateValid(tokens[Constants.ADD_TOKEN_TIMED_ENDDATE])) 
					&& (TokenValidation.isTimeValid(tokens[Constants.ADD_TOKEN_TIMED_ENDTIME])) 
					&& TokenValidation.isStartDateBeforeThanEndDate(tokens[Constants.ADD_TOKEN_TIMED_STARTDATE]
							, tokens[Constants.ADD_TOKEN_TIMED_ENDDATE]
									, tokens[Constants.ADD_TOKEN_TIMED_STARTTIME]
											, tokens[Constants.ADD_TOKEN_TIMED_ENDTIME])){
				return true;
			}
		}
		return false;
	}
	
	private static boolean checkEditTokens(String[] tokens) {		
			if(tokens.length==Constants.TOKEN_NUM_EDIT_TWO){
				if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE])){	//edit id -title 
					return true;
				}
			}
			
			else if(tokens.length==Constants.TOKEN_NUM_EDIT_FOUR){
				if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE]) 
						&& (TokenValidation.isDateValid(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDDATE])) 
						&& (TokenValidation.isTimeValid(tokens[Constants.EDIT_TOKEN_DEADLINE_ENDTIME]))){
					return true;
				}	
			}
			
			else if(tokens.length==Constants.TOKEN_NUM_EDIT_SIX){
				if(TokenValidation.isTitleValid(tokens[Constants.EDIT_TOKEN_TITLE]) 
						&& (TokenValidation.isDateValid(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE])) 
						&& (TokenValidation.isTimeValid(tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME])) 
						&& (TokenValidation.isDateValid(tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE])) 
						&& (TokenValidation.isTimeValid(tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME])) 
						&& (TokenValidation.isStartDateBeforeThanEndDate(tokens[Constants.EDIT_TOKEN_TIMED_STARTDATE]
								, tokens[Constants.EDIT_TOKEN_TIMED_ENDDATE]
										, tokens[Constants.EDIT_TOKEN_TIMED_STARTTIME]
												, tokens[Constants.EDIT_TOKEN_TIMED_ENDTIME]))){
					return true;
				}
			}
		return false;
	}
	
	private static boolean checkDeleteTokens(String[] tokens) {
		if(tokens.length != 1){
			return false;
		}

		String input = tokens[0];
		try { 
			int id = Integer.parseInt(input); 
			// if it is a number
			if( id < 1 ){
				return false;
			}
			return true;

		} catch(NumberFormatException e) { //if it is not a number
			if(!input.equalsIgnoreCase("a")){
				return false;
			}
			return true;
		}
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
}
