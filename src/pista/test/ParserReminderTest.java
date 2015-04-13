package pista.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pista.Constants;
import pista.parser.MainParser;

import com.joestelmach.natty.CalendarSource;

public class ParserReminderTest {

	@Before
	public void setUp() throws Exception {
		Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("05/15/2015");	//mm/dd/yyyy FRI
		CalendarSource.setBaseDate(reference);
	}
	
	@Test
	public void testParserReminderToken() {
		MainParser mp = MainParser.validateInput("remind 1 -17 may -5p");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
		
	}
	
	@Test
	public void testParserReminderDateFailToken() {
		MainParser mp = MainParser.validateInput("remind 1 -17may -5p");
		assertEquals(Constants.PARSER_MESSAGE_INVALD_NATTY_DATE, mp.getMessage());
	}
}
