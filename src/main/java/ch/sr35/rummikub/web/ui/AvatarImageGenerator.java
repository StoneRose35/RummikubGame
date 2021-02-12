package ch.sr35.rummikub.web.ui;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;



public class AvatarImageGenerator {
	
	private static String[] ELEMENTS_OF_INTEREST = {"g","path","circle","ellipse"};
	private static String[] SKIN_COLORS = {};
	private static String[] HAIR_COLORS = {};
	private static String HAIR_STYLE = "hairStyle";
	private static String BEARD_STYLE = "beardStyle";
	private List<Node> nodes;
	
	public AvatarImageGenerator()
	{
		this.nodes=new ArrayList<Node>();
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

	
	public void loadTemplate() throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("images/avatars_template.svg");
		File svgFile = new File(resource.toURI());
		DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc= dBuilder.parse(svgFile);
		doc.normalize();
		
		Arrays.stream(AvatarImageGenerator.ELEMENTS_OF_INTEREST).forEach(el -> {
			NodeList nl = doc.getElementsByTagName(el);
			if (el.equals("g"))
			{
				
				for(int c=0;c<nl.getLength();c++)
				{
					boolean ignore=false;
					NamedNodeMap attrs = nl.item(c).getAttributes();
					for (int c2=0;c2<attrs.getLength();c2++)
					{
						if(attrs.item(c2).getNodeName().startsWith("inkscape"))
						{
							ignore=true;
						}
					}
					if (!ignore)
					{
						nodes.add(nl.item(c));
					}
				}
			}
			else
			{
				for(int c=0;c<nl.getLength();c++)
				{
					nodes.add(nl.item(c));
				}
			}
			
		});
	       
	}
	
	public String generateAvatar()
	{
		throw new UnsupportedOperationException();
	}
	

	public String getStringById(String id)
	{
		Node n = this.getById(id);
		return this.getNodeString(n);
	}
	
	private Node getById(String id)
	{
		return this.nodes.stream().filter(n -> {
			NamedNodeMap a = n.getAttributes();
			Node idNode= a.getNamedItem("id");
			return idNode.getNodeValue().equals(id);
		}).findFirst().orElse(null);
	}
	
	private Node chooseHair()
	{
		throw new UnsupportedOperationException();
	}
	
	private Node chooseBeard()
	{
		throw new UnsupportedOperationException();
	}
	
	private void setColor(Node n)
	{
		throw new UnsupportedOperationException();
	}
	
	private String getNodeString(Node node) {
	    try {
	        StringWriter writer = new StringWriter();
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.transform(new DOMSource(node), new StreamResult(writer));
	        String output = writer.toString();
	        return output.substring(output.indexOf("?>") + 2);
	    } catch (TransformerException e) {
	    }
	    return null;
	}

}
