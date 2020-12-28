package api;
import api.Response;
import api.RummikubPlayerApi;

public class PlayerResponse extends Response {
	private RummikubPlayerApi player;

	public RummikubPlayerApi getPlayer() {
		return player;
	}

	public void setPlayer(RummikubPlayerApi player) {
		this.player = player;
	}
}
