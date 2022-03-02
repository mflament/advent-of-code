package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day10.KnotBuffer;


public class Day10Test {

	@Test
	public void test() {
		KnotBuffer buffer = new KnotBuffer(5);
		assertEquals("[0] 1 2 3 4", buffer.toString());
		assertEquals("2 1 0 [3] 4", buffer.process(3).toString());
		assertEquals("4 3 0 [1] 2", buffer.process(4).toString());
		assertEquals("4 [3] 0 1 2", buffer.process(1).toString());
		assertEquals("3 4 2 1 [0]", buffer.process(5).toString());
		assertEquals(4, buffer.getSkipSize());
		assertEquals(12, buffer.answer());

		buffer = new KnotBuffer();
		assertEquals("a2582a3a0e66e6e86e3812dcb672a272", buffer.denseHash(""));
		assertEquals("33efeb34ea91902bb2f59c9920caa6cd", buffer.denseHash("AoC 2017"));
		assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", buffer.denseHash("1,2,3"));
		assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", buffer.denseHash("1,2,4"));
	}

}
