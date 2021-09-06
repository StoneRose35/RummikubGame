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
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		Assert.assertTrue(responseMock.getCookie().getName().equals("RKToken"));
		Assert.assertTrue(responseMock.getCookie().getValue().length()==20);
		
		NewGameResponse r = controller.generateGame("Testgame",0,0, responseMock.getCookie().getValue());
		Assert.assertNotNull(r.getMessage());
		Assert.assertNull(r.getError());
		Assert.assertTrue(r.getGame().getPlayers().get(0).contentEquals("Tester"));

	}
	
	@Test
	public void wrongGameRegistration()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		Response resp;
		controller.generateGame("Testgame",0,0,responseMock.getCookie().getValue());	
		resp = controller.registerPlayer("Game 2", responseMock.getCookie().getValue());
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void tooManyPlayers()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		Response resp;
		NewGameResponse ngr = controller.generateGame("Testgame",0,0,responseMock.getCookie().getValue());
		controller.addPlayer("Tester1", responseMock);
		resp = controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		controller.addPlayer("Tester2", responseMock);
		resp = controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		controller.addPlayer("Tester3", responseMock);
		resp = controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		controller.addPlayer("TesterTooMuch", responseMock);
		resp = controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void drawTest()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		NewGameResponse ngr = controller.generateGame("Testgame",0,0,responseMock.getCookie().getValue());
		String cookieValue = responseMock.getCookie().getValue();
		Assert.assertTrue(controller.data.getTokens().stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
		controller.addPlayer("Tester2", responseMock);
		controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
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
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		controller.generateGame("Testgame",0,0,responseMock.getCookie().getValue());
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
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		NewGameResponse ngr = controller.generateGame("Testgame",0,0,responseMock.getCookie().getValue());
		controller.addPlayer("Testplayer", responseMock);
		controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		controller.disposeGame(responseMock.getCookie().getValue());

		PlayerResponse pr = controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		Assert.assertNotNull(pr.getError());

	}
	
	@Test
	public void AiPlayerGenerationTest()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		controller.generateGame("Testgame",3,0,responseMock.getCookie().getValue());
		Assert.assertEquals(3,controller.data.getGames().get(0).getPlayers().stream().filter(p -> {return p instanceof PlayerAsp;}).count());
	}
	
	private String setupGame()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.addPlayer("Tester", responseMock);
		NewGameResponse ngr = controller.generateGame("Testgame",0,0,responseMock.getCookie().getValue());
		controller.addPlayer("Tester2", responseMock);
		controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		controller.addPlayer("Tester3", responseMock);
		controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
		controller.addPlayer("Tester4", responseMock);
		controller.registerPlayer(ngr.getGameId(), responseMock.getCookie().getValue());
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
