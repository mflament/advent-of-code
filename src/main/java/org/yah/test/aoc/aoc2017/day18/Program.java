package org.yah.test.aoc.aoc2017.day18;

import org.yah.test.aoc.aoc2017.day18.Instruction.Operand;

public final class Program extends AbstractProgram {

	private long lastSoundFrequency;

	public Program(String[] instructions) {
		super(instructions);
	}

	@Override
	protected void snd(long value) {
		lastSoundFrequency = value;
	}

	@Override
	protected void rcv(Operand param) {
		if (param.value(registers) != 0) {
			result = lastSoundFrequency;
			terminate();
		}
	}
}