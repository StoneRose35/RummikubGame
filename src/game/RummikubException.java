package game;

public class RummikubException extends Exception {

	private String message = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = 7362505088384104234L;
	
	public RummikubException(String msg)
	{
		this.message = msg;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
