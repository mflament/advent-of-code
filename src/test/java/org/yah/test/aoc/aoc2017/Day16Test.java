package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day16.*;
import org.junit.Test;

public class Day16Test {

	@Test
	public void test() {
		Dance d = new Dance(5);
		Move[] moves = parseMoves("s1,x3/4,pe/b");
		d.execute(moves);
		assertEquals("baedc", d.toString());
		d.execute(moves);
		assertEquals("ceadb", d.toString());
	}

	@Test
	public void testMoves() {
		Dance d = new Dance(5);
		new Spin(3).execute(d);
		assertEquals("cdeab", d.toString());
	}

}
