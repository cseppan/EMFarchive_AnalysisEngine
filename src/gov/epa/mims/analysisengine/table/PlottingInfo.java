package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.DoubleSeries;
import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.TreeDialog;
import gov.epa.mims.analysisengine.AnalysisEngineConstants;
import gov.epa.mims.analysisengine.gui.UserInteractor;
import gov.epa.mims.analysisengine.stats.Percentile;
import gov.epa.mims.analysisengine.tree.DataSets;
import gov.epa.mims.analysisengine.tree.DataSetIfc;
import gov.epa.mims.analysisengine.tree.LabeledDataSetIfc;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;


/**
 * This class will serve as a model for the PlottingInfoGUI and contain all
 * the information other than the data to pass on to the Analysis Engine plotting
 * package. A reference to an object of this class will be passed on to each of
 * the dataseries so they can correctly report back the data elements and the labels
 * without any duplication of data.
 *
 * @author Prashant Pai, CEP UNC
 * @version $Id: PlottingInfo.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */
public class PlottingInfo implements java.io.Serializable
{
   /** serial version UID */
   static final long serialVersionUID = 1;

//   /** the columns from which to take the data values **/
//   protected boolean[] dataColumnSelections = null;

   /** the separator to use in creating the name **/
   protected char separator = ' ';

   /** the plot type that needs to be used **/
   protected String plotType = PlotTypeConverter.BAR_PLOT;

   /** the units for the dataset **/
   protected String units = " ";

   /** if unit specified using col row header then  this will be >= 0*/
   protected int colRowHeaderIndex = -1;

   /** if user selects the units from header option, value will be false, */
   protected boolean userSpecifiedUnits = false;

   /** the top most level table model **/
   transient protected OverallTableModel tableModel = null;

   /** store the selected Data column Names */
   protected String[] selDataColNames;

   public static final String NOT_SELECTED = "Not Selected";

   /** store the selected label column Names */
   protected String[] selLabelColNames = {NOT_SELECTED,NOT_SELECTED,NOT_SELECTED};

   protected String[] selLabelDateColNames = {NOT_SELECTED,NOT_SELECTED};

   protected String timeFormat= "HH:mm:ss";

   protected String plotName = " ";

//   /** the columns from which to take the data values **/
//   private int[] dataColumns = null;

   public PlottingInfo(OverallTableModel model)
   {
      this.tableModel = model;
      //setHeaderMap();
   }

   // PlottingInfo()

   /**
    * set the plottype
    * @param plotType
    */
   public void setPlotType(String plotType)
   {
      this.plotType = plotType;
   }

   // setPlotType(String)

   /**
    * gets the currently selected plot type
    * @return
    */
   public String getPlotType()
   {
      return this.plotType;
   }

   // getPlotType()

   /**
    * set the units
    * @param units
    */
   public void setUnits(String units)
   {
//System.out.println("IN plotting info ="+units);
      this.units = units;
   }

   // setUnits(String)
   public void setPlotName(String name)
   {
      this.plotName = name;
   }

   public String getPlotName()
   {
      return this.plotName;
   }

   /**
    * gets the currently selected units
    * @return
    */
   public String getUnits()
   {
      return this.units;
   }

   /** gets set of units from table model if available
    */
   public String [] getUnitsChoices()
   {
      String [] rowHeaders = tableModel.getColumnRowHeaders();
      String [] colHeaders1 = tableModel.getColumnHeaders(0);

      String row = "Row";

      if(rowHeaders != null && rowHeaders.length  ==  colHeaders1.length)
      {
         for(int i=0; i< rowHeaders.length; i++)
         {
            if(rowHeaders[i].trim().equals("")
            || rowHeaders[i].trim().equalsIgnoreCase("row"))
            {
               rowHeaders[i] = row+(i+1);
            }
         }//for
         return rowHeaders;
      }
      else //if(rowHeaders == null || rowHeaders[0].equalsIgnorecase("row"))
      {
         String [] newColRowHeaders = new String[colHeaders1.length];
         for(int i=0; i<colHeaders1.length; i++)
         {
            newColRowHeaders[i] = row +(i+1);
         }
         return newColRowHeaders;
      }
      //Other possible cases for the else case
      //it's assumed that this type will not occur
      //1. rowHeaders.length < colHeaders.length
      //2. rowHeaders.length > colHeaders.length
   }// getUnits()

