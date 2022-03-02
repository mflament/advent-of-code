package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.util.Arrays;

import org.yah.test.aoc.utils.DayExecutor;


public class Day02 {

	public Day02() {}

	private static class CountResult {

		private int two, three;

		public void set(int count) {
			if (count == 2)
				two++;
			else if (count == 3)
				three++;
		}

		public void add(CountResult cr) {
			if (cr.two > 0)
				two++;
			if (cr.three > 0)
				three++;
		}

		public int checksum() {
			return two * three;
		}
	}

	public int part1(String[] inputs) {
		CountResult res = new CountResult();
		Arrays.stream(inputs).map(Day02::count).forEach(c -> res.add(c));
		return res.checksum();
	}

	public String part2(String[] inputs) {
		String res = null;
		for (int i = 0; i < inputs.length - 1; i++) {
			for (int j = i + 1; j < inputs.length; j++) {
				String diff = createDiff(inputs[i], inputs[j]);
				if (diff != null) {
					if (res != null) {
						throw new IllegalStateException(
								"More than one single diff result found" + diff + " / " + res);
					}
					res = diff;
				}
			}
		}
		if (res == null)
			throw new IllegalStateException("No diff found");
		return res;
	}

	private static CountResult count(String input) {
		input = input.toLowerCase();
		int[] counts = new int[26];
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			counts[c - 'a']++;
		}

		CountResult res = new CountResult();
		for (int i = 0; i < counts.length; i++) {
			res.set(counts[i]);
		}
		return res;
	}

	private static String createDiff(String a, String b) {
		if (a.length() != b.length())
			throw new IllegalArgumentException("Mismatched id length " + a + " / " + b);
		int diffIndex = -1;
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != b.charAt(i)) {
				if (diffIndex < 0)
					diffIndex = i;
				else // more than one diff, no match
					return null;
			}
		}
		if (diffIndex < 0)
			throw new IllegalStateException("Identical inputs " + a);
		return a.substring(0, diffIndex) + a.substring(diffIndex + 1);
	}

	public static void main(String[] args) throws IOException {
		Day02 day02 = new Day02();
		String[] inputs = IOUtils2018.readLines("day02.txt");
		DayExecutor.execute(2, () -> day02.part1(inputs), () -> day02.part2(inputs));
	}
}
