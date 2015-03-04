package logic.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.Constants;
import logic.Parser;

//Contains all objects found in MainUI
public class UIController {
	
	public String userInput = null;
	
	//Objects
    @FXML
    private Button addButton;

    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button listButton;
    
    @FXML
    public TextField cmdTextField;
    
    @FXML
    private TextArea displayTextArea;

    @FXML
    private Button enterButton;
    
    //Methods
    public void add() {
    	cmdTextField.setText(Constants.ADD_COMMAND);
    }
    
    public void edit() {
    	cmdTextField.setText(Constants.EDIT_COMMAND);
    }
    
    public void delete() {
    	cmdTextField.setText(Constants.DELETE_COMMAND);
    }
    
    public void list() {
    	cmdTextField.setText(Constants.LIST_COMMAND);
    }
    
    public void enter() throws IOException {
    	userInput = cmdTextField.getText();
    	String output = Parser.inputHandler(userInput);
    	displayTextArea.appendText(output + "\n");
    }
}

