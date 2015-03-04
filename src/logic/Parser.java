package logic;

import java.io.IOException;

public class Parser {
	private static String[] tokens = null;
	//separates command and parameters, passes them through the logic, returning the
	//output
	public static String inputHandler(String userInput) throws IOException {
		String cmd = getCmd(userInput);
		String parameters = getParameters(userInput);
		String output = null;
		
		switch(cmd) {
		case "add":
			output = Functions.add(parameters);
			break;
		case "edit":
			output = Functions.edit(parameters);
			break;
		case "delete":
			output = Functions.delete(parameters);
			break;
		case "list":
			output = Functions.list(parameters);
			break;
		default:
			output = Constants.WRONG_CMD_MESSAGE;
		}
		
		return output;
	}
	
	public static String getCmd(String userInput) {
		tokens = userInput.split("\\s", 2);
		return tokens[0];
	}
	
	public static String getParameters(String userInput) {
		if (tokens.length == 2) {
			return userInput.split("\\s", 2)[1];
		} else {
			return Constants.NO_PARA_MESSAGE;
		}
	}
}
