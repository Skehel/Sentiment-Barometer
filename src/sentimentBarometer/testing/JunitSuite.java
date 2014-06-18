/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sentimentBarometer.testing;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import net.sf.extjwnl.JWNLException;

import org.junit.Test;
import org.xml.sax.SAXException;

import sentimentBarometer.controller.SentimentWorker;
import sentimentBarometer.model.Main;
import sentimentBarometer.model.Tokens;
import static org.junit.Assert.*;
/**
 *
 * @author Tim
 */
public class JunitSuite {
    
    @Test
    public void modelTest() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, FileNotFoundException, JWNLException {
        //test the main class
        Main testMain = new Main();
        testMain.runModel("C:/Users/Tim/Documents/testData.txt");
        ArrayList<Object> result = testMain.getValues();
        ArrayList<Object> expectedResult = new ArrayList<>();
        //create expectedResult values
        ArrayList<String> wordArray = new ArrayList<String>();
        wordArray.add("love");
        wordArray.add("sadness");
        wordArray.add("surprise");
        wordArray.add("apathy");
        //add expectedResult values to array
        expectedResult.add(1);
        expectedResult.add(1);
        expectedResult.add(1);
        expectedResult.add(1);
        expectedResult.add(4);
        expectedResult.add(0.0);
        expectedResult.add(wordArray);
        //check expectedResult against actual result
        assertEquals(expectedResult, result);   
    }
    
    @Test
    public void testSentimentWorkerConvertToString() throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, JWNLException {
    	 Main testMain = new Main();
         testMain.runModel("C:/Users/Tim/Documents/testData.txt");
         ArrayList<Object> result = testMain.getValues();
         SentimentWorker testSW = new SentimentWorker();
         
         ArrayList<String> expectedResult = new ArrayList<>();
         expectedResult.add("1");
         expectedResult.add("1");
         expectedResult.add("1");
         expectedResult.add("1");
         expectedResult.add("4");
         expectedResult.add("0.0");
         expectedResult.add(" love sadness surprise apathy");
         
         assertEquals(expectedResult, testSW.convertValues(result));
    }
    
    @Test
    public void observerTest() {
    	Main testMain = new Main();
    	SentimentWorker testSW = new SentimentWorker();
    	testMain.getMyTokenizer().getMyProgressKeeper().register(testSW);
    	int i = testMain.getMyTokenizer().getMyProgressKeeper()._observers.size();
    	assertEquals(1, i);
    }
    
    
    
    
}
