package ch.sr35.rummikub.common.exceptions;

/**
 * An explicit Exception for the application indicating that something is wrong on purpose
 * @author philipp
 *
 */
public class GeneralException extends Exception {

	private String message = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = 7362505088384104234L;
	
	public GeneralException(String msg)
	{
		this.message = msg;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
