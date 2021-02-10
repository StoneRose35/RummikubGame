package ch.sr35.rummikub.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.exceptions.GameException;


class RummikubCollectionTest {


	@Test
	void setupCorrectSeries() throws GameException {
		Collection rc = new Collection();
		rc.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));
		Assert.assertTrue(rc.isValid());
	}
	
	@Test
	void tooSmallTest() throws GameException {
		Collection rc = new Collection();
		rc.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		Assert.assertTrue(!rc.isValid());
	}
	
	
	@Test
	void addWrongNumber() {
		Collection rc = new Collection();
		try {
			rc.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
			rc.addFigure(Figure.getRummikubFigure("ontable(2,2,1)."));
			fail("Expected a RummikubGameException");
		} catch (GameException e) {

		} 
	}
	
	@Test
	void addTheSame() {
		Collection rc = new Collection();
		try {
			rc.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
			rc.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
			rc.addFigure(Figure.getRummikubFigure("ontable(6,2,2)."));
			fail("Expected a RummikubGameException");
		} catch (GameException e) {

		} 
	}
	
	@Test
	void listContainsCollectionTest()
	{
		List<IFigureBag> list = new ArrayList<IFigureBag>();
		IFigureBag rc = new Collection();
		rc.setHash(13);
		list.add(rc);
		rc = new Collection();
		rc.setHash(14);
		Assert.assertTrue(!list.contains(rc));
		list.add(rc);
		rc = new Collection();
		rc.setHash(13);
		Assert.assertTrue(list.contains(rc));
	}
	
	@Test
	void listTypeDifferentiation()
	{
		List<IFigureBag> list = new ArrayList<IFigureBag>();
		IFigureBag rc = new Collection();
		rc.setHash(13);
		list.add(rc);
		rc = new Series();
		rc.setHash(13);
		Assert.assertTrue(!list.contains(rc));
	}


}
