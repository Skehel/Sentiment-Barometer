package sentimentBarometer.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.extjwnl.JWNLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/**
 * Main.java
 * 
 * Runs SynsetSearch and HierarchySearch to prepare the arrays for later. Passes
 * the text file into the Tokenizer to begin processing and finally calls
 * getRating in HierarchyManager to get the results.
 * 
 * @Author Originally Brendan Warren, heavily edited by Tim Skehel
 */
public class Main {

	static SynsetManager MySynsetManager;
	static Tokens MyTokenizer;
	public ArrayList<Object> _valuesArray;

	// constructor
	public Main() {
		MyTokenizer = new Tokens();
		MySynsetManager = new SynsetManager();
	}
	
	/**
	 * Runs the entire model to analyse a text document for it's emotional rating.
	 * 
	 * 
	 * @param filePath String The filepath of the text document to be analysed
	 * @throws FileNotFoundException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws JWNLException
	 */
	public void runModel(String filePath) throws FileNotFoundException,
			XPathExpressionException, ParserConfigurationException,
			SAXException, IOException, JWNLException {
		SynsetSearch.getSynsetInstance();
		HierarchySearch.getHierarchyInstance();
		MyTokenizer.runTokenizer(filePath);
		_valuesArray = HierarchyManager.getRating();
	}
	/**
	 * Returns the results of the analysis in an ArrayList: 
	 * [0] - Positive Word Count
	 * [1] - Negative Word Count
	 * [2] - Neutral Word Count
	 * [3] - Ambiguous Word Count
	 * [4] - Total Word Count
	 * [5] - Rating
	 * [6] - ArrayList of all the emotional words
	 * 
	 * @return 
	 * @see #_valuesArray 
	 */
	public ArrayList<Object> getValues() {
		return _valuesArray;
	}

	public Tokens getMyTokenizer() {
		return MyTokenizer;
	}

	public SynsetManager getMySynsetManager() {
		return MySynsetManager;
	}
}