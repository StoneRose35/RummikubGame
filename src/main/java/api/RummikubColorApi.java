package api;

public class RummikubColorApi {
	private int[] rgb;
	private String name;
	private int code;
	
	
	public int[] getRgb() {
		return rgb;
	}
	public void setRgb(int[] rgb) {
		this.rgb = rgb;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public static RummikubColorApi fromCode(int code)
	{
		RummikubColorApi retval=null;
		switch(code)
		{
		case 1:
			retval = new RummikubColorApi();
			retval.setCode(1);
			retval.setName("black");
			retval.setRgb(new int[] {0,0,0});
			return retval;
		case 2:
			retval = new RummikubColorApi();
			retval.setCode(2);
			retval.setName("red");
			retval.setRgb(new int[] {255,0,0});
			return retval;
		case 3:
			retval = new RummikubColorApi();
			retval.setCode(3);
			retval.setName("yellow");
			retval.setRgb(new int[] {255,255,0});
			return retval;
		case 4:
			retval = new RummikubColorApi();
			retval.setCode(3);
			retval.setName("blue");
			retval.setRgb(new int[] {0,0,255});
			return retval;
		default:
			return retval;
		}
	}
	
	
}
