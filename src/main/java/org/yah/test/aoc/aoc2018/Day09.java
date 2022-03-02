package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import org.yah.test.aoc.utils.DayExecutor;

public class Day09 {

	public interface IntList {

		int capacity();

		int size();

		void insert(int index, int value);

		int remove(int index);

		int get(int index);
	}

	public static class ArrayIntList implements IntList {

		private final int[] buffer;

		private int size;

		public ArrayIntList(int capacity) {
			this.buffer = new int[capacity];
		}

		@Override
		public int capacity() {
			return buffer.length;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public void insert(int index, int value) {
			if (size == buffer.length)
				throw new IllegalStateException("buffer overflow");

			if (index < size)
				System.arraycopy(buffer, index, buffer, index + 1, size - index);
			buffer[index] = value;
			size++;
		}

		@Override
		public int remove(int index) {
			int res = buffer[index];
			if (index < size - 1)
				System.arraycopy(buffer, index + 1, buffer, index, size - (index + 1));
			size--;
			return res;
		}

		@Override
		public int get(int index) {
			return buffer[index];
		}

	}

	public static class Circle {
		private final int maxMarble;

		// next marble value
		private int nextMarble;

		private int currentMarble; // index in marbles
		private int nextPlayer; // index in playerScores

		private final int[] playerScores;

		private final IntList marbles;

		public Circle(int players, int maxMarble) {
			this.playerScores = new int[players];
			this.maxMarble = maxMarble;
			this.marbles = new ArrayIntList(maxMarble + 1);
			this.nextPlayer = 0;
			this.currentMarble = 0;
			marbles.insert(0, 0);
			this.nextMarble = marbles.size();
		}

		public boolean play() {
			int player = nextPlayer();
			int marble = nextMarble++;
			if (marble % 23 == 0) {
				playerScores[player] += marble;
				currentMarble = wrap(currentMarble - 7);
				// System.out.println(marble + " : " + marbles.get(currentMarble));
				playerScores[player] += marbles.remove(currentMarble);
			} else {
				currentMarble = wrap(currentMarble + 1) + 1;
				marbles.insert(currentMarble, marble);
			}

			if (isComplete())
				return false;

			return true;
		}

		public int getPercentDone() {
			return (int) ((nextMarble - 1) / (float) maxMarble * 100f);
		}

		private boolean isComplete() {
			return nextMarble > maxMarble;
		}

		private int nextPlayer() {
			int res = nextPlayer++;
			if (nextPlayer >= playerScores.length)
				nextPlayer = 0;
			return res;
		}

		private int wrap(int index) {
			int marblesCount = marbles.size();
			if (index < 0)
				return wrap(marblesCount + index);
			if (index >= marblesCount)
				return wrap(index - marblesCount);
			return index;
		}

		@Override
		public String toString() {
			if (marbles.size() > 99)
				return marbles.size() + " marbles is too much";

			StringBuilder sb = new StringBuilder();
			int player = nextPlayer == 0 ? playerScores.length : nextPlayer;
			if (nextMarble % 23 == 1)
				sb.append('*');
			else
				sb.append(' ');
			sb.append('[').append(marbles.size() == 1 ? "-" : player).append("] ");
			for (int i = 0; i < marbles.size(); i++) {
				sb.append(String.format("%4s", i == currentMarble ? "(" + marbles.get(i) + ")" : marbles.get(i) + " "));
			}
			return sb.toString();
		}

		public int winningScore() {
			return Arrays.stream(playerScores).max().getAsInt();
		}

	}

	public int part1(int players, int maxMarble) {
		Circle circle = new Circle(players, maxMarble);
		while (circle.play())
			;
		return circle.winningScore();
	}

	public int part2(int players, int maxMarble) {
		Circle circle = new Circle(players, maxMarble * 100);
		int lastPct = 0;
		long lastStart = System.currentTimeMillis();
		while (circle.play()) {
			int newPct = circle.getPercentDone();
			if (newPct != lastPct) {
				long elapsed = System.currentTimeMillis() - lastStart;
				long remaining = (100 - newPct) * elapsed;
				System.out.println(String.format("%d%% %ds/%% %ds remaining", newPct, elapsed / 1000,
						remaining / 1000));
				lastStart = System.currentTimeMillis();
			}
			lastPct = newPct;
		}
		System.out.println("100%");
		return circle.winningScore();
	}

	private static int parseInt(String str) {
		int res = 0;
		for (int i = 0; i < str.length(); i++) {
			int d = str.charAt(i) - '0';
			res = res * 10 + d;
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(parseInt("4599") + 1);
		// Day09 day9 = new Day09();
		// int players = 471;
		// int maxMarble = 72026;
		// DayExecutor.execute(9, () -> day9.part1(players, maxMarble), () ->
		// day9.part2(players, maxMarble));
	}

}
