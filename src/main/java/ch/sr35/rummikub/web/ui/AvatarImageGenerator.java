package ch.sr35.rummikub.web.ui;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


@Component
public class AvatarImageGenerator {
	
	private static String[] ELEMENTS_OF_INTEREST = {"g","path","circle","ellipse", "rect"};
	private static String[] BASIC_ELEMENTS = {"head","mouth", "leftEye", "rightEye", "leftEyeBrow", "rightEyeBrow", "nose", "leftEar", "rightEar"};
	private static String HAIR_STYLE = "hairStyle";
	private static String BEARD_STYLE = "beardStyle";
	private static String SKIN_COLOR = "skinColor";
	private static String HAIR_COLOR = "hairColor";
	private static String ID = "id";
	private static String COLOR_STYLE = "fill";
	protected static String STYLE = "style";
	private static String INKSCAPE = "inkscape";
	private static String SVG = "svg";
	private static String VIEWBOX_VAL = "0 0 8 9";
	private static String VIEWBOX = "viewBox";
	private static String HEIGHT_VAL = "80";
	private static String HEIGHT = "height";
	private static String WIDTH_VAL = "80";
	private static String WIDTH = "width";	
	private static String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
	private static String TEMPLATE_PATH = "images/avatars_template.svg";
	
	
	private List<String> skinColors;
	private List<String> hairColors;
	private List<Node> hairStyles;
	private List<Node> beardStyles;
	private List<Node> nodes;
	Document doc;
	private Random r;
	
