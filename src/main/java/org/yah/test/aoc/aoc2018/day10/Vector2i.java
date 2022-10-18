package org.yah.test.aoc.aoc2018.day10;

public class Vector2i {

	public final int x, y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i(Vector2i v) {
		this.x = v.x;
		this.y = v.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector2i other = (Vector2i) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return x + ", " + y;
	}

}
