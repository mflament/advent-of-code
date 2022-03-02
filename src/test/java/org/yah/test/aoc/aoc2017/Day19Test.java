package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day19.*;

import org.junit.Test;

public class Day19Test {

	@Test
	public void test() {
		Tracker tracker = newTracker("day19-sample.txt");
		assertEquals("ABCDEF", tracker.track());

		tracker = newTracker("day19-sample.txt");
		assertEquals(38, tracker.steps());
	}

}
