package gov.epa.mims.analysisengine.table.persist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gov.epa.mims.analysisengine.tree.Branch;
import gov.epa.mims.analysisengine.tree.DataSets;
import gov.epa.mims.analysisengine.tree.Node;
import gov.epa.mims.analysisengine.tree.Plot;
import gov.epa.mims.analysisengine.tree.ScatterPlot;

public class UpdateDataColumns {

	private DataSets newDataset;

	private Branch tree;

	public UpdateDataColumns(DataSets newDataset, Branch tree) {
		this.newDataset = newDataset;
		this.tree = tree;
	}

	public void update() throws Exception {
		Plot[] plots = plotsInConfig(tree);
		String[] keys = (String[]) newDataset.getKeys().toArray(new String[0]);

		for (int i = 0; i < plots.length; i++) {

			if (!(plots[i] instanceof ScatterPlot)) {
				Object[] retVal = new Object[1];
				retVal[0] = keys;
				plots[i].setDataSetKeys(retVal);
			} else {
				updateForScatterPlotsWithDataForXAxis(plots[i], keys);
			}
		}
	}

	private void updateForScatterPlotsWithDataForXAxis(Plot plot, String[] allNewKeys) throws Exception {

		String[] xKeys = plot.getKeys(0);
		// x axis dataset for scatter plot is optional and if key exist the length always 1
		String[] newYKeys = null;
		if (xKeys.length > 0) {
			checkForXKey(allNewKeys, xKeys[0]);
			newYKeys = createKeysListWithoutXKey(allNewKeys, xKeys[0]);
		} else {
			newYKeys = allNewKeys;
		}

		Object[] retVal = new Object[2];
		retVal[0] = xKeys;
		retVal[1] = newYKeys;
		plot.setDataSetKeys(retVal);
	}

	private void checkForXKey(String[] allKeysAvailable, String xKey) throws Exception {
		List allKeys = new ArrayList(Arrays.asList(allKeysAvailable));
		if (!allKeys.contains(xKey))
			throw new Exception("The data column '" + xKey + "' for x axis is not in the table");

	}

	private String[] createKeysListWithoutXKey(String[] allKeysAvailable, String xKey) {
		List allKeys = new ArrayList(Arrays.asList(allKeysAvailable));
		int index = allKeys.indexOf(xKey);
		if (index != -1)
			allKeys.remove(index);
		return (String[]) allKeys.toArray(new String[0]);
	}

	private Plot[] plotsInConfig(Branch tree) {
		List plots = new ArrayList();
		getPlotsFromTree(plots, tree);
		return (Plot[]) plots.toArray(new Plot[0]);
	}

	private void getPlotsFromTree(List plots, Node tree) {
		int childCount = tree.getChildCount();
		for (int i = 0; i < childCount; i++) {
			Node child = tree.getChild(i);
			if (child instanceof Plot) {
				plots.add(child);
				return;
			}
			getPlotsFromTree(plots, child);
		}
	}

}
