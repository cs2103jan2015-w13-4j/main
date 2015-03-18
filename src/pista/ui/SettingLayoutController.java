package pista.ui;

import java.io.File;

import pista.Constants;
import pista.logic.Logic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class SettingLayoutController {

	private final String ASSERT_EMPTY_XML_FILE_PATH_MESSAGE = "File path is empty";
	
	@FXML
    private Button btnBrowse;

    @FXML
    private TextField txtBoxDirectory;

    @FXML
    void onButtonBrowseClick(ActionEvent event) {
    	
    	String chosenFilePath = "";
    	boolean isXmlSaved = false;
    	
    	try{
    		chosenFilePath = chooseFile();
        	setTextBoxDirectory(chosenFilePath);
        	
        	assert(!chosenFilePath.equals("") || chosenFilePath.isEmpty()) : ASSERT_EMPTY_XML_FILE_PATH_MESSAGE;
        	
        	isXmlSaved = Logic.createNewXMLFile(chosenFilePath);
        	
    	}catch(AssertionError e){
    		
    	}catch(Exception e){
    		
    	}
    	
    }

    private String getUserDesktopDirectory(){
    	return Constants.SETTING_DEFAULT_FOLDER_PATH;
    }
    
    private String chooseDirectory(String initPath){
    	DirectoryChooser dc = new DirectoryChooser();
    
    	dc.setInitialDirectory(new File(initPath));
    	File selectedDir = dc.showDialog(null);
    	
    	if(selectedDir!= null){
    		return selectedDir.getPath();
    	}else{
    		return "";
    	}
    	
    }
    
    private String chooseFile(){
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
    	 }
    	 
    	 return file.getPath();
   
    }
    
    
    private void setTextBoxDirectory(String path){
    	txtBoxDirectory.setText(path);
    }
}
