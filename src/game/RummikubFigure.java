package game;
import java.util.regex.*;

public class RummikubFigure {
	private int number;
	private RummikubColor color;
	private int instance;
	private RummikubPlacement placement;
	
	public RummikubFigure()
	{
		
	}
	
	public RummikubFigure(String aspRepresentation) throws RummikubException
	{

		Pattern p = Pattern.compile("(onshelf|ontable)\\(([0-9]{1,2}),([0-9]),([0-9])\\)\\.");//. represents single character  
		Matcher m = p.matcher(aspRepresentation);  
		if (!m.matches())
		{
			throw new RummikubException(String.format("Could not Instantiate Figure from %s ", aspRepresentation));
		}
		
		if (m.group(1).equals("onshelf"))
		{
			this.placement = RummikubPlacement.ON_SHELF;
		}
		else if (m.group(1).equals("ontable"))
		{
			this.placement = RummikubPlacement.ON_TABLE;
		}
		else
		{
			throw new RummikubException(String.format("Could not decode placement from %s ", aspRepresentation));
		}
		
		this.setNumber(Integer.parseInt(m.group(2)));
		this.setInstance(Integer.parseInt(m.group(4)));
		this.setColor(RummikubColorFactory.getByCode(Integer.parseInt(m.group(3))));
		
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
		String encoding = "";
		switch (this.placement)
		{
		    case ON_SHELF:
		    	encoding += "onshelf(";
		        break;
		    case ON_TABLE:
		    	encoding += "ontable(";
		    	break;
		    default:
		    	return "";	    
		}
		encoding += this.number + "," + this.color.getColorcode() + "," + this.instance + ").";
		
		return encoding;
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
