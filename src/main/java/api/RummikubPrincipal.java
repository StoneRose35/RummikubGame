package api;

import java.security.Principal;

public class RummikubPrincipal implements Principal {
	
	private String token;
	
	public RummikubPrincipal(String token)
	{
		this.token = token;
	}

	@Override
	public String getName() {
		return this.token;
	}

}
