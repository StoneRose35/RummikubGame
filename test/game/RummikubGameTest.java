package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class RummikubGameTest {

	@Test
	void gameInitSmoketest() {
		Game g = new Game();
		g.initializeGame();
	}
	
	@Test
	void drawCountTest()
	{
		Game g = new Game();
		g.initializeGame();
		int cnt = 0;
		while ((g.drawFromStack()) != null)
		{
			cnt++;
		}
		Assert.assertEquals(106, cnt);
	}

}
