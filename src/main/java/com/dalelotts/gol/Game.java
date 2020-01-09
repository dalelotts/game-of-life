package com.dalelotts.gol;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;


final class Game {
	private static final Set<Point> NEIGHBORS = Set.of(
			new Point(-1, -1),
			new Point(-1, 0),
			new Point(-1, 1),
			new Point(0, -1),
			new Point(0, 1),
			new Point(1, -1),
			new Point(1, 0),
			new Point(1, 1)
	);

	private final Set<Point> liveCells = new HashSet<>();

	Game(final Point... initialState) {
		liveCells.addAll(Arrays.asList(initialState));
	}

	private int countLiveNeighbors(final Point point) {
		return (int) neighbors(point).filter(liveCells::contains).count();
	}

	Set<Point> getLiveCells() {
		return Collections.unmodifiableSet(liveCells);
	}

	private Boolean hasThreeLiveNeighbors(final Point point) {
		return countLiveNeighbors(point) == 3;
	}

	private Boolean hasTwoOrThreeLiveNeighbors(final Point point) {
		final int liveNeighbors = countLiveNeighbors(point);
		return liveNeighbors == 2 || liveNeighbors == 3;
	}

	private static Stream<Point> neighbors(final Point point) {
		return NEIGHBORS.stream()
				.map(neighbor -> new Point(point.x + neighbor.x, point.y + neighbor.y));
	}

	private Stream<Point> newCells() {
		return liveCells
				.stream()
				.flatMap(this::toDeadNeighbors)
				.distinct()
				.filter(this::hasThreeLiveNeighbors);
	}

	private Stream<Point> survivors() {
		return liveCells
				.stream()
				.filter(this::hasTwoOrThreeLiveNeighbors);
	}

	void tick() {
		final Set<Point> nextState = concat(survivors(), newCells()).collect(toSet());

		liveCells.clear();
		liveCells.addAll(nextState);
	}

	private Stream<Point> toDeadNeighbors(final Point point) {
		return neighbors(point).filter(not(liveCells::contains));
	}
}
