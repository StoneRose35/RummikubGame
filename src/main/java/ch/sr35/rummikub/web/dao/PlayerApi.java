package ch.sr35.rummikub.web.dao;

import ch.sr35.rummikub.web.Player;

public class PlayerApi {
	
	private String name;
	private boolean active;
	private int roundNr;
	private Integer finalScore;
	private double timeRemaining;
	private boolean ready=false;
	private String avatar;
	private int cards;
	
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
		this.timeRemaining=0.0;
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
		this.setTimeRemaining(p.getTimeRemaining());
		this.setReady(p.isReady());
		this.setAvatar(p.getAvatar());
		this.cards = p.getFigures().size();
	}

	public double getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(double timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getCards() {
		return cards;
	}
	

}
