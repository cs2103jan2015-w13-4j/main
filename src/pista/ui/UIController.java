package pista.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.controlsfx.control.PopOver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pista.Constants;
import pista.CustomPreferences;
import pista.log.CustomLogging;
import pista.logic.Logic;
import pista.logic.Task;
import pista.parser.MainParser;
import pista.storage.Storage;

//Contains all objects found in MainUI
public class UIController {

	private static CustomLogging mLog = null;
	private CustomPreferences mPrefs = null;
	private Storage mStorage;
	private PopOver mPopOverSetting = null;
	
	public String userInput = null;
	private static String searchKeyword = null;

	private static final String CSS_CLASS_IMAGE_BACKGROUND = "image-background";
	private static final String CSS_CLASS_TEXT_BACKGROUND  = "text-background";
	private static final String CSS_CLASS_TRANSPARENT_BACKGROUND = "transparent-background";
	private static final String CSS_CLASS_LIST_VIEW = "list-view-style";
	private static final String CSS_CLASS_TEXT_BOX = "text-box-style";
	private static final String CSS_CLASS_BUTTON = "button-style";
	private static final String CSS_CLASS_TEXT_STATUS = "text-status-style";

	private static final String CSS_CLASS_BUTTON_IMAGE = "button-image-style";
	private static final String CSS_CLASS_BUTTON_SETTING = "button-setting-style";
	
	private final String CSS_CLASS_POP_OVER_CONTENT_AREA = "pop-content-area";
    private final String CSS_CLASS_POP_OVER_TITLE= "pop-label-title";
    private final String CSS_CLASS_POP_OVER_BUTTON = "pop-btn";
    private final String CSS_CLASS_POP_OVER_ERROR_MESSAGE = "pop-label-error-message";
    private final String CSS_CLASS_POP_OVER_CORRECT_MESSAGE = "pop-label-correct-message";
    private final String POP_OVER_INVALID_SETTING_MESSAGE = "Invalid Date";
    private final String POP_OVER_SUCCESS_SETTING_MESSAGE = "Updated";
	//Objects
	@FXML
	private AnchorPane anchorPaneMain;

	@FXML
    private AnchorPane anchorPaneButtonAreaTop;
	 
	@FXML
	private HBox hBoxInputArea;

	@FXML
	private Text txtStatus;

	@FXML
	private Button btnEnter;

	@FXML
	public TextField txtBoxCommand;

	@FXML
	private VBox vBoxMainArea;

	@FXML
	private ListView<Task> listview_task_fx_id;

	private Button btnSetting;
	//private Label lblPopOverMessage = new Label();
	
	@FXML
	void onHelp(ActionEvent event) {
		showHelp();
	}

	@FXML
	void onRefresh(ActionEvent event) {
		this.initialize();
	}

