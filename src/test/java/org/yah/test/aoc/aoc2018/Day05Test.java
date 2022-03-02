package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class Day05Test {

	private Day05 day05;

	@Before
	public void setup() throws IOException {
		day05 = new Day05();
	}

	@Test
	public void testPart1() {
		assertEquals(10, day05.part1("dabAcCaCBAcCcaDA"));
	}

	@Test
	public void testPart2() {
		assertEquals(4, day05.part2("dabAcCaCBAcCcaDA"));
	}
}
