package api;

import java.util.List;

import asp.AspSolver;
import game.GameState;
import game.IRummikubFigureBag;

public class RummikubPlayerAsp extends RummikubPlayer {
	
	private AspSolver solver = new AspSolver();
	
	public GameState solve(GameState stateOld)
	{
		return solver.solveRound(stateOld);
	}
	
	public List<IRummikubFigureBag> getTableFigures()
	{
		return solver.getTableDescription();
	}

}
