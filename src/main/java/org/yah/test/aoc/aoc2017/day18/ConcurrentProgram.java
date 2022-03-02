package org.yah.test.aoc.aoc2017.day18;

import org.yah.test.aoc.aoc2017.day18.Instruction.Operand;

final class ConcurrentProgram extends AbstractProgram {

	private final ConcurrentPrograms programs;

	private final CircularBuffer buffer;

	private ConcurrentProgram other;

	int sndCount;

	private boolean paused;

	public ConcurrentProgram(ConcurrentPrograms programs, String[] instructions, int id, int queueCapacity) {
		super(instructions);
		this.programs = programs;
		buffer = new CircularBuffer(queueCapacity);
		registers.set('p', id);
	}

	void connect(ConcurrentProgram other) {
		this.other = other;
		other.other = this;
	}

	@Override
	public boolean running() {
		return !paused && super.running();
	}

	@Override
	protected void snd(long value) {
		other.buffer.offer(value);
		sndCount++;
	}

	@Override
	protected void rcv(Operand param) {
		if (!buffer.pop(v -> registers.set(param.register(), v))) {
			pause();
			programs.resumeOther();
		}
	}

	private void pause() {
		paused = true;
		nexteip = eip;
	}

	boolean resume() {
		if (paused && buffer.isEmpty()) // dead lock
			return false;
		paused = false;
		return true;
	}
}