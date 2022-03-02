package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class Day01Test {

	private Day01 day01;

	@Before
	public void setup() {
		day01 = new Day01();
	}

	@Test
	public void testPart1() {
		assertEquals(3, day01.part1(1, -2, 3, 1));
		assertEquals(3, day01.part1(1, 1, 1));
		assertEquals(0, day01.part1(1, 1, -2));
		assertEquals(-6, day01.part1(-1, -2, -3));
	}

	@Test
	public void testPart2() {
		Day01 day01 = new Day01();
		assertEquals(2, day01.part2(1, -2, 3, 1));
		assertEquals(0, day01.part2(1, -1));
		assertEquals(5, day01.part2(-6, 3, 8, 5, -6));
		assertEquals(14, day01.part2(7, 7, -2, -7, -4));
	}

}
