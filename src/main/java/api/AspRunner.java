package api;

import java.util.stream.Collectors;

import game.GameState;

public class AspRunner extends Thread {
	private RummikubGame game;
	
	public void run()
	{
		GameState gs = new GameState();
		gs.setOnshelf(game.getActivePlayer().getFigures());
		gs.setOntable(game.getTableFigures().stream().flatMap(l -> l.stream()).collect(Collectors.toList()));
		gs.setRoundNr(game.getRound());
		GameState gsNew = ((RummikubPlayerAsp)game.getActivePlayer()).solve(gs);
		//GameStateApi gsApi = new GameStateApi();
		//gsApi.setGameId(game.getName());
		//gsApi.setShelfFigures(gs.getOnshelf());
		//gsApi.setTableFigures(game.getTableFigures().stream().map(l -> l.stream().collect(Collectors.toList())).collect(Collectors.toList()));
		GameStateApi gsApiNew = new GameStateApi();
		gsApiNew.setGameId(game.getName());
		gsApiNew.setShelfFigures(gsNew.getShelfFigures().parallelStream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
		gsApiNew.setTableFigures(((RummikubPlayerAsp)game.getActivePlayer())
				.getTableFigures()
				.stream()
				.map(l -> l.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()))
				.collect(Collectors.toList()));
		game.setTableFigures(gsApiNew.getTableFiguresStructured());
		game.getPlayer(gsApiNew.getPlayer().getName()).setFigures(gsNew.getShelfFigures());
		game.rotatePlayer();
	}

	public void setGame(RummikubGame game) {
		this.game = game;
	}
}
