package game;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Generic Interface for a set of figures
 * @author philipp
 *
 */
public interface IRummikubFigureBag {
	public void addFigure(RummikubFigure fig) throws RummikubGameException;
	public boolean isValid();
	public Iterator<RummikubFigure> iterator();
	public Stream<RummikubFigure> stream();
	public long getHash();
	public void setHash(long hash);
	public int getFigureCount();
}
