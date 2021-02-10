package ch.sr35.rummikub.web;

import java.security.Principal;
import java.util.Map;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Service
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	
	private List<RummikubToken> tokens;
	
	
	public CustomHandshakeHandler(RummikubGameData data)
	{
		this.tokens = data.getTokens();
	}
	
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // get user using the cookie sent
    	if (request.getHeaders().get("cookie").get(0).contains("RKToken"))
    	{
    		String[] cookies = request.getHeaders().get("cookie").get(0).split(";");
    		for ( String c: cookies)
    		{
    			if (c.contains("RKToken"))
    			{
    				String tokenName = c.replace("RKToken=", "");
    				if (this.tokens.stream().anyMatch(e -> {return e.getToken().equals(tokenName);}))
    				{
    					return new RummikubPrincipal(tokenName);
    				}
    			}
    		}
    	}
        return null;
    }
}
