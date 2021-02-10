package ch.sr35.rummikub.web.dao;

import java.util.List;
import java.util.stream.Collectors;

import ch.sr35.rummikub.web.Game;

public class GameApi {
	
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

	public static GameApi fromRummikubGame(Game game)
	{
		GameApi g = new GameApi();
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
	
	public void declareAsJoined()
	{
		this.state = "joined";
	}


}
