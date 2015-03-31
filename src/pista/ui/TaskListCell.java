package pista.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.controlsfx.control.PopOver;

import com.sun.javafx.geom.Rectangle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import pista.Constants;
import pista.logic.Logic;
import pista.logic.Task;
import pista.parser.MainParser;
import pista.parser.TokenValidation;

public class TaskListCell extends ListCell<Task> {

    private final String TASK_LIST_CELL = "task-list-cell";
    private final String TASK_LIST_CELL_ID_CLASS = "task-list-cell-id";

    private final String TASK_LIST_CELL_BOX_RED_CLASS = "task-list-cell-box-red";
    private final String TASK_LIST_CELL_BOX_YELLOW_CLASS = "task-list-cell-box-yellow";
    private final String TASK_LIST_CELL_BOX_GREEN_CLASS = "task-list-cell-box-green";
    private final String TASK_LIST_CELL_BOX_TRANSPARENT_CLASS = "task-list-cell-box-transparent";
    
    private final String TASK_LIST_CELL_TITLE_CLASS = "task-list-cell-title";
    private final String TASK_LIST_CELL_DATETIME_CLASS = "task-list-cell-datetime";
    
    private final String TASK_LIST_CELL_BUTTON_IMAGE_CLASS = "task-list-cell-button-image";
    private final String TASK_LIST_CELL_DISABLE_ALARM_CLASS = "task-list-cell-disable-alarm";
    private final String TASK_LIST_CELL_ENABLE_ALARM_CLASS = "task-list-cell-enable-alarm";
    private final String TASK_LIST_CELL_IS_DONE_ALARM_CLASS = "task-list-cell-is-done-alarm";
    
    private final String TASK_LIST_CELL_PRIORITY_CRITICAL_CLASS = "task-list-cell-priority-critical";
    private final String TASK_LIST_CELL_PRIORITY_NORMAL_CLASS = "task-list-cell-priority-normal";
    private final String TASK_LIST_CELL_PRIORITY_LOW_CLASS = "task-list-cell-priority-low";
    private final String TASK_LIST_CELL_PRIORITY_DEFAULT_CLASS = "task-list-cell-priority-default";
    
   
    private final String TASK_LIST_CELL_TEXT_IS_DONE_CLASS = "task-list-cell-text-is-done";
    private final String TASK_LIST_CELL_BACKGROUND_IS_DONE_CLASS = "task-list-cell-background-is-done";
    
    private final String TASK_LIST_CELL_STRIKE_THROUGH_CLASS = "task-list-cell-label-strike-through";
    
    private final String POP_OVER_CONTENT_AREA_CLASS = "pop-content-area";
    private final String POP_OVER_TITLE_CLASS = "pop-title";
    private final String POP_OVER_LABEL_PRIORITY_CLASS = "label-priority";
    private final String POP_OVER_IMAGE_CRITICAL_CLASS = "pop-over-img-critical";
    private final String POP_OVER_IMAGE_NORMAL_CLASS = "pop-over-img-normal";
    private final String POP_OVER_IMAGE_LOW_CLASS = "pop-over-img-low";
    private final String POP_OVER_IMAGE_DEFAULT_CLASS = "pop-over-img-default";
    private final String POP_OVER_BUTTON_CHANGE_CLASS = "btn-change";
    
    
    private final int MAX_CHARACTER_IN_TITLE = 50;
    
    private String DISPLAY_START_DATE_TIME = "From [datetime]"; 
    private String DISPLAY_END_DATE_TIME = "To [datetime]"; 
    
    private final String DONE_COMMAND = "-" + Constants.MARK_DONE; //-done
    private final String NOT_DONE_COMMAND = "-" + Constants.MARK_UNDONE; //-undone
    
	private GridPane grid = new GridPane();
	private VBox vBoxDateTime = new VBox(0);
	private VBox vBoxCheckBox = new VBox(0);
	
    //private Label icon = new Label();
	private Label lblID = new Label();
    private Label lblTitle = new Label();
    private Label lblStartDateTime = new Label();
    private Label lblEndDateTime = new Label();
    private CheckBox checkBox = new CheckBox();
    private UIController mUIParent = null;
    private VBox vBoxColor = new VBox(0);
    
    private Button mBtnAlarm = new Button();
    private Button mBtnPriority = new Button();
    
