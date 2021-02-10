package ch.sr35.rummikub.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import ch.sr35.rummikub.common.exceptions.GeneralException;


/**
 * Represent all figures on the stack/in the bag
 * @author philipp
 *
 */
public class Stack {
	private List<Figure> on_stack;
	
	public Stack()
	{
		on_stack = new ArrayList<Figure>();
	}
	
	/**
	 * fills the stack. This is usually done at the beginning of the game
	 */
	public void fill()
	{
		Color[] colors = Color.values();
		Figure rf;
		on_stack.clear();
		
		// create regular rummikub figures
		for (int n=1;n<14;n++)
		{
			for(int c=0;c<4;c++)
			{
				rf = new Figure();
				rf.setColor(colors[c]);
				try {
					rf.setNumber(n);
					rf.setInstance(1);
				} catch (GeneralException e) {
				}
				rf.setPlacement(Placement.ON_STACK);
				this.on_stack.add(rf);
				
				rf = new Figure();
				rf.setColor(colors[c]);
				try {
					rf.setNumber(n);
					rf.setInstance(2);
				} catch (GeneralException e) {
				}
				rf.setPlacement(Placement.ON_STACK);
				this.on_stack.add(rf);
			}
		}
		
		// create jokers
		rf = new Figure();
		try {
			rf.setInstance(3);
		} catch (GeneralException e) {
		}
		this.on_stack.add(rf);
		rf = new Figure();
		try {
			rf.setInstance(4);
		} catch (GeneralException e) {
		}
		this.on_stack.add(rf);
		
	}
	
	public int getSize()
	{
		return this.on_stack.size();
	}
	
	/**
	 * removes on figure from the stack and places it on the shelf
	 * @return
	 */
	public Figure drawFromStack()
	{
		if (this.on_stack.size() > 0)
		{
		    Random r = new Random();
		    Figure rf = this.on_stack.get(r.nextInt(this.on_stack.size()));
		    this.on_stack.remove(rf);
		    rf.setPlacement(Placement.ON_SHELF);
		    return rf;
		}
		else
		{
			return null;
		}
	}

}
