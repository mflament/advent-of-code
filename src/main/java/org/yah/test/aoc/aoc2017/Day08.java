package org.yah.test.aoc.aoc2017;

import static org.yah.test.aoc.aoc2017.IOUtils2017.openReader;
import static org.yah.test.aoc.aoc2017.IOUtils2017.readLines;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.yah.test.aoc.utils.DayExecutor;
import org.yah.test.aoc2017.Day8Lexer;
import org.yah.test.aoc2017.Day8Parser;
import org.yah.test.aoc2017.Day8Parser.ConditionContext;
import org.yah.test.aoc2017.Day8Parser.InstructionContext;
import org.yah.test.aoc2017.Day8Parser.OperationContext;

@SuppressWarnings("unused")
public class Day08 {

	static class Registers {

		private final Map<String, Integer> values = new HashMap<>();

		private int maxSeenValue;

		public int get(String register) {
			Integer v = values.get(register);
			return v == null ? 0 : v;
		}

		public void set(String register, int v) {
			maxSeenValue = Math.max(maxSeenValue, v);
			values.put(register, v);
		}

		private int maxValue() {
			return values.values().stream().collect(Collectors.maxBy(Integer::compareTo)).orElse(0);
		}
	}

	interface Operator {
		String getRepresentation();
	}

	enum InstructionOperator implements Operator {
		INC("inc"),
		DEC("dec");

		private final String representation;

		private InstructionOperator(String representation) {
			this.representation = representation;
		}

		@Override
		public String getRepresentation() {
			return representation;
		}

		@Override
		public String toString() {
			return representation;
		}

		public InstructionFunction createFunction(int operand) {
			switch (this) {
			case INC:
				return i -> i + operand;
			case DEC:
				return i -> i - operand;
			default:
				throw new IllegalStateException("Unhandled instruction operator " + this);
			}
		}

		public static InstructionOperator parse(String s) {
			return parseOperator(values(), s);
		}

	}

	/**
	 * @see https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html
	 */
	enum ConditionalOperator implements Operator {
		EQ("=="),
		NEQ("!="),
		GT(">"),
		GTE(">="),
		LT("<"),
		LTE("<=");

		private final String representation;

		private ConditionalOperator(String expression) {
			this.representation = expression;
		}

		@Override
		public String getRepresentation() {
			return representation;
		}

		public boolean eval(int a, int b) {
			switch (this) {
			case EQ:
				return a == b;
			case NEQ:
				return a != b;
			case GT:
				return a > b;
			case GTE:
				return a >= b;
			case LT:
				return a < b;
			case LTE:
				return a <= b;
			default:
				throw new IllegalStateException("Unhandled conditional operator " + this);
			}
		}

		@Override
		public String toString() {
			return representation;
		}

		public static ConditionalOperator parse(String s) {
			return parseOperator(values(), s);
		}
	}

	private static <O extends Operator> O parseOperator(O[] values, String s) {
		for (int i = 0; i < values.length; i++) {
			if (values[i].getRepresentation().equals(s))
				return values[i];
		}
		throw new IllegalArgumentException("Invalid operator " + s);
	}

	@FunctionalInterface
	interface InstructionFunction {
		int apply(int input);
	}

	@FunctionalInterface
	interface RegistersCondition {
		boolean test(Registers registers);
	}

	private static class DefaultRegistersCondition implements RegistersCondition {
		private final String register;
		private final ConditionalOperator operator;
		private final int operand;

		private DefaultRegistersCondition(String register, ConditionalOperator operator, int operand) {
			super();
			this.register = register;
			this.operator = operator;
			this.operand = operand;
		}

		@Override
		public boolean test(Registers registers) {
			int v = registers.get(register);
			return operator.eval(v, operand);
		}

		public static RegistersCondition parse(ConditionContext condition) {
			String register = condition.register().getText();
			ConditionalOperator operator = ConditionalOperator.parse(condition.relationalOperator().getText());
			int operand = Integer.parseInt(condition.NUMBER().getText());
			return new DefaultRegistersCondition(register, operator, operand);
		}
	}

	private static class Instruction {

		private final String register;
		private final RegistersCondition condition;
		private final InstructionFunction function;