	public boolean initStorage(){
		try{
			mStorage = Storage.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	public boolean initLogging(){
		try{
			mLog = CustomLogging.getInstance(Storage.class.getName());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	private boolean initPreferences(){
		try{
			mPrefs = CustomPreferences.getInstance();
			mPrefs.initPreference(Constants.PREFERENCE_URL_PATH);

			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private String getPreferenceFilePath(){
		try{
			String filePath = "";
			filePath = mPrefs.getPreferenceFileLocation();
			return filePath;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	public void setTextStatus(String msg){
		if(txtStatus != null){
			txtStatus.setText(msg);
		}
	}

	public void add_outline() {
		txtBoxCommand.setText(Constants.ADD_COMMAND);
	}

	public void edit_outline() {
		txtBoxCommand.setText(Constants.EDIT_COMMAND);
	}

	public void delete_outline() {
		txtBoxCommand.setText(Constants.DELETE_COMMAND);
	}

	private void initButtonSetting(){
		if(this.btnSetting == null){
			this.btnSetting = new Button();
		}
		
		this.btnSetting.getStyleClass().addAll(CSS_CLASS_BUTTON_IMAGE, CSS_CLASS_BUTTON_SETTING);
		this.btnSetting.setPrefSize(25.0, 25.0);
		this.btnSetting.setMaxSize(25.0, 25.0);
		this.btnSetting.setMinSize(25.0, 25.0);
		this.btnSetting.addEventFilter(ActionEvent.ACTION, onBtnSettingClick); //set click method listener
	}
	
	private void addControlsToAnchorPaneAreaTop(){
		this.anchorPaneButtonAreaTop.getChildren().add(this.btnSetting);
		AnchorPane.setRightAnchor(this.btnSetting, 10.0);
	}
	
	@FXML
	public void enter() throws IOException {
		//user click mouse on the enter button
		String[] tokens = null;
		String parserOutput = "";
		String logicOutput = "";
		String command = "";

		userInput = txtBoxCommand.getText();

		mLog.logInfo(Constants.LOG_UI_RUN_ON_ENTER + userInput);
		MainParser mp = MainParser.validateInput(userInput);
		parserOutput = mp.getMessage();
		if(!parserOutput.equals(Constants.MESSAGE_VALID_INPUT)){
			//display error
			txtStatus.setText(parserOutput);
			mLog.logInfo(Constants.LOG_UI_FAIL_VALIDATE_INPUT + parserOutput);
			return; //exit method
		}

		mLog.logInfo(Constants.LOG_UI_SUCCESS_VALIDATE_INPUT + parserOutput);

		command = mp.getCommand();

		if(command.equalsIgnoreCase(Constants.VALUE_HELP)){
			showHelp();
			txtStatus.setText(Constants.LOGIC_SUCCESS_HELP);
			return;
		}

		tokens = mp.getTokens();
		
		if(command.equalsIgnoreCase(Constants.VALUE_SEARCH)){
			setSearchKeyword(tokens);
			initTaskListInListView();
			resetSearchKeyword();
			txtStatus.setText(Constants.LOGIC_SUCCESS_SEARCH + searchKeyword);
			return;
		}
		
		logicOutput = Logic.runCommand(command, tokens);

		txtStatus.setText(logicOutput);

		initTaskListInListView();
		
		Logic.storeToHistory(userInput);
	}

	@FXML
	public void initialize() {
		String logicOutput = "";

		this.clearContent();
		this.initStorage();
		this.initPreferences(); //initialize preferences
		this.initLogging(); //initialize logging

		this.initButtonSetting();
		this.addControlsToAnchorPaneAreaTop();
		
		anchorPaneMain.getStyleClass().addAll(CSS_CLASS_TRANSPARENT_BACKGROUND);
		txtStatus.getStyleClass().addAll(CSS_CLASS_TEXT_BACKGROUND, CSS_CLASS_TEXT_STATUS);
		txtBoxCommand.getStyleClass().addAll(CSS_CLASS_TEXT_BOX);
		btnEnter.getStyleClass().addAll(CSS_CLASS_BUTTON);

		mStorage.setDataFolderLocation(getPreferenceFilePath());
		mStorage.initLogging(); //initialize Storage logging
		Logic.initLogging(); //initialize Logic logging
		Logic.initStorage();
		Logic.initPreference();

		logicOutput = Logic.load();
		txtStatus.setText(logicOutput);

		final KeyCombination keyCombi = new KeyCodeCombination(KeyCode.SPACE,KeyCombination.CONTROL_DOWN);
		txtBoxCommand.addEventHandler(KeyEvent.KEY_RELEASED,new EventHandler<KeyEvent>(){
			@Override
				public void handle(KeyEvent event){
				if (keyCombi.match(event)){
					onCtrlSpacePressed();
				}
			}
		});
		
		if(logicOutput.equals(Constants.LOGIC_SUCCESS_LOAD_XML)){
			initTaskListInListView();
		}

	}//end initialize

	public boolean initTaskListInListView(){
		listview_task_fx_id.getStyleClass().addAll(CSS_CLASS_LIST_VIEW); //add css class
		listview_task_fx_id.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>(){ //populate every task into a custom cell
			@Override
			public ListCell<Task> call(ListView<Task> param) {
				final TaskListCell mCell = new TaskListCell();
				mCell.setUIParent(UIController.this);
				return mCell;
			}//end call
		});

		//Selected item
		//currently not in use
		listview_task_fx_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
			@Override      
			public void changed(ObservableValue<? extends Task> ov,
					Task oldTask, Task newTask) {
			}
		});

		try{
			ArrayList<Task> storageList = Logic.getStorageList();
			if (searchKeyword != null) {
				ArrayList<Task> displayList = new ArrayList<Task>();
				
				for (Task task: storageList) {
					if (hasKeyword(task, searchKeyword)) {
						displayList.add(task);
					}
				}
				
				storageList = displayList;
			}
			
			ObservableList<Task> myObservableList = FXCollections.observableList(storageList);
			listview_task_fx_id.setItems(null); 
			listview_task_fx_id.setItems(myObservableList);

			return true;
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			return false;
		}//end try

	}//end initTaskListInListView

	public boolean showHelp(){
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

		return true;
	}

	public void onCtrlSpacePressed(){
		userInput = txtBoxCommand.getText();
		String[] temp = userInput.split(" ",2);
		String command = temp[0];
		try { 
			int id = Integer.parseInt(temp[1]);
			if( command.equalsIgnoreCase("edit")){
				String processedString = Logic.processTaskInfo(id);
				String finalStr = processedString;
				txtBoxCommand.appendText(finalStr);
//				System.out.println(processedString);
//				System.out.println(finalStr);
			}
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void clearContent(){
		txtStatus.setText("");
	}
	
	private void setSearchKeyword(String[] tokens) {
		searchKeyword = getKeyword(tokens);
	}
	
	private void resetSearchKeyword() {
		searchKeyword = null;
	}
	
	private String getKeyword(String[] tokens) {
		return tokens[0];
	}
	
	private boolean hasKeyword(Task task, String keyword) {
		return task.getTitle().contains(keyword);
	}
	
	//============================== POPOVER Setting ======================================
	private EventHandler onBtnSettingClick = new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			showPopOverSetting();
		}
	};
	
	
	private void showPopOverSetting(){
		if(this.mPopOverSetting != null){
			if(this.mPopOverSetting.isShowing()){
				this.mPopOverSetting.setAutoHide(true);
				return;
			}
		}
		this.mPopOverSetting = new PopOver();
		this.initPopOverSetting();
		this.mPopOverSetting.show(this.btnSetting); 
		
	}
	
	private void initPopOverSetting(){
		final double popWidth = 500.0;
		final double popHeight = 400.0;
		
		VBox vBox = new VBox(8);
		HBox hBox = new HBox(4);
		Label lblSettingTitle = new Label("Setting");
		Label lblCurrentDirectory = new Label("Current Directory");
		final Label lblMessage = new Label();
		TextField txtBoxCurrentDirectory = new TextField();
		Button btnBrowse = new Button("Browse");
		Button btnSave = new Button("Save All Settings");
		
		//Textbox directory
		txtBoxCurrentDirectory.setPrefWidth(380.0);
		
		//Button browse
		btnBrowse.getStyleClass().addAll(CSS_CLASS_POP_OVER_BUTTON);
		btnBrowse.setPrefWidth(100.0);
		btnBrowse.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event) {
				//browse for file
				/*
				setPopOverLabelMessageStyle(lblMessage, popWidth);
				setPopOverLabelMessageText(lblMessage, POP_OVER_SUCCESS_SETTING_MESSAGE);
				setPopOverLabelMessageVisible(lblMessage,true,true);
				*/
			}
		});
		
		
		//Button save
		btnSave.getStyleClass().addAll(CSS_CLASS_POP_OVER_BUTTON);
		btnSave.setPrefWidth(popWidth);
		btnSave.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event) {
				//do save setting
				
				//Label message
				setPopOverLabelMessageStyle(lblMessage, popWidth);
				setPopOverLabelMessageText(lblMessage, POP_OVER_INVALID_SETTING_MESSAGE);
				setPopOverLabelMessageVisible(lblMessage,false,true);
			}
		});
		
		
		//label setting title
		lblSettingTitle.getStyleClass().addAll(CSS_CLASS_POP_OVER_TITLE);
		lblSettingTitle.setPrefWidth(popWidth);
		lblSettingTitle.setTextAlignment(TextAlignment.CENTER);
		lblSettingTitle.setAlignment(Pos.CENTER);
		
		
		
