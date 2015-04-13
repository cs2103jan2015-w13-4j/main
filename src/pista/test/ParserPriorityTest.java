//@author A0111884E
package pista.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pista.Constants;
import pista.parser.MainParser;

import com.joestelmach.natty.CalendarSource;

public class ParserPriorityTest {

	@Before
	public void setUp() throws Exception {
		Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("05/15/2015");	//mm/dd/yyyy FRI
		CalendarSource.setBaseDate(reference);
	}
	
	@Test
	public void testParserPriorityZeroToken() {
		MainParser mp = MainParser.validateInput("priority 1 -0");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
		
	}
	
	@Test
	public void testParserPriorityOneToken() {
		MainParser mp = MainParser.validateInput("priority 1 -1");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserPriorityTwoToken() {
		MainParser mp = MainParser.validateInput("priority 1 -2");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserPriorityThreeToken() {
		MainParser mp = MainParser.validateInput("priority 1 -3");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}	
	
	@Test
	public void testParserPriorityFourToken() {
		MainParser mp = MainParser.validateInput("priority 1 -4");
		assertEquals(Constants.PARSER_INVALID_PRIORITY_SCORE, mp.getMessage());
	}
	
	
}
