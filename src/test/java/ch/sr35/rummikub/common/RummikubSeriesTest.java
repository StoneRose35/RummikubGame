package ch.sr35.rummikub.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.exceptions.RummikubGameException;


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
	void setupWrongSeries2() throws RummikubGameException {
		RummikubSeries rs = new RummikubSeries();
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(9,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(10,1,1)."));
		rs.addFigure(RummikubFigure.getRummikubFigure("ontable(12,1,1)."));
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
	
	@Test
	void listContainsSeriesTest()
	{
		List<IRummikubFigureBag> list = new ArrayList<IRummikubFigureBag>();
		IRummikubFigureBag rs = new RummikubSeries();
		rs.setHash(13);
		list.add(rs);
		rs = new RummikubSeries();
		rs.setHash(14);
		Assert.assertTrue(!list.contains(rs));
		list.add(rs);
		rs = new RummikubSeries();
		rs.setHash(13);
		Assert.assertTrue(list.contains(rs));
	}

}
