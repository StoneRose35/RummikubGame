package game;

import gui.RummikubProgram;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class RummikubGameTest {

	@Test
	void gameInitSmoketest() {
		Stack g = new Stack();
		g.initializeGame();
	}
	
	@Test
	void drawCountTest()
	{
		Stack g = new Stack();
		g.initializeGame();
		int cnt = 0;
		while ((g.drawFromStack()) != null)
		{
			cnt++;
		}
		Assert.assertEquals(106, cnt);
	}
	
	@Test
	void readConfigTest() throws FileNotFoundException
	{
		RummikubProgram rg = new RummikubProgram();
		rg.initFromConfig();
		
	}

}
