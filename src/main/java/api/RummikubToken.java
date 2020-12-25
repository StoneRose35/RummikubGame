package api;

public class RummikubToken {
	private api.RummikubGame game;
	private api.RummikubPlayer player;
	private String token;
	
	public api.RummikubGame getGame() {
		return game;
	}
	public void setGame(api.RummikubGame game) {
		this.game = game;
	}
	public api.RummikubPlayer getPlayer() {
		return player;
	}
	public void setPlayer(api.RummikubPlayer player) {
		this.player = player;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
