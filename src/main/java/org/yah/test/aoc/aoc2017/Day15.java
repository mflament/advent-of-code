package org.yah.test.aoc.aoc2017;

import java.util.function.LongPredicate;

import org.yah.test.aoc.utils.DayExecutor;


public class Day15 {
	static final int GENERATOR_A_FACTOR = 16807;
	static final int GENERATOR_B_FACTOR = 48271;

	static final LongPredicate A_PREDICATE = v -> v % 4 == 0;
	static final LongPredicate B_PREDICATE = v -> v % 8 == 0;

	static final long K = 2147483647;

	static final int LOTS_OF_PAIRS = 40_000_000;
	static final int LESS_PAIRS = 5_000_000;

	static class Generator {
		private final long factor;

		private final LongPredicate valuePredicate;

		private long lastValue;

		public Generator(int start, int factor, LongPredicate valuePredicate) {
			super();
			this.lastValue = start;
			this.valuePredicate = valuePredicate;
			this.factor = factor;
		}

		public long generate() {
			do {
				lastValue = (lastValue * factor) % K;
			} while (!valuePredicate.test(lastValue));
			return lastValue;
		}
	}

	static class Judge {

		private int matches;

		private final Generator a, b;

		public Judge(int a, int b, boolean pickyGenerators) {
			super();
			this.a = new Generator(a, GENERATOR_A_FACTOR, pickyGenerators ? A_PREDICATE : v -> true);
			this.b = new Generator(b, GENERATOR_B_FACTOR, pickyGenerators ? B_PREDICATE : v -> true);
		}

		public int firstFirst() {
			int res = 1;
			while (!compare(a.generate(), b.generate()))
				res++;
			return res;
		}

		public int render(int samples) {
			for (int i = 0; i < samples; i++) {
				if (compare(a.generate(), b.generate()))
					matches++;
			}
			return matches;
		}

		static boolean compare(long a, long b) {
			return (a & 0xFFFF) == (b & 0xFFFF);
		}
	}

	public static void main(String[] args) {
		Judge judge1 = new Judge(679, 771, false);
		Judge judge2 = new Judge(679, 771, true);
		DayExecutor.execute(15, () -> judge1.render(LOTS_OF_PAIRS), () -> judge2.render(LESS_PAIRS));
	}
}
