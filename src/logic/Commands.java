package logic;

public class Commands {
	
	enum Available_COMMAND {
		ADD,DELETE,EDIT,LIST,INVALID;
	}
	
	public static Available_COMMAND determineCommand(String userInputCommand) {
		if (userInputCommand.equalsIgnoreCase(("add"))) {
			return Available_COMMAND.ADD;
		} else if (userInputCommand.equalsIgnoreCase("delete")) {
			return Available_COMMAND.DELETE;
		} else if (userInputCommand.equalsIgnoreCase("edit")) {
			return Available_COMMAND.EDIT;
		} 
		else if (userInputCommand.equalsIgnoreCase("list")) {
			return Available_COMMAND.LIST;
		}
		else
			return Available_COMMAND.INVALID;
	}
}
