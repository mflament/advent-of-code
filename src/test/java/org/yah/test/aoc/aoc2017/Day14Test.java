package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day14.Disk;

public class Day14Test {

	@Test
	public void test() {
		Disk disk = Disk.load("flqrgnkx");
		assertEquals(8108, disk.usedCount());
		assertEquals(1242, disk.createGroups().count);
	}

}
