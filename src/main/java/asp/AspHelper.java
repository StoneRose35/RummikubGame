package asp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AspHelper {
	
	public static AspPredicate parsePredicate(String predicate)
	{
		AspPredicate result = null;
		Pattern p = Pattern.compile("([a-zA-Z_0-9]+)\\((([a-zA-Z_0-9]+,?)+)\\)\\.?");//. represents single character  
		Matcher m = p.matcher(predicate);
		if (m.matches()==true)
		{
			result = new AspPredicate();
			result.setName(m.group(1));
			String atomslist = m.group(2);
			String[] list = atomslist.split(",");
			for (String entry : list)
			{
			    result.addAtom(entry);
			}
		}
		return result;
	}

}
