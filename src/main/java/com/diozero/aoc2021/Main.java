package com.diozero.aoc2021;

import java.util.stream.IntStream;

import com.diozero.aoc2021.util.AocBase;

public class Main {
	public static void main(String[] args) {
		IntStream.range(1, 24).mapToObj(Main::instantiateDay).filter(day -> day != null).forEach(AocBase::run);
	}

	private static AocBase instantiateDay(int day) {
		try {
			return (AocBase) Class.forName(Main.class.getPackageName() + ".Day" + day).getDeclaredConstructor()
					.newInstance();
		} catch (Exception e) {
			// Ignore
		}
		return null;
	}
}