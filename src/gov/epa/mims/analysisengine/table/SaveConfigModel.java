package gov.epa.mims.analysisengine.table;


import gov.epa.mims.analysisengine.tree.AnalysisOptions;
import gov.epa.mims.analysisengine.tree.DataSets;
import gov.epa.mims.analysisengine.tree.PageConstantsIfc;
import gov.epa.mims.analysisengine.tree.PageType;
import gov.epa.mims.analysisengine.gui.TreeDialog;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.Vector;
import java.util.Arrays;
import javax.swing.JFrame;

/**
 * Created on Jul 7, 2004
 * SaveConfigModel - model for SaveConfigGUI
 * Contains functions to manipulate the AnalysisConfiguration object
 * through the SaveConfigModel.
 *
 * @author  Krithiga Thangavelu, CEP, UNC CHAPEL HILL.
 * @version $Id: SaveConfigModel.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */

public class SaveConfigModel extends DefaultTableModel {

   /** confignames to be displayed on the ConfigList of SaveConfigGUI */
   private String[] configNames;

   /** column names to be displayed on the top of the ConfigTable */
   private static String[] columnNames = { "Configurations", "Type", "Save?" };

   /** AnalysisConfiguration view and manipulated by this Class */
   private AnalysisConfiguration aconfig;

   /**
    * Creates a new instance of SaveConfigModel
    * @param config String
    *
    */
   SaveConfigModel(AnalysisConfiguration config) throws Exception
   {
      super();
      if (config ==null) {
         throw new Exception("There are no configurations available");
      }
      aconfig = config;

//      configNames = aconfig.getKeys();
      configNames = aconfig.getConfigNames();
      Vector toSaveOrNot = new Vector();
      Vector plotTypes = new Vector();

      if (configNames.length == 0) {
         throw new Exception("There are no configurations available");
      }

      for(int i=0; i< configNames.length; i++)
      {
         plotTypes.add(getPlotType(configNames[i]));
      }

      for (int i = 0; i < configNames.length; i++) {
         toSaveOrNot.add(new Boolean("true"));
      }
      addColumn(columnNames[0], configNames);
      addColumn(columnNames[1], plotTypes);
      addColumn(columnNames[2], toSaveOrNot);
   }
  
   public String getPlotType(String configName)
   {
      Data dat = aconfig.getConfig(configName);
      if(dat!=null && dat.info!=null)
         return (String)dat.info.getPlotType();
      else 
         return "Table Configuration"; 
   }
   /**
    * prerequisite for TableModel
    * @return Class the class type of the column given the column index     */ 
   public Class getColumnClass(int index)
   {
      if (index == 2) {
         return Boolean.class;
      } else {
         return String.class;
      }
   }

   /**
    * removes the configNames[i] from the AnalysisConfiguration object 
    * @param rowIndex int 
    * @return none
    */
   public void remove(int rowIndex) {
      aconfig.removeKey(configNames[rowIndex]);
   }

   /**
    * rename the configname of configNames[rowIndex] to plotName
    * @param rowIndex int
    * @param newName String
    * @return None
    */
   public void renameConfig(int i, String newName) throws Exception 
   {
      String[] configNames = getConfigNames();
      //configNames[i]= old name
      //same name as old name
      if (newName.equals(configNames[i])) 
      {
         return;
      } 
      if (aconfig.getConfig(newName) != null) 
      {
         throw new Exception(newName + " already exists");
      }
      
      Data dat = aconfig.getConfig(configNames[i]);
      if(dat==null || dat.configType == Data.TABLE_TYPE)
      {
         throw new Exception("You cannot rename a table configuration");
      }
      aconfig.removeKey(configNames[i]);
      aconfig.storePlotConfig(newName, dat, true);
      
   }

   public String[] getConfigNames() 
   {
      return aconfig.getConfigNames();
   }
   
   /**
    * shows the Configuration for editing
    * @param configname String
    * @return None
    */
   public void showTree(String configname) throws Exception 
   {
      Data dat = aconfig.getConfig(configname);
      if(dat.configType==Data.TABLE_TYPE)
         throw new Exception("You cannot edit a Table Configuration");
      dat.tree = TreeDialog.showTreeDialog(new JFrame(), dat.tree,
            aconfig.getDataSets(dat.info), null, null);
      aconfig.storePlotConfig(configname, dat, true);
   }

