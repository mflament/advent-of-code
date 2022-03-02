package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.yah.test.aoc.utils.DayExecutor;

public class Day08 {

	public static class Node {

		private final List<Node> children;
		private final int[] metadata;
		private int value = -1;

		public Node(int childrenCount, int metaCount) {
			this.children = new ArrayList<>(childrenCount);
			this.metadata = new int[metaCount];
		}

		public int getMetadataCount() {
			return metadata.length;
		}

		public int getMetadata(int index) {
			return metadata[index];
		}

		public int sumMetadata() {
			return Arrays.stream(metadata).sum();
		}

		public void visit(Consumer<Node> visitor) {
			visitor.accept(this);
			children.forEach(c -> c.visit(visitor));
		}

		public int getChildrenCount() {
			return children.size();
		}

		public Node getChildren(int index) {
			return children.get(index);
		}

		public int getValue() {
			if (value < 0) {
				if (children.isEmpty())
					value = sumMetadata();
				else {
					value = 0;
					for (int i = 0; i < metadata.length; i++) {
						int childIndex = metadata[i] - 1;
						if (childIndex >= 0 && childIndex < children.size()) {
							Node child = children.get(childIndex);
							value += child.getValue();
						}
					}
				}
			}
			return value;
		}

		public static Node parse(String input) {
			return parse(Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray());
		}

		public static Node parse(int[] inputs) {
			return parseNode(IntIterator.of(inputs));
		}

		private static Node parseNode(IntIterator inputs) {
			int childrenCount = inputs.next();
			int metaCount = inputs.next();
			Node node = new Node(childrenCount, metaCount);
			for (int i = 0; i < childrenCount; i++) {
				node.children.add(parseNode(inputs));
			}
			for (int i = 0; i < metaCount; i++) {
				node.metadata[i] = inputs.next();
			}
			return node;
		}
	}

	private static class IntIterator {
		private final int[] values;

		private int index;

		private IntIterator(int[] values) {
			this.values = values;
		}

		public static IntIterator of(int[] inputs) {
			return new IntIterator(inputs);
		}

		public int next() {
			return values[index++];
		}
	}

	public int part1(Node tree) {
		AtomicInteger counter = new AtomicInteger();
		tree.visit(n -> counter.addAndGet(n.sumMetadata()));
		return counter.get();
	}

	public int part2(Node tree) {
		return tree.getValue();
	}

	public static void main(String[] args) throws IOException {
		Day08 day8 = new Day08();
		Node tree = Node.parse(IOUtils2018.toString("day08.txt").trim());
		DayExecutor.execute(8, () -> day8.part1(tree), () -> day8.part2(tree));
	}
}
