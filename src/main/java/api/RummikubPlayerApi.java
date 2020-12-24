package api;

import java.util.List;

import game.RummikubFigure;

public class RummikubPlayerApi extends RummikubPlayer{
	
	public RummikubPlayerApi(RummikubPlayer p)
	{
		this.setName(p.getName());
		this.setActive(p.isActive());
	}

	@Override
	public List<RummikubFigure> getFigures() {
		return null;
	}
}
