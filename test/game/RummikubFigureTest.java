package game;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import game.RummikubFigure;
import game.RummikubPlacement;
import game.RummikubColor;

class RummikubFigureTest {

	@Test
	void testRummikubFigureString() throws RummikubException {
		RummikubFigure rf = new RummikubFigure("onshelf(1,2,1).");
		Assert.assertNotNull(rf);
		Assert.assertEquals(1,rf.getNumber());
		Assert.assertEquals(RummikubColor.RED, rf.getColor());
		Assert.assertEquals(RummikubPlacement.ON_SHELF, rf.getPlacement());
	}

	@Test
	void testGetAspRepresentation() throws RummikubException {
		RummikubFigure rf = new RummikubFigure();
		rf.setColor(RummikubColor.BLUE);
		rf.setInstance(1);
		rf.setNumber(4);
		rf.setPlacement(RummikubPlacement.ON_TABLE);
		Assert.assertEquals("ontable(4,4,1).",rf.getAspRepresentation());
	}
	
	@Test
	void equalityTest() throws RummikubException {
		RummikubFigure rf = new RummikubFigure();
		rf.setColor(RummikubColor.YELLOW);
		rf.setInstance(1);
		rf.setNumber(7);
		rf.setPlacement(RummikubPlacement.ON_TABLE);
		
		RummikubFigure rf2 = new RummikubFigure();
		rf2.setColor(RummikubColor.YELLOW);
		rf2.setInstance(1);
		rf2.setNumber(11);
		rf2.setPlacement(RummikubPlacement.ON_TABLE);
		
		RummikubFigure rf3 = new RummikubFigure();
		rf3.setColor(RummikubColor.YELLOW);
		rf3.setInstance(1);
		rf3.setNumber(11);
		rf3.setPlacement(RummikubPlacement.ON_TABLE);
		
		Assert.assertFalse(rf.equals(rf2));
		Assert.assertTrue(rf2.equals(rf3));
	}

}
