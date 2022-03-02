package org.yah.test.aoc.aoc2017;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.yah.test.aoc.aoc2017.Day13.*;

import java.io.IOException;


public class Day13Test {

	@Test
	public void test() throws IOException {
		Firewall firewall = parseFirewall("day13-test.txt");

		assertArrayEquals(new int[] { 3, 2, 0, 0, 4, 0, 4 }, firewall.ranges);
		assertArrayEquals(new int[] { 0, 0, -1, -1, 0, -1, 0 }, firewall.scanners.positions);
		assertArrayEquals(new int[] { 1, 1, 0, 0, 1, 0, 1 }, firewall.scanners.directions);

		firewall.advance();
		assertArrayEquals(new int[] { 1, 1, -1, -1, 1, -1, 1 }, firewall.scanners.positions);
		assertArrayEquals(new int[] { 1, 1, 0, 0, 1, 0, 1 }, firewall.scanners.directions);

		firewall.advance();
		assertArrayEquals(new int[] { 2, 0, -1, -1, 2, -1, 2 }, firewall.scanners.positions);
		assertArrayEquals(new int[] { 1, -1, 0, 0, 1, 0, 1 }, firewall.scanners.directions);

		firewall.advance();
		assertArrayEquals(new int[] { 1, 1, -1, -1, 3, -1, 3 }, firewall.scanners.positions);
		assertArrayEquals(new int[] { -1, 1, 0, 0, 1, 0, 1 }, firewall.scanners.directions);

		firewall = parseFirewall("day13-test.txt");
		assertEquals(24, firewall.run());

		firewall = parseFirewall("day13-test.txt");
		assertEquals(10, firewall.findDelay());
	}

}
