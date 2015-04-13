//@author A0125474E
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
	public void test_read_file_location_preference(){
		assertEquals("Is file location key empty?", false, isStringEmpty(Constants.PREFERENCE_XML_FILE_LOCATION));
		
		String fileLocation = mPrefs.getPreferenceFileLocation();

		assertEquals("Is file location value empty?", false, isStringEmpty(fileLocation));
	
	}
	
	@Test
	public void test_read_flag_preference(){
		assertEquals("Is flag key empty?", false, isStringEmpty(Constants.PREFERENCE_PISTA_FLAG_NODE));
		
		int flag = mPrefs.getPreferencePistaFlag();

		assertEquals("Is key value empty?", 0, flag);
	
		
	}
	
		
	public boolean isStringEmpty(String str){
		
		if((str != null) || (!str.isEmpty())){
			return false;
		}
		return true;
	}
	
	
	
	
}
