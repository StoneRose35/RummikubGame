package ch.sr35.rummikub.web;

public class Token {
	private ch.sr35.rummikub.web.Game game;
	private ch.sr35.rummikub.web.Player player;
	private String token;
	
	public ch.sr35.rummikub.web.Game getGame() {
		return game;
	}
	public void setGame(ch.sr35.rummikub.web.Game game) {
		this.game = game;
	}
	public ch.sr35.rummikub.web.Player getPlayer() {
		return player;
	}
	public void setPlayer(ch.sr35.rummikub.web.Player player) {
		this.player = player;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
