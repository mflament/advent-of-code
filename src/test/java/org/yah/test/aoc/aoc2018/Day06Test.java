package org.yah.test.aoc.aoc2018;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.yah.test.aoc.aoc2018.Day06.Point;
import org.yah.test.aoc.aoc2018.Day06.Points;

public class Day06Test {

	private Day06 day06;

	private Point A = new Point(1, 1);
	private Point B = new Point(1, 6);
	private Point C = new Point(8, 3);
	private Point D = new Point(3, 4);
	private Point E = new Point(5, 5);
	private Point F = new Point(8, 9);

	private Points points;

	@Before
	public void setup() throws IOException {
		day06 = new Day06(32);
		points = new Points(List.of(A, B, C, D, E, F));
	}

	@Test
	public void testInfinite() {
		Set<Point> infinites = points.stream().filter(points::isInfinite).collect(Collectors.toSet());
		assertEquals(Set.of(A, B, C, F), infinites);

		Set<Point> insides = points.stream().filter(Predicate.not(points::isInfinite)).collect(Collectors.toSet());
		assertEquals(Set.of(D, E), insides);
	}

	@Test
	public void testArea() {
		assertEquals(9, points.area(D));
		assertEquals(17, points.area(E));
	}

	@Test
	public void testPart1() {
		assertEquals(17, day06.part1(points));
	}

	@Test
	public void testPart2() {
		assertEquals(16, day06.part2(points));
	}
}
