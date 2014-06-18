package sentimentBarometer.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.Synset;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
/**
 * 	SynsetManager.java
 * 
 * 	getIsa() sends the categ to HierarchyManager
 * 	getId() looks up the offset in SynsetSearch and then passes the categ to getIsa()
 * 	prepareOffset() prepares the offsets obtained from the synset for use by getIsa() and getId()
 * 
 */
public class SynsetManager
{
	
	
	
	HierarchyManager myHierarchy = new HierarchyManager();

	void getIsa(IndexWord noun, String id, String token, String type) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		// Takes noun or id and send them to the offset processor in HierarchyManager.java
		if (noun != null)
		{
			for (Synset synset : noun.getSenses())
			{
	          	String offset = prepareOffset("n", synset);
	          	myHierarchy.processOffset(offset, token, type);
	        }
		}
		
		if (id != null)
		{
          	myHierarchy.processOffset(id, token, type);
		}
	}
	
	String getId(IndexWord index, String token, String type, String letter) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		// Gets the ID from an offset using SynsetSearch before sending it to getIsa
		for (Synset synset : index.getSenses())
        {	
        	String offset = prepareOffset(letter, synset);
        	String categ = SynsetSearch.getSynsetInstance().getId(offset, type);
        	if (categ != null)
        	{
        		getIsa(null, categ, token, type);
        	}
        } 
		return type;
		
	}
	
	static String prepareOffset(String type, Synset synset)
	{
		//Takes the synset and prepares the offset for getId and getIsa
		int numzeros = 7 - ((int) Math.log10(synset.getOffset()));
    	String zeros = "";
    	for (int i = 0; i < numzeros; i++) { zeros = zeros+"0"; }
    	return type+"#"+zeros+synset.getOffset(); 
	}
        
        public HierarchyManager getMyHierachry() {
            return myHierarchy;
        }
	
}