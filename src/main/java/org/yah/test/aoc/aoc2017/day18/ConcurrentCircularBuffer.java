package org.yah.test.aoc.aoc2017.day18;

import java.nio.BufferOverflowException;
import java.util.function.LongConsumer;

public final class ConcurrentCircularBuffer {

	private final long[] buffer;

	private final Object monitor = new Object();

	/**
	 * first available value offset
	 */
	private int offset;

	/**
	 * number of available values
	 */
	private int size;

	public ConcurrentCircularBuffer(int capacity) {
		super();
		this.buffer = new long[capacity];
	}

	public void interrupt() {
		synchronized (monitor) {
			size = -1;
			monitor.notify();
		}
	}

	public void offer(long value) {
		synchronized (monitor) {
			if (size == buffer.length)
				throw new BufferOverflowException();
			buffer[position(offset + size)] = value;
			size++;
			monitor.notify();
		}
	}

	public long take() throws InterruptedException {
		synchronized (monitor) {
			while (size == 0)
				monitor.wait();
			if (size < 0)
				throw new InterruptedException();
			return removeFirst();
		}
	}

	public boolean pop(LongConsumer consumer) {
		synchronized (monitor) {
			if (size == 0)
				return false;
			consumer.accept(removeFirst());
			return true;
		}
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
		synchronized (monitor) {
			return size == 0;
		}
	}

}