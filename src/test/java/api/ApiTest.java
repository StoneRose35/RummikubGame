package api;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import game.RummikubFigure;

import static org.junit.Assert.assertTrue;

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
		Response r = controller.generateGame("Testgame");
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
		controller.generateGame("Testgame");
		resp = controller.registerPlayer("Tester", "Game 2", responseMock);
		Assert.assertNotNull(resp.getError());
		Assert.assertNull(resp.getMessage());
	}
	
	@Test
	public void tooManyPlayers()
	{
		Response resp;
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.generateGame("Testgame");
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
		controller.generateGame("Testgame");
		resp = controller.registerPlayer("Tester", "Testgame", responseMock);
		String cookieValue = responseMock.getCookie().getValue();
		Assert.assertTrue(controller.tokens.stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
		controller.registerPlayer("Tester2", "Testgame", responseMock);
		RummikubFigureApi f = controller.getFigure(cookieValue);
		Assert.assertNotNull(f);
		Assert.assertFalse(controller.tokens.stream().filter(tt -> tt.getToken()==cookieValue).findFirst().orElseThrow().getPlayer().isActive());
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
		Assert.assertTrue(this.controller.tokens.stream().filter(tt -> tt.getToken()==token).findFirst().orElseThrow().getPlayer()
			.getFigures().size()==18);
	}
	
	@Test
	public void AspPlayerTest()
	{
		final String tokenValue = "abcd";
		controller.generateGame("Testgame");
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.registerPlayer("Testplayer", "Testgame", responseMock);
		RummikubPlayerAsp aiPlayer = new RummikubPlayerAsp();
		RummikubToken rToken = new RummikubToken();
		RummikubGame g = controller.games.get(0);
		rToken.setGame(g);
		rToken.setPlayer(aiPlayer);
		rToken.setToken(tokenValue);
		g.getPlayers().add(aiPlayer);
		controller.tokens.add(rToken);
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
		Assert.assertTrue(t1-t0 > 1);
	}
	
	
	
	private String setupGame()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		controller.generateGame("Testgame");
		controller.registerPlayer("Tester", "Testgame", responseMock);
		controller.registerPlayer("Tester2", "Testgame", responseMock);
		controller.registerPlayer("Tester3", "Testgame", responseMock);
		controller.registerPlayer("Tester4", "Testgame", responseMock);
		return responseMock.getCookie().getValue();
		
	}
	
	@BeforeEach
	public void init()
	{
		controller.games.clear();
		controller.tokens.clear();
	}
	
}
