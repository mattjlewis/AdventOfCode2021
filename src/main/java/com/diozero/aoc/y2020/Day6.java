package com.diozero.aoc.y2020;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.diozero.aoc.AocBase;

public class Day6 extends AocBase {
	public static void main(String[] args) {
		new Day6().run();
	}

	@Override
	public long part1(Path input) throws IOException {
		return loadData(input, false).stream().mapToInt(Set::size).sum();
	}

	@Override
	public long part2(Path input) throws IOException {
		return loadData(input, true).stream().mapToInt(Set::size).sum();
	}

	private static List<Set<Character>> loadData(Path input, boolean union) throws FileNotFoundException, IOException {
		final List<Set<Character>> group_answers = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(input.toFile()))) {
			Set<Character> answers = null;
			while (true) {
				String line = br.readLine();
				if (line == null || line.isBlank()) {
					group_answers.add(answers);
					if (line == null) {
						break;
					}
					answers = null;
				} else {
					Set<Character> set = line.chars().mapToObj(ch -> Character.valueOf((char) ch))
							.collect(Collectors.toSet());
					if (answers == null) {
						answers = set;
					} else {
						if (union) {
							answers.retainAll(set);
						} else {
							answers.addAll(set);
						}
					}
				}
			}
		}

		return group_answers;
	}
}