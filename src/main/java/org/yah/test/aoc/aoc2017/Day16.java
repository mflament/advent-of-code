package org.yah.test.aoc.aoc2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yah.test.aoc.utils.DayExecutor;


public class Day16 {

	interface Move {
		void execute(Dance dance);
	}

	static class Spin implements Move {
		private final int x;

		public Spin(int x) {
			super();
			this.x = x;
		}

		@Override
		public void execute(Dance dance) {
			dance.programs.spin(x);
		}
	}

	static class Exchange implements Move {
		private final int a, b;

		public Exchange(int a, int b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public void execute(Dance dance) {
			dance.programs.swap(a, b);
		}
	}

	static class Partner implements Move {
		private final char a, b;

		public Partner(char a, char b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public void execute(Dance dance) {
			Programs programs = dance.programs;
			int ia = programs.indexOf(a);
			int ib = programs.indexOf(b);
			programs.swap(ia, ib);
		}
	}

	interface MoveParser {
		Optional<Move> parse(String s);
	}

	private static abstract class AbstractMoveParser implements MoveParser {

		private final Pattern pattern;

		public AbstractMoveParser(String regex) {
			super();
			pattern = Pattern.compile(regex);
		}

		@Override
		public Optional<Move> parse(String s) {
			Matcher matcher = pattern.matcher(s);
			if (matcher.matches()) {
				return Optional.of(create(matcher));
			}
			return Optional.empty();
		}

		protected abstract Move create(Matcher matcher);
	}

	private static class SpinParser extends AbstractMoveParser {

		public SpinParser() {
			super("s(\\d+)");
		}

		@Override
		protected Move create(Matcher matcher) {
			return new Spin(Integer.parseInt(matcher.group(1)));
		}
	}

	private static class ExchangeParser extends AbstractMoveParser {

		public ExchangeParser() {
			super("x(\\d+)/(\\d+)");
		}

		@Override
		protected Move create(Matcher matcher) {
			return new Exchange(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
		}
	}

	private static class PartnerParser extends AbstractMoveParser {

		public PartnerParser() {
			super("p(\\w)/(\\w)");
		}

		@Override
		protected Move create(Matcher matcher) {
			return new Partner(matcher.group(1).charAt(0), matcher.group(2).charAt(0));
		}
	}

	private static final MoveParser[] MOVER_PARSERS = { new SpinParser(),
			new ExchangeParser(),
			new PartnerParser() };

	private static int TIMES = 1000000000;

	static class Programs {

		private char[] programs;

		public Programs(int count) {
			super();
			programs = new char[count];
			for (int i = 0; i < count; i++) {
				programs[i] = (char) (i + 'a');
			}
		}

		public Programs(Programs from) {
			this.programs = new char[from.programs.length];
			System.arraycopy(from.programs, 0, programs, 0, programs.length);
		}

		public void spin(int x) {
			int length = programs.length;
			int start = length - x;
			char[] buffer = new char[length];
			System.arraycopy(programs, start, buffer, 0, x);
			System.arraycopy(programs, 0, buffer, x, length - x);
			programs = buffer;
		}

		public void swap(int a, int b) {
			char swap = programs[a];
			programs[a] = programs[b];
			programs[b] = swap;
		}

		public int indexOf(char p) {
			for (int i = 0; i < programs.length; i++) {
				if (programs[i] == p)
					return i;
			}
			throw new IllegalArgumentException("unknown program " + p);
		}

		public boolean equals(Programs other) {
			return Arrays.equals(programs, other.programs);
		}

		@Override
		public String toString() {
			return new String(programs);
		}
	}

	static class Dance {

		private final Programs programs;

		public Dance(int count) {
			super();
			programs = new Programs(count);
		}

		public Dance execute(Move[] moves, int times) {
			int loop = 0;
			Programs start = new Programs(programs);
			for (loop = 0; loop < times; loop++) {
				execute(moves);
				if (start.equals(programs))
					break;
			}

			int remaining = times % (loop + 1);
			while (remaining > 0) {
				execute(moves);
				remaining--;
			}
			return this;
		}

		Dance execute(Move[] moves) {
			for (int i = 0; i < moves.length; i++) {
				moves[i].execute(this);
			}
			return this;
		}

		@Override
		public String toString() {
			return programs.toString();
		}
	}

	static Move[] parseMoves(String moves) {
		String[] split = moves.split(",");
		Move[] res = new Move[split.length];
		for (int i = 0; i < split.length; i++) {
			res[i] = parseMove(split[i]);
		}
		return res;
	}

	private static Move parseMove(String string) {
		for (int i = 0; i < MOVER_PARSERS.length; i++) {
			Optional<Move> opt = MOVER_PARSERS[i].parse(string);
			if (opt.isPresent())
				return opt.get();
		}
		throw new IllegalArgumentException("Unable to parse move " + string);
	}

	public static void main(String[] args) throws IOException {
		Move[] moves = parseMoves(IOUtils2017.toString("day16.txt").trim());
		DayExecutor.execute(16,
				() -> new Dance(16).execute(moves),
				() -> new Dance(16).execute(moves, TIMES));
	}
}
