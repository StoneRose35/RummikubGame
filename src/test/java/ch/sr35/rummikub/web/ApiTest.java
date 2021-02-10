package ch.sr35.rummikub.web;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.sr35.rummikub.web.PlayerResponse;
import ch.sr35.rummikub.web.Response;
import ch.sr35.rummikub.web.RummikubController;
import ch.sr35.rummikub.web.RummikubGame;
import ch.sr35.rummikub.web.RummikubPlayerAsp;
import ch.sr35.rummikub.web.RummikubToken;
import ch.sr35.rummikub.web.dao.RummikubFigureApi;
import ch.sr35.rummikub.web.dao.RummikubPlayerApi;

import java.util.List;


@SpringBootTest
public class ApiTest {
	
	@Autowired
	RummikubController controller;
	
	String gameId;
	
	@Test
	public void instantiationTest()
	{
		Assert.assertNotNull(controller);
	}
	
	@Test
	public void successfulRegistration()
	{
		Response r = controller.generateGame("Testgame",0);
		Assert.assertNotNull(r.getMessage());
		Assert.assertNull(r.getError());
		ServletResponseMock responseMock = new ServletResponseMock();
		Response resp = controller.registerPlayer("Tester", "Testgame", responseMock);
		Assert.assertTrue(responseMock.getCookie().getName().equals("RKToken"));
		Assert.assertTrue(responseMock.getCookie().getValue().length()==20);
	}
	
	@Test
	public void wrongGameRegistration()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		Response resp;
		controller.generateGame("Testgame",0);
		resp = controller.registerPlayer("Tester", "Game 2", responseMock);
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void tooManyPlayers()
	{
		Response resp;
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.generateGame("Testgame",0);
		resp = controller.registerPlayer("Tester", "Testgame", responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("Tester 2", "Testgame", responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("Tester 3", "Testgame", responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("Tester 4", "Testgame", responseMock);
		Assert.assertNull(resp.getError());
		Assert.assertNotNull(resp.getMessage());
		resp = controller.registerPlayer("TesterTooMuch", "Testgame", responseMock);
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void drawTest()
	{
		Response resp;
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.generateGame("Testgame",0);
		resp = controller.registerPlayer("Tester", "Testgame", responseMock);
		String cookieValue = responseMock.getCookie().getValue();
		Assert.assertTrue(controller.data.getTokens().stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
		controller.registerPlayer("Tester2", "Testgame", responseMock);
		RummikubFigureApi f = controller.getFigure(cookieValue);
		Assert.assertNotNull(f);
		Assert.assertFalse(controller.data.getTokens().stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
	}
	
	@Test
	public void getPlayersTest()
	{
		String token = this.setupGame();
		List<RummikubPlayerApi> players = this.controller.getPlayers(token);
		
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
		controller.generateGame("Testgame",0);
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.registerPlayer("Testplayer", "Testgame", responseMock);
		RummikubPlayerAsp aiPlayer = new RummikubPlayerAsp();
		RummikubToken rToken = new RummikubToken();
		RummikubGame g = controller.data.getGames().get(0);
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
		controller.generateGame("Testgame",0);
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.registerPlayer("Testplayer", "Testgame", responseMock);
		controller.disposeGame(responseMock.getCookie().getValue());

		PlayerResponse pr = controller.registerPlayer("Testplayer", "Testgame", responseMock);
		Assert.assertNotNull(pr.getError());

	}
	
	@Test
	public void AiPlayerGenerationTest()
	{
		controller.generateGame("Testgame",3);
		Assert.assertEquals(3,controller.data.getGames().get(0).getPlayers().stream().filter(p -> {return p instanceof RummikubPlayerAsp;}).count());
	}
	
	private String setupGame()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.generateGame("Testgame",0);
		controller.registerPlayer("Tester", "Testgame", responseMock);
		controller.registerPlayer("Tester2", "Testgame", responseMock);
		controller.registerPlayer("Tester3", "Testgame", responseMock);
		controller.registerPlayer("Tester4", "Testgame", responseMock);
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
