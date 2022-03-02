package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.assertEquals;
import static org.yah.test.aoc.aoc2017.Day03.manathanDistance;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day03.ResultsBuffer;


public class Day03Test {

	@Test
	public void test() {
		assertEquals(0, manathanDistance(1));
		assertEquals(3, manathanDistance(12));
		assertEquals(2, manathanDistance(23));
		assertEquals(31, manathanDistance(1024));

		testResultsBuffer(1, 0);
		testResultsBuffer(2, 1);
		testResultsBuffer(4, 2);
		testResultsBuffer(5, 4);
		testResultsBuffer(10, 5);
		testResultsBuffer(11, 10);
		testResultsBuffer(23, 11);
		testResultsBuffer(25, 23);
		testResultsBuffer(26, 25);
		testResultsBuffer(54, 26);
		testResultsBuffer(57, 54);
		testResultsBuffer(59, 57);
		testResultsBuffer(122, 59);
		testResultsBuffer(133, 122);
		testResultsBuffer(142, 133);
		testResultsBuffer(147, 142);
		testResultsBuffer(304, 147);
		testResultsBuffer(330, 304);
		testResultsBuffer(351, 330);
		testResultsBuffer(362, 351);
		testResultsBuffer(747, 362);
		testResultsBuffer(806, 747);
	}

	private static void testResultsBuffer(int expected, int max) {
		ResultsBuffer buffer = new ResultsBuffer(5);
		assertEquals(expected, buffer.compute(max));
	}
}
