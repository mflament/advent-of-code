package org.yah.test.aoc.aoc2017;

import static java.lang.Math.abs;

import java.io.IOException;

import org.yah.test.aoc.utils.DayExecutor;

/**
 * @see http://keekerdc.com/2011/03/hexagon-grids-coordinate-systems-and-distance-calculations/
 */
public class Day11 {

	private static final class Vector {
		private int x, y, z;

		public Vector() {
			super();
		}

		public Vector(int x, int y, int z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public void move(Direction direction) {
			add(direction.vector);
		}

		private void add(Vector vector) {
			x += vector.x;
			y += +vector.y;
			z += vector.z;
		}

		@Override
		public String toString() {
			return String.format("%d, %d, %d", x, y, z);
		}

		public int distanceTo(Vector target) {
			return (abs(x - target.x) + abs(y - target.y) + abs(z - target.z)) >> 1;
		}
	}

	private static enum Direction {
		N(0, 1, -1),
		NW(-1, 1, 0),
		SW(-1, 0, 1),
		S(0, -1, 1),
		SE(1, -1, 0),
		NE(1, 0, -1);

		private final Vector vector;

		private Direction(int x, int y, int z) {
			this.vector = new Vector(x, y, z);
		}

		public static Direction parse(String s) {
			try {
				return valueOf(s);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Unknown direction '" + s + "'");
			}
		}
	}

	private static final Vector ORIGIN = new Vector();

	private static Direction[] parse(String resource) throws IOException {
		String[] split = IOUtils2017.toString(resource).trim().split(",");
		Direction[] res = new Direction[split.length];
		for (int i = 0; i < split.length; i++) {
			res[i] = Direction.parse(split[i].toUpperCase());
		}
		return res;
	}

	private static final class Result {
		private final int distance, maxDistance;

		public Result(int distance, int maxDistance) {
			super();
			this.distance = distance;
			this.maxDistance = maxDistance;
		}

		@Override
		public String toString() {
			return String.format("Result [distance=%s, maxDistance=%s]", distance, maxDistance);
		}
	}

	private static Result follow(Direction[] path) {
		Vector position = new Vector();
		int max = 0;
		for (int i = 0; i < path.length; i++) {
			position.move(path[i]);
			max = Math.max(max, ORIGIN.distanceTo(position));
		}
		return new Result(ORIGIN.distanceTo(position), max);
	}

	public static void main(String[] args) throws IOException {
		Direction[] path = parse("day11.txt");
		Result result = follow(path);
		DayExecutor.execute(11, () -> result.distance, () -> result.maxDistance);
	}

}
