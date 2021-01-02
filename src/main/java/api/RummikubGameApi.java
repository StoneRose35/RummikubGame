package api;

import java.util.List;
import java.util.stream.Collectors;

public class RummikubGameApi {
	
	private String name;
	private List<String> players;
	private String state;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public static RummikubGameApi fromRummikubGame(RummikubGame game)
	{
		RummikubGameApi g = new RummikubGameApi();
		g.setName(game.getName());
		g.setPlayers(game.getPlayers().stream().map(p -> p.getName()).collect(Collectors.toList()));
		if (game.getFinished()==true)
		{
			g.state="finished";
		}
		else if (game.getStarted()==true)
		{
			g.state="started";
		}
		else
		{
			g.state="initial";
		}
		return g;
	}
	
	public String getState()
	{
		return this.state;
	}


}
