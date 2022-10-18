package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day17.Spinlock;


public class Day17Test {

	@Test
	public void test() {
		Spinlock spinlock = new Spinlock(2018);
		assertEquals(638, spinlock.fill(3));
	}

}
