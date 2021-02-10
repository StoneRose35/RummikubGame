package ch.sr35.rummikub.common;

import ch.sr35.rummikub.common.exceptions.GeneralException;

/**
 * Factory for generating the RummikubColors based on an integer code
 * @author philipp
 *
 */
public class ColorFactory {
	
	public static Color getByCode(int code) throws GeneralException
	{
		switch(code)
		{
		case 1:
			return Color.BLACK;
		case 2:
			return Color.RED;
		case 3:
			return Color.YELLOW;
		case 4:
			return Color.BLUE;
		default:
			throw new GeneralException(String.format("%d is an invalid color code, valid codes are in the range 1-4", code));
		}
	}

}
