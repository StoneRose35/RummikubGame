package ch.sr35.rummikub.web.ui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
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
	}
	
	@Test 
	public void GetNodeXmlTest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		AvatarImageGenerator aig=new AvatarImageGenerator();
		aig.loadTemplate();
		List<String> hairColors = aig.getHairColors();
		Assert.assertTrue(hairColors.size() > 3);
	}
	
	@Test
	public void GenerateAvatarTest() throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		AvatarImageGenerator aig=new AvatarImageGenerator();
		aig.loadTemplate();
		String avatar = aig.generateAvatar();
		Assert.assertTrue(avatar.startsWith("<svg"));
	}
	

}