   /**
    * return the tablemodel for this plottinginfo object
    * @return
    */
   public OverallTableModel getModel()
   {
      return tableModel;
   }

   /**
    * return the number of data columns
    * @return the number of data columns
    */
   public int getNumOfDataColumns()
   {
      if(selDataColNames != null)
      {
         return selDataColNames.length;
      }

      return 0;
   }


   /** getter for the table model
    * @return OverallTableModel tableModel
    */
   public OverallTableModel getOverallTableModel()
   {
      return tableModel;
   }

   // getTableModel

   /**
    * sets the data columns
    * @param dataCols
    */
   public void setSelDataColumns(String [] dataColumns)
   {
      //this.dataColumnSelections = dataCols;
      //dataColumns = convertSelection(dataCols);
//      if (dataColumnSelections != null)
//      {
//         int length = dataColumns.length;
//         selDataColNames = new String[length];
//         for (int i = 0; i < length; i++)
//         {
//            selDataColNames[i] = tableModel.getColumnName(dataColumns[i] + 1); // to discount the first col
//         }
//        // for(i)
//      }
      selDataColNames = dataColumns;
      // if
   }
   // setDataColumns(int[])
//
//   /**
//    * return the data column selections
//    * @return
//    */
//   public boolean[] getDataColumns()
//   {
//      return this.dataColumnSelections;
//   }
//
//   // getDataColumns


   // setNameHeaders(int[])

   /** setter for the table model
    * @param OverallTableModel tableModel
    */
   public void setOverallTableModel(OverallTableModel tableModel)
                             throws Exception
   {
      String[] arrayColumnNames = tableModel.getColumnNames();
      Vector columnNames = new Vector();
      for(int i=0; i< arrayColumnNames.length; i++)
      {
         columnNames.add(arrayColumnNames[i]);
      }
      String[] dataCols = getSelDataColumnNames();
      boolean leastOneDataCol = false;
      boolean leastOneLabelCol = false;
      Vector  newDataColNames = new Vector() ;
      Vector  newLabelColNames = new Vector() ;
      Vector  newDateColNames = new Vector() ;
      Vector missDataColNames = new Vector();
      Vector missLabelColNames = new Vector();
      Vector missDateColNames = new Vector();
      if (dataCols != null)
      {
         for (int i = 0; i < dataCols.length; i++)
         {
            if (columnNames.contains(dataCols[i]))
            {
               newDataColNames.add(dataCols[i]);
            }
            else
            {
               missDataColNames.add(dataCols[i]);
            }
        }//for(i)
     }//if (dataCols != null)


     String[] labelCols = getSelLabelColumnNames();
     if (labelCols != null)
     {
         for (int i = 0; i < labelCols.length; i++)
         {
            if (columnNames.contains(labelCols[i]))
            {
               newLabelColNames.add(labelCols[i]);
            }
            else if(labelCols[i].equalsIgnoreCase(NOT_SELECTED))
            {
               newLabelColNames.add(labelCols[i]);
            }
            else
            {
               missLabelColNames.add(labelCols[i]);
            }
        }//for
    }//if(labelCols != null)

    String[] dateCols = getSelLabelDateColNames();
     if (dateCols != null)
     {
         for (int i = 0; i < dateCols.length; i++)
         {
            if (columnNames.contains(dateCols[i]))
            {
               newDateColNames.add(dateCols[i]);
            }
            else if(dateCols[i].equalsIgnoreCase(NOT_SELECTED))
            {
               newDateColNames.add(dateCols[i]);
            }
            else
            {
               missDateColNames.add(dateCols[i]);
            }
        }//for
    }//if(dateCols != null)

    if(newDataColNames.size() == 0)
    {
      throw new Exception("The present table does not have any column names that" +
         " matches the data column names in the configuration file.");
    }//if(newLabelColNames.size() == 0)
    else if (missDataColNames.size()>0)
    {
      DefaultUserInteractor.get().notify(null,"Warning", "The present table does " +
         "not have all the column names that matches the data column names in the " +
         "configuration file",UserInteractor.WARNING);
    }//else if (missDataColNames.size()>0)

    String aePlotType = PlotTypeConverter.getAEPlotType(plotType);
    if(TreeDialog.isLabelRequired(null,aePlotType))
    {
       if(aePlotType.equals(AnalysisEngineConstants.TIME_SERIES_PLOT))
       {
          if(newDateColNames.size() == 0)
          {
             throw new Exception("This plot '"+ plotType + "'requires a date label." +
               "\nThe present table does not have any column names that" +
               " matches the date label column names in the configuration file.");
          }
          else if(missDateColNames.size() > 0)
          {
             DefaultUserInteractor.get().notify(null,"Warning", "The present table does " +
               "not have all the column names that matches the date label column names in the " +
               "configuration file",UserInteractor.WARNING);
          }
       }
       else if(newLabelColNames.size() == 0)
       {
          throw new Exception("This plot '"+ plotType + "'requires a label." +
          "\nThe present table does not have column names that" +
            " matches the label column names in the configuration file.");
       }//if(newLabelColNames.size() == 0)
       else if(missLabelColNames.size() >0)
       {
          DefaultUserInteractor.get().notify(null,"Warning", "The present table does " +
         "not have all the column names that matches the label column names in the " +
         "configuration file",UserInteractor.WARNING);
       }//else if
    }//if(isLabelRequired)

    String [] newData = new String[newDataColNames.size()];
    String [] newLabel = new String[newLabelColNames.size()];
    String [] newDate = new String[newDateColNames.size()];
    this.selDataColNames = (String [])newDataColNames.toArray(newData);
    this.selLabelColNames = (String [])newLabelColNames.toArray(newLabel);
//System.out.println(selLabelColNames);
//for(int i=0; i< selLabelColNames.length; i++)
//{
//System.out.println("selLabelColNames[i]="+selLabelColNames[i]);
//}
    this.selLabelDateColNames = (String [])newDateColNames.toArray(newDate);
    this.tableModel = tableModel;
  }// setTableModel

