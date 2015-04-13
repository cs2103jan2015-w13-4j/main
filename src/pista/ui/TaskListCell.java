//@author A0125474E
package pista.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import pista.Constants;
import pista.logic.Logic;
import pista.logic.Task;
import pista.parser.MainParser;
import pista.parser.TokenValidation;

public class TaskListCell extends ListCell<Task> {
	private Logic mLogic = null;

    private String DISPLAY_START_DATE_TIME = "From [datetime]"; 
    private String DISPLAY_END_DATE_TIME = "To [datetime]";
    private final int MAX_CHARACTER_IN_TITLE = 45;
    private final double PREF_GRID_HEIGHT = 70.0;
    private final double BUTTON_IS_DONE_WIDTH = 70.0;
    private final double BUTTON_IS_DONE_HEIGHT = this.PREF_GRID_HEIGHT - 4.0;
    private final double BUTTON_IMAGE_WIDTH = 55.0;
    private final double BUTTON_IMAGE_HEIGHT = this.PREF_GRID_HEIGHT - 4.0;
    
    private UIController mUIParent = null;
	private GridPane grid = null;
	private VBox vBoxDateTime = new VBox();
	private VBox vBoxButtonIsDone = new VBox();
	
	private Label lblID = new Label();
    private Label lblTitle = new Label();
    private Label lblDateTime = new Label();
    
    private Button btnIsDone = new Button();
    private Button btnAlarm = new Button();
    private Button btnPriority = new Button();
    private Button btnEdit = new Button();
    private Button btnDelete = new Button();
    
    private PopOver mPopOverAlarm = null;
    private PopOver mPopOverPriority = null;
    private PopOver mPopOverEdit = null;
    
    private TextArea txtAreaPopOverEditTaskTitle = null;
    private TextField txtPopOverAlarmHourField = null;
    private TextField txtPopOverAlarmMinField = null;
    private TextField txtPopOverEditStartHourField = null;
    private TextField txtPopOverEditStartMinField = null;
    private TextField txtPopOverEditEndHourField = null;
    private TextField txtPopOverEditEndMinField = null; 
    private DatePicker datePickerPopOverEditStartDate = null;
    private DatePicker datePickerPopOverEditEndDate = null;
    private DatePicker datePickerPopOverAlarm = null;
    private Label lblPopOverAlarmMessage = null;
    private Label lblPopOverPriorityMessage = null;
    private Label lblPopOverEditMessage = null;
    private Label lblPopOverTimeTip  = null;
    private Label lblPopOverTitle = null;
    private Label lblPopOverDateTitle = null;
    private Label lblPopOverTimeTitle = null;
    private Label lblPopOverColon = null; 
    private ToggleGroup priorityGroup = null;
    
    private String getID = "";
    private String getTitle = "";
    private String getCategory = "";
    private String getStartDate = "";
    private String getEndDate = "";
    private String getStartTime = "";
    private Long getStartMillisecond = 0L;
    private Long getEndMillisecond = 0L;
    private String getEndTime = "";
    private boolean getIsDone = false;
    private Long getRemainder = 0L;
    private String getPriority = "";
    
    //Time format: HH:M
    //Date format: d/M/yyyy
    private String editPriorityCommand = "priority [id] -[level]";
    private String editAlarmCommand = "remind [id] -[start_date] -[start_time]";
    private String offAlarmCommand = "remind [id] -off";
    private String deleteCommand = "delete [id]";
    private String editTaskcommand = "edit [id] -[new_title] -[new_start_date] -[new_start_time] -[new_end_date] -[new_end_time]";
    

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
    
    /**Default constructor
     * **/
    public TaskListCell() {
    	super();
        initGrid(); 
        initButtonIsDone();
        initButtonImage();
        setButtonEditStyle();
        setButtonDeleteStyle();
        setLabelIDStyle(false);
        setLabelTitleStyle(false);
        setLabelDateTimeStyle(false);
        initVBoxIsDone();
        addControlsToVBoxDateTime();
        addControlsToVBoxIsDone();
        addControlsToGrid();            
    }
    
    /**This method set the parent of this cell which is UIController
     * **/
    public void setUIParent(UIController ui){
		mUIParent = ui;
	}
    
