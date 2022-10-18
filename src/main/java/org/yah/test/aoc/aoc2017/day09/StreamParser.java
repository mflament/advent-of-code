package org.yah.test.aoc.aoc2017.day09;

import java.io.IOException;

public interface StreamParser {
	Group parse(String input) throws IOException;
}