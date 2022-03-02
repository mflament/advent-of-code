package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.aoc2018.Day09.Circle;

public class Day09Test {

	private Day09 day09;

	@Before
	public void setup() throws IOException {
		day09 = new Day09();
	}

	@Test
	public void testPart1() {
		assertEquals(32, day09.part1(9, 25));
		assertEquals(8317, day09.part1(10, 1618));
		assertEquals(146373, day09.part1(13, 7999));
		assertEquals(2764, day09.part1(17, 1104));
		assertEquals(54718, day09.part1(21, 6111));
		assertEquals(37305, day09.part1(30, 5807));
	}

	@Test
	public void testPart2() {
		day09.part2(9, 25);
	}
	
	@Test
	public void testSandbox() {
		Circle circle = new Circle(9, 96);
		System.out.println(circle);
		while (circle.play()) {
			System.out.println(circle);
		}
	}

}
