package sentimentBarometer.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
/**
 * 	SynsetSearch.java
 * 
 * 	Creates the arrays used to search the Synset & takes Offsets from SynsetManager.java and returns the noun-id or categ depending on what is needed
 * 
 */
public class SynsetSearch {
	

	
	private static SynsetSearch synsetInstance;
	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	XPathFactory xpathFactory = XPathFactory.newInstance();
	XPath xpath = xpathFactory.newXPath();
	ArrayList<String> nounOffsetArray = new ArrayList<String>();
	ArrayList<String> nounCategArray = new ArrayList<String>();
	ArrayList<String> adverbOffsetArray = new ArrayList<String>();
	ArrayList<String> adverbIdArray = new ArrayList<String>();
	ArrayList<String> verbOffsetArray = new ArrayList<String>();
	ArrayList<String> verbIdArray = new ArrayList<String>();
	ArrayList<String> adjectiveOffsetArray = new ArrayList<String>();
	ArrayList<String> adjectiveIdArray = new ArrayList<String>();
	Document document;
	
	// Use Singleton design pattern so we only have one instance of SynsetSearch
	public static SynsetSearch getSynsetInstance() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		if (null == synsetInstance)
		{
			//If no synsetInstance exists make a new one and create the builder and build the arrays
			synsetInstance = new SynsetSearch();
			synsetInstance.setBuilder();
			synsetInstance.buildArrays();
		}
		//Return the synsetInstance
		return synsetInstance;
	}
	
	private void setBuilder() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException
	{
		//Create the builder and document
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.parse(new FileInputStream("E:/UNI/OOS/Stuff/wn-domains-3.2/wn-domains-3.2/wn-affect-1.1/a-synsets.xml"));
	}
	
	private void buildArrays() throws FileNotFoundException, SAXException, IOException, ParserConfigurationException, XPathExpressionException
	{
		//Build arrays from the Synset XML file, these can then be used to search instead of repeatedly using xpath
		 NodeList nounSynsets = (NodeList) xpath.evaluate("/syn-list/noun-syn-list/noun-syn", document, XPathConstants.NODESET);
		    for (int i = 0; i < nounSynsets.getLength(); i++) {
		    	Node item = nounSynsets.item(i);
		    	nounOffsetArray.add(item.getAttributes().getNamedItem("id").getNodeValue());
		    	nounCategArray.add(item.getAttributes().getNamedItem("categ").getNodeValue());
		    }

		 NodeList adverbSynsets = (NodeList) xpath.evaluate("/syn-list/adv-syn-list/adv-syn", document, XPathConstants.NODESET);
		    for (int i = 0; i < adverbSynsets.getLength(); i++) {
		    	Node item = adverbSynsets.item(i);
		    	adverbOffsetArray.add(item.getAttributes().getNamedItem("id").getNodeValue());
		    	adverbIdArray.add(item.getAttributes().getNamedItem("noun-id").getNodeValue());
		    }

		 NodeList verbSynsets = (NodeList) xpath.evaluate("/syn-list/verb-syn-list/verb-syn", document, XPathConstants.NODESET);
		    for (int i = 0; i < verbSynsets.getLength(); i++) {
		    	Node item = verbSynsets.item(i);
		    	verbOffsetArray.add(item.getAttributes().getNamedItem("id").getNodeValue());
		    	verbIdArray.add(item.getAttributes().getNamedItem("noun-id").getNodeValue());
		    }

		 NodeList adjectiveSynsets = (NodeList) xpath.evaluate("/syn-list/adj-syn-list/adj-syn", document, XPathConstants.NODESET);
		    for (int i = 0; i < adjectiveSynsets.getLength(); i++) {
		    	Node item = adjectiveSynsets.item(i);
		    	adjectiveOffsetArray.add(item.getAttributes().getNamedItem("id").getNodeValue());
		    	adjectiveIdArray.add(item.getAttributes().getNamedItem("noun-id").getNodeValue());
		    }
	}
	
	String getId(String offset, String type)
	{
		//Create offset and Id array, move relevant arrays into these to then search
		ArrayList<String> offsetArray = null;
		ArrayList<String> IdArray = null;
		if (type.equals("adjective")) { offsetArray = adjectiveOffsetArray; IdArray = adjectiveIdArray;  } 
		if (type.equals("verb")) { offsetArray = verbOffsetArray; IdArray = verbIdArray;  } 
		if (type.equals("adverb")) { offsetArray = adverbOffsetArray; IdArray = adverbIdArray;  } 
		if (type.equals("noun")) { offsetArray = nounOffsetArray; IdArray = nounCategArray;  } 
		
		//Look for the offset in the offset array
		int exist = -1;
		for(int i=0;i<offsetArray.size();i++){
	        if(offsetArray.get(i).equals(offset)){
	            exist=i;
	            break;
	        }
		}
		//Use the position of the offset in the first array to get the ID from the second array
		if(exist != -1 ) {
		    return IdArray.get(exist);
		} else {
		    return null;
		}
	}
}