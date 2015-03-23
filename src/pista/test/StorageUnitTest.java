package pista.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pista.CustomPreferences;
import pista.storage.Storage;

public class StorageUnitTest {

	private Storage mStorage = null;
	private CustomPreferences mPrefs = null;
	
	private String dummyFilePath = "";

	@Before
	public void init(){
		mStorage = Storage.getInstance();	
		mPrefs = CustomPreferences.getInstance();
		
		dummyFilePath = mPrefs.getPreferenceFileLocation();
	}
	
	@Test
	public void test_Storage_File_Exist() {
		//fail("Not yet implemented");
		assertEquals("Is storage file path exists?", true, mStorage.isFileExist(dummyFilePath));
	}

	@Test
	public void test_Storage_File_Empty() {
		assertEquals("Is storage file path empty?", false, mStorage.isFileEmpty(dummyFilePath));
	}
	
	@Test
	public void test_Storage_File_Format_Valid() {
		assertEquals("Is storage file format valid?", true, mStorage.isFileFormatValid(dummyFilePath));
	}
}
