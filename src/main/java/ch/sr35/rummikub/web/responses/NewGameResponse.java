package ch.sr35.rummikub.web.responses;

public class NewGameResponse extends Response {
	private String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
