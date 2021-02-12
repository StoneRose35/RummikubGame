package ch.sr35.rummikub.web.responses;
import ch.sr35.rummikub.web.dao.PlayerApi;

public class PlayerResponse extends Response {
	private PlayerApi player;
	private String token;
	private String gameName;
	private String gameId;

	public PlayerApi getPlayer() {
		return player;
	}

	public void setPlayer(PlayerApi player) {
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

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
