package org.yah.test.aoc.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.utils.StringUtils;

public class StringUtilsTest {

	@Before
	public void setUp() throws Exception {}

	@Test
	public void testCR_LF() {
		String input = "abcd\r\nef\r\ngh";
		assertArrayEquals(new String[] { "abcd", "ef", "gh" }, StringUtils.lines(input));
	}

	@Test
	public void test_LF() {
		String input = "abcd\nef\ngh";
		assertArrayEquals(new String[] { "abcd", "ef", "gh" }, StringUtils.lines(input));
	}

	@Test
	public void testEmptyLines() {
		String input = "\n\nabcd\nef\n\ngh\n\n\n";
		assertArrayEquals(new String[] { "", "", "abcd", "ef", "", "gh", "", "", "" }, StringUtils.lines(input));
	}

	@Test
	public void testNonEmptyLines() {
		String input = "\n\nabcd\nef\n\n\ngh\n\n\n";
		assertArrayEquals(new String[] { "abcd", "ef", "gh" }, StringUtils.nonEmptyLines(input));
	}
}
