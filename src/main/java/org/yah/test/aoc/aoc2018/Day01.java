/**
 * 
 */
package org.yah.test.aoc.aoc2018;

import static org.yah.test.aoc.aoc2018.IOUtils2018.readLines;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

import org.yah.test.aoc.utils.DayExecutor;


public class Day01 {

	public int part1(int... inputs) {
		return Arrays.stream(inputs).sum();
	}

	public int part2(int... inputs) {
		BitSet seenPos = new BitSet();
		seenPos.set(0);
		
		BitSet seenNeg = new BitSet();
		int frequency = 0, index = 0;
		do {
			frequency += inputs[index];
			if (frequency < 0 ? seenNeg.get(-frequency) : seenPos.get(frequency))
				return frequency;
			if (frequency < 0)
				seenNeg.set(-frequency);
			else
				seenPos.set(frequency);
			index++;
			if (index >= inputs.length)
				index = 0;
		} while (true);
	}

	public static void main(String[] args) throws IOException {
		Day01 day1 = new Day01();
		int[] inputs = Arrays.stream(readLines("day01.txt")).mapToInt(Integer::parseInt).toArray();
		DayExecutor.execute(1, () -> day1.part1(inputs), () -> day1.part2(inputs));
	}

}
