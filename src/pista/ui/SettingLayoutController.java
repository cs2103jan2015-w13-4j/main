package pista.ui;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import pista.Constants;
import pista.CustomPreferences;
import pista.logic.Logic;
import pista.storage.Storage;

public class SettingLayoutController {

	private static SettingLayoutController mSettingLayoutCtrl = new SettingLayoutController();
	private Storage mStorage;
	private Logic mLogic;
	
	private final String ASSERT_EMPTY_XML_FILE_PATH_MESSAGE = "File path is empty";
	private final String STATUS_EMPTY_XML_FILE_PATH_MESSAGE = "Please provide a XML file to keep track of your data";
	private final String STATUS_INVALID_XML_FILE_PATH_MESSAGE  = "Either your file is not empty or invalid format.";
	private final String STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE = "Failed to load selected file.";
	private final String STATUS_FAIL_TO_SAVE_DUE_TO_INVALID_FILE_MESSAGE = "Please ensure your file is valid before saving.";
	private final String STATUS_FAIL_TO_SAVE = "Unable to save setting. Please try again.";
	private final String STATUS_APPLICATION_ERROR_MESSAGE = "Application error. Please contact the administrator";
	private final String STATUS_SUCCESS_FILE_CREATED_MESSAGE = "[new_file_path] is loaded.";
	private String chosenFilePath = "";
	
	@FXML
    private Button btnSave;
	
	@FXML
    private Button btnBrowse;

    @FXML
    private TextField txtBoxDirectory;

    @FXML
    private Text txtStatus;
    
    private RootLayoutController mRootCtrl = null;
    
    private boolean isValidFile = false;
    
    private CustomPreferences mPrefs = null;
    
    public static SettingLayoutController getInstance(){
    	return mSettingLayoutCtrl;
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
    
	public boolean initLogic(){
		try{
			this.mLogic = Logic.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

    
	protected boolean initPreferences(){
		try{
			mPrefs = CustomPreferences.getInstance();
	    	mPrefs.initPreference(Constants.PREFERENCE_URL_PATH);
	    	
	    	return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
    }
	
	protected boolean setPreferenceFilePath(String newPath){
		try{
			mPrefs.setPreferenceFileLocation(newPath);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
    
    @FXML
	public void initialize() {
    	initStorage();
    	initLogic();
    	initPreferences();
    	setTextStatus("");
    	setTextBoxDirectory(mPrefs.getPreferenceFileLocation());
    }
    
    
    @FXML
    void onButtonBrowseClick(ActionEvent event) {
    	try{
    		chosenFilePath = chooseFile();

    		isValidFile = mLogic.checkFileBeforeSave(chosenFilePath);
    		
    		if(isValidFile){
    			setTextBoxDirectory(chosenFilePath);
    			setTextStatus("");
    		}else{
    			setTextBoxDirectory("");
    			setTextStatus(STATUS_INVALID_XML_FILE_PATH_MESSAGE);
    		}
    		    		
    	}catch(AssertionError e){
    		//log
    		e.printStackTrace();
    	}catch(Exception e){
    		//log
    		e.printStackTrace();
    		setTextStatus(STATUS_APPLICATION_ERROR_MESSAGE);
    	}
    	
    }

    @FXML
    void onButtonSaveClick(ActionEvent event) {
    	boolean isFileCreated = false;
    	boolean isPrefSave = false;
    	
    	if(isValidFile){		
        	try{
            	isFileCreated = mLogic.checkFileDuringSave(chosenFilePath);
            	
            	if(isFileCreated){
            		String status = STATUS_SUCCESS_FILE_CREATED_MESSAGE.replace("[new_file_path]", chosenFilePath);
        			setTextStatus(status);
            	}else{
            		//logging
            		setTextStatus(STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE);
            	}
            	
            	isPrefSave = setPreferenceFilePath(chosenFilePath); //save preferences
            	
            	if(!isPrefSave){ //unable to save
            		setTextStatus(STATUS_FAIL_TO_SAVE);
            		
            	}else{ //saved successfully
            		mStorage.setDataFolderLocation(chosenFilePath); //set new path to storage
            		refreshUIListView();
            	}

        	}catch(AssertionError e){
        		//log
        		e.printStackTrace();
        		setTextStatus(STATUS_EMPTY_XML_FILE_PATH_MESSAGE);
        		
        	}catch(Exception e){
        		//log
        		e.printStackTrace();
        		setTextStatus(STATUS_APPLICATION_ERROR_MESSAGE);
        	}//end try
    	}else{
    		setTextStatus(STATUS_FAIL_TO_SAVE_DUE_TO_INVALID_FILE_MESSAGE);
    	}//end isValid
    	
    }
    
    private String getUserDesktopDirectory(){
    	return Constants.SETTING_DEFAULT_FOLDER_PATH;
    }
       
    private String chooseFile(){
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
    	 
    	 return "";
   
    }
    
    
    private void setTextBoxDirectory(String path){
    	txtBoxDirectory.setText(path);
    }
    
    private void setTextStatus(String msg){
    	txtStatus.setText(msg);
    }
    
    public void setRootController(RootLayoutController ctrl){
    	mRootCtrl = ctrl;
    }
    
    private boolean refreshUIListView(){
    	boolean isRefresh = mRootCtrl.refreshUIListView();
    	return isRefresh;
    }
    
}
