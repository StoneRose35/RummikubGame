package ch.sr35.rummikub.web;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.sr35.rummikub.common.GameState;
import ch.sr35.rummikub.common.Figure;

@Service
public class AspRunner extends Thread {
	private Game game;
	

	WebsocketController wsController;
	
	public AspRunner(WebsocketController wsController)
	{
		this.wsController = wsController;
	}
	
	public void run()
	{
		GameState gs = new GameState();
		gs.setOnshelf(game.getActivePlayer().getFigures());
		gs.setOntable(game.getTableFigures().stream().flatMap(l -> l.stream()).collect(Collectors.toList()));
		gs.setRoundNr(game.getActivePlayer().getRoundNr());
		GameState gsNew = ((PlayerAsp)game.getActivePlayer()).solve(gs);
		if (gsNew.getSumLaid()==0)
		{
			Figure df = game.drawFigure();
			gsNew.getShelfFigures().add(df);
		}
		else
		{
			game.setTableFigures(((PlayerAsp)game.getActivePlayer()).getTableFigures());
			game.getActivePlayer().setRoundNr(game.getActivePlayer().getRoundNr()+1);
			if (gsNew.getShelfFigures().size()==0)
			{
				// player has legibly placed all the figures on the Table, s*he wins!
				// set final scores for all the players
				game.getActivePlayer().setFigures(gsNew.getShelfFigures());
				game.getPlayers().forEach(p -> {
					p.setFinalScore(p.getFigures().stream().mapToInt(f -> f.getScore()).sum());
				});
				
			}
		}
		game.getActivePlayer().setFigures(gsNew.getShelfFigures());
		try {Thread.sleep(1234);} catch (InterruptedException e) {}
		game.rotatePlayer();
		wsController.updatePlayers(game);
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
