package org.yah.test.aoc.ansi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yah.test.aoc.ansi.TextComparator;
import org.yah.test.aoc.ansi.LineComparator.UpdateType;
import org.yah.test.aoc.ansi.TextComparator.TextDeltaIterator;
import org.yah.test.aoc.ansi.TextComparator.TextLineDelta;

public class TextComparatorTest {

	@Test
	public void testUpdateAndInsert() {
		TextDeltaIterator iterator = TextComparator.compare("abcd", "AbcdEF\ndefg");
		assertTrue(iterator.hasNext());
		TextLineDelta delta = iterator.next();
		assertEquals(0, delta.line());
		assertEquals(UpdateType.UPDATE, delta.type());
		assertEquals("A", delta.newContent());

		assertTrue(iterator.hasNext());
		delta = iterator.next();
		assertEquals(0, delta.line());
		assertEquals(UpdateType.INSERT, delta.type());
		assertEquals("EF", delta.newContent());

		assertTrue(iterator.hasNext());
		delta = iterator.next();
		assertEquals(1, delta.line());
		assertEquals(UpdateType.INSERT, delta.type());
		assertEquals("defg", delta.newContent());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testEquals() {
		TextDeltaIterator iterator = TextComparator.compare("abcd", "abcd");
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testInsert() {
		TextDeltaIterator iterator = TextComparator.compare("abcd", "abcd\ndefg");
		assertTrue(iterator.hasNext());
		TextLineDelta delta = iterator.next();
		assertEquals(1, delta.line());
		assertEquals(UpdateType.INSERT, delta.type());
		assertEquals("defg", delta.newContent());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testDelete() {
		TextDeltaIterator iterator;
		iterator = TextComparator.compare("abcd\ndefg", "abcd");
		assertTrue(iterator.hasNext());
		TextLineDelta delta = iterator.next();
		assertEquals(1, delta.line());
		assertEquals(UpdateType.DELETE, delta.type());
		assertEquals("defg", delta.deletedContent());
		assertFalse(iterator.hasNext());
	}

}
