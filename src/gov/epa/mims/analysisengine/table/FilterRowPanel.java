package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.ChildHasChangedListener;
import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.HasChangedListener;
import gov.epa.mims.analysisengine.gui.UserInteractor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.Format;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JPanel;

/*
 * FilterRowPanel.java
 *
 * Created on April 22, 2005, 12:56 PM
 * @author  Parthee R Partheepan
 * @version $Id: FilterRowPanel.java,v 1.3 2005/09/21 19:17:09 parthee Exp $
 */
public class FilterRowPanel extends JPanel implements ChildHasChangedListener {

	/** The FilterPanel that handles data display and editing. */
	protected FilterPanel filterPanel = null;

	/** The list of all column names for the filter table */
	protected String[] allColumnNames = null;

	protected Class[] allColumnClasses = null;

	/** The filter criteria that will be created from this GUI. */
	protected FilterCriteria filterCriteria = null;

	/**
	 * The list of all formatters in the table. Used to parse values entered by
	 * the user for numbers and dates.
	 */
	protected Format[] allFormats = null;

	/**
	 * A hashtable to convert column names to column information. The key is the
	 * column name and the data is a Duplex (see end of this class) with Integer
	 * index, Class columnClass
	 */
	protected Hashtable nameToInfoHash = new Hashtable();

	/**
	 * An array of booleans whose indices match the operation numbers. The value
	 * is true if the operation is a String only operation (like starsWith,
	 * contains, endsWith...) and false otherwise.
	 */
	protected static boolean[] stringOperations = null;

	// Set up a boolean array that returns true for String onlyt operations and
	// false for other operations. The index in the array is one of the
	// operation constants from FilterCriteria. This is used to decide when
	// to convert non-String data to a String for comparisons. ie: convert a
	// Date to a String for a "sTARTS_WITH" comparison.
	static {
		stringOperations = new boolean[FilterCriteria.OPERATION_STRINGS.length];
		Arrays.fill(stringOperations, 0, FilterCriteria.NOT_EQUAL, false);
		Arrays.fill(stringOperations, FilterCriteria.STARTS_WITH,
				FilterCriteria.NOT_ENDS_WITH + 1, true);
	}

	private Component parentComponent = null;

	private boolean hasChanged = false;

	/** Creates a new instance of FilterRowPanel */
	public FilterRowPanel(String[] columnNames, Class[] columnClasses,
			FilterCriteria filterCriteria) {
		this.allColumnNames = columnNames;
		this.allColumnClasses = columnClasses;
		this.filterCriteria = filterCriteria;
		if ((allColumnClasses != null && allColumnNames != null)
				&& (allColumnClasses.length != allColumnClasses.length)) {
			throw new IllegalArgumentException(
					"FilterCriteria variables "
							+ "allColumnNames and allColumnClasses should have equal length");
		}
		// this.allFormats = columnFormats;
		buildHashtable(columnClasses);
		initialize();
		initGUIFromModel();
	}

	public FilterRowPanel(FilterCriteria filterCriteria) {
		this.allColumnNames = filterCriteria.getAllColumnNames();
		this.allColumnClasses = filterCriteria.getAllColumnClasses();
		this.filterCriteria = filterCriteria;
		if ((allColumnClasses == null) || (allColumnNames == null)
				|| (allColumnClasses.length != allColumnClasses.length)) {
			throw new IllegalArgumentException(
					"FilterCriteria variables "
							+ "allColumnNames and allColumnClasses not initialized properly");
		}
		buildHashtable(allColumnClasses);
		initialize();
		initGUIFromModel();
	}// FilterRowPanel

	/**
	 * Build the hastable that converts from column names to column classes,
	 * formats an indices.
	 */
	protected void buildHashtable(Class[] columnClasses) {
		for (int i = 0; i < allColumnNames.length; i++) {
			nameToInfoHash.put(allColumnNames[i], new Duplex(new Integer(i),
					columnClasses[i]));
		} // for(i)
	} // buildHashtable()

	/**
	 * Return the filter criteria based on this GUI. NOTE calling
	 * saveGUIValuesToModel is redundant if you call it before calling this
	 * method but it's done as precautionary measure => not a good design!!!
	 * 
	 * @return FilterCriteria that reflects the information in the GUI.
	 */
	public FilterCriteria getFilterCriteria() {
		saveGUIValuesToModel();
		return filterCriteria;
	} // getFilterCriteria()

