package ch.sr35.rummikub.web;

public class RummikubToken {
	private ch.sr35.rummikub.web.RummikubGame game;
	private ch.sr35.rummikub.web.RummikubPlayer player;
	private String token;
	
	public ch.sr35.rummikub.web.RummikubGame getGame() {
		return game;
	}
	public void setGame(ch.sr35.rummikub.web.RummikubGame game) {
		this.game = game;
	}
	public ch.sr35.rummikub.web.RummikubPlayer getPlayer() {
		return player;
	}
	public void setPlayer(ch.sr35.rummikub.web.RummikubPlayer player) {
		this.player = player;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
