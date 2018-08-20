/* See the file "LICENSE" for the full license governing this code. */

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Dale "Ducky" Lotts
 * @since 8/20/18.
 */

final class Application {

	public static void main(final String... args) {
		try (final Scanner scanner = new Scanner(System.in)) {
			System.out.println("Please press any key then enter to see the next game state:");
			System.out.println("(Press CTRL+Z to end the program):");

			final Game game = new Game(
					new Point(-1, 1),
					new Point(0, 1),
					new Point(-1, 0),
					new Point(-1, -1),
					new Point(0, -1)
			);

			System.out.println(renderAsciiTable(game.getLiveCells()));

			while (scanner.hasNext()) {
				scanner.next();

				game.tick();

				final Set<Point> liveCells = game.getLiveCells();

				if (liveCells.isEmpty()) {
					System.out.println("No live cells!");
					return;
				}

				System.out.println(renderAsciiTable(liveCells));
			}
		}
	}

	private Application() {
	}

	private static String renderAsciiTable(final Set<Point> cells) {
		final ArrayList<Point> liveCells = new ArrayList<>(cells);

		final int startRow = liveCells.stream().mapToInt(point -> point.x).min().orElse(0);
		final int endRow   = liveCells.stream().mapToInt(point -> point.x).max().orElse(0);

		final int startColumn = liveCells.stream().mapToInt(point -> point.y).min().orElse(0);
		final int endColumn   = liveCells.stream().mapToInt(point -> point.y).max().orElse(0);
		final int columnCount = endColumn - startColumn;

		final AsciiTable asciiTable = new AsciiTable();

		asciiTable.addRule();

		final List<String> columnHeaders = new ArrayList<>(columnCount);
		columnHeaders.add(" ");
		for (int column = startColumn; column <= endColumn; column++) {
			columnHeaders.add(String.valueOf(column));
		}
		asciiTable.addRow(columnHeaders.toArray()).setTextAlignment(TextAlignment.CENTER);
		asciiTable.addRule();

		for (int row = startRow; row <= endRow; row++) {
			final List<String> columnValues = new ArrayList<>(columnCount);
			columnValues.add(String.valueOf(row));

			for (int column = startColumn; column <= endColumn; column++) {
				final Point point = new Point(row, column);
				columnValues.add(liveCells.contains(point) ? " •‿• " : " ");
			}
			asciiTable.addRow(columnValues.toArray()).setTextAlignment(TextAlignment.CENTER);
			asciiTable.addRule();
		}

		return asciiTable.render();
	}

}
