package logic;

import java.io.IOException;
import java.util.Calendar;

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
		 //convert xxxxxx to d/m/YYYY
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(ms);
		 int mYear = calendar.get(Calendar.YEAR);
		 int mMonth = calendar.get(Calendar.MONTH);
		 int mDay = calendar.get(Calendar.DAY_OF_MONTH);
		 
		 return mDay + "/" + mMonth + "/" + mYear;
		 
	 }//end /convertMillisecondToDate
	
	public static String convertMillisecondToTime(long ms){
		//get time in HH:mm
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ms);
		int hr = calendar.get(Calendar.HOUR);
		int min = calendar.get(Calendar.MINUTE);
		
		return addZero(String.valueOf(hr)) + ":" + addZero(String.valueOf(min));
	}
	
	
	public static long convertDateToMillisecond(String date, String time){
		//date format dd/mm/yyyy
		//time format HH:mm;	
		String[] dateArray = date.split("/");
		String[] timeArray = time.split(":");
		
		Calendar cal = Calendar.getInstance();
		cal.set(stringToInt(dateArray[2]), stringToInt(dateArray[1]), stringToInt(dateArray[0]), 
				stringToInt(timeArray[0]), stringToInt(timeArray[1]));
		
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
