package ch.sr35.rummikub.common;
//import java.awt.Color;

/**
 * Defines all the figure colors
 * @author philipp
 *
 */
public enum Color {
	BLACK(1, new java.awt.Color(0,0,0), "black"),
	RED(2, new java.awt.Color(255,0,0), "red"),
	YELLOW(3, new java.awt.Color(255,255,0), "yellow"),
	BLUE (4, new java.awt.Color(0,0,255), "blue");
	
	private final int colorcode;
	private final java.awt.Color color;
	private final String repr;
	
	private Color(int clrcode, java.awt.Color clr, String repr)
	{
		this.color = clr;
		this.colorcode = clrcode;
		this.repr = repr;
	}
	
	public int getColorcode()
	{
		return this.colorcode;
	}
	
	public java.awt.Color getColor()
	{
		return this.color;
	}
	
	public static Color fromCode(int code)
	{
		switch(code)
		{
		case 1:
			return Color.BLACK;
		case 2:
			return Color.RED;
		case 3:
			return Color.YELLOW;
		case 4:
			return Color.BLUE;
		default:
			return null;
		}
	}
	
	@Override
    public String toString()
    {
    	return this.repr;
    }
}

