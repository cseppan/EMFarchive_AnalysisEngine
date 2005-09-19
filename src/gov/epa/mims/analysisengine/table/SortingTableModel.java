
package gov.epa.mims.analysisengine.table;

import javax.swing.event.*;
import javax.swing.table.*;

/**
 * <p>A data model that sorts it's data in ascending and descending order.
 *   It can also sort by multiple columns. </p>
 * @author Daniel Gatti
 * @version $Id: SortingTableModel.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */
public class SortingTableModel
      extends MultiRowHeaderTableModel
      implements TableModelListener
{
   /** The underlying data model that contains the unsorted data. */
   private MultiRowHeaderTableModel underlyingModel = null;

   /** The array that maps the sorted data to the unsorted data. */
   private int[] sortingMap = null;

   /** True if the data should be sorted in ascending order. */
   private SortCriteria sortCriteria = null;

   /**
    * Constructor.
    * @param model MultiRowHeaderTableModel that contains the underlying unsorted
    * data. Cannot be null, but could be emtpy.
    */
   public SortingTableModel(MultiRowHeaderTableModel model)
   {
      super(model);
      if (model == null)
         throw new IllegalArgumentException(
               "The underlying data model cannot be null in SortingTableModel().");

      underlyingModel = model;
      underlyingModel.addTableModelListener(this);

      int size = getRowCount();
      sortingMap = new int[size];
      for (int i = 0; i < getRowCount(); i++)
         sortingMap[i] = i;
   } // SortingTableModel()


   public Class getColumnClass(int column)
   {
//System.out.println("getColumnClass(" + column+ ") : " + underlyingModel.getColumnClass(column));
      return underlyingModel.getColumnClass(column);
   } // getColumnClass()


   /**
    * Get the number of columns of data.
    * @return int that is the number of columns in the data model.
    */
   public int getColumnCount()
   {
//System.out.println("getColumnCount() : " + underlyingModel.getColumnCount());
      return underlyingModel.getColumnCount();
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


   public String getColumnName(int column)
   {
//System.out.println("getColumnName(" + column + ") : " + underlyingModel.getColumnName(column));
      return underlyingModel.getColumnName(column);
   } // getColumnName()


   /**
    * Return the number
    * @return
    */
   public int getRowCount()
   {
// System.out.println("sort : getRowCount() : " + underlyingModel.getRowCount());
      return underlyingModel.getRowCount();
   } // getRowCount()


   /**
    * Return the SortCriteria that is currently being applid to the data.
    * @return SortCriteria that is being applied (could be null)
    */
   public SortCriteria getSortCriteria()
   {
      return sortCriteria;
   } // getSortCriteria


   public Object getValueAt(int rowIndex, int columnIndex)
   {
//System.out.println("SortingTableModel.getValueAt(" + rowIndex + ", " + columnIndex + ") map length =" + sortingMap.length);
      return underlyingModel.getValueAt(sortingMap[rowIndex], columnIndex);
   } // getValueAt()
   
   public int getBaseModelRowIndex(int rowIndex)
   {
      return underlyingModel.getBaseModelRowIndex(sortingMap[rowIndex]);
   }


   /**
    * Sort the value in the table based on the column(s) given.
    * @param criteria SortCritria to use to sort the columns.
    * @param lastRow int that is the last row to sort.
    */
   public void sortTable(SortCriteria criteria, int lastRow)
   {
//System.out.println("sortTable()");
      this.sortCriteria = criteria;
      // Reset the map in case the underlying model has changed it's number
      // of rows.
      int size = underlyingModel.getRowCount();
      sortingMap = new int[size];
      for (int i = 0; i < getRowCount(); i++)
      {
         sortingMap[i] = i;
      }

      if (criteria != null)
      {
         // Now sort the columns that we have from the underlying model.
         String[] columnNames = criteria.getColumnNames();
         int[] columns        = new int[columnNames.length];
         for (int i = 0; i < columns.length; i++)
         {
            Integer intObj = (Integer)
               ((FilteringTableModel)underlyingModel).nameToIndexHash.get(columnNames[i]);
            columns[i] = intObj.intValue();
         } // for(i)

         boolean[] ascending     = criteria.getAscending();
         boolean[] caseSensitive = criteria.getCaseSensitive();
         // Sort the last column first to that the first column is sorted
         // when we get done.
         for (int i = columns.length - 1; i >= 0; --i)
         {
//System.out.println("sorting column "+ columns[i] + ((ascending[i])?" ascending":" descending"));
            mergeSort(0, lastRow, columns[i], ascending[i], caseSensitive[i]);
            if(getColumnClass(columns[i])==Double.class)
               moveNaNsDown(0, lastRow, columns[i]);
         } // for(i)
      }

      fireTableDataChanged();
   } // sortTable()

   private void moveNaNsDown(int firstElement, int lastElement,  int column)
   {
       boolean begin = true;
       int index = firstElement;
       int[] tempMap = new int[sortingMap.length];
       for(int i=0; i<tempMap.length; i++)
           tempMap[i]=sortingMap[i];
       while(index < lastElement && ((Double)getValueAt(index, column)).equals(new Double(Double.NaN)))
           index++;
       if(index==firstElement)
           begin =false;
           
       if(begin)
       {
           int i = index;
            while(i > firstElement)
                sortingMap[lastElement-(--i)] = tempMap[i];
           for(i=0; i<=(lastElement-index); i++)
               sortingMap[i]=tempMap[i+index];
       }
   }     

   /**
    * mergesort algorithm
    * @return
    */
   private void mergeSort(int start, int finish, int column, boolean ascending,
                          boolean caseSensitive)
   {
      if (start >= finish)
         return;
      int middle = (start + finish) / 2;
      mergeSort(start, middle, column, ascending, caseSensitive);
      mergeSort(middle + 1, finish, column, ascending, caseSensitive);
      merge(start, middle, finish, column, ascending, caseSensitive);
   } // mergeSort()


   /**
    * Merge the two lists into one.
    */
   private void merge(int start, int middle, int finish, int column,
                      boolean ascending, boolean caseSensitive)
   {
      int lower = start;
      int upper = middle + 1;
      int[] tmp = new int[finish - start + 1];
System.out.println("column class="+getColumnClass(column));
      if (ascending)
      {
         int i = 0;
         for (i = 0; i < tmp.length; i++)
         {
            if (lower > middle)
            {
               tmp[i] = sortingMap[upper++];
            } // if (lower > middle)
            else if (upper > finish)
            {
               tmp[i] = sortingMap[lower++];
            } // else if (upper > finish)
            else
            {
               Object comp1 = getValueAt(lower, column);
               Object     comp2 = getValueAt(upper, column);
               if (!caseSensitive && getColumnClass(column).equals(String.class))
               {
                  String s1 = comp1.toString();
                  String s2 = comp2.toString();
                  if (s1.compareToIgnoreCase(s2) <= 0)
                  {
                     tmp[i] = sortingMap[lower++];
                  } // if (s1.compareToIgnoreCase(s2) <= 0)
                  else
                  {
                     tmp[i] = sortingMap[upper++];
                  } // else
               } // if (caseSensitive)
               else if(getColumnClass(column).equals(Boolean.class)){
                  boolean c1 = ((Boolean)comp1).booleanValue();
                  boolean c2 = ((Boolean)comp2).booleanValue();
                  if((c2 && !c1)){
                      tmp[i] = sortingMap[lower++];
                  } // if (s1.compareToIgnoreCase(s2) >= 0)
                  else
                  {
                     tmp[i] = sortingMap[upper++];
                  } // else
               }

               else
               {
                  if (((Comparable)comp1).compareTo(comp2) <= 0)
                  {
                     tmp[i] = sortingMap[lower++];
                  } // if (comp1.compareTo(comp2) <= 0)
                  else
                  {
                     tmp[i] = sortingMap[upper++];
                  } // else
               } // else
            } // else
         } // for(i)
      } // if (ascending)
      else // descending
      {
         int i = 0;
         for (i = 0; i < tmp.length; i++)
         {
            if (lower > middle)
            {
               tmp[i] = sortingMap[upper++];
            } // if (lower > middle)
            else if (upper > finish)
            {
               tmp[i] = sortingMap[lower++];
            } // else if (upper > finish)
            else
            {
               Object comp1 = getValueAt(lower, column);
               Object     comp2 = getValueAt(upper, column);
               if (!caseSensitive && getColumnClass(column).equals(String.class))
               {
                  String s1 = comp1.toString();
                  String s2 = comp2.toString();
                  if (s1.compareToIgnoreCase(s2) >= 0)
                  {
                     tmp[i] = sortingMap[lower++];
                  } // if (s1.compareToIgnoreCase(s2) >= 0)
                  else
                  {
                     tmp[i] = sortingMap[upper++];
                  } // else
                  
               } // if (!caseSensitive)
               else if(getColumnClass(column).equals(Boolean.class)){
                  boolean c1 = ((Boolean)comp1).booleanValue();
                  boolean c2 = ((Boolean)comp2).booleanValue();
                  if(!(c2 && !c1)){
                      tmp[i] = sortingMap[lower++];
                  } // if (s1.compareToIgnoreCase(s2) >= 0)
                  else
                  {
                     tmp[i] = sortingMap[upper++];
                  } // else
               }
               else
               {
                  
                  if (((Comparable)comp1).compareTo(comp2) >= 0)
                  {
                     tmp[i] = sortingMap[lower++];
                  } // if (comp1.compareTo(comp2) >= 0)
                  else
                  {
                     tmp[i] = sortingMap[upper++];
                  } // else
               } // else
            } // else
         }// for (i)
      } // else

      System.arraycopy(tmp, 0, sortingMap, start, tmp.length);
   } // merge()


   public void setValueAt(Object value, int row, int column)
   {
//System.out.println("setValueAt(" + value + ", " + row + ", " + column +")");
      underlyingModel.setValueAt(value, sortingMap[row], column);
   } // setValueAt()


   /**
    * This is called when the underlying table model (FilterModel) has changed.
    * @param e TableModelEvent
    */
   public void tableChanged(TableModelEvent e)
   {
      // We need this method to synch up the sort model with the filter model below.
//System.out.println(e.getColumn() + " " + e.getFirstRow() + " " + e.getLastRow());
      // When all three of these are -1, it means that a column has been hidden
      // or added. In this case, we don't want to resort. If we try to, we might
      // try to sort on a column that has been hidden and that causes trouble.

      // Check to see if we will try to sort a column that has been hidden.
      // If so, don't sort.
      boolean shouldSort = true;
      if (sortCriteria != null)
      {
         FilteringTableModel filterModel = (FilteringTableModel)underlyingModel;
         String[] columnNames = sortCriteria.getColumnNames();
         Object obj = null;

         for (int i = 0; i < columnNames.length; i++)
         {
            obj = filterModel.nameToIndexHash.get(columnNames[i]);
            // If we didn't find the column name in the hashtable of columns shown
            // by the underlying filter model, then we're trying to sort a hidden
            // column. We *don't* want to do that.
            if (obj == null)
            {
               shouldSort = false;
               break;
            }
         } // for(i)
      } // if (sortCriteria != null)
//System.out.println("SortingTableModel.tableChanged() : shouldSort = " + shouldSort);
      if (shouldSort)
      {
         sortTable(sortCriteria, getRowCount() - 1);
      }
      fireTableChanged(e);
   }
   

 // tableChanged()
   

} // class SortingTableModel

