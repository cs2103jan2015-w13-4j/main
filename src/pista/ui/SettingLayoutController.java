package pista.ui;

import java.io.File;

import pista.Constants;
import pista.CustomPreferences;
import pista.MainApp;
import pista.logic.Logic;
import pista.storage.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class SettingLayoutController {

	private final String ASSERT_EMPTY_XML_FILE_PATH_MESSAGE = "File path is empty";
	private final String STATUS_EMPTY_XML_FILE_PATH_MESSAGE = "Please provide a XML file to keep track of your data";
	private final String STATUS_INVALID_XML_FILE_PATH_MESSAGE  = "Either your file is not empty or invalid format.";
	private final String STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE = "Failed to load selected file.";
	private final String STATUS_FAIL_TO_SAVE_DUE_TO_INVALID_FILE_MESSAGE = "Please ensure your file is valid before saving.";
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
    
	private boolean initPreferences(){
		try{
			mPrefs = CustomPreferences.getInstance();
	    	mPrefs.initPreference(UIController.class.getName());
	    	return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
    }
	
	private boolean setPreferenceFilePath(String newPath){
		try{
			mPrefs.savePreference(Constants.PREFERENCE_DATA_FILE_PATH_KEY, newPath);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
    
    @FXML
	public void initialize() {
    	initPreferences();
    	setTextStatus("");
    	setTextBoxDirectory(Storage.getDataFolderLocation());
    }
    
    
    @FXML
    void onButtonBrowseClick(ActionEvent event) {
    	try{
    		chosenFilePath = chooseFile();

    		isValidFile = Logic.checkFileBeforeSave(chosenFilePath);
    		
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
    
    	if(isValidFile){		
        	try{
            	isFileCreated = Logic.checkFileDuringSave(chosenFilePath);
            	
            	if(isFileCreated){
            		String status = STATUS_SUCCESS_FILE_CREATED_MESSAGE.replace("[new_file_path]", chosenFilePath);
        			setTextStatus(status);
            	}else{
            		//logging
            		setTextStatus(STATUS_FAIL_TO_LOAD_XML_FILE_PATH_MESSAGE);
            	}
            	
            	setPreferenceFilePath(chosenFilePath);
            	refreshUIListView();
            	
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
