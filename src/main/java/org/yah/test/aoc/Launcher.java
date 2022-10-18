package org.yah.test.aoc;

import java.lang.reflect.Method;

public class Launcher {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("main class parameter missing");
			return;
		}

		String mc = args[0];
		try {
			Class<?> type = findClass(mc);
			Method mainMethod = type.getMethod("main", String[].class);

			String[] trimmedArgs = new String[args.length - 1];
			System.arraycopy(args, 1, trimmedArgs, 0, trimmedArgs.length);

			mainMethod.invoke(null, (Object) trimmedArgs);
		} catch (Exception e) {
			System.err.println("Error invoking " + mc);
			e.printStackTrace();
		}
	}

	private static Class<?> findClass(String mc) throws ClassNotFoundException {
		try {
			return Class.forName(mc);
		} catch (ClassNotFoundException e) {
			return Class.forName(Launcher.class.getPackage().getName() + "." + mc);
		}
	}
}
