package sentimentBarometer.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import sentimentBarometer.model.observer.Observer;
import sentimentBarometer.model.observer.Subject;
import org.apache.log4j.Logger;
/**
 * 	Rating.java
 * 
 * 	Takes the final results from HierarchyManager.java and updates the rating & produces the final report at the end of the program
 * 
 */
public class Rating implements Subject {

    
    BufferedReader myInput = new BufferedReader(new InputStreamReader(System.in));
    public double rating = 0;
    public ArrayList<String> matchesArray = new ArrayList<String>();
    public ArrayList<String> wordArray = new ArrayList<String>();
    public static ArrayList<String> verbArray = new ArrayList<String>();
    public static ArrayList<String> adjectiveArray = new ArrayList<String>();
    public static ArrayList<String> nounArray = new ArrayList<String>();
    public static ArrayList<String> adverbArray = new ArrayList<String>();
    public int posCount = 0;
    public int negCount = 0;
    public int neutCount = 0;
    public int ambiCount = 0;
    public int matchCount;
    static final Logger log = Logger.getLogger(Rating.class);
    //MY CODE
    public ArrayList<Object> valuesArray = new ArrayList<Object>();
    private ArrayList<Observer> _observers = new ArrayList<>();

    static void saveWord(String word, String type) {
        //saveWord is used to store any matches in the relevant array
        if (type.equals("verb")) {
            verbArray.add(word);
        } else if (type.equals("adverb")) {
            adverbArray.add(word);
        } else if (type.equals("adjective")) {
            adjectiveArray.add(word);
        } else if (type.equals("noun")) {
            nounArray.add(word);
        }
    }
    
    void updateRating(String emotion, String token, String type) {
        // updateRating handles all the rating and storing data in the relevant arrays
        if (!matchesArray.isEmpty()) {
            matchCount = matchesArray.size() + 1;
        } else {
            matchCount = 1;
        }
        String lastToken = null;
        if (!wordArray.isEmpty() && !matchesArray.isEmpty()) {
            // take note of the last token added to the array
            lastToken = wordArray.get(wordArray.size() - 1);
        }
        if (emotion.equals("positive-emotion") && !token.equals(lastToken)) {
            // If the emotion is positive and the token isn't the same as the last save the token, its emotion and type. Calculate the rating
            rating = rating + 1;
            matchesArray.add(token + " ~ Positive ~ Current Rating: " + (double) Math.round((rating / matchCount) * 100000) / 100000);
            wordArray.add(token);
            Rating.saveWord(token, type);
            posCount++;
        } else if (emotion.equals("negative-emotion") && !token.equals(lastToken)) {
            // If the emotion is negative and the token isn't the same as the last save the token, its emotion and type. Calculate the rating
            rating = rating - 1;
            matchesArray.add(token + " ~ Negative ~ Current Rating: " + (double) Math.round((rating / matchCount) * 100000) / 100000);
            wordArray.add(token);
            Rating.saveWord(token, type);
            negCount++;
        } else if (emotion.equals("neutral-emotion") && !token.equals(lastToken)) {
            // If the emotion is neutral and the token isn't the same as the last save the token and type
            neutCount++;
            Rating.saveWord(token, type);
            wordArray.add(token);
        } else if (emotion.equals("ambiguous-emotion") && !token.equals(lastToken)) {
            //DEBUGGING
            //log.debug("token: " + token + " last token: " + lastToken + " ambiCount: " +ambiCount);
        	
            // If the emotion is neutral and the token isn't the same as the last save the token and type
            ambiCount++;
            Rating.saveWord(token, type);
            wordArray.add(token);
        }
        notifyObservers();
    }

