package game;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class RummikubCollection implements IRummikubFigureBag{
	private List<RummikubFigure> figures;
	
	public RummikubCollection() {
		this.figures = new ArrayList<RummikubFigure>();
	}
	
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
	
	public boolean isValid()
	{
		return this.figures.size()>2;
	}
	
	public Iterator<RummikubFigure> iterator() {
        return this.figures.iterator();
	}
	
}
