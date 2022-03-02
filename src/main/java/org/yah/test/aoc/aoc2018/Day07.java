package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.yah.test.aoc.utils.DayExecutor;

public class Day07 {

	public static class Step implements Comparable<Step> {

		public final char name;

		public Set<Step> after = new HashSet<>();

		public Set<Step> before = new HashSet<>();

		public Step(char name) {
			this.name = Character.toUpperCase(name);
		}

		public void setBefore(Step other) {
			other.before.add(this);
			after.add(other);
		}

		@Override
		public int compareTo(Step o) {
			return Character.compare(name, o.name);
		}

		@Override
		public String toString() {
			return Character.toString(name);
		}

		public boolean isInitial() {
			return before.isEmpty();
		}

		public boolean isTerminal() {
			return after.isEmpty();
		}

		public int getTime() {
			return name - 'A' + 1;
		}

		public Set<Step> advance() {
			Set<Step> res = new HashSet<>();
			for (Step step : after) {
				step.before.remove(this);
				if (step.before.isEmpty())
					res.add(step);
			}
			return res;
		}
	}

	public static class Steps implements Iterable<Step> {

		private static final Pattern PATTERN = Pattern.compile(
				"Step (\\w) must be finished before step (\\w) can begin\\.");

		private final Set<Step> steps;

		public Steps(Collection<Step> steps) {
			this.steps = new HashSet<>(steps);
		}

		@Override
		public Iterator<Step> iterator() {
			return steps.iterator();
		}

		public String traverse() {
			StringBuilder sb = new StringBuilder();
			PriorityQueue<Step> availables = new PriorityQueue<>(steps.stream()
				.filter(Step::isInitial)
				.collect(Collectors.toList()));
			while (true) {
				Step step = availables.poll();
				sb.append(step.name);
				if (step.isTerminal())
					break;

				availables.addAll(step.advance());
			}
			return sb.toString();
		}

		public int parallelize(int workerCount, int fixedDuration) {
			PriorityQueue<Worker> workers = new PriorityQueue<>(workerCount);
			for (int i = 0; i < workerCount; i++) {
				workers.add(new Worker(i));
			}

			PriorityQueue<Step> availables = new PriorityQueue<>(steps.stream()
				.filter(Step::isInitial)
				.collect(Collectors.toList()));
			List<Worker> idleWorkers = new ArrayList<>();
			while (true) {
				Worker worker = workers.poll();
				if (worker.currentStep != null) {
					if (worker.currentStep.isTerminal())
						return worker.readyAt;

					availables.addAll(worker.currentStep.advance());
					worker.currentStep = null;

					if (!idleWorkers.isEmpty()) {
						workers.add(worker);
						for (Worker idleWorker : idleWorkers) {
							idleWorker.readyAt = worker.readyAt;
							workers.add(idleWorker);
						}
						idleWorkers.clear();
						continue;
					}
				}

				Step step = availables.poll();
				if (step != null) {
					worker.readyAt = worker.readyAt + fixedDuration + step.getTime();
					worker.currentStep = step;
					workers.add(worker);
				} else {
					idleWorkers.add(worker);
				}
			}
		}

		private class Worker implements Comparable<Worker> {
			private final int index;
			private int readyAt;
			private Step currentStep;

			public Worker(int index) {
				this.index = index;
			}

			@Override
			public int compareTo(Worker o) {
				int res = Integer.compare(readyAt, o.readyAt);
				if (res == 0)
					return Integer.compare(index, o.index);
				return res;
			}

			@Override
			public String toString() {
				StringBuilder builder = new StringBuilder();
				builder.append("Worker [index=")
					.append(index)
					.append(", readyAt=")
					.append(readyAt)
					.append(", currentStep=")
					.append(currentStep)
					.append("]");
				return builder.toString();
			}

		}

		public static Steps parse(String resource) {
			Map<Character, Step> steps = new HashMap<>();
			String[] lines;
			try {
				lines = IOUtils2018.readLines(resource);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			for (int i = 0; i < lines.length; i++) {
				Matcher matcher = PATTERN.matcher(lines[i]);
				if (!matcher.matches())
					throw new IllegalArgumentException("Invalid line " + lines[i]);
				Step before = steps.computeIfAbsent(matcher.group(1).charAt(0), Step::new);
				Step after = steps.computeIfAbsent(matcher.group(2).charAt(0), Step::new);
				before.setBefore(after);
			}
			return new Steps(steps.values());
		}

	}

	private int workerCount;

	private int fixedDuration;

	public Day07(int workerCount, int fixedDuration) {
		this.workerCount = workerCount;
		this.fixedDuration = fixedDuration;
	}

	public String part1(Steps steps) {
		return steps.traverse();
	}

	public int part2(Steps steps) {
		return steps.parallelize(workerCount, fixedDuration);
	}

	public static void main(String[] args) throws IOException {
		Day07 day7 = new Day07(5, 60);
		DayExecutor.execute(7, () -> day7.part1(Steps.parse("day07.txt")), () -> day7.part2(Steps.parse("day07.txt")));
	}
}
