package ch.sr35.rummikub.web.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.sr35.rummikub.common.GameState;
import ch.sr35.rummikub.common.IFigureBag;
import ch.sr35.rummikub.common.Collection;
import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.Placement;
import ch.sr35.rummikub.common.Series;
import ch.sr35.rummikub.common.exceptions.GameException;

public class GameStateApi {
	
	private List<IFigureBag> tableFiguresStructured;
	private List<List<FigureApi>> tableFigures;
	private List<FigureApi> shelfFigures;
	private boolean accepted;
	private int roundNr;
	private PlayerApi player;
	private String gameId;
	
	public List<IFigureBag> getTableFiguresStructured() {
		return tableFiguresStructured;
	}

	public List<List<FigureApi>> getTableFigures() {
		return tableFigures;
	}
	public void setTableFigures(List<List<FigureApi>> tableFigures) {
		this.tableFigures = tableFigures;
	}
	public List<FigureApi> getShelfFigures() {
		return shelfFigures;
	}
	public void setShelfFigures(List<FigureApi> shelfFigures) {
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
		this.tableFiguresStructured = new ArrayList<IFigureBag>();
	}
	
	public GameState toGameState()
	{
		GameState gs = new GameState();
		gs.setOntable(this.tableFigures.stream().flatMap(lf -> lf.stream().map(el -> el.toRummikubFigure(Placement.ON_TABLE))).collect(Collectors.toList()));
		return gs;
	}
	
	public void validate()
	{
		boolean acc=true;
		for (List<Figure> lf : this.tableFigures.stream()
										.map(tl -> tl.stream()
												.map(el -> el.toRummikubFigure(Placement.ON_TABLE))
												.collect(Collectors.toList()))
										.collect(Collectors.toList()))
		{
			boolean acc_coll=true;
			boolean acc_figure=true;
			Collection rc= new Collection();
			for (Figure el : lf)
			{
				try {
					rc.addFigure(el);
				} catch (GameException e) {
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
			Series rs= new Series();
			for (Figure el : lf)
			{
				try {
					rs.addFigure(el);
				} catch (GameException e) {
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
	public PlayerApi getPlayer() {
		return player;
	}
	public void setPlayer(PlayerApi player) {
		this.player = player;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
