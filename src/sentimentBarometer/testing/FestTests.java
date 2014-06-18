package sentimentBarometer.testing;

import static org.junit.Assert.*;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.timing.Condition;
import org.fest.swing.timing.Pause;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;




import sentimentBarometer.view.GUI;

public class FestTests {

	private FrameFixture window;

	public FestTests() {
	}

	@BeforeClass
	public static void setUpClass() {
		// Enable checking that GUI-interaction is happening on the correct
		// thread.
		FailOnThreadViolationRepaintManager.install();
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		GUI frame = GuiActionRunner
				.execute(new GuiQuery<GUI>() {
					protected GUI executeInEDT() {
						return new GUI();
					}
				});
		window = new FrameFixture(frame);
		window.show(); // shows the frame to test
	}

	@Test
	public void guiTest1() throws InterruptedException {
		//click analyse button
		window.button("btnAnalyse").click();
		//check error message
		window.label("lblWarning").requireText("Please select a valid file");
	}
	@Test
	public void guiTest2() {
		window.textBox("txtFilename").setText("C:/Users/Tim/Documents/testData.txt");
		window.button("btnAnalyse").click();
		//pause to let program run
		Pause.pause(new WaitForButtonEnabledCondition(window.button("btnAnalyse")));
		window.button("OKButton").requireEnabled();
	}

	@Test
	public void guiTest3(){
		//click the open button
		window.button("btnOpen").click();
		//get fixture for filechooser
		JFileChooserFixture fileChooser = window.fileChooser();
        // Enter a filename.
        fileChooser.fileNameTextBox().setText("C:/Users/Tim/Documents/testData.doc");
        // Click ok.
        fileChooser.approve();
        //try to run
        window.button("btnAnalyse").click();
        //check validation kicked in
        window.label("lblWarning").requireText("Please select a valid file");
	}
	@After
	public void tearDown() {
		window.cleanUp();
	}
	
	/**
     * A {@linnk Condition} that waits for a {@link JButton} to become enabled.
     */
	private class WaitForButtonEnabledCondition extends Condition {
        private final JButtonFixture button;

        private WaitForButtonEnabledCondition(JButtonFixture button) {
            //description for parent class
            super("Waits the button to be enabled.");
            this.button = button;
        }

        @Override
        public boolean test() {
            return this.button.component().isEnabled();
        }
    }

	
}



