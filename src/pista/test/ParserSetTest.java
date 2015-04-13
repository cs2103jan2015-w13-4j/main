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

public class ParserSetTest {

	@Before
	public void setUp() throws Exception {
		Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("05/15/2015");	//mm/dd/yyyy FRI
		CalendarSource.setBaseDate(reference);
	}
	
	@Test
	public void testParserSetToken() {
		MainParser mp = MainParser.validateInput("set file location -C:\task.xml");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSetFailToken() {
		MainParser mp = MainParser.validateInput("set file location -C:\task");
		assertEquals(Constants.LOGIC_INVALID_SET_FILE_NAME, mp.getMessage());
	}
}
