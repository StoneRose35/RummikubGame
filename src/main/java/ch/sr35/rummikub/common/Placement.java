package ch.sr35.rummikub.common;

public enum Placement {
	ON_SHELF("onshelf"),
	ON_TABLE("ontable"),
	ON_STACK("onstack");
	
	private final String repr;
	
	private Placement(String repr)
	{
		this.repr = repr;
	}
	
	@Override
	public String toString()
	{
		return this.repr;
	}
}
