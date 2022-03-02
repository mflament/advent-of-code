package org.yah.test.aoc.ansi;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.yah.test.aoc.ansi.LineComparator.LineDelta;
import org.yah.test.aoc.ansi.LineComparator.UpdateType;
import org.yah.test.aoc.utils.StringUtils;

public class TextComparator {

	public static final class TextLineDelta extends LineDelta {

		private final int line;

		public TextLineDelta(LineDelta from, int line) {
			super(from);
			this.line = line;
		}

		public TextLineDelta(UpdateType type, int offset, String fromContent, String toContent, int line) {
			super(type, offset, fromContent, toContent);
			this.line = line;
		}

		public int line() {
			return line;
		}

	}

	private TextComparator() {}

	public static TextDeltaIterator compare(String from, String to) {
		return new TextDeltaIterator(from, to);
	}

	public static final class TextDeltaIterator implements Iterator<TextLineDelta> {

		private final String[] fromLines, toLines;

		private int line;

		private Iterator<LineDelta> lineDeltaIterator;

		public TextDeltaIterator(String from, String to) {
			this.fromLines = StringUtils.lines(from);
			this.toLines = StringUtils.lines(to);
			lineDeltaIterator = nextLineIterator();
		}

		private Iterator<LineDelta> nextLineIterator() {
			Iterator<LineDelta> res;
			if (hasRemainingLines(fromLines) && hasRemainingLines(toLines)) {
				res = LineComparator.compare(fromLines[line], toLines[line]);
				while (!res.hasNext()) {
					line++;
					if (hasRemainingLines(fromLines) && hasRemainingLines(toLines))
						res = LineComparator.compare(fromLines[line], toLines[line]);
					else
						break;
				}
				if (!res.hasNext())
					return nextLineIterator();
			} else if (hasRemainingLines(fromLines)) {
				res = Collections.singleton(deletedLineDelta(fromLines[line])).iterator();
			} else if (hasRemainingLines(toLines)) {
				res = Collections.singleton(insertedLineDelta(toLines[line])).iterator();
			} else {
				res = null;
			}
			return res;
		}

		private LineDelta insertedLineDelta(String content) {
			return new TextLineDelta(UpdateType.INSERT, 0, null, content, line);
		}

		private LineDelta deletedLineDelta(String content) {
			return new TextLineDelta(UpdateType.DELETE, 0, content, null, line);
		}

		private boolean hasRemainingLines(String[] lines) {
			return line < lines.length;
		}

		public int fromLinesCount() {
			return fromLines.length;
		}

		public int toLinesCount() {
			return toLines.length;
		}

		@Override
		public boolean hasNext() {
			return lineDeltaIterator != null;
		}

		@Override
		public TextLineDelta next() {
			if (lineDeltaIterator == null || !lineDeltaIterator.hasNext())
				throw new NoSuchElementException();

			TextLineDelta res = new TextLineDelta(lineDeltaIterator.next(), line);
			if (!lineDeltaIterator.hasNext()) {
				line++;
				lineDeltaIterator = nextLineIterator();
			}
			return res;
		}

	}
}
