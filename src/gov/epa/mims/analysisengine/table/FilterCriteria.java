package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.UserInteractor;

import java.text.Format;
import java.util.*;

/**
 * A set of criteria for filtering data in a table.
 * @author  Daniel Gatti
 * @version $Id: FilterCriteria.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */
public class FilterCriteria implements java.io.Serializable,Cloneable
{
   /** serial version UID */
   static final long serialVersionUID = 1;
   
   public static final String NO_FILTER = "No Filter";
   
//   private long id;
   
   public static final int STARTS_WITH           =  0;
   public static final int NOT_STARTS_WITH       =  1;
   public static final int CONTAINS              =  2;
   public static final int NOT_CONTAINS          =  3;
   public static final int ENDS_WITH             =  4;
   public static final int NOT_ENDS_WITH         =  5;
   public static final int EQUAL                 =  6;
   public static final int NOT_EQUAL             =  7;
   public static final int GREATER_THAN          =  8;
   public static final int LESS_THAN             =  9;
   public static final int GREATER_THAN_OR_EQUAL =  10;
   public static final int LESS_THAN_OR_EQUAL    =  11;
   
   

   public static final String[] OPERATION_STRINGS =
   {
       "starts with", "does not start with", "contains", "does not contain", "ends with",
      "does not end with", "=", "not =",">", "<", ">=", "<=" 
   };

   public static final Hashtable symbolToConstantHash = new Hashtable();
   public static final Hashtable constantToSymbolHash = new Hashtable();

   static
   {
      for (int i = 0; i < OPERATION_STRINGS.length; i++)
      {
         symbolToConstantHash.put(OPERATION_STRINGS[i], new Integer(i));
         constantToSymbolHash.put(new Integer(i), OPERATION_STRINGS[i]);
      }
   } // static

   /** The column names on which to perform the filter operation. */
   private String[] columnNames = null;

   /** The operation to perform on each value in the given column.
    * Must be one of the constants in this class. */
   private int[] operations = null;

   /** The value to which the table values should be compared.
    *  Could be a Date, String, Integer, Double... */
   private Comparable[] values = null;
   
   private Class[] allColumnClasses = null;
   
   
   /** The formatters that are used for each column. This is needed to handle
    * filtering on String operations for Number and Date classes. */
//   protected Format[] formats = null;

   /** The list of all column names in the main table.
    * Note that this array and the showColumns array must be the same length.*/
   private String[] allColumnNames = null;

   /** A hashtable that accepts column names as keys and returns Integers that
    * are the index of the column in the table. Used to find the column index
    * in the accept() method. */
//   private Hashtable columnNameToIndex = new Hashtable();

   /** True if we should compare all of the filter criteria with an "AND", and
    * false if we should compare all of the filter criteria with an "OR".*/
   protected boolean compareWithAnd = true;

   /** The boolean array that contains true for each column that we show.
    * Note that this array and the allColumnNames array must be the same length. */
   private boolean[] showColumns = null;

   /** the model of which the filter criteria will work */
   private transient FormatAndIndexInfoIfc model = null;
   
   private boolean applyFilters = true;
   
   

   public FilterCriteria()
   {
//       if(valuesList!= null && valuesList.size()>0)
//       {
//            int size = valuesList.size();
//            values = new Comparable[size];
//            for(int i=0; i< size; i++)
//            {
//               values[i] = (Comparable)valuesList.get(i);
//            }
//       }
   }
   /**
   * Constructor or filtering Rows.
    * @param columnName String[] that is the column to filter on.
    * @param operation int[] that is one of the operation contants.
    * @param value Comparable[] that is the filter cutoff.
    * @param formats Format[] with one formatter for each value.
    * @param allColumnNames String[] that is the list of all columns in the table.
    *  ( this is to keep the filter columns operations from seeing a null value. )
    * @param comparison boolean that is true if we should compare with "AND" and
    *  false if we should compare with "OR".
    */
   public FilterCriteria(String[] columnNames, int[] operations, Comparable[] values,
                     /*    Format[] formats, */String[] allColumnNames, boolean comparison)
   {
      if (columnNames == null)
      {
         throw new IllegalArgumentException(
               "columnName cannot be null in FilterCriteria() constructor!");
      }
      if (values == null)
      {
         throw new IllegalArgumentException(
               "value cannot be null in FilterCriteria() constructor!");
      }

/*      if (formats == null)
      {
         throw new IllegalArgumentException(
               "formats cannot be null in FilterCriteria() constructor!");
      }
*/
      if (columnNames.length != operations.length ||
          columnNames.length != values.length /*||
          columnNames.length != formats.length*/)
      {
         throw new IllegalArgumentException(
              "All four arrays (columnNames, operations, values and formats " +
               "must be of equal length in FilterCriteria() constructor!");
      }
      this.columnNames = columnNames;
      this.operations  = operations;
      this.values      = values;
//      this.formats     = formats;
      this.allColumnNames = allColumnNames;
      this.compareWithAnd = comparison;
//      populateColumnIndexes();
      this.showColumns = new boolean[allColumnNames.length];
      Arrays.fill(showColumns, true);
   } // FilterCriteria()

