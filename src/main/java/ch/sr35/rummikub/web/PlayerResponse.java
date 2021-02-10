package ch.sr35.rummikub.web;
import ch.sr35.rummikub.web.Response;
import ch.sr35.rummikub.web.dao.RummikubPlayerApi;

public class PlayerResponse extends Response {
	private RummikubPlayerApi player;
	private String token;
	private String gameName;

	public RummikubPlayerApi getPlayer() {
		return player;
	}

	public void setPlayer(RummikubPlayerApi player) {
		this.player = player;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
