package org.yah.test.aoc.aoc2017.day09;

import java.io.EOFException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CharBufferParser implements StreamParser {
	private static final char GROUP_START = '{';
	private static final char GROUP_END = '}';
	private static final char GARBAGE_START = '<';
	private static final char GARBAGE_END = '>';
	private static final char CANCEL_PREFIX = '!';
	private static final char GROUP_SEPARATOR = ',';

	@Override
	public Group parse(String input) throws IOException {
		CharBuffer buffer = CharBuffer.wrap(input.trim());
		return parseGroup(buffer, 1);
	}

	private Group parseGroup(CharBuffer buffer, int score) throws IOException {
		List<GroupContent> contents = new LinkedList<>();
		consume(buffer, GROUP_START);
		do {
			GroupContent content;
			if (compare(buffer, GROUP_START))
				content = parseGroup(buffer, score + 1);
			else if (compare(buffer, GARBAGE_START))
				content = parseGarbage(buffer);
			else if (compare(buffer, GROUP_END)) {
				buffer.get();
				break;
			} else
				throw newUnexpectedTokenException(buffer, GROUP_START, GROUP_END, GARBAGE_START);

			contents.add(content);

			char token = get(buffer);
			if (token == GROUP_END)
				break;
			if (token != GROUP_SEPARATOR)
				throw newUnexpectedTokenException(buffer, buffer.position() - 1, token, GROUP_END, GROUP_SEPARATOR);
		} while (true);
		return new Group(contents, score);
	}

	private GroupContent parseGarbage(CharBuffer buffer) throws IOException {
		StringBuilder content = new StringBuilder();
		consume(buffer, GARBAGE_START);
		do {
			if (compare(buffer, CANCEL_PREFIX)) {
				get(buffer); // CANCEL_PREFIX
				get(buffer);// cancelled char
			} else {
				char token = get(buffer);
				if (token == GARBAGE_END)
					break;
				content.append(token);
			}
		} while (true);
		return new Garbage(content.toString());
	}

	private char get(CharBuffer buffer) throws IOException {
		if (!buffer.hasRemaining())
			throw new EOFException();
		return buffer.get();
	}

	private boolean compare(CharBuffer buffer, char expected) throws EOFException {
		if (!buffer.hasRemaining())
			return false;
		return peek(buffer) == expected;
	}

	private char peek(CharBuffer buffer) throws EOFException {
		if (!buffer.hasRemaining())
			throw new EOFException();
		return buffer.get(buffer.position());
	}

	private void consume(CharBuffer buffer, char expected) throws IOException {
		if (!buffer.hasRemaining())
			throw new EOFException();
		char actual = get(buffer);
		if (actual != expected)
			newUnexpectedTokenException(buffer, buffer.position() - 1, actual, expected);
	}

	private IOException newUnexpectedTokenException(CharBuffer buffer, char... expected) throws IOException {
		return newUnexpectedTokenException(buffer, buffer.position(), peek(buffer), expected);
	}

	private IOException newUnexpectedTokenException(CharBuffer buffer, int position, char actual,
			char... expected) {
		return new IOException(String.format("Unexpected token '%s' at index %d, expecting '%s'", actual, position,
				Arrays.toString(expected)));
	}
}