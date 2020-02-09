package asp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class AspHelperTest {

	@Test
	void predicateParseTest() {
		String mypredicate = "a_cool_predicate(1,2,3,42,honk).";
		AspPredicate pred = AspHelper.parsePredicate(mypredicate);
		Assert.assertNotNull(pred);
		Assert.assertTrue(pred.getAtoms().size()==5);
		Assert.assertNull(pred.atomsAsIntegers());
	}
	
	@Test
	void predicateParseTest2() {
		String mypredicate = "a_cool_predicate(1,2,3,42,13).";
		AspPredicate pred = AspHelper.parsePredicate(mypredicate);
		Assert.assertNotNull(pred);
		Assert.assertTrue(pred.getAtoms().size()==5);
		Assert.assertNotNull(pred.atomsAsIntegers());
	}

}
