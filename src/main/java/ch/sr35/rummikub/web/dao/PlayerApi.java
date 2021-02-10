package ch.sr35.rummikub.web.dao;

import ch.sr35.rummikub.web.Player;

public class PlayerApi {
	
	private String name;
	private boolean active;
	private int roundNr;
	private Integer finalScore;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	public PlayerApi()
	{
		this.finalScore=null;
	}
	

	public int getRoundNr() {
		return roundNr;
	}

	public void setRoundNr(int roundNr) {
		this.roundNr = roundNr;
	}

	public Integer getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Integer finalScore) {
		this.finalScore = finalScore;
	}

	public PlayerApi(Player p)
	{
		this.setActive(p.isActive());
		this.setName(p.getName());
		this.setRoundNr(p.getRoundNr());
		this.setFinalScore(p.getFinalScore());
	}
	

}
