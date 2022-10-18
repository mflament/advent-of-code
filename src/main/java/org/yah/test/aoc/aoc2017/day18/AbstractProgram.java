package org.yah.test.aoc.aoc2017.day18;

import org.yah.test.aoc.aoc2017.day18.Instruction.Operand;

abstract class AbstractProgram {

	protected final Registers registers;

	private final Instruction[] instructions;

	protected int eip, nexteip;

	protected long result;

	public AbstractProgram(String[] instructions) {
		super();
		this.registers = new Registers();
		this.instructions = new Instruction[instructions.length];
		for (int i = 0; i < instructions.length; i++) {
			this.instructions[i] = new Instruction(instructions[i]);
		}
	}

	public final long execute() {
		while (running()) {
			Instruction instruction = instructions[eip];
			Operand x = instruction.operands[0];
			Operand y = instruction.operands[1];
			nexteip = eip + 1;
			switch (instruction.operator) {
			case set:
				registers.set(x.register(), y.value(registers));
				break;
			case add:
				registers.add(x.register(), y.value(registers));
				break;
			case mul:
				registers.mul(x.register(), y.value(registers));
				break;
			case mod:
				registers.mod(x.register(), y.value(registers));
				break;
			case snd:
				snd(x.value(registers));
				break;
			case rcv:
				rcv(x);
				break;
			case jgz:
				if (x.value(registers) > 0)
					nexteip = eip + (int) y.value(registers);
				break;
			default:
				throw new IllegalArgumentException("Unknown operator " + instruction.operator + " in "
						+ instruction);
			}
			eip = nexteip;
		}

		return result;
	}

	public boolean running() {
		return eip >= 0 && eip < instructions.length;
	}

	protected final void terminate() {
		nexteip = instructions.length;
	}

	protected abstract void snd(long value);

	protected abstract void rcv(Operand param);

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(registers.toString()).append(System.lineSeparator());
		for (int i = 0; i < instructions.length; i++) {
			if (i == eip)
				sb.append(" -> ");
			else if (i == nexteip)
				sb.append("(->)");
			else
				sb.append("    ");
			sb.append("  ").append(instructions[i]);
			if (i < instructions.length - 1)
				sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

}