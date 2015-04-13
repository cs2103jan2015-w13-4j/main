package pista.test;

import static org.junit.Assert.*;

import org.junit.Test;

import pista.Constants;
import pista.parser.MainParser;

public class ParserSortTest {

	@Test
	public void testParserSortAscendingTitleToken() {
		MainParser mp = MainParser.validateInput("sort ascending title");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSortAscendingTitleFailToken() {
		MainParser mp = MainParser.validateInput("sort ascendingtitle");
		assertEquals(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION, mp.getMessage());
	}
	
	@Test
	public void testParserSortOverviewToken() {
		MainParser mp = MainParser.validateInput("sort overview");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSortOverviewFailToken() {
		MainParser mp = MainParser.validateInput("sort overviews");
		assertEquals(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION, mp.getMessage());
	}
	
	@Test
	public void testParserSortAscendingStartDateToken() {
		MainParser mp = MainParser.validateInput("sort ascending start date");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSortAscendingStartDateFailToken() {
		MainParser mp = MainParser.validateInput("sort ascending startdate");
		assertEquals(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION, mp.getMessage());
	}
	
	@Test
	public void testParserSortDescendingStartDateToken() {
		MainParser mp = MainParser.validateInput("sort descending end date");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSortDescendingStartDateFailToken() {
		MainParser mp = MainParser.validateInput("sort descending enddate");
		assertEquals(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION, mp.getMessage());
	}
	
	@Test
	public void testParserSortAscendingPriorityToken() {
		MainParser mp = MainParser.validateInput("sort ascending priority");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSortAscendingPriorityFailToken() {
		MainParser mp = MainParser.validateInput("sort ascendingpriority");
		assertEquals(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION, mp.getMessage());
	}
	
	@Test
	public void testParserSortDescendingPriorityToken() {
		MainParser mp = MainParser.validateInput("sort descending priority");
		assertEquals(Constants.PARSER_MESSAGE_VALID_INPUT, mp.getMessage());
	}
	
	@Test
	public void testParserSortDescendingPriorityFailToken() {
		MainParser mp = MainParser.validateInput("sort descendingpriority");
		assertEquals(Constants.PARSER_MESSAGE_INVALID_SORT_FUNCTION, mp.getMessage());
	}
}
