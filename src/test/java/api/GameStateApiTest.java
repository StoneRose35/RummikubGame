package api;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class GameStateApiTest {

	
	@Test
	public void validateTest()
	{
		GameStateApi gsApi=new GameStateApi();
		List<List<RummikubFigureApi>> tf = new ArrayList<List<RummikubFigureApi>>();
		RummikubFigureApi rf;
		
		List<RummikubFigureApi> lf= new ArrayList<RummikubFigureApi>();
		rf=new RummikubFigureApi();
		rf.setColor(RummikubColorApi.fromCode(3));
		rf.setInstance(1);
		rf.setNumber(1);
		lf.add(rf);
		rf=new RummikubFigureApi();
		rf.setColor(RummikubColorApi.fromCode(3));
		rf.setInstance(1);
		rf.setNumber(2);
		lf.add(rf);
		rf=new RummikubFigureApi();
		rf.setColor(RummikubColorApi.fromCode(3));
		rf.setInstance(2);
		rf.setNumber(3);
		lf.add(rf);
		tf.add(lf);
		
		gsApi.setTableFigures(tf);
		gsApi.validate();
		Assert.assertTrue(gsApi.isAccepted());
	}
	
	@Test
	public void validateTestCollection()
	{
		GameStateApi gsApi=new GameStateApi();
		List<List<RummikubFigureApi>> tf = new ArrayList<List<RummikubFigureApi>>();
		RummikubFigureApi rf;
		
		List<RummikubFigureApi> lf= new ArrayList<RummikubFigureApi>();
		rf=new RummikubFigureApi();
		rf.setColor(RummikubColorApi.fromCode(1));
		rf.setInstance(1);
		rf.setNumber(11);
		lf.add(rf);
		rf=new RummikubFigureApi();
		rf.setColor(RummikubColorApi.fromCode(2));
		rf.setInstance(1);
		rf.setNumber(11);
		lf.add(rf);
		rf=new RummikubFigureApi();
		rf.setColor(RummikubColorApi.fromCode(3));
		rf.setInstance(2);
		rf.setNumber(11);
		lf.add(rf);
		tf.add(lf);
		
		gsApi.setTableFigures(tf);
		gsApi.validate();
		Assert.assertTrue(gsApi.isAccepted());
	}
}
