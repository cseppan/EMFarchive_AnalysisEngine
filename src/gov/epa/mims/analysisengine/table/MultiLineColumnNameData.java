package gov.epa.mims.analysisengine.table;

import java.util.Vector;
/*
 * MultiLineColumnNameData.java
 * To holde multiple lines of column names
 * Created on March 19, 2004, 12:11 PM
 * @author  Parthee Partheepan
 * @version $Id: MultiLineColumnNameData.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */
public class MultiLineColumnNameData
{
   /** a place holder for the columns */
   private Vector columnNames;

   /** no of columns*/
   private int noOfColumns;
   
   private String [] units;
   
   /** Creates a new instance of ColumnHeaderString */
   public MultiLineColumnNameData(int noOfColumns)
   {
      columnNames = new Vector();
      this.noOfColumns = noOfColumns;
      for(int i=0; i<noOfColumns; i++)
      {
         Vector aColumn = new Vector();
         columnNames.add(aColumn);
      }//for i
      units = new String[noOfColumns];
      
   }//ColumnHeaderString()
   
//   /** get the specified column name specific with the column number
//    */
//   public Vector getColumnNameObject(int columnNo)
//   {
//      if(columnNo >= columnNames.size())
//      {
//         System.err.println("The column name object size is "+ columnNames.size()+ " > " + columnNo);
//      }
//      return (Vector)columnNames.get(columnNo);
//   }
   
   /** A temporary method to get the string contained in the specfic column  
    *  
    *
    */
   public String getMultiLineColumnName(int columnNo)
   {
      Vector aColumnName = (Vector)columnNames.get(columnNo);
      String name = "";
      int size = aColumnName.size();
      for(int i=0; i< size-1; i++)
      {
         name += (String)aColumnName.get(i) + " || ";
      }//for i
      //last one 
      name += (String)aColumnName.get(size-1) ;
      return name;
   }//getColumnName(int columnNo)
   
   /** initialize the units 
    * @param columnNo int a column to which unit is added
    * @param unit String  a string to be added
    */
   public void addUnitName(int columnNo, String unit)
   {
      units[columnNo] = unit;
   }
   
   /** a getter for the unit name 
    */
   public String getUnitName(int columnNo)
   {
      return units[columnNo];
   }//getUnitName(int columnNo)
   
   
   /** to add a string to a specific columnNames
    * @param columnNo int a column to which string is added
    * @param name String  a string to be added
    */
   public void addStringToColumnName(int columnNo, String name)
   {
      if(columnNo >= columnNames.size())
      {
         System.err.println("The column name object size is "+ 
               columnNames.size()+ " > " + columnNo);
      }//if(columnNo >= columnNames.size())
      
      Vector aColumnName = (Vector)columnNames.get(columnNo);
      aColumnName.add(name);
      
   }//addStringColumnName(int columnNo, String name)
   
}

