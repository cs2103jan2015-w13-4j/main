package pista.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pista.ui.SettingLayoutController;
import pista.ui.UIController;

public class SettingLayoutControllerUnitTest {

	UIController mUICtrl;
	SettingLayoutControllerUtility mSettingCtrlUtility;
	
	private final String DUMMY_NEW_FILE_PATH = "logtest.txt";
	
	@Before
	public void init(){
		mSettingCtrlUtility = SettingLayoutControllerUtility.getInstance();
	}
	
	@Test
	public void test_setting() {
		//fail("Not yet implemented");
		 /* This is a boundary case for the ‘negative value’ partition */
		//have to implement boundary values
		//assertEquals(true, mSettingCtrl.initPreferences());
		assertEquals(true, mSettingCtrlUtility.initPreferences());
		assertEquals(true, mSettingCtrlUtility.setPreferenceFilePath(DUMMY_NEW_FILE_PATH));
		
	}
	
	public void test2(){
		
	}
	

}
