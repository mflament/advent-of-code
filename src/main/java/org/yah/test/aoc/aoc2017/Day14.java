package org.yah.test.aoc.aoc2017;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

import org.yah.test.aoc.aoc2017.Day10.KnotBuffer;
import org.yah.test.aoc.utils.DayExecutor;

public class Day14 {

	private static final int GRID_SIZE = 128;

	private static final int DISK_SIZE = GRID_SIZE * GRID_SIZE;

	static class Disk {

		public static Disk load(String key) {
			KnotBuffer knotBuffer = new KnotBuffer();
			long[] buffer = new long[Math.max(1, DISK_SIZE / 64)];
			for (int row = 0; row < GRID_SIZE; row++) {
				String rowKey = String.format("%s-%d", key, row);
				String hash = knotBuffer.denseHash(rowKey);
				parseRow(hash, buffer, row * 2);
			}
			return new Disk(BitSet.valueOf(buffer));
		}

		private static void parseRow(String hash, long[] buffer, int offset) {
			for (int i = 0; i < hash.length(); i++) {
				long b = Byte.parseByte(Character.toString(hash.charAt(i)), 16);
				int bitIndex = i * 4;
				int bankIndex = bitIndex / 64;
				int shift = bitIndex % 64;
				buffer[offset + bankIndex] |= miror(b) << shift;
			}
		}

		/**
		 * mirror the first 4 bits of b
		 */
		private static long miror(long b) {
			long res = b >> 3;
			res |= b >> 1 & 2;
			res |= b << 1 & 4;
			res |= b << 3 & 8;
			return res;
		}

		private final BitSet buffer;

		private Disk(BitSet buffer) {
			super();
			this.buffer = buffer;
		}

		public Disk(Disk disk) {
			buffer = (BitSet) disk.buffer.clone();
		}

		public int usedCount() {
			return buffer.cardinality();
		}

		private boolean get(int index) {
			return buffer.get(index);
		}

		private void clear(int index) {
			buffer.clear(index);
		}

		public GroupMap createGroups() {
			return new Disk(this).fetchGroups();
		}

		private GroupMap fetchGroups() {
			GroupMap groupMap = new GroupMap();
			for (int index = buffer.nextSetBit(0); index >= 0; index = buffer.nextSetBit(index + 1)) {
				fetchGroup(index, groupMap);
			}
			return groupMap;
		}

		private void fetchGroup(int startIndex, GroupMap groupMap) {
			int groupId = ++groupMap.count;
			Queue<Integer> queue = new LinkedList<>();
			queue.offer(startIndex);
			while (!queue.isEmpty()) {
				int index = queue.poll();
				clear(index);
				groupMap.set(index, groupId);
				int row = index / 128, col = index % 128;
				if (col > 0)
					checkNeighbor(index - 1, queue); // left
				if (col < GRID_SIZE - 1)
					checkNeighbor(index + 1, queue); // right
				if (row > 0)
					checkNeighbor(index - GRID_SIZE, queue); // up
				if (row < GRID_SIZE - 1)
					checkNeighbor(index + GRID_SIZE, queue); // down
			}
		}

		private void checkNeighbor(int index, Queue<Integer> next) {
			if (get(index))
				next.offer(index);
		}

		@Override
		public String toString() {
			return printGrid((i, sb) -> sb.append(get(i) ? '#' : '.'));
		}

	}

	static final class GroupMap {
		final int[] groups;
		int count;

		public GroupMap() {
			super();
			groups = new int[DISK_SIZE];
		}

		public void set(int index, int groupId) {
			groups[index] = groupId;
		}

		@Override
		public String toString() {
			return printGrid((i, sb) -> sb.append(groups[i] == 0 ? "."
					: groups[i] > 9 ? "x" : Integer.toString(groups[i])));
		}
	}

	private interface GridPrinter {
		void print(int index, StringBuilder sb);
	}

	private static String printGrid(GridPrinter printer) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DISK_SIZE; i++) {
			if (i % GRID_SIZE == 0) {
				if (i > 0)
					sb.append(System.lineSeparator());
				sb.append("row ").append(String.format("%3d", i / GRID_SIZE)).append(": ");
			}
			printer.print(i, sb);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Disk disk = Disk.load("jxqlasbh");
		DayExecutor.execute(14, disk::usedCount, () -> disk.createGroups().count);
	}

}