		public Instruction(String register, RegistersCondition condition, InstructionFunction function) {
			super();
			this.register = register;
			this.condition = condition;
			this.function = function;
		}

		public void execute(Registers registers) {
			if (condition.test(registers)) {
				int v = function.apply(registers.get(register));
				registers.set(register, v);
			}
		}
	}

	static class Program {
		private final List<Instruction> instructions;

		public Program(List<Instruction> instructions) {
			super();
			this.instructions = instructions;
		}

		public ExecutionResult execute() {
			Registers registers = new Registers();
			for (Instruction instruction : instructions) {
				instruction.execute(registers);
			}
			return new ExecutionResult(registers.maxValue(), registers.maxSeenValue);
		}
	}

	interface ProgramParser {
		Program parse(String resource) throws IOException;
	}

	private static final Pattern INSTRUCTION_PATTERN = Pattern
		.compile("(\\w+)\\s+(inc|dec)\\s+(-?\\d+)\\s+if\\s+(\\w+)\\s+([!=<>]+)\\s+(-?\\d+)");

	private static final ProgramParser REGEXP_PARSER = new ProgramParser() {

		@Override
		public Program parse(String resource) throws IOException {
			String[] lines = readLines(resource);
			ArrayList<Instruction> instructions = Arrays.stream(lines)
				.map(this::parseInstruction)
				.collect(Collectors.toCollection(() -> new ArrayList<>(lines.length)));
			return new Program(instructions);
		}

		private Instruction parseInstruction(String input) {
			Matcher matcher = INSTRUCTION_PATTERN.matcher(input);
			if (!matcher.matches())
				throw new IllegalArgumentException("Does not match pattern");
			String register = matcher.group(1);

			InstructionOperator op = InstructionOperator.parse(matcher.group(2));
			int instructionOperand = Integer.parseInt(matcher.group(3));

			String conditionRegister = matcher.group(4);
			ConditionalOperator conditionalOperator = ConditionalOperator.parse(matcher.group(5));
			int conditionOperand = Integer.parseInt(matcher.group(6));
			return new Instruction(register,
					new DefaultRegistersCondition(conditionRegister, conditionalOperator, conditionOperand),
					op.createFunction(instructionOperand));
		}
	};

	private static final ProgramParser ANTLR_PARSER = new ProgramParser() {
		@Override
		public Program parse(String resource) throws IOException {
			try (Reader reader = openReader(resource, StandardCharsets.US_ASCII)) {
				Day8Lexer lexer = new Day8Lexer(CharStreams.fromReader(reader));
				Day8Parser parser = new Day8Parser(new CommonTokenStream(lexer));
				List<InstructionContext> instructionContexts = parser.instructions().instruction();
				List<Instruction> instructions = instructionContexts.stream()
					.map(this::parseInstruction)
					.collect(Collectors.toCollection(LinkedList::new));
				return new Program(instructions);
			}
		}

		private Instruction parseInstruction(InstructionContext context) {
			String register = context.register().getText();
			RegistersCondition condition = DefaultRegistersCondition.parse(context.condition());
			InstructionFunction function = parseFunction(context.operation());
			return new Instruction(register, condition, function);
		}

		private InstructionFunction parseFunction(OperationContext operation) {
			InstructionOperator operator = InstructionOperator.parse(operation.operator().getText());
			return operator.createFunction(Integer.parseInt(operation.NUMBER().getText()));
		}

	};

	static final ProgramParser PARSER = REGEXP_PARSER;

	static final class ExecutionResult {
		final int currentMax;
		final int maxSeenValue;

		public ExecutionResult(int currentMax, int maxSeenValue) {
			super();
			this.currentMax = currentMax;
			this.maxSeenValue = maxSeenValue;
		}

		@Override
		public String toString() {
			return "ExecutionResult [currentMax=" + currentMax + ", maxSeenValue=" + maxSeenValue + "]";
		}
	}

	public static void main(String[] args) throws IOException {
		Program program = PARSER.parse("day08.txt");
		ExecutionResult result = program.execute();
		DayExecutor.execute(8, () -> result.currentMax, () -> result.maxSeenValue);
	}
}
