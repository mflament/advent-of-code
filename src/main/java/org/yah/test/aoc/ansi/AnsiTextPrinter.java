/**
 * 
 */
package org.yah.test.aoc.ansi;

import java.io.PrintStream;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Erase;
import org.yah.test.aoc.ansi.TextComparator.TextDeltaIterator;
import org.yah.test.aoc.ansi.TextComparator.TextLineDelta;
import org.yah.test.aoc.utils.StringUtils;
import org.fusesource.jansi.AnsiConsole;


/**
 *
 */
public class AnsiTextPrinter {

	private static final String ENABLED_PROPERTY = "ansi.enabled";

	static {
		AnsiConsole.systemInstall();
	}

	private final PrintStream printStream;

	private String lastString;

	private String[] lines;

	private int line, column;

	private Ansi ansi;

	private boolean enabled = true;

	public AnsiTextPrinter(PrintStream printStream) {
		super();
		this.printStream = printStream;
		String sp = System.getProperty(ENABLED_PROPERTY);
		this.enabled = sp == null || sp.equals("true");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void print(Object o) {
		String s = String.valueOf(o);
		if (!enabled || lastString == null) {
			printStream.print(s);
			lines = StringUtils.lines(s);
			line = lines.length - 1;
			column = lines[line].length();
		} else {
			TextDeltaIterator iterator = TextComparator.compare(lastString, s);
			ansi = Ansi.ansi();
			while (iterator.hasNext()) {
				handleDelta(iterator.next());
			}
			printStream.print(ansi);
			ansi = null;
		}
		lastString = s;
	}

	private void handleDelta(TextLineDelta delta) {
		moveTo(delta);
		switch (delta.type()) {
			case INSERT:
			case UPDATE:
				append(delta.newContent());
				break;
			case DELETE:
				ansi.eraseLine(Erase.FORWARD);
				break;
			default:
				throw new IllegalArgumentException("Unhandled delta type " + delta);
		}
	}

	private void moveTo(TextLineDelta delta) {
		int d = delta.line() - line;
		if (d < 0)
			ansi.cursorUp(Math.abs(d));
		else if (d > 0)
			ansi.cursorDown(d);
		line += d;

		d = delta.offset() - column;
		if (d < 0)
			ansi.cursorLeft(Math.abs(d));
		else if (d > 0)
			ansi.cursorRight(d);
		column += d;
	}

	private void append(String s) {
		ansi.a(s);
		column += s.length();
	}

}
