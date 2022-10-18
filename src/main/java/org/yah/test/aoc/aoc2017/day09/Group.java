package org.yah.test.aoc.aoc2017.day09;

import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Group implements GroupContent {
	final List<GroupContent> content;
	private final int score;

	public Group(List<GroupContent> content, int score) {
		super();
		this.content = content;
		this.score = score;
	}

	@Override
	public int totalGroups() {
		return 1 + sumContent(GroupContent::totalGroups);
	}

	@Override
	public int garbageCount() {
		return sumContent(GroupContent::garbageCount);
	}

	@Override
	public int totalScore() {
		return score + sumContent(GroupContent::totalScore);
	}

	private int sumContent(ToIntFunction<GroupContent> func) {
		return content.stream().collect(Collectors.summingInt(func));
	}

	public List<GroupContent> getContent() {
		return content;
	}
}