package game;
import java.util.List;

import asp.AspHelper;
import asp.AspPredicate;

/**
 * a Representation of a rummikub figurem, it has a number and a color and exists somewhere (on the table, in the bag/on the stack or 
 * an on player's shelf. Since all Rummikub figures exist twice they are distinguished using their instance which is 0 or 1 
 * for normal figures and 3 or 4 for jokers
 * @author philipp
 *
 */
public class RummikubFigure implements Comparable<RummikubFigure> {
	private int number = 0;
	private RummikubColor color=null;
	private int instance=0;
	private RummikubPlacement placement=null;
	
	public RummikubFigure()
	{
		
	}
	
	/**
	 * Generates a figure from a asp representation, this is actually a factory method
	 * return null if something goes wrong
	 * @param aspRepresentation
	 * @return
	 */
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
		else if (atoms != null && atoms.size()==1 && this.placement == RummikubPlacement.ON_SHELF) // special case of Joker on shelf, only the instance is defined
		{
			this.setInstance(atoms.get(0)+2);
		}
		else if (atoms != null && atoms.size()==1 && this.placement == RummikubPlacement.ON_TABLE) // Joker on table
		{
			throw new RummikubException("Joker on table is created only using the exact representation");
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
	
	public int getScore()
	{
		if(this.instance < 3)
		{
			return this.number;
		}
		else
		{
			return 50;
		}
	}

	public void setInstance(int instance) throws RummikubException {
		if (instance <1 || instance > 4)
		{
			throw new RummikubException("Instance should be 1 or 2 for normal figures or 3 to 4 for jokers");
		}
		this.instance = instance;
	}
	
	public String getAspRepresentation() 
	{
		if (this.getInstance()<3)
		{
		    return this.placement + "(" + this.number + "," + this.color.getColorcode() + "," + this.instance + ").";
		}
		else // Joker
		{
			return this.placement + "(" + (this.instance-2) + ").";
		}
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
		return String.format("<#%s in %s instance %s>", this.getNumber(),this.getColor(), this.getInstance());
	}
	

	/**
	 * Comparison function used for sorting, comparison is done hierarchically using
	 * * Color
	 * * Number
	 * * Instance
	 */
	@Override
	public int compareTo(RummikubFigure fig) {
		RummikubColor[] colors=RummikubColor.values();
		int idx1=-1,idx2=-2;
		for (int cnt=0;cnt<colors.length;cnt++)
		{
			if (fig.getColor()==colors[cnt])
			{
				idx2 = cnt;
			}
			if (this.getColor()==colors[cnt])
			{
				idx1 = cnt;
			}
		}
		if (idx1 == idx2)
		{
			int n1,n2;
			n1 = this.getNumber();
			n2 = fig.getNumber();
			if (n1 ==n2)
			{
				return this.getInstance() - fig.getInstance();
			}
			else
			{
				return n1-n2;
			}
		}
		else
		{
			return idx1-idx2;
		}
	}
	
}
