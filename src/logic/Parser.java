package logic;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Parser {
	
	/*
	 * tokens
	 * [0] - command type
	 * [1] - parameter in string 
	 * */
	private static String[] tokens = null;
	//separates command and parameters, passes them through the logic, returning the
	//output
	/*
	public static String inputHandler(String userInput) throws IOException {
		String cmd = getCmd(userInput);
		String parameters = getParameters(userInput);
		String output = null;
		
		switch(cmd) {
		case "add":
			output = Logic.add(parameters);
			break;
		case "edit":
			output = Logic.edit(parameters);
			break;
		case "delete":
			output = Logic.delete(parameters);
			break;
		case "list":
			output = Logic.list(parameters);
			break;
		default:
			output = Constants.WRONG_CMD_MESSAGE;
		}
		
		
		return output;
	}
	*/
	
	public static String validateString(String userInput) {
		boolean isValidCommand = false;
		boolean isValidParameter = false;
		
		isValidCommand = checkCommand(userInput);
		if(!isValidCommand){
			return Constants.WRONG_CMD_MESSAGE;
		}
		
		isValidParameter = checkParameter(userInput);
		if(!isValidParameter){
			return Constants.WRONG_PARAMETER_MESSAGE;
		}
		
		return Constants.CORRECT_INPUT_MESSAGE;
		
	}//end inputHandler
	
	public static String[] getToken(){
		return tokens;
	}//end getToken
	
	/*
	 * Grabs user command and verify if matches the supported commands
	 * Add, edit, delete*/
	private static boolean checkCommand(String input){
		tokens = input.split("\\s", 2);
		String cmd = "";
		
		if(tokens.length <= 1){
			return false;
			
		}else{
			cmd = input.split("\\s", 2)[0].trim().toLowerCase(); //ensure system accept upper and lower case
			if(cmd.equals(Constants.ADD_COMMAND_VALUE) || cmd.equals(Constants.EDIT_COMMAND_VALUE) || 
				cmd.equals(Constants.DELETE_COMMAND_VALUE)){ //check for command type		
				return true;
				
			}else{
				return false;
			}//end if
		}//end if 

	}//end checkCommand
	
	/*
	 * check parameters
	 * true=parameter exists
	 * false=no parameters
	 */
	private static boolean checkParameter(String input){
		String parameter = "";
		
		if(tokens.length < 1){
			return false;
			
		}else{
			parameter = input.split("\\s", 2)[0].trim().toLowerCase(); //ensure parameter no leading or ending spaces
			if(!parameter.equals("")){ //parameter is not empty	
				return true;
			}else{
				return false;
			}//end if
		}//end if 
	}//end checkParameter
	
	
	private static String getCmd(String userInput) {
		tokens = userInput.split("\\s", 2);
		return tokens[0];
	}
	
	private static String getParameters(String userInput) {
		if (tokens.length == 2) {
			return userInput.split("\\s", 2)[1];
		} else {
			return Constants.WRONG_PARAMETER_MESSAGE;
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
	
	private static String addZero(String i){
		if(i.length() == 1){
			return "0"+i;
		}
		return i;
	}
}
