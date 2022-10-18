package org.yah.test.aoc.aoc2017;

import java.io.IOException;

import org.yah.test.aoc.aoc2017.day09.AntlrStreamParser;
import org.yah.test.aoc.aoc2017.day09.CharBufferParser;
import org.yah.test.aoc.aoc2017.day09.Group;
import org.yah.test.aoc.aoc2017.day09.StreamParser;
import org.yah.test.aoc.utils.DayExecutor;

@SuppressWarnings("unused")
public class Day09 {

	static StreamParser antlrParser() {
		return new AntlrStreamParser();
	}

	static StreamParser charBufferParser() {
		return new CharBufferParser();
	}

	private static boolean timed = true;

	private static StreamParser parser = charBufferParser();

	public static void main(String[] args) throws IOException {
		String input = IOUtils2017.toString("day09.txt");
		Group group = parser.parse(input);
		DayExecutor.execute(9, group::totalScore, group::garbageCount);
	}


}
