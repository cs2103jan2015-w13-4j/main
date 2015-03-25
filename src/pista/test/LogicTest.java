package pista.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pista.Constants;
import pista.logic.Logic;

public class LogicTest {
	Logic tLogic;
	
	@Before
	public void init(){
		tLogic = Logic.getInstance();
	}
	
	@Test
	public void deleteTest(){
		// boundary case of positive value within the size of the taskList
		assertEquals(tLogic.delete(new String[] {"a"}),Constants.LOGIC_SUCCESS_DELETE_ALL_TASKS);
		assertEquals(tLogic.delete(new String[] {"1"}),Constants.LOGIC_SUCCESS_DELETE_TASK);
		assertEquals(tLogic.delete(new String[] {"0"}),Constants.LOGIC_DELETE_TASK_NOT_FOUND);
		assertEquals(tLogic.delete(new String[] {"1000"}),Constants.LOGIC_DELETE_TASK_NOT_FOUND);
	}
}
