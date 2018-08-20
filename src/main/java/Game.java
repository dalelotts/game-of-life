import com.google.common.collect.Streams;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Game {

	private static final Point[] NEIGHBORS = {
			new Point(-1, -1),
			new Point(-1, 0),
			new Point(-1, 1),
			new Point(0, -1),
			new Point(0, 1),
			new Point(1, -1),
			new Point(1, 0),
			new Point(1, 1)
	};

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
		return Arrays
				.stream(NEIGHBORS)
				.map(neighbor -> new Point(point.x + neighbor.x, point.y + neighbor.y));
	}

	// This will not be necessary as Predicate#not is being added in Java 11.
	private static <R> Predicate<R> not(final Predicate<R> predicate) {
		return predicate.negate();
	}

	void tick() {
		final Set<Point> nextState = Streams.concat(
				liveCells
						.stream()
						.filter(this::hasTwoOrThreeLiveNeighbors),
				liveCells
						.stream()
						.flatMap(this::toDeadNeighbors)
						.distinct()
						.filter(this::hasThreeLiveNeighbors)
		).collect(Collectors.toSet());

		liveCells.clear();
		liveCells.addAll(nextState);
	}

	private Stream<Point> toDeadNeighbors(final Point point) {
		return neighbors(point).filter(not(liveCells::contains));
	}
}
