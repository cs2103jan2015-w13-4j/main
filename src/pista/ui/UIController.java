package pista.ui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import pista.Constants;
import pista.CustomPreferences;
import pista.MainApp;
import pista.log.CustomLogging;
import pista.logic.Logic;
import pista.logic.Task;
import pista.parser.MainParser;
import pista.storage.Storage;

public class UIController {
	private MainApp mApp = null;
	private Stage stageHelp = null;
	private CustomLogging mLog = null;
	private CustomPreferences mPrefs = null;
	private Storage mStorage;
	private Logic mLogic;

	//Controls
	private AnchorPane anchorPaneStartGuide;
	private Button btnOkStartGuide = null;
	private Button btnSetting;
	private Button btnAddNewTask;
	private Button btnRefresh;
	private Button btnHelp;
	private Button btnRedo;
	private Button btnUndo;
	private PopOver mPopOverSetting = null;
	private PopOver mPopOverAdd = null;

	private String userInput = null;
	private String searchKeyword = null;
	private String[] possibleKeywords = null;

	private boolean isValidFilePath = false;
	private boolean isFileCreated = false;
	private boolean isCopied = false;
	private boolean isPrefSave = false;


	//Objects
	@FXML
	private StackPane mStackPane;

	@FXML
	private AnchorPane anchorPaneMain;

	@FXML
	private AnchorPane anchorPaneButtonAreaTop;

	@FXML
	private HBox hBoxInputArea;

	@FXML
	private Text txtStatus;

	@FXML
	private Text txtClock;

	@FXML
	private Button btnEnter;

	@FXML
	public TextField txtBoxCommand;

	@FXML
	private VBox vBoxMainArea;

	@FXML
	private ListView<Task> listviewTask;

	@FXML
	void onHelp(ActionEvent event) {
		showHelp(); //open help guide
	}

	@FXML
	void onRefresh(ActionEvent event) {
		this.initialize(); //initialize controls in UI
	}

	@FXML
	void onUIKeyPressed(KeyEvent event) { // UI listen key press
		if (Constants.KEY_COMBINATION_HELP.match(event)){ //when pressed F1, show help
			showHelp();
		}

		if (Constants.KEY_COMBINATION_START_GUIDE.match(event)){ //when pressed F2, show start guide
			initStartGuide();
		}	
	}

