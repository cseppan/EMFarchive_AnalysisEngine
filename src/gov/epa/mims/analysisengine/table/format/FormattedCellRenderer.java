package gov.epa.mims.analysisengine.table.format;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class FormattedCellRenderer extends JLabel implements TableCellRenderer, HasFormatter {

	/** The formatter to use when displaying values. Could be null. */
	protected Format format = null;

	/** The Font to use when displaying values. */
	protected Font font = null;

	/** The horizontal alignment to use when displaying values. */
	protected int horizontalAlignment = SwingConstants.RIGHT;

	/** The color for the background when the cell is selceted. */
	protected Color selectionColor = UIManager.getDefaults().getColor("TextField.selectionBackground");

	/** The non-selected background color. */
	protected Color backgroundColor = Color.white;

	/** A null formatter to use when there is no formatter in a column. */
	public static final NullFormatter nullFormatter = new NullFormatter();

	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	private static boolean printedMessage = false;
	
	public FormattedCellRenderer(Format format, int horizontalAlignment) {
		if (format == null) {
			format = nullFormatter;
		}
		this.format = format;
		font = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
		setFont(font);
		setHorizontalAlignment(horizontalAlignment);
		setOpaque(true);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	public Format getFormat() {
		return format;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (isSelected) {
			super.setBackground(table.getSelectionBackground());
		} else {
			super.setBackground(table.getBackground());
		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				super.setForeground(UIManager.getColor("Table.focusCellForeground"));
				super.setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		} else {
			setBorder(getNoFocusBorder());
		}

		try
		{
			setValue(value, format);
		}
		catch (Throwable t)
		{
		  setText((value == null) ? "" : value.toString());
		  if (!printedMessage)
		  {
			  System.out.println("Problem setting value for row,col="+row+","+column+", value="+value);
			  printedMessage = true;
		  }
		}

		return this;

	}

	private static Border getNoFocusBorder() {
		if (System.getSecurityManager() != null) {
			return SAFE_NO_FOCUS_BORDER;
		}
		return noFocusBorder;
	}

	protected void setValue(Object value, Format format) {
		if (format == null)
			setText((value == null) ? "" : value.toString());
		else
			setText(format.format(value));
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		backgroundColor = c;
	}

	public void setFormat(Format newFormat) {
		format = newFormat;
	}

	public static FormattedCellRenderer getDefaultFormattedCellRenderer(Class columnClass) {
		Format format = getDefaultFormat(columnClass);
		int alignment = getDefaultAlignment(columnClass);
		FormattedCellRenderer result = new FormattedCellRenderer(format, alignment);
		result.setBackground(Color.white);
		return result;
	}

	public static int getDefaultAlignment(Class columnClass) {
		if (columnClass.equals(Double.class) || columnClass.equals(Float.class) || columnClass.equals(Integer.class)) {
			return SwingConstants.RIGHT;
		}
		return SwingConstants.LEFT;
	}

	public static Format getDefaultFormat(Class columnClass) {
		if (columnClass.equals(Integer.class)) {
			return new DecimalFormat("0");
		} else if (columnClass.equals(Double.class) || columnClass.equals(Float.class)) {
			SignificantDigitsFormat sigFormat = new SignificantDigitsFormat();
			sigFormat.applyPattern(sigFormat.toPattern());
			return sigFormat;
		} else if (columnClass.equals(Date.class) || columnClass.equals(Timestamp.class)) {
			return new SimpleDateFormat("MM/dd/yy HH:mm");
		} else {
			return null;
		}
	}

}
