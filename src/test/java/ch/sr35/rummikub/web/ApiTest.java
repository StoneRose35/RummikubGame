package ch.sr35.rummikub.web;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.sr35.rummikub.web.RestController;
import ch.sr35.rummikub.web.Game;
import ch.sr35.rummikub.web.PlayerAsp;
import ch.sr35.rummikub.web.Token;
import ch.sr35.rummikub.web.dao.FigureApi;
import ch.sr35.rummikub.web.dao.PlayerApi;
import ch.sr35.rummikub.web.responses.NewGameResponse;
import ch.sr35.rummikub.web.responses.PlayerResponse;
import ch.sr35.rummikub.web.responses.Response;

import java.util.List;


@SpringBootTest
public class ApiTest {
	
	@Autowired
	RestController controller;
	
	String gameId;
	
	@Test
	public void instantiationTest()
	{
		Assert.assertNotNull(controller);
	}
	
	@Test
	public void successfulRegistration()
	{
		NewGameResponse r = controller.generateGame("Testgame",0,0);
		Assert.assertNotNull(r.getMessage());
		Assert.assertNull(r.getError());
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.registerPlayer("Tester", r.getGameId(), responseMock);
		Assert.assertTrue(responseMock.getCookie().getName().equals("RKToken"));
		Assert.assertTrue(responseMock.getCookie().getValue().length()==20);
	}
	
	@Test
	public void wrongGameRegistration()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		Response resp;
		controller.generateGame("Testgame",0,0);
		resp = controller.registerPlayer("Tester", "Game 2", responseMock);
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void tooManyPlayers()
	{
		Response resp;
		ServletResponseMock responseMock = new ServletResponseMock();
		NewGameResponse ngr = controller.generateGame("Testgame",0,0);
		resp = controller.registerPlayer("Tester", ngr.getGameId(), responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("Tester 2", ngr.getGameId(), responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("Tester 3", ngr.getGameId(), responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("Tester 4", ngr.getGameId(), responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("TesterTooMuch", ngr.getGameId(), responseMock);
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void drawTest()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		NewGameResponse ngr = controller.generateGame("Testgame",0,0);
		controller.registerPlayer("Tester", ngr.getGameId(), responseMock);
		String cookieValue = responseMock.getCookie().getValue();
		Assert.assertTrue(controller.data.getTokens().stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
		controller.registerPlayer("Tester2", ngr.getGameId(), responseMock);
		FigureApi f = controller.getFigure(cookieValue);
		Assert.assertNotNull(f);
		Assert.assertFalse(controller.data.getTokens().stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
	}
	
	@Test
	public void getPlayersTest()
	{
		String token = this.setupGame();
		List<PlayerApi> players = this.controller.getPlayers(token);
		
		for(int c=0;c<4;c++)
		{
			controller.getFigure(token);
		}
		Assert.assertEquals(players.size(),4);
		Assert.assertTrue(this.controller.data.getTokens().stream().filter(tt -> tt.getToken()==token).findFirst().orElseThrow().getPlayer()
			.getFigures().size()==18);
	}
	
	@Test
	public void AspPlayerTest()
	{
		/**
		 * sets up a game, added a human player, add a ai player, the "human" player then draws a figure.
		 * Then it is checked that the ai player is active for at least 100ms
		 */
		final String tokenValue = "abcd";
		NewGameResponse ngr = controller.generateGame("Testgame",0,0);
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.registerPlayer("Testplayer", ngr.getGameId(), responseMock);
		PlayerAsp aiPlayer = new PlayerAsp();
		Token rToken = new Token();
		Game g = controller.data.getGames().get(0);
		rToken.setGame(g);
		rToken.setPlayer(aiPlayer);
		rToken.setToken(tokenValue);
		g.getPlayers().add(aiPlayer);
		controller.data.getTokens().add(rToken);
		for (int c=0;c<14;c++)
		{
			aiPlayer.getFigures().add(g.drawFigure());
		}
		
		controller.getFigure(responseMock.getCookie().getValue());
		Assert.assertTrue(aiPlayer.isActive());
		long t0 = System.currentTimeMillis();
		while (aiPlayer.isActive())
		{
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
		long t1 = System.currentTimeMillis();
		Assert.assertTrue(t1-t0 > 100);
	}
	
	
	@Test
	public void DisposeTest()
	{
		NewGameResponse ngr = controller.generateGame("Testgame",0,0);
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.registerPlayer("Testplayer", ngr.getGameId(), responseMock);
		controller.disposeGame(responseMock.getCookie().getValue());

		PlayerResponse pr = controller.registerPlayer("Testplayer", ngr.getGameId(), responseMock);
		Assert.assertNotNull(pr.getError());

	}
	
	@Test
	public void AiPlayerGenerationTest()
	{
		controller.generateGame("Testgame",3,0);
		Assert.assertEquals(3,controller.data.getGames().get(0).getPlayers().stream().filter(p -> {return p instanceof PlayerAsp;}).count());
	}
	
	private String setupGame()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		NewGameResponse ngr = controller.generateGame("Testgame",0,0);
		controller.registerPlayer("Tester", ngr.getGameId(), responseMock);
		controller.registerPlayer("Tester2", ngr.getGameId(), responseMock);
		controller.registerPlayer("Tester3", ngr.getGameId(), responseMock);
		controller.registerPlayer("Tester4", ngr.getGameId(), responseMock);
		return responseMock.getCookie().getValue();
		
	}
	
	
	@Test
	public void webSocketAvailableTest()
	{
		Assert.assertNotNull(controller.wsController);
	}
	
	@BeforeEach
	public void init()
	{
		controller.data.getGames().clear();
		controller.data.getTokens().clear();
	}
	
}
