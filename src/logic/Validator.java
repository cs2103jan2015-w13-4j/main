package logic;
import java.util.StringTokenizer;


public class Validator {
	
	public static String validLogic(StringTokenizer st) {
		Commands.Available_COMMAND userCommand = Commands.determineCommand(st.nextToken());
		if(TokenValidation.isTokenValid(st)){
//			Logic.executeCommands(userCommand, st);
		}
	
		return String.format(Message.MSG_INPUT_FAILED);		
	}
}
