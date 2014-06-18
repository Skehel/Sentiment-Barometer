package sentimentBarometer.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 	HierarchySearch.java
 * 
 * 	Creates the arrays used to search the Hierarchy & takes categ from HierarchyManager.java and returns the relevant isa
 * 
 */
public class HierarchySearch {
	
	
	
	private static HierarchySearch hierarchyInstance;
	
	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	
	XPathFactory xpathFactory = XPathFactory.newInstance();
	XPath xpath = xpathFactory.newXPath();
	
	//Make arrays to store Hierarchy
	ArrayList<String> hierarchyNameArray = new ArrayList<String>();
	ArrayList<String> hierarchyIsaArray = new ArrayList<String>();
	
	Document document;
	
	// Use Singleton design pattern so we only have one instance of HierarchySearch
	public static HierarchySearch getHierarchyInstance() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		// If there is no hierarchyInstance make one. Return hierarchyInstance
		if (null == hierarchyInstance)
		{
			hierarchyInstance = new HierarchySearch();
			hierarchyInstance.setBuilder();
			hierarchyInstance.buildHierarchy();
		}
		return hierarchyInstance;
	}
	
	private void setBuilder() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException
	{
		// Sets up the Hierarchy builder
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.parse(new FileInputStream("E:/UNI/OOS/Stuff/wn-domains-3.2/wn-domains-3.2/wn-affect-1.1/a-hierarchy.xml"));
	}
	
	private void buildHierarchy() throws XPathExpressionException
	{
		//Builds the Hierarchy arrays
		 NodeList hierarchyList = (NodeList) xpath.evaluate("/categ-list/categ", document, XPathConstants.NODESET);
		    for (int i = 0; i < hierarchyList.getLength(); i++) {
		    	Node item = hierarchyList.item(i);
		    	if (!item.getAttributes().getNamedItem("name").getNodeValue().equals("root"))
		    	{
		    		hierarchyNameArray.add(item.getAttributes().getNamedItem("name").getNodeValue());
		    		hierarchyIsaArray.add(item.getAttributes().getNamedItem("isa").getNodeValue());
		    	}
		    }
	}
	
	String getIsa(String name)
	{
	//Looks up name in the Hierarchy arrays and returns the result
	int exist = -1;
		for(int i=0;i<hierarchyNameArray.size();i++){
	        if(hierarchyNameArray.get(i).equals(name)){
	            exist=i;
	            break;
	        }
		}	
	
		if(exist != -1 ) {
		    return hierarchyIsaArray.get(exist);
		} else {
		    return null;
		}
	}

}