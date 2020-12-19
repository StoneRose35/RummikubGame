package game;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import game.RummikubFigure;
import game.RummikubPlacement;
import game.RummikubColor;

class RummikubFigureTest {

	@Test
	void testRummikubFigureString() throws RummikubException {
		RummikubFigure rf = RummikubFigure.getRummikubFigure("onshelf(1,2,1).");
		Assert.assertNotNull(rf);
		Assert.assertEquals(1,rf.getNumber());
		Assert.assertEquals(RummikubColor.RED, rf.getColor());
		Assert.assertEquals(RummikubPlacement.ON_SHELF, rf.getPlacement());
	}
	
	@Test
	void testRummikubFigureStringWoDot() throws RummikubException {
		RummikubFigure rf = RummikubFigure.getRummikubFigure("onshelf(1,2,1)");
		Assert.assertNotNull(rf);
		Assert.assertEquals(1,rf.getNumber());
		Assert.assertEquals(RummikubColor.RED, rf.getColor());
		Assert.assertEquals(RummikubPlacement.ON_SHELF, rf.getPlacement());
	}
	
	@Test
	void testRummikubFigureWrong() throws RummikubException {
		RummikubFigure rf = RummikubFigure.getRummikubFigure("test(4,5,2)");
		Assert.assertNull(rf);
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
	
	@Test
	void sortingTest()
	{
		RummikubFigure rf = RummikubFigure.getRummikubFigure("ontable(1,1,1).");
		RummikubFigure rf1 = RummikubFigure.getRummikubFigure("ontable(4,1,1).");
		RummikubFigure rf2 = RummikubFigure.getRummikubFigure("ontable(12,1,1).");
		RummikubFigure rf5 = RummikubFigure.getRummikubFigure("ontable(4,2,1).");
		RummikubFigure rf3 = RummikubFigure.getRummikubFigure("ontable(10,1,1).");
		RummikubFigure rf4 = RummikubFigure.getRummikubFigure("ontable(1,2,1).");
		RummikubFigure rf6 = RummikubFigure.getRummikubFigure("ontable(4,3,2).");
		List<RummikubFigure> lrf= new ArrayList<RummikubFigure>();
		lrf.add(rf);
		lrf.add(rf1);
		lrf.add(rf2);
		lrf.add(rf5);
		lrf.add(rf3);
		lrf.add(rf4);
		lrf.add(rf6);
		System.out.println("Unsorted List");
		lrf.forEach(el -> {
			System.out.println(el);
		});
		lrf.sort(null);
		System.out.println("Sorted List");
		lrf.forEach(el -> {
			System.out.println(el);
		});
		Assert.assertTrue(lrf.get(3).getColor()==RummikubColor.BLACK);
		Assert.assertTrue(lrf.get(3).getNumber()==12);
	}

}
