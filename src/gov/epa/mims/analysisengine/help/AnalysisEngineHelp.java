package gov.epa.mims.analysisengine.help;

import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.ScreenUtils;
import gov.epa.mims.analysisengine.gui.UserInteractor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.JHelp;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AnalysisEngineHelp extends JFrame {

	public static final String ANALYSIS_ENGINE_TARGET = "sensitivity_intro";

	private JHelp helpViewer = null;

	private static AnalysisEngineHelp emisviewHelp = null;
	
	public static final String ANALYSIS_ENGINE_HELPSET="ANALYSIS_ENGINE_HELPSET";

	private AnalysisEngineHelp() {
		setTitle("Analysis Engine Users Guide");
		helpViewer = createHelpViewer();
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(800,800));
		mainPanel.add(helpViewer);
		getContentPane().add(mainPanel);
		pack();
		setLocation(ScreenUtils.getPointToCenter(this));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}

	private JHelp createHelpViewer() {
		String value = System.getProperty(ANALYSIS_ENGINE_HELPSET);
		System.out.println("value -"+value);
		if(value == null || !new File(value).isFile() ||  !value.endsWith(".hs")){
			throw new RuntimeException("Please specify the emisview help set file in the batch file");
		}
		ClassLoader cl = AnalysisEngineHelp.class.getClassLoader();
		URL url = null;
		try {
			url = new URL("file:///"+value);
		} catch (MalformedURLException e1) {
			DefaultUserInteractor.get().notify(this, "Error",
					"The help set file does not exist in the location '"+value+"'", UserInteractor.ERROR);
			e1.printStackTrace();
		}
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
