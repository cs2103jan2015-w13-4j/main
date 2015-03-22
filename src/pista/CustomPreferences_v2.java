package pista;

import java.util.prefs.Preferences;

public class CustomPreferences_v2 {
	private static CustomPreferences_v2 mCustomPref = new CustomPreferences_v2();
	private Preferences mPref = null;
	
	public static CustomPreferences_v2 getInstance(){
		return mCustomPref;
	}
	
	public boolean init(String className){
		//mPref = Preferences.userNodeForPackage(getClass());
		this.mPref = Preferences.userRoot().node(className); //.node(className)
		return true;
	}
	
	public boolean setPreferences(String key, String value){
		mPref.put(key, value);
		return true;
	}
	
	public String getPreference(String key){
		return mPref.get(key, "");
	}
}
