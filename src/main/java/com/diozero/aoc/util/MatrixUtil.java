package com.diozero.aoc.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.diozero.aoc.geometry.Point2D;

public class MatrixUtil {
	private MatrixUtil() {
	}

	public static int mask(int x, int y, int width) {
		return 1 << (y * width + x);
	}

	public static boolean isSet(int value, int x, int y, int width) {
		return (value & mask(x, y, width)) != 0;
	}

	public static int convertToBitMask(boolean[][] matrix) {
		final int width = matrix[0].length;

		int value = 0;
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix[y][x]) {
					value |= mask(x, y, width);
				}
			}
		}

		return value;
	}

	public static boolean[][] convertToMatrix(int mask, int width, int height) {
		final boolean[][] matrix = new boolean[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				matrix[y][x] = isSet(mask, x, y, width);
			}
		}

		return matrix;
	}

	public static boolean compare(boolean[][] array1, boolean[][] array2) {
		if (array1.length != array2.length || array1[0].length != array2[0].length) {
			return false;
		}

		for (int y = 0; y < array1.length; y++) {
			if (Arrays.compare(array1[y], array2[y]) != 0) {
				return false;
			}
		}

		return true;
	}

	public static boolean[][] clone(final boolean[][] matrix) {
		final boolean[][] new_array = new boolean[matrix.length][matrix[0].length];

		for (int y = 0; y < matrix.length; y++) {
			System.arraycopy(matrix[y], 0, new_array[y], 0, new_array[y].length);
		}

		return new_array;
	}

	public static char[][] clone(char[][] matrix) {
		final char[][] new_array = new char[matrix.length][matrix[0].length];

		for (int y = 0; y < matrix.length; y++) {
			System.arraycopy(matrix[y], 0, new_array[y], 0, new_array[y].length);
		}

		return new_array;
	}

	public static Set<Point2D> toPoints(boolean[][] matrix) {
		final Set<Point2D> points = new HashSet<>();

		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				if (matrix[y][x]) {
					points.add(new Point2D(x, y));
				}
			}
		}

		return points;
	}
}
