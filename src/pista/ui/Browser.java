package pista.ui;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.*;


public class Browser extends Region{

	final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public Browser() {
 
    	String url = getClass().getResource("html/index.html").toExternalForm();  
        //apply the styles
        getStyleClass().addAll("html/bootstrap/css/bootstrap-theme.css", "html/bootstrap/css/navbar-fixed-top.css", "html/bootstrap/css/index.css");
        // load the web page
        webEngine.load(url);
        //add the web view to the scene
        getChildren().add(browser);
 
    }
    
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
    
    @Override 
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override 
    protected double computePrefWidth(double height) {
        return 500;
    }
 
    @Override 
    protected double computePrefHeight(double width) {
        return 700;
    }
}
