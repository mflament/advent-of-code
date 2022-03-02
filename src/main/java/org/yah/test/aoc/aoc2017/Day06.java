package org.yah.test.aoc.aoc2017;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.yah.test.aoc.utils.DayExecutor;

public class Day06 {

	private static final int[] INPUTS = { 0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11 };

	private static class MemoryBanks {

		private final int[] blocks;

		private final int maxIndex;

		public MemoryBanks(int[] blocks) {
			this(blocks, findMaxIndex(blocks));
		}

		public MemoryBanks(int[] blocks, int maxIndex) {
			super();
			this.blocks = blocks;
			this.maxIndex = maxIndex;
		}

		private static int findMaxIndex(int[] blocks) {
			int res = 0;
			for (int i = 1; i < blocks.length; i++) {
				if (blocks[i] > blocks[res]) {
					res = i;
				}
			}
			return res;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(blocks);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MemoryBanks other = (MemoryBanks) obj;
			return Arrays.equals(blocks, other.blocks);
		}

		public MemoryBanks reallocate() {
			int[] nextBlocks = new int[blocks.length];
			System.arraycopy(blocks, 0, nextBlocks, 0, blocks.length);
			int remaining = nextBlocks[maxIndex];
			nextBlocks[maxIndex] = 0;
			int nextMaxIndex = maxIndex;
			int index = nextIndex(maxIndex);
			while (remaining > 0) {
				nextBlocks[index]++;
				remaining--;
				if (nextBlocks[index] > nextBlocks[nextMaxIndex] ||
						(nextBlocks[index] == nextBlocks[nextMaxIndex] && index < nextMaxIndex)) {// handle tie
					nextMaxIndex = index;
				}
				index = nextIndex(index);
			}
			return new MemoryBanks(nextBlocks, nextMaxIndex);
		}

		/**
		 * next index of circular buffer
		 */
		private int nextIndex(int index) {
			index++;
			return index >= blocks.length ? index - blocks.length : index;
		}

		@Override
		public String toString() {
			return Arrays.toString(blocks);
		}

	}

	static class Result {
		final int cycles, loopSize;

		public Result(int cycles, int loopSize) {
			super();
			this.cycles = cycles;
			this.loopSize = loopSize;
		}

		@Override
		public String toString() {
			return String.format("%d cycles, loop size: %d", cycles, loopSize);
		}
	}

	static Result reallocate(int... inputs) {
		Map<MemoryBanks, Integer> seen = new HashMap<>();
		MemoryBanks memoryBanks = new MemoryBanks(inputs);
		int cycles = 0;
		seen.put(memoryBanks, cycles);
		Integer origin = cycles;
		do {
			memoryBanks = memoryBanks.reallocate();
			cycles++;
		} while ((origin = seen.put(memoryBanks, cycles)) == null);
		int loopSize = cycles - origin;
		return new Result(cycles, loopSize);
	}

	public static void main(String[] args) {
		Result result = reallocate(INPUTS);
		DayExecutor.execute(6, () -> result.cycles, () -> result.loopSize);
	}

}
