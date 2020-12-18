package game;

import java.util.Iterator;

/**
 * Generic Interface for a set of figures
 * @author philipp
 *
 */
public interface IRummikubFigureBag {
	public void addFigure(RummikubFigure fig) throws RummikubGameException;
	public boolean isValid();
	public Iterator<RummikubFigure> iterator();
	public long getHash();
	public void setHash(long hash);
	public int getFigureCount();
}
