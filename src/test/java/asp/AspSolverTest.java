package asp;


import org.junit.Assert;

import asp.AspSolver;
import game.*;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class AspSolverTest {
	
	private final String exampleJson = "{\n" + 
			"  \"Solver\": \"clingo version 5.4.0\",\n" + 
			"  \"Input\": [\n" + 
			"    \"game1.lp\",\"rummikub.lp\"\n" + 
			"  ],\n" + 
			"  \"Call\": [\n" + 
			"    {\n" + 
			"      \"Witnesses\": [\n" + 
			"        {\n" + 
			"          \"Value\": [\n" + 
			"            \"ontable(1,2,1)\", \"ontable(2,2,1)\", \"ontable(3,2,1)\", \"ontable(3,3,1)\", \"ontable(3,1,1)\", \"ontable(3,4,1)\", \"ontable(1)\", \"ontable(4,2,1)\", \"ontable(3,2,3)\", \"series(1,4,1,2,1,1)\", \"series(1,4,2,2,1,1)\", \"series(1,4,3,2,1,1)\", \"series(1,4,4,2,1,1)\", \"collection(3,3,1,1)\", \"collection(3,1,1,1)\", \"collection(3,4,1,1)\", \"collection(3,2,3,1)\", \"sum_on_table(4)\", \"remaining(7,3,1)\"\n" + 
			"          ],\n" + 
			"          \"Costs\": [\n" + 
			"            1, 0, -4, -3\n" + 
			"          ]\n" + 
			"        },\n" + 
			"        {\n" + 
			"          \"Value\": [\n" + 
			"            \"ontable(1,2,1)\", \"ontable(2,2,1)\", \"ontable(3,2,1)\", \"ontable(3,3,1)\", \"ontable(3,1,1)\", \"ontable(3,4,1)\", \"ontable(1)\", \"ontable(4,2,1)\", \"ontable(5,2,3)\", \"series(1,5,1,2,1,1)\", \"series(1,5,2,2,1,1)\", \"series(1,5,3,2,1,1)\", \"series(1,5,4,2,1,1)\", \"series(1,5,5,2,3,1)\", \"collection(3,3,1,2)\", \"collection(3,1,1,2)\", \"collection(3,4,1,2)\", \"sum_on_table(4)\", \"remaining(7,3,1)\"\n" + 
			"          ],\n" + 
			"          \"Costs\": [\n" + 
			"            1, 0, -4, -5\n" + 
			"          ]\n" + 
			"        }\n" + 
			"      ]\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"Result\": \"OPTIMUM FOUND\",\n" + 
			"  \"Models\": {\n" + 
			"    \"Number\": 2,\n" + 
			"    \"More\": \"no\",\n" + 
			"    \"Optimum\": \"yes\",\n" + 
			"    \"Optimal\": 1,\n" + 
			"    \"Costs\": [\n" + 
			"      1, 0, -4, -5\n" + 
			"    ]\n" + 
			"  },\n" + 
			"  \"Calls\": 1,\n" + 
			"  \"Time\": {\n" + 
			"    \"Total\": 0.582,\n" + 
			"    \"Solve\": 0.126,\n" + 
			"    \"Model\": 0.013,\n" + 
			"    \"Unsat\": 0.008,\n" + 
			"    \"CPU\": 0.582\n" + 
			"  }\n" + 
			"}\n";


	@Test
	void gamestateGenerationTest() throws RummikubException
	{
		AspSolver as = new AspSolver();
		as.setJsonresult(this.exampleJson);
	
		GameState gs = as.jsonToGamestate();
		Assert.assertTrue(gs != null);
		Assert.assertTrue(gs.getTableFigures().size() > 0);
		Assert.assertTrue(gs.getShelfFigures().size() > 0);
		Assert.assertTrue(gs.getSumLaid() == 4);
	}
	
	@Test
	void solve_round_test() throws RummikubException
	{
		AspSolver as = new AspSolver();
		as.setJsonresult(this.exampleJson);
		
		GameState state_old = as.jsonToGamestate();
		state_old.setRoundNr(1);
		GameState state_new = as.solveRound(state_old);
		Assert.assertNotNull(state_new);
		List<RummikubFigure>  tf = state_new.getTableFigures();
		Assert.assertNotNull(tf);
		Assert.assertEquals(8, tf.size());
	}
	

	@Test
	void getTableDescriptionTest() 
	{
		AspSolver as = new AspSolver();
		as.setJsonresult(this.exampleJson);
		List<IRummikubFigureBag> td = as.getTableDescription();
		Assert.assertNotNull(td);
		Assert.assertEquals(2, td.size());
		
	}
	
	@Test 
	void mappingTest()
	{
		AspSolver as = new AspSolver();
		as.setJsonresult(this.exampleJson);
		List<IRummikubFigureBag> td = as.getTableDescription();
		List<RummikubFigure> arr = td.stream().flatMap(e -> e.stream()).collect(Collectors.toList());
		Assert.assertTrue(arr.size()==8);
	}
}

