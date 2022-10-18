package org.yah.test.aoc.aoc2018;

import java.io.IOException;

import org.yah.test.aoc.utils.DayExecutor;

public class Day05 {

	public static class Polymer {

		private final StringBuilder buffer;

		private int index;

		public Polymer(String input) {
			this(new StringBuilder(input));
		}

		public Polymer(StringBuilder buffer) {
			this.buffer = buffer;
		}

		@Override
		public String toString() {
			return buffer.toString();
		}

		public int react() {
			while (index < buffer.length() - 1) {
				char current = buffer.charAt(index);
				char next = buffer.charAt(index + 1);
				if (react(current, next)) {
					buffer.delete(index, index + 2);
					if (index > 0)
						index--;
				} else {
					index++;
				}
			}
			return buffer.length();
		}

		public Polymer filter(char c) {
			StringBuilder sb = new StringBuilder(buffer.capacity());
			for (int i = 0; i < length(); i++) {
				char current = buffer.charAt(i);
				if (!isSameType(current, c))
					sb.append(current);
			}
			return new Polymer(sb);
		}

		private boolean react(char a, char b) {
			return isSameType(a, b) && !isSamePolarity(a, b);
		}

		private boolean isSameType(char a, char b) {
			return Character.toLowerCase(a) == Character.toLowerCase(b);
		}

		private boolean isSamePolarity(char a, char b) {
			return Character.isLowerCase(a) == Character.isLowerCase(b);
		}

		public int length() {
			return buffer.length();
		}
	}

	public int part1(String input) {
		return new Polymer(input).react();
	}

	public int part2(String input) {
		Polymer polymer = new Polymer(input);
		int best = polymer.filter('a').react();
		for (char c = 'b'; c < 'z'; c++) {
			best = Math.min(polymer.filter(c).react(), best);
		}
		return best;
	}

	public static void main(String[] args) throws IOException {
		Day05 day5 = new Day05();
		String s = IOUtils2018.toString("day05.txt").trim();
		DayExecutor.execute(5, () -> day5.part1(s), () -> day5.part2(s));
	}
}
