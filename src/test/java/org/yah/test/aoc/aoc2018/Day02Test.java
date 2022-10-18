package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class Day02Test {
	
	private static String[] INPUTS = { "abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab" };
	
	private static String[] INPUTS2 = { "abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz" };

	private Day02 day02;

	@Before
	public void setup() {
		day02 = new Day02();
	}

	@Test
	public void testPart1() {
		assertEquals(12, day02.part1(INPUTS)); 
	}

	@Test
	public void testPart2() {
		assertEquals("fgij", day02.part2(INPUTS2)); 
	}

}
