package org.yah.test.aoc.aoc2017;

import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;

import org.yah.test.aoc.aoc2017.day18.ConcurrentPrograms;
import org.yah.test.aoc.aoc2017.day18.Program;
import org.yah.test.aoc.utils.DayExecutor;

public class Day18 {

	public static void main(String[] args) throws IOException {
		String[] instructions = readLines("day18.txt");
		DayExecutor.execute(18,
				() -> new Program(instructions).execute(),
				() -> new ConcurrentPrograms(instructions, 256).execute());

	}
}
