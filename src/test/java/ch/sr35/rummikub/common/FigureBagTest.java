package ch.sr35.rummikub.common;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.exceptions.GameException;

public class FigureBagTest {
	
	@Test
	public void testFullMatch() throws GameException
	{
		Collection rc = new Collection();
		rc.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));
		
		Collection rc2 = new Collection();
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));
		
		Assert.assertEquals(1.0, rc.match(rc2), 0.01); 
	}
	
	
	@Test
	public void testPartialMatch1() throws GameException
	{
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		
		Series rs2 = new Series();
		rs2.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(4,1,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		
		Assert.assertTrue(rs.match(rs2) < 1.0);
		Assert.assertTrue(rs.match(rs2) > 0.0);
	}
	
	@Test
	public void testPartialMatch2() throws GameException
	{
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		
		Collection rc2 = new Collection();
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));
		
		Assert.assertTrue(rs.match(rc2) < 1.0);
		Assert.assertTrue(rs.match(rc2) > 0.0);
	}
	
	@Test
	public void testMatchInvariance() throws GameException
	{
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(2,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		
		Collection rc2 = new Collection();
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc2.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));
		
		Assert.assertEquals(rs.match(rc2),rc2.match(rs),0.01);
	}

}
