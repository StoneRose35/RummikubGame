package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RummikubSeries implements IRummikubFigureBag{
	private List<RummikubFigure> figures;
	private long hash;
	
	public RummikubSeries() {
		this.figures = new ArrayList<RummikubFigure>();
	}
	
	public void addFigure(RummikubFigure fig) throws RummikubGameException
	{
		if(fig.getPlacement()!=RummikubPlacement.ON_TABLE)
		{
			throw new RummikubGameException("A Series can only be made from figures on the table");
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
				if (tf.getNumber()==fig.getNumber())
				{
					same_element_exists = true;
				}
			}
			if (same_element_exists==true)
			{
				throw new RummikubGameException(String.format("The Number %d already exists in the series",fig.getNumber()));
			}
			if (fig.getColor()!=null && fig.getColor() != this.figures.get(0).getColor())
			{
				throw new RummikubGameException(String.format("%s has the wrong color for the series which is %s", fig, this.figures.get(0).getColor()));
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
			int n_last = this.figures.get(this.figures.size()-1).getNumber();
			int n_first = this.figures.get(0).getNumber();
			int sum=0;
			Iterator<RummikubFigure> it= figures.iterator();
			RummikubFigure el;
			while (it.hasNext())
			{
				el=it.next();
				sum += el.getNumber();
			}
			int sum_calc =(this.figures.size()*(n_last+n_first));
			sum_calc /= 2;
			return sum_calc == sum;
		}
	}

	public Iterator<RummikubFigure> iterator() {
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
		if (other instanceof RummikubSeries)
		{
			return ((IRummikubFigureBag)other).getHash() == this.getHash();
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
}