    private PopOver mPopOverAlarm = null;
    private PopOver mPopOverPriority = null;
    
	TextField txtHourField = null;
	TextField txtMinField = null;
	DatePicker datePicker = null;
    
    
    ToggleGroup priorityGroup = null;
    
    private String getID = "";
    private String getTitle = "";
    private String getCategory = "";
    private String getStartDate = "";
    private String getEndDate = "";
    private String getStartTime = "";
    private String getEndTime = "";
    private boolean getIsDone = false;
    private Long getRemainder = 0L;
    private String getPriority = "";
    
    public TaskListCell() {
    	
        configureGrid(); 
        configureCheckBoxIsDone();
        configureButtonImage();
        configureLabelID();
        configureLabelTitle();
        configureLabelDateTime();
        configureVBoxCheckBox();
        addControlsToVBox();
        addControlsToGrid();            
    }
    
    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(4);
        grid.setPadding(new Insets(0, 2, 0, 2));
        grid.setMaxHeight(70.0);
        grid.setMinHeight(70.0);
        grid.setPrefHeight(70.0);
        
        ColumnConstraints col1 = new ColumnConstraints(); //color box
        col1.setPercentWidth(2);
        col1.setHalignment(HPos.LEFT);
        
        ColumnConstraints col2 = new ColumnConstraints(); //check box
        col2.setPercentWidth(3);
        col2.setHalignment(HPos.CENTER);
        
        ColumnConstraints col3 = new ColumnConstraints(); //ID
        col3.setPercentWidth(5);
        col3.setHalignment(HPos.CENTER);
        
        ColumnConstraints col4 = new ColumnConstraints(); //title
        col4.setPercentWidth(60);
        col4.setHalignment(HPos.LEFT);
        
        ColumnConstraints col5 = new ColumnConstraints(); // priority
        col5.setPercentWidth(5);
        col5.setHalignment(HPos.RIGHT);
        
        ColumnConstraints col6 = new ColumnConstraints(); // alarm
        col6.setPercentWidth(5);
        col6.setHalignment(HPos.RIGHT);
        
        ColumnConstraints col7 = new ColumnConstraints(); //date time
        col7.setPercentWidth(20);
        col7.setHalignment(HPos.RIGHT);
        
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setPrefHeight(70.0);
        grid.getRowConstraints().addAll(row1);
        
