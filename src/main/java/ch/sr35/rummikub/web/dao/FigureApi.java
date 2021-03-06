package ch.sr35.rummikub.web.dao;

import ch.sr35.rummikub.common.Color;
import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.Placement;
import ch.sr35.rummikub.common.exceptions.GeneralException;

public class FigureApi {
	private int colorcode;
	private int instance;
	private int number;
	private Integer shelfNr;
	private Integer position;

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

	public int getColorcode() {
		return colorcode;
	}

	public void setColorcode(int colorcode) {
		this.colorcode = colorcode;
	}
	
	public Figure toRummikubFigure(Placement placement)
	{
		Figure res = new Figure();
		try {
			res.setInstance(this.getInstance());
			res.setNumber(this.getNumber());
			res.setColor(Color.fromCode(this.getColorcode()));
			res.setPlacement(placement);
			res.setPosition(this.getPosition());
			res.setShelfNr(this.getShelfNr());
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		return res;
	}
	
	public static FigureApi fromRummikubFigure(Figure f)
	{
		FigureApi fa = new FigureApi();
		if (f.getColor() != null)
		{
			fa.setColorcode(f.getColor().getColorcode());
		}
		fa.setInstance(f.getInstance());
		fa.setNumber(f.getNumber());
		fa.setPosition(f.getPosition());
		fa.setShelfNr(f.getShelfNr());
		return fa;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof FigureApi)
		{
			FigureApi rfo=(FigureApi)o;
			if (this.instance < 3 && rfo.instance < 3)
			{
				return rfo.colorcode==this.colorcode && rfo.instance==this.instance && rfo.number==this.number;
			}
			else if (this.instance > 2 && rfo.instance > 2 )
			{
				return this.instance == rfo.instance;
			}
		}
		return false;
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
	

}
