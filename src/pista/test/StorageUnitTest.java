package pista.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pista.CustomPreferences;
import pista.logic.Task;
import pista.storage.Storage;

public class StorageUnitTest {

	private Storage mStorage = null;
	private CustomPreferences mPrefs = null;
	
	//file path shall not take it from preference
	//let use dummy task list for testing
	private final String nonExistFile = "abc.xml";
	private final String dummyFilePath = "dummy_task_list.xml";
	private final String wrongFormatFile1 = "wrong_format_1.xml";
	private final String wrongFormatFile2 = "wrong_format_2.xml";
	private final String emptyFormatFile = "empty_format.xml";
	@Before
	public void init(){
		mStorage = Storage.getInstance();	
		mPrefs = CustomPreferences.getInstance();
	}
	
	@Test
	public void test_Storage_File_Exist() {
		assertEquals("Is storage file path exists?", true, mStorage.isFileExist(dummyFilePath));
		assertEquals("Is storage file path exists?", false, mStorage.isFileExist(nonExistFile));
	}

	@Test
	public void test_Storage_File_Empty() {
		assertEquals("Is storage file path empty?", false, mStorage.isFileEmpty(dummyFilePath));
		assertEquals("Is storage file path empty?", true, mStorage.isFileEmpty(emptyFormatFile));
	}
	
	@Test
	public void test_Storage_File_Format_Valid() {
		assertEquals("Is storage file format valid?", true, mStorage.isFileFormatValid(dummyFilePath));
		assertEquals("Is storage file format valid?", false, mStorage.isFileFormatValid(wrongFormatFile1));
		assertEquals("Is storage file format valid?", false, mStorage.isFileFormatValid(wrongFormatFile2));
	}
	
	@Test 
	public void test_Storage_Set_Folder_location(){
		mStorage.setDataFolderLocation(dummyFilePath);
		assertEquals("Set correct location?", dummyFilePath, mStorage.getDataFolderLocation());
	}
	
	@Test
	public void test_Storage_Content(){
		mStorage.setDataFolderLocation(dummyFilePath);
		mStorage.load();
		ArrayList<Task> mList = mStorage.getTaskList();
		assertEquals("Assume task list size", 1, mList.size());
	}
	
}
