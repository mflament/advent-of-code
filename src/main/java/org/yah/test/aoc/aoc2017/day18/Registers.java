package org.yah.test.aoc.aoc2017.day18;

final class Registers {

	private static int toIndex(char c) {
		return c - 'a';
	}

	private static char toChar(int index) {
		return (char) ('a' + index);
	}

	private final long[] values;

	public Registers() {
		super();
		values = new long[toIndex('p') + 1];
	}

	public long get(char c) {
		return values[toIndex(c)];
	}

	public void set(char register, long value) {
		values[toIndex(register)] = value;
	}

	public void add(char register, long value) {
		values[toIndex(register)] += value;
	}

	public void mul(char register, long value) {
		values[toIndex(register)] *= value;
	}

	public void mod(char register, long value) {
		values[toIndex(register)] %= value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			sb.append(toChar(i)).append('=').append(values[i]);
			if (i < values.length - 1)
				sb.append(", ");
		}
		return sb.toString();
	}

}