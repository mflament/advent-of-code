package org.yah.test.aoc.aoc2017.day18;

import java.nio.BufferOverflowException;
import java.util.function.LongConsumer;

public final class CircularBuffer {

	private final long[] buffer;

	/**
	 * first available value offset
	 */
	private int offset;

	/**
	 * number of available values
	 */
	private int size;

	public CircularBuffer(int capacity) {
		super();
		this.buffer = new long[capacity];
	}

	public void offer(long value) {
		if (size == buffer.length)
			throw new BufferOverflowException();
		buffer[position(offset + size)] = value;
		size++;
	}

	public boolean pop(LongConsumer consumer) {
		if (size == 0)
			return false;
		consumer.accept(removeFirst());
		return true;
	}

	private long removeFirst() {
		long res = buffer[offset];
		offset = position(offset + 1);
		size--;
		return res;
	}

	private int position(int pos) {
		if (pos >= buffer.length)
			pos = pos - buffer.length;
		return pos;
	}

	public boolean isEmpty() {
		return size == 0;
	}

}