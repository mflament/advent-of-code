/**
 * 
 */
package org.yah.test.aoc.utils;

import java.util.function.Supplier;

/**
 *
 */
public class DayExecutor {

	private DayExecutor() {}

	public static void execute(int day, Supplier<?> star1, Supplier<?> star2) {
		long startTime = System.currentTimeMillis();
		Object res = star1.get();
		long elapsed = System.currentTimeMillis() - startTime;
		System.out.println(String.format("day%02d-%d: %s (%dms)", day, 1, res, elapsed));

		startTime = System.currentTimeMillis();
		res = star2.get();
		elapsed = System.currentTimeMillis() - startTime;
		System.out.println(String.format("day%02d-%d: %s (%dms)", day, 2, res, elapsed));
	}
}
