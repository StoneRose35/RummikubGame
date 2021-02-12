package ch.sr35.rummikub.web.ui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class AvatarGenerationTest {
	

	@Test 
	public void instantiationTest()
	{
		AvatarImageGenerator aig=new AvatarImageGenerator();
		Assert.assertNotNull(aig);
	}
	
	
	@Test 
	public void templateLoadTest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		AvatarImageGenerator aig=new AvatarImageGenerator();
		aig.loadTemplate();
		List<Node> nodes = aig.getNodes();
		Assert.assertNotNull(nodes);
		Assert.assertTrue(nodes.size() > 8);
	}
	
	@Test 
	public void GetNodeXmlTest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		AvatarImageGenerator aig=new AvatarImageGenerator();
		aig.loadTemplate();
		String mouthSvg = aig.getStringById("mouth");
		Assert.assertTrue(mouthSvg.length() > 10);

	}

}
