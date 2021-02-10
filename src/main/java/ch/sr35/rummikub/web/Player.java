package ch.sr35.rummikub.web;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.exceptions.ApiException;
import ch.sr35.rummikub.web.dao.PlayerApi;

@Component
public class Player extends PlayerApi {
	

	private List<Figure> figures;
	

	public Player()
	{
		super();
		this.figures=new ArrayList<Figure>();
	}

	
	public List<Figure> getFigures() {
		return figures;
	}
	

	public void setFigures(List<Figure> figures) {
		this.figures = figures;
	}
	
	public void addFigure(Figure rf) throws ApiException
	{
		if (this.figures.stream().map(f -> f.isValid()).anyMatch(v -> v==false))
		{
			throw new ApiException("Figures of Player " + this.getName() + " are invalid");
		}
		int lastShelf = this.figures.stream().mapToInt(f -> f.getShelfNr()).max().orElse(0);
		int lastPosition = this.figures.stream().filter(f -> f.getShelfNr()==lastShelf).mapToInt(f -> f.getPosition()).max().orElse(-1);
		rf.setPosition(lastPosition + 1);
		rf.setShelfNr(lastShelf);
		this.figures.add(rf);
	}
		

}
