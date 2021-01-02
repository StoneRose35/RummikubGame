package api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import game.IRummikubFigureBag;
import game.RummikubFigure;
import game.Stack;

public class RummikubGame {
	
	private List<RummikubPlayer> players;
	private List<IRummikubFigureBag> tableFigures;
	private boolean drawn=false;
	private Date created;
	private Date lastAccessed;
	
	
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
		this.lastAccessed = new Date();
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
		this.created = new Date();
	}
	
	public RummikubFigure drawFigure()
	{
		this.drawn=true;
		this.lastAccessed = new Date();
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
		if (this.getStarted()==true)
		{
			throw new RummikubApiException("Game " + this.getName() + " already started");
		}
		RummikubPlayer p = new RummikubPlayer();
		List<RummikubFigure> f = p.getFigures();

		for (int c=0;c<14;c++)
		{
			f.add(this.stack.drawFromStack());
		}		
		p.setName(name);
		p.setActive(this.players.stream().filter(pl -> !(pl instanceof RummikubPlayerAsp)).count()==0);
		this.players.add(p);
		return p;
	}
	
	public void addPlayer(RummikubPlayerAsp rp) throws RummikubApiException
	{
		if (this.players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null)!=null)
		{
			throw new RummikubApiException("Player " + rp.getName() + " already exists");
		}
		if (this.players.size()>=4)
		{
			throw new RummikubApiException("Game " + this.getName() + " Full");
		}
		if (this.getStarted()==true)
		{
			throw new RummikubApiException("Game " + this.getName() + " already started");
		}
		
		List<RummikubFigure> f = rp.getFigures();

		for (int c=0;c<14;c++)
		{
			f.add(this.stack.drawFromStack());
		}	
		rp.setActive(false);
		this.players.add(rp);
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
		this.lastAccessed = new Date();
	}
	
	public boolean getStarted()
	{
		return !this.tableFigures.isEmpty() || this.drawn; 
	}
	
	public boolean getFinished()
	{
		return this.players.stream().anyMatch(p -> p.getFinalScore()!=null);
	}

	public Date getCreated() {
		return created;
	}

	public Date getLastAccessed() {
		return lastAccessed;
	}
}