   /**
    *
    * @param selected
    * @return
    */
   public int[] convertSelection(boolean[] selected)
   {
      int[] selections = new int[selected.length];
      int count = 0;

      for (int i = 0; i < selected.length; i++)
      {
         if (selected[i])
         {
            selections[count] = i;
            count++;
         }
         // if (selected[i])
      }

      // for int i
      int[] newArray = new int[count];

      for (int j = 0; j < count; j++)
      {
         newArray[j] = selections[j];
      }
      return newArray;
   }

   // getSelectedHeaders()
   public void setSeparator(char separator)
   {
      this.separator = separator;
   }

   // setSeparator()
   public String getSeparator()
   {
      return "" + separator;
   }

   // getSeparator()

   /**
    * combine the selected data columns to display in the textfield
    * @return a string that concatenates all the data column selections
    */
   public String getDataColumnSelection()
   {
      String retn = "";

      if ((selDataColNames == null) || (selDataColNames.length == 0))
      {
         return retn;
      }

      for (int i = 0; i < selDataColNames.length; i++)
      {
         retn = retn.concat(selDataColNames[i] + ", ");
      }
      //truncate the last ', '
      if (retn.length() > 2)
      {
         retn = retn.substring(0, retn.length() - 2);
      }
      return retn;
   }