	/**
	 * Build the GUI.
	 */
	public void initialize() {
		filterPanel = new FilterPanel();
		filterPanel.setParentComponent(this);
		this.setLayout(new BorderLayout());
		this.add(filterPanel);
	} // initialize()

	/**
	 * Initialize the GUI based onthe FilterCriteria passed in.
	 */
	public void initGUIFromModel() {
		// Set the column names and the available operations no matter what.
		filterPanel.setFilteringChoices(allColumnNames,
				FilterCriteria.OPERATION_STRINGS, 0, 0);
		// Only populate the filter criteria if we have one.
		if (filterCriteria != null) {
			filterPanel.setUseAnd(filterCriteria.compareWithAnd);
			int numCriteria = filterCriteria.getCriteriaCount();
			String[][] filterData = new String[numCriteria][];
			for (int i = 0; i < numCriteria; i++) {
				String[] rowData = new String[3];
				rowData[0] = filterCriteria.getColumnName(i);
				rowData[1] = filterCriteria.getOperationString(i);
				Duplex duplex = (Duplex) nameToInfoHash.get(rowData[0]);
				Format format = filterCriteria.getFormat(rowData[0]);
				if (format == null) {
					format = FormattedCellRenderer
							.getDefaultFormat(duplex.columnClass);
				}
				// if still it's null then it's column type os column class is
				// String
				Integer operation = (Integer) FilterCriteria.symbolToConstantHash
						.get(rowData[1]);
				if (format != null && !isStringOperation(operation.intValue())) {
					// System.out.println("duplex.ColumnClass="+duplex.columnClass);
					// System.out.println("filterCriteria.getValue(i)='"+filterCriteria.getValue(i)+
					// "'");
					// System.out.println("filterCriteria.getValue(i).getClass="+filterCriteria.getValue(i).getClass());
					// System.out.println("format="+format.getClass());
					rowData[2] = format.format(filterCriteria.getValue(i));
				} else {
					rowData[2] = (String) filterCriteria.getValue(i);
				}
				filterData[i] = rowData;
			}

			filterPanel.setTableData(filterData);
			boolean applyFilter = filterCriteria.isApplyFilters();
			filterPanel.setApplyFilter(applyFilter);
		} // if (filterCriteria != null)
		else {
			filterPanel.setApplyFilter(true);
		}
		filterPanel.setHasChanged(false);
	} // initGUIFrommodel()

	public void initGUIFromModel(FilterCriteria filterCriteria) {
		this.filterCriteria = filterCriteria;
		initGUIFromModel();
	}

	/**
	 * For the operations that have to be performed on a String return true.
	 */
	protected boolean isStringOperation(int operation) {
		return stringOperations[operation];
	} // isStringOperation()

	private boolean isStringColumnClass(String colName) {
		int index = -1;
		for (int i = 0; i < allColumnNames.length; i++) {
			if (colName.equalsIgnoreCase(allColumnNames[i])) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			throw new IllegalArgumentException(
					"The argument is not in the 'allColumnNames' array");
		}
		return (allColumnClasses[index] == String.class) ? true : false;
	}

