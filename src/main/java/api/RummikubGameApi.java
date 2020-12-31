package api;

import java.util.List;
import java.util.stream.Collectors;

public class RummikubGameApi {
	
	private String name;
	private List<String> players;
	
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
		return g;
	}


}
