package game;

public class RummikubColorFactory {
	
	public static RummikubColor getByCode(int code) throws RummikubException
	{
		switch(code)
		{
		case 1:
			return RummikubColor.BLACK;
		case 2:
			return RummikubColor.RED;
		case 3:
			return RummikubColor.YELLOW;
		case 4:
			return RummikubColor.BLUE;
		default:
			throw new RummikubException(String.format("%d is an invalid color code, valid codes are in the range 1-4", code));
		}
	}

}
