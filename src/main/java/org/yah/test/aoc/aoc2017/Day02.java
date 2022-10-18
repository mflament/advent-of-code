package org.yah.test.aoc.aoc2017;

import java.io.IOException;
import java.util.Arrays;

import org.yah.test.aoc.utils.DayExecutor;

public class Day02 {

	static int[][] loadInputs(String resource) throws IOException {
		String[] lines = IOUtils2017.readLines(resource);
		int[][] res = new int[lines.length][];
		int index = 0;
		for (String line : lines) {
			String[] split = line.split("\\s+");
			int row[] = new int[split.length];
			for (int i = 0; i < split.length; i++) {
				row[i] = Integer.parseInt(split[i]);
			}
			res[index++] = row;
		}
		return res;
	}

	@FunctionalInterface
	private interface RowFunction {
		int apply(int[] row);
	}

	static int checksum(int[][] inputs, RowFunction function) {
		int checksum = 0;
		for (int r = 0; r < inputs.length; r++) {
			int[] row = inputs[r];
			checksum += function.apply(row);
		}
		return checksum;
	}

	static int rangeChecksum(int[][] inputs) {
		return checksum(inputs, row -> {
			int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
			for (int c = 0; c < row.length; c++) {
				int v = row[c];
				min = Math.min(v, min);
				max = Math.max(v, max);
			}
			return max - min;
		});
	}

	static int integerDivChecksum(int[][] inputs) {
		return checksum(inputs, row -> {
			for (int i1 = 0; i1 < row.length - 1; i1++) {
				for (int i2 = i1 + 1; i2 < row.length; i2++) {
					int min = Math.min(row[i1], row[i2]);
					int max = Math.max(row[i1], row[i2]);
					float div = max / (float) min;
					if (isInteger(div))
						return (int) div;
				}
			}
			throw new IllegalStateException("No integer division found in row " + Arrays.toString(row));
		});
	}

	private static boolean isInteger(float f) {
		return (f - (int) f) == 0;
	}

	public static void main(String[] args) throws IOException {
		int[][] inputs = loadInputs("day02.txt");
		DayExecutor.execute(2, () -> rangeChecksum(inputs), () -> integerDivChecksum(inputs));
	}

}
