/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.awt.Point;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GameTest {

	@Test
	public void cellWithFourLiveNeighborsDies() {
		// Cells are intentionally out of order since order does not matter.
		final Game game = new Game(
				new Point(-1, 1),
				new Point(0, 1),
				new Point(-1, 0),
				new Point(-1, -1),
				new Point(0, -1)
		);
		final TestObserver<Point> testObserver = game.tick().test();

		assertTrue(testObserver.awaitTerminalEvent());

		testObserver
				.assertNoErrors()
				.assertTerminated();

		assertThat(testObserver.values(), hasItems(
				new Point(-1, -1),
				new Point(-1, 1),
				new Point(0, -1),
				new Point(0, 1)
		));
	}

	@Test
	public void cellWithNoNeighborsDies() {
		final Game                game         = new Game(new Point(0, 0));
		final TestObserver<Point> testObserver = game.tick().test();

		assertTrue(testObserver.awaitTerminalEvent());

		testObserver
				.assertNoErrors()
				.assertValueCount(0);
	}

	@Test
	public void cellWithOneLiveNeighborInColumnDies() {
		// Cells are intentionally out of order since order does not matter.
		final Game game = new Game(
				new Point(0, 0),
				new Point(0, 1),
				new Point(0, -1)
		);
		final TestObserver<Point> testObserver = game.tick().test();

		assertTrue(testObserver.awaitTerminalEvent());

		testObserver
				.assertNoErrors()
				.assertTerminated();

		assertThat(testObserver.values(), hasItems(
				new Point(0, 0)
		));
	}

	@Test
	public void cellWithOneLiveNeighborInRowDies() {
		final Game game = new Game(
				new Point(-1, 1),
				new Point(0, 0),
				new Point(1, -1)
		);
		final TestObserver<Point> testObserver = game.tick().test();

		assertTrue(testObserver.awaitTerminalEvent());

		testObserver
				.assertNoErrors()
				.assertValues(
						new Point(0, 0)
				);
	}

	@Test
	public void deadCellWithThreeLiveNeighborIsBorn() {
		// Cells are intentionally out of order since order does not matter.
		final Game game = new Game(
				new Point(-1, 1),
				new Point(0, 1),
				new Point(-1, 0)
		);
		final TestObserver<Point> testObserver = game.tick().test();

		assertTrue(testObserver.awaitTerminalEvent());

		testObserver
				.assertNoErrors()
				.assertValues(
						new Point(-1, 0),
						new Point(-1, 1),
						new Point(0, 0),
						new Point(0, 1)
				);
	}


}
