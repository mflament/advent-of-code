package org.yah.test.aoc.aoc2017;

import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;
import java.util.Arrays;

import org.yah.test.aoc.utils.DayExecutor;


public class Day05 {

	static int[] program(int... program) {
		return program;
	}

	private static int[] readProgram(String resource) throws IOException {
		return readProgram(readLines(resource));
	}

	private static int[] readProgram(String[] lines) {
		int[] res = new int[lines.length];
		for (int i = 0; i < lines.length; i++) {
			res[i++] = Integer.parseInt(lines[i]);
		}
		return res;
	}

	static int[] cloneProgram(int[] program) {
		return Arrays.copyOf(program, program.length);
	}

	static int execute(int[] program, InstructionProcessor processor) {
		int index = 0, steps = 0;
		while (index < program.length) {
			int instruction = program[index];
			program[index] += processor.process(instruction);
			index += instruction;
			steps++;
		}
		return steps;
	}

	static InstructionProcessor forwardProcessor() {
		return i -> 1;
	}

	static InstructionProcessor schizophrenicProcessor() {
		return i -> i >= 3 ? -1 : 1;
	}

	@FunctionalInterface
	public interface InstructionProcessor {
		int process(int instruction);
	}

	public static void main(String[] args) throws IOException {
		int[] program = readProgram("day05.txt");
		DayExecutor.execute(5,
				() -> execute(cloneProgram(program), forwardProcessor()),
				() -> execute(cloneProgram(program), schizophrenicProcessor()));
	}

}
