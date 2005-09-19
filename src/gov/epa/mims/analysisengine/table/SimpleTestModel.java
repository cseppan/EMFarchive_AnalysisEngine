package gov.epa.mims.analysisengine.table;

import javax.swing.table.DefaultTableModel;

/**
 * <p>Description: A simple table model that can be instantiated for testing. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Daniel Gatti
 * @version $Id: SimpleTestModel.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */
public class SimpleTestModel extends MultiRowHeaderTableModel
{
   Object[][] data = null;

   public SimpleTestModel(Object[][] data, String[] columnRowHeaders, String[][] columnHeaders)
   {
      super(columnRowHeaders, columnHeaders);
      this.data = data;
   } // TestModel()


   public int getColumnCount()
   {
      if (data != null)
      {
         return data[0].length;
      }
      else
      {
         return 0;
      }
   }

   public int getRowCount()
   {
      if (data != null)
      {
         return data.length;
      }
      else
      {
         return 0;
      }
   }

   public Object getValueAt(int row, int col)
   {
      return data[row][col];
   }
   
   public int getBaseModelRowIndex(int rowIndex)
   {
      return rowIndex;
   }
   
} // class SimpleTestModel

