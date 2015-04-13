package pista.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pista.Constants;
import pista.logic.Logic;
import pista.logic.Task;

public class LogicTest {
	Logic tLogic;
	
	@Before
	public void init(){
		ArrayList<Task> tempList = new ArrayList<Task>();
		tLogic = Logic.getInstance();
		tLogic.initStorage();
		tLogic.initLogging();
		tLogic.initPreference();
		tLogic.getStorage().setDataFileLocation("test.xml");
		tLogic.getStorage().setTaskList(tempList);
	}
	
	@Test
	public void testAdd(){
		assertEquals("Test adding floating Task",tLogic.runCommand("add",new String[] {"meeting"}),Constants.LOGIC_SUCCESS_ADD_TASK);
		assertEquals("Test adding deadline Task",tLogic.runCommand("add",new String[] {"meeting1,today,2pm"}),Constants.LOGIC_SUCCESS_ADD_TASK);
		assertEquals("Test adding timed Task",tLogic.runCommand("add",new String[] {"meeting2,today,2pm,20/4/2015,10:00"}),Constants.LOGIC_SUCCESS_ADD_TASK);
		
	}
	
	@Test
	public void testEdit(){
		populateList();
		// boundary case of positive value within the size of the taskList
		assertEquals(tLogic.runCommand("edit",new String[] {"1,new title"}),Constants.LOGIC_SUCCESS_EDIT_TASK);
//		assertEquals(tLogic.edit(new String[] {"7", "meeting"}),Constants.LOGIC_SUCCESS_EDIT_TASK);
	}
	
	public void populateList(){
		tLogic.runCommand("add",new String[] {"meeting"});
		tLogic.runCommand("add",new String[] {"meeting1,today,2pm"});
		tLogic.runCommand("add",new String[] {"meeting2,today,2pm,20/4/2015,10:00"});
	}
}
