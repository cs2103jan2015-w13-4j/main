package pista.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pista.Constants;
import pista.CustomPreferences;
import pista.storage.Storage;

public class PreferencesUnitTest {

	private CustomPreferences mPrefs = null;
	
	@Before
	public void init(){
		mPrefs = CustomPreferences.getInstance();
	}
	
	@Test
	public void test_init_preference() {
		assertEquals("Initializing correct preferences", true, mPrefs.initPreference(Constants.PREFERENCE_URL_PATH));
	}

	@Test
	public void test_read_preference(){
		assertEquals("Is key empty?", false, isStringEmpty(Constants.PREFERENCE_XML_FILE_LOCATION_NODE));
		
		String fileLocation = mPrefs.getPreferenceFileLocation();

		assertEquals("Is value empty?", false, isStringEmpty(fileLocation));
	
	}
	
		
	public boolean isStringEmpty(String str){
		
		if((str != null) || (!str.isEmpty())){
			return false;
		}
		return true;
	}
	
	
	
}
