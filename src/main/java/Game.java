import io.reactivex.Observable;

import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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

	private final Set<Point> liveCells = new TreeSet<>(Comparator.comparingInt((Point point) -> point.x).thenComparingInt(point -> point.y));

	Game(final Point... initialState) {
		liveCells.addAll(Arrays.asList(initialState));
	}

	private int countLiveNeighbors(final Point point) {
		return (int) getNeighbors(point).filter(liveCells::contains).count();
	}

	private Stream<Point> deadNeighbors(final Point point) {
		return getNeighbors(point).filter(neighbor -> !liveCells.contains(neighbor));
	}

	private static Stream<Point> getNeighbors(final Point point) {
		return Arrays
				.stream(NEIGHBORS)
				.map(neighborAdjustment -> new Point(point.x + neighborAdjustment.x, point.y + neighborAdjustment.y));
	}

	Observable<Point> tick() {
		final List<Point> newLiveCells = liveCells
				.stream()
				.filter(point -> {
					final int liveNeighbors = countLiveNeighbors(point);
					return liveNeighbors > 1 && liveNeighbors < 3;
				})
				.collect(Collectors.toList());

		final Set<Point> newCells = liveCells
				.stream()
				.flatMap(this::deadNeighbors)
				.distinct()
				.filter(point -> countLiveNeighbors(point) == 3)
				.collect(Collectors.toSet());


		liveCells.clear();
		liveCells.addAll(newLiveCells);
		liveCells.addAll(newCells);

		return Observable.fromIterable(liveCells);
	}
}
