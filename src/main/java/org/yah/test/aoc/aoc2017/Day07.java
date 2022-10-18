package org.yah.test.aoc.aoc2017;

import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yah.test.aoc.utils.DayExecutor;


public class Day07 {

	static class Node implements Comparable<Node> {

		final String name;

		int weight;

		int totalWeight;

		Node parent;

		final List<Node> children = new ArrayList<>();

		public Node(String name) {
			this.name = name;
		}

		@Override
		public int compareTo(Node o) {
			return Integer.compare(totalWeight, o.totalWeight);
		}

		public void addChild(Node child) {
			child.parent = this;
			children.add(child);
			addWeight(child.totalWeight);
		}

		public void setWeight(int weight) {
			this.weight = weight;
			addWeight(weight);
		}

		private void addWeight(int w) {
			if (w == 0)
				return;
			totalWeight += w;
			if (parent != null)
				parent.addWeight(w);
		}

		public boolean isRoot() {
			return parent == null;
		}

		public boolean isBalanced() {
			int lastWeight = 0;
			for (Node child : children) {
				if (lastWeight == 0)
					lastWeight = child.totalWeight;
				else if (lastWeight != child.totalWeight)
					return false;
			}
			return true;
		}

		public boolean isUnbalanced() {
			return !isBalanced();
		}

		public Optional<InvalidWeightedNode> findInvalidWeighted() {
			if (children.size() < 2 || isBalanced())
				return Optional.empty();
			if (children.size() == 2)
				throw new IllegalStateException("Ambigous misweighted children in " + this);

			Node[] ca = children.toArray(new Node[children.size()]);
			Arrays.sort(ca);

			int commonWeight;
			Node invalidNode;
			if (ca[0].totalWeight == ca[1].totalWeight) {
				commonWeight = ca[0].totalWeight;
				invalidNode = ca[ca.length - 1];
			} else {
				commonWeight = ca[ca.length - 1].totalWeight;
				invalidNode = ca[0];
			}
			return Optional.of(new InvalidWeightedNode(invalidNode, commonWeight));
		}

		@Override
		public String toString() {
			return String.format("%s (%d = %d + %d)%s",
					name,
					totalWeight,
					weight,
					totalWeight - weight,
					isUnbalanced() ? " (unbalanced)" : "");
		}

		@SuppressWarnings("unused")
		public String toTreeString() {
			StringBuilder sb = new StringBuilder();
			toString(sb, 0);
			return sb.toString();
		}

		private void toString(StringBuilder sb, int level) {
			for (int i = 0; i < level; i++) {
				sb.append(' ');
			}
			sb.append(this).append(System.lineSeparator());
			children.forEach(c -> c.toString(sb, level + 1));
		}

	}

	static class InvalidWeightedNode {
		final Node node;
		final int expectedTotalWeight;

		public InvalidWeightedNode(Node node, int expectedTotalWeight) {
			super();
			this.node = node;
			this.expectedTotalWeight = expectedTotalWeight;
		}

		public int delta() {
			return expectedTotalWeight - node.totalWeight;
		}

		public int expectedWeight() {
			return node.weight + delta();
		}

		@Override
		public String toString() {
			return node + ": " + delta() + ", weight should be " + expectedWeight();
		}
	}

	static final Pattern NODE_PATTERN = Pattern.compile("(\\w+) \\((\\d+)\\)(?: -> (.+))?");

	static void parseNode(String line, Map<String, Node> nodes) {
		Matcher matcher = NODE_PATTERN.matcher(line);
		if (matcher.matches()) {
			String name = matcher.group(1);
			int weight = Integer.parseInt(matcher.group(2));
			String[] childrenNames = matcher.group(3) != null ? matcher.group(3).split("\\s*,\\s*") : new String[0];
			Node node = nodes.computeIfAbsent(name, Node::new);
			if (node.weight == 0)
				node.setWeight(weight);
			for (int i = 0; i < childrenNames.length; i++) {
				Node child = nodes.computeIfAbsent(childrenNames[i], Node::new);
				node.addChild(child);
			}
		}
	}

	static Node parseTree(String resource) throws IOException {
		String[] lines = readLines(resource);
		Map<String, Node> nodes = new HashMap<>();
		Arrays.stream(lines).forEach(l -> parseNode(l, nodes));
		Node root = nodes.values()
				.stream()
				.filter(Node::isRoot)
				.findFirst()
				.orElseThrow(IllegalStateException::new);
		return root;
	}

	static Optional<InvalidWeightedNode> findInvalidNode(Node node) {
		if (node.isBalanced())
			return Optional.empty();
		Optional<Node> unbalanced = node.children.stream().filter(Node::isUnbalanced).findFirst();
		if (unbalanced.isPresent())
			return findInvalidNode(unbalanced.get());
		return node.findInvalidWeighted();
	}

	public static void main(String[] args) throws IOException {
		Node root = parseTree("day07.txt");
		DayExecutor.execute(7,
				() -> root.name,
				() -> findInvalidNode(root).orElseThrow(() -> new AssertionError("Invalid node not found"))
						.expectedWeight());
	}
}
