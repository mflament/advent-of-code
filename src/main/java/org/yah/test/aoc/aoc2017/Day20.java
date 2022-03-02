package org.yah.test.aoc.aoc2017;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20 {

	private static final class Vector {
		private int x, y, z;

		public Vector(String s) {
			String[] split = s.split(",");
			x = Integer.valueOf(split[0].trim());
			y = Integer.valueOf(split[1].trim());
			z = Integer.valueOf(split[2].trim());
		}

		@Override
		public String toString() {
			return x + ", " + y + ", " + z;
		}

		public void add(Vector v) {
			x += v.x;
			y += v.y;
			z += v.z;
		}
		
	}

	private static class Particle {
		private static final Pattern PATTERN = Pattern.compile("p=<(.*)>, v=<(.*)>, a=<(.*)>");

		private final Vector position;
		private final Vector velocity;
		private final Vector acceleration;

		public Particle(String line) {
			super();
			Matcher matcher = PATTERN.matcher(line);
			if (matcher.matches()) {
				position = new Vector(matcher.group(1));
				velocity = new Vector(matcher.group(2));
				acceleration = new Vector(matcher.group(3));
			} else
				throw new IllegalArgumentException("Invalid particle " + line);
		}

		public void update() {
			velocity.add(acceleration);
			position.add(velocity);
		}

		@Override
		public String toString() {
			return String.format("p=<%s>, v=<%s>, a=<%s>", position, velocity, acceleration);
		}
	}


	private static class Particles {
		private final Particle[] particles;

		public Particles(String[] lines) {
			super();
			particles = new Particle[lines.length];
			for (int i = 0; i < lines.length; i++) {
				particles[i] = new Particle(lines[i]);
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < particles.length; i++) {
				sb.append(particles[i].toString());
				if (i < particles.length - 1)
					sb.append(System.lineSeparator());
			}
			return sb.toString();
		}

		public void update() {
			for (int i = 0; i < particles.length; i++) {
				particles[i].update();
			}
		}

	}

	public static Particles loadParticles(String resource) throws IOException {
		return new Particles(IOUtils2017.readLines(resource));
	}

	public static void main(String[] args) throws IOException {
		test();

		Particles particles = loadParticles("day20.txt");
		// System.out.println(particles);
	}

	private static void test() throws IOException {
		Particles particles = loadParticles("day20-test.txt");
		particles.update();
		System.out.println(particles);
		System.out.println();

		particles.update();
		System.out.println(particles);
		System.out.println();

		particles.update();
		System.out.println(particles);
		System.out.println();
	}

}
