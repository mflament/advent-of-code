package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day05.*;

import org.junit.Test;


public class Day05Test {

	@Test
	public void test() {
		int[] testProgram = new int[] { 0, 3, 0, 1, -3 };
		assertEquals(5, execute(cloneProgram(testProgram), forwardProcessor()));
		assertEquals(10, execute(testProgram, schizophrenicProcessor()));
		assertArrayEquals(program(2, 3, 2, 3, -1), testProgram);
	}

}
