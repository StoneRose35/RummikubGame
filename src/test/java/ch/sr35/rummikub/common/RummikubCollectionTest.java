package ch.sr35.rummikub.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.exceptions.RummikubGameException;


class RummikubCollectionTest {


	@Test
	void setupCorrectSeries() throws RummikubGameException {
		RummikubCollection rc = new RummikubCollection();
		rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,2,1)."));
		rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,4,1)."));
		rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,3,1)."));
		Assert.assertTrue(rc.isValid());
	}
	
	@Test
	void tooSmallTest() throws RummikubGameException {
		RummikubCollection rc = new RummikubCollection();
		rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,2,1)."));
		Assert.assertTrue(!rc.isValid());
	}
	
	
	@Test
	void addWrongNumber() {
		RummikubCollection rc = new RummikubCollection();
		try {
			rc.addFigure(RummikubFigure.getRummikubFigure("ontable(1,1,1)."));
			rc.addFigure(RummikubFigure.getRummikubFigure("ontable(2,2,1)."));
			fail("Expected a RummikubGameException");
		} catch (RummikubGameException e) {

		} 
	}
	
	@Test
	void addTheSame() {
		RummikubCollection rc = new RummikubCollection();
		try {
			rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,1,1)."));
			rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,2,1)."));
			rc.addFigure(RummikubFigure.getRummikubFigure("ontable(6,2,2)."));
			fail("Expected a RummikubGameException");
		} catch (RummikubGameException e) {

		} 
	}
	
	@Test
	void listContainsCollectionTest()
	{
		List<IRummikubFigureBag> list = new ArrayList<IRummikubFigureBag>();
		IRummikubFigureBag rc = new RummikubCollection();
		rc.setHash(13);
		list.add(rc);
		rc = new RummikubCollection();
		rc.setHash(14);
		Assert.assertTrue(!list.contains(rc));
		list.add(rc);
		rc = new RummikubCollection();
		rc.setHash(13);
		Assert.assertTrue(list.contains(rc));
	}
	
	@Test
	void listTypeDifferentiation()
	{
		List<IRummikubFigureBag> list = new ArrayList<IRummikubFigureBag>();
		IRummikubFigureBag rc = new RummikubCollection();
		rc.setHash(13);
		list.add(rc);
		rc = new RummikubSeries();
		rc.setHash(13);
		Assert.assertTrue(!list.contains(rc));
	}


}