	public AvatarImageGenerator()
	{
		this.skinColors = new ArrayList<String>();
		this.hairColors = new ArrayList<String>();
		this.hairStyles = new ArrayList<Node>();
		this.beardStyles = new ArrayList<Node>();
		this.r = new Random();
		try {
			this.loadTemplate();
		} catch (ParserConfigurationException | SAXException | IOException | URISyntaxException e) {
		}
		
	}
	
	
	public void loadTemplate() throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		List<Node> nodes = new ArrayList<>();
		ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(TEMPLATE_PATH);
		File svgFile = new File(resource.toURI());
		DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc= dBuilder.parse(svgFile);
		doc.normalize();
		this.doc = doc;
		
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
						if(attrs.item(c2).getNodeName().startsWith(INKSCAPE))
						{
							ignore=true;
						}
					}
					if (!ignore)
					{
						nodes.add(NodeBuilder.instance()
									.loadNode(nl.item(c))
									.removeDisplayNone()
									.cleanAttributes()
									.node());
					}
				}
			}
			else
			{
				for(int c=0;c<nl.getLength();c++)
				{
					nodes.add(NodeBuilder.instance()
							.loadNode(nl.item(c))
							.removeDisplayNone()
							.cleanAttributes()
							.node());
				}
			}
			
		});
	     this.loadBeardStyles(nodes);
	     this.beardStyles.add(null);
	     this.loadHairStyles(nodes);
	     this.hairStyles.add(null);
	     this.loadSkinColors(nodes);
	     this.loadHairColors(nodes);
	     this.nodes = nodes;
	}
	
	public String generateAvatar()
	{
		List<Node> avatarNodes = new ArrayList<Node>();
		Arrays.stream(BASIC_ELEMENTS).forEach(el -> {
			avatarNodes.add(this.getNode(el).cloneNode(true));
		});
		this.setColor(avatarNodes.get(0),this.skinColors.get(this.r.nextInt(this.skinColors.size())));
		avatarNodes.add(this.chooseHair());
		avatarNodes.add(this.chooseBeard());
		Node svgNode = doc.createElementNS(SVG_NAMESPACE, SVG);
		Node aNode = this.doc.createAttribute(WIDTH);
		aNode.setNodeValue(WIDTH_VAL);
		svgNode.getAttributes().setNamedItem(aNode);
		aNode = this.doc.createAttribute(HEIGHT);
		aNode.setNodeValue(HEIGHT_VAL);
		svgNode.getAttributes().setNamedItem(aNode);		
		aNode = this.doc.createAttribute(VIEWBOX);
		aNode.setNodeValue(VIEWBOX_VAL);
		svgNode.getAttributes().setNamedItem(aNode);	
		avatarNodes.stream().filter(e -> e != null).forEach(el -> {
			svgNode.appendChild(el);
		});
		return this.getNodeString(svgNode);
	}
	
	
	private void loadBeardStyles(List<Node> nodes)
	{
		this.beardStyles = nodes.stream().filter(n -> {
			NamedNodeMap a = n.getAttributes();
			Node idNode= a.getNamedItem(ID);
			return idNode.getNodeValue().startsWith(BEARD_STYLE);
		}).collect(Collectors.toList());
	}
	
	private void loadHairStyles(List<Node> nodes)
	{
		this.hairStyles = nodes.stream().filter(n -> {
			NamedNodeMap a = n.getAttributes();
			Node idNode= a.getNamedItem(ID);
			return idNode.getNodeValue().startsWith(HAIR_STYLE);
		}).collect(Collectors.toList());
	}
	
	private void loadSkinColors(List<Node> nodes)
	{
		this.skinColors = loadColors(SKIN_COLOR,nodes);
	}
	
	private void loadHairColors(List<Node> nodes)
	{
		this.hairColors = loadColors(HAIR_COLOR,nodes);
	}
	
	private List<String> loadColors(String elementId, List<Node> nodes)
	{
		return 
		nodes.stream().filter(n -> {
			NamedNodeMap a = n.getAttributes();
			Node idNode= a.getNamedItem(ID);
			return idNode.getNodeValue().startsWith(elementId);
		}).map(el -> {
			String style = el.getAttributes().getNamedItem(STYLE).getNodeValue();
			return Arrays.stream(style.split(";")).map(s -> {
				return s.split(":");
			}).filter(sv -> sv[0].equals(COLOR_STYLE)).findFirst().orElse(null)[1];
			
		}).collect(Collectors.toList());
	}
	
	private Node chooseHair()
	{
		Node chosen = this.hairStyles.get(r.nextInt(this.hairStyles.size()));
		if (chosen != null)
		{
			chosen = chosen.cloneNode(true);
			String clr = this.hairColors.get(r.nextInt(this.hairColors.size()));
			this.setColor(chosen, clr);
		}
		return chosen;
	}
	
	private Node chooseBeard()
	{
		Node chosen = this.beardStyles.get(r.nextInt(this.beardStyles.size()));
		if (chosen != null)
		{
			chosen = chosen.cloneNode(true);
			String clr = this.hairColors.get(r.nextInt(this.hairColors.size()));
			this.setColor(chosen, clr);
		}
		return chosen;
	}
	
	private void setColor(Node n,String color)
	{
		Node styleNode = n.getAttributes().getNamedItem(STYLE);
		if (styleNode != null)
		{
			String style = styleNode.getNodeValue();
			style = style.replaceFirst(COLOR_STYLE + ":#[0-9a-f]{6}", COLOR_STYLE + ":" + color);
			styleNode.setNodeValue(style);
			n.getAttributes().setNamedItem(styleNode);
		}
		NodeList children = n.getChildNodes();
		for (int c=0;c < children.getLength();c++)
		{
			if (children.item(c) instanceof Element)
			{
				this.setColor(children.item(c), color);
			}
		}
	}
	
	private Node getNode(String elementId)
	{
		return this.nodes.stream().filter(n -> {
				NamedNodeMap a = n.getAttributes();
				Node idNode= a.getNamedItem(ID);
				return idNode.getNodeValue().equals(elementId);
				}).findFirst().orElse(null);
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

	public List<String> getSkinColors() {
		return skinColors;
	}

	public List<String> getHairColors() {
		return hairColors;
	}

}