		vBox.setPadding(new Insets(5,10,5,10)); //set Padding
		vBox.getChildren().add(lblSettingTitle);
		vBox.getChildren().add(lblCurrentDirectory);
		
		hBox = new HBox(4);
		HBox.setMargin(txtBoxCurrentDirectory, new Insets(2,0,0,0));
		hBox.getChildren().add(txtBoxCurrentDirectory); //Text box current directory
		hBox.getChildren().add(btnBrowse); //Button browse
		
		vBox.getChildren().add(hBox); //each horizontal boxes
		vBox.getChildren().add(btnSave); //Button save
		vBox.getChildren().add(lblMessage); //Label message (red or green)
		
		vBox.setPrefSize(popWidth, popHeight);
		vBox.getStyleClass().addAll(CSS_CLASS_POP_OVER_CONTENT_AREA); //set style for the vbox
		
		
		this.mPopOverSetting = new PopOver(vBox);
		this.mPopOverSetting.setHideOnEscape(true);
		this.mPopOverSetting.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
		this.mPopOverSetting.setAutoFix(true);
		this.mPopOverSetting.setAutoHide(true);
		this.mPopOverSetting.setDetachable(false);
	
		
	}
	
	private void setPopOverLabelMessageVisible(Label lbl, boolean isValid, boolean isVisible){
		lbl.getStyleClass().removeAll(CSS_CLASS_POP_OVER_CORRECT_MESSAGE, CSS_CLASS_POP_OVER_ERROR_MESSAGE);
		if(isValid){ //valid, green background
			lbl.getStyleClass().addAll(CSS_CLASS_POP_OVER_CORRECT_MESSAGE);	
		}else{//invalid, red background
			lbl.getStyleClass().addAll(CSS_CLASS_POP_OVER_ERROR_MESSAGE);
		}
		
		lbl.setVisible(isVisible);
	}
	
	private void setPopOverLabelMessageText(Label lbl, String msg){
		lbl.setText(msg);
	}
	
	private void setPopOverLabelMessageStyle(Label lbl, double width){
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setAlignment(Pos.CENTER);
		lbl.setPrefWidth(width);
		lbl.setPrefHeight(30.0);
		setPopOverLabelMessageVisible(lbl, false, false);
	}
	
}
