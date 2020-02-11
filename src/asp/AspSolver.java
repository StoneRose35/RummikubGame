package asp;

import game.*;
import org.json.JSONArray; 
import org.json.JSONObject; 

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class AspSolver {
	
	private String jsonresult;

	public String getJsonresult() {
		return jsonresult;
	}

	public void setJsonresult(String jsonresult) {
		this.jsonresult = jsonresult;
	}

	public AspSolver()
	{
	}
	
	public GameState solve_round(GameState state_old)
	{
		Runtime rt=Runtime.getRuntime();
		File clingoinput=new File("game.lp");
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
			    proc = rt.exec("clingo --outf=2 game.lp ASP/rummikub.lp ASP/first_round_rule.lp ");
			}
			else
			{
				proc = rt.exec("clingo --outf=2 game.lp ASP/rummikub.lp ");
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
			if (!this.getSolverResult().equals("UNSATISFIABLE"))
			{
				GameState state = this.jsonToGamestate();
				if (state.getSumLaid()>0)
				{
					state.setRoundNr(state_old.getRoundNr()+1);
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
		values.forEach(val -> {
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
		});
		return result;
	}
	
	public List<IRummikubFigureBag> getTableDescription()
	{
		JSONArray values = this.getLastWitnessValues();
		List<IRummikubFigureBag> result = new ArrayList<IRummikubFigureBag>();
		values.forEach(val -> {
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
				long hash = a.get(0) + a.get(2)*17;
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
		});
		
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
		JSONArray witnesses = ((JSONArray)((JSONArray)((JSONObject)((JSONArray)jobj.get("Call")).get(0))
				.get("Witnesses")));
		JSONObject witness = (JSONObject)witnesses.get(witnesses.length()-1);
		JSONArray values = ((JSONArray)witness.get("Value"));
		
		return values;
	}
}
