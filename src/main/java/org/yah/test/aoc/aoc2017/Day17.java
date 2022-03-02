package org.yah.test.aoc.aoc2017;

import org.yah.test.aoc.utils.DayExecutor;

public class Day17 {
	private static final int INPUT = 324;

	private static final int VALUES = 50_000_000;

	static final class Spinlock {

		private final int[] buffer;

		private int size;

		private int position;

		public Spinlock(int length) {
			super();
			buffer = new int[length];
		}

		public int fill(int steps) {
			insert(0, 0);
			for (int i = 1; i < buffer.length; i++) {
				stepForward(steps);
				insert(i);
			}
			if (position < size - 1)
				return buffer[position + 1];
			throw new IllegalStateException("end of buffer " + this);
		}

		public int fillFast(int steps, int count) {
			int res = -1;
			size = 1;
			for (int i = 1; i < count; i++) {
				stepForward(steps);
				position += 1;
				size++;
				if (position == 1)
					res = i;
			}
			return res;
		}

		private void stepForward(int steps) {
			position += steps;
			if (position >= size)
				position = position % size;
		}

		public void insert(int v) {
			position += 1;
			insert(position, v);
		}

		public void insert(int index, int v) {
			int remaining = size - index;
			if (remaining > 0)
				System.arraycopy(buffer, index, buffer, index + 1, remaining);
			buffer[index] = v;
			size++;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < size; i++) {
				if (i == position)
					sb.append('(').append(buffer[i]).append(')');
				else
					sb.append(buffer[i]);
				if (i < size - 1)
					sb.append(' ');
			}
			return sb.toString();
		}
	}

	public static void main(String[] args) {
		test();

		DayExecutor.execute(17,
				() -> new Spinlock(2017).fill(INPUT),
				() -> new Spinlock(0).fillFast(INPUT, VALUES));
	}

	private static void test() {

	}
}
