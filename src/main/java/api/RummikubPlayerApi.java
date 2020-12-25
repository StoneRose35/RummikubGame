package api;

import java.util.List;

import game.RummikubFigure;

public class RummikubPlayerApi {
	
	private String name;
	private boolean active;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	public RummikubPlayerApi()
	{
		
	}
	
	public RummikubPlayerApi(RummikubPlayer p)
	{
		this.setActive(p.isActive());
		this.setName(p.getName());
	}


	

}
