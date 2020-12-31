package api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import game.GameState;
import game.IRummikubFigureBag;
import game.RummikubFigure;
import game.Stack;

public class RummikubGame {
	
	private List<RummikubPlayer> players;
	private List<IRummikubFigureBag> tableFigures;
	private boolean started=false;
	
	
	public List<RummikubPlayer> getPlayers() {
		return players;
	}
	
	public RummikubPlayer getPlayer(String playerName) 
	{
		return this.players.stream().filter( p -> p.getName().equals(playerName)).findFirst().orElse(null);
	}
	
	public RummikubPlayer getActivePlayer() 
	{
		return this.players.stream().filter(p -> p.isActive()==true).findFirst().orElseThrow();
	}

	public void setPlayers(List<RummikubPlayer> players) {
		this.players = players;
	}


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
	
	public RummikubFigure drawFigure()
	{
		return this.stack.drawFromStack();
	}
	
	public RummikubPlayer addPlayer(String name) throws RummikubApiException 
	{
		if (this.players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null)!=null)
		{
			throw new RummikubApiException("Player " + name + " already exists");
		}
		if (this.players.size()>=4)
		{
			throw new RummikubApiException("Game " + this.getName() + " Full");
		}
		if (this.started==true)
		{
			throw new RummikubApiException("Game " + this.getName() + " already started");
		}
		RummikubPlayer p = new RummikubPlayer();
		List<RummikubFigure> f = p.getFigures();
		for (int c=0;c<14;c++)
		{
			f.add(this.drawFigure());
		}
		p.setName(name);
		p.setActive(this.players.size()==0);
		this.players.add(p);
		return p;
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
	
	
	public void rotatePlayer()
	{
		int idx = 0;
		this.started=true;
		for (RummikubPlayer p : this.players)
		{
			if (p.isActive()==true)
			{
				p.setActive(false);
				break;
			}
			idx++;
		}

		idx = (idx + 1) % this.players.size();

		RummikubPlayer currentPlayer = this.players.get(idx);
		currentPlayer.setActive(true);
		if (currentPlayer instanceof RummikubPlayerAsp)
		{
			AspRunner aspRunner = new AspRunner();
			aspRunner.setGame(this);
			aspRunner.start();
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

}
