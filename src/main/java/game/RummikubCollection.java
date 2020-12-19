package game;

import java.util.List;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A Collection of figures must have the same number and different colors on each, a collection must contain at least 3 figures
 * @author philipp
 *
 */
public class RummikubCollection implements IRummikubFigureBag{
	private List<RummikubFigure> figures;
	private long hash;
	
	public RummikubCollection() {
		this.figures = new ArrayList<RummikubFigure>();
	}
	
	/**
	 * Adds a figure, only adds it if the Number corresponds and the color is not already in the collection
	 */
	@Override
	public void addFigure(RummikubFigure fig) throws RummikubGameException
	{
		if(fig.getPlacement()!=RummikubPlacement.ON_TABLE)
		{
			throw new RummikubGameException("A Collection can only be made from figures on the table");
		}
		if (this.figures.size()==0)
		{
			this.figures.add(fig);
		}
		else
		{
			Iterator<RummikubFigure> irf = this.figures.iterator();
			RummikubFigure tf;
			boolean same_element_exists = false;
			while (irf.hasNext())
			{
				tf=irf.next();
				if (tf.getColor()==fig.getColor())
				{
					same_element_exists = true;
				}
			}
			if (same_element_exists==true)
			{
				throw new RummikubGameException(String.format("The Color %s already exists in the collection",fig.getColor()));
			}
			
			if (fig.getNumber()!= 0 && fig.getNumber() != this.figures.get(0).getNumber())
			{
				throw new RummikubGameException(String.format("%s has the wrong number for the collection which is %d", fig, this.figures.get(0).getNumber()));
			}
			else
			{
				this.figures.add(fig);
				this.figures.sort(null);
			}
		}
	}
	
	/**
	 * Validity check, only checks for size 3 or more since sanity check is already made upon adding a figure
	 */
	public boolean isValid()
	{
		return this.figures.size()>2;
	}
	
	public Iterator<RummikubFigure> iterator() {
        return this.figures.iterator();
	}

	@Override
	public long getHash() {
		return this.hash;
	}

	/**
	 * Sets a hash value the actual computation is done in The AspSolver
	 */
	@Override
	public void setHash(long hash) {
		this.hash = hash;
	}
	
	/**
	 * Equality check based on the has value
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof RummikubCollection)
		{
			return ((IRummikubFigureBag)other).getHash() == this.getHash();
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * returns the number of figure in the collection
	 */
	@Override
	public int getFigureCount() {
        return this.figures.size();
	}
	
	@Override public Stream<RummikubFigure> stream()
	{
		return  this.figures.stream();
	}
	
}
