package org.yah.test.aoc.aoc2017;

import org.yah.test.aoc.utils.DayExecutor;

public class Day10 {

	private static final String INPUT = "120,93,0,90,5,80,129,74,1,165,204,255,254,2,50,113";

	private static final int[] SUFFIX = { 17, 31, 73, 47, 23 };

	public static class KnotBuffer {

		private interface TranslateFunction {
			int translate(int i);
		}

		private final int[] elements;

		private int position;

		private int skipSize;

		private final TranslateFunction translateFunction;

		public KnotBuffer() {
			this(256);
		}

		public KnotBuffer(int elementsCount) {
			super();

			elements = new int[elementsCount];
			fillElements();

			if (isPowerOfTow(elementsCount))
				translateFunction = powerOfTowTranslator();
			else
				translateFunction = genericTranslator();
		}
		
		public int getSkipSize() {
			return skipSize;
		}

		private void fillElements() {
			for (int i = 0; i < elements.length; i++) {
				elements[i] = i;
			}
		}

		public KnotBuffer process(int[] lengths) {
			reset();
			return process(1, lengths);
		}

		public KnotBuffer process(int length) {
			reverse(length);
			position = translate(position + length + skipSize);
			skipSize++;
			return this;
		}

		public String denseHash(String input) {
			int[] lengths = new int[input.length() + SUFFIX.length];
			for (int i = 0; i < input.length(); i++) {
				lengths[i] = input.charAt(i);
			}
			System.arraycopy(SUFFIX, 0, lengths, input.length(), SUFFIX.length);
			return denseHash(lengths);
		}

		public String denseHash(int[] lengths) {
			reset();
			process(64, lengths);
			StringBuilder sb = new StringBuilder();
			int value = elements[0];
			for (int i = 1; i < elements.length; i++) {
				value ^= elements[i];
				if ((i & 15) == 15) { // i % 16
					sb.append(String.format("%02X", value).toLowerCase());
					value = 0;
				}
			}
			return sb.toString();
		}

		private void reset() {
			fillElements();
			position = skipSize = 0;
		}

		private KnotBuffer process(int rounds, int[] lengths) {
			for (int round = 0; round < rounds; round++) {
				for (int i = 0; i < lengths.length; i++) {
					process(lengths[i]);
				}
			}
			return this;
		}

		private void reverse(int length) {
			int hl = length >> 1;
			for (int i = 0; i < hl; i++) {
				swap(position + i, position + length - 1 - i);
			}
		}

		private void swap(int source, int target) {
			source = translate(source);
			target = translate(target);
			int temp = elements[source];
			elements[source] = elements[target];
			elements[target] = temp;
		}

		private int translate(int i) {
			return translateFunction.translate(i);
		}

		private TranslateFunction genericTranslator() {
			return target -> target < 0 ? elements.length + target
					: target >= elements.length ? target - elements.length : target;
		}

		private TranslateFunction powerOfTowTranslator() {
			int capacityBitmask = elements.length - 1;
			return target -> target & capacityBitmask;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < elements.length; i++) {
				if (position == i)
					sb.append('[').append(elements[i]).append(']');
				else
					sb.append(elements[i]);
				if (i < elements.length - 1)
					sb.append(' ');
			}
			return sb.toString();
		}

		public int answer() {
			return elements[0] * elements[1];
		}
	}

	private static final int[] parse(String input) {
		String[] split = input.split(",");
		int[] res = new int[split.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = Integer.parseInt(split[i]);
		}
		return res;
	}

	private static boolean isPowerOfTow(int n) {
		return Integer.bitCount(n) == 1;
	}

	public static void main(String[] args) {
		KnotBuffer buffer = new KnotBuffer();
		buffer.process(parse(INPUT));
		DayExecutor.execute(10, () -> buffer.answer(), () -> buffer.denseHash(INPUT));
	}

}
