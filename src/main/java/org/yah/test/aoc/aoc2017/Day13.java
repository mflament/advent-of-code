package org.yah.test.aoc.aoc2017;

import static java.lang.Integer.parseInt;
import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.fusesource.jansi.AnsiConsole;
import org.yah.test.aoc.ansi.AnsiTextPrinter;
import org.yah.test.aoc.utils.DayExecutor;


public class Day13 {

	static {
		// allow usage of ansi escape codes on windows
		AnsiConsole.systemInstall();
	}

	private static int max(int... values) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			max = Math.max(max, values[i]);
		}
		return max;
	}

	static final class Scanners {
		final int[] ranges;
		final int[] positions;
		final int[] directions; // -1 or +1

		public Scanners(int[] ranges) {
			this.ranges = ranges;
			this.positions = new int[ranges.length];
			this.directions = new int[ranges.length];
			reset();
		}

		public void reset() {
			for (int depth = 0; depth < ranges.length; depth++) {
				positions[depth] = isScannable(depth, 0) ? 0 : -1;
				directions[depth] = canMove(depth) ? 1 : 0;
			}
		}

		public void move() {
			for (int depth = 0; depth < ranges.length; depth++) {
				if (canMove(depth))
					move(depth);
			}
		}

		private boolean canMove(int depth) {
			return ranges[depth] > 0;
		}

		private void move(int depth) {
			positions[depth] += directions[depth];
			if (positions[depth] == -1) {
				positions[depth] = directions[depth] = 1;
			} else if (positions[depth] == ranges[depth]) {
				positions[depth] = ranges[depth] - 2;
				directions[depth] = -1;
			}
		}

		public boolean isScannable(int depth, int range) {
			return range < ranges[depth];
		}

		public boolean isScanned(int depth, int range) {
			return isScannable(depth, range) && positions[depth] == range;
		}

		public boolean isSafe(int picoseconds) {
			for (int depth = 0; depth < ranges.length; depth++) {
				if (canMove(depth) && (picoseconds + depth) % ((ranges[depth] * 2) - 2) == 0)
					return false;
			}
			return true;
		}

	}

	static final class Firewall {

		static final int MAX_DELAY = 100_000_000;
		final int[] ranges;

		final Scanners scanners;

		int picosecond;

		int packetPosition = -1;
		int severity;

		public Firewall(int[] ranges) {
			super();
			this.ranges = ranges;
			this.scanners = new Scanners(ranges);
		}

		public int run() {
			while (advance())
				;
			return severity;
		}

		public int findDelay() {
			for (int delay = 0; delay < MAX_DELAY; delay++) {
				if (scanners.isSafe(delay))
					return delay;
			}
			throw new IllegalStateException("No safe delay found");
		}

		public boolean advance() {
			packetPosition++;
			if (packetPosition == ranges.length)
				return false;

			if (isCaught())
				severity += severity(packetPosition);

			scanners.move();

			picosecond++;
			return true;
		}

		private int severity(int depth) {
			return depth * ranges[depth];
		}

		private boolean isCaught() {
			return scanners.isScanned(packetPosition, 0);
		}

		private boolean isPacketAt(int range, int depth) {
			return range == 0 && depth == packetPosition;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("picosecond: %d, severity: %d", picosecond, severity))
					.append(System.lineSeparator());
			int maxRange = max(ranges);
			for (int range = -1; range < maxRange; range++) {
				for (int depth = 0; depth < ranges.length; depth++) {
					boolean scanned = scanners.isScanned(depth, range);
					boolean scannable = scanners.isScannable(depth, range);

					if (range < 0) { // header
						if (depth < 10)
							sb.append(' ');
						sb.append(depth).append(' ');
					} else if (isPacketAt(range, depth)) {
						sb.append("(")
								.append(scanned ? 'S' : scannable ? ' ' : '.')
								.append(')');
					} else if (scanned)
						sb.append("[S]");
					else if (scannable)
						sb.append("[ ]");
					else if (range == 0)
						sb.append("...");
					else
						sb.append("   ");
					if (depth < ranges.length - 1)
						sb.append(' ');
				}
				sb.append(System.lineSeparator());
			}

			return sb.toString();
		}

	}

	static Firewall parseFirewall(String resource) throws IOException {
		String[] lines = readLines(resource);

		List<int[]> parsedLines = Arrays.stream(lines)
				.map(Day13::parseLine)
				.collect(Collectors.toCollection(() -> new ArrayList<>(lines.length)));
		int maxDepth = parsedLines.stream()
				.collect(Collectors.summarizingInt(l -> l[0]))
				.getMax();
		int[] depths = new int[maxDepth + 1];
		for (int[] parsedLine : parsedLines) {
			depths[parsedLine[0]] = parsedLine[1];
		}
		return new Firewall(depths);
	}

	private static int[] parseLine(String line) {
		String[] split = line.split(":");
		if (split.length != 2)
			throw new IllegalArgumentException("Invalid line " + line);
		return new int[] { parseInt(split[0].trim()), parseInt(split[1].trim()) };
	}

	public static void main(String[] args) throws IOException {
		if (args.length > 0 && "demo".equals(args[0]))
			ansiDemo(parseFirewall("day13.txt"));
		else {
			Firewall firewall = parseFirewall("day13.txt");
			DayExecutor.execute(13, firewall::run, firewall::findDelay);
		}
	}

	private static void ansiDemo(Firewall firewall) {
		AtomicBoolean stopRequested = new AtomicBoolean();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> stopRequested.set(true)));
		AnsiTextPrinter terminalUpdater = new AnsiTextPrinter(System.out);
		while (!stopRequested.get()) {
			terminalUpdater.print(firewall);
			if (!firewall.advance())
				firewall.packetPosition = 0;
			sleep(100);
		}
	}

	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
