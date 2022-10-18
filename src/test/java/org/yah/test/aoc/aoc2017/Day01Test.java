package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.assertEquals;
import static org.yah.test.aoc.aoc2017.Day01.computeHalf;
import static org.yah.test.aoc.aoc2017.Day01.computeNext;

import org.junit.Test;

public class Day01Test {

	@Test
	public void test() {
		assertEquals(3, computeNext("1122"));
		assertEquals(4, computeNext("1111"));
		assertEquals(0, computeNext("1234"));
		assertEquals(9, computeNext("91212129"));
		assertEquals(9, computeNext("91212129"));

		assertEquals(6, computeHalf("1212"));
		assertEquals(0, computeHalf("1221"));
		assertEquals(4, computeHalf("123425"));
		assertEquals(12, computeHalf("123123"));
		assertEquals(4, computeHalf("12131415"));
	}

}