   /** set the table model off which the filter criteria will work
    * @param model FormatAndIndexInfoIfc
     */
   public void setTableModel(FormatAndIndexInfoIfc model) 
   {
      this.model = model;
   }

   public FilterCriteria checkCompatibility(OverallTableModel newModel) throws Exception
   {
        String [] colNames = newModel.getColumnNames();
        ArrayList allColNames = new ArrayList();
        for(int i=0; i<colNames.length; i++)
        {
           allColNames.add(colNames[i]);
        }
        if(columnNames == null)
        {
            String [] tempColumnNames = newModel.getBaseColumnNames();
            //showColumns = new boolean[columnNames.length];
            //Arrays.fill(showColumns,true);
            
            return new FilterCriteria(tempColumnNames,showColumns);
        }
        boolean [] available = new boolean[this.columnNames.length]; 
        int count = 0;
        String missingColNames = "";
        for(int i=0; i<this.columnNames.length; i++)
        {
           if(allColNames.contains(columnNames[i]))
           {
              available[i] = true;
              count ++;
           }
           else 
           {
              missingColNames = missingColNames + columnNames[i] + ", ";
           }//else
        }//for(i)

        if(count == 0)
        {
            throw new Exception("The table does not contain any column names "+
               "specified for filter criteria in the configuration file");
        }
        else if(count < columnNames.length)
        {
            missingColNames = missingColNames.substring(0, missingColNames.length()-2);
            DefaultUserInteractor.get().notify(null,"Filter Criteria","The table does not " +
            "contain " + missingColNames + " specified for filter criteria in the" + 
             " configuration file",UserInteractor.WARNING);
           String []  newColNames = new String[count];
            int [] newOperations = new int[count];
            Comparable [] newValues = new Comparable[count];
            count =0;
            for(int i=0; i< columnNames.length; i++)
            {
               if(available[i])
               {
                  newColNames[count] = columnNames[i];
//System.out.println("columnNames["+i+"]="+columnNames[i]);                  
                  newOperations[count] = operations[i];
                  newValues[count] = values[i];
                  count ++;
               }//if(available[i])
            }//for(i)
            return new FilterCriteria(newColNames, newOperations, newValues,
               colNames,this.compareWithAnd);
        }//else if(count < columnNames.length)
        else
        {
            return this;
        }
   }

   /**
    * Constructor for filtering Columns.
    * @param columnNames String[] that is a list of the column names that we
    *    want to display.
    * @param formats Format[] that are the formatters used in each column.
    * @param show boolean[] that contains true for each column that we should show.
    */
   public FilterCriteria(String[] columnNames, /*Format[] formats,*/ boolean[] show)
   {
      this.allColumnNames = columnNames;
      this.showColumns = show;
//      this.formats     = formats;
//      populateColumnIndexes();
   } // FilterCriteria()

