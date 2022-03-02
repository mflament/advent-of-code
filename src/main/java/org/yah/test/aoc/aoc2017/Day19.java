package org.yah.test.aoc.aoc2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.yah.test.aoc.utils.DayExecutor;
import org.yah.test.aoc.utils.StringUtils;

public class Day19 {

	private static final class Vector {
		private int x, y;

		public Vector(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return x + ", " + y;
		}

		public void add(Vector vector) {
			x += vector.x;
			y += vector.y;
		}

	}

	private static final class Diagram {

		private final String[] terrain;

		public Diagram(String[] lines) {
			super();
			terrain = validateTerrain(lines);
		}

		private static String[] validateTerrain(String[] lines) {
			List<String> res = new ArrayList<>(lines.length);
			res.add(lines[0]);
			int width = lines[0].length();
			for (int i = 1; i < lines.length; i++) {
				if (lines[i].length() > 0) {
					if (lines[i].length() != width)
						throw new IllegalArgumentException("Invalid line " + i + " width " + lines[i].length()
								+ ", expected " + width);
					res.add(lines[i]);
				}
			}
			return res.toArray(new String[res.size()]);
		}

		public Vector findEntry() {
			int x = terrain[0].indexOf('|');
			if (x < 0)
				throw new IllegalStateException("No entry point found in " + this);
			return new Vector(x, 0);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int row = 0; row < terrain.length; row++) {
				sb.append(new String(terrain[row]));
				if (row < terrain.length - 1)
					sb.append(System.lineSeparator());
			}
			return sb.toString();
		}

		public char charAt(Vector v) {
			return charAt(v.x, v.y);
		}

		public char charAt(int x, int y) {
			return terrain[y].charAt(x);
		}

		public int height() {
			return terrain.length;
		}

		public String row(int row) {
			return terrain[row];
		}

	}

	enum Direction {
		N(0, -1),
		S(0, 1),
		E(1, 0),
		W(-1, 0);

		private final Vector vector;

		private Direction(int x, int y) {
			this.vector = new Vector(x, y);
		}

	}

	static final class Tracker {
		private final Diagram diagram;

		private final Vector position;

		private Direction direction;

		public Tracker(Diagram diagram) {
			super();
			this.diagram = diagram;
			position = diagram.findEntry();
			direction = Direction.S;
		}

		public void track(Consumer<Vector> visitor) {
			visitor.accept(position);
			while (findNext()) {
				visitor.accept(position);
			}
		}

		private boolean findNext() {
			position.add(direction.vector);
			char newChar = diagram.charAt(position);
			if (newChar == ' ')
				return false;

			if (newChar == '+')
				direction = switchDirection();
			return true;
		}

		private Direction switchDirection() {
			if (direction == Direction.N || direction == Direction.S) {
				if (canTurn(Direction.E))
					return Direction.E;
				if (canTurn(Direction.W))
					return Direction.W;
			} else {
				if (canTurn(Direction.N))
					return Direction.N;
				if (canTurn(Direction.S))
					return Direction.S;
			}
			throw new IllegalStateException("No alternative output at " + position);
		}

		private boolean canTurn(Direction direction) {
			char c = diagram.charAt(position.x + direction.vector.x, position.y + direction.vector.y);
			if (Character.isLetter(c))
				return true;
			if (direction == Direction.E || direction == Direction.W)
				return c == '-';
			return c == '|';
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int row = 0; row < diagram.height(); row++) {
				if (position.y == row) {
					char[] line = diagram.row(row).toCharArray();
					line[position.x] = '*';
					sb.append(new String(line));
				} else {
					sb.append(new String(diagram.row(row)));
				}
				if (row < diagram.height() - 1)
					sb.append(System.lineSeparator());
			}
			return sb.toString();
		}

		public String track() {
			StringBuilder sb = new StringBuilder();
			track(p -> filterLetters(p, sb));
			return sb.toString();
		}

		public int steps() {
			AtomicInteger res = new AtomicInteger();
			track(p -> res.incrementAndGet());
			return res.get();
		}

		private void filterLetters(Vector position, StringBuilder sb) {
			char c = diagram.charAt(position);
			if (Character.isLetter(c))
				sb.append(c);
		}

	}

	public static void main(String[] args) {
		DayExecutor.execute(19,
				() -> newTracker("day19.txt").track(),
				() -> newTracker("day19.txt").steps());
	}

	static Tracker newTracker(String resource) {
		Diagram diagram;
		try {
			diagram = new Diagram(StringUtils.lines(IOUtils2017.toString(resource)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new Tracker(diagram);
	}
}
