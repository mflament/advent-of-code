package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.assertEquals;
import static org.yah.test.aoc.aoc2017.Day02.*;

import java.io.IOException;

import org.junit.Test;


public class Day02Test {

	@Test
	public void test() throws IOException {
		int[][] inputs = loadInputs("day02-sample-1.txt");
		assertEquals(18, rangeChecksum(inputs));
		inputs = loadInputs("day02-sample-2.txt");
		assertEquals(9, integerDivChecksum(inputs));
	}

}
