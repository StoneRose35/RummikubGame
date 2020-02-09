package game;
import java.util.List;
import java.util.regex.*;
import asp.AspHelper;
import asp.AspPredicate;

public class RummikubFigure {
	private int number;
	private RummikubColor color;
	private int instance;
	private RummikubPlacement placement;
	
	public RummikubFigure()
	{
		
	}
	
	public static RummikubFigure getRummikubFigure(String aspRepresentation)
	{
		RummikubFigure fig = null;
		try {
			fig = new RummikubFigure(aspRepresentation);
		}
		catch (RummikubException e)
		{ 
		}
		return fig;
		
	}
	
	private RummikubFigure(String aspRepresentation) throws RummikubException
	{
 
		AspPredicate pred = AspHelper.parsePredicate(aspRepresentation);
		if (pred==null)
		{
			throw new RummikubException(String.format("Could not Instantiate Figure from %s ", aspRepresentation));
		}
		
		if (pred.getName().equals("onshelf") || pred.getName().equals("remaining"))
		{
			this.placement = RummikubPlacement.ON_SHELF;
		}
		else if (pred.getName().equals("ontable"))
		{
			this.placement = RummikubPlacement.ON_TABLE;
		}
		else
		{
			throw new RummikubException(String.format("Could not decode placement from %s ", aspRepresentation));
		}
		List<Integer> atoms = pred.atomsAsIntegers();
		if (atoms != null && atoms.size()==3)
		{
		    this.setNumber(atoms.get(0));
		    this.setInstance(atoms.get(2));
		    this.setColor(RummikubColorFactory.getByCode(atoms.get(1)));
		}
		else
		{
			throw new RummikubException(String.format("Could not decode figure representation from %s ", aspRepresentation));
		}
		
	}
	

	public RummikubPlacement getPlacement() {
		return placement;
	}

	public void setPlacement(RummikubPlacement placement) {
		this.placement = placement;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) throws RummikubException {
		if (number < 1 || number > 13)
		{
			throw new RummikubException("Number should bei within 1 to 13");
		}
		this.number = number;
	}

	public RummikubColor getColor() {
		return color;
	}

	public void setColor(RummikubColor color) {
		this.color = color;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) throws RummikubException {
		if (instance <1 || instance > 4)
		{
			throw new RummikubException("Instance should be 1 or 2 for regular figures or 3, 4 for jokers");
		}
		this.instance = instance;
	}
	
	public String getAspRepresentation() 
	{
		return this.placement + "(" + this.number + "," + this.color.getColorcode() + "," + this.instance + ").";	
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof RummikubFigure)
		{
			RummikubFigure rf = (RummikubFigure)other;
			if(rf.getColor() == this.getColor() && rf.getInstance() == this.getInstance() && rf.getNumber() == this.getNumber())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("#%s in %s, instance %s", this.getNumber(),this.getColor(), this.getInstance());
	}
	
}
