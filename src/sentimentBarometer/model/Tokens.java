package sentimentBarometer.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;


import org.xml.sax.SAXException;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
/**
 * 	Tokens.java
 * 
 * 	Runs the tokenizer to get all the tokens and then processes each individual token
 * 
 */

public class Tokens
{
	
	
	ArrayList<String> tokenArray = new ArrayList<String>();
	public static ProgressKeeper MyProgressKeeper = new ProgressKeeper();
        
        public ProgressKeeper getMyProgressKeeper() {
            return MyProgressKeeper;
        }
	void runTokenizer(String brFile) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, JWNLException
	{
               
                          
                MyProgressKeeper.setStartTime(System.currentTimeMillis());
		BufferedReader br = new BufferedReader(new FileReader(brFile));

		try {
	        String line = br.readLine();
	        InputStream is = new FileInputStream("E:/UNI/OOS/Year3/SentimentBarometer/res/en-token.bin");
    		TokenizerModel model = new TokenizerModel(is);
    		TokenizerME tokenizer = new TokenizerME(model);
    		
			while (line != null)
			{
					String tokens[] = tokenizer.tokenize(line);
					for (String a : tokens)
					{	
						//Add all tokens into the tokenArray
						tokenArray.add(a);
					}
				line = br.readLine();	         
			}
		}
		finally
		{
			//Once done close the bufferedReader and start processing the tokens
			br.close();
			processTokens();  
		}
	}
	
	void processTokens() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, JWNLException
	{
		MyProgressKeeper.setProcessingStartTime(System.currentTimeMillis());
		
		Dictionary dictionary = Dictionary.getInstance(new FileInputStream("E:/UNI/OOS/Year3/SentimentBarometer/res/file_properties.xml"));
        
		//For each token
		for (String token : tokenArray)
		{
			MyProgressKeeper.getProgress();
			//Check the token with regex. Only words with 3 or more letters can ever match so skip anything else
		    if (token.matches("[a-zA-Z]{3,}") == true)
		    {			
		    	//Get the POS and pass it onto the SynsetManager
		    	for (POS pos : POS.getAllPOS())
		    	{
		    		IndexWord word = dictionary.getIndexWord(pos, token);
		    		String type = pos.toString();
		    		String letter = null;
		    		switch (type)
		    		{
			            case "[POS: adverb]":  letter = "r"; type = "adverb"; break;
			            case "[POS: verb]":  letter = "v"; type = "verb"; break;
			            case "[POS: noun]":  letter = "n"; type = "noun"; break;
			            case "[POS: adjective]":  letter = "a"; type = "adjective"; break;
		    		}
		    		//Once we have a match send any nouns to getIsa and anything else to getId
		    		if (type.equals("noun")) { if (word != null) Main.MySynsetManager.getIsa(word, null, token, "noun"); }
		    		else { if (word != null) Main.MySynsetManager.getId(word, token, type, letter); }
		    	}
			}
		}
	}
}