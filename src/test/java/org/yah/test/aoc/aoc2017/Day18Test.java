package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.day18.ConcurrentPrograms;
import org.yah.test.aoc.aoc2017.day18.Program;


public class Day18Test {

	@Test
	public void test() throws IOException {
		String[] instructions = readLines("day18-test1.txt");
		Program program = new Program(instructions);
		assertEquals(4, program.execute());

		instructions = readLines("day18-test2.txt");
		assertEquals(3, new ConcurrentPrograms(instructions, 3).execute());
	}

}
