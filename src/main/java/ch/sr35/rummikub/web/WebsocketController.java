package ch.sr35.rummikub.web;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ch.sr35.rummikub.web.dao.FigureApi;
import ch.sr35.rummikub.web.dao.GameApi;
import ch.sr35.rummikub.web.dao.PlayerApi;

@Controller
public class WebsocketController {
	
	  private final SimpMessagingTemplate simpMessagingTemplate;
	  
	  
	  @Autowired
	  GameData data;

	  
	  WebsocketController(SimpMessagingTemplate simpMessagingTemplate) {
	        this.simpMessagingTemplate = simpMessagingTemplate;
	    }
	  
	  @MessageMapping("/getplayers")
	  public void watchPlayers(@Payload String msg)
	  {
		  Token token = data.getTokens().stream().filter(t -> t.getToken().equals(msg.replace("RKToken=", ""))).findFirst().orElse(null);
		  if (token != null)
		  {
			  updatePlayers(token.getGame());
		  }
	  }
	  
	  
	  public void updatePlayers(Game g)
	  {
		  
		  simpMessagingTemplate.convertAndSend("/topic/game" + g.getGameId() + "/players",
				  g.getPlayers().stream().map(ps -> new PlayerApi(ps)).collect(Collectors.toList()));
	  }
	  
	  public void updateGames()
	  {
		  simpMessagingTemplate.convertAndSend("/topic/games", data.getGames().stream().map(g -> GameApi.fromRummikubGame(g)).collect(Collectors.toList()));
	  }
	  
	  public void updateShelfFigures(Player activePlayer, Game currentGame)
	  {
		  Token token = this.data.getTokens().stream().filter(t -> t.getPlayer().getName().equals(activePlayer.getName()) && t.getGame().getGameId().equals(currentGame.getGameId())).findFirst().orElse(null);
		  simpMessagingTemplate.convertAndSend("/topic/player" + token.getToken() + "/common", token.getPlayer().getFigures().stream().map(f -> FigureApi.fromRummikubFigure(f)).collect(Collectors.toList()));
	  }

}
