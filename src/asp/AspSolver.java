package asp;

import game.*;
import org.json.JSONArray; 
import org.json.JSONObject; 

import java.io.*;

public class AspSolver {
	

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
			Process proc = rt.exec("clingo --outf=2 game.lp ASP/rummikub.lp ");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine())!=null)
			{
				System.out.println(line);
				jsoncontent += line;
			}
			br.close();
			isr.close();
			is.close();
			
			return this.jsonToGamestate(jsoncontent);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (RummikubException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public GameState jsonToGamestate(String jsoncontent) throws RummikubException
	{
		JSONObject jobj = new JSONObject(jsoncontent);
		JSONArray witnesses = ((JSONArray)((JSONArray)((JSONObject)((JSONArray)jobj.get("Call")).get(0))
				.get("Witnesses")));
		JSONObject witness = (JSONObject)witnesses.get(witnesses.length()-1);
		JSONArray values = ((JSONArray)witness.get("Value"));
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
}
