package org.yah.test.aoc.aoc2017.day09;

public class Garbage implements GroupContent {
	final String content;

	public Garbage(String content) {
		super();
		this.content = content;
	}

	@Override
	public int totalGroups() {
		return 0;
	}

	@Override
	public int totalScore() {
		return 0;
	}

	@Override
	public int garbageCount() {
		return content.length();
	}

	public String getContent() {
		return content;
	}

}