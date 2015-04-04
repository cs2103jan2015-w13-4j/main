package pista.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.controlsfx.control.PopOver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
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
	private PopOver mPopOverAdd = null;
	
	public String userInput = null;
	private static String searchKeyword = null;
	private boolean isValidFilePath;
	
	private static final String CSS_IMAGE_BACKGROUND = "image-background";
	private static final String CSS_TEXT_BACKGROUND  = "text-background";
	private static final String CSS_TRANSPARENT_BACKGROUND = "transparent-background";
	private static final String CSS_LIST_VIEW = "list-view-style";
	private static final String CSS_TEXT_BOX = "text-box-style";
	private static final String CSS_BUTTON = "button-style";
	private static final String CSS_TEXT_STATUS = "text-status-style";

	private static final String CSS_BUTTON_IMAGE = "button-image-style";
	private static final String CSS_BUTTON_SETTING = "button-setting-style";
	private static final String CSS_BUTTON_ADD = "button-add-style";
	private static final String CSS_BUTTON_HELP = "button-help-style";
	private static final String CSS_BUTTON_REFRESH = "button-refresh-style";
	private static final String CSS_BUTTON_REDO = "button-redo-style";		
	private static final String CSS_BUTTON_UNDO = "button-undo-style";
	
	private final String CSS_POP_OVER_TEXTAREA = "pop-over-text-area";
	private final String CSS_POP_OVER_TOOLTIP = "pop-label-time-tip";
	
	
	private final String CSS_POP_OVER_CONTENT_AREA = "pop-content-area";
    private final String CSS_POP_OVER_TITLE= "pop-label-title";
    private final String CSS_POP_OVER_BUTTON = "pop-btn";
    private final String CSS_POP_OVER_ERROR_MESSAGE = "pop-label-error-message";
    private final String CSS_POP_OVER_CORRECT_MESSAGE = "pop-label-correct-message";
    private final String POP_OVER_FAILED_SETTING_MESSAGE = "Setting Failed";
    private final String POP_OVER_INVALID_FILE_MESSAGE = "Invalid File";
    private final String POP_OVER_SUCCESS_SETTING_MESSAGE = "Updated";
    
	private final String STATUS_EMPTY_XML_FILE_PATH_MESSAGE = "Please provide a XML file to keep track of your data";
	private final String STATUS_INVALID_XML_FILE_PATH_MESSAGE  = "Either your file is not empty or invalid format.";
	private final String STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE = "Failed to load selected file.";
	private final String STATUS_FAIL_TO_SAVE_DUE_TO_INVALID_FILE_MESSAGE = "Please ensure your file is valid before saving.";
	private final String STATUS_FAIL_TO_SAVE = "Unable to save setting. Please try again.";
	private final String STATUS_APPLICATION_ERROR_MESSAGE = "Application error. Please contact the administrator";
	private final String STATUS_SUCCESS_FILE_CREATED_MESSAGE = "[new_file_path] is loaded.";
	
	private final String TOOLTIP_HELP = "Help";
	private final String TOOLTIP_SETTING = "Setting";
	private final String TOOLTIP_REFRESH = "Refresh the list";
	private final String TOOLTIP_ADD = "Add New Task";
	private final String TOOLTIP_REDO = "Redo to last action";
	private final String TOOLTIP_UNDO = "Undo to previous action";
	
	private String addTaskCommand = "add [task_title] -[start_date] -[start_time] -[end_date] -[end_time]";
			
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
	private Button btnAddNewTask;
	private Button btnRefresh;
	private Button btnHelp;
	private Button btnRedo;
	private Button btnUndo;
	
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

	private String getPreferenceFilePath(){ //get file path from preference
		try{
			String filePath = "";
			filePath = mPrefs.getPreferenceFileLocation();
			return filePath;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	private boolean setPreferenceFilePath(String newPath){ //set new file path from preference
		try{
			mPrefs.setPreferenceFileLocation(newPath);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
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

	private void initButtonRedo(){
		double size = 40.0;
		
		ImageView img = new ImageView(new Image("images/redo.png"));
		img.setPreserveRatio(true);
		img.setFitHeight(20);
		img.setFitWidth(20);
		
		if(this.btnRedo == null){
			this.btnRedo = new Button("Redo"); //, img
		}
		
		setButtonToolTip(this.btnRedo, this.TOOLTIP_UNDO);
		this.btnRedo.setTextAlignment(TextAlignment.RIGHT);
		this.btnRedo.getStyleClass().addAll(CSS_BUTTON_IMAGE, CSS_BUTTON_REDO);
		this.btnRedo.setPrefWidth(75.0);
		this.btnRedo.setPrefHeight(size);
		this.btnRedo.addEventFilter(ActionEvent.ACTION, onBtnRedoClick); //set click method listener
	}
	
	private void initButtonUndo(){
		
		double size = 40.0;
		
		if(this.btnUndo == null){
			this.btnUndo = new Button("Undo");
		}

		setButtonToolTip(this.btnUndo, this.TOOLTIP_REDO);
		this.btnUndo.setTextAlignment(TextAlignment.RIGHT);
		this.btnUndo.getStyleClass().addAll(CSS_BUTTON_IMAGE, CSS_BUTTON_UNDO);
		this.btnUndo.setPrefWidth(75.0);
		this.btnUndo.setPrefHeight(size);
		this.btnUndo.addEventFilter(ActionEvent.ACTION, onBtnUndoClick); //set click method listener
	}
	
	private void initButtonSetting(){
		double size = 40.0;
		
		if(this.btnSetting == null){
			this.btnSetting = new Button();
		}
		
		setButtonToolTip(this.btnSetting, this.TOOLTIP_SETTING);
		this.btnSetting.getStyleClass().addAll(CSS_BUTTON_IMAGE, CSS_BUTTON_SETTING);
		this.btnSetting.setPrefSize(size, size);
		this.btnSetting.setMaxSize(size, size);
		this.btnSetting.setMinSize(size, size);
		this.btnSetting.addEventFilter(ActionEvent.ACTION, onBtnSettingClick); //set click method listener
	}
	
	private void initButtonAddNewTask(){
		double size = 40.0;
		
		if(this.btnAddNewTask == null){
			this.btnAddNewTask = new Button();
		}
		
		setButtonToolTip(this.btnAddNewTask, this.TOOLTIP_ADD);
		this.btnAddNewTask.getStyleClass().addAll(CSS_BUTTON_IMAGE, CSS_BUTTON_ADD);
		this.btnAddNewTask.setPrefSize(size, size);
		this.btnAddNewTask.setMaxSize(size, size);
		this.btnAddNewTask.setMinSize(size, size);
		this.btnAddNewTask.addEventFilter(ActionEvent.ACTION, onBtnAddNewTaskClick); //set click method listener
	}
	
	private void initButtonHelp(){
		double size = 40.0;
		
		if(this.btnHelp == null){
			this.btnHelp = new Button();
		}
		
		setButtonToolTip(this.btnHelp, this.TOOLTIP_HELP);
		this.btnHelp.getStyleClass().addAll(CSS_BUTTON_IMAGE, CSS_BUTTON_HELP);
		this.btnHelp.setPrefSize(size, size);
		this.btnHelp.setMaxSize(size, size);
		this.btnHelp.setMinSize(size, size);
		this.btnHelp.addEventFilter(ActionEvent.ACTION, onBtnHelpClick); //set click method listener
	}
	
	private void initButtonRefresh(){
		double size = 40.0;
		
		if(this.btnRefresh == null){
			this.btnRefresh = new Button();
		}
		
		setButtonToolTip(this.btnRefresh, this.TOOLTIP_REFRESH);
		this.btnRefresh.getStyleClass().addAll(CSS_BUTTON_IMAGE, CSS_BUTTON_REFRESH);
		this.btnRefresh.setPrefSize(size, size);
		this.btnRefresh.setMaxSize(size, size);
		this.btnRefresh.setMinSize(size, size);
		this.btnRefresh.addEventFilter(ActionEvent.ACTION, onBtnRefreshClick); //set click method listener
	}
	
	private void addControlsToAnchorPaneAreaTop(Node mNode, double anchorTop, double anchorRight, double anchorBottom, double anchorLeft){
		this.anchorPaneButtonAreaTop.getChildren().add(mNode);
		AnchorPane.setTopAnchor(mNode, anchorTop);
		AnchorPane.setBottomAnchor(mNode, anchorBottom);
		
		if(!(anchorRight == 0.0)){
			AnchorPane.setRightAnchor(mNode, anchorRight);
		}
		
		if(!(anchorLeft == 0.0)){
			AnchorPane.setLeftAnchor(mNode, anchorLeft);
		}
		
	}
	
	private void setButtonToolTip(Button btn, String msg){
		btn.setTooltip(new Tooltip(msg));
	}
	
	@FXML
	public void enter() throws IOException {
		//user click mouse on the enter button
		String userInput = txtBoxCommand.getText();
		executeCommand(userInput);
	}

	public boolean executeCommand(String userInput){
		String[] tokens = null;
		String parserOutput = "";
		String logicOutput = "";
		String command = "";
		
		mLog.logInfo(Constants.LOG_UI_RUN_ON_ENTER + userInput);
		MainParser mp = MainParser.validateInput(userInput);
		parserOutput = mp.getMessage();
		if(!parserOutput.equals(Constants.MESSAGE_VALID_INPUT)){
			//display error
			txtStatus.setText(parserOutput);
			mLog.logInfo(Constants.LOG_UI_FAIL_VALIDATE_INPUT + parserOutput);
			return false; //fail
		}

		mLog.logInfo(Constants.LOG_UI_SUCCESS_VALIDATE_INPUT + parserOutput);

		command = mp.getCommand();

		if(command.equalsIgnoreCase(Constants.VALUE_HELP)){
			showHelp();
			txtStatus.setText(Constants.LOGIC_SUCCESS_HELP);
			return true;
		}

		tokens = mp.getTokens();
		
		if(command.equalsIgnoreCase(Constants.VALUE_SEARCH)){
			setSearchKeyword(tokens);
			initTaskListInListView();
			txtStatus.setText(Constants.LOGIC_SUCCESS_SEARCH + searchKeyword);
			resetSearchKeyword();
			return true;
		}
		
		logicOutput = Logic.runCommand(command, tokens);

		txtStatus.setText(logicOutput);

		initTaskListInListView();
		
		Logic.storeToHistory(userInput);
		
		txtBoxCommand.clear();
		
		return true;
	}
	
	
	@FXML
	public void initialize() {
		String logicOutput = "";

		this.clearContent();
		this.initStorage();
		this.initPreferences(); //initialize preferences
		this.initLogging(); //initialize logging

		this.initButtonSetting();
		this.initButtonAddNewTask();
		this.initButtonHelp();
		this.initButtonRefresh();
		this.initButtonRedo();
		this.initButtonUndo();
		
		this.addControlsToAnchorPaneAreaTop(this.btnSetting, 0.0, 5.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnAddNewTask, 0.0, 45.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnRefresh, 0.0, 85.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnHelp, 0.0, 125.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnUndo, 0.0, 0.0, 0.0, 5.0);
		this.addControlsToAnchorPaneAreaTop(this.btnRedo, 0.0, 0.0, 0.0, 70.0);
		
		
		anchorPaneMain.getStyleClass().addAll(CSS_TRANSPARENT_BACKGROUND);
		txtStatus.getStyleClass().addAll(CSS_TEXT_BACKGROUND, CSS_TEXT_STATUS);
		txtBoxCommand.getStyleClass().addAll(CSS_TEXT_BOX);
		btnEnter.getStyleClass().addAll(CSS_BUTTON);

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
		listview_task_fx_id.getStyleClass().addAll(CSS_LIST_VIEW); //add css class
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
				storageList = searchTasks(storageList, searchKeyword);
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
	
	private EventHandler<ActionEvent> onBtnUndoClick = new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			executeCommand("undo");
		}
	};
	
	private EventHandler<ActionEvent> onBtnRedoClick = new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			executeCommand("redo");
		}
	};
	
	private EventHandler<ActionEvent> onBtnHelpClick = new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			showHelp();
		}
	};
	
	private EventHandler<ActionEvent> onBtnRefreshClick = new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			initTaskListInListView();
		}
	};
	//============================== SEARCH FUNCTIONS ====================================
	private void setSearchKeyword(String[] tokens) {
		searchKeyword = getKeyword(tokens);
	}
	
	private String getKeyword(String[] tokens) {
		return tokens[0];
	}
	
	private void resetSearchKeyword() {
		searchKeyword = null;
	}
	
	private ArrayList<Task> searchTasks(ArrayList<Task> storageList, String keyword) {
		ArrayList<Task> displayList = new ArrayList<Task>();
		
		for (Task task: storageList) {
			if (hasKeyword(task, searchKeyword)) {
				displayList.add(task);
			}
		}
		
		return displayList;
	}
	
	private boolean hasKeyword(Task task, String keyword) {
		String taskDescription = task.getTitle();
		String[] descriptionTokens = makeIntoTokens(taskDescription);
		
		for (String token: descriptionTokens) {
			if (keyword.equals("")) {
				return false;
			} else if (token.equals(keyword)) {
				return true;
			}
		}
		
		return false;
	}
	
	private String[] makeIntoTokens(String taskDescription) {
		return taskDescription.split("\\s+");
	}
	//============================== POPOVER ADD NEW TASK ====================================
	private EventHandler<ActionEvent> onBtnAddNewTaskClick = new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			//initTaskListInListView();
			System.out.println("helo");
			showPopOverAdd();
		}
	};
	
	//============================== POPOVER Setting ======================================
	private EventHandler<ActionEvent> onBtnSettingClick = new EventHandler<ActionEvent>() {
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
		final TextField txtBoxCurrentDirectory = new TextField();
		Button btnBrowse = new Button("Browse");
		Button btnSave = new Button("Save All Settings");
		
		//Set Message width first
		setPopOverLabelMessageStyle(lblMessage, popWidth);
		
		//Get current file location from preference
		final String currentFileDir = mPrefs.getPreferenceFileLocation();
		
		//Textbox directory
		txtBoxCurrentDirectory.setPrefWidth(380.0);
		txtBoxCurrentDirectory.setEditable(false);
		setTextFieldText(txtBoxCurrentDirectory, currentFileDir);
		
		//Button browse
		btnBrowse.getStyleClass().addAll(CSS_POP_OVER_BUTTON);
		btnBrowse.setPrefWidth(100.0);
		btnBrowse.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() { //button browse click
			@Override
	        public void handle(ActionEvent event) {
				//browse for file
				mPopOverSetting.setAutoHide(false); //set hide to false when browse file
				
				//Hide popver message
				setPopOverLabelMessageVisible(lblMessage, false, false);
				
				try{
					String newPath = chooseFile(currentFileDir);
					
					isValidFilePath = Logic.checkFileBeforeSave(newPath);
					if(isValidFilePath){
						setTextFieldText(txtBoxCurrentDirectory, newPath);
		    			//setTextStatus("");
		    		}else{
		    			setTextFieldText(txtBoxCurrentDirectory, currentFileDir); //set back to old file path 
		    			setPopOverLabelMessageText(lblMessage, POP_OVER_INVALID_FILE_MESSAGE); //show user file is invalid
		    			setPopOverLabelMessageVisible(lblMessage, false, true); //set to red and display
		    		}
				}catch(AssertionError e){
		    		//log
		    		e.printStackTrace();
		    	}catch(Exception e){
		    		//log
		    		e.printStackTrace();
		    	}
				
				mPopOverSetting.setAutoHide(true); //set hide to true again after browsing
			}
		});
		
		
		//Button save
		btnSave.getStyleClass().addAll(CSS_POP_OVER_BUTTON);
		btnSave.setPrefWidth(popWidth);
		btnSave.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event) {
				//do save setting
				boolean isFileCreated = false;
		    	boolean isPrefSave = false;
		    	
		    	String newPath = getTextFieldText(txtBoxCurrentDirectory); 
		    	
		    	if(isValidFilePath){ //check valid during the browse stage	
			    		try{
			    		isFileCreated = Logic.checkFileDuringSave(newPath);
			    		
			    		if(isFileCreated){
		            		String status = STATUS_SUCCESS_FILE_CREATED_MESSAGE.replace("[new_file_path]", newPath);
		        			setTextStatus(status);
		            	}else{
		            		//logging
		            		setTextStatus(STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE);
		            	}
		            	
		            	isPrefSave = setPreferenceFilePath(newPath); //save preferences
	            	
		            	if(!isPrefSave){ //unable to save
		            		setTextStatus(STATUS_FAIL_TO_SAVE);
		            		
		            	}else{ //saved successfully
		            		mStorage.setDataFolderLocation(newPath); //set new path to storage
		            		initTaskListInListView(); //refresh the listview 
		            	}
		            	
		            	//Label message
						setPopOverLabelMessageText(lblMessage, POP_OVER_SUCCESS_SETTING_MESSAGE);
						setPopOverLabelMessageVisible(lblMessage, true, true);
						
		    		}catch(AssertionError e){
		        		//log
		        		e.printStackTrace();
		        		setTextStatus(STATUS_EMPTY_XML_FILE_PATH_MESSAGE);
		        		//Label message
						setPopOverLabelMessageText(lblMessage, POP_OVER_FAILED_SETTING_MESSAGE);
						setPopOverLabelMessageVisible(lblMessage, false, true);
		        		
		        	}catch(Exception e){
		        		//log
		        		e.printStackTrace();
		        		setTextStatus(STATUS_APPLICATION_ERROR_MESSAGE);
		        		//Label message
						setPopOverLabelMessageText(lblMessage, POP_OVER_FAILED_SETTING_MESSAGE);
						setPopOverLabelMessageVisible(lblMessage, false, true);
		        	}//end try
		    		
		    	}//end isValidFilePath
				
			}//end handle
		});
		
		
		//label setting title
		lblSettingTitle.getStyleClass().addAll(CSS_POP_OVER_TITLE);
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
		vBox.getStyleClass().addAll(CSS_POP_OVER_CONTENT_AREA); //set style for the vbox
		
		this.mPopOverSetting = new PopOver(vBox);
		this.mPopOverSetting.setHideOnEscape(true);
		this.mPopOverSetting.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
		this.mPopOverSetting.setAutoFix(true);
		this.mPopOverSetting.setAutoHide(true);
		this.mPopOverSetting.setDetachable(false);
	}
	
	//================== POP OVER ADD NEW TASK ========================
	private void showPopOverAdd(){
		double popWidth = 400.0;
		double popHeight = 380.0;
	
		VBox vBox = new VBox(8);
		HBox hBox = new HBox(4);
		
		final TextArea txtAreaTaskTitle = new TextArea();
		Label lblTitle = new Label("Add New Task");
		Label lblDateTitle = new Label("Start Date:");
		Label lblTimeTitle = new Label("Start Time:");
		Label lblTimeTip = new Label("(24 hrs format)");
		Label lblColon = new Label(":");
		final DatePicker datePickerStartDate = new DatePicker(); //start date
		final DatePicker datePickerEndDate = new DatePicker(); //start date
		final TextField txtFieldStartHour = new TextField(); //end hour
		final TextField txtFieldStartMin = new TextField(); //end min
		final TextField txtFieldEndHour = new TextField(); //end hour
		final TextField txtFieldEndMin = new TextField(); //end min

		Button btnAdd = new Button("Add New Task");
		final Label lblMessage = new Label();
		
		String startStr = "Start ";
		String endStr = "End ";
		String defaultDate = "01/01/1970";
		String defaultHour = "07";
		String defaultMin = "30";
		String defaultTime = defaultHour + ":" + defaultMin;
		
		if(this.mPopOverAdd != null){
			if(this.mPopOverAdd.isShowing()){
				this.mPopOverAdd.setAutoHide(true);
				return;
			}
		}
		
		datePickerStartDate.setConverter(datePickerStringConverter);
		datePickerEndDate.setConverter(datePickerStringConverter);
	
		setPopOverLabelTitle(lblTitle, popWidth); //style the label title 
		setPopOverLabelDateTime(lblDateTitle, lblTimeTitle, 80.0); //style the labels 
		setPopOverTextFieldHourMinute(txtFieldStartHour, txtFieldStartMin);
		setPopOverLabelTimeTip(lblTimeTip);
		setPopOverLabelMessageStyle(lblMessage, popWidth); //set message style
		
		txtAreaTaskTitle.getStyleClass().addAll(this.CSS_POP_OVER_TEXTAREA);
		txtAreaTaskTitle.setPrefRowCount(3); //text area
		txtAreaTaskTitle.setPrefWidth(popWidth);
		txtAreaTaskTitle.setWrapText(true);
		
		vBox.getChildren().add(lblTitle); //add title
		vBox.getChildren().add(txtAreaTaskTitle); //add task title
			
		hBox = new HBox(4); //start date
		hBox.getChildren().add(lblDateTitle); 
		hBox.getChildren().add(datePickerStartDate);
		vBox.getChildren().add(hBox);
		
		hBox = new HBox(4); //start hour and minute		
		hBox.getChildren().add(lblTimeTitle);  
		hBox.getChildren().add(txtFieldStartHour);
		hBox.getChildren().add(lblColon);
		hBox.getChildren().add(txtFieldStartMin);
		hBox.getChildren().add(lblTimeTip);
		HBox.setMargin(lblTimeTip, new Insets(10,0,0,0));
		vBox.getChildren().add(hBox);
		
		lblDateTitle = new Label("End Date:");
		lblTimeTitle = new Label("End Time:");
		lblTimeTip = new Label("(24 hrs format)");
		lblColon = new Label(":");
		
		setPopOverLabelDateTime(lblDateTitle, lblTimeTitle, 80.0); //style the labels again
		setPopOverTextFieldHourMinute(txtFieldEndHour, txtFieldEndMin);
		setPopOverLabelTimeTip(lblTimeTip);
		
		hBox = new HBox(4); //end date 
		hBox.getChildren().add(lblDateTitle); //start date
		hBox.getChildren().add(datePickerEndDate);
		vBox.getChildren().add(hBox);
		
		hBox = new HBox(4); //end hour and minute
		hBox.getChildren().add(lblTimeTitle);  
		hBox.getChildren().add(txtFieldEndHour);
		hBox.getChildren().add(lblColon);
		hBox.getChildren().add(txtFieldEndMin);
		hBox.getChildren().add(lblTimeTip);
		HBox.setMargin(lblTimeTip, new Insets(10,0,0,0));
		vBox.getChildren().add(hBox);
				
		
		VBox.setMargin(btnAdd, new Insets(10,0,0,0));
		vBox.setPadding(new Insets(5,10,5,10));
		
		vBox.getChildren().add(btnAdd); //add edit button
		vBox.getChildren().add(lblMessage); //add message
		vBox.setPrefSize(popWidth, popHeight); //set size
		vBox.getStyleClass().addAll(this.CSS_POP_OVER_CONTENT_AREA); //set style
		
		this.mPopOverAdd = new PopOver(vBox);
		this.mPopOverAdd.setHideOnEscape(true);
		this.mPopOverAdd.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
		this.mPopOverAdd.setAutoFix(true);
		this.mPopOverAdd.setAutoHide(true);
		this.mPopOverAdd.setDetachable(false);
    	this.mPopOverAdd.show(this.btnAddNewTask);
		
    	btnAdd.getStyleClass().addAll(CSS_POP_OVER_BUTTON);
    	btnAdd.setPrefWidth(popWidth);
    	btnAdd.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event) {
				LocalDate rawStartDate = datePickerStartDate.getValue();
				LocalDate rawEndDate = datePickerEndDate.getValue();
				
				String newTitle = txtAreaTaskTitle.getText(); //new title
				String newStartDate = convertDateToStorageFormat(rawStartDate); //new start date
				String newEndDate = convertDateToStorageFormat(rawEndDate); //new end date
				String newStartHour = txtFieldStartHour.getText(); //new start hour
				String newStartMinute = txtFieldStartMin.getText(); //new start minute
				String newEndHour = txtFieldEndHour.getText(); //new end hour
				String newEndMinute = txtFieldEndMin.getText(); //new end minute
				
				String newStartTime = "";
				String newEndTime = "";
				String addComamnd = addTaskCommand;
				
				//addTaskCommand = "add [task_title] -[start_date] -[start_time] -[end_date] -[end_time]";
				
				if(!(isValidHour(newStartHour) && isValidMinute(newStartMinute))){ //check is valid for start hour and minute (accept empty)
					setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
	    			setPopOverLabelMessageText(lblPopOverEditMessage, POP_OVER_INVALID_EDIT_START_TIME_MESSAGE);
	    			return;
				}
				
				if(!(isValidHour(newEndHour) && isValidMinute(newEndMinute))){ //check is valid for end hour and minute (accept empty)
					setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
	    			setPopOverLabelMessageText(lblPopOverEditMessage, POP_OVER_INVALID_EDIT_END_TIME_MESSAGE);
	    			return;
				}
				
				
			}
    	});
    	
	}
	
	
	private void setPopOverLabelTitle(Label lbl, double width){
		lbl.getStyleClass().addAll(this.CSS_POP_OVER_TITLE);
		lbl.setPrefWidth(width);
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setAlignment(Pos.CENTER);
	}
	
	private void setPopOverLabelDateTime(Label lblDate, Label lblTime, double width){
		lblDate.setPrefWidth(width);
		lblDate.setAlignment(Pos.CENTER_RIGHT);
		lblTime.setPrefWidth(width);
		lblTime.setAlignment(Pos.CENTER_RIGHT);
	}
	
	private void setPopOverTextFieldHourMinute(TextField txtHr, TextField txtMin){
		txtHr.setPrefWidth(60.0);
		txtHr.setPromptText("HH");
		txtMin.setPrefWidth(60.0);
		txtMin.setPromptText("MM");
	}
	
	private void setPopOverLabelTimeTip(Label lbl){
		lbl.getStyleClass().addAll(this.CSS_POP_OVER_TOOLTIP); //set tip style (after the minute input)
	}
	
	
	private void setPopOverLabelMessageVisible(Label lbl, boolean isValid, boolean isVisible){
		lbl.getStyleClass().removeAll(CSS_POP_OVER_CORRECT_MESSAGE, CSS_POP_OVER_ERROR_MESSAGE);
		if(isValid){ //valid, green background
			lbl.getStyleClass().addAll(CSS_POP_OVER_CORRECT_MESSAGE);	
		}else{//invalid, red background
			lbl.getStyleClass().addAll(CSS_POP_OVER_ERROR_MESSAGE);
		}
		
		lbl.setVisible(isVisible);
	}
	
	private String getTextFieldText(TextField txt){
		return txt.getText();
	}
	
	private void setTextFieldText(TextField txt, String str){
		txt.setText(str);
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
	
	private String chooseFile(String oldPath){
    	 FileChooser fileChooser = new FileChooser();
    	 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                 "XML files (*.xml)", "*.xml");
    	 fileChooser.getExtensionFilters().add(extFilter);
    	 fileChooser.setInitialDirectory(new File(getUserDesktopDirectory())); //set init directory
    	
    	 File file = fileChooser.showOpenDialog(null);
    	 
    	 if(file != null){
    		 if (!file.getPath().endsWith(".xml")) {
                 file = new File(file.getPath() + ".xml");
             }
    		 return file.getPath();
    	 }
    	 
    	 return oldPath;
	 }
	 
	 private String getUserDesktopDirectory(){
		 return Constants.SETTING_DEFAULT_FOLDER_PATH;
	 }	
	 
	 private StringConverter<LocalDate> datePickerStringConverter = new StringConverter<LocalDate>() {
		String datePattern = "dd MMMM yyyy"; //e.g. 18 January 2015
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

		@Override 
		public String toString(LocalDate date) {
			if (date != null) {
				return dateFormatter.format(date);
			} else {
				return "";
			}
		}

		@Override 
		public LocalDate fromString(String string) {
			if (string != null && !string.isEmpty()) {
				return LocalDate.parse(string, dateFormatter);
			} else {
				return null;
			}
		}
	};
		
	 private String convertDateToStorageFormat(LocalDate rawDate){
    	String datePatternForStorage = "d/M/yyyy"; //e.g. 18/4/2015
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePatternForStorage);
		
		if(rawDate == null){
			return "";
		}
		
		return dateFormatter.format(rawDate);
    }
	 
	 private boolean isValidHour(String hour){
    	//accept empty, means 0
    	try{
    		if(convertStringToInteger(hour) < 0 || convertStringToInteger(hour) > 23){
        		return false;
        	}
    	}catch(NumberFormatException e){ //maybe hour contain alphabet
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
	    
    private boolean isValidMinute(String minute){
    	//accept empty, means 0
    	try{
    		if(convertStringToInteger(minute) < 0 || convertStringToInteger(minute) > 60){
        		return false;
        	}
    	}catch(NumberFormatException e){ //maybe hour contain alphabet
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
	    
    private String convertTimeEmptyToValue(String t){
    	if(t.isEmpty() || t.equals("")){
    		return "00";
    	}
    	return t;
    }
    
	private int convertStringToInteger(String s){
		if(s.equals("") || s.isEmpty()){
			return 0;
		}
		return Integer.parseInt(s);
	}
	
	private String addLeadingZero(String str){
		if(str.length() == 1){
			return "0" + str;
		}
		return str;
	}
	
	
}
