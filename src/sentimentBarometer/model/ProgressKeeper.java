package sentimentBarometer.model;


import java.util.ArrayList;

import java.util.concurrent.TimeUnit;
import sentimentBarometer.model.observer.Observer;
import sentimentBarometer.model.observer.Subject;

/**
 * 	ProgressKeeper.java
 * 
 * 	Keeps track of relevant times when the program is running & displays progress.
 * 
 */
public class ProgressKeeper implements Subject {
	
	
	
	long startTime = 0;
	long processingStartTime = 0;
	long processingEndTime = 0;
	long currTime = 0;
	long elapsedTime = 0;
	double tokenCount = 0.0;
	int lastpercent = 0;
	int tokensize = 0;
	private ArrayList<Observer> _observers = new ArrayList<>();
        
	void setStartTime(long time) { startTime = time; }
	void setProcessingStartTime(long time) { processingStartTime = time; }
	void setProcessingEndTime(long time) { processingEndTime = time; }
	void setCurrTime(long time) { currTime = time; }
	
	String getElapsedTime()
	{
		//Calculates the elapsed time
		elapsedTime = (processingEndTime - startTime);
		return formatTime(elapsedTime);
	}
	
	void getProgress()
	{
		//calculates and displays the current progress in the console
		tokenCount = tokenCount+1;
		double progress = tokenCount/getTokenArraySize();
		int percent = (int) Math.round(progress*100.0f);
		
		if (percent != lastpercent)
		{  
			double timeRemainingPercent = (100.0/percent)-1;
			setCurrTime(System.currentTimeMillis());
			long timeElapsed = (currTime - processingStartTime);
			double timeRemaining = (timeElapsed*timeRemainingPercent);
			String timeRemainingReadable = formatTime(timeRemaining);
			for (int j = 0; j < 100; j++) { System.out.println(); }
			/*System.out.println("****************************************************************************************************");
			System.out.println("*                                                                                                  *");
			System.out.println("*                        Progress: " + String.format("%03d", percent) + "% - Est Time Remaining: " + timeRemainingReadable + "                       *");
			System.out.println("*                                                                                                  *");
			System.out.println("****************************************************************************************************");
			*/
                        lastpercent = percent;
                        notifyObservers();
		}
	}
	
	String formatTime(double time)
	{
		//Formats the time to make it readable
		int mins = (int) TimeUnit.MILLISECONDS.toMinutes((long) time);
		int secs = (int) (TimeUnit.MILLISECONDS.toSeconds((long) time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) time)));
		return (String.format(mins + " mins %02d", secs))+" secs";
	}
	
	int getTokenArraySize()
	{
		//Get the size of the array, if we havent done this before get .size() from the array, else return the result we got last time
		if (tokensize == 0)
		{
			tokensize = Main.MyTokenizer.tokenArray.size();
			return tokensize;
		} else { return tokensize; }
	}
        
        //MY CODE
        //Implement Subject
        @Override 
        public void register(Observer obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        if(!_observers.contains(obj)) _observers.add(obj);
        }
    
        @Override
        public void unregister(Observer obj) {
        _observers.remove(obj);
        }
        
        @Override
        public void notifyObservers() {
            for (Observer obj : _observers)
                obj.update();
        }
        
        @Override
        public Object getUpdate(Observer obj) {
            return lastpercent;
        }
        
        
    


        
        
}
