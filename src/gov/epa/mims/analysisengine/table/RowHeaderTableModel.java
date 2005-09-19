package gov.epa.mims.analysisengine.table;

import javax.swing.event.*;
import javax.swing.table.*;

/**
 * A table model that add a row number in the left hand column. All other
 * rows are shifted to the right by one row.  This class also supports
 * optional units.
 *
 * @author  Daniel Gatti
 * @version $Id: RowHeaderTableModel.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */
public class RowHeaderTableModel
      extends MultiRowHeaderTableModel
      implements TableModelListener
{
   private MultiRowHeaderTableModel underlyingModel;

   /** Creates a new instance of RowHeaderTableModel */
   public RowHeaderTableModel(MultiRowHeaderTableModel model)
   {
      super(model);

      if (model == null)
         throw new IllegalArgumentException(
               "The underlying data model cannot be null in SortingTableModel().");

      underlyingModel = model;
      underlyingModel.addTableModelListener(this);
   } // RowHeaderTableModel()


   public Class getColumnClass(int col)
   {
//System.out.print("Row : getColumnClass("+col+")");
      if (col <= 0)
      {
//System.out.println(" " + String.class);
         return String.class;
      }
      else
      {
//System.out.println(" " + underlyingModel.getColumnClass(col-1));
         return underlyingModel.getColumnClass(col-1);
      }
   } // getColumnClass()



   /**
    * Return the number of columns in the underlying model plus one.
    */
   public int getColumnCount()
   {
      return underlyingModel.getColumnCount() + 1;
   } // getColumnCount()


   /**
    * Return the list of headers for the given column.
    * Pass through to the underlying model.
    * @param col int that is the column index.
    * @return String[] with the headers for one column. Could be "" if
    *  there are no column headers.
    */
   public String[] getColumnHeaders(int col)
   {
      return underlyingModel.getColumnHeaders(col);
   } // getColumnHeaders()


   public String getColumnName(int col)
   {
      if (col == 0)
      {
         return "Row";
      }
      else
      {
         return underlyingModel.getColumnName(col-1);
      }
   }


   public int getRowCount()
   {
      return underlyingModel.getRowCount();
   } // getRowCount()



   public Object getValueAt(int row, int col)
   {
//System.out.println("RowHeaderTableModel.getValueAt("+row+", "+col+")");      
      // Return the 1-based row for column 1.
      if (col == 0)
      {
         if (row < 0)
         {
            return "Row";
         }
         else
         {
            row++;
            return Integer.toString(row);
         }
      }
      // Return the underlying data in column - 1.
      else
      {
         return underlyingModel.getValueAt(row, col-1);
      }
   } // getValueAt()

   /**Editing is allowed only if the column type is boolean */
   public void setValueAt(Object aValue,int row, int col){
      underlyingModel.setValueAt(aValue, row, col-1);
   }
   
   public void tableChanged(TableModelEvent e)
   {
      fireTableChanged(e);
   }
   
   public int getBaseModelRowIndex(int rowIndex)
   {
      return underlyingModel.getBaseModelRowIndex(rowIndex);
   } // getValueAt()
} // class RowHeaderTableModel

