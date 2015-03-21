package pista.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pista.storage.Storage;

public class StorageUnitTest {

	private Storage mStorage;
	private String dummyFilePath = "init_task.xml";

	@Before
	public void init(){
		mStorage = Storage.getInstance();	
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
