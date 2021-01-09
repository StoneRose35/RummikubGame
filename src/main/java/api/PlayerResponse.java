package api;
import api.Response;
import api.RummikubPlayerApi;

public class PlayerResponse extends Response {
	private RummikubPlayerApi player;
	private String token;

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
}
