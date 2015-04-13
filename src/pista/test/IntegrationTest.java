package pista.test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pista.Constants;
import pista.ui.UIController;

public class IntegrationTest {
	UIController tUICtrl = null;
	
	@Before
	public void init(){
		tUICtrl = new UIController();
		tUICtrl.executeCommandForTest("set file location -test.xml");
	}
	
	@Test
	public void testAdd(){
		assertEquals(Constants.PARSER_MESSAGE_INVALID_TOKEN_LENGTH, tUICtrl.executeCommandForTest("add"));
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add floatingtask"));
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add deadline task -12/05/2015 -14:14") );
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add timed -12/05/2015 -14:14 -13/7/2015 -15:15"));
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add deadlinetask -sun -noon"));
	}
	
	@Test
	public void testDelete(){
		assertEquals(Constants.LOGIC_DELETE_TASK_NOT_FOUND, tUICtrl.executeCommandForTest("delete 1"));
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK,tUICtrl.executeCommandForTest("add timed -7/7 -8p -10/7 -10a"));
		assertEquals(Constants.LOGIC_SUCCESS_DELETE_TASK, tUICtrl.executeCommandForTest("delete 1"));
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add deadlinetask -tomorrow -8p"));
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add timed -7/7 -8p -10/7 -10a"));
		assertEquals(Constants.LOGIC_SUCCESS_DELETE_ALL_TASKS, tUICtrl.executeCommandForTest("delete a"));
	}
	
	@Test
	public void testPriority(){
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add floating"));
		assertEquals(Constants.PARSER_INVALID_PRIORITY_SCORE, tUICtrl.executeCommandForTest("priority 1 --1"));
		assertEquals(Constants.PARSER_INVALID_PRIORITY_SCORE, tUICtrl.executeCommandForTest("priority 1 -4"));
		assertEquals(Constants.LOGIC_SUCCESS_PRIORITY_TASK, tUICtrl.executeCommandForTest("priority 1 -3"));
	}
	
	@Test
	public void testReminderFloating(){
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add floating"));
		assertEquals(Constants.LOGIC_FAIL_REMIND_FLOATING_TASK, tUICtrl.executeCommandForTest("remind 1-today -5p"));
	}
	
	@Test
	public void testEdit(){
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add deadlinetask -today -noon"));
		assertEquals(Constants.LOGIC_SUCCESS_EDIT_TASK, tUICtrl.executeCommandForTest("edit 1-d-tomorrow -night"));
		assertEquals(Constants.PARSER_MESSAGE_EDIT_EMPTY_TOKENS, tUICtrl.executeCommandForTest("edit 2"));
	}
	
	@Test
	public void testReminder(){
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add deadlinetask -today -noon"));
		assertEquals(Constants.LOGIC_FAIL_REMIND_LATER_THAN_ENDDATE_TASK, tUICtrl.executeCommandForTest("remind 1-tomorrow -night"));
		assertEquals(Constants.LOGIC_SUCCESS_REMIND_TASK, tUICtrl.executeCommandForTest("remind 1-yesterday -morning"));
	}
	
	@Test
	public void testMark(){
		assertEquals(Constants.LOGIC_SUCCESS_ADD_TASK, tUICtrl.executeCommandForTest("add deadlinetask -today -noon"));
		assertEquals(Constants.LOGIC_FAIL_MARK_NOT_FOUND_TASK, tUICtrl.executeCommandForTest("mark 2-done"));
		assertEquals(Constants.LOGIC_SUCCESS_MARK_TASK, tUICtrl.executeCommandForTest("mark 1-done"));
		assertEquals(Constants.LOGIC_FAIL_MARK_NOT_FOUND_TASK, tUICtrl.executeCommandForTest("mark 2-undone"));
		assertEquals(Constants.LOGIC_SUCCESS_MARK_TASK, tUICtrl.executeCommandForTest("mark 1-undone"));
	}
	
	@After
	public void clear(){
		tUICtrl.executeCommandForTest("delete a");
	}
}
