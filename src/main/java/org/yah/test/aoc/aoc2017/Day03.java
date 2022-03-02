package org.yah.test.aoc.aoc2017;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

import java.nio.BufferUnderflowException;

import org.yah.test.aoc.utils.DayExecutor;


public class Day03 {

	private static final int INPUT = 277678;

	public static final class Point {

		public static final Point ORIGIN = new Point(0, 0);

		private final int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		/**
		 * @see https://math.stackexchange.com/questions/139600/how-do-i-calculate-euclidean-and-manhattan-distance-by-hand
		 */
		public int manathanDistance(Point to) {
			return abs(x - to.x) + abs(y - to.y);
		}

		@Override
		public String toString() {
			return x + ", " + y;
		}

	}

	public static class ResultsBuffer {
		private final int squareSize;
		private final int halfSquareSize;
		private final int[][] buffer;

		public ResultsBuffer(int size) {
			this.squareSize = size + (1 - (size % 2)); // ensure buffer size is odd to keep central axis aligned
			this.halfSquareSize = this.squareSize >> 1;
			buffer = new int[squareSize][squareSize];
			set(0, 0, 1);
		}

		private int get(int x, int y) {
			if (isInBound(x, y)) {
				int ax = x + halfSquareSize, ay = halfSquareSize + y;
				return buffer[ay][ax];
			}
			return 0;
		}

		private void set(int x, int y, int v) {
			if (isInBound(x, y))
				buffer[y + halfSquareSize][x + halfSquareSize] = v;
			else
				throw new BufferUnderflowException();
		}

		private boolean isInBound(int x, int y) {
			return abs(x) <= halfSquareSize && abs(y) <= halfSquareSize;
		}

		public int compute(int max) {
			int n = 2, last;
			do {
				Point p = ulam_point(n++);
				last = sum(p);
				set(p.x, p.y, last);
			} while (last <= max);
			return last;
		}

		private int sum(Point p) {
			return get(p.x + 1, p.y) + // right
					get(p.x + 1, p.y + 1) + // top right
					get(p.x, p.y + 1) + // top
					get(p.x - 1, p.y + 1) + // top left
					get(p.x - 1, p.y) + // left
					get(p.x - 1, p.y - 1) + // bottom left
					get(p.x, p.y - 1) + // bottom
					get(p.x + 1, p.y - 1); // bottom right
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = buffer.length - 1; y >= 0; y--) {
				int[] row = buffer[y];
				for (int x = 0; x < row.length; x++) {
					sb.append(String.format("%-8d", row[x]));
				}
				sb.append(System.lineSeparator());
			}
			return sb.toString();
		}
	}

	/**
	 * @see https://groups.google.com/forum/#!topic/sci.math/Fv9hMosmMHY
	 * @see https://math.stackexchange.com/questions/617574/inverse-of-ulams-spiral
	 */
	private static Point ulam_point(int n) {
		n -= 1;
		if (n == 0)
			return Point.ORIGIN;

		int m = (int) floor((sqrt(n) + 1) / 2);
		int k = n - 4 * m * (m - 1);
		if (1 <= k && k <= 2 * m)
			return new Point(m, k - m);
		if (2 * m < k && k <= 4 * m)
			return new Point(3 * m - k, m);
		if (4 * m < k && k <= 6 * m)
			return new Point(-m, 5 * m - k);
		if (6 * m < k && k <= 8 * m)
			return new Point(k - 7 * m, -m);
		throw new IllegalStateException();
	}

	public static int manathanDistance(int n) {
		Point p = ulam_point(n);
		return p.manathanDistance(Point.ORIGIN);
	}

	public static void main(String[] args) {
		ResultsBuffer buffer = new ResultsBuffer(10);
		DayExecutor.execute(3, () -> manathanDistance(277678), () -> buffer.compute(INPUT));
	}

}
