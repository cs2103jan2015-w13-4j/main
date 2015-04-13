//@author A0112522Y
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
		assertEquals("Test adding deadline Task",tLogic.runCommand("add",new String[] {"meeting1","today","2pm"}),Constants.LOGIC_SUCCESS_ADD_TASK);
		assertEquals("Test adding timed Task",tLogic.runCommand("add",new String[] {"meeting2","today","2pm","20/4/2015","10:00"}),Constants.LOGIC_SUCCESS_ADD_TASK);
		
	}
	
	@Test
	public void testEdit(){
		populateList();
		assertEquals("Test editing Task succesfully",tLogic.runCommand("edit",new String[] {"1","new title"}),Constants.LOGIC_SUCCESS_EDIT_TASK);
		assertEquals("Checking edited date",tLogic.getStorage().getTaskList().get(2).getTitle(),"new title");
		assertEquals("Test editing Task with ID out of range",tLogic.runCommand("edit",new String[] {"5","new title"}),Constants.LOGIC_EDIT_TASK_NOT_FOUND);
	}
	
	@Test
	public void testDelete(){
		populateList();
		assertEquals("Test delete a task",tLogic.runCommand("delete", new String[] {"1"}),Constants.LOGIC_SUCCESS_DELETE_TASK);
		assertEquals("Check if the task is deleted",tLogic.getStorage().getTaskList().size(),2);
		assertEquals("Test delete task that is non existent",tLogic.runCommand("delete", new String[] {"10"}),Constants.LOGIC_DELETE_TASK_NOT_FOUND);
	}
	
	@Test
	public void testPriority(){
		populateList();
		assertEquals("Test setting new priority",tLogic.runCommand("priority", new String[] {"1","3"}),Constants.LOGIC_SUCCESS_PRIORITY_TASK);
		assertEquals("Check if the task is updated",tLogic.getStorage().getTaskList().get(2).getPriority(),"3");
		assertEquals("Test setting for task that is not in the list",tLogic.runCommand("priority", new String[] {"10","3"}),Constants.LOGIC_FAIL_PRIORITY_NOT_FOUND_TASK);
	}
	
	@Test
	public void testMark(){
		populateList();
		assertEquals("Test marking a task as done",tLogic.runCommand("mark", new String[] {"1","done"}),Constants.LOGIC_SUCCESS_MARK_TASK);
		assertEquals("Check if the task is updated",tLogic.getStorage().getTaskList().get(2).getIsDone(),true);
		assertEquals("Test marking for task that is not in the list",tLogic.runCommand("mark", new String[] {"10","done"}),Constants.LOGIC_FAIL_MARK_NOT_FOUND_TASK);
		assertEquals("Test marking a task as undone",tLogic.runCommand("mark", new String[] {"1","undone"}),Constants.LOGIC_SUCCESS_MARK_TASK);
		assertEquals("Check if the task is updated",tLogic.getStorage().getTaskList().get(2).getIsDone(),false);
	}
	
	public void populateList(){
		tLogic.runCommand("add",new String[] {"meeting"});
		tLogic.runCommand("add",new String[] {"meeting1","today","2pm"});
		tLogic.runCommand("add",new String[] {"meeting2","today","2pm","20/4/2015","10:00"});
	}
}