	@FXML
	public void initialize() {
		String logicOutput = "";
		
		this.initTimeClock();
		this.initReminder();
		this.initMainParser();
		this.initStorage();
		this.initLogic();
		this.initPreferences(); //initialize preferences
		this.initLogging(); //initialize logging
		this.initButtonRedo();
		this.initButtonUndo();
		this.initButtonSetting(); 
		this.initButtonAddNewTask();
		this.initButtonHelp();
		this.initButtonRefresh();

		this.addControlsToTopArea(); //add controls to the top anchor top area
		
		Logic.initLogging(); //initialize Logic logging
		Logic.initStorage();
		Logic.initPreference();
		
		if(this.getPreferencePistaFlag() == Constants.PREFERENCE_FIRST_LAUNCH_VALUE){ //equals 0
			this.initStartGuide(); //start the start guide
			this.setPreferencePistaFlag(Constants.PREFERENCE_SUBSQUENT_LAUNCH_VALUE); //update the preference of the flag = 1
			this.setPreferenceFilePath(this.getNewLaunchFileLocation()); //set preference file location to new file path
			Logic.writeNewFile(this.getNewLaunchFileLocation()); //write default XML string to new file
		}		

		this.mStorage.setDataFileLocation(getPreferenceFilePath());
		this.mStorage.initLogging(); //initialize Storage logging
		
		this.anchorPaneMain.getStyleClass().addAll(Constants.UI_CSS_TRANSPARENT_BACKGROUND);
		this.txtStatus.getStyleClass().addAll(Constants.UI_CSS_TEXT_BACKGROUND, Constants.UI_CSS_TEXT_STATUS);
		this.txtBoxCommand.getStyleClass().addAll(Constants.UI_CSS_TEXT_BOX);
		this.btnEnter.getStyleClass().addAll(Constants.UI_CSS_BUTTON);

		this.mStorage.setDataFileLocation(getPreferenceFilePath());
		this.mStorage.initLogging(); //initialize Storage logging
		Logic.initLogging(); //initialize Logic logging
		Logic.initStorage();
		Logic.initPreference();

		this.txtStatus.setText(logicOutput);

		this.txtBoxCommand.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event){
				if (Constants.KEY_COMBINATION_AUTO_COMPLETE.match(event)){
					onCtrlSpacePressed();

				}else if (Constants.KEY_COMBINATION_UP.match(event)) {
					onUpPressed();

				}else if (Constants.KEY_COMBINATION_DOWN.match(event)) {
					onDownPressed();
				}
			}
		});

		if(logicOutput.equals(Constants.LOGIC_SUCCESS_LOAD_XML)){
			initTaskListInListView(); //initialize listview
		}

	}//end initialize

	
	private void addControlsToTopArea(){
		this.addControlsToAnchorPaneAreaTop(this.btnSetting, 0.0, 5.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnAddNewTask, 0.0, 45.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnRefresh, 0.0, 85.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnHelp, 0.0, 125.0, 0.0, 0.0);
		this.addControlsToAnchorPaneAreaTop(this.btnUndo, 0.0, 0.0, 0.0, 5.0);
		this.addControlsToAnchorPaneAreaTop(this.btnRedo, 0.0, 0.0, 0.0, 70.0);
	}
	
	
	private void initTimeClock(){
		this.txtClock.getStyleClass().addAll(Constants.UI_CSS_TEXT_CLOCK);
		this.txtClock.setTextAlignment(TextAlignment.RIGHT);

		Timeline mTimeLine = new Timeline(new KeyFrame(Duration.seconds(0),
			new EventHandler<ActionEvent>(){
				DateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT_CLOCK);
				@Override
				public void handle(ActionEvent event) {
					Calendar nowDate = Calendar.getInstance();
					txtClock.setText(dateFormat.format(nowDate.getTime()));
				}
			}),
		new KeyFrame(Duration.seconds(1))
		);

		mTimeLine.setCycleCount(Animation.INDEFINITE);
		mTimeLine.play();
	}

	private void initReminder(){
		Timeline aTimeLine = new Timeline(new KeyFrame(Duration.seconds(0),
			new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					runReminder();
				}//end handle
			}),
		new KeyFrame(Duration.seconds(1))
		);
		aTimeLine.setCycleCount(Animation.INDEFINITE);
		aTimeLine.play();
	}

	public boolean initMainParser(){
		try{
			//this.mParser = MainParser.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean initStorage(){
		try{
			this.mStorage = Storage.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	public boolean initLogic(){
		try{
			this.mLogic = Logic.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private boolean initPreferences(){
		try{
			this.mPrefs = CustomPreferences.getInstance();
			this.mPrefs.initPreference(Constants.PREFERENCE_URL_PATH);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean initLogging(){
		try{
			this.mLog = CustomLogging.getInstance(Storage.class.getName());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	private void initStartGuide(){
		if(this.anchorPaneStartGuide == null){
			this.anchorPaneStartGuide = new AnchorPane();
		}

		this.anchorPaneStartGuide.getStyleClass().addAll(Constants.UI_CSS_START_GUIDE_PANE);
		this.anchorPaneStartGuide.setPrefWidth(Constants.UI_START_GUIDE_WIDTH);
		this.anchorPaneStartGuide.setPrefHeight(Constants.UI_START_GUIDE_HEIGHT);

		btnOkStartGuide = new Button(Constants.UI_START_GUIDE_BUTTON_OK_TITLE);
		btnOkStartGuide.getStyleClass().addAll(Constants.UI_CSS_START_GUIDE_BUTTON);
		btnOkStartGuide.setPrefSize(Constants.UI_START_GUIDE_BUTTON_OK_WIDTH, Constants.UI_START_GUIDE_BUTTON_OK_HEIGHT);
		btnOkStartGuide.addEventFilter(ActionEvent.ACTION, onBtnStartGuideClick);

		this.anchorPaneStartGuide.getChildren().add(btnOkStartGuide);
		AnchorPane.setRightAnchor(btnOkStartGuide, Constants.UI_START_GUIDE_BUTTON_OK_ANCHOR_RIGHT);
		AnchorPane.setTopAnchor(btnOkStartGuide, Constants.UI_START_GUIDE_BUTTON_OK_ANCHOR_TOP);

		this.mStackPane.setPadding(new Insets(0,0,0,0));
		this.mStackPane.getChildren().add(1, this.anchorPaneStartGuide);
	}

	private void hideStartGuide(){
		this.mStackPane.getChildren().removeAll(this.anchorPaneStartGuide);
	}

	private void initButtonRedo(){
		ImageView img = new ImageView(new Image(Constants.UI_REDO_IMAGE_LINK));
		img.setPreserveRatio(true);
		img.setFitHeight(Constants.UI_IMG_INSIDE_BUTTON_WIDTH);
		img.setFitWidth(Constants.UI_IMG_INSIDE_BUTTON_HEIGHT);

		if(this.btnRedo == null){
			this.btnRedo = new Button(Constants.UI_REDO_BUTTON_TITLE); //
		}

		setButtonToolTip(this.btnRedo, Constants.UI_TOOLTIP_UNDO);
		this.btnRedo.setTextAlignment(TextAlignment.RIGHT);
		this.btnRedo.getStyleClass().addAll(Constants.UI_CSS_BUTTON_IMAGE, Constants.UI_CSS_BUTTON_REDO);
		this.btnRedo.setPrefWidth(Constants.UI_BUTTON_IMAGE_TOP_WIDTH);
		this.btnRedo.setPrefHeight(Constants.UI_BUTTON_TOP_HEIGHT);
		this.btnRedo.addEventFilter(ActionEvent.ACTION, onBtnRedoClick); //set click method listener
	}

	private void initButtonUndo(){
		if(this.btnUndo == null){
			this.btnUndo = new Button("Undo");
		}

		this.setButtonToolTip(this.btnUndo, Constants.UI_TOOLTIP_REDO);
		this.btnUndo.setTextAlignment(TextAlignment.RIGHT);
		this.btnUndo.getStyleClass().addAll(Constants.UI_CSS_BUTTON_IMAGE, Constants.UI_CSS_BUTTON_UNDO);
		this.btnUndo.setPrefWidth(Constants.UI_BUTTON_IMAGE_TOP_WIDTH);
		this.btnUndo.setPrefHeight(Constants.UI_BUTTON_TOP_HEIGHT);
		this.btnUndo.addEventFilter(ActionEvent.ACTION, this.onBtnUndoClick); //set click method listener
	}

	private void initButtonSetting(){
		if(this.btnSetting == null){
			this.btnSetting = new Button();
		}

		this.setButtonToolTip(this.btnSetting, Constants.UI_TOOLTIP_SETTING);
		this.btnSetting.getStyleClass().addAll(Constants.UI_CSS_BUTTON_IMAGE, Constants.UI_CSS_BUTTON_SETTING);
		this.btnSetting.setPrefSize(Constants.UI_BUTTON_TOP_WIDTH, Constants.UI_BUTTON_TOP_HEIGHT);
		this.btnSetting.addEventFilter(ActionEvent.ACTION, this.onBtnSettingClick); //set click method listener
	}

	private void initButtonAddNewTask(){
		if(this.btnAddNewTask == null){
			this.btnAddNewTask = new Button();
		}

		setButtonToolTip(this.btnAddNewTask, Constants.UI_TOOLTIP_ADD);
		this.btnAddNewTask.getStyleClass().addAll(Constants.UI_CSS_BUTTON_IMAGE, Constants.UI_CSS_BUTTON_ADD);
		this.btnAddNewTask.setPrefSize(Constants.UI_BUTTON_TOP_WIDTH, Constants.UI_BUTTON_TOP_HEIGHT);
		this.btnAddNewTask.addEventFilter(ActionEvent.ACTION, this.onBtnAddNewTaskClick); //set click method listener
	}

	private void initButtonHelp(){
		if(this.btnHelp == null){
			this.btnHelp = new Button();
		}

		setButtonToolTip(this.btnHelp, Constants.UI_TOOLTIP_HELP);
		this.btnHelp.getStyleClass().addAll(Constants.UI_CSS_BUTTON_IMAGE, Constants.UI_CSS_BUTTON_HELP);
		this.btnHelp.setPrefSize(Constants.UI_BUTTON_TOP_WIDTH, Constants.UI_BUTTON_TOP_HEIGHT);
		this.btnHelp.addEventFilter(ActionEvent.ACTION, this.onBtnHelpClick); //set click method listener
	}

	private void initButtonRefresh(){
		if(this.btnRefresh == null){
			this.btnRefresh = new Button();
		}

		setButtonToolTip(this.btnRefresh, Constants.UI_TOOLTIP_REFRESH);
		this.btnRefresh.getStyleClass().addAll(Constants.UI_CSS_BUTTON_IMAGE, Constants.UI_CSS_BUTTON_REFRESH);
		this.btnRefresh.setPrefSize(Constants.UI_BUTTON_TOP_WIDTH, Constants.UI_BUTTON_TOP_HEIGHT);
		this.btnRefresh.addEventFilter(ActionEvent.ACTION, this.onBtnRefreshClick); //set click method listener
	}

	/**
	 * 
	 * @param mNode
	 * @param anchorTop
	 * @param anchorRight
	 * @param anchorBottom
	 * @param anchorLeft
	 */
	
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

	private String getNewLaunchFileLocation(){
		String defaultPath = Constants.USER_DIRECTORY + "" + Constants.SETTING_SAVE_AS_DEFAULT_XML_FILE_NAME;
		return defaultPath;
	}
	
	private int getPreferencePistaFlag(){
		try{
			int flag = 0; //either 1 or 0
			flag = this.mPrefs.getPreferencePistaFlag();
			return flag;
		}catch(Exception e){
			e.printStackTrace();
			return Constants.PREFERENCE_ERROR_LAUNCH_VALUE;
		}
	}

	private String getPreferenceFilePath(){ //get file path from preference
		try{
			String filePath = "";
			filePath = this.mPrefs.getPreferenceFileLocation();
			return filePath;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	private boolean setPreferencePistaFlag(int flag){
		try{
			this.mPrefs.setPreferencePistaFlag(flag);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private boolean setPreferenceFilePath(String newPath){ //set new file path from preference
		try{
			this.mPrefs.setPreferenceFileLocation(newPath);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean setTextStatus(String msg){
		if(this.txtStatus != null){
			this.txtStatus.setText(msg);
			return true;
		}
		return false;
	}

	private boolean setButtonToolTip(Button btn, String msg){
		try{
			if(btn != null){
				btn.setTooltip(new Tooltip(msg));
				return true;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
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

		this.mLog.logInfo(Constants.LOG_UI_RUN_ON_ENTER + userInput);
		MainParser mp = MainParser.validateInput(userInput);
		parserOutput = mp.getMessage();
		if(!parserOutput.equals(Constants.MESSAGE_VALID_INPUT)){
			//display error
			this.setTextStatus(parserOutput);
			this.mLog.logInfo(Constants.LOG_UI_FAIL_VALIDATE_INPUT + parserOutput);
			return false; //fail
		}

		this.mLog.logInfo(Constants.LOG_UI_SUCCESS_VALIDATE_INPUT + parserOutput);

		command = mp.getCommand();

		if(command.equalsIgnoreCase(Constants.VALUE_HELP)){
			showHelp();
			this.setTextStatus(Constants.LOGIC_SUCCESS_HELP);
			return true;
		}

		tokens = mp.getTokens();

		if(command.equalsIgnoreCase(Constants.VALUE_SEARCH)){
			this.setSearchKeyword(tokens);
			this.initTaskListInListView();
			this.setTextStatus(Constants.LOGIC_SUCCESS_SEARCH + searchKeyword);
			this.resetSearchKeyword();
			return true;
		}

		logicOutput = mLogic.runCommand(command, tokens);

		this.setTextStatus(logicOutput);
		this.initTaskListInListView();
		this.clearTextCommand();

		mLogic.storeToHistory(userInput);

		return true;
	}

	private boolean setTextCommand(String command){
		this.txtBoxCommand.setText(command);
		return true;
	}

	private String getTextCommand(){
		return this.txtBoxCommand.getText();
	}

	private boolean clearTextCommand(){
		this.txtBoxCommand.clear();
		return true;
	}

	public boolean initTaskListInListView(){
		try{
			ArrayList<Task> storageList = null;
			mLogic.reorderStorageList();
			storageList = mLogic.getStorageList();

			if (this.searchKeyword != null) {
				storageList = searchTasks(storageList, this.searchKeyword);
			}

			ObservableList<Task> myObservableList = FXCollections.observableList(storageList);
			this.listviewTask.setItems(null); 
			this.listviewTask.setItems(myObservableList);
			this.listviewTask.getStyleClass().addAll(Constants.UI_CSS_LIST_VIEW); //add css class
			this.listviewTask.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>(){ //populate every task into a custom cell
				@Override
				public ListCell<Task> call(ListView<Task> param) {
					final TaskListCell mCell = new TaskListCell();
					mCell.setUIParent(UIController.this);
					return mCell;
				}//end call
			});

			//Selected item
			//currently not in use
			this.listviewTask.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
				@Override      
				public void changed(ObservableValue<? extends Task> ov,
						Task oldTask, Task newTask) {
				}
			});

			return true;
		}catch(Exception e){
			this.mLog.logSevere(e.getMessage());
			return false;
		}//end try

	}//end initTaskListInListView

	public boolean showHelp(){
		if(this.stageHelp == null){
			this.stageHelp = new Stage();
		}

		double startX = this.mApp.getPrimaryStage().getX() + this.mApp.getPrimaryStageWidth();
		double startY = this.mApp.getPrimaryStage().getY();

		this.stageHelp.setTitle(Constants.HELP_TITLE);
		this.stageHelp.initStyle(StageStyle.UTILITY);
		this.stageHelp.initModality(Modality.NONE);
		this.stageHelp.setResizable(false);
		this.stageHelp.setX(startX);
		this.stageHelp.setY(startY);

		Scene scene = new Scene(new Browser(), 500, 700, Color.web("#666970"));
		this.stageHelp.setScene(scene);    
		this.stageHelp.show();

		return true;
	}

	public Stage getHelpStage(){
		return this.stageHelp;
	}

	public void onCtrlSpacePressed(){
		this.userInput = this.getTextCommand();
		String[] temp = this.userInput.split("\\s+",2);
		String command = temp[0];
		try { 
			int id = Integer.parseInt(temp[1]);
			if( command.equalsIgnoreCase("edit")){
				String processedString = mLogic.processTaskInfo(id);
				String finalStr = processedString;
				this.setTextCommand(this.userInput + finalStr);
			}
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void onUpPressed(){
		if(Constants.HISTORY_INDEX > 0){
			Constants.HISTORY_INDEX -= 1;
			this.setTextCommand(this.mStorage.getHistoryList().get(Constants.HISTORY_INDEX));
		}else{
			this.setTextCommand(this.mStorage.getHistoryList().get(0));
		}
	}

	public void onDownPressed(){
		if(Constants.HISTORY_INDEX < this.mStorage.getHistoryList().size() - 1){
			Constants.HISTORY_INDEX += 1;
			this.setTextCommand(this.mStorage.getHistoryList().get(Constants.HISTORY_INDEX));
		}else{
			Constants.HISTORY_INDEX = this.mStorage.getHistoryList().size();
			this.clearTextCommand();
		}
	}

	private EventHandler<ActionEvent> onBtnStartGuideClick = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			hideStartGuide();
		}
	};

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

	//============================== POPOVER ADD NEW TASK ====================================
	private EventHandler<ActionEvent> onBtnAddNewTaskClick = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
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
		final Label lblSettingTitle = new Label("Setting");
		final Label lblCurrentFileLocationTitle = new Label("Current File Location");
		final Label lblCurrentFileLocation = new Label("");
		final String currentFileDir = mPrefs.getPreferenceFileLocation(); //Get current file location from preference
		VBox vBox = new VBox(Constants.UI_POP_OVER_VBOX_SPACING);

		Button btnOpenFileBrowse = new Button("Open");
		Button btnSaveAsFileBrowse = new Button("Save As");

		if(!(currentFileDir.isEmpty() || currentFileDir == "")){
			lblCurrentFileLocation.setText(mPrefs.getPreferenceFileLocation());
		}

		//Button browse
		btnOpenFileBrowse.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_BUTTON);
		btnOpenFileBrowse.setPrefWidth(Constants.UI_POP_OVER_SETTING_WIDTH);
		btnOpenFileBrowse.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() { //button browse click
			@Override
			public void handle(ActionEvent event) {
				mPopOverSetting.setAutoHide(false); //set hide to false when browse file

				try{
					String newPath = openFile(currentFileDir);

					isValidFilePath = mLogic.checkFileBeforeSave(newPath);
					if(isValidFilePath){
						isFileCreated = mLogic.checkFileDuringSave(newPath);

						if(isFileCreated){
							String status = Constants.UI_STATUS_SUCCESS_FILE_OPEN_MESSAGE.replace("[new_file_path]", newPath);
							setTextStatus(status);
						}else{
							//logging
							setTextStatus(Constants.UI_STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE);
						}

						isPrefSave = setPreferenceFilePath(newPath); //save preferences
						if(!isPrefSave){ //unable to save
							setTextStatus(Constants.UI_STATUS_FAIL_PREFERENCE_SAVE);

						}else{ //saved successfully
							mStorage.setDataFileLocation(newPath); //set new path to storage
							initTaskListInListView(); //refresh the listview 
							setPopOverLabelText(lblCurrentFileLocation, newPath);
						}

						setTextStatus(Constants.UI_STATUS_SUCCESS_FILE_OPEN_MESSAGE); //success message

					}else{
						setPopOverLabelText(lblCurrentFileLocation, currentFileDir); //set back to old file path 
						setTextStatus(Constants.UI_STATUS_INVALID_XML_FILE_PATH_MESSAGE); //failed message
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


		//Button Save As 
		btnSaveAsFileBrowse.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_BUTTON);
		btnSaveAsFileBrowse.setPrefWidth(Constants.UI_POP_OVER_SETTING_WIDTH);
		btnSaveAsFileBrowse.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mPopOverSetting.setAutoHide(false); //set hide to false when browse file

				//Hide popver message
				//setPopOverLabelMessageVisible(lblMessage, false, false);

				try{
					String newPath = saveAsFile(currentFileDir);

					isCopied = mLogic.copyFile(currentFileDir, newPath);
					if(isCopied){
						setTextStatus(Constants.UI_STATUS_SUCCESS_FILE_SAVE_AS_MESSAGE.replace("[new_file_path]", newPath));

						isPrefSave = setPreferenceFilePath(newPath); //save preferences
						if(!isPrefSave){ //unable to save
							setTextStatus(Constants.UI_STATUS_FAIL_PREFERENCE_SAVE);

						}else{ //saved successfully
							mStorage.setDataFileLocation(newPath); //set new path to storage
							initTaskListInListView(); //refresh the listview 

							setPopOverLabelText(lblCurrentFileLocation, newPath);
						}

					}else{
						setTextStatus(Constants.UI_STATUS_FAIL_FILE_SAVE_AS_MESSAGE);
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

		//label setting title
		lblSettingTitle.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_TITLE);
		lblSettingTitle.setPrefWidth(Constants.UI_POP_OVER_SETTING_WIDTH);
		lblSettingTitle.setTextAlignment(TextAlignment.CENTER);
		lblSettingTitle.setAlignment(Pos.CENTER);

		vBox.setPadding(new Insets(Constants.UI_POP_OVER_VBOX_PADDING_INSETS_TOP, Constants.UI_POP_OVER_VBOX_PADDING_INSETS_RIGHT, 
					Constants.UI_POP_OVER_VBOX_PADDING_INSETS_BOTTOM, Constants.UI_POP_OVER_VBOX_PADDING_INSETS_LEFT)); //set Padding
		vBox.getChildren().add(lblSettingTitle);
		vBox.getChildren().add(lblCurrentFileLocationTitle);
		vBox.getChildren().add(lblCurrentFileLocation);
		vBox.getChildren().add(btnOpenFileBrowse);
		vBox.getChildren().add(btnSaveAsFileBrowse);

		vBox.setPrefSize(Constants.UI_POP_OVER_SETTING_WIDTH, Constants.UI_POP_OVER_SETTING_HEIGHT);
		vBox.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_CONTENT_AREA); //set style for the vbox

		this.mPopOverSetting = new PopOver(vBox);
		this.mPopOverSetting.setHideOnEscape(true);
		this.mPopOverSetting.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
		this.mPopOverSetting.setAutoFix(true);
		this.mPopOverSetting.setAutoHide(true);
		this.mPopOverSetting.setDetachable(false);
	}

	//================== POP OVER ADD NEW TASK ========================
	private void showPopOverAdd(){
		VBox vBox = new VBox(Constants.UI_POP_OVER_VBOX_SPACING);
		HBox hBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING);

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

		if(this.mPopOverAdd != null){
			if(this.mPopOverAdd.isShowing()){
				this.mPopOverAdd.setAutoHide(true);
				return;
			}
		}

		datePickerStartDate.setPromptText("01 January 2015");
		datePickerStartDate.setConverter(datePickerStringConverter);
		datePickerEndDate.setPromptText("22 January 2015");
		datePickerEndDate.setConverter(datePickerStringConverter);

		setPopOverLabelTitle(lblTitle, Constants.UI_POP_OVER_ADD_WIDTH); //style the label title 
		setPopOverLabelDateTime(lblDateTitle, lblTimeTitle, Constants.UI_POP_OVER_LABEL_DATETIME_WIDTH); //style the labels 
		setPopOverTextFieldHourMinute(txtFieldStartHour, txtFieldStartMin);
		setPopOverLabelTimeTip(lblTimeTip);
		
		txtAreaTaskTitle.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_TEXTAREA);
		txtAreaTaskTitle.setPrefRowCount(3); //text area
		txtAreaTaskTitle.setPrefWidth(Constants.UI_POP_OVER_ADD_WIDTH);
		txtAreaTaskTitle.setWrapText(true);
		txtAreaTaskTitle.setPromptText("Type in a new task title");

		vBox.getChildren().add(lblTitle); //add title
		vBox.getChildren().add(txtAreaTaskTitle); //add task title

		hBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING); //start date
		hBox.getChildren().add(lblDateTitle); 
		hBox.getChildren().add(datePickerStartDate);
		vBox.getChildren().add(hBox);

		hBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING); //start hour and minute		
		hBox.getChildren().add(lblTimeTitle);  
		hBox.getChildren().add(txtFieldStartHour);
		hBox.getChildren().add(lblColon);
		hBox.getChildren().add(txtFieldStartMin);
		hBox.getChildren().add(lblTimeTip);
		HBox.setMargin(lblTimeTip, new Insets(Constants.UI_POP_OVER_HBOX_MARGIN_TOP,
												Constants.UI_POP_OVER_HBOX_MARGIN_RIGHT,
												Constants.UI_POP_OVER_HBOX_MARGIN_BOTTOM,
												Constants.UI_POP_OVER_HBOX_MARGIN_LEFT));
		vBox.getChildren().add(hBox);

		lblDateTitle = new Label("End Date:");
		lblTimeTitle = new Label("End Time:");
		lblTimeTip = new Label("(24 hrs format)");
		lblColon = new Label(":");

		setPopOverLabelDateTime(lblDateTitle, lblTimeTitle, Constants.UI_POP_OVER_LABEL_DATETIME_WIDTH); //style the labels again
		setPopOverTextFieldHourMinute(txtFieldEndHour, txtFieldEndMin);
		setPopOverLabelTimeTip(lblTimeTip);

		hBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING); //end date 
		hBox.getChildren().add(lblDateTitle); //start date
		hBox.getChildren().add(datePickerEndDate);
		vBox.getChildren().add(hBox);

		hBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING); //end hour and minute
		hBox.getChildren().add(lblTimeTitle);  
		hBox.getChildren().add(txtFieldEndHour);
		hBox.getChildren().add(lblColon);
		hBox.getChildren().add(txtFieldEndMin);
		hBox.getChildren().add(lblTimeTip);
		HBox.setMargin(lblTimeTip, new Insets(Constants.UI_POP_OVER_HBOX_MARGIN_TOP,
												Constants.UI_POP_OVER_HBOX_MARGIN_RIGHT,
												Constants.UI_POP_OVER_HBOX_MARGIN_BOTTOM,
												Constants.UI_POP_OVER_HBOX_MARGIN_LEFT));
		vBox.getChildren().add(hBox);
		VBox.setMargin(btnAdd, new Insets(Constants.UI_POP_OVER_VBOX_MARGIN_TOP,
											Constants.UI_POP_OVER_VBOX_MARGIN_RIGHT,
											Constants.UI_POP_OVER_VBOX_MARGIN_BOTTOM,
											Constants.UI_POP_OVER_VBOX_MARGIN_LEFT));
		vBox.setPadding(new Insets(Constants.UI_POP_OVER_VBOX_PADDING_INSETS_TOP, Constants.UI_POP_OVER_VBOX_PADDING_INSETS_RIGHT, 
					Constants.UI_POP_OVER_VBOX_PADDING_INSETS_BOTTOM, Constants.UI_POP_OVER_VBOX_PADDING_INSETS_LEFT)); //set Padding

		vBox.getChildren().add(btnAdd); //add edit button
		vBox.getChildren().add(lblMessage); //add message
		vBox.setPrefSize(Constants.UI_POP_OVER_ADD_WIDTH, Constants.UI_POP_OVER_ADD_HEIGHT); //set size
		vBox.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_CONTENT_AREA); //set style

		this.mPopOverAdd = new PopOver(vBox);
		this.mPopOverAdd.setHideOnEscape(true);
		this.mPopOverAdd.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
		this.mPopOverAdd.setAutoFix(true);
		this.mPopOverAdd.setAutoHide(true);
		this.mPopOverAdd.setDetachable(false);
		this.mPopOverAdd.show(this.btnAddNewTask);

		btnAdd.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_BUTTON);
		btnAdd.setPrefWidth(Constants.UI_POP_OVER_ADD_WIDTH);
		btnAdd.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				boolean isCreated = false;

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
				String addCommand = Constants.UI_POP_OVER_ADD_TASK_COMMAND;

				//addTaskCommand = "add [task_title] -[start_date] -[start_time] -[end_date] -[end_time]";

				if(!isTitleValid(newTitle)){ //check title
					setPopOverLabelMessageVisible(lblMessage, false, true); //show error message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_INVALID_TITLE_MESSAGE);
					return;
				}

				if(!(isValidHour(newStartHour) && isValidMinute(newStartMinute))){ //check is valid for start hour and minute (accept empty)
					setPopOverLabelMessageVisible(lblMessage, false, true); //show error message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_INVALID_START_TIME_MESSAGE);
					return;
				}

				if(!(isValidHour(newEndHour) && isValidMinute(newEndMinute))){ //check is valid for end hour and minute (accept empty)
					setPopOverLabelMessageVisible(lblMessage, false, true); //show error message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_INVALID_END_TIME_MESSAGE);
					return;
				}

				//Check both start date and time
				if((newStartDate.equals("") || newStartDate.isEmpty()) && 
						(newStartHour.equals("") || newStartHour.isEmpty()) && 
						(newStartMinute.equals("") || newStartMinute.isEmpty())){ //start date, time is empty
	
					addCommand = addCommand.replace("-[start_date]", "").replace("-[start_time]", ""); //remove start date and time
					
				}else if(!(newStartDate.equals("") || newStartDate.isEmpty()) && 
						((newStartHour.equals("") || newStartHour.isEmpty()) ||
								(newStartMinute.equals("") || newStartMinute.isEmpty()))) { //date is not empty, but hour or minute is empty

					setPopOverLabelMessageVisible(lblMessage, false, true); //show error message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_INVALID_START_TIME_MESSAGE);
					return;

				}else{
					newStartTime = newStartHour + ":" + newStartMinute;
					addCommand = addCommand.replace("[start_date]", newStartDate).replace("[start_time]", newStartTime);
				}

				//Check both end date and time
				if((newEndDate.equals("") || newEndDate.isEmpty()) && 
						(newEndHour.equals("") || newEndHour.isEmpty()) && 
						(newEndMinute.equals("") || newEndMinute.isEmpty())){
					//start date, time is empty
					//remove start date and time
					addCommand = addCommand.replace("-[end_date]", "").replace("-[end_time]", "");
				}else if(!(newStartDate.equals("") || newStartDate.isEmpty()) && 
						((newEndHour.equals("") || newEndHour.isEmpty()) ||
								(newEndMinute.equals("") || newEndMinute.isEmpty()))) { //date is not empty, but hour or minute is empty

					setPopOverLabelMessageVisible(lblMessage, false, true); //show error message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_INVALID_END_TIME_MESSAGE);
					return;

				}else{
					newEndTime = newEndHour + ":" + newEndMinute;
					addCommand = addCommand.replace("[end_date]", newEndDate).replace("[end_time]", newEndTime);
				}

				addCommand = addCommand.replace("[task_title]", newTitle);	
				System.out.println(addCommand);

				isCreated = executeCommand(addCommand);	
				if(isCreated){
					setPopOverLabelMessageVisible(lblMessage, true, true); //show success message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_SUCCESS_ADD_MESSAGE);
					return;
				}else{
					setPopOverLabelMessageVisible(lblMessage, false, true); //show error message
					setPopOverLabelMessageText(lblMessage, Constants.UI_POP_OVER_FAIL_ADD_MESSAGE);
					return;
				}

			}//end handle
		});

	}


	private void setPopOverLabelTitle(Label lbl, double width){
		lbl.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_TITLE);
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
		lbl.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_TOOLTIP); //set tip style (after the minute input)
	}

	private void setPopOverLabelMessageVisible(Label lbl, boolean isValid, boolean isVisible){
		lbl.getStyleClass().removeAll(Constants.UI_CSS_POP_OVER_CORRECT_MESSAGE, Constants.UI_CSS_POP_OVER_ERROR_MESSAGE);
		if(isValid){ //valid, green background
			lbl.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_CORRECT_MESSAGE);	
		}else{//invalid, red background
			lbl.getStyleClass().addAll(Constants.UI_CSS_POP_OVER_ERROR_MESSAGE);
		}
		lbl.setVisible(isVisible);
	}

	private void setPopOverLabelText(Label lbl, String msg){
		lbl.setText(msg);
	}

	private void setPopOverLabelMessageText(Label lbl, String msg){
		lbl.setText(msg);
	}

	private String openFile(String oldPath){
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

	private String saveAsFile(String oldPath){
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialDirectory(new File(getUserDesktopDirectory())); //set init directory

		File file = fileChooser.showSaveDialog(null);

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
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_DATEPICKER); //e.g. 18 January 2015
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
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_STORAGE); //e.g. 18/4/2015
		if(rawDate == null){
			return "";
		}
		return dateFormatter.format(rawDate);
	}

	private boolean isTitleValid(String title){
		if(title.isEmpty() || title.equals("")){ //don't accept empty
			return false;
		}
		return true;
	}

	private boolean isValidHour(String hour){
		try{ //accept empty, means 0
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
		try{ //accept empty, means 0
			if(convertStringToInteger(minute) < 0 || convertStringToInteger(minute) > 60){
				return false;
			}
		}catch(NumberFormatException e){ //maybe hour contain alphabet
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private int convertStringToInteger(String s){
		if(s.equals("") || s.isEmpty()){
			return 0;
		}
		return Integer.parseInt(s);
	}

	//============================== SEARCH FUNCTIONS ====================================
	private void setSearchKeyword(String[] tokens) {
		this.searchKeyword = getKeyword(tokens);
		this.possibleKeywords = tokens;
	}

	private String getKeyword(String[] tokens) {
		return tokens[Constants.SEARCH_TOKEN_KEYWORD];
	}

	private void resetSearchKeyword() {
		this.searchKeyword = null;
		this.possibleKeywords = null;
	}

	private ArrayList<Task> searchTasks(ArrayList<Task> storageList, String keyword) {
		ArrayList<Task> displayList = new ArrayList<Task>();

		for (Task task: storageList) {
			if (hasKeyword(task, possibleKeywords)) {
				displayList.add(task);
			}
		}
		return displayList;
	}

	private boolean hasKeyword(Task task, String[] possibleKeywords) {
		String taskDescription = task.getTitle();
		String taskStartDate = task.getStartDate();
		String taskEndDate = task.getEndDate();
		String taskStartTime = task.getStartTime();
		String taskEndTime = task.getEndTime();

		//check if keyword exists in title of task
		String[] descriptionTokens = makeIntoTokens(taskDescription);

		for (String token: descriptionTokens) {
			if (token.equalsIgnoreCase(searchKeyword)) {
				return true;
			}
		}
		
		//check if keyword is a time
		String keywordAsTime = this.getTime();
		if (keywordAsTime != null) {
			if (keywordAsTime.equals(taskStartTime) || keywordAsTime.equals(taskEndTime)) {
				System.out.println("checking as time");
				return true;
			}
		}
				
		//check if keyword is a date
		String keywordAsDate = this.getDate();
		if (keywordAsDate != null) {
			if (keywordAsDate.equals(taskStartDate) || keywordAsDate.equals(taskEndDate)) {
				return true;
			}
		}
		
		return false;
	}

	private String[] makeIntoTokens(String taskDescription) {
		return taskDescription.split("\\s+");
	}
	
	private String getDate() {
		return this.possibleKeywords[Constants.SEARCH_KEYWORD_DATE];
	}
	
	private String getTime() {
		return this.possibleKeywords[Constants.SEARCH_KEYWORD_TIME];
	}

	private void runReminder(){
		for (int i = 0; i < mLogic.getStorageList().size(); i++) {
			Task extractedTask = mLogic.getStorageList().get(i);
			String taskTitle = extractedTask.getTitle();
			boolean taskIsReminded = extractedTask.getIsReminded();
			Long taskReminder = extractedTask.getReminder();
			Long taskEndMillisecond = extractedTask.getEndMilliseconds();
			Long timeNow = System.currentTimeMillis();

			if(taskReminder != 0L){
				if(timeNow >= taskReminder && !taskIsReminded){
					String datePattern = "dd MMMM yyyy"; //e.g. 18 January 2015
					SimpleDateFormat mDateFormat = new SimpleDateFormat(datePattern);
					String endDate = mDateFormat.format(new Date(taskEndMillisecond));

					//update the reminded status in the storage list
					mLogic.getStorageList().get(i).setIsReminded(true);

					showReminder("Upcoming task: " + taskTitle, 
							"Due on " + endDate);

					File file = new File (Constants.BUILD_PATH + Constants.UI_ALARM_LOCATION);
					playAlarm(file.toURI().toString());
				}
			}
		}
	}

	private void showReminder(String title, String msg){
		Notifications.create().title(title).text(msg).showWarning();	
	}

	private void playAlarm(String url){
		Media alarm = new Media (url);
		startMediaPlayer(alarm);
	}

	private void startMediaPlayer(Media m){
		if(m != null){
			MediaPlayer mp = new MediaPlayer(m);
			mp.play();
		}
	}

	public void setMainAppController(MainApp app){
		this.mApp = app;
	}


}
