package asp;

import java.util.List;
import java.util.ArrayList;

public class AspPredicate {
	private List<String> atoms;
	private String name;
	
	public AspPredicate()
	{
		this.atoms = new ArrayList<String>();
	}
	
	public List<String> getAtoms() {
		return atoms;
	}
	public void addAtoms(String atom) {
		this.atoms.add(atom);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Integer> atomsAsIntegers()
	{
		List<Integer> atomsAsInteger = new ArrayList<Integer>();
		this.atoms.forEach(atom ->
		{
			try
			{
			    Integer i = Integer.parseInt(atom);
			    atomsAsInteger.add(i);
			}
			catch (Exception e){
			}
			
		});
		if (atomsAsInteger.size() != this.atoms.size() )
		{
			return null;
		}
		return atomsAsInteger;
	}
}
