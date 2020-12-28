package api;

import java.util.List;

public class AspPlayerDispatcher extends Thread {
	
	private List<api.RummikubGame> games;
	
	public void run()
	{
		while (true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			for (RummikubGame g : games)
			{
				if (!(g.getPlayers().stream().filter(p -> p instanceof RummikubPlayerAsp).findFirst().isPresent()) && g.getRound()==0 && g.getPlayers().isEmpty()==false)
					// game hasn't started and the is not Asp Player yet
				{
					RummikubPlayerAsp aiPlayer = new RummikubPlayerAsp();
					aiPlayer.setActive(false);
					aiPlayer.setName("Clingon42");
					for (int c=0;c<14;c++)
					{
						aiPlayer.getFigures().add(g.drawFigure());
					}
					g.getPlayers().add(aiPlayer);
				}
			}
		}
	}

	public List<api.RummikubGame> getGames() {
		return games;
	}

	public void setGames(List<api.RummikubGame> games) {
		this.games = games;
	}

}
