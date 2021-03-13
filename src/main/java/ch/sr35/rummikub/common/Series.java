package ch.sr35.rummikub.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import ch.sr35.rummikub.common.exceptions.GameException;

/**
 * A series of figures consists of the figures of the same color a successive number, example: 1,2,3,4
 * @author philipp
 *
 */
public class Series implements IFigureBag{
	private List<Figure> figures;
	private long hash;
	
	public Series() {
		this.figures = new ArrayList<Figure>();
	}
	
	public void addFigure(Figure fig) throws GameException
	{
		if(fig.getPlacement()!=Placement.ON_TABLE)
		{
			throw new GameException("A Series can only be made from figures on the table");
		}
		if (this.figures.size()==0)
		{
			this.figures.add(fig);
		}
		else
		{
			Iterator<Figure> irf = this.figures.iterator();
			Figure tf;
			boolean same_element_exists = false;
			while (irf.hasNext())
			{
				tf=irf.next();
				if (tf.getNumber()==fig.getNumber())
				{
					same_element_exists = true;
				}
			}
			if (same_element_exists==true)
			{
				throw new GameException(String.format("The Number %d already exists in the series",fig.getNumber()));
			}
			if (fig.getColor()!=null && fig.getColor() != this.figures.get(0).getColor())
			{
				throw new GameException(String.format("%s has the wrong color for the series which is %s", fig, this.figures.get(0).getColor()));
			}
			else
			{
				this.figures.add(fig);
				this.figures.sort(null);
			}
		}
	}
	
	public boolean isValid()
	{
		if (this.figures.size()<3)
		{
			return false;
		}
		else // check if the accumulated sum is equal so the sum of the arithmetic series
		{
			int[] fnumbers = this.figures
					.stream()
					.mapToInt(f -> f.getNumber())
					.sorted()
					.toArray();
			for (int cnt=0;cnt<fnumbers.length-1;cnt++)
			{
				if (fnumbers[cnt+1]-fnumbers[cnt] != 1)
				return false;
			}
			return true;
		}
	}

	public Iterator<Figure> iterator() {
         return this.figures.iterator();
	}

	@Override
	public long getHash() {
		return this.hash;
	}

	@Override
	public void setHash(long hash) {
		this.hash = hash;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Series)
		{
			return ((IFigureBag)other).getHash() == this.getHash();
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int getFigureCount() {
        return this.figures.size();
	}
	
	
	@Override public Stream<Figure> stream()
	{
		return  this.figures.stream();
	}

	@Override
	public float match(IFigureBag other) {
		
		float matchcnt=0;
		Iterator<Figure> otherFigures = other.iterator();
		while (otherFigures.hasNext()) {
			Figure f = otherFigures.next();
			Iterator<Figure> thisFigures = this.iterator();
			while (thisFigures.hasNext())
			{
				Figure f2 = thisFigures.next();
				if (f2.compareTo(f)==0)
				{
					if(this.figures.indexOf(f2) == other.getFigures().indexOf(f))
					{
						matchcnt += 2;
					}
					else
					{
						matchcnt += 1;
					}
				}
			}
			
		}
		float divisor = (this.getFigureCount() < other.getFigureCount()) ? other.getFigureCount() : this.getFigureCount();
		if (divisor > 0.0 )
		{
			matchcnt /= (divisor*2.0);
		}
		return matchcnt;
	}
	
@Override
public List<Figure> getFigures() {
	
	return this.figures;
}
}
