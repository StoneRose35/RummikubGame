package game;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class Stack {
	private List<RummikubFigure> on_stack;
	
	public Stack()
	{
		on_stack = new ArrayList<RummikubFigure>();
	}
	
	public void initializeGame()
	{
		RummikubColor[] colors = RummikubColor.values();
		RummikubFigure rf;
		
		// create regular rummikub figures
		for (int n=1;n<14;n++)
		{
			for(int c=0;c<4;c++)
			{
				rf = new RummikubFigure();
				rf.setColor(colors[c]);
				try {
					rf.setNumber(n);
					rf.setInstance(1);
				} catch (RummikubException e) {
				}
				rf.setPlacement(RummikubPlacement.ON_STACK);
				this.on_stack.add(rf);
				
				rf = new RummikubFigure();
				rf.setColor(colors[c]);
				try {
					rf.setNumber(n);
					rf.setInstance(2);
				} catch (RummikubException e) {
				}
				rf.setPlacement(RummikubPlacement.ON_STACK);
				this.on_stack.add(rf);
			}
		}
		
		// create jokers
		rf = new RummikubFigure();
		try {
			rf.setInstance(3);
		} catch (RummikubException e) {
		}
		this.on_stack.add(rf);
		rf = new RummikubFigure();
		try {
			rf.setInstance(4);
		} catch (RummikubException e) {
		}
		this.on_stack.add(rf);
		
	}
	
	public int getSize()
	{
		return this.on_stack.size();
	}
	
	public RummikubFigure drawFromStack()
	{
		if (this.on_stack.size() > 0)
		{
		    Random r = new Random();
		    RummikubFigure rf = this.on_stack.get(r.nextInt(this.on_stack.size()));
		    this.on_stack.remove(rf);
		    rf.setPlacement(RummikubPlacement.ON_SHELF);
		    return rf;
		}
		else
		{
			return null;
		}
	}

}
