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
}
