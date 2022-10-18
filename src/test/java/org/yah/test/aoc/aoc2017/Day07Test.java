package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;
import static org.yah.test.aoc.aoc2017.Day07.*;

import java.io.IOException;

import org.junit.Test;
import org.yah.test.aoc.aoc2017.Day07.InvalidWeightedNode;
import org.yah.test.aoc.aoc2017.Day07.Node;


public class Day07Test {

	@Test
	public void test() throws IOException {
		Node root = parseTree("day07-test.txt");
		assertEquals("tknk", root.name);
		InvalidWeightedNode invalidWeightedNode = findInvalidNode(root)
				.orElseThrow(() -> new AssertionError("Invalid node not found"));
		assertEquals("ugml", invalidWeightedNode.node.name);
		assertEquals(243, invalidWeightedNode.expectedTotalWeight);
		assertEquals(60, invalidWeightedNode.expectedWeight());
		assertEquals(-8, invalidWeightedNode.delta());
	}

}
