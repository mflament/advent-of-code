package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.aoc2018.Day04.GuardLogs;

public class Day04Test {

	private Day04 day04;

	private GuardLogs recordsLog;

	@Before
	public void setup() throws IOException {
		day04 = new Day04();
		recordsLog = GuardLogs.parse("day04-test.txt");
	}

	@Test
	public void testPart1() {
		assertEquals(240, day04.part1(recordsLog));
	}

	@Test
	public void testPart2() {
		assertEquals(4455, day04.part2(recordsLog));
	}
}
