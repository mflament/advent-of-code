package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.yah.test.aoc.aoc2017.Day04.validateNoAnagram;
import static org.yah.test.aoc.aoc2017.Day04.validateNoDuplicate;

import org.junit.Test;

public class Day04Test {

	@Test
	public void test() {
		assertTrue(validateNoDuplicate("aa bb cc dd ee"));
		assertFalse(validateNoDuplicate("aa bb cc dd aa"));
		assertTrue(validateNoDuplicate("aa bb cc dd aaa"));

		assertTrue(validateNoAnagram("aa bb cc dd ee"));
		assertFalse(validateNoAnagram("abcde xyz ecdab"));
		assertTrue(validateNoAnagram("a ab abc abd abf abj"));
		assertTrue(validateNoAnagram("iiii oiii ooii oooi oooo"));
		assertFalse(validateNoAnagram("oiii ioii iioi iiio"));
	}

}