   /**
    * shows the plot based on the current table with configNames[i] configuration
    * @param i int
    * @return None else returns Exception if the configuration is not applicable on the table
    */
   public void showPlot(int i) throws Exception 
   {
      String pageType = null;
      AnalysisOptions options;
      Data dat = aconfig.getConfig((String)getValueAt(i, 0));

      if (dat != null) 
      {
         if(dat.configType == Data.TABLE_TYPE) return; 
         DataSets dset = aconfig.getDataSets(dat.info);
         DataSets tempTree = (DataSets) (dat.tree);

         if (dset != null) 
         {
            options = (AnalysisOptions) tempTree.getChild(0);
            pageType = setToDefaultScreenPageType(options);
            dset.add(tempTree.getChild(0));
         } 
         else
         {
            throw new Exception("Empty Data Set");
         }
         TreeDialog.createPlotWithoutGUI(dset, dset, null, null);
         if (pageType != null) 
         {
            PageType pt = (PageType) options.getOption("PAGE_TYPE");
            pt.setTextString(pageType);
            options.addOption("PAGE_TYPE", pt);
         }
      }
   }

   /** Sets the plot output to SCREEN
    * @param options AnalysisOptions
    * @return String PageType settings before modification
    **/
   protected String setToDefaultScreenPageType(AnalysisOptions options) {
      PageType pt = (PageType) options.getOption("PAGE_TYPE");
      String retval = null;

      if (pt != null) 
      {
         if (!((pt.getForm()).equals(PageConstantsIfc.SCREEN))) 
         {
            retval = pt.getTextString();
            pt.setForm(PageConstantsIfc.SCREEN);
            options.addOption("PAGE_TYPE", pt);
         }
      }
      return retval;
   }

   /** reorders the configuration names in Sorted order or inserted order
    * @param None
    * @returns None
    */            
   public void reorderColumns() 
   {
      Vector oldconfigNames = new Vector();
      Vector oldTypes = new Vector();
      Vector oldValues = new Vector();
      for(int i=0; i<getRowCount(); i++)
      {
         oldconfigNames.add(getValueAt(i, 0));
         oldTypes.add(getValueAt(i, 1));
         oldValues.add(getValueAt(i, 2));
      }
      String[] configNames = aconfig.getConfigNames();
      Vector data = new Vector();
      Vector types = new Vector();
      for (int i = 0; i < configNames.length; i++) 
      {
         Vector entry = new Vector();
         entry.add(configNames[i]);
         entry.add((String)oldTypes.remove(oldconfigNames.indexOf(configNames[i])));
         entry.add((Boolean) oldValues.remove(
               oldconfigNames.indexOf(configNames[i])));
         oldconfigNames.remove(configNames[i]);
         data.add(entry);
      }
      setDataVector(data, new Vector(Arrays.asList(columnNames))); 
   }

   /** getter for configname at index i
    *  @param i int
    *  @return String
    */
   public String getConfigName(int i) 
   {
      aconfig.getConfigNames();
      String[] names = aconfig.getConfigNames();
      if (i >= 0 && i < names.length) 
      {
         return names[i];
      }
      return null;
   }
    
   /** getter for Save? selection
    * @param None
    * @return Vector a collection of selected config names
    */
   public Vector getSelectedValues() 
   {
      Vector result = new Vector();
      for(int i=0; i < getRowCount(); i++)
         if(((Boolean)getValueAt(i, 2)).booleanValue() == true)
            result.add((String)getValueAt(i, 0));
      return result;
   }

   /** Save Configuration based on Save? selection to File
    * @param File java.io.File
    * @return None else throws IOException 
    */
   public void saveConfiguration(File file) throws java.io.IOException 
   {
      file.createNewFile();
      aconfig.saveConfiguration(file, getSelectedValues());
   }
   
   public boolean isCellEditable(int row, int column)
   {
      if(column == 0)
      {
         String value = (String)getValueAt(row, column);
         if(value.equals(AnalysisConfiguration.TABLE_FILTER_CRITERIA)
            ||value.equals(AnalysisConfiguration.TABLE_FORMAT)
            ||value.equals(AnalysisConfiguration.TABLE_SORT_CRITERIA))
         {
            return false;
         }
         return true;
      }
      else if(column==1)
      {
         return false;
      }
      return true;
         
   }
   
}
