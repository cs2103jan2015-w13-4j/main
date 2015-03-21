package pista.test;

import pista.ui.SettingLayoutController;

public class SettingLayoutControllerUtility extends SettingLayoutController{
	
	private static final SettingLayoutControllerUtility mSelfUtility = new SettingLayoutControllerUtility();;

	
	public static SettingLayoutControllerUtility getInstance(){
    	return mSelfUtility;
    }
	
	public boolean initPreferences(){
		return super.initPreferences();
	}
	
	public boolean setPreferenceFilePath(String newPath){
		return super.setPreferenceFilePath(newPath);
	}
	/*
	public boolean init(){
		try{
			mSettingCtrl = SettingLayoutController.getInstance();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		
	}*/
	
	
}