    /**
     * Creates and returns an ArrayList containing all that make up the rating and the rating itself
     * [0] - Positive Word Count
	 * [1] - Negative Word Count
	 * [2] - Neutral Word Count
	 * [3] - Ambiguous Word Count
	 * [4] - Total Word Count
	 * [5] - Rating
	 * [6] - ArrayList of all the emotional words
	 * 
     * @return valuesArray ArrayList of the values that make up the rating and the rating itself
     */
    public ArrayList<Object> getRating() {
        valuesArray.add(posCount);
        valuesArray.add(negCount);
        valuesArray.add(neutCount);
        valuesArray.add(ambiCount);
        int wordCount = posCount + negCount + neutCount + ambiCount;
        valuesArray.add(wordCount);
        if (!matchesArray.isEmpty()) {
            matchCount = matchesArray.size();
        } else {
            matchCount = 1;
        }
        rating = (rating / matchCount);
        rating = (double) Math.round(rating * 100000) / 100000;
        valuesArray.add(rating);
        valuesArray.add(wordArray);
        return valuesArray;
    }

    //implement observer pattern
    @Override
    public void register(Observer obj) {
        //register observer
        if (obj == null) {
            throw new NullPointerException("Null Observer");
        }
        if (!_observers.contains(obj)) {
            _observers.add(obj);
        }
    }

    @Override
    public void unregister(Observer obj) {
        _observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        for (Observer obj : _observers) {
            obj.update();
        }
    }
    /**
     * Calculates rating as the program runs and updates observers
     */
    @Override
    public Object getUpdate(Observer obj) {
        //calculate rating on the fly
        int mCount;
        if (!matchesArray.isEmpty()) {
             mCount = matchesArray.size();
        } else {
             mCount = 0;
        }
        double currentRating = (rating / mCount);
        //calculate current wordcount
        double wordCount = posCount + negCount + neutCount + ambiCount;
            
        ArrayList<Double> ratingNwords = new ArrayList<Double>();
        //add values to array 
        ratingNwords.add(wordCount);
        ratingNwords.add(currentRating);
        return ratingNwords;
    }
    //Brendan's code for printing to console
    /*void getRating() throws IOException
     {
     // getRating is used to print out all the data collected during the analysis in a readable format
     Tokens.MyProgressKeeper.setProcessingEndTime(System.currentTimeMillis());
     for (int j = 0; j < 100; j++) { System.out.println(); }
     System.out.println("*********************************************************************************");
     System.out.println();
     System.out.println("It took " + Tokens.MyProgressKeeper.getElapsedTime() + " to process " + Main.MyTokenizer.tokenArray.size() + " tokens.");
     System.out.println();
     System.out.println("In total " + (posCount+negCount+neutCount+ambiCount) + " words were matched which were split into 4 emotions:");
     System.out.println("Positive Words: " + posCount + " ~ Negative Words: " + negCount + " ~ Neutral Words: " + neutCount + " ~ Ambiguous Count: " + ambiCount);
     System.out.println("Verbs: "+verbArray.size()+" ~ Nouns: "+nounArray.size()+" ~ Adjectives: "+adjectiveArray.size()+" ~ Adverbs: "+adverbArray.size());
     System.out.println();
     System.out.println("*********************************************************************************");
     System.out.println();
     System.out.println("Looking at just the Positive & Negative words there were " + matchCount + " matches.");
     System.out.println();
     if (!matchesArray.isEmpty()) {
     matchCount = matchesArray.size();
     } else { matchCount = 1; }
     rating = (rating/matchCount);
     rating = (double)Math.round(rating * 100000) / 100000;
     if (rating > 0.8) { System.out.println("The Text Was Very Positive ~ Rating: "+(rating)); }
     else if (rating > 0.3) { System.out.println("The Text Was Positive ~ Rating: "+(rating)); }
     else if (rating > 0) { System.out.println("The Text Was Slightly Positive ~ Rating: "+(rating)); }
     else if (rating < 0) { System.out.println("The Text Was Slightly Negative ~ Rating: "+(rating)); }
     else if (rating < -0.3) { System.out.println("The Text Was Negative ~ Rating: "+(rating)); }
     else if (rating < -0.8) { System.out.println("The Text Was Very Negative ~ Rating: "+(rating)); }
     else { System.out.println("The Text Was Neutral ~ Rating: "+rating); }
     System.out.println();
     System.out.println("*********************************************************************************");
     System.out.println();
     System.out.println("Do you want to see the matching words? (y/n)");
     String input = myInput.readLine();
		
     if (input.equals("y"))
     {
     for (String s : matchesArray)
     {
     System.out.println(s);
     }
     }
           
     } */
}
