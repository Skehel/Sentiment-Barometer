package sentimentBarometer.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;
/**
 * 	HierarchyManager.java
 * 
 * 	Takes Offsets from SynsetManager.java & runs them through the Hierarchy until an emotion type is reached before passing the results to Rating.java
 * 
 */
public class HierarchyManager {
	
	
	
	public static Rating textRating = new Rating();
	
	static ArrayList<Object> getRating() throws IOException
	{
                //My code 
		ArrayList valuesArray = textRating.getRating();
                return valuesArray;
	}
	
	String processOffset(String offset, String token, String type) throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		// processOffset runs through the Hierarchy until we get to a final emotional value
		//Get the categ from the offset
      	String categ = SynsetSearch.getSynsetInstance().getId(offset, "noun");
      	if (categ != null)
      	{
      		//Get the isa from the categ
      		String isa = HierarchySearch.getHierarchyInstance().getIsa(categ);
			if (isa != null)
      		{		
				while (!isa.equals("positive-emotion") && !isa.equals("negative-emotion") && !isa.equals("neutral-emotion") && !isa.equals("ambiguous-emotion"))
				{
					//Unless we have a positive, negative, neutral or ambiguous emotion keep getting the next result in the Hierarchy
					String isa2 = HierarchySearch.getHierarchyInstance().getIsa(isa);
					isa = isa2;
					//If the Hierarchy returns a null value break
					if (isa == null) break;
				}
				
				if (isa != null)
				{
					//As long as the isa isn't null send it to updateRating
					textRating.updateRating(isa, token, type);
				}
      		}
      	}
      	return null;
	}
        
        public Rating getTextRating() {
            return textRating;
        }
}