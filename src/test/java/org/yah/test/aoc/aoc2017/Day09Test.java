package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day09.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.day09.Garbage;
import org.yah.test.aoc.aoc2017.day09.Group;
import org.yah.test.aoc.aoc2017.day09.GroupContent;
import org.yah.test.aoc.aoc2017.day09.StreamParser;

public class Day09Test {
	
	private static void countGarbage(StreamParser parser, String input, int expected) throws IOException {
		Group group = parser.parse(input);
		assertEquals(expected, group.garbageCount());
	}

	private static void checkScore(StreamParser parser, String input, int expected) throws IOException {
		Group group = parser.parse(input);
		assertEquals(expected, group.totalScore());
	}

	private static void countGroups(StreamParser parser, String input, int expected) throws IOException {
		Group group = parser.parse(input);
		assertEquals(expected, group.totalGroups());
	}

	private static void checkGabage(StreamParser parser, String input, String expected) throws IOException {
		Group group = parser.parse(input);
		List<GroupContent> content = group.getContent();
		assertFalse("no garbage found", content.isEmpty());
		assertTrue("not garbage", content.get(0) instanceof Garbage);
		assertEquals(expected, ((Garbage) content.get(0)).getContent());
	}

	@Test
	public void test_antlr() throws IOException {
		test(antlrParser());
	}

	@Test
	public void test_charbuffer() throws IOException {
		test(charBufferParser());
	}
	
	private static void test(StreamParser parser) throws IOException {
		checkGabage(parser, "{<>}", "");
		checkGabage(parser, "{<random characters>}", "random characters");
		checkGabage(parser, "{<<<<>}", "<<<");
		checkGabage(parser, "{<{!>}>}", "{}");
		checkGabage(parser, "{<!!>}", "");
		checkGabage(parser, "{<!!!>>}", "");
		checkGabage(parser, "{<{o\"i!a,<{i<a>}", "{o\"i,<{i<a");

		countGroups(parser, "{}", 1);
		countGroups(parser, "{{{}}}", 3);
		countGroups(parser, "{{},{}}", 3);
		countGroups(parser, "{{{},{},{{}}}}", 6);
		countGroups(parser, "{<{},{},{{}}>}", 1);
		countGroups(parser, "{<a>,<a>,<a>,<a>}", 1);
		countGroups(parser, "{{<a>},{<a>},{<a>},{<a>}}", 5);
		countGroups(parser, "{{<!>},{<!>},{<!>},{<a>}}", 2);

		checkScore(parser, "{}", 1);
		checkScore(parser, "{{{}}}", 6);
		checkScore(parser, "{{},{}}", 5);
		checkScore(parser, "{{{},{},{{}}}}", 16);
		checkScore(parser, "{<{},{},{{}}>}", 1);
		checkScore(parser, "{<a>,<a>,<a>,<a>}", 1);
		checkScore(parser, "{{<ab>},{<ab>},{<ab>},{<ab>}}", 9);
		checkScore(parser, "{{<!!>},{<!!>},{<!!>},{<!!>}}", 9);
		checkScore(parser, "{{<a!>},{<a!>},{<a!>},{<ab>}}", 3);

		countGarbage(parser, "{<>}", 0);
		countGarbage(parser, "{<random characters>}", 17);
		countGarbage(parser, "{<<<<>}", 3);
		countGarbage(parser, "{<{!>}>}", 2);
		countGarbage(parser, "{<!!>}", 0);
		countGarbage(parser, "{<!!!>>}", 0);
		countGarbage(parser, "{<{o\"i!a,<{i<a>}", 10);
	}

}
