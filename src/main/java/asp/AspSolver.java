package asp;

import game.*;
import org.json.JSONArray; 
import org.json.JSONObject; 

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Represent the ASP solver
 * @author philipp
 *
 */
public class AspSolver {
	
	private String jsonresult;
	private String strategyFile;

	public String getJsonresult() {
		return jsonresult;
	}

	public void setJsonresult(String jsonresult) {
		this.jsonresult = jsonresult;
	}

	public AspSolver()
	{
		this.strategyFile = "minim_score.lp";
	}
	
	public AspSolver(String strategy)
	{
		this.strategyFile = strategy;
	}
	
	/**
	 * solves a round, takes the initial state as an input and outputs the new state, which can be the same as the old one
	 * @param state_old
	 * @return
	 */
	public GameState solveRound(GameState state_old)
	{
		Random r = new Random();
		Runtime rt=Runtime.getRuntime();
		String gname = "game" + r.nextInt(9999-1000)+1000 +  ".lp";
		File clingoinput=new File(gname);
		FileWriter fw;
        String jsoncontent = "";
		try {
			fw = new FileWriter(clingoinput);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(state_old.toAspRepresentation());
			bw.close();
			fw.close();
			Process proc;
			if (state_old.getRoundNr()<1)
			{
			    proc = rt.exec(String.format("clingo --outf=2 " + gname + " ASP/rummikub.lp ASP/%s ASP/first_round_rule.lp ",this.strategyFile));
			}
			else
			{
				proc = rt.exec(String.format("clingo --outf=2 " + gname + " ASP/rummikub.lp  ASP/%s ",this.strategyFile));
			}
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine())!=null)
			{
				System.out.println(line);
				jsoncontent += line;
			}
			this.jsonresult = jsoncontent;
			br.close();
			isr.close();
			is.close();
			clingoinput.delete();
			if (!this.getSolverResult().equals("UNSATISFIABLE"))
			{
				
				GameState state = this.jsonToGamestate();
				if (state.getSumLaid()>0)
				{
					state.setRoundNr(state_old.getRoundNr()+1);
				}
				else
				{
					state.setRoundNr(state_old.getRoundNr());
				}
				return state;
			}
			else
			{
				return state_old;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (RummikubException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public GameState jsonToGamestate() throws RummikubException
	{
		JSONArray values = this.getLastWitnessValues();
		GameState result = new GameState();
		for (int c=0;c<values.length();c++)
		{
			Object val=values.get(c);
			String strval = (String)val;
			RummikubFigure fig = RummikubFigure.getRummikubFigure(strval);
			if (fig != null)
			{
		        result.addFigure(fig);
			}
			else 
			{
				AspPredicate pred = AspHelper.parsePredicate(strval);
				if (pred.getName().equals("sum_on_table"))
				{
					result.setSumLaid(pred.atomsAsIntegers().get(0));
				}
			}
		}
		return result;
	}
	
	public List<IRummikubFigureBag> getTableDescription()
	{

		List<IRummikubFigureBag> result = new ArrayList<IRummikubFigureBag>();
		JSONArray values = this.getLastWitnessValues();
		if (values==null)
		{
			return result;
		}
		for (int c=0;c< values.length();c++) {
			Object val=values.get(c);
			String strval = (String)val;
			AspPredicate pred = AspHelper.parsePredicate(strval);
			if (pred.getName().equals("series"))
			{
				List<Integer> a = pred.atomsAsIntegers();
				long hash = a.get(0) + a.get(1)*13 + a.get(3)*13*13 + a.get(5)*13*13*13;
				IRummikubFigureBag rs;
				rs = new RummikubSeries();
				rs.setHash(hash);
				if (!result.contains(rs))
				{
					result.add(rs);
				}
				else
				{
					rs = result.get(result.indexOf(rs));
				}
				RummikubFigure rf = new RummikubFigure();
				try {
					rf.setColor(RummikubColorFactory.getByCode(a.get(3)));
					rf.setNumber(a.get(2));
					rf.setInstance(a.get(4));
					rf.setPlacement(RummikubPlacement.ON_TABLE);
					rs.addFigure(rf);
				} catch (RummikubException e) {
				}
			}
			else if (pred.getName().equals("collection"))
			{
				List<Integer> a = pred.atomsAsIntegers();
				long hash = a.get(0) + a.get(3)*17;
				IRummikubFigureBag rs;
				rs = new RummikubCollection();
				rs.setHash(hash);
				if (!result.contains(rs))
				{
					result.add(rs);
				}
				else
				{
					rs = result.get(result.indexOf(rs));
				}
				RummikubFigure rf = new RummikubFigure();
				try {
					rf.setColor(RummikubColorFactory.getByCode(a.get(1)));
					rf.setNumber(a.get(0));
					rf.setInstance(a.get(2));
					rf.setPlacement(RummikubPlacement.ON_TABLE);
					rs.addFigure(rf);
				} catch (RummikubException e) {
				}
			}
		}
		
		return result;
	}
	
	private String getSolverResult()
	{
		JSONObject jobj = new JSONObject(this.jsonresult);
		return (String)jobj.get("Result");
	}
	
	private JSONArray getLastWitnessValues()
	{
		JSONObject jobj = new JSONObject(this.jsonresult);
		if (jobj.get("Result").equals("UNSATISFIABLE"))
		{
			return null;
		}
		JSONArray witnesses = ((JSONArray)((JSONArray)((JSONObject)((JSONArray)jobj.get("Call")).get(0))
				.get("Witnesses")));
		JSONObject witness = (JSONObject)witnesses.get(witnesses.length()-1);
		JSONArray values = ((JSONArray)witness.get("Value"));
		
		return values;
	}
}
