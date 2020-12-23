package api;

import java.util.ArrayList;
import java.util.List;

import game.IRummikubFigureBag;
import game.RummikubFigure;
import game.Stack;

public class RummikubGame {
	
	private List<RummikubPlayer> players;
	private int round=0;
	
	public List<RummikubPlayer> getPlayers() {
		return players;
	}
	
	public RummikubPlayer getPlayer(String playerName) 
	{
		return this.players.stream().filter( p -> p.getName().equals(playerName)).findFirst().orElse(null);
	}

	public void setPlayers(List<RummikubPlayer> players) {
		this.players = players;
	}

	private List<IRummikubFigureBag> tableFigures;
	public List<IRummikubFigureBag> getTableFigures() {
		return tableFigures;
	}

	public void setTableFigures(List<IRummikubFigureBag> tableFigures) {
		this.tableFigures = tableFigures;
	}

	private Stack stack;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RummikubGame()
	{
		this.players = new ArrayList<RummikubPlayer>();
		this.tableFigures=new ArrayList<IRummikubFigureBag>();
		this.stack=new Stack();
		this.stack.fill();
	}
	
	public RummikubFigure drawFigures()
	{
		return this.stack.drawFromStack();
	}
	
	public void addPlayer(String name) throws RummikubApiException 
	{
		if (this.players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null)!=null)
		{
			throw new RummikubApiException("Player " + name + " already exists");
		}
		RummikubPlayer p = new RummikubPlayer();
		List<RummikubFigure> f = p.getFigures();
		for (int c=0;c<14;c++)
		{
			f.add(this.drawFigures());
		}
		p.setName(name);
		this.players.add(p);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof RummikubGame)
		{
			return ((RummikubGame) other).getName() == this.getName();
		}
		return false;
	}
	
	public void increaseRound()
	{
		this.round++;
	}

	public int getRound() {
		return round;
	}

}
