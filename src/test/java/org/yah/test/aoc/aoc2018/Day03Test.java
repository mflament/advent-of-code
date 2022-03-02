package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.aoc2018.Day03.Claim;
import org.yah.test.aoc.aoc2018.Day03.Rectangle;

public class Day03Test {

	private Day03 day03;

	private List<Claim> claims;

	@Before
	public void setup() throws IOException {
		day03 = new Day03();
		claims = Day03.readClaims("day03-test.txt");
	}

	@Test
	public void testPart1() {
		assertEquals(4, day03.part1(claims));
	}

	@Test
	public void testPart2() {
		assertEquals(3, day03.part2(claims));
	}

	@Test
	public void testRectangle_expand() {
		Rectangle r = new Rectangle(1, 1, 1, 1);
		r.expand(new Rectangle(2, 2, 2, 2));
		assertEquals(new Rectangle(1, 1, 3, 3), r);

		r = new Rectangle(2, 2, 2, 2);
		r.expand(new Rectangle(1, 1, 1, 1));
		assertEquals(new Rectangle(1, 1, 3, 3), r);
	}
}
