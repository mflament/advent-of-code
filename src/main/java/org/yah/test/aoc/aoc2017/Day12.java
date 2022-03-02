package org.yah.test.aoc.aoc2017;

import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yah.test.aoc.utils.DayExecutor;

public class Day12 {

	static final class ProgramNode {

		private final int id;

		private final Set<ProgramNode> neighbors = new LinkedHashSet<>();

		public ProgramNode(int id) {
			super();
			this.id = id;
		}

		public void connect(ProgramNode to) {
			neighbors.add(to);
			to.neighbors.add(this);
		}

		public Set<ProgramNode> collectConnected() {
			Set<ProgramNode> results = new LinkedHashSet<>();
			collectConnected(results);
			return results;
		}

		private void collectConnected(Set<ProgramNode> results) {
			Set<ProgramNode> nexts = new LinkedHashSet<>(neighbors.size());
			for (ProgramNode neighbor : neighbors) {
				if (results.add(neighbor))
					nexts.add(neighbor);
			}
			for (ProgramNode next : nexts) {
				next.collectConnected(results);
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(id).append(" <-> ");
			Iterator<ProgramNode> iter = neighbors.iterator();
			while (iter.hasNext()) {
				ProgramNode neighbor = iter.next();
				sb.append(neighbor.id);
				if (iter.hasNext())
					sb.append(", ");
			}
			return sb.toString();
		}
	}

	private static final Pattern PROGRAM_PATTERN = Pattern.compile("(\\d+) <-> (\\d+(?:, \\d+)*)");

	static ProgramNode[] parse(String[] lines) throws IOException {
		ProgramNode[] res = new ProgramNode[lines.length];
		for (String line : lines) {
			Matcher matcher = PROGRAM_PATTERN.matcher(line);
			if (!matcher.matches())
				throw new IOException("Invalid program defintion " + line);
			int id = Integer.parseInt(matcher.group(1));
			if (res[id] == null)
				res[id] = new ProgramNode(id);
			String[] targets = matcher.group(2).split(",\\s*");
			for (int i = 0; i < targets.length; i++) {
				int targetId = Integer.parseInt(targets[i]);
				if (res[targetId] == null)
					res[targetId] = new ProgramNode(targetId);
				res[id].connect(res[targetId]);
			}
		}
		return res;
	}

	public static List<Set<ProgramNode>> programGroups(ProgramNode[] graph) {
		Set<ProgramNode> remainings = new HashSet<>(Arrays.asList(graph));
		List<Set<ProgramNode>> res = new LinkedList<>();
		while (!remainings.isEmpty()) {
			ProgramNode first = remainings.iterator().next();
			Set<ProgramNode> group = first.collectConnected();
			res.add(group);
			remainings.removeAll(group);
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		ProgramNode[] graph = parse(readLines("day12.txt"));
		DayExecutor.execute(12, () -> graph[0].collectConnected().size(), () -> programGroups(graph).size());
	}
}
