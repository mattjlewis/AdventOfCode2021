package com.diozero.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import org.tinylog.Logger;

import com.diozero.aoc2021.util.AocBase;

public class Day2 extends AocBase {
	public static void main(String[] args) {
		new Day2().run();
	}

	@Override
	public long part1(Path input) throws IOException {
		final AtomicInteger horizontal = new AtomicInteger();
		final AtomicInteger depth = new AtomicInteger();
		Files.lines(input).map(Instruction::new).forEach(instruction -> {
			switch (instruction.movement) {
			case FORWARD:
				horizontal.addAndGet(instruction.amount);
				break;
			default:
				depth.addAndGet(instruction.amount);
				break;
			}
		});
		Logger.debug("depth: {}, horizontal: {}, d*h={}", depth, horizontal, depth.get() * horizontal.get());
		return depth.get() * horizontal.get();
	}

	@Override
	public long part2(Path input) throws IOException {
		final AtomicInteger horizontal = new AtomicInteger();
		final AtomicInteger depth = new AtomicInteger();
		final AtomicInteger aim = new AtomicInteger();
		Files.lines(input).map(Instruction::new).forEach(instruction -> {
			switch (instruction.movement) {
			case FORWARD:
				horizontal.addAndGet(instruction.amount);
				depth.addAndGet(aim.get() * instruction.amount);
				break;
			default:
				aim.addAndGet(instruction.amount);
				break;
			}
		});
		Logger.debug("depth: {}, horizontal: {}, aim={}, d*h={}", depth, horizontal, aim,
				depth.get() * horizontal.get());
		return depth.get() * horizontal.get();
	}

	public static class Instruction {
		public enum Movement {
			FORWARD, UP, DOWN;
		}

		final Movement movement;
		final int amount;

		public Instruction(String line) {
			String[] parts = line.split(" ");
			this.movement = Instruction.Movement.valueOf(parts[0].toUpperCase());
			int val = Integer.parseInt(parts[1]);

			if (movement == Movement.UP) {
				this.amount = -val;
			} else {
				this.amount = val;
			}
		}
	}
}