/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentBarometer.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import net.sf.extjwnl.JWNLException;
import org.xml.sax.SAXException;
import sentimentBarometer.model.Main;
import sentimentBarometer.model.observer.Observer;
import sentimentBarometer.model.observer.Subject;

/**
 * SentimentWorker extends Swingworker to run code in background thread also
 * listener to sentimentBarometer.model.ProgressKeeper so it can update the
 * progress bar in the sentimentBarometer.view.GUI
 * 
 * @see sentimentBarometer.view.GUI 
 * @see sentimentBarometer.model.ProgressKeeper
 * @author Tim
 */
public class SentimentWorker extends SwingWorker implements Observer {

	// Observable pattern for notification of "done" event.
	private List<Runnable> _isDoneChangedHandlers = new ArrayList<>();
	private String _filePath;
	private Subject _progressKeeper;
	private Main _main = new Main();
	private Subject _chartSubject = _main.getMySynsetManager().getMyHierachry()
			.getTextRating();

	public void addIsDoneChangedObserver(Runnable handler) {
		_isDoneChangedHandlers.add(handler);
	}

	public void removeIsDoneChangedObserver(Runnable handler) {
		_isDoneChangedHandlers.remove(handler);
	}

	private void fireIsDoneChanged() {
		for (Runnable handler : _isDoneChangedHandlers) {
			handler.run();
		}
	}

	/**
	 * Overrides SwingWorker
	 * 
	 * Working in background thread registers observer to get Progress, then
	 * runs the model returning the values in an ArrayList
	 * 
	 */
	@Override
	protected ArrayList<String> doInBackground() throws FileNotFoundException,
			XPathExpressionException, ParserConfigurationException,
			SAXException, IOException, JWNLException, InterruptedException {

		// set up observer
		_main.getMyTokenizer().getMyProgressKeeper().register(this);
		setSubject(_main.getMyTokenizer().getMyProgressKeeper());
		// run the model
		_main.runModel(_filePath);
		// get and convert values
		ArrayList<String> stringArray = convertValues(_main.getValues());
		// reset _main
		// return values
		return stringArray;
	}

	// Implement done
	@Override
	protected void done() {
		super.done();
		// We expect to be on the UI thread here.

		// Notify our observers - we're on the UI thread so this is safe to do.
		fireIsDoneChanged();

	}

	public void runModel(String file) {
		_filePath = file;
		execute();
	}

	public Subject getChartSubject() {
		return _chartSubject;
	}

	// Private methods to make doInBackground Simpler
	private ArrayList<String> convertValues(ArrayList<Object> objectArray)
			throws FileNotFoundException, XPathExpressionException,
			ParserConfigurationException, SAXException, IOException,
			JWNLException {

		ArrayList<String> stringArray = new ArrayList<>();
		// convert values to strings and add to stringArray
		// first the ints

		for (int i = 0; i < 5; i++) {
			stringArray.add(convertToString(objectArray, i));
		}
		// then the double
		Double ratingInt = (Double) objectArray.get(5);
		String rating = ratingInt.toString();
		stringArray.add(rating);
		// then the array
		ArrayList<String> wordArray = (ArrayList<String>) objectArray.get(6);
		String words = "";
		for (String word : wordArray) {
			words = words + " " + word;
		}
		stringArray.add(words);
		return stringArray;

	}

	private String convertToString(ArrayList<Object> list, int i) {
		Integer x = (Integer) list.get(i);
		String converted = x.toString();
		return converted;
	}

	// Implementaion of Observer
	/**
	 * Listens to sentimentBarometer.Model.ProgressKeeper and updates the progress
	 * 
	 * @see sentimentBarometer.model.ProgressKeeper
	 */
	@Override
	public void update() {
		if (_progressKeeper.getUpdate(this) != null) {
			int percentage = (int) _progressKeeper.getUpdate(this);
			setProgress(percentage);
		} else {
			System.out.println("NO UPDATE");
		}
	}

	@Override
	public void setSubject(Subject sub) {
		_progressKeeper = sub;
	}
}
