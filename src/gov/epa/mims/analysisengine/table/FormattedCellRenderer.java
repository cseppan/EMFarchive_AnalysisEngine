package gov.epa.mims.analysisengine.table;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import java.util.Date;
import gov.epa.mims.analysisengine.gui.OptionDialog;



/**
 * <p>Description: A TabelCellRenderer that can hold a formatter to format
 * the data before presentation. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UNC - CEP</p>
 * @author Daniel Gatti
 * @version $Id: FormattedCellRenderer.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */
public class FormattedCellRenderer extends JLabel
      implements TableCellRenderer, HasFormatter
{
   /** The formatter to use when displaying values. Could be null.*/
   protected Format format = null;

   /** The Font to use when displaying values.*/
   protected Font font = null;

   /** The horizontal alignmentto use when displaying values.*/
   protected int horizontalAlignment = SwingConstants.RIGHT;
   
   /** The color for the background when the cell is selceted. */
   protected Color selectionColor = UIManager.getDefaults().getColor(
            "TextField.selectionBackground");

   /** The non-selected background color. */
   protected Color backgroundColor = Color.white;

  /** A null formatter to use when there is no formatter in a column. */
   public static final NullFormatter nullFormatter = new NullFormatter();

   /**
    * Constructor.
    * @param format Format touse to format the cells. Could be null in which case
    *   we won't do any formatting.
    */
   public FormattedCellRenderer(Format format,int horizontalAlignment)
   {
      if (format == null)
      {
         format = nullFormatter;
      }
      this.format = format;
      font = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
      setFont(font);
      setHorizontalAlignment(horizontalAlignment);
      setOpaque(true);
      this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
   } // FormattedCellRenderer()


   /**
   * Return the formatter being ued by this renderer.
    * This is used by the RowHeaderTable.setColumnWidths method.
    * @return Format that is used to render values in this GUI.
    */
   public Format getFormat()
   {
      return format;
   } // getFormat()


   /**
    * Return the rendering component with the value in it.
    */
   public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column)
   {
//System.out.println("isSelected("+row+", "+column+") = " + isSelected);
      if (isSelected)
      {
         setBackground(selectionColor);
      }
      else
      {
         setBackground(backgroundColor);
      }
      // Return and empty cell if there is no value.
      if (value == null)
      {
         setText("");
      }
      //RP: Function below handled by SignificantDigitsFormat.format()
//      else if( table.getColumnClass(column).equals(Double.class)
//         && ((Double)table.getValueAt(row,column)).isNaN())
//      {
//         setText("N/A");
//      }
      else if (format == null)
      {
         setText(value.toString());
      }
      else
      {
         setText(format.format(value));
      }
     return this;
   } // getTableCellRendererComponent()





   /**
   * Save the non-selected background color.
    */
   public void setBackground(Color c)
   {
      super.setBackground(c);
      backgroundColor = c;
   }


   /**
    * Set the format to be used by this renderer.
    * @param newFormat Format to be used by this renderer.
    */
   public void setFormat(Format newFormat)
   {
      format = newFormat;
   } // setFormat()

   /** returns an instance of FormattedCellRenderer given the class type
     * @param columnClass Class
     * @return FormattedCellRenderer
     */
   public static FormattedCellRenderer getDefaultFormattedCellRenderer(Class columnClass)
   {
      Format format = getDefaultFormat(columnClass);
      int alignment = getDefaultAlignment(columnClass);
      FormattedCellRenderer result = new FormattedCellRenderer(format,alignment);
      result.setBackground(Color.white);
      return result;
   }

   public static int getDefaultAlignment(Class columnClass)
   {
      if(columnClass.equals(Double.class)
      || columnClass.equals(Float.class)
      || columnClass.equals(Integer.class))
      {
         return SwingConstants.RIGHT;
      }
      else
      {
         return SwingConstants.LEFT;
      }
   }

   /** returns an instance of Format given the class type
     * @param columnClass Class
     * @return Format
     */
   public static Format getDefaultFormat(Class columnClass)
   {
        if (columnClass.equals(Integer.class))
        {
           //return new SignificantDigitsFormat("0");
           return new DecimalFormat("0");
         }
         else if (columnClass.equals(Double.class) || 
            columnClass.equals(Float.class))
         {
            SignificantDigitsFormat sigFormat = 
           
            new SignificantDigitsFormat();
            //sigFormat.setSelectedOption(SignificantDigitsFormat.SCIENTIFIC_FORMAT);
            //sigFormat.setNumberOfSignificantDigits(3);
            sigFormat.applyPattern(sigFormat.toPattern());
            return sigFormat;
          }
         else if (columnClass.equals(Date.class) || columnClass.equals(Timestamp.class))
         {
              return new SimpleDateFormat("MM/dd/yy HH:mm");
         }
         else
         {
            return null;
         }
    }

} // class FormattedCellRenderer



