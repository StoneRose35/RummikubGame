package api;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import game.GameState;
import game.RummikubFigure;

@Service
public class AspRunner extends Thread {
	private RummikubGame game;
	

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
		GameState gsNew = ((RummikubPlayerAsp)game.getActivePlayer()).solve(gs);
		if (gsNew.getSumLaid()==0)
		{
			RummikubFigure df = game.drawFigure();
			gsNew.getShelfFigures().add(df);
		}
		else
		{
			game.setTableFigures(((RummikubPlayerAsp)game.getActivePlayer()).getTableFigures());
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

	public void setGame(RummikubGame game) {
		this.game = game;
	}
}
