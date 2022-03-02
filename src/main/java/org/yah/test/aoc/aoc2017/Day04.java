package org.yah.test.aoc.aoc2017;

import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.yah.test.aoc.utils.DayExecutor;

public class Day04 {

	@FunctionalInterface
	private interface PassphraseValidator {
		boolean validate(String passphrase);
	}

	private static int validate(String[] lines, PassphraseValidator validator) {
		int res = 0;
		for (String line : lines) {
			if (validator.validate(line))
				res++;
		}
		return res;
	}

	public static boolean validateNoDuplicate(String string) {
		String[] split = string.split("\\s+");
		Set<String> set = new HashSet<>(split.length);
		for (int i = 0; i < split.length; i++) {
			if (!set.add(split[i]))
				return false;
		}
		return true;
	}

	public static boolean validateNoAnagram(String string) {
		String[] split = string.split("\\s+");
		Set<String> set = new HashSet<>(split.length);
		for (int i = 0; i < split.length; i++) {
			String word = split[i];
			if (set.stream().filter(s -> isAnagram(s, word)).findFirst().isPresent())
				return false;
			if (!set.add(split[i]))
				return false;
		}
		return true;
	}

	private static boolean isAnagram(String s1, String s2) {
		if (s1.length() != s2.length())
			return false;
		int[] cc1 = countChars(s1);
		int[] cc2 = countChars(s2);
		return Arrays.equals(cc1, cc2);
	}

	private static int[] countChars(String s) {
		int[] res = new int[26];
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			res[c - 'a']++;
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		String[] lines = readLines("day04.txt");
		DayExecutor.execute(4,
				() -> validate(lines, Day04::validateNoDuplicate),
				() -> validate(lines, Day04::validateNoAnagram));
	}


}
