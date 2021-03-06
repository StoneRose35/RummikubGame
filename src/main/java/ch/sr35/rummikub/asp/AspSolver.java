package ch.sr35.rummikub.asp;

import org.json.JSONArray; 
import org.json.JSONObject;

import ch.sr35.rummikub.common.GameState;
import ch.sr35.rummikub.common.IFigureBag;
import ch.sr35.rummikub.common.Collection;
import ch.sr35.rummikub.common.ColorFactory;
import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.Placement;
import ch.sr35.rummikub.common.Series;
import ch.sr35.rummikub.common.exceptions.GeneralException;
import ch.sr35.rummikub.web.WebRunner;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represent the ASP solver
 * @author philipp
 *
 */
public class AspSolver {
	
	private static final int READ_BUFFER_DELAY = 100;
	private static String ASP_FOLDER = "ASP";
	private String jsonresult;
	private String strategyFile;
	private String clingo;

	public String getClingo() {
		return clingo;
	}

	public String getJsonresult() {
		return jsonresult;
	}

	public void setJsonresult(String jsonresult) {
		this.jsonresult = jsonresult;
	}

	public AspSolver()
	{
		this.strategyFile = "minim_score.lp";
		if (System.getProperty("os.name")=="Windows")
		{
			this.clingo="clingo.exe";
		}
		else
		{
			this.clingo = "clingo";
		}
	}
	
	public String getVersion()
	{
		Runtime rt=Runtime.getRuntime();
		try {
			Process proc = rt.exec(this.clingo + " --version");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			String version="";
			Thread.sleep(READ_BUFFER_DELAY, 0);
			while ((line = br.readLine())!=null)
			{
				version += line + System.lineSeparator();
			}
			return version;
			
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		}
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
			    proc = rt.exec(String.format(this.clingo + " --outf=2 " +
			    			gname + " " + 
			    			Paths.get("ASP","rummikub.lp") + " " +
			    			Paths.get("ASP", "%s") + " " + 
			    			Paths.get("ASP", "first_round_rule.lp"),this.strategyFile));
			}
			else
			{
				proc = rt.exec(String.format(this.clingo + " --outf=2 " +
								gname + " " + 
								Paths.get("ASP","rummikub.lp") + " " +  
								Paths.get("ASP","%s"),this.strategyFile));
			}
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			Thread.sleep(READ_BUFFER_DELAY, 0);
			while ((line = br.readLine())!=null)
			{
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
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public GameState jsonToGamestate() throws GeneralException
	{
		JSONArray values = this.getLastWitnessValues();
		GameState result = new GameState();
		for (int c=0;c<values.length();c++)
		{
			Object val=values.get(c);
			String strval = (String)val;
			Figure fig = Figure.getRummikubFigure(strval);
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
	
	public List<IFigureBag> getTableDescription()
	{

		List<IFigureBag> result = new ArrayList<IFigureBag>();
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
				IFigureBag rs;
				rs = new Series();
				rs.setHash(hash);
				if (!result.contains(rs))
				{
					result.add(rs);
				}
				else
				{
					rs = result.get(result.indexOf(rs));
				}
				Figure rf = new Figure();
				try {
					rf.setColor(ColorFactory.getByCode(a.get(3)));
					rf.setNumber(a.get(2));
					rf.setInstance(a.get(4));
					rf.setPlacement(Placement.ON_TABLE);
					rs.addFigure(rf);
				} catch (GeneralException e) {
				}
			}
			else if (pred.getName().equals("collection"))
			{
				List<Integer> a = pred.atomsAsIntegers();
				long hash = a.get(0) + a.get(3)*17;
				IFigureBag rs;
				rs = new Collection();
				rs.setHash(hash);
				if (!result.contains(rs))
				{
					result.add(rs);
				}
				else
				{
					rs = result.get(result.indexOf(rs));
				}
				Figure rf = new Figure();
				try {
					rf.setColor(ColorFactory.getByCode(a.get(1)));
					rf.setNumber(a.get(0));
					rf.setInstance(a.get(2));
					rf.setPlacement(Placement.ON_TABLE);
					rs.addFigure(rf);
				} catch (GeneralException e) {
				}
			}
		}
		
		return result;
	}
	
	
	public int deploy() 
	{
		ClassLoader classLoader = getClass().getClassLoader();

	    Path AspFilesPath = Paths.get(ASP_FOLDER);
	    
	    if(Files.exists(AspFilesPath))
	    {
	    	try {
	    	    Files.walk(AspFilesPath)
	    	      .sorted(Comparator.reverseOrder())
	    	      .map(Path::toFile)
	    	      .forEach(File::delete);
			} catch (IOException e) {
			}
	    }
	    

		try {
	    	Files.createDirectory(AspFilesPath);
		    CodeSource src = WebRunner.class.getProtectionDomain().getCodeSource();
		    if (src != null) {
		        URL jar = src.getLocation();
		        String jarpath = jar.getPath();
		        if (jarpath.contains(".jar"))
		        {
			        ZipInputStream zip;
					zip = new ZipInputStream(jar.openStream());
				      while(true) {
					        ZipEntry e = zip.getNextEntry();
					        if (e == null)
					          return 0;
					        String name = e.getName();

					        if (name.startsWith("BOOT-INF/classes/ASP/") && name.endsWith(".lp")) {
					            InputStream resource = classLoader.getResourceAsStream(name);
					            String fname = name.replace("BOOT-INF/classes/ASP/", "");
					            Files.copy(resource, Paths.get(ASP_FOLDER, fname));
				        }	      
					}
		        }
		        else
		        {
		        	Path p = Paths.get(jarpath);
		        	try {
			        	Files.walk(p).forEach(f -> {
			        		String fpath = f.toString();
			        		if (fpath.endsWith(".lp"))
			        		{
				        		Path destFileName = Paths.get(ASP_FOLDER, f.getFileName().toString());
				        		try {
				        			Files.copy(f, destFileName);
				        		}
				        		catch (IOException e)
				        		{
				        			throw new RuntimeException(e);
				        		}
			        		}
			        	});
		        	}
		        	catch (RuntimeException e2)
		        	{
		        		return 1;
		        	}
		        	
		        }
			}
	    } catch (IOException e1) {
				return 1;
			}
		    
	   
	    return 0;
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