   /**
    * This method is able to understand the selections made by the user in the
    * plotting dialog and chop up the data accordingly.
    * @return the DataSets correctly chopped up
    */
   public DataSets createDataSets() throws Exception
   {
      if ((selDataColNames == null) || (selDataColNames.length == 0))
      {
         throw new Exception("There are no Data Columns selected. Please select some");
      }
      DataSets tableDataSets = new DataSets();
      if(plotType.equals(PlotTypeConverter.TIMESERIES_PLOT))
      {
         TableDateTimeSeries dataSeries;
         // there will be one data series for each valueColumn
         int [] dateColumns = null;
         String pattern = "";
         //assume that selLabelDateColNames size =2
         //first columat type is simple date foramat
         SimpleDateFormat dateFormat = (SimpleDateFormat)tableModel.getFormat(
            selLabelDateColNames[0]);
         if(!selLabelDateColNames[1].equals(PlottingInfo.NOT_SELECTED))
         {
            pattern = timeFormat + ' ' + dateFormat.toPattern();
            dateColumns = new int[2];
            dateColumns[0] = tableModel.getColumnNameIndex(selLabelDateColNames[0]);
            dateColumns[1] = tableModel.getColumnNameIndex(selLabelDateColNames[1]);
         }
         else
         {
            pattern = dateFormat.toPattern();
            dateColumns = new int[1];
            dateColumns[0] = tableModel.getColumnNameIndex(selLabelDateColNames[0]);
         }
         for (int i = 0; i < selDataColNames.length; i++)
         {
            //Make sure for the TimeSeries Plot that only one column is selected
            // and type of the column SHOULD be of type Date
            int index = tableModel.getColumnNameIndex(selDataColNames[i]);
            dataSeries = new TableDateTimeSeries(tableModel, index,
               dateColumns,pattern);
            tableDataSets.add(dataSeries, dataSeries.getName());
         }//for
      }//if
      else
      {
         TableDataSeries dataSeries = null;
         //there will be one data series for each valueColumn
         String prevUnit = "";
         int [] labelColumns = new int[selLabelColNames.length];
//System.out.println("selLabelColNames.length="+selLabelColNames.length);
         for(int i=0; i < selLabelColNames.length; i++)
         {
//System.out.println("selLabelColNames["+i+"]="+selLabelColNames[i]);
            labelColumns[i] = tableModel.getColumnNameIndex(selLabelColNames[i]);
         }//for(i)

         for (int i = 0; i < selDataColNames.length; i++)
         {
            int index = tableModel.getColumnNameIndex(selDataColNames[i]);
            dataSeries = new TableDataSeries(tableModel,index, labelColumns,separator);
            tableDataSets.add(dataSeries, dataSeries.getName());
         }//for

      }//else
      Vector dataSets = tableDataSets.getDataSets(null,null);
      if(!userSpecifiedUnits)
      {
         checkSameUnits();
         for(int i=0; i< dataSets.size(); i++)
         {
            TableDataSeries dataSet = (TableDataSeries)dataSets.get(i);
            int index = tableModel.getColumnNameIndex(selDataColNames[i]);
            String[] colHeaders = tableModel.getColumnHeaders(index);
            if(colHeaders != null && colRowHeaderIndex != -1)
            {
                 dataSet.setUnits(colHeaders[colRowHeaderIndex]);
            }
         }//for
      }//if(!userSpecifiedUnits)
      else
      {
         for(int i=0; i< dataSets.size(); i++)
         {
            TableDataSeries dataSet = (TableDataSeries)dataSets.get(i);
            dataSet.setUnits(units);
         }//for(i)
      }//else
      return tableDataSets;
   }

   /** a helper method to check whether user labels are same(not case sensitive) or not
    * if there are not same then show a warning message
    *
    */
   private void checkSameUnits()
   {
      String [] colHeaders = tableModel.getColumnHeadersInARow(colRowHeaderIndex);
      if(colHeaders == null)
      {
         return;
      }
      int index = tableModel.getColumnNameIndex(selDataColNames[0]);
      String firstLabel = colHeaders[index];
//System.out.println("firstUnitLabel = "+ firstLabel);
      for(int i =1; i<selDataColNames.length; i++)
      {
         index = tableModel.getColumnNameIndex(selDataColNames[i]);
         if(!firstLabel.equalsIgnoreCase(colHeaders[index]))
         {
//System.out.println("colHeaders["+i+"]="+ colHeaders[dataColumns[i]]);
            DefaultUserInteractor.get().notify(null,"WARNING", "The selected columns"
               + " do not have same units", UserInteractor.WARNING);
            units = "";
            return;
         }
      }
   }


