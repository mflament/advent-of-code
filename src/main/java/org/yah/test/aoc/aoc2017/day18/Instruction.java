package org.yah.test.aoc.aoc2017.day18;

class Instruction {
	
	enum Operator {
		set,
		add,
		mul,
		mod,
		snd,
		rcv,
		jgz;
	}

	interface Operand {
		long value(Registers registers);

		default char register() {
			throw new UnsupportedOperationException();
		}
	}

	private static class RegisterOperand implements Operand {
		private final char register;

		public RegisterOperand(char register) {
			super();
			this.register = register;
		}

		@Override
		public long value(Registers registers) {
			return registers.get(register);
		}

		@Override
		public char register() {
			return register;
		}
	}

	private static Operand parseOperand(String part) {
		try {
			long value = Long.parseLong(part);
			return r -> value;
		} catch (NumberFormatException e) {
			return new RegisterOperand(register(part));
		}
	}

	private static char register(String s) {
		if (s.length() > 1)
			throw new IllegalArgumentException("Invalid register '" + s + "'");
		char r = s.charAt(0);
		if (r < 'a' || r > 'p')
			throw new IllegalArgumentException("Invalid register '" + s + "'");
		return r;
	}

	final String code;
	final Operator operator;
	final Operand[] operands = new Operand[2];

	public Instruction(String code) {
		super();
		this.code = code;
		String[] parts = code.split("\\s+");
		operator = Operator.valueOf(parts[0]);
		operands[0] = parseOperand(parts[1]);
		if (parts.length > 2)
			operands[1] = parseOperand(parts[2]);
	}

	@Override
	public String toString() {
		return code;
	}
}