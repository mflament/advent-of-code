package org.yah.test.aoc.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	private static final Pattern LINE_SEPARATOR_PATTERN = Pattern.compile("\r?\n");

	private static final String[] EMPTY_RESULT = new String[0];

	private StringUtils() {}

	public static String[] nonEmptyLines(String text) {
		return lines(text, StringUtils::trimToNull);
	}

	public static String trimToNull(String s) {
		if (s == null)
			return null;
		s = s.trim();
		return s.length() == 0 ? null : s;
	}

	public static String[] lines(String text) {
		return lines(text, Function.identity());
	}

	public static String[] lines(String text, Function<String, String> transformer) {
		if (text == null)
			return EMPTY_RESULT;

		Matcher matcher = LINE_SEPARATOR_PATTERN.matcher(text);
		List<String> results = new LinkedList<>();
		int start = 0;
		while (matcher.find()) {
			String part = text.substring(start, matcher.start());
			collect(part, transformer, results);
			start = matcher.end();
		}
		collect(text.substring(start), transformer, results);
		return results.toArray(new String[results.size()]);
	}

	private static final void collect(String part, Function<String, String> transformer, List<String> results) {
		part = transformer.apply(part);
		if (part != null)
			results.add(part);
	}

}
