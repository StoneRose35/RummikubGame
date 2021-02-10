package ch.sr35.rummikub.web.dao;

public class RummikubColorApi {
	private String rgb;
	private String name;
	private int code;
	
	
	public String getRgb() {
		return rgb;
	}
	public void setRgb(String rgb) {
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
			retval.setRgb("#000000");
			return retval;
		case 2:
			retval = new RummikubColorApi();
			retval.setCode(2);
			retval.setName("red");
			retval.setRgb("#ff0000");
			return retval;
		case 3:
			retval = new RummikubColorApi();
			retval.setCode(3);
			retval.setName("yellow");
			retval.setRgb("#d8d800");
			return retval;
		case 4:
			retval = new RummikubColorApi();
			retval.setCode(4);
			retval.setName("blue");
			retval.setRgb("#0000ff");
			return retval;
		default:
			return retval;
		}
	}
	
	
}
