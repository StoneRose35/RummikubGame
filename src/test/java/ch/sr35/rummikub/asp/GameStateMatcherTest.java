package ch.sr35.rummikub.asp;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ch.sr35.rummikub.common.Collection;
import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.IFigureBag;
import ch.sr35.rummikub.common.Series;
import ch.sr35.rummikub.common.exceptions.GameException;


public class GameStateMatcherTest {
	
	@Test
	public void smokeTest() throws GameException {
		List<IFigureBag> listOld = new ArrayList<IFigureBag>();
		List<IFigureBag> listNew = new ArrayList<IFigureBag>();
		
		Collection rc = new Collection();
		rc.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));
		listOld.add(rc);
		listNew.add(rc);
		
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(6,1,2)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(7,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(8,1,1)."));
		listOld.add(rs);
		
		Series rs3 = new Series();
		rs3.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(6,1,2)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(7,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(8,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(9,1,1)."));
		listNew.add(rs3);
		
		Series rs2 = new Series();
		rs2.addFigure(Figure.getRummikubFigure("ontable(9,3,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(10,3,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(11,3,1)."));
		listNew.add(rs2);
		

		List<IFigureBag> listSorted = GameStateMatcher.match(listOld, listNew);
		
		Assert.assertNotNull(listSorted);
	}
	
	
	@Test
	public void reorderingTest() throws GameException {
		List<IFigureBag> listOld = new ArrayList<IFigureBag>();
		List<IFigureBag> listNew = new ArrayList<IFigureBag>();
		
		Collection rc = new Collection();
		rc.addFigure(Figure.getRummikubFigure("ontable(6,1,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,2,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,4,1)."));
		rc.addFigure(Figure.getRummikubFigure("ontable(6,3,1)."));

		
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(6,1,2)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(7,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(8,1,1)."));

		
		
		Series rs2 = new Series();
		rs2.addFigure(Figure.getRummikubFigure("ontable(9,3,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(10,3,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(11,3,1)."));

		
		listOld.add(rc);
		listOld.add(rs);
		listOld.add(rs2);
		
		listNew.add(rs2);
		listNew.add(rc);
		listNew.add(rs);
		

		List<IFigureBag> listSorted = GameStateMatcher.match(listOld, listNew);
		for(int c=0;c<listOld.size();c++)
		{
			if (listOld.get(c)!=listSorted.get(c))
			{
				Assert.fail();
			}
		}
		
	}
	
	@Test
	public void reductionTest() throws GameException {
		List<IFigureBag> listOld = new ArrayList<IFigureBag>();
		List<IFigureBag> listNew = new ArrayList<IFigureBag>();
		
		Series rs = new Series();
		rs.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(4,1,2)."));
		rs.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		
		Series rs4 = new Series();
		rs4.addFigure(Figure.getRummikubFigure("ontable(6,1,2)."));
		rs4.addFigure(Figure.getRummikubFigure("ontable(7,1,1)."));
		rs4.addFigure(Figure.getRummikubFigure("ontable(8,1,1)."));
		
		Series rs2 = new Series();
		rs2.addFigure(Figure.getRummikubFigure("ontable(10,1,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(11,1,1)."));
		rs2.addFigure(Figure.getRummikubFigure("ontable(12,1,1)."));
		
		
		
		Series rs3 = new Series();
		rs3.addFigure(Figure.getRummikubFigure("ontable(3,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(4,1,2)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(5,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(6,1,2)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(7,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(8,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(9,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(10,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(11,1,1)."));
		rs3.addFigure(Figure.getRummikubFigure("ontable(12,1,1)."));
		
		Collection c1 = new Collection();
		c1.addFigure(Figure.getRummikubFigure("ontable(1,1,1)."));
		c1.addFigure(Figure.getRummikubFigure("ontable(1,2,1)."));
		c1.addFigure(Figure.getRummikubFigure("ontable(1,4,1)."));
		c1.addFigure(Figure.getRummikubFigure("ontable(1,3,1)."));

		
		listOld.add(rs);
		listOld.add(rs2);
		listOld.add(rs4);
		
		listNew.add(c1);
		listNew.add(rs3);
		

		List<IFigureBag> listSorted = GameStateMatcher.match(listOld, listNew);
		
		Assert.assertTrue(listSorted.get(0) instanceof Series);
		
		
	}

}
