package api;

import game.GameState;
import game.IRummikubFigureBag;
import game.RummikubCollection;
import game.RummikubFigure;
import game.RummikubGameException;
import game.RummikubPlacement;
import game.RummikubSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameStateApi {
	
	private List<IRummikubFigureBag> tableFiguresStructured;
	private List<List<RummikubFigureApi>> tableFigures;
	private List<RummikubFigureApi> shelfFigures;
	private boolean accepted;
	private int roundNr;
	private RummikubPlayerApi player;
	private String gameId;
	
	public List<IRummikubFigureBag> getTableFiguresStructured() {
		return tableFiguresStructured;
	}

	public List<List<RummikubFigureApi>> getTableFigures() {
		return tableFigures;
	}
	public void setTableFigures(List<List<RummikubFigureApi>> tableFigures) {
		this.tableFigures = tableFigures;
	}
	public List<RummikubFigureApi> getShelfFigures() {
		return shelfFigures;
	}
	public void setShelfFigures(List<RummikubFigureApi> shelfFigures) {
		this.shelfFigures = shelfFigures;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public int getRoundNr() {
		return roundNr;
	}
	public void setRoundNr(int roundNr) {
		this.roundNr = roundNr;
	}

	
	public GameStateApi()
	{
		this.tableFiguresStructured = new ArrayList<IRummikubFigureBag>();
	}
	
	public GameState toGameState()
	{
		GameState gs = new GameState();
		gs.setOntable(this.tableFigures.stream().flatMap(lf -> lf.stream().map(el -> el.toRummikubFigure(RummikubPlacement.ON_TABLE))).collect(Collectors.toList()));
		return gs;
	}
	
	public void validate()
	{
		boolean acc=true;
		for (List<RummikubFigure> lf : this.tableFigures.stream()
										.map(tl -> tl.stream()
												.map(el -> el.toRummikubFigure(RummikubPlacement.ON_TABLE))
												.collect(Collectors.toList()))
										.collect(Collectors.toList()))
		{
			boolean acc_coll=true;
			boolean acc_figure=true;
			RummikubCollection rc= new RummikubCollection();
			for (RummikubFigure el : lf)
			{
				try {
					rc.addFigure(el);
				} catch (RummikubGameException e) {
					acc_coll=false;
				}
				if(el.isValid()==false)
				{
					acc_figure=false;
				}
			}
			if (acc_coll==true)
			{
				acc_coll = rc.isValid();
			}
			
			if (acc_coll==true)
			{
				this.tableFiguresStructured.add(rc);
			}
			
			boolean acc_series=true;
			RummikubSeries rs= new RummikubSeries();
			for (RummikubFigure el : lf)
			{
				try {
					rs.addFigure(el);
				} catch (RummikubGameException e) {
					acc_series=false;
				}
			}
			if (acc_series==true)
			{
				acc_series = rs.isValid();
			}
			if (acc_series==true)
			{
				this.tableFiguresStructured.add(rs);
			}

			if (!(acc_coll || acc_series))
			{
				acc= (acc_coll || acc_series) && acc_figure;
				break;
			}
			else if (!acc_figure)
			{
				acc = acc_figure;
				break;
			}
		}
		
		this.accepted=acc;
	}
	public RummikubPlayerApi getPlayer() {
		return player;
	}
	public void setPlayer(RummikubPlayerApi player) {
		this.player = player;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
