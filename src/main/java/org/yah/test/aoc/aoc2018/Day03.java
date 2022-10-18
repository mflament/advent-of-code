package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.yah.test.aoc.utils.DayExecutor;

public class Day03 {

	static class Claim {

		private static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (.*)");

		private final int id;

		private final Rectangle rectangle;

		public Claim(int id, Rectangle rectangle) {
			this.id = id;
			this.rectangle = rectangle;
		}

		@Override
		public String toString() {
			return String.format("#%d @ %s", id, rectangle);
		}

		public static Claim parse(String input) {
			Matcher matcher = PATTERN.matcher(input);
			if (matcher.matches()) {
				return new Claim(Integer.parseInt(matcher.group(1)), Rectangle.parse(matcher.group(2)));
			}
			throw new IllegalArgumentException("Invalid Claim input: " + input);
		}
	}

	static class Rectangle {

		private static final Pattern PATTERN = Pattern.compile("(\\d+),(\\d+)\\: (\\d+)x(\\d+)");

		int left, top, width, height;

		public Rectangle(int left, int top, int width, int height) {
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}

		public Rectangle(Rectangle from) {
			this(from.left, from.top, from.width, from.height);
		}

		public static Rectangle parse(String input) {
			Matcher matcher = PATTERN.matcher(input);
			if (matcher.matches()) {
				return new Rectangle(Integer.parseInt(matcher.group(1)),
						Integer.parseInt(matcher.group(2)),
						Integer.parseInt(matcher.group(3)),
						Integer.parseInt(matcher.group(4)));
			}
			throw new IllegalArgumentException("Invalid Rectangle input: " + input);
		}

		@Override
		public String toString() {
			return String.format("%d,%d: %dx%d", left, top, width, height);
		}

		public void expand(Rectangle rectangle) {
			int dx = left + width;
			left = Math.min(left, rectangle.left);

			int tmp = Math.max(rectangle.left + rectangle.width, dx);
			width = tmp - left;

			int dy = top + height;
			top = Math.min(top, rectangle.top);
			tmp = Math.max(rectangle.top + rectangle.height, dy);
			height = tmp - top;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + height;
			result = prime * result + left;
			result = prime * result + top;
			result = prime * result + width;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rectangle other = (Rectangle) obj;
			if (height != other.height)
				return false;
			if (left != other.left)
				return false;
			if (top != other.top)
				return false;
			if (width != other.width)
				return false;
			return true;
		}

	}

	public static List<Claim> readClaims(String resource) throws IOException {
		return readClaims(IOUtils2018.readLines(resource));
	}

	public static List<Claim> readClaims(String[] inputs) {
		return Arrays.stream(inputs)
			.map(Claim::parse)
			.collect(Collectors.toCollection(() -> new ArrayList<>(inputs.length)));
	}

	public int part1(List<Claim> claims) {
		Rectangle bounds = getBounds(claims);
		BitSet map = new BitSet(bounds.width * bounds.height);
		BitSet overlap = new BitSet(bounds.width * bounds.height);
		for (Claim claim : claims) {
			for (int x = claim.rectangle.left; x < claim.rectangle.left + claim.rectangle.width; x++) {
				for (int y = claim.rectangle.top; y < claim.rectangle.top + claim.rectangle.height; y++) {
					int index = (y - bounds.top) * bounds.width + (x - bounds.left);
					if (map.get(index))
						overlap.set(index);
					map.set(index);
				}
			}
		}

		return overlap.cardinality();
	}

	public int part2(List<Claim> claims) {
		Rectangle bounds = getBounds(claims);
		Claim[][] map = new Claim[bounds.width][bounds.height];
		Set<Claim> allClaims = new HashSet<>(claims);
		for (Claim claim : claims) {
			for (int x = claim.rectangle.left; x < claim.rectangle.left + claim.rectangle.width; x++) {
				for (int y = claim.rectangle.top; y < claim.rectangle.top + claim.rectangle.height; y++) {
					int mx = x - bounds.left;
					int my = y - bounds.top;
					Claim overlapped = map[mx][my];
					if (overlapped != null) {
						allClaims.remove(overlapped);
						allClaims.remove(claim);
					}
					map[mx][my] = claim;
				}
			}
		}
		if (allClaims.size() != 1)
			throw new IllegalStateException("Invalid remaining claims count " + allClaims.size());
		return allClaims.iterator().next().id;
	}

	private Rectangle getBounds(List<Claim> claims) {
		Iterator<Claim> iterator = claims.iterator();
		Rectangle res = new Rectangle(iterator.next().rectangle);
		while (iterator.hasNext()) {
			Claim claim = iterator.next();
			res.expand(claim.rectangle);
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		Day03 day3 = new Day03();
		List<Claim> claims = readClaims("day03.txt");
		DayExecutor.execute(3, () -> day3.part1(claims), () -> day3.part2(claims));
	}
}
