package com.diozero.aoc.algorithm.dijkstra;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.diozero.aoc.algorithm.GraphNode;
import com.diozero.aoc.algorithm.LondonUnderground;
import com.diozero.aoc.algorithm.Station;

@SuppressWarnings("static-method")
public class LondonUndergroundDijkstraTest {
	private static Map<String, GraphNode<String, Station>> ALL_NODES;

	@BeforeAll
	public static void setup() {
		ALL_NODES = LondonUnderground.getGraph();
	}

	@Test
	public void findRoute() {
		GraphNode<String, Station> from = ALL_NODES.get("Earl's Court");
		GraphNode<String, Station> to = ALL_NODES.get("Angel");

		long start = System.currentTimeMillis();
		Dijkstra.findRoute(from, to);
		long duration = System.currentTimeMillis() - start;
		System.out.format("Route distance: %,.1f km, duration: %,dms%n", Float.valueOf(to.cost() / 1000f), duration);

		Deque<String> path = new LinkedList<>();
		GraphNode<String, Station> current = to;
		do {
			path.offerFirst(current.value().name());
			current = current.getParent();
		} while (current != null);

		String path_string = String.join(" -> ", path);

		Assertions.assertEquals(
				"Earl's Court -> Gloucester Road -> Knightsbridge -> Hyde Park Corner -> Green Park -> Oxford Circus"
						+ " -> Warren Street -> Euston -> King's Cross St. Pancras -> Angel",
				path_string);
	}
}