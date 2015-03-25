package pista.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Date;

import pista.Constants;
import pista.parser.MainParser;
import pista.logic.Logic;
import com.joestelmach.natty.CalendarSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import pista.storage.Storage;

public class ParserAddTest {
	

	@Before
	public void setUp() throws Exception {
		Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("05/15/2015");	//mm/dd/yyyy FRI
		CalendarSource.setBaseDate(reference);
	}

	@After
	public void tearDown() throws Exception {
		//Logic.getStorageList().clear();
	}

	@Test
	public void testParserAddWithoutToken() {
		MainParser mp = MainParser.validateInput("add");
		assertEquals(Constants.MESSAGE_INVALID_TOKEN_LENGTH,mp.getMessage());
	}
	
	@Test
	public void testParserAddFloatedToken() {
		MainParser mp = MainParser.validateInput("add floatedtask");
		assertEquals(Constants.MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserAddDeadlineToken() {
		MainParser mp = MainParser.validateInput("add deadlinetask -12/05/2015 -14:14");
		assertEquals(Constants.MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserAddTimedToken() {
		MainParser mp = MainParser.validateInput("add deadlinetask -12/05/2015 -14:14 -13/7/2015 -15:15");
		assertEquals(Constants.MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testDeadlineFlexibleDates() {
		MainParser mp = MainParser.validateInput("add deadlinetask -sun -noon");
		assertEquals("17/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("12:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
		
		mp = MainParser.validateInput("add deadlinetask -2 fri -evening");
		assertEquals("29/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("19:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
		
		mp = MainParser.validateInput("add deadlinetask -yesterday -morning");
		assertEquals("14/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("08:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
		
		mp = MainParser.validateInput("add deadlinetask -next year -5pm");
		assertEquals("15/05/2016", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("17:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
		
		mp = MainParser.validateInput("add deadlinetask -tomorrow -8p");
		assertEquals("16/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("20:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
		
		mp = MainParser.validateInput("add deadlinetask -2 days -midnight");
		assertEquals("17/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("00:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
		
		mp = MainParser.validateInput("add deadlinetask -last week -night");
		assertEquals("08/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDDATE));
		assertEquals("20:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_DEADLINE_ENDTIME));
	}
	
	@Test
	public void testTimedFlexibleDates() {
		MainParser mp = MainParser.validateInput("add timed -0001/0000011/2015 -0622h -1/12/2015 -07:33h");
		assertEquals("01/11/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTDATE));
		assertEquals("06:22", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTTIME));
		assertEquals("01/12/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDDATE));
		assertEquals("07:33", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDTIME));
		
		mp = MainParser.validateInput("add timed -5/7/2015 -08:57 hours -6.9.2015 -19");
		assertEquals("05/07/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTDATE));
		assertEquals("08:57", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTTIME));
		assertEquals("06/09/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDDATE));
		assertEquals("19:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDTIME));
		
		mp = MainParser.validateInput("add timed -7/8 -8p -10/7 -10a");
		assertEquals("07/08/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTDATE));
		assertEquals("20:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTTIME));
		assertEquals("10/07/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDDATE));
		assertEquals("10:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDTIME));
		
		mp = MainParser.validateInput("add timed -8 april -4 hours before noon -10 may -5 hours after night");
		assertEquals("08/04/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTDATE));
		assertEquals("08:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTTIME));
		assertEquals("10/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDDATE));
		assertEquals("01:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDTIME));
		
		mp = MainParser.validateInput("add timed -tues last week -1 hours after noon -in a week -5 hours after midnight");
		assertEquals("05/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTDATE));
		assertEquals("13:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_STARTTIME));
		assertEquals("22/05/2015", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDDATE));
		assertEquals("05:00", mp.getItemInTokenIndex(Constants.ADD_TOKEN_TIMED_ENDTIME));
		
		mp = MainParser.validateInput("add timed -tomorrow -1 hours after noon -yesterday -5 hours after midnight");
		assertEquals(Constants.MESSAGE_STARTDATE_GREATER_THAN_ENDDATE, mp.getMessage());

	}

}
