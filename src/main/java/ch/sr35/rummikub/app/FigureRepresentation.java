package ch.sr35.rummikub.app;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import ch.sr35.rummikub.common.IFigureBag;
import ch.sr35.rummikub.common.Figure;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class FigureRepresentation extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6847047541601038545L;
	private final double H_RATIO = 1.35;
	private List<IFigureBag> tableFigures;
	private List<Player> players;
	
	public FigureRepresentation()
	{
		this.tableFigures = new ArrayList<IFigureBag>();
		this.players = new ArrayList<Player>();
	}
	

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		this.updateFigures(g);
		
	}

	
	private void updateFigures(Graphics g)
	{
		IFigureBag figureBag;
		// draw table figures
		g.setColor(new Color(105, 15, 45));
		Dimension d = this.getSize();
		int dy = (int)(d.getWidth()/13.0*this.H_RATIO);

		
		// compute table dimensions and fill table
		int n_places = 0;
		Iterator<IFigureBag> itb = this.tableFigures.iterator();
		while(itb.hasNext())
		{
			figureBag = itb.next();
			n_places += figureBag.getFigureCount()+1;
		}
		n_places =  (int)Math.ceil((double)n_places/13);
		g.fillRect(0, 0, this.getWidth(), n_places*dy);
		
		Coordinates coords = new Coordinates();
		itb = this.tableFigures.iterator();
		while(itb.hasNext())
		{
			figureBag = itb.next();
			Iterator<Figure> it = figureBag.iterator();
			while(it.hasNext())
			{
				this.drawRummikubFigure(it.next(), coords, g);
				coords.increase();
			}
			coords.increase();
		}
		if(coords.getColumn()> 0)
		{
		    coords.nextLine();
		}
		// draw shelf figures
		for (Player p : this.players)
		{
			Iterator<Figure> its = p.getOnShelf().iterator();
			while (its.hasNext())
			{
				this.drawRummikubFigure(its.next(), coords, g);
				coords.increase();
			}
			coords.nextLine();
		}
	}
	
	public List<IFigureBag> getTableFigures() {
		return tableFigures;
	}


	public void setTableFigures(List<IFigureBag> tableFigures) {
		this.tableFigures = tableFigures;
	}
	
	public void setPlayers(List<Player> p)
	{
		this.players = p;
	}
	
	public List<Player> getState()
	{
		return this.players;
	}

	private void drawRummikubFigure(Figure f,Coordinates c, Graphics g)
	{
		drawRummikubFigure(f,c.getRow(),c.getColumn(),g);
	}

	private void drawRummikubFigure(Figure f,int row,int column, Graphics g)
	{
		Dimension d = this.getSize();
		int figureWidth = (int)(d.getWidth()/13.5);
		int figureHeight = (int)(figureWidth*this.H_RATIO);
		int x0 = (int)(column*d.getWidth()/13.0);
		int y0 = (int)(row*d.getWidth()/13.0*this.H_RATIO);
		
		g.setColor(new Color(151, 153, 136));
		g.fillRoundRect(x0,y0,figureWidth, figureHeight, figureWidth/10, figureWidth/10);
		g.setColor(new Color(240, 204, 113));
		g.drawRoundRect(x0,y0,figureWidth, figureHeight, figureWidth/10, figureWidth/10);
		Font nr_font =  new Font(Font.SANS_SERIF,Font.BOLD,figureHeight/4);
		g.setFont(nr_font);
		if (f.getInstance()<3)
		{
			g.setColor(f.getColor().getColor());

			g.drawString(String.format("%d", f.getNumber()), x0 + figureWidth/3, y0 + figureHeight/3);
		}
		else
		{
			if (f.getInstance() == 3)
			{
				g.setColor(Color.RED);
			}
			else
			{
				g.setColor(Color.BLACK);
			}
			g.drawString("J", x0 + figureWidth/2, y0 + figureHeight/4);
		}
	}
	
	class Coordinates
	{
		private int column;
		private int row;
		
		public Coordinates()
		{
			this.column = 0;
			this.row = 0;
		}
		
		public Coordinates(int r,int c)
		{
			this.column = c;
			this.row = r;
		}
		
		public void increase()
		{
			this.column++;
			if (this.column==13)
			{
				this.column = 0;
				this.row++;
			}
		}
		
		public void nextLine()
		{
			this.row++;
			this.column = 0;
		}
		
		public Integer getColumn() {
			return column;
		}
		public void setColumn(Integer column) {
			this.column = column;
		}
		public Integer getRow() {
			return row;
		}
		public void setRow(Integer row) {
			this.row = row;
		}
		
	}
		
}
	
