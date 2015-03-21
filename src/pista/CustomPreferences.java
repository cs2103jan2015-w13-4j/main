package pista;

import java.util.prefs.Preferences;


public class CustomPreferences {

	private static CustomPreferences mCustomPref = new CustomPreferences();
	private Preferences mPrefs = null;
	
	private CustomPreferences(){} //make sure this class cannot be instantiate
	
	public static CustomPreferences getInstance(){
		return mCustomPref;
	}
	

	public boolean initPreference(String className){
		try{
			//mPrefs = Preferences.userNodeForPackage(getClass());
			mPrefs = Preferences.userRoot().node(className);
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			
			return false;
		}
		
	}
	
	public boolean savePreference(String name, String value){
		try{
			if(mPrefs == null){			
				initPreference(getClass().getName());
			}	
			mPrefs.put(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean savePreference(String name, int value){
		try{
			if(mPrefs == null){
				initPreference(getClass().getName());
			}
			mPrefs.putInt(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean savePreference(String name, boolean value){
		try{
			if(mPrefs == null){
				initPreference(getClass().getName());
			}
			mPrefs.putBoolean(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public String getPreferenceStringValue(String name){
		if(mPrefs == null){ return ""; }
		String value = mPrefs.get(name, "");
		return value;
	}
	
	public int getPreferenceIntValue(String name){
		if(mPrefs == null){ return 0; }
		int value = mPrefs.getInt(name, 0);
		return value;
	}
	
	public boolean getPreferenceBooleanValue(String name){
		if(mPrefs == null){ return false; }
		boolean value = mPrefs.getBoolean(name, false);
		return value;
	}
	
	
}
