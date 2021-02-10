package ch.sr35.rummikub.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.sr35.rummikub.common.IFigureBag;
import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.Stack;
import ch.sr35.rummikub.common.exceptions.ApiException;

public class Game {
	
	private List<Player> players;
	private List<IFigureBag> tableFigures;
	private boolean drawn=false;
	private Date created;
	private Date lastAccessed;
	private WebsocketController wsController;
	
	public Game(WebsocketController wsc)
	{
		this.players = new ArrayList<Player>();
		this.tableFigures=new ArrayList<IFigureBag>();
		this.stack=new Stack();
		this.stack.fill();
		this.created = new Date();
		this.wsController = wsc;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Player getPlayer(String playerName) 
	{
		return this.players.stream().filter( p -> p.getName().equals(playerName)).findFirst().orElse(null);
	}
	
	public Player getActivePlayer() 
	{
		return this.players.stream().filter(p -> p.isActive()==true).findFirst().orElseThrow();
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}


	public List<IFigureBag> getTableFigures() {
		return tableFigures;
	}

	public void setTableFigures(List<IFigureBag> tableFigures) {
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

	
	public Figure drawFigure()
	{
		this.drawn=true;
		this.lastAccessed = new Date();
		return this.stack.drawFromStack();
	}
	
	public Player addPlayer(String name) throws ApiException 
	{
		if (this.players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null)!=null)
		{
			throw new ApiException("Player " + name + " already exists");
		}
		if (this.players.size()>=4)
		{
			throw new ApiException("Game " + this.getName() + " Full");
		}
		if (this.getStarted()==true)
		{
			throw new ApiException("Game " + this.getName() + " already started");
		}
		Player p = new Player();

		for (int c=0;c<14;c++)
		{
			p.addFigure(this.stack.drawFromStack());

		}		
		p.setName(name);
		p.setActive(this.players.stream().filter(pl -> !(pl instanceof PlayerAsp)).count()==0);
		this.players.add(p);
		return p;
	}
	
	public void addPlayer(PlayerAsp rp) throws ApiException
	{
		if (this.players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null)!=null)
		{
			throw new ApiException("Player " + rp.getName() + " already exists");
		}
		if (this.players.size()>=4)
		{
			throw new ApiException("Game " + this.getName() + " Full");
		}
		if (this.getStarted()==true)
		{
			throw new ApiException("Game " + this.getName() + " already started");
		}
		

		for (int c=0;c<14;c++)
		{
			Figure rf = this.stack.drawFromStack();
			rp.addFigure(rf);
		}	
		rp.setActive(false);
		this.players.add(rp);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Game)
		{
			return ((Game) other).getName() == this.getName();
		}
		return false;
	}
	
	
	public void rotatePlayer()
	{
		int idx = 0;
		for (Player p : this.players)
		{
			if (p.isActive()==true)
			{
				p.setActive(false);
				break;
			}
			idx++;
		}

		idx = (idx + 1) % this.players.size();

		Player currentPlayer = this.players.get(idx);
		currentPlayer.setActive(true);
		if (currentPlayer instanceof PlayerAsp)
		{
			AspRunner aspRunner = new AspRunner(this.wsController);
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
