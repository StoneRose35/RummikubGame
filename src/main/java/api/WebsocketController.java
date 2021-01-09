package api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebsocketController {
	
	  private final SimpMessagingTemplate simpMessagingTemplate;
	  
	  
	  @Autowired
	  RummikubGameData data;

	  
	  WebsocketController(SimpMessagingTemplate simpMessagingTemplate) {
	        this.simpMessagingTemplate = simpMessagingTemplate;
	    }
	  
	  @MessageMapping("/getplayers")
	  @SendTo("/topic/players")
	  public List<RummikubPlayerApi> updatePlayers(@Payload String msg)
	  {
		  RummikubToken token = data.getTokens().stream().filter(t -> t.getToken().equals(msg.replace("RKToken=", ""))).findFirst().orElse(null);
		  if (token != null)
		  {
			  return token.getGame().getPlayers().stream().map(ps -> new RummikubPlayerApi(ps)).collect(Collectors.toList());
		  }
		  return null;
	  }
	  
	  
	  public void updatePlayers2(List<RummikubPlayerApi> players)
	  {
		  simpMessagingTemplate.convertAndSend("/topic/players",
		            players);
	  }

}
