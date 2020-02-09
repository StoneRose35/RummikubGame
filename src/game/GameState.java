package game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	/**
	 * Represents a Rummikub game state as seen by a single player, which sees all the figures in the game and his figures
	 * on the shelf
	 */
	private List<RummikubFigure> onshelf;
	private List<RummikubFigure> ontable;
	private String aspRepresentation;
	private int sumLaid;
	
	public GameState() 
	{
		this.onshelf = new ArrayList<RummikubFigure>();
		this.ontable = new ArrayList<RummikubFigure>();
	}
	
	public void drawFigure(RummikubFigure figure) throws RummikubGameException
	{
		if (this.onshelf.contains(figure))
		{
			throw new RummikubGameException(String.format("The Figure %s is already in the game",figure),4);
		}
		this.onshelf.add(figure);
	}
	
	public void addFigure(RummikubFigure fig)
	{
		if (fig.getPlacement()==RummikubPlacement.ON_TABLE)
		{
		    this.ontable.add(fig);
		}
		else if (fig.getPlacement()==RummikubPlacement.ON_SHELF)
		{
			this.onshelf.add(fig);
		}
	}
	
	public String toAspRepresentation()
	{
		this.aspRepresentation = "";
		this.onshelf.forEach(fig -> {
			this.aspRepresentation += fig.getAspRepresentation() + "\n";
		});
		return this.aspRepresentation;
	}
	
	public List<RummikubFigure> getTableFigures()
	{
		return this.ontable;
	}
	
	public List<RummikubFigure> getShelfFigures()
	{
		return this.onshelf;
	}

	public int getSumLaid() {
		return sumLaid;
	}

	public void setSumLaid(int sumLaid) {
		this.sumLaid = sumLaid;
	}
	
}
