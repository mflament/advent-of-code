package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day06.*;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day06.Result;

public class Day06Test {

	@Test
	public void test() {
		Result result = reallocate(0, 2, 7, 0);
		assertEquals(5, result.cycles);
		assertEquals(4, result.loopSize);
	}

}
