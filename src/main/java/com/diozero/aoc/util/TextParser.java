package com.diozero.aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.tinylog.Logger;

public class TextParser {
	private TextParser() {
	}

	public static boolean[][] loadBooleanArray(final Path input) throws IOException {
		return loadBooleanArray(input, '#');
	}

	public static boolean[][] loadBooleanArray(final Path input, final char ch) throws IOException {
		return Files.lines(input).map(line -> toBooleanArray(line, ch)).toArray(boolean[][]::new);
	}

	public static int[] loadIntArray(final Path input) throws IOException {
		return Files.lines(input).mapToInt(Integer::parseInt).toArray();
	}

	public static int[] loadIntArray(final Path input, final boolean sorted) throws IOException {
		IntStream is = Files.lines(input).mapToInt(Integer::parseInt);
		if (sorted) {
			is = is.sorted();
		}
		return is.toArray();
	}

	public static List<Integer> loadFirstLineAsIntegerList(final Path input) throws IOException {
		return Files.lines(input).findFirst()
				.map(l -> l.chars().mapToObj(ch -> Integer.valueOf(charToInt(ch))).toList()).orElseThrow();
	}

	public static int charToInt(int ch) {
		return ch - 48;
	}

	public static int toInt(char ch) {
		return ch - 48;
	}

	public static long[] loadLongArray(final Path input) throws IOException {
		return Files.lines(input).mapToLong(Long::parseLong).toArray();
	}

	public static int[][] loadIntMatrix(final Path input) throws IOException {
		// Note the lazy conversion from ASCII character code to integer
		final int[][] matrix = Files.lines(input).map(line -> line.chars().map(TextParser::charToInt).toArray())
				.toArray(int[][]::new);

		if (Logger.isDebugEnabled()) {
			// Print the matrix if not too big
			if (matrix.length < 20) {
				for (int[] row : matrix) {
					Logger.debug("matrix: {}", Arrays.toString(row));
				}
			}
		}

		return matrix;
	}

	public static boolean[] toBooleanArray(final String line, final char ch) {
		final boolean[] data = new boolean[line.length()];

		for (int i = 0; i < data.length; i++) {
			data[i] = line.charAt(i) == ch ? true : false;
		}

		return data;
	}

	public static int[] loadFirstLineAsCsvIntArray(Path input) throws IOException {
		return loadFirstLineAsCsvIntArray(input, false);
	}

	public static int[] loadFirstLineAsCsvIntArray(Path input, boolean sorted) throws IOException {
		IntStream stream = Arrays.stream(Files.lines(input).findFirst().orElseThrow().split(","))
				.mapToInt(Integer::parseInt);
		if (sorted) {
			stream = stream.sorted();
		}

		return stream.toArray();
	}

	public static long[] loadFirstLineAsCsvLongArray(Path input) throws IOException {
		return loadFirstLineAsCsvLongArray(input, false);
	}

	public static long[] loadFirstLineAsCsvLongArray(Path input, boolean sorted) throws IOException {
		LongStream stream = Arrays.stream(Files.lines(input).findFirst().orElseThrow().split(","))
				.mapToLong(Long::parseLong);
		if (sorted) {
			stream = stream.sorted();
		}

		return stream.toArray();
	}
}