    /**Initialize Logic class in this class
     * **/
	public boolean initLogic(){
		try{
			this.mLogic = Logic.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

    /**This method configure the grid which is the skeleton for the cell
     * **/
    private void initGrid() {
    	//these total width is 100%
    	final double hGap = 0.0;
    	final double vGap = 0.0;
    	final double colMarkDonePercentWidth = 8.0;
        final double colIDPercentWidth = 5.0;
        final double colTitlePercentWidth = 42.0;
        final double colEditPercentWidth = 7.0;
        final double colPriorityPercentWidth = 7.0;
    	final double colAlarmPercentWidth = 7.0;
    	final double colDateTimePercentWidth = 20.0;
    	final double colDeletePercentWidth = 4.0;
    	
    	this.grid = new GridPane();
    	this.grid.setHgap(hGap);
        this.grid.setVgap(vGap);
        this.grid.setPadding(new Insets(0, 0, 0, 0));
        this.grid.setPrefHeight(PREF_GRID_HEIGHT);

        ColumnConstraints colMarkDone = new ColumnConstraints(); //check box
        colMarkDone.setPercentWidth(colMarkDonePercentWidth); // mark done/undone
        colMarkDone.setHalignment(HPos.CENTER);
        
        ColumnConstraints colID = new ColumnConstraints(); //ID
        colID.setPercentWidth(colIDPercentWidth);
        colID.setHalignment(HPos.CENTER);
        
        ColumnConstraints colTitle = new ColumnConstraints(); //title
        colTitle.setPercentWidth(colTitlePercentWidth);
        colTitle.setHalignment(HPos.LEFT);
        
        ColumnConstraints colEdit = new ColumnConstraints(); //Edit
        colEdit.setPercentWidth(colEditPercentWidth);
        colEdit.setHalignment(HPos.RIGHT);
        
        ColumnConstraints colPriority = new ColumnConstraints(); // priority
        colPriority.setPercentWidth(colPriorityPercentWidth);
        colPriority.setHalignment(HPos.CENTER);
        
        ColumnConstraints colAlarm = new ColumnConstraints(); // alarm
        colAlarm.setPercentWidth(colAlarmPercentWidth);
        colAlarm.setHalignment(HPos.LEFT);
        
        ColumnConstraints colDateTime = new ColumnConstraints(); //date time
        colDateTime.setPercentWidth(colDateTimePercentWidth);
        colDateTime.setHalignment(HPos.RIGHT);
        
        ColumnConstraints colDelete = new ColumnConstraints(); //delete
        colDelete.setPercentWidth(colDeletePercentWidth);
        colDelete.setHalignment(HPos.RIGHT);
        
        
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setPrefHeight(70.0);
        this.grid.getRowConstraints().addAll(row1);
        
        //colBoxColor
        this.grid.getColumnConstraints().addAll(colMarkDone, colID, colTitle, colEdit, colPriority, colAlarm, colDateTime, colDelete);  
    }
    
    /**This method close the pop over priority
	 * **/
    public boolean closePopOverPriority(){
    	if(this.mPopOverPriority != null){
    		if(this.mPopOverPriority.isShowing()){
    			this.mPopOverPriority.hide();
    			return true;
    		}
    	}
    	return false;
    }
    
    /**This method close the pop over alarm
   	 * **/
    public boolean closePopOverAlarm(){
    	if(this.mPopOverAlarm != null){
    		if(this.mPopOverAlarm.isShowing()){
    			this.mPopOverAlarm.hide();
    			return true;
    		}
    	}
    	return false;
    }
    
    /**This method close the pop over edit
   	 * **/
    public boolean closePopOverEdit(){
    	if(this.mPopOverEdit != null){
    		if(this.mPopOverEdit.isShowing()){
    			this.mPopOverEdit.hide();
    			return true;
    		}
    	}
    	return false;
    }
    
    /**This method set the cell background
     * **/
    private void setCellBackground(boolean isDone){
    	getStyleClass().removeAll(Constants.CSS_CELL_BACKGROUND_IS_DONE, Constants.CSS_CELL);
   
    	if(isDone){
    		getStyleClass().add(Constants.CSS_CELL_BACKGROUND_IS_DONE);
    	}else{
    		getStyleClass().add(Constants.CSS_CELL);
    	}
    }
    
    /**This method initialize button is done
     * **/
    private void initButtonIsDone(){
    	this.btnIsDone.addEventFilter(MouseEvent.MOUSE_CLICKED, btnIsDoneEventHandler);
    	this.btnIsDone.setPrefSize(this.BUTTON_IS_DONE_WIDTH, this.BUTTON_IS_DONE_HEIGHT);
    } 
    
    /**This method initialize buttons (e.g. Alarm, Priority, Edit, Delete)
     * **/
    private void initButtonImage(){
    	initButtonAlarm();
    	initButtonPriority();
    	initButtonEdit();
    	initButtonDelete();
    }
    
    /**This method initialize button alarm
     * **/
    private void initButtonAlarm(){
    	this.btnAlarm.setPrefSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnAlarm.setMinSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnAlarm.setMaxSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnAlarm.addEventFilter(MouseEvent.MOUSE_CLICKED, this.btnShowAlarmEventHandler);
    }
    
    /**This method initialize button priority
     * **/
    private void initButtonPriority(){
    	this.btnPriority.setPrefSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnPriority.setMinSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnPriority.setMaxSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnPriority.addEventFilter(MouseEvent.MOUSE_CLICKED, this.btnShowPriorityEventHandler);
    }
    
    /**This method initialize button edit
     * **/
    private void initButtonEdit(){
    	this.btnEdit.setPrefSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnEdit.setMinSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnEdit.setMaxSize(BUTTON_IMAGE_WIDTH, BUTTON_IMAGE_HEIGHT);
    	this.btnEdit.addEventFilter(MouseEvent.MOUSE_CLICKED, this.btnPriorityEditEventHandler);
    }
    
    /**This method initialize button delete
     * **/
    private void initButtonDelete(){
    	double width = 30.0;
    	this.btnDelete.setPrefSize(width, BUTTON_IMAGE_HEIGHT);
    	this.btnDelete.setMinSize(width, BUTTON_IMAGE_HEIGHT);
    	this.btnDelete.setMaxSize(width, BUTTON_IMAGE_HEIGHT);
    	this.btnDelete.addEventFilter(MouseEvent.MOUSE_CLICKED, this.btnDeleteEventHandler);
    }
    
    /**This method init vertical box is done
     * **/
    private void initVBoxIsDone(){
    	this.vBoxButtonIsDone.setPadding(new Insets(0, 0, 0, 0));
    }
    
    /**This method set button alarm style
     * **/
    private void setButtonAlarmStyle(Long reminder, boolean isDone){
    	btnAlarm.getStyleClass().removeAll(Constants.CSS_BUTTON_ALARM_SET, Constants.CSS_BUTTON_ALARM_NOT_SET);
    	btnAlarm.getStyleClass().addAll(Constants.CSS_BUTTON_ALARM);

		if(reminder > 0L){
			btnAlarm.getStyleClass().addAll(Constants.CSS_BUTTON_ALARM_SET);
    	}else{
    		btnAlarm.getStyleClass().addAll(Constants.CSS_BUTTON_ALARM_NOT_SET);
    	}  
    }
    
    /**This method set button priority style
     * **/
    private void setButtonPriorityStyle(String lvlString){
    	this.btnPriority.getStyleClass().removeAll(Constants.CSS_PRIORITY_LOW, Constants.CSS_PRIORITY_NORMAL, 
    											Constants.CSS_PRIORITY_CRITICAL, Constants.CSS_PRIORITY_DEFAULT);
    	
    	this.btnPriority.getStyleClass().addAll(Constants.CSS_BUTTON_PRIORITY);
    	switch (lvlString){
	    	case "1": //lowest
	    		this.btnPriority.getStyleClass().addAll(Constants.CSS_PRIORITY_LOW);
	    		break;
	    	case "2": //normal
	    		this.btnPriority.getStyleClass().addAll(Constants.CSS_PRIORITY_NORMAL);
	    		break;
	    	case "3": //highest
	    		this.btnPriority.getStyleClass().addAll(Constants.CSS_PRIORITY_CRITICAL);
	    		break;
	    		
	    	default: //either set nothing or error
	    		this.btnPriority.getStyleClass().addAll(Constants.CSS_PRIORITY_DEFAULT);
	    		break;
    	}
    }
    
    /**This method set button delete style
     * **/
    private void setButtonDeleteStyle(){
    	this.btnDelete.getStyleClass().addAll(Constants.CSS_BUTTON_DELETE); 	
    }
    
    /**This method set button edit style
     * **/
    private void setButtonEditStyle(){
    	this.btnEdit.getStyleClass().addAll(Constants.CSS_BUTTON_EDIT);
    }
    
    /**This method set label ID style
     * **/
    private void setLabelIDStyle(boolean isDone){
    	this.lblID.getStyleClass().removeAll(Constants.CSS_CELL_TEXT_IS_DONE, Constants.CSS_CELL_TEXT_STRIKE_THROUGH);
    	this.lblID.getStyleClass().addAll(Constants.CSS_CELL_ID);
    	this.lblID.setAlignment(Pos.CENTER);
    	if(isDone){
    		this.lblID.getStyleClass().addAll(Constants.CSS_CELL_TEXT_IS_DONE, Constants.CSS_CELL_TEXT_STRIKE_THROUGH);
    	}
    }
    
    /**This method set label title style
     * **/
    private void setLabelTitleStyle(boolean isDone) {
    	this.lblTitle.getStyleClass().removeAll(Constants.CSS_CELL_TEXT_IS_DONE, Constants.CSS_CELL_TEXT_STRIKE_THROUGH);
    	this.lblTitle.getStyleClass().add(Constants.CSS_CELL_TITLE);
    	this.lblTitle.setAlignment(Pos.CENTER_LEFT);
    	if(isDone){
    		this.lblTitle.getStyleClass().addAll(Constants.CSS_CELL_TEXT_IS_DONE, Constants.CSS_CELL_TEXT_STRIKE_THROUGH);
    	}
    }
    
    /**This method set label date time style
     * **/
    private void setLabelDateTimeStyle(boolean isDone) {
    	double maxWidth = 180.0;
    	this.lblDateTime.setMaxWidth(maxWidth);
    	this.lblDateTime.getStyleClass().removeAll(Constants.CSS_CELL_TEXT_STRIKE_THROUGH);
    	this.lblDateTime.getStyleClass().add(Constants.CSS_CELL_DATETIME);
        this.lblDateTime.setWrapText(true);
        if(isDone){
        	this.lblDateTime.getStyleClass().addAll(Constants.CSS_CELL_TEXT_STRIKE_THROUGH);
        }
    }
    
    /**This method set button is done style
     * **/
    private void setButtonIsDoneStyle(boolean isDone){
    	this.btnIsDone.getStyleClass().addAll(Constants.CSS_BUTTON_IS_DONE);
    	this.btnIsDone.getStyleClass().removeAll(Constants.CSS_BUTTON_IS_DONE_SET, Constants.CSS_BUTTON_IS_DONE_UNSET);
  
    	if(isDone){ //is done
    		this.btnIsDone.getStyleClass().addAll(Constants.CSS_BUTTON_IS_DONE_SET);
    	}else{
    		this.btnIsDone.getStyleClass().addAll(Constants.CSS_BUTTON_IS_DONE_UNSET);
    	}
    }
    
    /**
     * This method set button is done background
     * **/
    private void setButtonIsDoneBackground(int type){
    	this.btnIsDone.getStyleClass().removeAll(Constants.CSS_BUTTON_IS_DONE_BACKGROUND_YELLOW,
    									Constants.CSS_BUTTON_IS_DONE_BACKGROUND_GREEN, Constants.CSS_BUTTON_IS_DONE_BACKGROUND_RED);
    	if(type == 1){
    		this.btnIsDone.getStyleClass().addAll(Constants.CSS_BUTTON_IS_DONE_BACKGROUND_RED);
    	}
    	
    	if (type == -1 || type == 0){
    		this.btnIsDone.getStyleClass().addAll(Constants.CSS_BUTTON_IS_DONE_BACKGROUND_YELLOW);
    	}

    	if (type == 3){ //is done
    		this.btnIsDone.getStyleClass().addAll(Constants.CSS_BUTTON_IS_DONE_BACKGROUND_GREEN);
    	}
    }
    
    /**
     * This method add controls to VBox date time
     * **/
    private void addControlsToVBoxDateTime(){
    	this.vBoxDateTime.getChildren().add(this.lblDateTime);
    	this.vBoxDateTime.setAlignment(Pos.CENTER_RIGHT);
    }
    
    /**
     * This method add controls to VBox date time
     * **/
    private void addControlsToVBoxIsDone(){
    	this.vBoxButtonIsDone.getChildren().add(this.btnIsDone);
    	this.vBoxButtonIsDone.setAlignment(Pos.CENTER_LEFT);
    }
    
    /**
     * This method add controls to main grid
     * **/
    private void addControlsToGrid() {
    	//add(node, column index, row index)
    	this.grid.add(this.vBoxButtonIsDone, 0, 0);
    	GridPane.setMargin(this.vBoxButtonIsDone,new Insets(0, 0, 0, -5));
    	this.grid.add(lblID, 1, 0); 
    	this.grid.add(this.lblTitle, 2, 0);
    	this.grid.add(this.btnEdit, 3, 0);
    	this.grid.add(this.btnPriority, 4, 0);
        this.grid.add(this.btnAlarm, 5, 0);
        this.grid.add(this.vBoxDateTime, 6, 0);
        this.grid.add(this.btnDelete, 7, 0);
        GridPane.setMargin(this.btnDelete,new Insets(0, 5, 0, 0));
    }
    
    /**
     * This method set button tool tip
     * **/
    private void setButtonTooltip(Button btn, String tooltipMsg){
    	btn.setTooltip(new Tooltip(tooltipMsg));
    }
    
    /**
     * This method clear content in task cell
     * **/
    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
 
    /**
     * This method add content to controls
     * **/
    private void addContent(Task mTask) {
    	String priorityToolTipMessage = "";
        String isDoneToolTipMessage = "";
        int currentTaskStatus = 0;
  
        this.clearContent();
        this.getID = String.valueOf(mTask.getID());
        this.getTitle = mTask.getTitle();
        this.getCategory = mTask.getCategory();
        this.getStartDate = mTask.getStartDate();
        this.getEndDate = mTask.getEndDate();
        this.getStartTime = mTask.getStartTime();
        this.getEndTime = mTask.getEndTime();
        this.getIsDone = mTask.getIsDone();
        this.getStartMillisecond = mTask.getStartMilliseconds();
        this.getEndMillisecond = mTask.getEndMilliseconds();
        this.getRemainder = mTask.getReminder();
        this.getPriority = mTask.getPriority();
        
        if(mTask.getTitle().length() > MAX_CHARACTER_IN_TITLE){
        	this.getTitle = this.getTitle.substring(0, MAX_CHARACTER_IN_TITLE) + "...";
        }
        
        this.lblTitle.setText(this.getTitle);
        this.lblID.setText(this.getID);
        this.btnIsDone.setUserData(this.getIsDone);

        if(this.getCategory.equals("timed")){
        	this.lblDateTime.setText(DISPLAY_START_DATE_TIME.replace("[datetime]", this.convertToFullDateString(this.getStartDate) + " " + this.getStartTime) + "\n" +
        							DISPLAY_END_DATE_TIME.replace("[datetime]", this.convertToFullDateString(this.getEndDate) + " " + this.getEndTime));
        	
        } else if(this.getCategory.equals("deadline")){
        	this.lblDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", convertToFullDateString(this.getEndDate) + " " + this.getEndTime));
        	
        } else if(this.getCategory.equals("floating")){
        	this.lblDateTime.setText("");
        	
        }
        
        if(this.getIsDone){ //task is done or floating
        	setButtonIsDoneBackground(3); //green
        	isDoneToolTipMessage = "Task is done";
        }else{
    		if (this.getCategory.equals("floating")){ //if category == float
        		setButtonIsDoneBackground(4);	
        		isDoneToolTipMessage = "Click to mark as done";
            }else{
            	currentTaskStatus = TokenValidation.compareWithCurrentDate(this.getEndMillisecond, this.getRemainder);
            	setButtonIsDoneBackground(currentTaskStatus);
            	
            	if(currentTaskStatus == 1){ //red - overdue
            		isDoneToolTipMessage = "Overdue task\nClick to mark as done";	
            	}else{ //yellow - expiring soon
            		isDoneToolTipMessage = "Click to mark as done";	
            	}
            	
            }//end if

        }//end if
        
        this.btnIsDone.setTooltip(new Tooltip(isDoneToolTipMessage));
        
        
        setButtonIsDoneStyle(this.getIsDone);
        setLabelDateTimeStyle(this.getIsDone); //if is done, label will be strike out
        setLabelIDStyle(this.getIsDone); 
        setLabelTitleStyle(this.getIsDone); 
        setButtonPriorityStyle(this.getPriority); //set different color for different priority
        setButtonAlarmStyle(this.getRemainder, this.getIsDone); //set alarm icon - enable or disable
        setCellBackground(this.getIsDone); //if is done, background will be darker
        setGraphic(this.grid); //add grid to cell graphic
		
        
        //set btnAlarm tooltip - show reminder date and time
        String getAlarmDate = MainParser.convertMillisecondToDate(this.getRemainder);
        String getAlarmTime = MainParser.convertMillisecondToTime(this.getRemainder);
        if(getAlarmDate.equals("01/01/1970")){
        	getAlarmDate = "Alarm not set";
        	setButtonTooltip(this.btnAlarm, getAlarmDate);
        }else{
        	setButtonTooltip(this.btnAlarm, convertToFullDateString(getAlarmDate) + " " + getAlarmTime);
        }
        
        //set btnPriority tooltip - show priority (Critical, normal, low, or nothing)
        if(this.getPriority.equals("1")){
        	priorityToolTipMessage = "Low";
        }else if(this.getPriority.equals("2")){
        	priorityToolTipMessage = "Normal";
        }else if(this.getPriority.equals("3")){
        	priorityToolTipMessage = "Critical";
        }else{
        	priorityToolTipMessage = "Priority not set";
        }
        
        setButtonTooltip(btnPriority, priorityToolTipMessage);
        
    }
	
	/**Convert date into various type of date string
	 * Parameters:			rawDate - incoming date in format dd/mm/yyyy
	 * Returns:				String - date format in dd MMM yyyy or dd MMM
	 * **/
	private String convertToFullDateString(String rawDate){
		
		if(rawDate.equals("") || rawDate.isEmpty()){
			return "";
		}
		
		//convert from dd/MM/yyyy to dd MMM yyyy
	    SimpleDateFormat oldFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT_STORAGE_2);// dd/MM/yyyy
        SimpleDateFormat newWithYearFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT_FULL_SHORT); //dd MMM yyyy
        SimpleDateFormat newWithoutYearFormat2 = new SimpleDateFormat(Constants.DATETIME_FORMAT_FULL_SHORT_WITHOUT_YEAR); //dd MMM
        
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
			e.printStackTrace();
			return "-";
		}      
	}
	
	/**This method is the event handler for button is done
	 * **/
	EventHandler<MouseEvent> btnIsDoneEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
	    	boolean isUpdated = false;
	    	boolean getCurrentStatus = false;
	    	
	    	String markCommandFormatString = "mark [id] -[type]";
	    	String markCommandEditedString = "";
	    	
	    	if(btnIsDone.getUserData() instanceof Boolean){
	    		getCurrentStatus = (boolean) btnIsDone.getUserData(); //get the current status of done 
	    	}
            
            if(!getCurrentStatus){ //if false
            	getIsDone = true; //change to true (done)
            	markCommandEditedString = markCommandFormatString.replace("[id]", getID).replace("[type]", Constants.PARSER_MARK_DONE);
            	
            }else{
            	getIsDone = false; //change to false (undone)
            	markCommandEditedString = markCommandFormatString.replace("[id]", getID).replace("[type]", Constants.PARSER_MARK_UNDONE);
            }
            
            isUpdated = mUIParent.executeCommand(markCommandEditedString);
            
            if(isUpdated){
            	setCellBackground(getIsDone);
            }     

	    }//end handle
	};
	
	/**This method is the event handler for button show alarm
	 * **/
	EventHandler<MouseEvent> btnShowAlarmEventHandler = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			showAlarmPopOver(getRemainder);
		}
	};
	
	/**This method is the event handler for button show priority
	 * **/
	EventHandler<MouseEvent> btnShowPriorityEventHandler = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			showPriorityPopOver(convertStringToInteger(getPriority));
		}	
	};
	
	/**This method is the event handler for button priority edit
	 * **/
	EventHandler<MouseEvent> btnPriorityEditEventHandler = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
        	showEditPopOver();
		}	
	};
	
	/**This method is the event handler for button delete
	 * **/
	EventHandler<MouseEvent> btnDeleteEventHandler = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			String command = deleteCommand.replace("[id]", getID).trim(); //replace id by actual id	
			if(mUIParent != null){
				mUIParent.executeCommand(command); //execute delete command 
			}
		}	
	};
	
	/**This method show priority pop over with all the controls added
	 * **/
	private void showPriorityPopOver(int selectedPriorityLvl){
		if(this.mPopOverPriority != null){
			if(this.mPopOverPriority.isShowing()){
				this.mPopOverPriority.setAutoHide(true);
				return;
			}
		}
		
		String[] priorityCssArray = {Constants.CSS_POP_OVER_IMAGE_CRITICAL, Constants.CSS_POP_OVER_IMAGE_NORMAL, Constants.CSS_POP_OVER_IMAGE_LOW, Constants.CSS_POP_OVER_IMAGE_DEFAULT};
		String[] priorityArray = {"Critical", "Normal", "Low", "Default"};
		int[] priorityLvlArray = {3, 2, 1, 0};
		
		VBox vBox = new VBox();
		HBox hBox = new HBox();
		this.setPopOverMainHBoxStyle(hBox);
		
		this.lblPopOverTitle = new Label(Constants.UI_POP_OVER_PRIORITY_TITLE);
		this.lblPopOverPriorityMessage = new Label();
		Button btnPriorityChange = new Button("Change");
		
		setPopOverLabelMessageStyle(lblPopOverPriorityMessage, Constants.UI_POP_OVER_PRIORITY_WIDTH); //set message style
		setPopOverLabelTitle(lblPopOverTitle, Constants.UI_POP_OVER_PRIORITY_WIDTH);
		setPopOverButton(btnPriorityChange, Constants.UI_POP_OVER_PRIORITY_WIDTH, onPopOverBtnPriorityClick);
			
		vBox.getChildren().add(lblPopOverTitle); //Add title
		
		generatePriorityOptions(vBox, hBox, priorityArray, priorityLvlArray, priorityCssArray, selectedPriorityLvl);
		
		vBox.getChildren().add(btnPriorityChange); //add btn change
		VBox.setMargin(btnPriorityChange, new Insets(Constants.UI_POP_OVER_VBOX_MARGIN_TOP, 
														Constants.UI_POP_OVER_VBOX_MARGIN_RIGHT,
														Constants.UI_POP_OVER_VBOX_MARGIN_BOTTOM,
														Constants.UI_POP_OVER_VBOX_MARGIN_LEFT)); //set margin of btn change
		this.setPopOverMainVBoxStyle(vBox, Constants.UI_POP_OVER_PRIORITY_WIDTH, Constants.UI_POP_OVER_PRIORITY_HEIGHT);
		
		this.mPopOverPriority = new PopOver(vBox);
		this.mPopOverPriority.setHideOnEscape(true);
		this.mPopOverPriority.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
		this.mPopOverPriority.setAutoFix(true);
		this.mPopOverPriority.setAutoHide(true);
		this.mPopOverPriority.setDetachable(false);
		this.mPopOverPriority.show(btnPriority);  	
	}
	
	/**This method generate each priority levels in the parent horziontal box
	 * **/
	private void generatePriorityOptions(VBox grandParent, HBox parent, String[] priorityArray, int[] lvlArray, String[] cssArray, int selectedPriority){
		
		this.priorityGroup = new ToggleGroup();
		
		for(int i=0; i < priorityArray.length; i++){
			
			ImageView imgPriority = new ImageView();
			Label lblPriorityLevel = new Label(priorityArray[i]);
			RadioButton radioBtn = new RadioButton();
			
			setPopOverPriorityIconStyle(imgPriority, cssArray[i]);
			setPopOverPriorityLabelStyle(lblPriorityLevel, Constants.CSS_POP_OVER_LABEL_PRIORITY);
			
			radioBtn.setToggleGroup(this.priorityGroup);
			radioBtn.setUserData(lvlArray[i]);
			
			if(lvlArray[i] == selectedPriority){
				radioBtn.setSelected(true);
			}
			
			parent = new HBox(Constants.UI_POP_OVER_HBOX_SPACING);
			parent.getChildren().add(radioBtn);
			parent.getChildren().add(imgPriority);
			parent.getChildren().add(lblPriorityLevel);
			grandParent.getChildren().add(parent);
			
		}
	
	}
	
	/**This method set the icon style in pop over priority
	 * **/
	private void setPopOverPriorityIconStyle(ImageView iv, String cssClass){
		iv.setFitWidth(Constants.IMAGE_WIDTH);
		iv.setPreserveRatio(true);
		iv.setSmooth(true);
		iv.setCache(true);
		iv.getStyleClass().addAll(cssClass);
	}
	
	/**This method set the label style in pop over priority
	 * **/
	private void setPopOverPriorityLabelStyle(Label lbl, String cssClass){
		lbl.setAlignment(Pos.BASELINE_LEFT);
		lbl.getStyleClass().addAll(cssClass);
	}
	
	/**This method set the label message style in all pop over
	 * **/
	private void setPopOverLabelMessageStyle(Label lbl, double width){
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setAlignment(Pos.CENTER);
		lbl.setPrefWidth(width);
		lbl.setPrefHeight(Constants.LABEL_MESSAGE_HEIGHT);
		setPopOverLabelMessageVisible(lbl, false, false);
	}
	
	/**This method set the label message visibility
	 * **/
	private void setPopOverLabelMessageVisible(Label lbl, boolean isValid, boolean isVisible){
		lbl.getStyleClass().removeAll(Constants.CSS_POP_OVER_LABEL_CORRECT_MESSAGE, Constants.CSS_POP_OVER_LABEL_ERROR_MESSAGE);
		
		if(isVisible){
			if(isValid){ //valid, green background
				lbl.getStyleClass().addAll(Constants.CSS_POP_OVER_LABEL_CORRECT_MESSAGE);
				
			}else{//invalid, red background
				lbl.getStyleClass().addAll(Constants.CSS_POP_OVER_LABEL_ERROR_MESSAGE);	
			}
		}
		
		lbl.setVisible(isVisible);
	}
	
	/**This method set the label message text
	 * **/
	private void setPopOverLabelMessageText(Label lbl, String msg){
		lbl.setText(msg);
	}

	/**This method show pop over alarm with controls added
	 * Parameters: 		alarmValue - reminder value in long
	 * **/
	private void showAlarmPopOver(Long alarmValue){
	    /*
		 * ---------------------
		 * |       Alarm       |
		 * ---------------------
		 * |[=================]|
		 * |            <Enter>|
		 * |[   Message area  ]|
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
		
		VBox vBox = new VBox();
		HBox hDateBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING);
		HBox hTimeBox = new HBox(Constants.UI_POP_OVER_HBOX_SPACING);
		this.setPopOverMainHBoxStyle(hDateBox);
		this.setPopOverMainHBoxStyle(hTimeBox);
		
		this.lblPopOverTitle = new Label(Constants.UI_POP_OVER_ALARM_TITLE);
		this.lblPopOverAlarmMessage = new Label();
		this.lblPopOverTimeTip = new Label(Constants.UI_POP_OVER_COLON);
		this.lblPopOverDateTitle = new Label(Constants.UI_POP_OVER_DATE_TITLE);
		this.lblPopOverTimeTitle = new Label(Constants.UI_POP_OVER_TIME_TITLE);
		this.lblPopOverColon = new Label(Constants.UI_POP_OVER_COLON);
		this.txtPopOverAlarmHourField = new TextField();
		this.txtPopOverAlarmMinField = new TextField();
		
		Button btnAlarmChange = new Button(Constants.UI_POP_OVER_ALARM_BUTTON_CHANGE_TITLE);

		this.datePickerPopOverAlarm = new DatePicker(); 
		this.datePickerPopOverAlarm.setPromptText(Constants.UI_POP_OVER_DATEPICKER_PROMPT_TEXT);
		
		if(!getAlarmDate.equals(defaultDate)){
			this.datePickerPopOverAlarm.setValue(convertStringToLocalDate(getAlarmDate)); //set year, month, day to datepicker
		}
		
		this.datePickerPopOverAlarm.setConverter(datePickerStringConverter);

		setPopOverLabelTitle(this.lblPopOverTitle, Constants.UI_POP_OVER_ALARM_WIDTH);
		setPopOverLabelDateTime(this.lblPopOverDateTitle, this.lblPopOverTimeTitle, 50.0);
		setPopOverTextFieldHourMinute(this.txtPopOverAlarmHourField, this.txtPopOverAlarmMinField);
		setPopOverLabelTimeTip(this.lblPopOverTimeTip);
		setPopOverLabelMessageStyle(this.lblPopOverAlarmMessage, Constants.UI_POP_OVER_ALARM_WIDTH); //set message style
		setPopOverButton(btnAlarmChange, Constants.UI_POP_OVER_ALARM_WIDTH, onPopOverBtnAlarmClick);
		
		if(!(getAlarmDate.equals(defaultDate) && getAlarmTime.split(":")[0].equals(defaultHour) && getAlarmTime.split(":")[1].equals(defaultMin))){
			this.txtPopOverAlarmHourField.setText(getAlarmTime.split(":")[0]); // Hour
			this.txtPopOverAlarmMinField.setText(getAlarmTime.split(":")[1]); // Minute
		}

				
		hDateBox.getChildren().add(this.lblPopOverDateTitle); //Datee:
		hDateBox.getChildren().add(this.datePickerPopOverAlarm); // datepicker
		hTimeBox.getChildren().add(this.lblPopOverTimeTitle); //Time:
		hTimeBox.getChildren().add(this.txtPopOverAlarmHourField); //hour
		hTimeBox.getChildren().add(this.lblPopOverColon); // :
		hTimeBox.getChildren().add(this.txtPopOverAlarmMinField); //min
		hTimeBox.getChildren().add(this.lblPopOverTimeTip); //time tip
		HBox.setMargin(this.lblPopOverTimeTip, new Insets(Constants.UI_POP_OVER_HBOX_MARGIN_TOP, 
															Constants.UI_POP_OVER_HBOX_MARGIN_RIGHT,
															Constants.UI_POP_OVER_HBOX_MARGIN_BOTTOM, 
															Constants.UI_POP_OVER_HBOX_MARGIN_LEFT));
		VBox.setMargin(btnAlarmChange, new Insets(Constants.UI_POP_OVER_VBOX_MARGIN_TOP, 
													Constants.UI_POP_OVER_VBOX_MARGIN_RIGHT,
													Constants.UI_POP_OVER_VBOX_MARGIN_BOTTOM, 
													Constants.UI_POP_OVER_VBOX_MARGIN_LEFT));
		vBox.getChildren().add(this.lblPopOverTitle);
		vBox.getChildren().add(hDateBox);
		vBox.getChildren().add(hTimeBox);
		vBox.getChildren().add(btnAlarmChange);
		vBox.getChildren().add(this.lblPopOverAlarmMessage);
		this.setPopOverMainVBoxStyle(vBox, Constants.UI_POP_OVER_ALARM_WIDTH, Constants.UI_POP_OVER_ALARM_HEIGHT);
		
		this.mPopOverAlarm = new PopOver(vBox);
		this.mPopOverAlarm.setHideOnEscape(true);
		this.mPopOverAlarm.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
		this.mPopOverAlarm.setAutoFix(true);
		this.mPopOverAlarm.setAutoHide(true);
		this.mPopOverAlarm.setDetachable(false);
    	this.mPopOverAlarm.show(this.btnAlarm); 
	}
	
	//================== POP OVER EDIT TASK ========================
	private void showEditPopOver(){
		String defaultTime = Constants.UI_POP_OVER_DEFAULT_HOUR + Constants.UI_POP_OVER_COLON + Constants.UI_POP_OVER_DEFAULT_MINUTE;
		
		if(this.mPopOverEdit != null){
			if(this.mPopOverEdit.isShowing()){
				this.mPopOverEdit.setAutoHide(true);
				return;
			}
		}
		
		this.lblPopOverTitle = new Label(Constants.UI_POP_OVER_EDIT_TITLE);
		this.txtAreaPopOverEditTaskTitle = new TextArea();
		this.lblPopOverDateTitle = new Label(Constants.UI_POP_OVER_START_DATE_TITLE);
		this.lblPopOverTimeTitle = new Label(Constants.UI_POP_OVER_START_TIME_TITLE);
		this.lblPopOverColon = new Label(Constants.UI_POP_OVER_COLON);
		this.lblPopOverTimeTip = new Label(Constants.UI_POP_OVER_TIME_TIP);
		this.txtPopOverEditStartHourField = new TextField(); //start hour
		this.txtPopOverEditStartMinField = new TextField(); //start minute
		this.datePickerPopOverEditStartDate = new DatePicker(); //start datae
		this.txtPopOverEditEndHourField = new TextField(); //end hour
		this.txtPopOverEditEndMinField = new TextField(); //end min
		this.datePickerPopOverEditEndDate = new DatePicker(); //end date
		this.lblPopOverEditMessage = new Label(); //message 
	
		Button btnEdit = new Button(Constants.UI_POP_OVER_EDIT_BUTTON_CHANGE_TITLE);	
		VBox vBox = new VBox();
		HBox hBox = new HBox();
		this.setPopOverMainHBoxStyle(hBox);
		
		//Set Contents
		this.txtAreaPopOverEditTaskTitle.setPromptText("Type in a new task title");
		this.txtAreaPopOverEditTaskTitle.setText(this.getTitle);
		this.datePickerPopOverEditStartDate.setPromptText("01 January 2015");
		this.datePickerPopOverEditStartDate.setConverter(datePickerStringConverter);
		this.datePickerPopOverEditEndDate.setPromptText("22 January 2015");
		this.datePickerPopOverEditEndDate.setConverter(datePickerStringConverter);
		
		
		if(!(this.getStartDate == null || this.getStartDate.equals("") && this.getStartDate.isEmpty() || 
				this.getStartDate.equals(Constants.UI_POP_OVER_DEFAULT_DATE) && this.getStartTime.equals(defaultTime))){ //not default date time

			this.datePickerPopOverEditStartDate.setValue(convertStringToLocalDate(this.getStartDate)); //set year, month, day to datepicker		
			this.txtPopOverEditStartHourField.setText(this.getStartTime.split(":")[0]);
			this.txtPopOverEditStartMinField.setText(this.getStartTime.split(":")[1]);
		}
		
		if(!(this.getEndDate == null || this.getEndDate.equals("") && this.getEndDate.isEmpty() ||
				this.getEndDate.equals(Constants.UI_POP_OVER_DEFAULT_DATE) && this.getEndTime.equals(defaultTime))){ //not default date time
			
			this.datePickerPopOverEditEndDate.setValue(convertStringToLocalDate(this.getEndDate)); //set year, month, day to datepicker
			this.txtPopOverEditEndHourField.setText(this.getEndTime.split(":")[0]);
			this.txtPopOverEditEndMinField.setText(this.getEndTime.split(":")[1]);
		}

		setPopOverLabelTitle(this.lblPopOverTitle, Constants.UI_POP_OVER_EDIT_WIDTH); //style the label title 
		setPopOverLabelDateTime(this.lblPopOverDateTitle, this.lblPopOverTimeTitle, Constants.UI_POP_OVER_LABEL_DATETIME_WIDTH); //style the labels 
		setPopOverTextFieldHourMinute(this.txtPopOverEditStartHourField, this.txtPopOverEditStartMinField);
		setPopOverLabelTimeTip(this.lblPopOverTimeTip);
		setPopOverButton(btnEdit, Constants.UI_POP_OVER_EDIT_WIDTH, this.onPopOverBtnEditClick);	 //style the button edit 
		setPopOverLabelMessageStyle(this.lblPopOverEditMessage, Constants.UI_POP_OVER_EDIT_WIDTH); //set message style
		
		this.txtAreaPopOverEditTaskTitle.getStyleClass().addAll(Constants.CSS_POP_OVER_TEXTAREA_EDIT);
		this.txtAreaPopOverEditTaskTitle.setPrefRowCount(3); //text area
		this.txtAreaPopOverEditTaskTitle.setPrefWidth(Constants.UI_POP_OVER_EDIT_WIDTH);
		this.txtAreaPopOverEditTaskTitle.setWrapText(true);
		
		vBox.getChildren().add(lblPopOverTitle); //add title
		vBox.getChildren().add(txtAreaPopOverEditTaskTitle); //add task title
			
		hBox = new HBox(); //start date
		this.setPopOverMainHBoxStyle(hBox);
		hBox.getChildren().add(this.lblPopOverDateTitle); 
		hBox.getChildren().add(this.datePickerPopOverEditStartDate);
		vBox.getChildren().add(hBox);
		
		hBox = new HBox(); //start hour and minute
		this.setPopOverMainHBoxStyle(hBox);
		hBox.getChildren().add(this.lblPopOverTimeTitle);  
		hBox.getChildren().add(this.txtPopOverEditStartHourField);
		hBox.getChildren().add(lblPopOverColon);
		hBox.getChildren().add(this.txtPopOverEditStartMinField);
		hBox.getChildren().add(this.lblPopOverTimeTip);
		HBox.setMargin(this.lblPopOverTimeTip, new Insets(10,0,0,0));
		vBox.getChildren().add(hBox);
		
		this.lblPopOverDateTitle = new Label(Constants.UI_POP_OVER_END_DATE_TITLE);
		this.lblPopOverTimeTitle = new Label(Constants.UI_POP_OVER_END_TIME_TITLE);
		this.lblPopOverColon = new Label(Constants.UI_POP_OVER_COLON);
		this.lblPopOverTimeTip = new Label(Constants.UI_POP_OVER_TIME_TIP);
		
		setPopOverLabelDateTime(this.lblPopOverDateTitle, this.lblPopOverTimeTitle, 80.0); //style the labels again
		setPopOverTextFieldHourMinute(this.txtPopOverEditEndHourField, this.txtPopOverEditEndMinField);
		setPopOverLabelTimeTip(this.lblPopOverTimeTip);
		
		hBox = new HBox(); //end date
		this.setPopOverMainHBoxStyle(hBox);
		hBox.getChildren().add(this.lblPopOverDateTitle); 
		hBox.getChildren().add(this.datePickerPopOverEditEndDate);
		vBox.getChildren().add(hBox);
		
		hBox = new HBox(); //end hour and minute
		this.setPopOverMainHBoxStyle(hBox);
		hBox.getChildren().add(this.lblPopOverTimeTitle);  
		hBox.getChildren().add(this.txtPopOverEditEndHourField);
		hBox.getChildren().add(this.lblPopOverColon);
		hBox.getChildren().add(this.txtPopOverEditEndMinField);
		hBox.getChildren().add(this.lblPopOverTimeTip);
		HBox.setMargin(this.lblPopOverTimeTip, new Insets(Constants.UI_POP_OVER_HBOX_MARGIN_TOP,
														Constants.UI_POP_OVER_HBOX_MARGIN_RIGHT,
														Constants.UI_POP_OVER_HBOX_MARGIN_BOTTOM,
														Constants.UI_POP_OVER_HBOX_MARGIN_LEFT));
		
		this.setPopOverMainVBoxStyle(vBox, Constants.UI_POP_OVER_EDIT_WIDTH, Constants.UI_POP_OVER_EDIT_HEIGHT);
		vBox.getChildren().add(hBox);
		vBox.getChildren().add(btnEdit); //add edit button
		VBox.setMargin(btnEdit, new Insets(Constants.UI_POP_OVER_VBOX_MARGIN_TOP,
											Constants.UI_POP_OVER_VBOX_MARGIN_RIGHT,
											Constants.UI_POP_OVER_VBOX_MARGIN_BOTTOM,
											Constants.UI_POP_OVER_VBOX_MARGIN_LEFT));
		vBox.getChildren().add(this.lblPopOverEditMessage); //add message
		
		this.mPopOverEdit = new PopOver(vBox);
		this.mPopOverEdit.setHideOnEscape(true);
		this.mPopOverEdit.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
		this.mPopOverEdit.setAutoFix(true);
		this.mPopOverEdit.setAutoHide(true);
		this.mPopOverEdit.setDetachable(false);
    	this.mPopOverEdit.show(this.btnEdit);
		
	}
	
	/**This method set the horizontal box style
	 * **/
	private void setPopOverMainHBoxStyle(HBox hBox){
		hBox.setSpacing(Constants.UI_POP_OVER_HBOX_SPACING);
	}
	
	/**This method set the vertical box style
	 * **/
	private void setPopOverMainVBoxStyle(VBox vBox, double width, double height){
		vBox.setSpacing(Constants.UI_POP_OVER_VBOX_SPACING);
		vBox.setPadding(new Insets(Constants.UI_POP_OVER_VBOX_PADDING_INSETS_TOP,
				Constants.UI_POP_OVER_VBOX_PADDING_INSETS_RIGHT,
				Constants.UI_POP_OVER_VBOX_PADDING_INSETS_BOTTOM,
				Constants.UI_POP_OVER_VBOX_PADDING_INSETS_LEFT));
		
		vBox.getStyleClass().addAll(Constants.CSS_POP_OVER_CONTENT_AREA); //set style
	}
	
	/**DatePicker string converter that override toString and fromString.
	 * Used for formatting date
	 * **/
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
	
	/**This method style the pop over label title
	 * Parameters: 		lbl - the label of each pop over
	 * 					width - the desired width for the title
	 * **/
	private void setPopOverLabelTitle(Label lbl, double width){
		lbl.getStyleClass().addAll(Constants.CSS_POP_OVER_TITLE);
		lbl.setPrefWidth(width);
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setAlignment(Pos.CENTER);
	}
	
	/**This method style the pop over label date and time
	 * Parameters: 		lblDate - the date label of each pop over
	 * 					lblTime - the time label for each pop over
	 * 					width - the desired width for the both date and time
	 * **/
	private void setPopOverLabelDateTime(Label lblDate, Label lblTime, double width){
		lblDate.setPrefWidth(width);
		lblDate.setAlignment(Pos.CENTER_RIGHT);
		lblTime.setPrefWidth(width);
		lblTime.setAlignment(Pos.CENTER_RIGHT);
	}
	
	/**This method style the pop over label title
	 * Parameters: 		lbl - the label of each pop over
	 * 					width - the desired width for the title
	 * **/
	private void setPopOverTextFieldHourMinute(TextField txtHr, TextField txtMin){
		txtHr.setPrefWidth(Constants.UI_POP_OVER_TEXT_HOUR_WIDTH);
		txtHr.setPromptText(Constants.UI_POP_OVER_TEXT_HOUR_PROMPT_TEXT);
		txtMin.setPrefWidth(Constants.UI_POP_OVER_TEXT_MINUTE_WIDTH);
		txtMin.setPromptText(Constants.UI_POP_OVER_TEXT_MINUTE_PROMPT_TEXT);
	}
	
	/**This method style the pop over label time
	 * Parameters: 		lbl - the label time of each pop over
	 * **/
	private void setPopOverLabelTimeTip(Label lbl){
		lbl.getStyleClass().addAll(Constants.CSS_POP_OVER_TIME_TIP); //set tip style (after the minute input)
	}
	
	/**This method style the pop over label button
	 * Parameters: 		btn - the button of each pop over
	 * 					width - the desired width for the title
	 * 					event - action event for the button
	 * **/
	private void setPopOverButton(Button btn, double width, EventHandler<ActionEvent> event){
		btn.setPrefWidth(width);
		btn.getStyleClass().addAll(Constants.CSS_POP_OVER_BUTTON_CHANGE);
		btn.addEventFilter(ActionEvent.ACTION, event);
	}
	
	/**Event handler for button edit
	 * **/
	private EventHandler<ActionEvent> onPopOverBtnEditClick = new EventHandler<ActionEvent>() { //
		@Override
        public void handle(ActionEvent event) {
			boolean isEdited = false;
			boolean isStartDateTimeEmpty = false;
			boolean isEndDateTimeEmpty = false;
			
			LocalDate rawStartDate = datePickerPopOverEditStartDate.getValue();
			LocalDate rawEndDate = datePickerPopOverEditEndDate.getValue();
			
			String newTitle = txtAreaPopOverEditTaskTitle.getText(); //new title
			String newStartDate = convertDateToStorageFormat(rawStartDate); //new start date
			String newEndDate = convertDateToStorageFormat(rawEndDate); //new end date
			String newStartHour = txtPopOverEditStartHourField.getText(); //new start hour
			String newStartMinute = txtPopOverEditStartMinField.getText(); //new start minute
			String newEndHour = txtPopOverEditEndHourField.getText(); //new end hour
			String newEndMinute = txtPopOverEditEndMinField.getText(); //new end minute
			
			String newStartTime = "";
			String newEndTime = "";
			String command = editTaskcommand;
			
			if(!isTitleValid(newTitle)){ //check title
				setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_INVALID_EDIT_TITLE_MESSAGE);
    			return;
			}
			
			if(!(isValidHour(newStartHour) && isValidMinute(newStartMinute))){ //check is valid for start hour and minute (accept empty)
				setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_INVALID_EDIT_START_TIME_MESSAGE);
    			return;
			}
			
			if(!(isValidHour(newEndHour) && isValidMinute(newEndMinute))){ //check is valid for end hour and minute (accept empty)
				setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_INVALID_EDIT_END_TIME_MESSAGE);
    			return;
			}
			
			
			//Check both start date and time
			if((newStartDate.equals("") || newStartDate.isEmpty()) && 
					(newStartHour.equals("") || newStartHour.isEmpty()) && 
					(newStartMinute.equals("") || newStartMinute.isEmpty())){
				//start date, time is empty
				//remove start date and time
				isStartDateTimeEmpty = true;
					
			}else if(!(newStartDate.equals("") || newStartDate.isEmpty()) && 
					((newStartHour.equals("") || newStartHour.isEmpty()) ||
					(newStartMinute.equals("") || newStartMinute.isEmpty()))) { //date is not empty, but hour or minute is empty
				
				setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_INVALID_EDIT_START_TIME_MESSAGE);
    			return;
    			
			}else{	
				newStartTime = newStartHour + ":" + newStartMinute;
				isStartDateTimeEmpty = false;	
			}
			
			//Check both end date and time
			if((newEndDate.equals("") || newEndDate.isEmpty()) && 
					(newEndHour.equals("") || newEndHour.isEmpty()) && 
					(newEndMinute.equals("") || newEndMinute.isEmpty())){
				
				isEndDateTimeEmpty = true;
				
			}else if(!(newStartDate.equals("") || newStartDate.isEmpty()) && 
					((newEndHour.equals("") || newEndHour.isEmpty()) ||
					(newEndMinute.equals("") || newEndMinute.isEmpty()))) { //date is not empty, but hour or minute is empty

				setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_INVALID_EDIT_END_TIME_MESSAGE);
    			return;
    			
			}else{
				newEndTime = newEndHour + ":" + newEndMinute;
				isEndDateTimeEmpty = false;
			}
			
			if(isStartDateTimeEmpty && isEndDateTimeEmpty){ //anything to float
				command = command.replace("-[new_start_date]", "").replace("-[new_start_time]", "");
				command = command.replace("-[new_end_date]", "").replace("-[new_end_time]", "");

			}else if (isStartDateTimeEmpty && !isEndDateTimeEmpty){ //timed to deadline
				command = command.replace("[new_start_date]", newEndDate).replace("[new_start_time]", newEndTime);
				command = command.replace("-[new_end_date]", "").replace("-[new_end_time]", "");
				
			}else if (!isStartDateTimeEmpty && isEndDateTimeEmpty){ //timed to deadline
				command = command.replace("[new_start_date]", newStartDate).replace("[new_start_time]", newStartTime);
				command = command.replace("-[new_end_date]", "").replace("-[new_end_time]", "");
				
			}else{
				command = command.replace("[new_start_date]", newStartDate).replace("[new_start_time]", newStartTime);
				command = command.replace("[new_end_date]", newEndDate).replace("[new_end_time]", newEndTime);
			}
			
			command = command.replace("[id]", getID);
			command = command.replace("[new_title]", newTitle);
			
			isEdited = mUIParent.executeCommand(command);
			if(isEdited){
				setPopOverLabelMessageVisible(lblPopOverEditMessage, true, true); //show success message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_SUCCESS_EDIT_MESSAGE);
			}else{
				setPopOverLabelMessageVisible(lblPopOverEditMessage, false, true); //show error message
    			setPopOverLabelMessageText(lblPopOverEditMessage, Constants.CELL_POP_OVER_FAIL_EDIT_MESSAGE);
			}
			
			closePopOverEdit();
			//edit [id] -[new_title] -[new_start_date] -[new_start_time] -[new_end_date] -[new_end_time]";
			
		}
	};
	
	/**Event handler for pop over button priority change
	 * **/
	private EventHandler<ActionEvent> onPopOverBtnPriorityClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(priorityGroup != null){
        		//priority command e.g. = priority id- 0/1/2/3
        		String newLevel = priorityGroup.getSelectedToggle().getUserData().toString();
        		String priorityCommand = editPriorityCommand.replace("[id]", getID).replace("[level]", newLevel);
        		boolean hasChanged = false;
        		
        		//Execute command
        		if(mUIParent != null){
        			hasChanged = mUIParent.executeCommand(priorityCommand);
        		}
        		
        		if(hasChanged){
        			setPopOverLabelMessageVisible(lblPopOverPriorityMessage, true, true); //show success message
        			setPopOverLabelMessageText(lblPopOverPriorityMessage, Constants.CELL_POP_OVER_SUCCESS_PRIORITY_MESSAGE);
        		}else{
        			setPopOverLabelMessageVisible(lblPopOverPriorityMessage, false, true); //show error message
        			setPopOverLabelMessageText(lblPopOverPriorityMessage, Constants.CELL_POP_OVER_FAIL_PRIORITY_MESSAGE);
        		}
        		
        		closePopOverPriority();
        	}
        }
    };
    
    /**Event handler for pop over button alarm change
	 * **/
    private EventHandler<ActionEvent> onPopOverBtnAlarmClick = new EventHandler<ActionEvent>() {
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
        		boolean hasChanged = false;
        		
        		if(!(isValidHour(newHour) && isValidMinute(newMinute))){ //check is valid for hour and minute
        			setPopOverLabelMessageVisible(lblPopOverAlarmMessage, false, true); //show error message
        			setPopOverLabelMessageText(lblPopOverAlarmMessage, Constants.CELL_POP_OVER_INVALID_ALARM_TIME_MESSAGE);
        			return;
        		}
        		
        		if(!(newDate.isEmpty() || newDate.equals("") 
        				|| newDate.isEmpty() || newHour.equals("") 
        				|| newMinute.isEmpty() || newMinute.equals(""))){
        			alarmCommand = editAlarmCommand.replace("[id]", getID).replace("[start_date]", newDate)
        											.replace("[start_time]", addLeadingZero(convertTimeEmptyToValue(newHour)) + ":" + 
        																		addLeadingZero(convertTimeEmptyToValue(newMinute)));
        		}else{
        			//date is empty, time is empty
        			//means off
        			alarmCommand = offAlarmCommand.replace("[id]", getID);
        		}
        		
        		//Execute command
        		if(mUIParent != null){
        			hasChanged = mUIParent.executeCommand(alarmCommand);
        		}
        		
        		if(hasChanged){
	        		setPopOverLabelMessageVisible(lblPopOverAlarmMessage, true, true); //show success message
	    			setPopOverLabelMessageText(lblPopOverAlarmMessage, Constants.CELL_POP_OVER_SUCCESS_ALARM_MESSAGE);
        		}else{
	    			setPopOverLabelMessageVisible(lblPopOverAlarmMessage, false, true); //show error message
	    			setPopOverLabelMessageText(lblPopOverAlarmMessage, Constants.CELL_POP_OVER_FAIL_ALARM_MESSAGE);
        		}
        		
        		closePopOverAlarm();
    			        		
        	}
        	
        }
    };
    
    /**This method validate title
     * Parameters:		title - string title
     * Returns:			boolean - true or false
   	 * **/
    private boolean isTitleValid(String title){
		if(title.isEmpty() || title.equals("")){ //don't accept empty
			return false;
		}
		return true;
	}
    
    /**This method validate hour 
     * Parameters:		hour - string hour
     * Returns:			boolean - true or false
   	 * **/
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
    
    /**This method validate minute
     * Parameters:		minute - string minute
     * Returns:			boolean - true or false
   	 * **/
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
    
    /**This method convert a date string to a valid date format
     * Parameters:		rawDate - in the format dd/MM/yyyy
     * Returns:			LocalDate - valid date of the raw date
   	 * **/
    private LocalDate convertStringToLocalDate(String rawDate){ //rawDate in format dd/mm/yyyy
    	return LocalDate.of(convertStringToInteger(rawDate.split("/")[2]), 
							convertStringToInteger(rawDate.split("/")[1]), 
							convertStringToInteger(rawDate.split("/")[0]));
    }
    
    /**This method convert date to a valid storage date format
     * Parameters:		rawDate - in the format dd/MM/yyyy
     * Returns:			String - date in storage format d/M/yyyy
   	 * **/
    private String convertDateToStorageFormat(LocalDate rawDate){
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_STORAGE); //e.g. 18/4/2015
		
		if(rawDate == null){
			return "";
		}
		
		return dateFormatter.format(rawDate);
    }
    
    /**This method convert a empty time to zeros if there's any
     * Parameters:		time - String time
     * Returns:			String - either 00 or the same
   	 * **/
    private String convertTimeEmptyToValue(String time){
    	if(time.isEmpty() || time.equals("")){
    		return "00";
    	}
    	return time;
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
