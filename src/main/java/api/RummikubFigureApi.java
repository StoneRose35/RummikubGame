package api;

import game.RummikubColor;
import game.RummikubException;
import game.RummikubFigure;
import game.RummikubPlacement;

public class RummikubFigureApi {
	private RummikubColorApi color;
	private int instance;
	private int number;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public RummikubColorApi getColor() {
		return color;
	}

	public void setColor(RummikubColorApi color) {
		this.color = color;
	}
	
	public RummikubFigure toRummikubFigure(RummikubPlacement placement)
	{
		RummikubFigure res = new RummikubFigure();
		try {
			res.setInstance(this.getInstance());
			res.setNumber(this.getNumber());
			res.setColor(RummikubColor.fromCode(this.getColor().getCode()));
			res.setPlacement(placement);
		} catch (RummikubException e) {
			e.printStackTrace();
		}

		return res;
	}
	
	public static RummikubFigureApi fromRummikubFigure(RummikubFigure f)
	{
		RummikubFigureApi fa = new RummikubFigureApi();
		if (f.getInstance()<3)
		{
			fa.setColor(RummikubColorApi.fromCode(f.getColor().getColorcode()));
		}
		fa.setInstance(f.getInstance());
		fa.setNumber(f.getNumber());
		return fa;
	}
	

}
