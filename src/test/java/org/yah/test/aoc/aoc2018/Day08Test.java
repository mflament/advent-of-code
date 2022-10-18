package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.aoc2018.Day08.Node;

public class Day08Test {

	private Day08 day08;
	
	private Node tree;

	@Before
	public void setup() throws IOException {
		day08 = new Day08();
		tree = Node.parse("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2");
	}

	@Test
	public void testPart1() {
		assertEquals(138, day08.part1(tree));
	}

	@Test
	public void testPart2() {
		assertEquals(66, day08.part2(tree));
	}
}
