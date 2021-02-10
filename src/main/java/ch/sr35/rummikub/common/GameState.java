package ch.sr35.rummikub.common;

import java.util.ArrayList;
import java.util.List;

import ch.sr35.rummikub.common.exceptions.RummikubGameException;

public class GameState {
	/**
	 * Represents a Rummikub game state as seen by a single player, which sees all the figures in the game and his figures
	 * on the shelf
	 */
	private List<RummikubFigure> onshelf;
	public List<RummikubFigure> getOnshelf() {
		return onshelf;
	}

	public void setOnshelf(List<RummikubFigure> onshelf) {
		this.onshelf = onshelf;
	}

	public List<RummikubFigure> getOntable() {
		return ontable;
	}

	public void setOntable(List<RummikubFigure> ontable) {
		this.ontable = ontable;
	}

	private List<RummikubFigure> ontable;
	private String aspRepresentation;
	private int sumLaid;
	private int roundNr;
	
	public GameState() 
	{
		this.onshelf = new ArrayList<RummikubFigure>();
		this.ontable = new ArrayList<RummikubFigure>();
		this.roundNr = 0;
	}
	
	/**
	 * CAll this function when the player draws a figure, e.g. places the figure on his shelf
	 * @param figure the figure to be drawn
	 * @throws RummikubGameException if the figure is already on the shelf
	 */
	public void drawFigure(RummikubFigure figure) throws RummikubGameException
	{
		if (this.onshelf.contains(figure))
		{
			throw new RummikubGameException(String.format("The Figure %s is already in the game",figure),4);
		}
		this.onshelf.add(figure);
	}
	
	/**
	 * Adds a figure either to the player's shelf or the table, this function doesn't throw an exception 
	 * for the case a figure is already in the game
	 * @param fig
	 */
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
	
	/**
	 * return a clingo representation of the game state meaning a representation of the figures in the sheplf and on the table
	 * 
	 * @return the clingo representation as a string
	 */
	public String toAspRepresentation()
	{
		this.aspRepresentation = "";
		this.onshelf.forEach(fig -> {
			this.aspRepresentation += fig.getAspRepresentation() + "\n";
		});
		this.ontable.forEach(fig -> {
			this.aspRepresentation += fig.getAspRepresentation() + "\n";
		});
		return this.aspRepresentation;
	}
	
	/**
	 * return the figures on the table
	 * @return
	 */
	public List<RummikubFigure> getTableFigures()
	{
		return this.ontable;
	}
	
	/**
	 * returns the figures on the shelf
	 * @return
	 */
	public List<RummikubFigure> getShelfFigures()
	{
		return this.onshelf;
	}

	/**
	 * returns the sum of point laid down on the table.
	 * @return
	 */
	public int getSumLaid() {
		return sumLaid;
	}

	/**
	 * The the sum laid, this sum is currently computed by the ASP solver after each round
	 * @param sumLaid
	 */
	public void setSumLaid(int sumLaid) {
		this.sumLaid = sumLaid;
	}

	/**
	 * Returns the number of rounds played
	 * @return
	 */
	public int getRoundNr() {
		return roundNr;
	}
	
	/**
	 * The number of round is normally increased after each round played and set in all GameStates
	 * @param roundNr
	 */
	public void setRoundNr(int roundNr)
	{
		this.roundNr=roundNr;
	}
	
	/**
	 * a reset function, sets the roundnumber and sumlaid back to zero and clears the table and shelf list
	 */
	public void initialize()
	{
		this.sumLaid = 0;
		this.roundNr = 0;
		this.onshelf.clear();
		this.ontable.clear();
	}
	
}