        grid.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6, col7);
        
    }
    
    //private void configureIcon() {
        //icon.setFont(Font.font(FONT_AWESOME, FontWeight.BOLD, 24));
        //icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
    //}
    
    private void setCellBackground(boolean isDone){
    	if(isDone){
    		getStyleClass().add(TASK_LIST_CELL_BACKGROUND_IS_DONE_CLASS);
    	}else{
    		getStyleClass().add(TASK_LIST_CELL);
    	}
    }
    
    private void configureButtonImage(){
    	//mImgView.setImage(mImg);
    	//Alarm
    	double size = 25.0;
    	mBtnAlarm.setPrefSize(size, size);
    	mBtnAlarm.setMaxSize(size, size);
    	mBtnAlarm.setMinSize(size, size);
    	mBtnAlarm.setMouseTransparent(false);
    	mBtnAlarm.addEventFilter(MouseEvent.MOUSE_CLICKED, btnAlarmEnterEventHandler); //ActionEvent.ACTION
    	
    	//Priority
    	mBtnPriority.setPrefSize(size, size);
    	mBtnPriority.setMaxSize(size, size);
    	mBtnPriority.setMinSize(size, size);
    	mBtnPriority.addEventFilter(MouseEvent.MOUSE_CLICKED, btnPriorityEnterEventHandler);
    	
    }
    
    private void setImageViewAlarm(Long remainder, boolean isDone){
    	if(!isDone){
    		if(remainder > 0L){
    			mBtnAlarm.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_ENABLE_ALARM_CLASS);
        	}else{
        		mBtnAlarm.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_DISABLE_ALARM_CLASS);
        	}
    	}else{
    		mBtnAlarm.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_IS_DONE_ALARM_CLASS);
    	}
    	
    }
    
    private void setImageViewPriority(String lvlString){

    	switch (lvlString){
	    	case "1": //lowest
	    		mBtnPriority.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_PRIORITY_LOW_CLASS);
	    		break;
	    	case "2": //normal
	    		mBtnPriority.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_PRIORITY_NORMAL_CLASS);
	    		break;
	    	case "3": //highest
	    		mBtnPriority.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_PRIORITY_CRITICAL_CLASS);
	    		break;
	    		
	    	default: //either set nothing or error
	    		mBtnPriority.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IMAGE_CLASS, TASK_LIST_CELL_PRIORITY_DEFAULT_CLASS);
	    		break;
    	}
    }
    
    private void configureBoxColor(int type){
    	vBoxColor.setMaxWidth(12.0);
    	vBoxColor.setMinWidth(12.0);
    	vBoxColor.setPrefWidth(12.0);
    	vBoxColor.setAlignment(Pos.CENTER_LEFT);  	
    	
    	if(type == 1){
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_RED_CLASS);
    	}
    	
    	if (type == -1 || type == 0){
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_YELLOW_CLASS);
    	}
    	
    	if (type == -2 || type == 4){
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_TRANSPARENT_CLASS);
    	}
    	
    	if (type == 3){ //is done
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_GREEN_CLASS);
    	}
    	
    }
    
    
    private void configureCheckBoxIsDone(){
    	checkBox.setOnAction(eh);
    }
    
    private void configureVBoxCheckBox(){
    	vBoxCheckBox.setPadding(new Insets(0, 0, 0, 0));
    }
    
    private void configureLabelID(){
    	//System.out.println("type - " + type);
    	lblID.getStyleClass().addAll(TASK_LIST_CELL_ID_CLASS);
    	lblID.setAlignment(Pos.CENTER);
    }
    
    private void configureLabelTitle() {
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_TITLE_CLASS);
    	lblTitle.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void configureLabelDateTime() {
    	lblStartDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
    	lblEndDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
    	configureLabels(lblStartDateTime);
    	configureLabels(lblEndDateTime);  	
    }
    
    private void configureLabels(Label lbl){
    	lbl.setMaxWidth(180.0);
    	lbl.setMaxHeight(20.0);
    }
    
    private void strikeThroughLabels(){
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    	lblID.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    	lblStartDateTime.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    	lblEndDateTime.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    }
    
    private void isDoneLabel(){
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    	lblID.getStyleClass().add(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    }
    
    private void addControlsToVBox(){
    	vBoxDateTime.getChildren().add(lblEndDateTime);
    	vBoxDateTime.getChildren().add(lblStartDateTime);
    	vBoxDateTime.setAlignment(Pos.CENTER_RIGHT);
    	
    	vBoxCheckBox.getChildren().add(checkBox);
    	vBoxCheckBox.setAlignment(Pos.CENTER);
    }
    
    private void addControlsToGrid() {
    	//add(node, column index, row index)
    	grid.add(vBoxColor, 0, 0);
    	vBoxColor.setPadding(new Insets(0, 0, 0, 5));
    	
    	grid.add(vBoxCheckBox, 1, 0);
    	grid.add(lblID, 2, 0); 

        grid.add(lblTitle, 3, 0);
        grid.add(mBtnPriority, 4, 0);
        GridPane.setMargin(mBtnPriority, new Insets(5, 10, 5, 5));
        
        grid.add(mBtnAlarm, 5, 0);
        GridPane.setMargin(mBtnAlarm, new Insets(5, 10, 5, 5));
        grid.add(vBoxDateTime, 6, 0);
 
    }
    
    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
 
    private void addContent(Task mTask) {
        setText(null);
        //icon.setText(GeocachingIcons.getIcon(cache).toString());
        
        getID = String.valueOf(mTask.getID());
        getTitle = mTask.getTitle();
        getCategory = mTask.getCategory();
        getStartDate = mTask.getStartDate();
        getEndDate = mTask.getEndDate();
        getStartTime = mTask.getStartTime();
        getEndTime = mTask.getEndTime();
        getIsDone = mTask.getIsDone();
        getRemainder = mTask.getReminder();
        getPriority = mTask.getPriority();
        
        if(mTask.getTitle().length() > MAX_CHARACTER_IN_TITLE){
        	getTitle = getTitle.substring(0, MAX_CHARACTER_IN_TITLE) + "...";
        }
        
        lblTitle.setText(getTitle);
        lblID.setText(getID);
        checkBox.setId("checkBox_" + getID);
        checkBox.setSelected(getIsDone);
        
        if(getCategory.equals("timed")){
        	lblStartDateTime.setText(DISPLAY_START_DATE_TIME.replace("[datetime]", getStartDate + " " + getStartTime));
        	lblEndDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", getEndDate + " " + getEndTime));	
        	
        } else if(getCategory.equals("deadline")){
        	lblEndDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", getEndDate + " " + getEndTime));
        	
        } else if(getCategory.equals("floating")){
        	lblStartDateTime.setText("");
        	lblEndDateTime.setText("");
        }
        //setStyleClassDependingOnFoundState(mTask);  
        
        if(getIsDone){ //task is done or floating
        	strikeThroughLabels();
        	isDoneLabel();
        	configureBoxColor(3); //green
        	
        }else if (getCategory.equals("floating")){
        	configureBoxColor(4);
        	
        }else{
        	configureBoxColor(TokenValidation.compareWithCurrentDate(getEndDate, getEndTime));
        }
        
        setImageViewPriority(getPriority);
        setImageViewAlarm(getRemainder, getIsDone); //set alarm icon - enable or disable
        setCellBackground(getIsDone);
        setGraphic(grid);
		
    }
    
   
    /*
     * for switching classes
    private void setStyleClassDependingOnFoundState(Cache cache) {
    	
        if (CacheUtils.hasUserFoundCache(cache, new Long(3906456))) {
            addClasses(this, CACHE_LIST_FOUND_CLASS);
            removeClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
        } else {
            addClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
            removeClasses(this, CACHE_LIST_FOUND_CLASS);
        }
    }
    */
    
	@Override
	public void updateItem(Task mTask, boolean empty){
		super.updateItem(mTask,  empty);
		if(empty){
			clearContent();
		}else {		
			addContent(mTask);	
		}//end if 
	}//end updateItem
	
	
	EventHandler eh = new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
	    	boolean isUpdated = false;
	    	String markCommandFormatString = "mark [id] [type]";
	    	String markCommandEditedString = "";
	    	
	        if (event.getSource() instanceof CheckBox) {
	            CheckBox chk = (CheckBox) event.getSource();
	            
	            String getTaskID = chk.getId().replace("checkBox_", "").trim();
	            
	            if(chk.isSelected()){
	            	getIsDone = true; 
	            	markCommandEditedString = markCommandFormatString.replace("[id]", getTaskID).replace("[type]", DONE_COMMAND);
	            	
	            }else{
	            	getIsDone = false;
	            	markCommandEditedString = markCommandFormatString.replace("[id]", getTaskID).replace("[type]", NOT_DONE_COMMAND);
	            }
	            
	            isUpdated = executeCommand(markCommandEditedString);
	            if(isUpdated){
	            	setCellBackground(getIsDone);
	            }
	            /*
	            isUpdated = updateTaskDoneStatus(Integer.parseInt(getTaskID), getIsDone);
	            if(isUpdated){
	            	setCellBackground(getIsDone);
		            refreshUIControllerParentListView();
	            }
	            */
	            
	            
	        }//end if
	    }//end handle
	};
	
	
	EventHandler btnAlarmEnterEventHandler = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			System.out.println("Alarm Popover");
			showAlarmPopOver(getRemainder);
		}
	};
	
	
	EventHandler btnPriorityEnterEventHandler = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			System.out.println("Priority Popover");
			showPriorityPopOver(convertStringToInteger(getPriority));
		}
		
	};
	
	
	
	public void setUIParent(UIController ui){
		mUIParent = ui;
	}
	
	private boolean executeCommand(String input){
		String[] tokens = null;
		String parserOutput = "";
		String logicOutput = "";
		String command = "";
		
		MainParser mp = MainParser.validateInput(input);
		parserOutput = mp.getMessage();
		if(!parserOutput.equals(Constants.MESSAGE_VALID_INPUT)){
			setUIControllerParentTextStatus(parserOutput);
			return false;
		}
		
		command = mp.getCommand();
		tokens = mp.getTokens();
		
		logicOutput = Logic.runCommand(command, tokens);
		setUIControllerParentTextStatus(logicOutput);
		
		refreshUIControllerParentListView();
		
		return true;
		
	}
	
	private void setUIControllerParentTextStatus(String msg){
		if(mUIParent != null){
			mUIParent.setTextStatus(msg);
		}
	}
	
	private void refreshUIControllerParentListView(){
		if(mUIParent != null){
			mUIParent.initTaskListInListView();
		}
	}
	
	
	private void showPriorityPopOver(int selectedPriorityLvl){
		double popWidth = 300.0;
		double popHeight = 150.0;
		
		if(this.mPopOverPriority != null){
			if(this.mPopOverPriority.isShowing()){
				this.mPopOverPriority.setAutoHide(true);
				return;
			}
		}
		
		VBox vBox = new VBox(8);
		HBox hBox = new HBox(4);
		
		Label lblPopOverTitle = new Label("Priority Level");
		/*
		Label lblPriorityLevel = null;
		ImageView imgPriority = null;
		RadioButton radioBtn = null;
		*/
		Button btnPriorityChange = new Button("Change");
		
		String[] priorityCssArray = {POP_OVER_IMAGE_CRITICAL_CLASS, POP_OVER_IMAGE_NORMAL_CLASS, POP_OVER_IMAGE_LOW_CLASS, POP_OVER_IMAGE_DEFAULT_CLASS};
		String[] priorityArray = {"Critical", "Normal", "Low", "Default"};
		int[] priorityLvlArray = {3, 2, 1, 0};
		
		btnPriorityChange.setPrefWidth(popWidth);
		btnPriorityChange.getStyleClass().addAll(POP_OVER_BUTTON_CHANGE_CLASS);
		btnPriorityChange.addEventFilter(ActionEvent.ACTION, onBtnPriorityClick);
		
		lblPopOverTitle.getStyleClass().addAll(POP_OVER_TITLE_CLASS);
		lblPopOverTitle.setPrefWidth(popWidth);
		lblPopOverTitle.setTextAlignment(TextAlignment.CENTER);
		lblPopOverTitle.setAlignment(Pos.CENTER);
		
		//Set title
		vBox.getChildren().add(lblPopOverTitle);
		
		generatePriorityOptions(vBox, hBox, priorityArray, priorityLvlArray, priorityCssArray, selectedPriorityLvl);
		
		/*
		//Critical row
		imgPriority = new ImageView();
		setPopOverPriorityIconStyle(imgPriority, POP_OVER_IMAGE_CRITICAL_CLASS);
		lblPriorityLevel = new Label("Critical");
		setPopOverPriorityLabelStyle(lblPriorityLevel, POP_OVER_LABEL_PRIORITY_CLASS );
		radioBtn = new RadioButton();
		radioBtn.setToggleGroup(priorityGroup);
		radioBtn.setUserData(3);
		hBox = new HBox(4);
		hBox.getChildren().add(radioBtn);
		hBox.getChildren().add(imgPriority);
		hBox.getChildren().add(lblPriorityLevel);
		vBox.getChildren().add(hBox);
		
		//Normal row
		imgPriority = new ImageView();
		setPopOverPriorityIconStyle(imgPriority, POP_OVER_IMAGE_NORMAL_CLASS);
		lblPriorityLevel = new Label("Normal");
		setPopOverPriorityLabelStyle(lblPriorityLevel, POP_OVER_LABEL_PRIORITY_CLASS );
		radioBtn = new RadioButton();
		radioBtn.setToggleGroup(priorityGroup);
		radioBtn.setUserData(2);
		hBox = new HBox(4);
		hBox.getChildren().add(radioBtn);
		hBox.getChildren().add(imgPriority);
		hBox.getChildren().add(lblPriorityLevel);
		vBox.getChildren().add(hBox);
		
		//Low
		imgPriority = new ImageView();
		setPopOverPriorityIconStyle(imgPriority, POP_OVER_IMAGE_LOW_CLASS);
		lblPriorityLevel = new Label("Low");
		setPopOverPriorityLabelStyle(lblPriorityLevel, POP_OVER_LABEL_PRIORITY_CLASS );
		radioBtn = new RadioButton();
		radioBtn.setToggleGroup(priorityGroup);
		radioBtn.setUserData(1);
		hBox = new HBox(4);
		hBox.getChildren().add(radioBtn);
		hBox.getChildren().add(imgPriority);
		hBox.getChildren().add(lblPriorityLevel);
		vBox.getChildren().add(hBox);
		
		//Default
		imgPriority = new ImageView();
		setPopOverPriorityIconStyle(imgPriority, POP_OVER_IMAGE_DEFAULT_CLASS);
		lblPriorityLevel = new Label("Default");
		setPopOverPriorityLabelStyle(lblPriorityLevel, POP_OVER_LABEL_PRIORITY_CLASS);
		radioBtn = new RadioButton();
		radioBtn.setToggleGroup(priorityGroup);
		radioBtn.setUserData(0);
		hBox = new HBox(4);
		hBox.getChildren().add(radioBtn);
		hBox.getChildren().add(imgPriority);
		hBox.getChildren().add(lblPriorityLevel);
		vBox.getChildren().add(hBox);
		*/
		
		
		vBox.setPadding(new Insets(5,10,5,10)); //set padding
		VBox.setMargin(btnPriorityChange, new Insets(10,0,0,0)); //set margin of btn change
		vBox.getChildren().add(btnPriorityChange); //add btn change
		vBox.setPrefSize(popWidth, popHeight);
		
		vBox.getStyleClass().addAll(POP_OVER_CONTENT_AREA_CLASS);
		
		this.mPopOverPriority = new PopOver(vBox);
		this.mPopOverPriority.setHideOnEscape(true);
		this.mPopOverPriority.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
		this.mPopOverPriority.setAutoFix(true);
		this.mPopOverPriority.setAutoHide(true);
		this.mPopOverPriority.setDetachable(false);
		this.mPopOverPriority.show(mBtnPriority); 
    	
	}
	
	private void generatePriorityOptions(VBox grandParent, HBox parent, String[] priorityArray, int[] lvlArray, String[] cssArray, int selectedPriority){
		
		this.priorityGroup = new ToggleGroup();
		
		for(int i=0; i < priorityArray.length; i++){
			
			ImageView imgPriority = new ImageView();
			Label lblPriorityLevel = new Label(priorityArray[i]);
			RadioButton radioBtn = new RadioButton();
			
			setPopOverPriorityIconStyle(imgPriority, cssArray[i]);
			setPopOverPriorityLabelStyle(lblPriorityLevel, POP_OVER_LABEL_PRIORITY_CLASS);
			
			radioBtn.setToggleGroup(this.priorityGroup);
			radioBtn.setUserData(lvlArray[i]);
			
			if(lvlArray[i] == selectedPriority){
				radioBtn.setSelected(true);
			}
			
			parent = new HBox(4);
			parent.getChildren().add(radioBtn);
			parent.getChildren().add(imgPriority);
			parent.getChildren().add(lblPriorityLevel);
			grandParent.getChildren().add(parent);
			
		}
	
	}
	
	private void setPopOverPriorityIconStyle(ImageView iv, String cssClass){
		iv.setFitWidth(25.0);
		iv.setPreserveRatio(true);
		iv.setSmooth(true);
		iv.setCache(true);
		iv.getStyleClass().addAll(cssClass);
	}
	
	private void setPopOverPriorityLabelStyle(Label lb, String cssClass){
		lb.setAlignment(Pos.BASELINE_LEFT);
		lb.getStyleClass().addAll(cssClass);
	}
	
	
	private void showAlarmPopOver(Long alarmValue){
		double popWidth = 300.0;
		double popHeight = 150.0;
	    /*
		 * ---------------------
		 * |       Alarm       |
		 * ---------------------
		 * |[=================]|
		 * |            <Enter>|
		 * 
		 * 
		 * */
		if(this.mPopOverAlarm != null){
			if(this.mPopOverAlarm.isShowing()){
				this.mPopOverAlarm.setAutoHide(true);
				return;
			}
		}
		
		String getAlarmDate = MainParser.convertMillisecondToDate(alarmValue); //dd/MM/yyyy
		String getAlarmTime = MainParser.convertMillisecondToTime(alarmValue); //HH:mm
		
		VBox vBox = new VBox(8);
		HBox hDateBox = new HBox(4);
		HBox hTimeBox = new HBox(4);
		
		Label lblPopOverTitle = new Label("Alarm");
		Label lblDateTitle = new Label("Date:");
		Label lblTimeTitle = new Label("Time:");
		Label lblColon = new Label(":");
		this.txtHourField = new TextField();
		this.txtMinField = new TextField();
		Button btnAlarmChange = new Button("Change");
		
		final String pattern = "dd/MM/yyyy";
		this.datePicker = new DatePicker(LocalDate.of(convertStringToInteger(getAlarmDate.split("/")[2]), 
												convertStringToInteger(getAlarmDate.split("/")[1]), 
												convertStringToInteger(getAlarmDate.split("/")[0]))); //set year, month, day to datepicker
		this.datePicker.setPromptText(pattern.toLowerCase());

		
		
		
		this.datePicker.setConverter(new StringConverter<LocalDate>() {
		     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

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
		});
		
		this.datePicker.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				LocalDate date = datePicker.getValue();
				
				System.out.println("Selected date: " + datePicker.getConverter().toString(date));
			}
		    
		});

		//txtTitle.setId("pop-title");
		lblPopOverTitle.getStyleClass().addAll(POP_OVER_TITLE_CLASS);
		lblPopOverTitle.setPrefWidth(popWidth);
		lblPopOverTitle.setTextAlignment(TextAlignment.CENTER);
		lblPopOverTitle.setAlignment(Pos.CENTER);
		
		lblDateTitle.setPrefWidth(50.0);
		lblDateTitle.setAlignment(Pos.CENTER_RIGHT);
		lblTimeTitle.setPrefWidth(50.0);
		lblTimeTitle.setAlignment(Pos.CENTER_RIGHT);
		
		
		this.txtHourField.setText(getAlarmTime.split(":")[0]); // Hour
		this.txtMinField.setText(getAlarmTime.split(":")[1]); // Minute
		
		this.txtHourField.setPrefWidth(60.0);
		this.txtMinField.setPrefWidth(60.0);
		
		btnAlarmChange.setPrefWidth(popWidth);
		btnAlarmChange.getStyleClass().addAll(POP_OVER_BUTTON_CHANGE_CLASS);
		btnAlarmChange.addEventFilter(ActionEvent.ACTION, onBtnAlarmClick);
		
		
		
		hDateBox.getChildren().add(lblDateTitle); //Datee:
		hDateBox.getChildren().add(this.datePicker); // datepicker
		hTimeBox.getChildren().add(lblTimeTitle); //Time:
		hTimeBox.getChildren().add(this.txtHourField); //hour
		hTimeBox.getChildren().add(lblColon); // :
		hTimeBox.getChildren().add(this.txtMinField); //min
		
		
		VBox.setMargin(btnAlarmChange, new Insets(10,0,0,0));
		vBox.setPadding(new Insets(5,10,5,10));
		vBox.getChildren().add(lblPopOverTitle);
		vBox.getChildren().add(hDateBox);
		vBox.getChildren().add(hTimeBox);
		vBox.getChildren().add(btnAlarmChange);
		vBox.setPrefSize(popWidth, popHeight);
		vBox.getStyleClass().addAll(POP_OVER_CONTENT_AREA_CLASS);
    	
		
		this.mPopOverAlarm = new PopOver(vBox);
		this.mPopOverAlarm.setHideOnEscape(true);
		this.mPopOverAlarm.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
		this.mPopOverAlarm.setAutoFix(true);
		this.mPopOverAlarm.setAutoHide(true);
		this.mPopOverAlarm.setDetachable(false);
    	this.mPopOverAlarm.show(this.mBtnAlarm); 
    	
	}
	
	
	private EventHandler onBtnPriorityClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(priorityGroup != null){
        		System.out.println(priorityGroup.getSelectedToggle().getUserData().toString());
        	}
        }
    };
    
    private EventHandler onBtnAlarmClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(datePicker != null && txtHourField != null && txtMinField != null){
        		LocalDate date = datePicker.getValue();
				
        		System.out.println(datePicker.getConverter().toString(date) + " - " + txtHourField.getText() + " - " + txtMinField.getText());
        	}
        	
        }
    };
	
	private int convertStringToInteger(String s){
		return Integer.parseInt(s);
	}
	/*
	private boolean updateTaskDoneStatus(int id, boolean newDone){
		boolean isUpdated = Logic.updateTaskIsDone(id, newDone);
		return isUpdated;
	}
	*/
	
}
