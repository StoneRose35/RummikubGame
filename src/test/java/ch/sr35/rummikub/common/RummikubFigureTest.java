package ch.sr35.rummikub.common;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.exceptions.GeneralException;


class RummikubFigureTest {

	@Test
	void testRummikubFigureString() throws GeneralException {
		Figure rf = Figure.getRummikubFigure("onshelf(1,2,1).");
		Assert.assertNotNull(rf);
		Assert.assertEquals(1,rf.getNumber());
		Assert.assertEquals(Color.RED, rf.getColor());
		Assert.assertEquals(Placement.ON_SHELF, rf.getPlacement());
	}
	
	@Test
	void testRummikubFigureStringWoDot() throws GeneralException {
		Figure rf = Figure.getRummikubFigure("onshelf(1,2,1)");
		Assert.assertNotNull(rf);
		Assert.assertEquals(1,rf.getNumber());
		Assert.assertEquals(Color.RED, rf.getColor());
		Assert.assertEquals(Placement.ON_SHELF, rf.getPlacement());
	}
	
	@Test
	void testRummikubFigureWrong() throws GeneralException {
		Figure rf = Figure.getRummikubFigure("test(4,5,2)");
		Assert.assertNull(rf);
	}

	@Test
	void testGetAspRepresentation() throws GeneralException {
		Figure rf = new Figure();
		rf.setColor(Color.BLUE);
		rf.setInstance(1);
		rf.setNumber(4);
		rf.setPlacement(Placement.ON_TABLE);
		Assert.assertEquals("ontable(4,4,1).",rf.getAspRepresentation());
	}
	
	@Test
	void equalityTest() throws GeneralException {
		Figure rf = new Figure();
		rf.setColor(Color.YELLOW);
		rf.setInstance(1);
		rf.setNumber(7);
		rf.setPlacement(Placement.ON_TABLE);
		
		Figure rf2 = new Figure();
		rf2.setColor(Color.YELLOW);
		rf2.setInstance(1);
		rf2.setNumber(11);
		rf2.setPlacement(Placement.ON_TABLE);
		
		Figure rf3 = new Figure();
		rf3.setColor(Color.YELLOW);
		rf3.setInstance(1);
		rf3.setNumber(11);
		rf3.setPlacement(Placement.ON_TABLE);
		
		Assert.assertFalse(rf.equals(rf2));
		Assert.assertTrue(rf2.equals(rf3));
	}
	
	@Test
	void sortingTest()
	{
		Figure rf = Figure.getRummikubFigure("ontable(1,1,1).");
		Figure rf1 = Figure.getRummikubFigure("ontable(4,1,1).");
		Figure rf2 = Figure.getRummikubFigure("ontable(12,1,1).");
		Figure rf5 = Figure.getRummikubFigure("ontable(4,2,1).");
		Figure rf3 = Figure.getRummikubFigure("ontable(10,1,1).");
		Figure rf4 = Figure.getRummikubFigure("ontable(1,2,1).");
		Figure rf6 = Figure.getRummikubFigure("ontable(4,3,2).");
		List<Figure> lrf= new ArrayList<Figure>();
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
		Assert.assertTrue(lrf.get(3).getColor()==Color.BLACK);
		Assert.assertTrue(lrf.get(3).getNumber()==12);
	}
	
	@Test
	void validationTest(){
		Figure rf = new Figure();
		rf.setColor(Color.BLUE);
		rf.setPlacement(Placement.ON_SHELF);
		rf.setPosition(1);
		rf.setShelfNr(0);
		Assert.assertTrue(rf.isValid());
		rf.setPlacement(Placement.ON_STACK);
		Assert.assertFalse(rf.isValid());
		rf.setPosition(null);
		rf.setShelfNr(null);
		Assert.assertTrue(rf.isValid());
		rf.setPlacement(Placement.ON_SHELF);
		rf.setPosition(-1);
		rf.setShelfNr(0);
		Assert.assertFalse(rf.isValid());
	}

}
