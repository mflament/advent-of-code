package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.aoc2018.Day07.Steps;

public class Day07Test {

	private Day07 day07;

	private Steps steps;

	@Before
	public void setup() throws IOException {
		day07 = new Day07(2, 0);
		steps = Steps.parse("day07-test.txt");
	}

	@Test
	public void testPart1() {
		assertEquals("CABDFE", day07.part1(steps));
	}

	@Test
	public void testPart2() {
		assertEquals(15, day07.part2(steps));
	}
}
