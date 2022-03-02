package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day15.*;

import org.junit.Test;


public class Day15Test {

	@Test
	public void test() {
		testGenerator(new Generator(65, GENERATOR_A_FACTOR, v -> true),
				1092455,
				1181022009,
				245556042,
				1744312007,
				1352636452);
		testGenerator(new Generator(8921, GENERATOR_B_FACTOR, v -> true),
				430625591,
				1233683848,
				1431495498,
				137874439,
				285222916);

		assertTrue(Judge.compare(245556042, 1431495498));

		Judge judge = new Judge(65, 8921, false);
		assertEquals(588, judge.render(LOTS_OF_PAIRS));

		judge = new Judge(679, 771, false);

		testGenerator(new Generator(65, GENERATOR_A_FACTOR, A_PREDICATE),
				1352636452,
				1992081072,
				530830436,
				1980017072,
				740335192);
		testGenerator(new Generator(8921, GENERATOR_B_FACTOR, B_PREDICATE),
				1233683848,
				862516352,
				1159784568,
				1616057672,
				412269392);

		judge = new Judge(65, 8921, true);
		assertEquals(1056, judge.firstFirst());
		judge = new Judge(65, 8921, true);
		assertEquals(309, judge.render(LESS_PAIRS));
	}

	private static void testGenerator(Generator generator, int... expected) {
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], generator.generate());
		}
	}
}
