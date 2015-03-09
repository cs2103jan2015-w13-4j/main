package logic.view;

import java.io.IOException;
import java.util.ArrayList;

import logic.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.Constants;
import logic.Logic;
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
    
    @FXML
    private Text txtStatus;
    
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
    	//user click mouse on the enter button
    	String parserOutput = "";
    	String logicOutput = "";
    	
    	String[] parserOutputToken = null;
    	String parserTokenParameter = "";
    	
    	userInput = cmdTextField.getText();
    	
    	//1. Parser - check the incoming command string first
    	parserOutput = Parser.validateString(userInput);
    	if(!parserOutput.equals(Constants.CORRECT_INPUT_MESSAGE)){ //wrong commands and parameter
    		//print out the error message
    		txtStatus.setText(parserOutput);
    		return;
    	}
    	
    	parserOutputToken = Parser.getToken();
    	parserTokenParameter = parserOutputToken[1]; //index 1 is parameter
    	
    	//2. Logic - check individual parameters
    	logicOutput = Logic.validateString(parserTokenParameter);
    	if(!logicOutput.equals(Constants.LOGIC_VALID_PARAMETER_MESSAGE)){ //wrong parameter
    		//print out the error message
    		txtStatus.setText(logicOutput);
    		return;
    	}
    	
    	//3. Logic - add new task based on parameter
    	logicOutput = Logic.add(parserTokenParameter);
    	if(!logicOutput.equals(Constants.LOGIC_SUCCESS_ADD_TASK)){ //fail to add
    		//print out the error message
    		txtStatus.setText(logicOutput);
    		return;
    	}
    	
    	//4. print the final output string..success..

    	txtStatus.setText(logicOutput);

    	ObservableList<Task> myObservableList = FXCollections.observableList(Logic.taskList);
    	listview_task_fx_id.setItems(null); 
    	listview_task_fx_id.setItems(myObservableList);
    	
    }
      
    @FXML
    public void initialize() {
    	clearContent();
    	prepareList();
    	
    	ObservableList<Task> myObservableList = FXCollections.observableList(Logic.initTaskFromXML());
    	listview_task_fx_id.setItems(myObservableList);
    	
    	listview_task_fx_id.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>(){
    		
    		
    		@Override
    		public ListCell<Task> call(ListView<Task> param) {
    			//final Tooltip tooltip = new Tooltip();
    			final TaskListCell mCell = new TaskListCell();
    			return mCell;
    		}//end call
    	});
    	
    	
    	//Selected item
    	listview_task_fx_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
			@Override      
			public void changed(ObservableValue<? extends Task> ov,
	          Task oldTask, Task newTask) {
				System.out.println("Selected : " + newTask.getID() + " - " + newTask.getTitle());
			}
        });

    	
    

	}
    
    /*
    public static <T> void triggerUpdate(ListView<T> listView, T newValue, int i) {
        EventType<? extends ListView.EditEvent<T>> type = ListView.editCommitEvent();
        Event event = new ListView.EditEvent<>(listView, type, newValue, i);
        listView.fireEvent(event);
    }*/
    
    
    public ListView getListView(){
    	return listview_task_fx_id;
    }
    
    private void clearContent(){
    	txtStatus.setText("");
    }
    
    private void prepareList(){
    	this.taskList = Storage.XmltoTable(Constants.XML_FILE_PATH);
    }
    
    
}

