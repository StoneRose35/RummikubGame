package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

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


}
