//@author A0125474E
package pista;

import java.util.prefs.Preferences;

public class CustomPreferences {

	private static CustomPreferences mCustomPref = new CustomPreferences();
	private Preferences mPrefs = null;
	
	public static CustomPreferences getInstance(){
		return mCustomPref;
	}
	
	public boolean initPreference(String className){
		try{
			mPrefs = Preferences.userRoot().node(className); 
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	private boolean savePreference(String name, String value){
		try{
			if(mPrefs == null){			
				initPreference(Constants.PREFERENCE_URL_PATH);
			}	
			mPrefs.put(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean savePreference(String name, int value){
		try{
			if(mPrefs == null){
				initPreference(Constants.PREFERENCE_URL_PATH);
			}
			mPrefs.putInt(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private boolean savePreference(String name, boolean value){
		try{
			if(mPrefs == null){
				initPreference(Constants.PREFERENCE_URL_PATH);
			}
			mPrefs.putBoolean(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private String getPreferenceStringValue(String key){
		if(mPrefs == null){ return ""; }
		String value = mPrefs.get(key, "");
		return value;
	}
	
	private int getPreferenceIntValue(String key){
		if(mPrefs == null){ return 0; }
		int value = mPrefs.getInt(key, 0);
		return value;
	}
	
	private boolean getPreferenceBooleanValue(String key){
		if(mPrefs == null){ return false; }
		boolean value = mPrefs.getBoolean(key, false);
		return value;
	}
	
	public boolean setPreferenceFileLocation(String value){
		boolean isSet = this.savePreference(Constants.PREFERENCE_XML_FILE_LOCATION, value);
		return isSet;
	}
	
	public String getPreferenceFileLocation(){
		String value = this.getPreferenceStringValue(Constants.PREFERENCE_XML_FILE_LOCATION);
		return value;
	}
	
	public boolean setPreferencePistaFlag(int value){ //should be more than 0
		boolean isSet = this.savePreference(Constants.PREFERENCE_PISTA_FLAG_NODE, value);
		return isSet;
	}
	
	public int getPreferencePistaFlag(){ //first launch, is 0. subsequent launch will be 1
		int value = this.getPreferenceIntValue(Constants.PREFERENCE_PISTA_FLAG_NODE);
		return value;
	}
	
}
