package ch.sr35.rummikub.web;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.sr35.rummikub.web.responses.NewGameResponse;
import ch.sr35.rummikub.web.responses.Response;

@SpringBootTest
public class StopwatchTest {
	
	@Autowired
	RestController controller;
	
	@Test
	public void stopWatchInstantiationTest()
	{
		Stopwatch sw = new Stopwatch();
		Assert.assertNotNull(sw);
	}
	
	@Test
	public void GameStartTest()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		NewGameResponse ngr = controller.generateGame("Testgame", 0, 4);
		controller.registerPlayer("Tester0", ngr.getGameId(), responseMock);
		String token0 = responseMock.getCookie().getValue();
		controller.registerPlayer("Tester1", ngr.getGameId(), responseMock);
		String token1 = responseMock.getCookie().getValue();
		controller.registerPlayer("Tester2", ngr.getGameId(), responseMock);
		String token2 = responseMock.getCookie().getValue();
		controller.registerPlayer("Tester3", ngr.getGameId(), responseMock);
		String token3 = responseMock.getCookie().getValue();
		Assert.assertFalse( controller.getGames(token0).get(0).isStarted());
		Assert.assertFalse( controller.getGames(token1).get(0).isStarted());
		Assert.assertFalse( controller.getGames(token2).get(0).isStarted());
		Assert.assertFalse( controller.getGames(token3).get(0).isStarted());
		
		Response r = controller.setReady(token2);
		Assert.assertNull(r.getError());
		Assert.assertFalse( controller.getGames(token0).get(0).isStarted());
		Assert.assertFalse( controller.getGames(token1).get(0).isStarted());
		Assert.assertFalse( controller.getGames(token2).get(0).isStarted());
		Assert.assertFalse( controller.getGames(token3).get(0).isStarted());
		
		r = controller.setReady(token0);
		r = controller.setReady(token1);
		r = controller.setReady(token3);
		Assert.assertTrue(controller.getGames(token2).get(0).isStarted());
		try {
			Thread.sleep(2100, 0);
		} catch (InterruptedException e) {}
		// next player is active
		Assert.assertTrue(controller.getPlayers(token1).stream().filter(p -> p.getName()=="Tester1").anyMatch(p -> p.isActive()));
		
		//first player has drawn a card
		Assert.assertEquals(15, controller.getShelfFigure(token0).size());
		
		
	}
	
	@Test
	public void ReadinessTest()
	{
		ServletResponseMock responseMock = new ServletResponseMock();
		NewGameResponse ngr = controller.generateGame("Testgame", 2, 4);
		Assert.assertFalse(controller.getGames("").get(0).isStarted());
		controller.registerPlayer("Tester0", ngr.getGameId(), responseMock);
		String token0 = responseMock.getCookie().getValue();
		controller.setReady(token0);
		Assert.assertTrue(controller.getGames("").get(0).isStarted());
	}
	
	@BeforeEach
	public void init()
	{
		controller.data.getGames().clear();
		controller.data.getTokens().clear();
	}
	

}
