package gov.epa.mims.analysisengine.table;

import javax.swing.table.*;

/**
 * <p>Description: A table column that contains multiple rows of header
 * information. Used with the MultiRowHeaderRenderer. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UNC - CEP</p>
 * @author Daniel Gatti
 * @version $Id: MultiRowTableColumn.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */
public class MultiRowTableColumn extends TableColumn
{
   /** The column headers. */
   protected String[] columnHeaders = null;

   public MultiRowTableColumn(int index, String[] headers)
   {
      super(index);
      columnHeaders  = headers;
      headerRenderer = new MultiRowHeaderRenderer(headers);
   } // MultiRowTableColumn()


   /**
    * Return the String[] of columnHeaders.
    * @return String[]
    */
   public Object getHeaderValue()
   {
      return columnHeaders;
   } // getHeaderValue()


   /**
    * Return all of the column headers concatenated with '|' between them.
    * @return String[]
    */
   public Object getIdentifier()
   {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < columnHeaders.length - 1; i++)
      {
         sb.append(columnHeaders[i]);
         sb.append('|');
      }
      sb.append(columnHeaders[columnHeaders.length - 1]);

      return sb.toString();
   } // getIdentifier()


   /**
    * Return the special MultiRowHeaderRenderer for this column.
    * @return MultiRowHeaderRenderer
    */
   public TableCellRenderer getTableCellRenderer()
   {
      return headerRenderer;
   } // getTableCellRenderer()
} //class MultiRowTableColumn

