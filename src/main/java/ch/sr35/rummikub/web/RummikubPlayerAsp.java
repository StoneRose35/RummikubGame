package ch.sr35.rummikub.web;

import java.util.List;

import ch.sr35.rummikub.asp.AspSolver;
import ch.sr35.rummikub.common.GameState;
import ch.sr35.rummikub.common.IRummikubFigureBag;

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
