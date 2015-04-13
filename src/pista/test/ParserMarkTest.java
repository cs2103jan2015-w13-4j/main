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

public class ParserMarkTest {

	@Before
	public void setUp() throws Exception {
		Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("05/15/2015");	//mm/dd/yyyy FRI
		CalendarSource.setBaseDate(reference);
	}
	
	@Test
	public void testParserMarkDoneToken() {
		MainParser mp = MainParser.validateInput("mark 1 -done");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
		
	}
	
	@Test
	public void testParserMarkUndoneToken() {
		MainParser mp = MainParser.validateInput("mark 1 -undone");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserMarkUndoneFailToken() {
		MainParser mp = MainParser.validateInput("mark 1 -undonedfgh");
		assertEquals(Constants.PARSER_INVALID_MARK, mp.getMessage());
	}
	
	@Test
	public void testParserMarkDoneFailToken() {
		MainParser mp = MainParser.validateInput("mark 1 -done reg");
		assertEquals(Constants.PARSER_INVALID_MARK, mp.getMessage());
	}
	
}