   // createDataSets(model)

//   /** a helper method to create a date set containing the percentile values for the
//    * CDF plot
//    */
//   private DataSetIfc createPercentileDataSeries(double [] percentile)
//   {
//      DoubleSeries labelSeries = new DoubleSeries();
//      try
//      {
//         labelSeries.setName("Fraction");
//         labelSeries.open();
//         for(int i=0; i< percentile.length; i++)
//         {
//            labelSeries.addData(percentile[i]);
//         }//for(i)
//         labelSeries.close();
//      }
//      catch(Exception e)
//      {
//         DefaultUserInteractor.get().notifyOfException("Error", e, UserInteractor.ERROR);
//         e.printStackTrace();
//      }
//      return labelSeries;
//   }

   /** getter for the selected data column Names
    *@return String[]
    */
   public String[] getSelDataColumnNames()
   {
      return selDataColNames;
   }

   // getSelDataColumnNames()

   /** getter for the selected label column Names
    *@return String[]
    */
   public String[] getSelLabelColumnNames()
   {
      return selLabelColNames;
   }

   public void setSelLabelColumnNames(String [] labelColNames)
   {
      selLabelColNames = labelColNames;
   }

   // getSelDataColumnNames()
   public PlottingInfo copy()
   {
      PlottingInfo info = new PlottingInfo(this.tableModel);
      String sep = getSeparator();
      info.setSeparator(sep.charAt(0));
      info.setPlotType(getPlotType());
//      if(dataColumnSelections != null)
//      {
//         info.setDataColumns(dataColumnSelections);
//      }

      info.setUserSpecifiedUnits(userSpecifiedUnits);
      info.setColRowHeaderIndex(colRowHeaderIndex);

      if(this.selDataColNames != null)
      {
         info.setSelDataColumns(selDataColNames);
      }
      if(this.selLabelColNames != null)
      {
         info.setSelLabelColumnNames(selLabelColNames);
      }
      if(selLabelDateColNames != null)
      {
         info.setSelLabelDateColNames(selLabelDateColNames);
      }
      info.setTimeFormat(getTimeFormat());
      info.setUnits(getUnits());
      return info;
   }

   /** Getter for property colRowHeaderIndex.
    * @return Value of property colRowHeaderIndex.
    *
    */
   public int getColRowHeaderIndex()
   {
      return colRowHeaderIndex;
   }

   /** Setter for property colRowHeaderIndex.
    * @param colRowHeaderIndex New value of property colRowHeaderIndex.
    *
    */
   public void setColRowHeaderIndex(int colRowHeaderIndex)
   {
      this.colRowHeaderIndex = colRowHeaderIndex;
      String [] headers = tableModel.getColumnHeadersInARow(colRowHeaderIndex);
      if(selDataColNames != null && selDataColNames.length >0)
      {
         int index = tableModel.getColumnNameIndex(selDataColNames[0]);
         if(headers != null)
         {
            units = headers[index];//set the first units for the label
         }
      }
   }

   /** Getter for property userSpecifiedUnits.
    * @return Value of property userSpecifiedUnits.
    *
    */
   public boolean isUserSpecifiedUnits()
   {
      return userSpecifiedUnits;
   }

   /** Setter for property userSpecifiedUnits.
    * @param userSpecifiedUnits New value of property userSpecifiedUnits.
    *
    */
   public void setUserSpecifiedUnits(boolean userSpecifiedUnits)
   {
      this.userSpecifiedUnits = userSpecifiedUnits;
   }

   /** Getter for property selLabelDateColNames.
    * @return Value of property selLabelDateColNames.
    *
    */
   public String[] getSelLabelDateColNames()
   {
      return this.selLabelDateColNames;
   }

   /** Setter for property selLabelDateColNames.
    * @param selLabelDateColNames New value of property selLabelDateColNames.
    *
    */
   public void setSelLabelDateColNames(String[] selLabelDateColNames)
   {
      this.selLabelDateColNames = selLabelDateColNames;
   }

   /** Getter for property timeFormat.
    * @return Value of property timeFormat.
    *
    */
   public String getTimeFormat()
   {
      return timeFormat;
   }

   /** Setter for property timeFormat.
    * @param timeFormat New value of property timeFormat.
    *
    */
   public void setTimeFormat(String timeFormat)
   {
      this.timeFormat = timeFormat;
   }

}


// PlottingInfo
