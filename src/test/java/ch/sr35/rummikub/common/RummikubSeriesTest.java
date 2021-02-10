package ch.sr35.rummikub.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.exceptions.GameException;


class RummikubSeriesTest {

	@Test
	void setupCorrectSeries() throws GameException {
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(4,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		Assert.assertTrue(rs.isValid());
	}
	
	@Test
	void tooSmallTest() throws GameException {
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		Assert.assertTrue(!rs.isValid());
	}
	
	@Test
	void setupWrongSeries() throws GameException {
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		Assert.assertTrue(!rs.isValid());
	}
	
	@Test
	void setupWrongSeries2() throws GameException {
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(9,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(10,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(12,1,1)."));
		Assert.assertTrue(!rs.isValid());
	}
	
	
	@Test
	void addWrongColor() {
		Series rs = new Series();
		try {
			rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
			rs.addFigure(Figure.getRummikubFigure("ontable(2,2,1)."));
			fail("Expected a RummikubGameException");
		} catch (GameException e) {

		} 
	}
	
	@Test
	void addTheSame() {
		Series rs = new Series();
		try {
			rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
			rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
			rs.addFigure(Figure.getRummikubFigure("ontable(2,1,2)."));
			fail("Expected a RummikubGameException");
		} catch (GameException e) {

		} 
	}
	
	@Test
	void listContainsSeriesTest()
	{
		List<IFigureBag> list = new ArrayList<IFigureBag>();
		IFigureBag rs = new Series();
		rs.setHash(13);
		list.add(rs);
		rs = new Series();
		rs.setHash(14);
		Assert.assertTrue(!list.contains(rs));
		list.add(rs);
		rs = new Series();
		rs.setHash(13);
		Assert.assertTrue(list.contains(rs));
	}

}
