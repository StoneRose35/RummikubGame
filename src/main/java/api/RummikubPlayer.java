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
	
	public void addFigure(RummikubFigure rf) throws RummikubApiException
	{
		if (this.figures.stream().map(f -> f.isValid()).anyMatch(v -> v==false))
		{
			throw new RummikubApiException("Figures of Player " + this.getName() + " are invalid");
		}
		int lastShelf = this.figures.stream().mapToInt(f -> f.getShelfNr()).max().orElse(0);
		int lastPosition = this.figures.stream().filter(f -> f.getShelfNr()==lastShelf).mapToInt(f -> f.getPosition()).max().orElse(-1);
		rf.setPosition(lastPosition + 1);
		rf.setShelfNr(lastShelf);
		this.figures.add(rf);
	}
		

}
