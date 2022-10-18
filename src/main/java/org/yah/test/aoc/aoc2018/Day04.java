package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.yah.test.aoc.utils.DayExecutor;

public class Day04 {

	public static class RecordDate implements Comparable<RecordDate> {

		private static Comparator<RecordDate> DATE_COMPARATOR = Comparator.comparing(RecordDate::getYear)
			.thenComparing(Comparator.comparing(RecordDate::getMonth))
			.thenComparing(Comparator.comparing(RecordDate::getDay))
			.thenComparing(Comparator.comparing(RecordDate::getHour))
			.thenComparing(Comparator.comparing(RecordDate::getMinute));

		private static final Pattern DATE_PATTERN = Pattern.compile(
				"\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})\\]");

		public final int year, month, day, hour, minute;

		public RecordDate(int year, int month, int day, int hour, int minute) {
			this.year = year;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
		}

		private int getYear() {
			return year;
		}

		private int getMonth() {
			return month;
		}

		private int getDay() {
			return day;
		}

		private int getHour() {
			return hour;
		}

		private int getMinute() {
			return minute;
		}

		@Override
		public int compareTo(RecordDate o) {
			return DATE_COMPARATOR.compare(this, o);
		}

		@Override
		public String toString() {
			return String.format("[%02d-%02d-%02d %02d:%02d]", year, month, day, 0, minute);
		}

		public static RecordDate parse(String input) {
			Matcher matcher = DATE_PATTERN.matcher(input);
			if (matcher.matches()) {
				return new RecordDate(Integer.parseInt(matcher.group(1)),
						Integer.parseInt(matcher.group(2)),
						Integer.parseInt(matcher.group(3)),
						Integer.parseInt(matcher.group(4)),
						Integer.parseInt(matcher.group(5)));
			}
			throw new IllegalArgumentException("Invalid date input " + input);
		}

	}

	public abstract static class AbstractLog implements Comparable<AbstractLog> {

		private static final Pattern RECORD_PATTERN = Pattern.compile("([^\\]]+\\]) (.*)");

		public final RecordDate date;

		public AbstractLog(RecordDate date) {
			this.date = date;
		}

		@Override
		public int compareTo(AbstractLog o) {
			return date.compareTo(o.date);
		}

		@Override
		public String toString() {
			return String.format("%s %s", date, describe());
		}

		protected abstract String describe();

		public static AbstractLog parse(String input) {
			Matcher matcher = RECORD_PATTERN.matcher(input);
			if (matcher.matches()) {
				RecordDate date = RecordDate.parse(matcher.group(1));
				String recordInput = matcher.group(2);
				AbstractLog res = ShiftLog.parse(date, recordInput);
				if (res == null)
					res = SleepLog.parse(date, recordInput);
				if (res == null)
					res = WakesUpLog.parse(date, recordInput);
				if (res != null)
					return res;
			}
			throw new IllegalArgumentException("Invalid record input " + input);
		}

	}

	public static class ShiftLog extends AbstractLog {

		private static final Pattern PATTERN = Pattern.compile("Guard #(\\d+) begins shift");

		public final int guardId;

		public ShiftLog(RecordDate date, int guardId) {
			super(date);
			this.guardId = guardId;
		}

		@Override
		protected String describe() {
			return String.format("Guard #%d begins shift", guardId);
		}

		static ShiftLog parse(RecordDate date, String input) {
			Matcher matcher = PATTERN.matcher(input);
			if (matcher.matches())
				return new ShiftLog(date, Integer.parseInt(matcher.group(1)));
			return null;
		}
	}

	public static class SleepLog extends AbstractLog {

		private static final String PATTERN = "falls asleep";

		public SleepLog(RecordDate date) {
			super(date);
		}

		@Override
		protected String describe() {
			return PATTERN;
		}

		static SleepLog parse(RecordDate date, String input) {
			if (PATTERN.equals(input))
				return new SleepLog(date);
			return null;
		}

	}

