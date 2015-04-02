package pista.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.controlsfx.control.PopOver;
import org.omg.CORBA.Environment;

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
    /*
    private final String TASK_LIST_CELL_BOX_COLOR_CLASS = "-fx-effect: dropshadow( three-pass-box, rgba(0,0,0,0.4), 2, 0.7, 0, 0);";
    private final String TASK_LIST_CELL_BOX_RED_CLASS = "-fx-background-color: #FF4444;";
    private final String TASK_LIST_CELL_BOX_YELLOW_CLASS = "-fx-background-color: #FFBB33;";
    private final String TASK_LIST_CELL_BOX_GREEN_CLASS = "-fx-background-color: #99CC00;";
    private final String TASK_LIST_CELL_BOX_TRANSPARENT_CLASS = "-fx-background-color: transparent;";
    */
    
    private final String TASK_LIST_CELL_TITLE_CLASS = "task-list-cell-title";
    private final String TASK_LIST_CELL_DATETIME_CLASS = "task-list-cell-datetime";
    
    private final String TASK_LIST_CELL_BUTTON_IS_DONE_CLASS = "btn-is-done";
    private final String BUTTON_IS_DONE_UNSET_CLASS = "btn-is-done-unset";
    private final String BUTTON_IS_DONE_SET_CLASS = "btn-is-done-set";
    
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
    private final String POP_OVER_TIME_TIP_CLASS = "pop-label-time-tip";
    private final String POP_OVER_LABEL_PRIORITY_CLASS = "pop-label-priority";
    private final String POP_OVER_LABEL_ERROR_MESSAGE_CLASS = "pop-label-error-message";
    private final String POP_OVER_LABEL_CORRECT_MESSAGE_CLASS = "pop-label-correct-message";
    private final String POP_OVER_IMAGE_CRITICAL_CLASS = "pop-over-img-critical";
    private final String POP_OVER_IMAGE_NORMAL_CLASS = "pop-over-img-normal";
    private final String POP_OVER_IMAGE_LOW_CLASS = "pop-over-img-low";
    private final String POP_OVER_IMAGE_DEFAULT_CLASS = "pop-over-img-default";
    private final String POP_OVER_BUTTON_CHANGE_CLASS = "btn-change";
    
    private final String POP_OVER_INVALID_ALARM_TIME_MESSAGE = "Invalid Time";
    private final String POP_OVER_INVALID_ALARM_DATE_MESSAGE = "Invalid Date";
    private final String POP_OVER_SUCCESS_ALARM_MESSAGE = "Updated";
    private final String POP_OVER_SUCCESS_PRIORITY_MESSAGE = "Updated";
    
    private final int MAX_CHARACTER_IN_TITLE = 50;
    
    private String DISPLAY_START_DATE_TIME = "From [datetime]"; 
    private String DISPLAY_END_DATE_TIME = "To [datetime]"; 
    
    private final String DONE_COMMAND = "-" + Constants.MARK_DONE; //-done
    private final String NOT_DONE_COMMAND = "-" + Constants.MARK_UNDONE; //-undone
    
	private GridPane grid = new GridPane();
	private VBox vBoxDateTime = new VBox(0);
	private VBox vBoxButtonIsDone = new VBox(0);
	
    //private Label icon = new Label();
	private Label lblID = new Label();
    private Label lblTitle = new Label();
    private Label lblDateTime = new Label();
    //private Label lblEndDateTime = new Label();
    //private CheckBox checkBox = new CheckBox();
    private Button btnIsDone = new Button();
    private UIController mUIParent = null;
    private VBox vBoxColor = new VBox(0);
    
    private Button mBtnAlarm = new Button();
    private Button mBtnPriority = new Button();
    
    private PopOver mPopOverAlarm = null;
    private PopOver mPopOverPriority = null;
    
	TextField txtPopOverAlarmHourField = null;
	TextField txtPopOverAlarmMinField = null;
	DatePicker datePickerPopOverAlarm = null;
	Label lblPopOverAlarmMessage = null;
	Label lblPopOverPriorityMessage = null;
    
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
    
    private String editPriorityCommand = "priority [id] -[level]";
    private String editAlarmCommand = "remind [id] -[start_date] -[start_time]";
    private String offAlarmCommand = "remind [id] -off";
        	
    private final double PREF_GRID_HEIGHT = 70.0;
    
    public TaskListCell() {
    	super();
        configureGrid(); 
        configureButtonIsDone();
        configureButtonImage();
        configureLabelID();
        configureLabelTitle();
        configureLabelDateTime();
        configureVBoxCheckBox();
        addControlsToVBox();
        addControlsToGrid();            
    }
    
    private void configureGrid() {
        this.grid.setHgap(0);
        this.grid.setVgap(0);
        this.grid.setPadding(new Insets(0, 0, 0, 0));
        this.grid.setMaxHeight(PREF_GRID_HEIGHT);
        this.grid.setMinHeight(PREF_GRID_HEIGHT);
        this.grid.setPrefHeight(PREF_GRID_HEIGHT);

        //ColumnConstraints colBoxColor = new ColumnConstraints(); //color box
        //colBoxColor.setPercentWidth(2);
        //colBoxColor.setHalignment(HPos.LEFT);
        
        ColumnConstraints colCheckBox = new ColumnConstraints(); //check box
        colCheckBox.setPercentWidth(8); //3
        colCheckBox.setHalignment(HPos.CENTER);
        
        ColumnConstraints colID = new ColumnConstraints(); //ID
        colID.setPercentWidth(5);
        colID.setHalignment(HPos.CENTER);
        
        ColumnConstraints colTitle = new ColumnConstraints(); //title
        colTitle.setPercentWidth(57);
        colTitle.setHalignment(HPos.LEFT);
        
        ColumnConstraints colPriority = new ColumnConstraints(); // priority
        colPriority.setPercentWidth(5);
        colPriority.setHalignment(HPos.RIGHT);
        
        ColumnConstraints colAlarm = new ColumnConstraints(); // alarm
        colAlarm.setPercentWidth(5);
        colAlarm.setHalignment(HPos.RIGHT);
        
        ColumnConstraints colDateTime = new ColumnConstraints(); //date time
        colDateTime.setPercentWidth(20);
        colDateTime.setHalignment(HPos.RIGHT);
        
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setPrefHeight(70.0);
        this.grid.getRowConstraints().addAll(row1);
        
        //colBoxColor
        this.grid.getColumnConstraints().addAll(colCheckBox, colID, colTitle, colPriority, colAlarm, colDateTime);
        
    }
    
    private void setCellBackground(boolean isDone){
    	getStyleClass().removeAll(TASK_LIST_CELL_BACKGROUND_IS_DONE_CLASS, TASK_LIST_CELL);
   
    	if(isDone){
    		getStyleClass().add(TASK_LIST_CELL_BACKGROUND_IS_DONE_CLASS);
    	}else{
    		getStyleClass().add(TASK_LIST_CELL);
    	}
    }
    
    private void configureButtonImage(){
    	//Alarm
    	double size = 25.0;
    	mBtnAlarm.setPrefSize(size, size);
    	mBtnAlarm.setMaxSize(size, size);
    	mBtnAlarm.setMinSize(size, size);
    	mBtnAlarm.addEventFilter(MouseEvent.MOUSE_CLICKED, btnAlarmEnterEventHandler); //ActionEvent.ACTION
    	
    	//Priority
    	mBtnPriority.setPrefSize(size, size);
    	mBtnPriority.setMaxSize(size, size);
    	mBtnPriority.setMinSize(size, size);
    	mBtnPriority.addEventFilter(MouseEvent.MOUSE_CLICKED, btnPriorityEnterEventHandler);
    	
    }
    
    private void setImageViewAlarm(Long remainder, boolean isDone){
    	mBtnAlarm.getStyleClass().removeAll(TASK_LIST_CELL_ENABLE_ALARM_CLASS);
    	mBtnAlarm.getStyleClass().removeAll(TASK_LIST_CELL_DISABLE_ALARM_CLASS);
    	mBtnAlarm.getStyleClass().removeAll(TASK_LIST_CELL_IS_DONE_ALARM_CLASS);

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
    	mBtnPriority.getStyleClass().removeAll(TASK_LIST_CELL_PRIORITY_LOW_CLASS, TASK_LIST_CELL_PRIORITY_NORMAL_CLASS, 
    										TASK_LIST_CELL_PRIORITY_CRITICAL_CLASS, TASK_LIST_CELL_PRIORITY_DEFAULT_CLASS);
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
/*
    	this.vBoxColor.setMaxWidth(12.0);
    	this.vBoxColor.setMinWidth(12.0);
    	this.vBoxColor.setPrefWidth(12.0);
    	this.vBoxColor.setAlignment(Pos.CENTER_LEFT);  	
    	
    	this.vBoxColor.getStyleClass().removeAll(TASK_LIST_CELL_BOX_RED_CLASS,
    			TASK_LIST_CELL_BOX_YELLOW_CLASS, TASK_LIST_CELL_BOX_TRANSPARENT_CLASS, TASK_LIST_CELL_BOX_GREEN_CLASS);
    	
    	
    	if(type == 1){
    		this.vBoxColor.getStyleClass().addAll(this.TASK_LIST_CELL_BOX_RED_CLASS);
    	}
    	
    	if (type == -1 || type == 0){
    		this.vBoxColor.getStyleClass().addAll(this.TASK_LIST_CELL_BOX_YELLOW_CLASS);
    	}
    	
    	if (type == -2 || type == 4){
    		this.vBoxColor.getStyleClass().addAll(this.TASK_LIST_CELL_BOX_TRANSPARENT_CLASS);

    	}
    	
    	if (type == 3){ //is done
    		this.vBoxColor.getStyleClass().addAll(this.TASK_LIST_CELL_BOX_GREEN_CLASS);
    	}
*/    	
    }
    
    //private void 
    
    
    private void configureButtonIsDone(){
    	//this.checkBox.setOnAction(eh);
    	this.btnIsDone.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMarkIsDone);
    	this.btnIsDone.setPrefSize(70.0, PREF_GRID_HEIGHT - 4.0);
    } 
    
    private void configureVBoxCheckBox(){
    	this.vBoxButtonIsDone.setPadding(new Insets(0, 0, 0, 0));
    }
    
    private void configureLabelID(){
    	//System.out.println("type - " + type);
    	this.lblID.getStyleClass().addAll(TASK_LIST_CELL_ID_CLASS);
    	this.lblID.setAlignment(Pos.CENTER);
    }
    
    private void configureLabelTitle() {
    	this.lblTitle.getStyleClass().add(TASK_LIST_CELL_TITLE_CLASS);
    	this.lblTitle.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void configureLabelDateTime() {
    	this.lblDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
        this.lblDateTime.setWrapText(true);
    	configureLabels(this.lblDateTime);	
    }
    
    private void configureLabels(Label lbl){
    	lbl.setMaxWidth(180.0);
    }
    
    private void strikeThroughLabels(boolean isDone){
    	if(isDone){
    		this.lblTitle.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
        	this.lblID.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
        	this.lblDateTime.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    	}else{
    		this.lblTitle.getStyleClass().remove(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    		this.lblID.getStyleClass().remove(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
        	this.lblDateTime.getStyleClass().remove(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    	}
    	
    }
    
    private void isDoneLabel(boolean isDone){
    	this.lblTitle.getStyleClass().removeAll(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    	this.lblID.getStyleClass().removeAll(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    	
    	if(isDone){
    		this.lblTitle.getStyleClass().add(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
        	this.lblID.getStyleClass().add(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    	}
    }
    
    private void setButtonIsDone(boolean isDone){
    	this.btnIsDone.getStyleClass().addAll(TASK_LIST_CELL_BUTTON_IS_DONE_CLASS);
    	this.btnIsDone.getStyleClass().removeAll(BUTTON_IS_DONE_SET_CLASS, BUTTON_IS_DONE_UNSET_CLASS);
  
    	if(isDone){ //is done
    		this.btnIsDone.getStyleClass().addAll(BUTTON_IS_DONE_SET_CLASS);
    	}else{
    		this.btnIsDone.getStyleClass().addAll(BUTTON_IS_DONE_UNSET_CLASS);
    	}
    	
    }
    
    
    private void addControlsToVBox(){
    	this.vBoxDateTime.getChildren().add(this.lblDateTime);
    	this.vBoxDateTime.setAlignment(Pos.CENTER_RIGHT);
    	
    	this.vBoxButtonIsDone.getChildren().add(this.btnIsDone);
    	this.vBoxButtonIsDone.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void addControlsToGrid() {
    	//add(node, column index, row index)
    	//this.grid.add(this.vBoxColor, 0, 0);
    	//this.vBoxColor.setPadding(new Insets(0, 0, 0, 5));
    	
    	this.grid.add(this.vBoxButtonIsDone, 0, 0);
    	GridPane.setMargin(this.vBoxButtonIsDone,new Insets(0,0,0,-5));
    	this.grid.add(lblID, 1, 0); 

    	this.grid.add(this.lblTitle, 2, 0);
    	this.grid.add(this.mBtnPriority, 3, 0);
        GridPane.setMargin(this.mBtnPriority, new Insets(5, 10, 5, 5));
        
        this.grid.add(this.mBtnAlarm, 4, 0);
        GridPane.setMargin(this.mBtnAlarm, new Insets(5, 10, 5, 5));
        this.grid.add(this.vBoxDateTime, 5, 0);
 
    }
    
    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
 
    private void addContent(Task mTask) {
        setText(null);

        this.getID = String.valueOf(mTask.getID());
        this.getTitle = mTask.getTitle();
        this.getCategory = mTask.getCategory();
        this.getStartDate = mTask.getStartDate();
        this.getEndDate = mTask.getEndDate();
        this.getStartTime = mTask.getStartTime();
        this.getEndTime = mTask.getEndTime();
        this.getIsDone = mTask.getIsDone();
        this.getRemainder = mTask.getReminder();
        this.getPriority = mTask.getPriority();
        
        if(mTask.getTitle().length() > MAX_CHARACTER_IN_TITLE){
        	this.getTitle = this.getTitle.substring(0, MAX_CHARACTER_IN_TITLE) + "...";
        }
        
        this.lblTitle.setText(this.getTitle);
        this.lblID.setText(this.getID);
        this.btnIsDone.setUserData(this.getIsDone);
        //this.checkBox.setId("checkBox_" + this.getID);
        //this.checkBox.setSelected(this.getIsDone);
        
        if(this.getCategory.equals("timed")){
        	this.lblDateTime.setText(DISPLAY_START_DATE_TIME.replace("[datetime]", this.convertToFullDateString(getStartDate) + " " + getStartTime) + "\n" +
        							DISPLAY_END_DATE_TIME.replace("[datetime]", this.convertToFullDateString(getEndDate) + " " + getEndTime));
        	
        } else if(this.getCategory.equals("deadline")){
        	this.lblDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", convertToFullDateString(getEndDate) + " " + getEndTime));
        	
        } else if(this.getCategory.equals("floating")){
        	this.lblDateTime.setText("");
        	
        }
        
        if(this.getIsDone){ //task is done or floating
        	configureBoxColor(3); //green
        }else{
        	if (this.getCategory.equals("floating")){
            	configureBoxColor(4);	
            }else{
            	configureBoxColor(TokenValidation.compareWithCurrentDate(getEndDate, getEndTime));
            }
        }
        
        setButtonIsDone(this.getIsDone);
        strikeThroughLabels(this.getIsDone); //if is done, label will be strike out
        isDoneLabel(this.getIsDone); 
        setImageViewPriority(this.getPriority); //set different color for different priority
        setImageViewAlarm(this.getRemainder, this.getIsDone); //set alarm icon - enable or disable
        setCellBackground(this.getIsDone); //if is done, background will be darker
        setGraphic(this.grid); //add grid to cell graphic
		
    }
    
    
	@Override
	public void updateItem(Task mTask, boolean empty){
		super.updateItem(mTask,  empty);
		if(empty || mTask == null){
			clearContent();
			return;
		}else {		
			addContent(mTask);
		}//end if 
	}//end updateItem
	
	
	EventHandler eventHandlerMarkIsDone = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
	    	boolean isUpdated = false;
	    	String markCommandFormatString = "mark [id] [type]";
	    	String markCommandEditedString = "";
	    	
	    	if(btnIsDone.getUserData() instanceof Boolean){
	    		boolean getCurrentStatus = (boolean) btnIsDone.getUserData();
	    		System.out.println(getCurrentStatus);
	    	}
            
            /*
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
       */
            
	    }//end handle
	};
	
	
	private String convertToFullDateString(String rawDate){
		//convert from dd/MM/yyyy to dd MMM yyyy
	    SimpleDateFormat oldFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat newWithYearFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat newWithoutYearFormat2 = new SimpleDateFormat("dd MMM");
        
        int rawYear = convertStringToInteger(rawDate.split("/")[2]);
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        
        String newDate = "";
        try {
        	if(rawYear == nowYear){
        		newDate = newWithoutYearFormat2.format(oldFormat.parse(rawDate));
        	}else{
        		newDate = newWithYearFormat.format(oldFormat.parse(rawDate));
        	}
        	
			return newDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-";
		}
        
	}
	
	
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
		double popHeight = 250.0;
		
		if(this.mPopOverPriority != null){
			if(this.mPopOverPriority.isShowing()){
				this.mPopOverPriority.setAutoHide(true);
				return;
			}
		}
		
		VBox vBox = new VBox(8);
		HBox hBox = new HBox(4);
		
		Label lblPopOverTitle = new Label("Priority Level");
		
		this.lblPopOverPriorityMessage = new Label();
		setPopOverLabelMessageStyle(lblPopOverPriorityMessage, popWidth); //set message style
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
		btnPriorityChange.addEventFilter(ActionEvent.ACTION, onBtnPriorityChangeClick); //set click method listener
		
		lblPopOverTitle.getStyleClass().addAll(POP_OVER_TITLE_CLASS);
		lblPopOverTitle.setPrefWidth(popWidth);
		lblPopOverTitle.setTextAlignment(TextAlignment.CENTER);
		lblPopOverTitle.setAlignment(Pos.CENTER);
		
		//Set title
		vBox.getChildren().add(lblPopOverTitle);
		
		generatePriorityOptions(vBox, hBox, priorityArray, priorityLvlArray, priorityCssArray, selectedPriorityLvl);
		
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
	
	private void setPopOverLabelMessageStyle(Label lbl, double width){
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setAlignment(Pos.CENTER);
		lbl.setPrefWidth(width);
		lbl.setPrefHeight(30.0);
		setPopOverLabelMessageVisible(lbl, false, false);
	}
	
	private void setPopOverLabelMessageVisible(Label lbl, boolean isValid, boolean isVisible){
		
		//lbl.getStyleClass().removeAll(POP_OVER_LABEL_ERROR_MESSAGE_CLASS, POP_OVER_LABEL_CORRECT_MESSAGE_CLASS); //reset
		if(isValid){ //valid, green background
			lbl.getStyleClass().addAll(POP_OVER_LABEL_CORRECT_MESSAGE_CLASS);
			
		}else{//invalid, red background
			lbl.getStyleClass().addAll(POP_OVER_LABEL_ERROR_MESSAGE_CLASS);
			
		}
		
		lbl.setVisible(isVisible);
	}
	
	private void setPopOverLabelMessageText(Label lbl, String msg){
		lbl.setText(msg);
	}

	
	private void showAlarmPopOver(Long alarmValue){
		double popWidth = 300.0;
		double popHeight = 200.0;
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
		
		String defaultDate = "01/01/1970";
		String defaultHour = "07";
		String defaultMin = "30";
		
		VBox vBox = new VBox(8);
		HBox hDateBox = new HBox(4);
		HBox hTimeBox = new HBox(4);
		
		Label lblPopOverTitle = new Label("Alarm");
		Label lblDateTitle = new Label("Date:");
		Label lblTimeTitle = new Label("Time:");
		Label lblColon = new Label(":");
		Label lblTimeTip  = new Label("(24 hrs format)");
		this.lblPopOverAlarmMessage = new Label();
		
		this.txtPopOverAlarmHourField = new TextField();
		this.txtPopOverAlarmMinField = new TextField();
		Button btnAlarmChange = new Button("Change");

		this.datePickerPopOverAlarm = new DatePicker(); 
		this.datePickerPopOverAlarm.setPromptText("01 January 2015");
		
		if(!getAlarmDate.equals(defaultDate)){
			this.datePickerPopOverAlarm.setValue(LocalDate.of(convertStringToInteger(getAlarmDate.split("/")[2]), 
									convertStringToInteger(getAlarmDate.split("/")[1]), 
									convertStringToInteger(getAlarmDate.split("/")[0]))); //set year, month, day to datepicker
		}
		
		
		this.datePickerPopOverAlarm.setConverter(new StringConverter<LocalDate>() {
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
			
		});
		
		this.datePickerPopOverAlarm.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				LocalDate date = datePickerPopOverAlarm.getValue();		
				System.out.println("Selected date: " + convertDateToStorageFormat(date) + " => " + datePickerPopOverAlarm.getConverter().toString(date));
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
		
		if(!(getAlarmDate.equals(defaultDate) && getAlarmTime.split(":")[0].equals(defaultHour) && getAlarmTime.split(":")[1].equals(defaultMin))){
			this.txtPopOverAlarmHourField.setText(getAlarmTime.split(":")[0]); // Hour
			this.txtPopOverAlarmMinField.setText(getAlarmTime.split(":")[1]); // Minute
		}

		this.txtPopOverAlarmHourField.setPrefWidth(60.0);
		this.txtPopOverAlarmHourField.setPromptText("HH");
		this.txtPopOverAlarmMinField.setPrefWidth(60.0);
		this.txtPopOverAlarmMinField.setPromptText("MM");
		
		btnAlarmChange.setPrefWidth(popWidth);
		btnAlarmChange.getStyleClass().addAll(POP_OVER_BUTTON_CHANGE_CLASS);
		btnAlarmChange.addEventFilter(ActionEvent.ACTION, onBtnAlarmChangeClick);
		
		lblTimeTip.getStyleClass().addAll(this.POP_OVER_TIME_TIP_CLASS); //set tip style (after the minute input)
		
		setPopOverLabelMessageStyle(lblPopOverAlarmMessage, popWidth); //set message style
		
		hDateBox.getChildren().add(lblDateTitle); //Datee:
		hDateBox.getChildren().add(this.datePickerPopOverAlarm); // datepicker
		hTimeBox.getChildren().add(lblTimeTitle); //Time:
		hTimeBox.getChildren().add(this.txtPopOverAlarmHourField); //hour
		hTimeBox.getChildren().add(lblColon); // :
		hTimeBox.getChildren().add(this.txtPopOverAlarmMinField); //min
		hTimeBox.getChildren().add(lblTimeTip); //time tip
		HBox.setMargin(lblTimeTip, new Insets(10,0,0,0));
		
		
		VBox.setMargin(btnAlarmChange, new Insets(10,0,0,0));
		vBox.setPadding(new Insets(5,10,5,10));
		vBox.getChildren().add(lblPopOverTitle);
		vBox.getChildren().add(hDateBox);
		vBox.getChildren().add(hTimeBox);
		vBox.getChildren().add(btnAlarmChange);
		vBox.getChildren().add(this.lblPopOverAlarmMessage);
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
	
	
	private EventHandler onBtnPriorityChangeClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(priorityGroup != null){
        		//priority command e.g. = priority id- 0/1/2/3
        		String newLevel = priorityGroup.getSelectedToggle().getUserData().toString();
        		String priorityCommand = editPriorityCommand.replace("[id]", getID).replace("[level]", newLevel);
        		
        		//Execute command
        		MainParser mp = MainParser.validateInput(priorityCommand); // since format is confirm correct, do not need to check again
        		String command = mp.getCommand(); //get command which is remind
        		String[] tokens = mp.getTokens(); //get tokens which is either off or startdate + starttime
        		
        		String logicOutput = Logic.runCommand(command, tokens);
        		
        		setUIControllerParentTextStatus(logicOutput); // show the status in UIController
        		refreshUIControllerParentListView(); //refresh listview in UIController
        		
        		setPopOverLabelMessageVisible(lblPopOverPriorityMessage, true, true); //show error message
    			setPopOverLabelMessageText(lblPopOverPriorityMessage, POP_OVER_SUCCESS_PRIORITY_MESSAGE);
    			

        	}
        }
    };
    
    private EventHandler onBtnAlarmChangeClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(datePickerPopOverAlarm != null && txtPopOverAlarmHourField != null && txtPopOverAlarmMinField != null){
        		LocalDate date = datePickerPopOverAlarm.getValue();
				
        		//edit alarm command = remind id -sd -st
        		//off alarm command  = remind id -off
        		String newDate = convertDateToStorageFormat(date);
        		String newHour = txtPopOverAlarmHourField.getText();
        		String newMinute = txtPopOverAlarmMinField.getText();
        		String alarmCommand = "";
        		
        		if(!(isValidHour(newHour) && isValidMinute(newMinute))){ //check is valid for hour and minute
        			setPopOverLabelMessageVisible(lblPopOverAlarmMessage, false, true); //show error message
        			setPopOverLabelMessageText(lblPopOverAlarmMessage, POP_OVER_INVALID_ALARM_TIME_MESSAGE);
        			return;
        		}
        		
        		alarmCommand = offAlarmCommand.replace("[id]", getID);
        		
        		if(!(newDate.isEmpty() || newDate.equals(""))){ //if date is not empty, assume off
        			alarmCommand = editAlarmCommand.replace("[id]", getID).replace("[start_date]", newDate)
        											.replace("[start_time]", addLeadingZero(convertTimeEmptyToValue(newHour)) + ":" + 
        																		addLeadingZero(convertTimeEmptyToValue(newMinute)));
        		}else{
        			//date is empty, error
        			setPopOverLabelMessageVisible(lblPopOverAlarmMessage, false, true); //show error message
        			setPopOverLabelMessageText(lblPopOverAlarmMessage, POP_OVER_INVALID_ALARM_DATE_MESSAGE);
        			return;
        		}
        		

        		System.out.println(alarmCommand);
        		
        		//Execute command
        		MainParser mp = MainParser.validateInput(alarmCommand); // since format is confirm correct, do not need to check again
        		String command = mp.getCommand(); //get command which is remind
        		String[] tokens = mp.getTokens(); //get tokens which is either off or startdate + starttime
        		
        		System.out.println(tokens.length);
        		String logicOutput = Logic.runCommand(command, tokens);
        		
        		setUIControllerParentTextStatus(logicOutput); // show the status in UIController
        		refreshUIControllerParentListView(); //refresh listview in UIController
        		
        		setPopOverLabelMessageVisible(lblPopOverAlarmMessage, true, true); //show error message
    			setPopOverLabelMessageText(lblPopOverAlarmMessage, POP_OVER_SUCCESS_ALARM_MESSAGE);
    			
        	}
        	
        }
    };
    
    
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
	
    private String convertDateToStorageFormat(LocalDate rawDate){
    	String datePatternForStorage = "d/M/yyyy"; //e.g. 18/4/2015
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePatternForStorage);
		
		if(rawDate == null){
			return "";
		}
		
		return dateFormatter.format(rawDate);
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
