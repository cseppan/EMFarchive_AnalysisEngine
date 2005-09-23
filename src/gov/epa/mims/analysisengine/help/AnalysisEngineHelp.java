package gov.epa.mims.analysisengine.help;

import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.ScreenUtils;
import gov.epa.mims.analysisengine.gui.UserInteractor;

import java.net.URL;

import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.JHelp;
import javax.swing.JFrame;

public class AnalysisEngineHelp extends JFrame {

	public static final String ANALYSIS_ENGINE_TARGET = "sensitivity_intro";

	private JHelp helpViewer = null;

	private static AnalysisEngineHelp emisviewHelp = null;

	private AnalysisEngineHelp() {
		setTitle("Analysis Engine Users Guide");
		helpViewer = createHelpViewer();
		getContentPane().add(helpViewer);
		pack();
		setLocation(ScreenUtils.getPointToCenter(this));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private JHelp createHelpViewer() {
		ClassLoader cl = AnalysisEngineHelp.class.getClassLoader();
		URL url = HelpSet.findHelpSet(cl,
				"gov/epa/mims/analysisengine/help/ae_help.hs");
		try {
			return new JHelp(new HelpSet(cl, url));
		} catch (HelpSetException e) {
			DefaultUserInteractor.get().notify(this, "Error",
					"Could not find the help set file", UserInteractor.ERROR);
			e.printStackTrace();
			return null;
		}
	}

	private void setCurrentTarget(String target) {
		helpViewer.setCurrentID(target);
	}

	public static void showHelp(String currentTarget) {
		if (emisviewHelp == null) {
			emisviewHelp = new AnalysisEngineHelp();
		}
		emisviewHelp.setCurrentTarget(currentTarget);
		emisviewHelp.setVisible(true);
	}

	public static void main(String[] arg) {
		AnalysisEngineHelp.showHelp(AnalysisEngineHelp.ANALYSIS_ENGINE_TARGET);
	}
}
