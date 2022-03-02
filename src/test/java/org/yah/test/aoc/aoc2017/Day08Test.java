package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.assertEquals;
import static org.yah.test.aoc.aoc2017.Day08.PARSER;

import java.io.IOException;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day08.ExecutionResult;
import org.yah.test.aoc.aoc2017.Day08.Program;

public class Day08Test {

	@Test
	public void test() throws IOException {
		Program program = PARSER.parse("day08-test.txt");
		ExecutionResult result = program.execute();
		assertEquals(1, result.currentMax);
	}

}
