package logic;

import java.io.IOException;

import logic.MainApp;
import logic.view.ListViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
    private BorderPane rootLayout;
       
    //private Constants mConstants;
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.PRODUCT_NAME);

        initRootLayout();
        showMainUI();
        initListViewLayout();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Constants.ROOT_LAYOUT_PATH));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void initListViewLayout(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Constants.LISTVIEW_LAYOUT_PATH));
			
			//AnchorPane mAP = (AnchorPane) primaryStage.getScene().lookup("#anchorRightPane");
			
			//BorderPane bp = (BorderPane) loader.load();
			
			//mAP.getChildren().add(bp);
			

			/*
			anchorRightPane.getChildren().add(mPane);
			AnchorPane.setTopAnchor(mPane, 10.0);  
			AnchorPane.setRightAnchor(mPane, 10.0);  
			AnchorPane.setTopAnchor(mPane, 40.0);  
			AnchorPane.setRightAnchor(mPane,10.0); 
			*/
			
			ListViewController controller = loader.getController();
			//controller.setMainListView(this);
			
		}catch (Exception e){
			e.printStackTrace();
		}//end try
	}
	
	
    public void showMainUI() {
        try {
            // Load MainUI
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Constants.MAIN_UI_LAYOUT_PAH));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set MainUI into the center of RootLayout
            rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}
