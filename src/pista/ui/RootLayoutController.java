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

		FXMLLoader loader = new FXMLLoader();
		
        loader.setLocation(getClass().getResource(Constants.SETTING_LAYOUT_PATH));
        BorderPane rootLayout;
		try {
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout, 500, 300);
	        
			stage.setScene(scene);    
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
