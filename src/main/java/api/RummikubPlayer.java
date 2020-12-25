package api;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import game.RummikubFigure;

@Component
public class RummikubPlayer extends RummikubPlayerApi {
	

	private List<RummikubFigure> figures;
	

	public RummikubPlayer()
	{
		super();
		this.figures=new ArrayList<RummikubFigure>();
	}

	public List<RummikubFigure> getFigures() {
		return figures;
	}

	public void setFigures(List<RummikubFigure> figures) {
		this.figures = figures;
	}
		

}