	/**
	 * Save the data from the GUI to a FilterCriteria.
	 */
	public boolean saveGUIValuesToModel() {
		String[][] filterData = filterPanel.getTableData();
		boolean applyFilters = filterPanel.isApplyFilters();
		int rowCount = filterData.length;
		String[] names = new String[rowCount];
		int[] operations = new int[rowCount];
		Comparable[] values = new Comparable[rowCount];
		Format[] formats = new Format[rowCount];
		/* try { */
		for (int r = 0; r < rowCount; r++) {
			names[r] = filterData[r][FilterPanel.NAME_COLUMN];
			String operStr = filterData[r][FilterPanel.OPERATION_COLUMN];
			Integer operation = (Integer) FilterCriteria.symbolToConstantHash
					.get(operStr);
			operations[r] = operation.intValue();

			Duplex duplex = (Duplex) nameToInfoHash.get(names[r]);
			if (duplex == null) {
				DefaultUserInteractor.get().notify(
						this,
						"Cannot Filter on Hidden Column",
						"In order to filter using the " + names[r]
								+ " column,\n"
								+ " you must show that column in the table.",
						UserInteractor.ERROR);
				return false;
			} // else
			formats[r] = FormattedCellRenderer
					.getDefaultFormat(duplex.columnClass);

			// Parse the entered value based on the formatter available.
			String cellValue = filterData[r][FilterPanel.VALUE_COLUMN];
			if (cellValue == null || cellValue.trim().length() == 0) {
				DefaultUserInteractor.get().notify(this,
						"Empty comparison value",
						"Please enter a value into the value column.",
						UserInteractor.ERROR);
				// shouldContinueClosing = false;
				return false;
			}
			cellValue = cellValue.trim();
			// Turn the value into a String if we are doing a String only
			// operation.
			// Set the value to be a formatted String.
			if (isStringOperation(operation.intValue())) // &&
			// isStringColumnClass(names[r]))
			{
				values[r] = cellValue;
			} else {
				// We have to check the column type because the
				// DecimalFormat
				// returns a Long for *anything* that can be parsed as an
				// integer.
				// See the DecimalFormat.parseObject() javadoc. :-(
				// WARNING THIS ON THE ASSUMPTION THAT ONLY STRING COLULMNS
				// HAVE NULL FORMATS
				try {
					if (duplex.columnClass == Integer.class) {
						Number numObject = (Number) formats[r]
								.parseObject(cellValue);
						values[r] = new Integer(numObject.intValue());
					} else if (duplex.columnClass == Double.class) {
						if (cellValue.equals("NaN")
								|| cellValue.equals("Double.NaN"))
							values[r] = new Double(Double.NaN);
						else {
							Number numObject = (Number) formats[r]
									.parseObject(cellValue);
							values[r] = new Double(numObject.doubleValue());
						}
					} else if (duplex.columnClass == Long.class) {
						Number numObject = (Number) formats[r]
								.parseObject(cellValue);
						values[r] = new Long(numObject.longValue());
					} else if (duplex.columnClass == Float.class) {
						Number numObject = (Number) formats[r]
								.parseObject(cellValue);
						values[r] = new Float(numObject.floatValue());
					} else if (duplex.columnClass == Date.class) {
						values[r] = (Date) formats[r].parseObject(cellValue);
					} else if (duplex.columnClass == Boolean.class) {
						Boolean bool = new Boolean(cellValue);
						// System.out.println("bool=" +
						// bool.booleanValue());
						values[r] = new MyBoolean(bool);
					} else {
						values[r] = cellValue;
					}
				} catch (ParseException e) {
					values[r] = cellValue; // treat as a string value if it's
											// not appropriate type
				}
			} // else
		} // for(r)
		// If the FilterCriteria is null, create a new one.
		if (filterCriteria == null) {
			boolean[] show = new boolean[allColumnNames.length];
			Arrays.fill(show, true);
			filterCriteria = new FilterCriteria(allColumnNames, /* allFormats, */
			show);
		}
		filterCriteria.setRowCriteria(names, operations, values,
		/* formats, */filterPanel.getUseAnd());
		filterCriteria.setApplyFilters(applyFilters);
		filterCriteria.setAllColumnClasses(allColumnClasses);
		/*
		 * } // try catch (ParseException pe) {
		 * DefaultUserInteractor.get().notifyOfException(this, "Invalid
		 * filtering value", pe, UserInteractor.ERROR); return false; }
		 */
		filterPanel.setHasChanged(false);
		return true;
	} // saveGUIValuesToModel()

	public void setEnabled(boolean enabled) {
		filterPanel.setEnabled(enabled, enabled);
	}

	public boolean isApplyFilters() {
		return filterPanel.isApplyFilters();
	}

	public void removeFilterColumns(java.util.List colNames) {
		filterPanel.removeFilterColumns(colNames);
	}

	public void addFilterRowPanelData(String[][] data) {
		filterPanel.setTableData(data);
	}

	public String[][] getFilterPanelData() {
		return filterPanel.getTableData();
	}

	public java.util.List getFilterColumnNames() {
		return filterPanel.getFilterColumnNames();
	}

	public void setParentComponent(Component parent) {
		parentComponent = parent;
	}

	/**
	 * IF hasChanged is false then update calls the update method in the parent
	 * component otherwise it changes the hasChanged value to true
	 */
	public void update() {
		if (parentComponent != null
				&& parentComponent instanceof HasChangedListener) {
			if (!hasChanged) {
				((HasChangedListener) parentComponent).update();
			}
			hasChanged = true;
		}
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
		filterPanel.setHasChanged(hasChanged);
	}

	// public void setApplyFilters(boolean applyFilters)
	// {
	// filterPanel.setApplyFilter(applyFilters);
	// }

	/**
	 * Class to hold two items about each column in the table.
	 */
	class Duplex {
		Integer index = null;

		Class columnClass = null;

		// Format format = null;
		public Duplex(Integer index, Class columnClass) {
			this.index = index;
			this.columnClass = columnClass;
		} // Triplet()
	} // class Triplet

}
