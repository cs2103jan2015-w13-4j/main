package pista.ui;

import java.io.IOException;

import pista.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootLayoutController {
	
	private SettingLayoutController mSettingCtrl = null;
	private UIController mUICtrl = null;

	@FXML
    void onMenuItemSettingClick(ActionEvent event) {
		Stage stage = new Stage();

		stage.setTitle(Constants.SETTING_LAYOUT_TITLE);
		stage.initModality(Modality.NONE);
		stage.initStyle(StageStyle.DECORATED);
		stage.setResizable(false);
		//stage.setMaxHeight(800);
		//stage.setMaxWidth(600);
		//stage.setMinHeight(600);
		//stage.setMinWidth(400);
		try {
			FXMLLoader loader = new FXMLLoader();
			
	        loader.setLocation(getClass().getResource(Constants.SETTING_LAYOUT_PATH));
	        BorderPane rootLayout;
			rootLayout = (BorderPane) loader.load();
			
			mSettingCtrl  = loader.getController(); //setting page will not capture rootcontroller during initialize
	        mSettingCtrl.setRootController(this); //setting will capture rootcontroller after initialize
	        
			Scene scene = new Scene(rootLayout, 500, 300);
	        
			stage.setScene(scene);    
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void setUIController(UIController ctrl){
    	mUICtrl = ctrl;
    }
	
	public void hello(){
		System.out.println("hello");
	}
	
	public boolean refreshUIListView(){
		boolean isUpdated = mUICtrl.showTaskListInListView();
		return isUpdated;
	}
	
	
}
