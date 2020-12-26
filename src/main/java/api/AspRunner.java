package api;

import java.util.stream.Collectors;

import game.GameState;
import game.RummikubFigure;

public class AspRunner extends Thread {
	private RummikubGame game;
	
	public void run()
	{
		GameState gs = new GameState();
		gs.setOnshelf(game.getActivePlayer().getFigures());
		gs.setOntable(game.getTableFigures().stream().flatMap(l -> l.stream()).collect(Collectors.toList()));
		gs.setRoundNr(game.getRound());
		GameState gsNew = ((RummikubPlayerAsp)game.getActivePlayer()).solve(gs);
		if (gsNew.getSumLaid()==0)
		{
			RummikubFigure df = game.drawFigure();
			gsNew.getShelfFigures().add(df);
		}
		GameStateApi gsApiNew = new GameStateApi();
		gsApiNew.setGameId(game.getName());
		gsApiNew.setShelfFigures(gsNew.getShelfFigures().stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
		gsApiNew.setTableFigures(((RummikubPlayerAsp)game.getActivePlayer())
				.getTableFigures()
				.stream()
				.map(l -> l.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()))
				.collect(Collectors.toList()));
		game.setTableFigures(gsApiNew.getTableFiguresStructured());
		game.getActivePlayer().setFigures(gsNew.getShelfFigures());
		game.rotatePlayer();
	}

	public void setGame(RummikubGame game) {
		this.game = game;
	}
}
