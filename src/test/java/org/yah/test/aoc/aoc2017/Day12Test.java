package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;
import static org.yah.test.aoc.aoc2017.Day12.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class Day12Test {

	@Test
	public void test() throws IOException {
		String[] lines = readLines("day12-test.txt");
		testParse(lines);

		ProgramNode[] graph = parse(lines);
		Set<ProgramNode> connected = graph[0].collectConnected();
		assertEquals(6, connected.size());
		assertTrue(connected.containsAll(Arrays.asList(graph[0], graph[2], graph[3], graph[4], graph[5], graph[6])));

		List<Set<ProgramNode>> groups = programGroups(graph);
		assertEquals(2, groups.size());
	}


	private static void testParse(String[] lines) throws IOException {
		ProgramNode[] graph = parse(lines);
		int index = 0;
		for (String line : lines) {
			assertEquals(line, graph[index++].toString());
		}
	}

}
