package ch.sr35.rummikub.common;

public enum RummikubPlacement {
	ON_SHELF("onshelf"),
	ON_TABLE("ontable"),
	ON_STACK("onstack");
	
	private final String repr;
	
	private RummikubPlacement(String repr)
	{
		this.repr = repr;
	}
	
	@Override
	public String toString()
	{
		return this.repr;
	}
}
