package ch.sr35.rummikub.web;

import java.util.List;

import ch.sr35.rummikub.asp.AspSolver;
import ch.sr35.rummikub.common.GameState;
import ch.sr35.rummikub.common.IFigureBag;

public class PlayerAsp extends Player {
	
	private AspSolver solver = new AspSolver();
	
	public PlayerAsp()
	{
		this.setReady(true);
	}
	
	public GameState solve(GameState stateOld)
	{
		return solver.solveRound(stateOld);
	}
	
	public List<IFigureBag> getTableFigures()
	{
		return solver.getTableDescription();
	}

}
