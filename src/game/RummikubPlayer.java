package game;

import java.util.List;

import asp.AspSolver;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representation of a player whose brain is an asp solver
 * @author philipp
 *
 */
public class RummikubPlayer {
    private List<RummikubFigure> onShelf;
    private AspSolver solver;
    private String name;
    private boolean isInFirstRound;
    
    public RummikubPlayer()
    {
    	this.onShelf = new ArrayList<RummikubFigure>();
    	this.solver = new AspSolver();
    }
    
    public RummikubPlayer(String strategy)
    {
    	this.onShelf = new ArrayList<RummikubFigure>();
    	this.solver = new AspSolver(strategy);
    }
    
    /**
     * Solves a round. Basically the following happens: The initial state is put into a GameState.
     * Then the state is updated using solve
     * The onShelf is updated according the new state
     * @param onTableBeginning
     * @return
     */
    public RummikubResult solveRound(List<IRummikubFigureBag> onTableBeginning)
    {
    	RummikubResult result = new RummikubResult();
    	GameState stateInitial = new GameState();
    	if (this.isInFirstRound==false)
    	{
    		stateInitial.setRoundNr(1);
    	}
    	else
    	{
    		stateInitial.setRoundNr(0);
    	}
    	onTableBeginning.forEach(el -> {
    		Iterator<RummikubFigure> it = el.iterator();
    		while (it.hasNext())
    		{
    			stateInitial.addFigure(it.next());
    		}
    	});
    	this.onShelf.forEach(el->{stateInitial.addFigure(el);});
    	GameState stateNew = this.solver.solveRound(stateInitial);
    	if (stateNew.getSumLaid()>0)
    	{
    		result.setOnTable(this.solver.getTableDescription());
    		if (this.isInFirstRound == true)
    		{
    			this.isInFirstRound = false;
    		}
    	}
    	else
    	{
    		result.setOnTable(onTableBeginning);
    	}
    	result.setScoreLaid(stateNew.getSumLaid());
    	this.onShelf = stateNew.getShelfFigures();
    	return result;
    }

	public List<RummikubFigure> getOnShelf() {
		return onShelf;
	}

	public void setOnShelf(List<RummikubFigure> onShelf) {
		this.onShelf = onShelf;
	}
	
	/**
	 * Returns the sum of all the figures on the shelf
	 * @return
	 */
	public int getTotalScore()
	{
		return this.onShelf.stream().mapToInt(fig -> fig.getScore()).sum();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
