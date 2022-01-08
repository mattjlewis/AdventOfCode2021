package com.diozero.aoc.y2020;

import static com.diozero.aoc.util.ChineseRemainderTheorem.chineseRemainder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.diozero.aoc.AocBase;

public class Day13 extends AocBase {
	public static void main(String[] args) {
		new Day13().run();
	}

	@Override
	public long part1(Path input) throws IOException {
		List<String> lines = Files.readAllLines(input);
		// The earliest departure time
		final int departure_time = Integer.parseInt(lines.get(0));

		return Stream.of(lines.get(1).split(",")).filter(s -> !s.equals("x")).map(Integer::valueOf)
				// Map from bus id to the time to wait after the departure time
				.collect(Collectors.toMap(v -> v, v -> Integer.valueOf(v.intValue() - departure_time % v.intValue())))
				.entrySet().stream()
				// Get the minimum wait time
				.min((e1, e2) -> Integer.compare(e1.getValue().intValue(), e2.getValue().intValue()))
				// Return the first bus id multiplied by it's wait time
				.map(e -> Integer.valueOf(e.getKey().intValue() * e.getValue().intValue())).orElseThrow().intValue();
	}

	@Override
	public long part2(Path input) throws IOException {
		// Only need the bus ids, can ignore the earliest departure time
		String[] bus_departures = Files.lines(input).skip(1).findFirst().orElseThrow().split(",");

		/*
		 * The bus ids are all coprime numbers. Two numbers are coprime if the only
		 * positive integer that is a divisor of both of them is 1. For example 14 and
		 * 25 are coprime despite neither being prime numbers.
		 */
		List<Integer> bus_ids = new ArrayList<>();
		List<Integer> remainders = new ArrayList<>();
		for (int i = 0; i < bus_departures.length; i++) {
			if (bus_departures[i].equals("x")) {
				continue;
			}

			int bus_id = Integer.parseInt(bus_departures[i]);
			bus_ids.add(Integer.valueOf(bus_id));
			remainders.add(Integer.valueOf(bus_id - i));
		}

		return chineseRemainder(bus_ids, remainders);
	}
}
