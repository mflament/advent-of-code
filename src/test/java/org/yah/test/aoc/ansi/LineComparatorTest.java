package org.yah.test.aoc.ansi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.yah.test.aoc.ansi.LineComparator;
import org.yah.test.aoc.ansi.LineComparator.LineDelta;
import org.yah.test.aoc.ansi.LineComparator.UpdateType;

public class LineComparatorTest {

	@Test
	public void testUpdateAndInsert() {
		Iterator<LineDelta> iter = LineComparator.compare("Abcdef", "abCDefg");
		assertTrue(iter.hasNext());

		LineDelta delta = iter.next();
		assertEquals(UpdateType.UPDATE, delta.type());
		assertEquals(0, delta.offset());
		assertEquals(1, delta.length());
		assertEquals("A", delta.previousContent());
		assertEquals("a", delta.newContent());
		assertTrue(iter.hasNext());

		delta = iter.next();
		assertEquals(UpdateType.UPDATE, delta.type());
		assertEquals(2, delta.offset());
		assertEquals(2, delta.length());
		assertEquals("cd", delta.previousContent());
		assertEquals("CD", delta.newContent());
		assertTrue(iter.hasNext());

		delta = iter.next();
		assertEquals(UpdateType.INSERT, delta.type());
		assertEquals(6, delta.offset());
		assertEquals(1, delta.length());
		assertEquals("g", delta.newContent());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testInsert() {
		Iterator<LineDelta> iter = LineComparator.compare("abc", "abcdefg");
		assertTrue(iter.hasNext());
		LineDelta delta = iter.next();
		assertEquals(UpdateType.INSERT, delta.type());
		assertEquals(3, delta.offset());
		assertEquals(4, delta.length());
		assertEquals("defg", delta.newContent());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testDelete() {
		Iterator<LineDelta> iter = LineComparator.compare("abcdefg", "abc");
		assertTrue(iter.hasNext());
		LineDelta delta = iter.next();
		assertEquals(UpdateType.DELETE, delta.type());
		assertEquals(3, delta.offset());
		assertEquals(4, delta.length());
		assertEquals("defg", delta.deletedContent());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testEquals() {
		assertFalse(LineComparator.compare("abc", "abc").hasNext());
	}

}
