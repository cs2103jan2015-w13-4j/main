package logic.view;

import java.io.IOException;
import java.util.ArrayList;

import logic.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import logic.Constants;
import logic.Parser;
import logic.Task;
import logic.storage.Storage;

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
    
    //@FXML
    //private TextArea displayTextArea;

    @FXML
    private Button enterButton;
    
    @FXML
    private AnchorPane anchorRightPane_fx_id;
    
    @FXML
    private ListView<Task> listview_task_fx_id;
    
    private ArrayList<Task> taskList;
    
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
    	//displayTextArea.appendText(output + "\n");
    }
      
    @FXML
    public void initialize() {
    	
    	prepareList();
    	
    	ObservableList<Task> myObservableList = FXCollections.observableList(this.taskList);
    	listview_task_fx_id.setItems(myObservableList);
    	
    	listview_task_fx_id.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>(){
    		@Override
    		public ListCell<Task> call(ListView<Task> param) {
    			return new TaskListCell();
    		}
    	});

	}
    
    private void prepareList(){
    	this.taskList = Storage.XmltoTable(Constants.XML_FILE_PATH);
    }
    
    
}

