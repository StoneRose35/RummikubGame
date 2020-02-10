package game;
import java.awt.Color;


public enum RummikubColor {
	BLACK(1, new Color(0,0,0), "black"),
	RED(2, new Color(255,0,0), "red"),
	YELLOW(3, new Color(255,255,0), "yellow"),
	BLUE (4, new Color(0,0,255), "blue");
	
	private final int colorcode;
	private final Color color;
	private final String repr;
	
	private RummikubColor(int clrcode, Color clr, String repr)
	{
		this.color = clr;
		this.colorcode = clrcode;
		this.repr = repr;
	}
	
	public int getColorcode()
	{
		return this.colorcode;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	@Override
    public String toString()
    {
    	return this.repr;
    }
}

