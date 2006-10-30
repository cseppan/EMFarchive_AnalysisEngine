package gov.epa.mims.analysisengine.table.persist;

import gov.epa.mims.analysisengine.gui.GUIUserInteractor;
import gov.epa.mims.analysisengine.gui.UserInteractor;
import gov.epa.mims.analysisengine.table.SortFilterTablePanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

class LoadConfigAction extends AbstractAction {

	private SortFilterTablePanel parent = null;

	public LoadConfigAction(SortFilterTablePanel parent) {
		super("Load Configuration", null);
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		do {
			int returnVal = chooser.showOpenDialog(parent);
			try {
				if (returnVal == JFileChooser.CANCEL_OPTION) {
					return;
				}
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					parent.showLoadConfigGUI(chooser.getSelectedFile());
					return;
				}
			} catch (Exception ex) {
				new GUIUserInteractor().notify(parent, "Error loading file", ex.getMessage(), UserInteractor.ERROR);
			}
		} while (true);
	}
}