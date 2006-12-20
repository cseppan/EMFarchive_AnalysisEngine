package gov.epa.mims.analysisengine.table.persist;

import gov.epa.mims.analysisengine.gui.ScreenUtils;
import gov.epa.mims.analysisengine.table.TableApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ConfigFileHistoryDialog extends JDialog {

	private JTable table;

	private TableApp tableApp;

	public ConfigFileHistoryDialog(TableApp tableApp, ConfigFileHistory history) {
		super(tableApp);
		setTitle("Recently Used Config Files");
		this.tableApp = tableApp;
		setLayout(history);
		pack();
		setLocation(ScreenUtils.getPointToCenter(this));
	}

	private void setLayout(ConfigFileHistory history) {
		this.table = table(history);
		table.setRowHeight(16);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	    TableColumn col = table.getColumnModel().getColumn(0);
	    col.setPreferredWidth(200);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(300, 300));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.add(pane);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(mainPanel);
		contentPane.add(buttonPanel(), BorderLayout.SOUTH);
	}

	private JPanel buttonPanel() {
		JButton importButton = new JButton("Import");
		importButton.addActionListener(importListener());

		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(removeListener());

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(closeListener());

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(importButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(closeButton);

		return buttonPanel;
	}

	private ActionListener importListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doImport();
			}
		};
	}

	private ActionListener removeListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemove();
			}
		};
	}

	private ActionListener closeListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClose();
			}
		};
	}

	private JTable table(ConfigFileHistory history) {
		JTable table = new JTable(history);
		return table;
	}

	protected void doRemove() {
		int index = table.getSelectedRow();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.removeRow(index);
	}

	protected void doClose() {
		setVisible(false);
		dispose();
	}

	protected void doImport() {
		int index = table.getSelectedRow();
		String fileName = (String) table.getValueAt(index, 0);
		String format = (String) table.getValueAt(index, 1);
		boolean binaryFormat = true;
		if (format.equals("xml"))
			binaryFormat = false;

		tableApp.showLoadConfigGUI(binaryFormat, new File(fileName));
	}

	public static void showGUI(TableApp app, ConfigFileHistory history) {
		ConfigFileHistoryDialog hisDialog = new ConfigFileHistoryDialog(app, history);
		hisDialog.setVisible(true);

	}

}
