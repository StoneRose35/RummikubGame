package ch.sr35.rummikub.asp;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents an ASP predicate, someething in the form "pred(a1,a2,a3)
 * @author philipp
 *
 */
public class AspPredicate {
	private List<String> atoms;
	private String name;
	
	public AspPredicate()
	{
		this.atoms = new ArrayList<String>();
	}
	
	/**
	 * returns the atoms, with the example given this would be ["a1", "a2", "a3"]
	 * @return
	 */
	public List<String> getAtoms() {
		return atoms;
	}
	public void addAtom(String atom) {
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
