package ch.sr35.rummikub.web.responses;

import ch.sr35.rummikub.web.dao.GameApi;

public class NewGameResponse extends Response {
	private String gameId;
	private GameApi game;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public GameApi getGame() {
		return game;
	}

	public void setGame(GameApi game) {
		this.game = game;
	}
}
