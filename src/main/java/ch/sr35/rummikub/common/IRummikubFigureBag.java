package ch.sr35.rummikub.common;

import java.util.Iterator;
import java.util.stream.Stream;

import ch.sr35.rummikub.common.exceptions.RummikubGameException;

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