   /**
    * Return true if the value passed in meets the criteria.
    * @param rowData Comparable[] that is all of the data in one row.
    */
   public boolean accept(Comparable[] rowData)
   {
      // Set the initial value to True if we are cojmparing with AND and
      // false if we are comparing with OR.
      boolean retval = compareWithAnd;
      boolean tmp    = true;
      if(columnNames!=null)

//System.out.println("rowData.length="+ rowData.length);
      for (int i = 0; i < columnNames.length; i++)
      {
        int index = model.getColumnNameIndex(columnNames[i]);
        Class colClass = ((OverallTableModel)model).getColumnClass(index);
//System.out.println("column classes=" + colClass);        
        if(colClass.equals(Double.class))
        {
            double d1 = ((Double)values[i]).doubleValue();
            double d2 = ((Double)rowData[index]).doubleValue();
            if(!Double.isNaN(d1) && Double.isNaN(d2))
            {
               return false;
            }
        }

//System.out.println("index =" + index);        
        Format format = model.getFormat(columnNames[i]);
        
        
//System.out.println("format="+format.getClass());        
//System.out.println("FilterCriteria.accept(): rowData["+index+"]="+rowData[index]);
       //  int index = ((Integer)columnNameToIndex.get(columnNames[i])).intValue();
        // index of the column
         switch (operations[i])
         {
            case GREATER_THAN:
               tmp = (values[i].compareTo(rowData[index]) < 0);
               break;
            case LESS_THAN:
               tmp = (values[i].compareTo(rowData[index]) > 0);
               break;
            case GREATER_THAN_OR_EQUAL:
               tmp = (values[i].compareTo(rowData[index]) <= 0);
               break;
            case LESS_THAN_OR_EQUAL:
               tmp = (values[i].compareTo(rowData[index]) >= 0);
               break;
            case EQUAL:
               tmp = (values[i].equals(rowData[index]));
               break;
           case NOT_EQUAL:
               tmp = !(values[i].equals(rowData[index]));
               break;
            case STARTS_WITH:
               // get format of that column
               tmp = (format.format(rowData[index]).startsWith(values[i].toString()));
               break;
            case NOT_STARTS_WITH:
               // get the format of the column
               tmp = (!format.format(rowData[index]).startsWith(values[i].toString()));
               break;
            case CONTAINS:
              // get the format of the column
              tmp = (format.format(rowData[index]).indexOf(values[i].toString()) > -1);
               break;
            case NOT_CONTAINS:
              // get the format of the column
               tmp = (!(format.format(rowData[index]).indexOf(values[i].toString()) > -1));
               break;
            case ENDS_WITH:
              // get the format of the column
               tmp = (format.format(rowData[index]).endsWith(values[i].toString()));
               break;
            case NOT_ENDS_WITH:
              // get the format of the column
               tmp = (!format.format(rowData[index]).endsWith(values[i].toString()));
               break;
            default:
               throw new IllegalArgumentException("Bad operation " + operations[i] +
                  " in accept()!");
         } // switch (operations[i])


        if (compareWithAnd)
         {
            retval = retval && tmp;
         }
         else
         {
            retval = retval || tmp;
         }
      } // for(i)
     return retval;
   } // accept()


  /**
    * Return the column names that are shown by this filter.
    */
   public String[] getAllColumnNames()
   {
      return allColumnNames;
   }

   /**
    * Return the column names that are shown by this filter.
    */
   public boolean[] getColumntoShow()
   {
      return showColumns;
   }

   /**
    * Return the column name that this filter should apply to.
    */
   public String getColumnName(int index)
   {
     return columnNames[index];
   } // getColumnName()

   /**
    * Return the number of criteria in this FilterCriteria.
    * @return in that is the number of criteria.
    */
   public int getCriteriaCount()
   {
      if (columnNames != null)
      {
         return columnNames.length;
      }
      else
      {
         return 0;
      }
   } // getCriteriaCount()

  /**
    * Return the formatter for the given criteria index.
    * @return Format that is the formatter for the requested index.
    */
   public Format getFormat(String columnName)
   {
      if(model == null)
      {
          return null;
      }
      return model.getFormat(columnName);
   }

   /**
    * Return the String that represents the operation in this Criteria.
    * @return String that is the text symbol for the given operation or null if
    * the constant is not valid.
    */
   public String getOperationString(int index)
   {
      return (String)constantToSymbolHash.get(new Integer(operations[index]));
   }
   
    /**
    * Return the String that represents the operation in this Criteria.
    * @return String that is the text symbol for the given operation or null if
    * the constant is not valid.
    */
   public int getOperation(int index)
   {
      return operations[index];
   }

  /**
    * Return the value that should be compared to.
    * @return Comparable
    */
   public Comparable getValue(int index)
   {
      return values[index];
   }
   
   public boolean isCompareWithAnd()
   {
      return compareWithAnd;
   }

   /**
    * Return true if the Class passed in can be compared with the class
    * of the comparison value.
    */
   public boolean isClassComparable(int index, Class prospectiveClass)
   {
      if (values[index] instanceof String || values[index] instanceof Object)
      {
         return (prospectiveClass.equals(String.class));
      }
      else if (values[index] instanceof Number)
      {
         return (prospectiveClass.equals(Number.class));
      }
      else if (values[index] instanceof Date)
      {
         return (prospectiveClass.equals(Date.class));
      }
      else
      {
         return false;
      }
   } // isClassComparable()

