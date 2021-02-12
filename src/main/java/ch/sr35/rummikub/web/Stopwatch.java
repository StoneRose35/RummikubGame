package ch.sr35.rummikub.web;

import ch.sr35.rummikub.common.exceptions.ApiException;

public class Stopwatch extends Thread {
	
	private Game game;
	private int maxRoundTime;
	private boolean aborted=false;
	private double relativeTime=0;
	
	public void run()
	{
		int cnt=0;
		while (cnt < this.maxRoundTime && !this.aborted)
		{
			this.relativeTime = (double)cnt/(double)(this.maxRoundTime-1);
			this.game.getActivePlayer().setTimeRemaining((1.0-this.relativeTime)*100.0);
			if(this.game.getWsController() != null)
			{
				this.game.getWsController().updatePlayers(game);
			}
			try {
				Thread.sleep(500, 0);
			} catch (InterruptedException e) {}
			cnt++;
		}	
		if (!this.aborted)
		{
			try {
				this.game.getActivePlayer().addFigure(this.game.drawFigure());
				if(this.game.getWsController() != null)
				{
					this.game.getWsController().updateShelfFigures(this.game.getActivePlayer(),this.game);
				}
			} catch (ApiException e) {}
			this.game.rotatePlayer();
		}
	}
	
	public Stopwatch clone()
	{
		Stopwatch sw = new Stopwatch();
		sw.setMaxRoundTime(this.getMaxRoundTime());
		sw.setGame(this.getGame());
		return sw;
	}

	public int getMaxRoundTime() {
		return maxRoundTime;
	}

	public void setMaxRoundTime(int maxRoundTime) {
		this.maxRoundTime = maxRoundTime;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean isAborted() {
		return aborted;
	}

	public void Abort() {
		this.aborted = true;
	}

	public double getRelativeTime() {
		return relativeTime;
	}

}
