package com.diozero.aoc.y2021;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.diozero.aoc.Day;
import com.diozero.aoc.algorithm.GraphNode;
import com.diozero.aoc.algorithm.astar.AStarPathFinder;
import com.diozero.aoc.algorithm.dijkstra.Dijkstra;
import com.diozero.aoc.geometry.Point2D;
import com.diozero.aoc.util.TextParser;

/*
 * Note the Dijkstra solution takes ~1.9 seconds for part 2, A* ~450ms.
 */
public class Day15 extends Day {
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/main/resources/input/2021/day15.txt");
		for (int i = 0; i < 2; i++) {
			part1Dijkstra(input);
			part2Dijkstra(input);
		}
		long start = System.currentTimeMillis();
		String result = part1Dijkstra(input);
		long duration = System.currentTimeMillis() - start;
		System.out.format("Dijkstra part1: %s. Duration: %,dms%n", result, duration);

		start = System.currentTimeMillis();
		result = part2Dijkstra(input);
		duration = System.currentTimeMillis() - start;
		System.out.format("Dijkstra part2: %s. Duration: %,dms%n", result, duration);

		System.setProperty("perf", "2");

		new Day15().run();
	}

	@Override
	public String name() {
		return "Chiton";
	}

	@Override
	public String part1(Path input) throws IOException {
		final int[][] cost_matrix = TextParser.loadIntMatrix(input);
		final int width = cost_matrix[0].length;
		final int height = cost_matrix.length;

		final Map<Integer, GraphNode<Integer, Point2D>> all_nodes = buildGraph(cost_matrix);

		final GraphNode<Integer, Point2D> start = all_nodes.get(Integer.valueOf(0));
		final GraphNode<Integer, Point2D> end = all_nodes.get(Integer.valueOf(width * height - 1));

		return Integer.toString(AStarPathFinder.findRoute(start, end, Day15::heuristic).cost());
	}

	@Override
	public String part2(Path input) throws IOException {
		int[][] cost_matrix = TextParser.loadIntMatrix(input);

		// Expand the cost matrix - this could be optimised...
		int expansion = 5;
		int orig_height = cost_matrix.length;
		int orig_width = cost_matrix[0].length;
		int[][] new_cost_matrix = new int[orig_height * expansion][orig_width * expansion];
		for (int grid_y = 0; grid_y < expansion; grid_y++) {
			for (int grid_x = 0; grid_x < expansion; grid_x++) {
				if (grid_y == 0 && grid_x == 0) {
					for (int y = 0; y < orig_height; y++) {
						for (int x = 0; x < orig_width; x++) {
							new_cost_matrix[y][x] = cost_matrix[y][x];
						}
					}
				} else if (grid_x == 0) {
					// Get from the cell above
					for (int y = 0; y < orig_height; y++) {
						for (int x = 0; x < orig_width; x++) {
							int xx = grid_x * orig_height + x;
							int new_val = new_cost_matrix[(grid_y - 1) * orig_height + y][xx] + 1;
							new_cost_matrix[grid_y * orig_height + y][xx] = new_val > 9 ? 1 : new_val;
						}
					}
				} else {
					// Get from the cell to the left
					for (int y = 0; y < orig_height; y++) {
						for (int x = 0; x < orig_width; x++) {
							int yy = grid_y * orig_height + y;
							int new_val = new_cost_matrix[yy][(grid_x - 1) * orig_width + x] + 1;
							new_cost_matrix[yy][grid_x * orig_width + x] = new_val > 9 ? 1 : new_val;
						}
					}
				}
			}
		}
		cost_matrix = new_cost_matrix;
		final int width = cost_matrix[0].length;
		final int height = cost_matrix.length;

		final Map<Integer, GraphNode<Integer, Point2D>> all_nodes = buildGraph(cost_matrix);

		final GraphNode<Integer, Point2D> start = all_nodes.get(Integer.valueOf(0));
		final GraphNode<Integer, Point2D> end = all_nodes.get(Integer.valueOf(width * height - 1));

		return Integer.toString(AStarPathFinder.findRoute(start, end, Day15::heuristic).cost());
	}

	public static String part1Dijkstra(Path input) throws IOException {
		final int[][] cost_matrix = TextParser.loadIntMatrix(input);
		final int width = cost_matrix[0].length;
		final int height = cost_matrix.length;

		// Populate the possible destinations and associated costs for each node
		final Map<Integer, GraphNode<Integer, Point2D>> all_nodes = populateDestinations(cost_matrix);

		final GraphNode<Integer, Point2D> start_node = all_nodes.get(Integer.valueOf(0));
		final GraphNode<Integer, Point2D> end_node = all_nodes.get(Integer.valueOf(width * height - 1));

		Dijkstra.findRoute(start_node, end_node);

		return Integer.toString(end_node.cost());
	}

	public static String part2Dijkstra(Path input) throws IOException {
		final int[][] start_cost_matrix = TextParser.loadIntMatrix(input);

		// Expand the cost matrix - this could be optimised...
		final int expansion = 5;
		final int orig_height = start_cost_matrix.length;
		final int orig_width = start_cost_matrix[0].length;
		final int[][] cost_matrix = new int[orig_height * expansion][orig_width * expansion];
		for (int grid_y = 0; grid_y < expansion; grid_y++) {
			for (int grid_x = 0; grid_x < expansion; grid_x++) {
				if (grid_y == 0 && grid_x == 0) {
					for (int y = 0; y < orig_height; y++) {
						for (int x = 0; x < orig_width; x++) {
							cost_matrix[y][x] = start_cost_matrix[y][x];
						}
					}
				} else if (grid_x == 0) {
					// Get from the cell above
					for (int y = 0; y < orig_height; y++) {
						for (int x = 0; x < orig_width; x++) {
							int xx = grid_x * orig_height + x;
							int new_val = cost_matrix[(grid_y - 1) * orig_height + y][xx] + 1;
							cost_matrix[grid_y * orig_height + y][xx] = new_val > 9 ? 1 : new_val;
						}
					}
				} else {
					// Get from the cell to the left
					for (int y = 0; y < orig_height; y++) {
						for (int x = 0; x < orig_width; x++) {
							int yy = grid_y * orig_height + y;
							int new_val = cost_matrix[yy][(grid_x - 1) * orig_width + x] + 1;
							cost_matrix[yy][grid_x * orig_width + x] = new_val > 9 ? 1 : new_val;
						}
					}
				}
			}
		}
		final int width = cost_matrix[0].length;
		final int height = cost_matrix.length;

		// Populate the possible destinations and associated costs for each node
		final Map<Integer, GraphNode<Integer, Point2D>> all_nodes = populateDestinations(cost_matrix);

		final GraphNode<Integer, Point2D> start_node = all_nodes.get(Integer.valueOf(0));
		final GraphNode<Integer, Point2D> end_node = all_nodes.get(Integer.valueOf(width * height - 1));

		Dijkstra.findRoute(start_node, end_node);

		return Integer.toString(end_node.cost());
	}

	private static Map<Integer, GraphNode<Integer, Point2D>> buildGraph(int[][] costMatrix) {
		final int width = costMatrix[0].length;
		final int height = costMatrix.length;

		final Map<Integer, GraphNode<Integer, Point2D>> all_nodes = new HashMap<>();

		// Convert the integer matrix into a graph
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final Point2D cell = new Point2D(x, y);
				final Integer id = Integer.valueOf(x + y * width);

				final GraphNode<Integer, Point2D> node = all_nodes.computeIfAbsent(id, i -> new GraphNode<>(i, cell));

				// The end node has no onward connections
				if (y == height - 1 && x == width - 1) {
					continue;
				}

				// Add the neighbours
				for (int dy = Math.max(y - 1, 0); dy <= Math.min(y + 1, height - 1); dy++) {
					for (int dx = Math.max(x - 1, 0); dx <= Math.min(x + 1, width - 1); dx++) {
						// No diagonals
						if ((x != dx || y != dy) && (x == dx || y == dy)) {
							final Point2D neighbour_cell = new Point2D(dx, dy);
							final Integer neighbour_id = Integer.valueOf(dx + dy * width);

							final GraphNode<Integer, Point2D> neighbour = all_nodes.computeIfAbsent(neighbour_id,
									i -> new GraphNode<>(i, neighbour_cell));
							node.addNeighbour(neighbour, costMatrix[dy][dx]);
						}
					}
				}
			}
		}

		return all_nodes;
	}

	private static Map<Integer, GraphNode<Integer, Point2D>> populateDestinations(int[][] costMatrix) {
		final int width = costMatrix[0].length;
		final int height = costMatrix.length;

		final Map<Integer, GraphNode<Integer, Point2D>> all_nodes = new HashMap<>();

		// Convert the integer matrix into a graph
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final Point2D cell = new Point2D(x, y);
				final Integer id = Integer.valueOf(x + y * width);

				final GraphNode<Integer, Point2D> node = all_nodes.computeIfAbsent(id, i -> new GraphNode<>(i, cell));

				// The end node has no onward connections
				if (y == height - 1 && x == width - 1) {
					continue;
				}

				// Add the neighbours
				// The optimal path can go up, down, left and right; not just down and right
				for (int dy = Math.max(0, y - 1); dy <= Math.min(height - 1, y + 1); dy++) {
					for (int dx = Math.max(0, x - 1); dx <= Math.min(width - 1, x + 1); dx++) {
						// No diagonals
						if ((x != dx || y != dy) && (x == dx || y == dy)) {
							final Point2D neighbour_cell = new Point2D(dx, dy);
							final Integer neighbour_id = Integer.valueOf(dx + dy * width);

							final GraphNode<Integer, Point2D> neighbour = all_nodes.computeIfAbsent(neighbour_id,
									i -> new GraphNode<>(i, neighbour_cell));
							node.addNeighbour(neighbour, costMatrix[dy][dx]);
						}
					}
				}
			}
		}

		return all_nodes;
	}

	public static int heuristic(Point2D from, Point2D to) {
		// Assume that 5 is a good approximation of the average cost
		// FIXME Why does changing to 5 * from.manhattanDistance(to); break it?
		return from.manhattanDistance(to);
	}
}