   /**
    * Populate the columnNameToIndex hashtable.
    */
/*   private void populateColumnIndexes()
   {
      for (int i = 0; i < allColumnNames.length; i++)
      {
         columnNameToIndex.put(allColumnNames[i], new Integer(i));
      }
   } // populateColumnIndexes()
*/
   /**
    * Set the columns to be shown/hidden.
    * @param columnNames String[] with all column names in the base model.
    * @param boolean[] show with true values for columns to show.
    */
   protected void setColumnsToShow(String[] columnNames, boolean[] show)
   {
      this.allColumnNames = columnNames;
      this.showColumns = show;
   } // setColumnsToShow()

  /**
    * Set the column name on which to filter.
    * @param columnName String[] that is the column to filter on.
    * @param operation int[] that is one of the operation contants.
    * @param value Comparable[] that is the filter cutoff.
    */
    public void setRowCriteria(String[] newNames, int[] newOperations,
       Comparable[] newValues, /*Format[] newFormats, */boolean comparison)
    {
       columnNames = newNames;
       operations  = newOperations;
       values      = newValues;
//       formats     = newFormats;
       compareWithAnd = comparison;
    } // setRowCriteria()
    
    /***
     * NOTE: this method produces shallow copy for instance var 'model'.
     */
    public Object clone() throws CloneNotSupportedException
    {
      return super.clone();
    }
    
    /** Getter for property applyFilters.
     * @return Value of property applyFilters.
     *
     */
    public boolean isApplyFilters()
    {
       return applyFilters;
    }
    
    /** Setter for property applyFilters.
     * @param applyFilters New value of property applyFilters.
     *
     */
    public void setApplyFilters(boolean applyFilters)
    {
       this.applyFilters = applyFilters;
    }
    
    public String[] getColumnNames()
    {
      return columnNames;
    }
    
    private int[] getOperations()
    {
      return operations;
    }
    
    private boolean[] getShowColumns()
    {
      return showColumns;
    }
    
   
    private void setShowColumns(boolean[] showColumns)
    {
       this.showColumns = showColumns;
    }
    
    private void setCompareWithAnd(boolean compareWithAnd)
    {
      this.compareWithAnd = compareWithAnd;
    }
    
    private void setAllColumnNames(String [] allColumnNames)
    {
       this.allColumnNames = allColumnNames;
    }
    
    private void setOperations(int[] operations)
    {
       this.operations = operations;
    }
    
    private void setColumnNames(String[] columnNames)
    {
       this.columnNames = columnNames;
    }
    
   public String toString()
   {
      if(columnNames==null || columnNames.length == 0)
      {
         return "";//NO_FILTER;
      }     
      StringBuffer sb = new StringBuffer();
      int length = columnNames.length ;
      for(int i=0; i<length-1; i++)
      {
         sb.append(columnNames[i]).
         append(" ").
         append(OPERATION_STRINGS[i]).
         append(" ").
         append(values[i]).
         append(", ");
      }
      sb.append(columnNames[length-1]).append(" ").
         append(OPERATION_STRINGS[length-1]).append(" ").
         append(values[length-1]);     
      return sb.toString();
   }
   
   public Class[] getAllColumnClasses()
   {
      return allColumnClasses;
   }
   
   public void setAllColumnClasses(Class[] columnClasses)
   {
      if(columnClasses.length != allColumnNames.length)
      {
         throw new IllegalArgumentException("The column classes and " +
            "allColumnNames arrays should be of same length");
      }
      this.allColumnClasses = columnClasses;
   }
    
//    /** Getter for property id.
//     * @return Value of property id.
//     *
//     */
//    public long getId()
//    {
//       return id;
//    }    
//    
//    /** Setter for property id.
//     * @param id New value of property id.
//     *
//     */
//    public void setId(long id)
//    {
//       this.id = id;
//    }
    
    /** Getter for property valuesList.
     * @return Value of property valuesList.
     *
     */
//    public List getValuesList()
//    {
//       return valuesList;
//    }
//    
//    /** Setter for property valuesList.
//     * @param valuesList New value of property valuesList.
//     *
//     */
//    public void setValuesList(List valuesList)
//    {
//       this.valuesList = valuesList;
//    }
    
} //class FilterCriteria



