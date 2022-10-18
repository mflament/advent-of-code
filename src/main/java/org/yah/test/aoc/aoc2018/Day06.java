package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.yah.test.aoc.utils.DayExecutor;

public class Day06 {

	private int maxDist;

	public Day06(int maxDist) {
		super();
		this.maxDist = maxDist;
	}

	public static class Point {

		private final int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return String.format("%d, %d", x, y);
		}

		public static Point parse(String input) {
			String[] split = input.split(",");
			return new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
		}

		public int distanceTo(int x2, int y2) {
			return Math.abs(x2 - x) + Math.abs(y2 - y);
		}

		public boolean is(int x2, int y2) {
			return x == x2 && y == y2;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			return is(other.x, other.y);
		}

	}

	public static class Points {

		private final List<Point> points;

		private final Point min, max;

		private final Point[][] closests;

		private final int[][] distanceSums;

		private final Set<Point> infinites = new HashSet<>();

		public Points(List<Point> points) {
			this.points = points;

			Iterator<Point> iter = points.iterator();
			Point point = iter.next();
			int minX = point.x, minY = point.y, maxX = point.x, maxY = point.y;
			while (iter.hasNext()) {
				point = iter.next();
				minX = Math.min(point.x, minX);
				minY = Math.min(point.y, minY);
				maxX = Math.max(point.x, maxX);
				maxY = Math.max(point.y, maxY);
			}
			min = new Point(minX, minY);
			max = new Point(maxX, maxY);

			int width = max.x - min.x + 1;
			int height = max.y - min.y + 1;
			closests = new Point[width][height];
			distanceSums = new int[width][height];
			points.forEach(this::set);

			for (int x = min.x; x <= max.x; x++) {
				for (int y = min.y; y <= max.y; y++) {
					Point p = get(x, y);
					if (p == null) {
						p = closest(x, y);
						set(x, y, p);
					}
					if (p != null && x == min.x || x == max.x || y == min.y || y == max.y) {
						infinites.add(p);
					}
					sumDistances(x, y);
				}
			}
		}

		public int countPointsInDistance(int maxDist) {
			int count = 0;
			for (int x = min.x; x <= max.x; x++) {
				for (int y = min.y; y <= max.y; y++) {
					int d = getDistanceSum(x, y);
					if (d < maxDist)
						count++;
				}
			}
			return count;
		}

		private int getDistanceSum(int x, int y) {
			return distanceSums[x - min.x][y - min.y];
		}

		private void sumDistances(int x, int y) {
			distanceSums[x - min.x][y - min.y] = points.stream().mapToInt(p -> p.distanceTo(x, y)).sum();
		}

		public Set<Point> getInfinites() {
			return infinites;
		}

		public boolean isInfinite(Point p) {
			return infinites.contains(p);
		}

		private Point closest(int x, int y) {
			Iterator<Point> iter = points.iterator();
			Point best = iter.next();
			int bestDistance = best.distanceTo(x, y);
			boolean conflict = false;
			while (iter.hasNext()) {
				Point point = iter.next();
				int distance = point.distanceTo(x, y);
				if (distance < bestDistance) {
					bestDistance = distance;
					best = point;
					conflict = false;
				} else if (distance == bestDistance) {
					conflict = true;
				}
			}
			return conflict ? null : best;
		}

		public int area(Point p) {
			int res = 0;
			for (int x = min.x; x <= max.x; x++) {
				for (int y = min.y; y <= max.y; y++) {
					if (get(x, y) == p)
						res++;
				}
			}
			return res;
		}

		public Point get(int x, int y) {
			return closests[x - min.x][y - min.y];
		}

		public void set(Point p) {
			set(p.x, p.y, p);
		}

		public void set(int x, int y, Point p) {
			closests[x - min.x][y - min.y] = p;
		}

		public Stream<Point> stream() {
			return points.stream();
		}

		public Point get(int index) {
			return points.get(index);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = min.y; y <= max.y; y++) {
				for (int x = min.x; x <= max.x; x++) {
					Point point = get(x, y);
					if (point == null)
						sb.append('.');
					else {
						char c = (char) ('a' + points.indexOf(point));
						if (point.is(x, y))
							c = Character.toUpperCase(c);
						sb.append(c);
					}
				}
				sb.append(System.lineSeparator());
			}
			return sb.toString();
		}

		public static Points parse(String resource) throws IOException {
			return new Points(Arrays.stream(IOUtils2018.readLines(resource))
				.map(Point::parse)
				.collect(Collectors.toList()));
		}
	}

	public int part1(Points points) {
		Optional<Point> res = points.stream()
			.filter(Predicate.not(points::isInfinite))
			.max(Comparator.comparing(points::area));
		return points.area(res.get());
	}

	public int part2(Points points) {
		return points.countPointsInDistance(maxDist);
	}

	public static void main(String[] args) throws IOException {
		Day06 day6 = new Day06(10000);
		Points points = Points.parse("day06.txt");
		DayExecutor.execute(6, () -> day6.part1(points), () -> day6.part2(points));
	}
}
