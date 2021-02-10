package ch.sr35.rummikub.app;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.Stack;

class RummikubGameTest {

	@Test
	void gameInitSmoketest() {
		Stack g = new Stack();
		g.fill();
	}
	
	@Test
	void drawCountTest()
	{
		Stack g = new Stack();
		g.fill();
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
