package ch.sr35.rummikub.common;
import java.util.List;

import ch.sr35.rummikub.asp.AspHelper;
import ch.sr35.rummikub.asp.AspPredicate;
import ch.sr35.rummikub.common.exceptions.GeneralException;

/**
 * a Representation of a rummikub figurem, it has a number and a color and exists somewhere (on the table, in the bag/on the stack or 
 * an on player's shelf. Since all Rummikub figures exist twice they are distinguished using their instance which is 0 or 1 
 * for normal figures and 3 or 4 for jokers
 * @author philipp
 *
 */
public class Figure implements Comparable<Figure> {
	private int number = 0;
	private Color color=null;
	private int instance=0;
	private Placement placement=null;
	private Integer shelfNr;
	private Integer position;
	
	public Figure()
	{
		
	}
	
	/**
	 * Generates a figure from a asp representation, this is actually a factory method
	 * return null if something goes wrong
	 * @param aspRepresentation
	 * @return
	 */
	public static Figure getRummikubFigure(String aspRepresentation)
	{
		Figure fig = null;
		try {
			fig = new Figure(aspRepresentation);
		}
		catch (GeneralException e)
		{ 
		}
		return fig;
		
	}
	
	
	private Figure(String aspRepresentation) throws GeneralException
	{
 
		AspPredicate pred = AspHelper.parsePredicate(aspRepresentation);
		if (pred==null)
		{
			throw new GeneralException(String.format("Could not Instantiate Figure from %s ", aspRepresentation));
		}
		
		if (pred.getName().equals("onshelf") || pred.getName().equals("remaining"))
		{
			this.placement = Placement.ON_SHELF;
		}
		else if (pred.getName().equals("ontable"))
		{
			this.placement = Placement.ON_TABLE;
		}
		else
		{
			throw new GeneralException(String.format("Could not decode placement from %s ", aspRepresentation));
		}
		List<Integer> atoms = pred.atomsAsIntegers();
		if (atoms != null && atoms.size()==3)
		{
		    this.setNumber(atoms.get(0));
		    this.setInstance(atoms.get(2));
		    this.setColor(ColorFactory.getByCode(atoms.get(1)));
		}
		else if (atoms != null && atoms.size()==1 && this.placement == Placement.ON_SHELF) // special case of Joker on shelf, only the instance is defined
		{
			this.setInstance(atoms.get(0)+2);
		}
		else if (atoms != null && atoms.size()==1 && this.placement == Placement.ON_TABLE) // Joker on table
		{
			throw new GeneralException("Joker on table is created only using the exact representation");
		}
		else
		{
			throw new GeneralException(String.format("Could not decode figure representation from %s ", aspRepresentation));
		}
		
	}
	

	public Placement getPlacement() {
		return placement;
	}

	public void setPlacement(Placement placement) {
		this.placement = placement;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) throws GeneralException {
		if (number < 0 || number > 13)
		{
			throw new GeneralException("Number should be within 0 to 13");
		}
		this.number = number;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
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

	public void setInstance(int instance) throws GeneralException {
		if (instance <1 || instance > 4)
		{
			throw new GeneralException("Instance should be 1 or 2 for normal figures or 3 to 4 for jokers");
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
		if (other instanceof Figure)
		{
			Figure rf = (Figure)other;
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
	public int compareTo(Figure fig) {
		Color[] colors=Color.values();
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

	public Integer getShelfNr() {
		return shelfNr;
	}

	public void setShelfNr(Integer shelfNr) {
		this.shelfNr = shelfNr;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public boolean isValid()
	{
		if ((this.placement != Placement.ON_SHELF && (this.position != null || this.position!=null))
			|| (this.placement == Placement.ON_SHELF && (this.position == null || this.position==null)))
		{
			return false;
		}
		if (this.position != null && this.position < 0)
		{
			return false;
		}
		if (this.shelfNr != null && this.shelfNr < 0)
		{
			return false;
		}
		return true;
	}
	
}
