package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class RummikubSeriesTest {

	@Test
	void setupCorrectSeries() throws RummikubGameException {
		RummikubSeries rs = new RummikubSeries();
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(4,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(3,1,1)."));
		Assert.assertTrue(rs.isValid());
	}
	
	@Test
	void tooSmallTest() throws RummikubGameException {
		RummikubSeries rs = new RummikubSeries();
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(2,1,1)."));
		Assert.assertTrue(!rs.isValid());
	}
	
	@Test
	void setupWrongSeries() throws RummikubGameException {
		RummikubSeries rs = new RummikubSeries();
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(5,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(3,1,1)."));
		Assert.assertTrue(!rs.isValid());
	}
	
	@Test
	void addWrongColor() {
		RummikubSeries rs = new RummikubSeries();
		try {
			rs.addFigure(RummikubFigure.getRummikubFigure("ontable(1,1,1)."));
			rs.addFigure(RummikubFigure.getRummikubFigure("ontable(2,2,1)."));
			fail("Expected a RummikubGameException");
		} catch (RummikubGameException e) {

		} 
	}
	
	@Test
	void addTheSame() {
		RummikubSeries rs = new RummikubSeries();
		try {
			rs.addFigure(RummikubFigure.getRummikubFigure("ontable(1,1,1)."));
			rs.addFigure(RummikubFigure.getRummikubFigure("ontable(2,1,1)."));
			rs.addFigure(RummikubFigure.getRummikubFigure("ontable(2,1,2)."));
			fail("Expected a RummikubGameException");
		} catch (RummikubGameException e) {

		} 
	}

}
