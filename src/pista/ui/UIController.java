package pista.ui;

import java.io.IOException;

import pista.Constants;
import pista.log.Logging;
import pista.logic.Logic;
import pista.logic.Task;
import pista.parser.Parser;
import pista.storage.Storage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

//Contains all objects found in MainUI
public class UIController {
	//private static Logging mLog = new Logging(Logic.class.getName(), Constants.LOG_FILE_NAME);
	public String userInput = null;

	//Objects
	@FXML
	private Button btnHelp;

	@FXML
	private Button btnRefresh;
	
	@FXML
	private Button btnAdd;
	
	@FXML
	private Button btnEdit;

	@FXML
	private Button btnDelete;

	@FXML
	public TextField cmdTextField;

	@FXML
	private Text txtStatus;

	@FXML
	private Button enterButton;

	@FXML
	private AnchorPane anchorRightPane_fx_id;

	@FXML
	private ListView<Task> listview_task_fx_id;

	
	@FXML
	void onHelp(ActionEvent event) {
		Stage stage = new Stage();

		stage.setTitle("Help");
		stage.initModality(Modality.NONE);
		stage.initStyle(StageStyle.DECORATED);
		stage.setResizable(false);
		//stage.setMaxHeight(800);
		//stage.setMaxWidth(600);
		//stage.setMinHeight(600);
		//stage.setMinWidth(400);

		Scene scene = new Scene(new Browser(), 500,700, Color.web("#666970"));
		stage.setScene(scene);    
		stage.show();
	}

	@FXML
	void onRefresh(ActionEvent event) {
		this.initialize();
		//ObservableList<Task> myObservableList = FXCollections.observableList(Logic.taskList);
		//listview_task_fx_id.setItems(null); 
		//listview_task_fx_id.setItems(myObservableList);
	}

	public void add_outline() {
		cmdTextField.setText(Constants.ADD_COMMAND);
	}
	
	public void edit_outline() {
		cmdTextField.setText(Constants.EDIT_COMMAND);
	}
	
	public void delete_outline() {
		cmdTextField.setText(Constants.DELETE_COMMAND);
	}
	
	public void enter() throws IOException {
		//user click mouse on the enter button
		String[] tokens = null;
		String parserOutput = "";
		String logicOutput = "";
		String command = "";
		
		userInput = cmdTextField.getText();
		//mLog.logInfo(Constants.LOG_UI_RUN_ON_ENTER + userInput);
		parserOutput = Parser.validateInput(userInput);
		
		if(!parserOutput.equals(Constants.MESSAGE_VALID_INPUT)){
			//display error
			txtStatus.setText(parserOutput);
			//mLog.logInfo(Constants.LOG_UI_FAIL_VALIDATE_INPUT + parserOutput);
			return; //exit method
		}
		//mLog.logInfo(Constants.LOG_UI_SUCCESS_VALIDATE_INPUT + parserOutput);
		
		command = Parser.getCommand(userInput);
		tokens = Parser.getTokens(userInput);
		
		logicOutput = Logic.runCommand(command, tokens);

		txtStatus.setText(logicOutput);

		showTaskListInListView();
	}

	@FXML
	public void initialize() {
		String logicOutput = "";
		
		clearContent();
		Storage.init();
		
		logicOutput = Logic.load();
		txtStatus.setText(logicOutput);
		
		if(logicOutput.equals(Constants.LOGIC_SUCCESS_LOAD_XML)){
			showTaskListInListView();
		}
		

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
				if(newTask != null){
					System.out.println("Selected : " + newTask.getID() + " - " + 
				newTask.getTitle()+ " - "+newTask.getStartDate()+ " - "+
							newTask.getStartTime()+ " - "+newTask.getEndDate()+ " - "+
				newTask.getEndTime()+ " - "+newTask.getCategory());

				}
			}
		});

	}//end initialize


	public boolean showTaskListInListView(){
		try{
			ObservableList<Task> myObservableList = FXCollections.observableList(Logic.getStorageList());
			listview_task_fx_id.setItems(null); 
			listview_task_fx_id.setItems(myObservableList);
			
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	

	private void clearContent(){
		txtStatus.setText("");
	}


}
