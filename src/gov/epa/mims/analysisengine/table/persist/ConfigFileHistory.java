package gov.epa.mims.analysisengine.table.persist;

import gov.epa.mims.analysisengine.gui.GUIUserInteractor;
import gov.epa.mims.analysisengine.gui.UserInteractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ConfigFileHistory extends DefaultTableModel {

	private File file;

	private Vector columnNames;

	private String fileName;

	public ConfigFileHistory(String fileName) throws Exception {
		this.fileName = fileName(fileName);
		columnNames = new Vector(Arrays.asList(new String[] { "File Name" }));
		initialize();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	private void initialize() throws Exception {
		setColumnIdentifiers(columnNames);
		file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new Exception("Could not create a new config file: " + e.getMessage());
			}
		} else
			loadHistory();
	}

	private String fileName(String fileName) {
		String recentConfigFileName = System.getProperty("RecentConfigFiles");
		if (recentConfigFileName == null)
			recentConfigFileName = System.getProperty("user.dir") + File.separator + fileName;
		return recentConfigFileName;
	}

	private void loadHistory() {
		Vector fileData = new Vector();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				Vector row = new Vector();
				row.add(line);
				fileData.add(row);
			}
			setDataVector(fileData, columnNames);
		} catch (Exception e) {
			new GUIUserInteractor().notify(null, "Error with Recent Files List", e.getMessage(), UserInteractor.ERROR);
		}
	}

	public void addToHistory(String fullFileName) {
		addRow(new String[] { fullFileName });
	}

	public void saveHistory() throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		try {
			Vector data = getDataVector();
			for (int i = 0; i < data.size(); i++) {
				Vector info = (Vector) data.get(i);
				writer.println(info.get(0));
			}
		} finally {
			writer.close();
		}
	}

}
