package api;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import game.RummikubFigure;

@Component
public class RummikubPlayer {
	
	private String name;
	private boolean active;
	private List<RummikubFigure> figures;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public RummikubPlayer()
	{
		this.figures=new ArrayList<RummikubFigure>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RummikubFigure> getFigures() {
		return figures;
	}

	public void setFigures(List<RummikubFigure> figures) {
		this.figures = figures;
	}
		

}
