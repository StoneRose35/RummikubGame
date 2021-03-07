package ch.sr35.rummikub.common;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import ch.sr35.rummikub.common.exceptions.GameException;

/**
 * Generic Interface for a set of figures
 * @author philipp
 *
 */
public interface IFigureBag {
	public void addFigure(Figure fig) throws GameException;
	public boolean isValid();
	public Iterator<Figure> iterator();
	public Stream<Figure> stream();
	public List<Figure> getFigures();
	public long getHash();
	public void setHash(long hash);
	public int getFigureCount();
	public float match(IFigureBag other);
}
