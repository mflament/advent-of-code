package org.yah.test.aoc.aoc2017.day18;

public final class ConcurrentPrograms {

	private final ConcurrentProgram[] programs;

	private int current;

	public ConcurrentPrograms(String[] instructions, int queueCapacity) {
		super();
		programs = new ConcurrentProgram[2];
		programs[0] = new ConcurrentProgram(this, instructions, 0, queueCapacity);
		programs[1] = new ConcurrentProgram(this, instructions, 1, queueCapacity);
		programs[0].connect(programs[1]);
	}

	public long execute() {
		do {
			programs[current].execute();
		} while (programs[0].running() || programs[1].running());
		return programs[1].sndCount;
	}

	private int other() {
		return current == 0 ? 1 : 0;
	}

	public void resumeOther() {
		ConcurrentProgram other = programs[other()]; 
		if (other.resume()) {
			current = other();
		}
		// else: deadlock
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("program ").append(current).append(System.lineSeparator());
		sb.append(programs[current]);
		return sb.toString();
	}
}