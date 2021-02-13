package ch.sr35.rummikub.web.ui;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeBuilder {
	
	private Node n;
	
	private NodeBuilder()
	{
		
	}
	
	
	public static NodeBuilder instance()
	{
		/*
		if (NodeBuilder.instance == null)
		{
			NodeBuilder.instance = new NodeBuilder();
		}
		return NodeBuilder.instance;
		*/
		return new NodeBuilder();
	}
	
	public NodeBuilder loadNode(Node n)
	{
		this.n = n;
		return this;
	}
	
	public Node node() {
		return this.n;
	}
	
	public NodeBuilder removeDisplayNone()
	{
		Node styleNode = this.n.getAttributes().getNamedItem(AvatarImageGenerator.STYLE);
		if (styleNode!= null)
		{
			String style = styleNode.getNodeValue();
			style = style.replace("display:none", "")
						 .replaceFirst("^;", "")
						 .replaceFirst(";$", "")
						 .replaceAll(";;", ";");
			if (!style.isEmpty())
			{
				styleNode.setNodeValue(style);
			}
			else
			{
				this.n.getAttributes().removeNamedItem(AvatarImageGenerator.STYLE);
			}
		}
		return this;
	}
	
	public NodeBuilder cleanAttributes()
	{
		NamedNodeMap attrs = this.n.getAttributes();
		List<String> removeList = new ArrayList<String>();
		for (int c=0;c< attrs.getLength();c++)
		{
			if (attrs.item(c).getNodeName().contains(":")) 
			{
				removeList.add(attrs.item(c).getNodeName());
			}
		}
		removeList.stream().forEach(el -> {
			attrs.removeNamedItem(el);
		});
		return this;
	}
	

}