	public static class WakesUpLog extends AbstractLog {

		private static final String PATTERN = "wakes up";

		public WakesUpLog(RecordDate date) {
			super(date);
		}

		@Override
		protected String describe() {
			return PATTERN;
		}

		static WakesUpLog parse(RecordDate date, String input) {
			if (PATTERN.equals(input))
				return new WakesUpLog(date);
			return null;
		}
	}

	public static class GuardLogs implements Iterable<AbstractLog> {

		private final List<AbstractLog> records;

		public GuardLogs(List<AbstractLog> records) {
			super();
			this.records = records;
		}

		public List<Guard> process() {
			Map<Integer, Guard> guards = new HashMap<>();
			Guard currentGuard = null;
			for (AbstractLog log : records) {
				if (log instanceof ShiftLog) {
					ShiftLog shiftLog = (ShiftLog) log;
					if (currentGuard != null && currentGuard.isSleeping())
						throw new IllegalStateException("Guard " + currentGuard.getId() + " never wakes up!!");
					currentGuard = guards.computeIfAbsent(shiftLog.guardId, Guard::new);
				} else if (log instanceof WakesUpLog) {
					currentGuard.wakeup(log.date);
				} else if (log instanceof SleepLog) {
					currentGuard.sleep(log.date);
				}
			}

			return guards.values()
				.stream()
				.sorted(Comparator.comparing(Guard::getId))
				.collect(Collectors.toList());
		}

		@Override
		public Iterator<AbstractLog> iterator() {
			return records.iterator();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			records.forEach(r -> sb.append(r).append(System.lineSeparator()));
			return sb.toString();
		}

		public static GuardLogs parse(String resource) throws IOException {
			List<AbstractLog> records = Arrays.stream(IOUtils2018.readLines(resource))
				.map(AbstractLog::parse)
				.sorted()
				.collect(Collectors.toList());
			return new GuardLogs(records);
		}
	}

	public static class Guard {
		private final int id;
		// for each minutes from 00:00 to 00:59 (60 minutes), the number of times he was
		// asleep
		private final int[] agenda = new int[60];

		int startSleep = -1;

		public Guard(int id) {
			this.id = id;
		}

		public boolean isSleeping() {
			return startSleep >= 0;
		}

		public int getId() {
			return id;
		}

		void sleep(RecordDate date) {
			startSleep = date.minute;
		}

		void wakeup(RecordDate date) {
			for (int i = startSleep; i < date.minute; i++) {
				agenda[i]++;
			}
			startSleep = -1;
		}

		public int timeSlept() {
			return Arrays.stream(agenda).sum();
		}

		public int bestMinuteTimes() {
			return agenda[bestMinute()];
		}

		public int bestMinute() {
			int best = 0;
			for (int i = 1; i < agenda.length; i++) {
				if (agenda[i] > agenda[best])
					best = i;
			}
			return best;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(id).append(" ");
			for (int i = 0; i < agenda.length; i++) {
				sb.append(agenda[i] == 0 ? '.' : '#');
			}
			return sb.toString();
		}
	}

	public Day04() {}

	public int part1(GuardLogs logs) {
		List<Guard> guards = logs.process();
		Guard guard = guards.stream().max(Comparator.comparing(Guard::timeSlept)).get();
		return guard.id * guard.bestMinute();
	}

	public int part2(GuardLogs logs) {
		List<Guard> guards = logs.process();
		Guard guard = guards.stream().max(Comparator.comparing(Guard::bestMinuteTimes)).get();
		return guard.id * guard.bestMinute();
	}

	public static void main(String[] args) throws IOException {
		Day04 day4 = new Day04();
		GuardLogs recordsLog = GuardLogs.parse("day04.txt");
		DayExecutor.execute(4, () -> day4.part1(recordsLog), () -> day4.part2(recordsLog));
	}
}
