package game;

public class RummikubGameException extends RummikubException {
	/**
	 * Exception Class for non-technical Exceptions occurring during the game, i.e. not following game rule
	 * , "cheating"
	 */

	private int severity;
	public RummikubGameException(String msg) {
		super(msg);
		this.severity = 1;
	}
	
	public RummikubGameException(String msg, int severity) {
		super(msg);
		this.severity = severity;
	}
	
	
	public int getSeverity() {
		return severity;
	}


}
