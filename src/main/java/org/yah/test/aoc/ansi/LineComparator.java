package org.yah.test.aoc.ansi;

import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineComparator {

	public enum UpdateType {
		UPDATE,
		INSERT,
		DELETE
	}

	public static class LineDelta {

		private final UpdateType type;

		private final int offset, length;

		private final String fromContent;

		private final String toContent;

		public LineDelta(UpdateType type, int offset, String fromContent, String toContent) {
			this.type = type;
			this.offset = offset;
			this.length = fromContent == null ? toContent.length() : fromContent.length();
			this.fromContent = fromContent;
			this.toContent = toContent;
		}

		public LineDelta(LineDelta from) {
			this.type = from.type;
			this.offset = from.offset;
			this.length = from.length;
			this.fromContent = from.fromContent;
			this.toContent = from.toContent;
		}

		public UpdateType type() {
			return type;
		}

		public int offset() {
			return offset;
		}

		public int length() {
			return length;
		}

		public String newContent() {
			if (type == UpdateType.INSERT || type == UpdateType.UPDATE)
				return toContent;
			return null;
		}

		public String previousContent() {
			if (type == UpdateType.UPDATE)
				return fromContent;
			return null;
		}

		public String deletedContent() {
			if (type == UpdateType.DELETE)
				return fromContent;
			return null;
		}

		@Override
		public String toString() {
			return String.format("StringDelta [type=%s, offset=%s, length=%s]", type, offset, length);
		}
	}

	private LineComparator() {}

	public static Iterator<LineDelta> compare(String from, String to) {
		return new LineDeltaIterator(from, to);
	}

	private static char peek(CharBuffer buffer) {
		return buffer.get(buffer.position());
	}

	private static final class LineDeltaIterator implements Iterator<LineDelta> {

		private final CharBuffer from, to;

		private LineDelta next;

		public LineDeltaIterator(String from, String to) {
			this.from = CharBuffer.wrap(from);
			this.to = CharBuffer.wrap(to);
			next = findNext();
		}

		private String readRemaining(CharBuffer buffer) {
			char[] chars = new char[buffer.remaining()];
			buffer.get(chars);
			return new String(chars);
		}

		private LineDelta findNext() {
			while (match())
				consume();

			if (from.hasRemaining() && to.hasRemaining()) {
				return createUpdate();
			} else if (from.hasRemaining()) {
				return createDelete();
			} else if (to.hasRemaining()) {
				return createInsert();
			}
			return null;
		}

		private LineDelta createInsert() {
			return new LineDelta(UpdateType.INSERT, from.position(), null, readRemaining(to));
		}

		private LineDelta createDelete() {
			return new LineDelta(UpdateType.DELETE, from.position(), readRemaining(from), null);
		}

		private LineDelta createUpdate() {
			int offset = from.position();
			CharBuffer previousContent = from.slice();
			CharBuffer newContent = to.slice();
			int length = 0;
			while (mismatch()) {
				consume();
				length++;
			}
			previousContent.limit(length);
			newContent.limit(length);
			return new LineDelta(UpdateType.UPDATE, offset, previousContent.toString(), newContent.toString());
		}

		private boolean match() {
			return from.hasRemaining() && to.hasRemaining() && peek(from) == peek(to);
		}

		private boolean mismatch() {
			return from.hasRemaining() && to.hasRemaining() && peek(from) != peek(to);
		}

		private void consume() {
			from.get();
			to.get();
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public LineDelta next() {
			LineDelta res = next;
			if (res == null)
				throw new NoSuchElementException();
			next = findNext();
			return res;
		}

	}
}
