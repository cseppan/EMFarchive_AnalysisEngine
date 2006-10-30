package gov.epa.mims.analysisengine.table.persist;

import gov.epa.mims.analysisengine.gui.GUIUserInteractor;
import gov.epa.mims.analysisengine.gui.ScreenUtils;
import gov.epa.mims.analysisengine.gui.TreeDialog;
import gov.epa.mims.analysisengine.gui.UserInteractor;
import gov.epa.mims.analysisengine.table.ColumnFormatInfo;
import gov.epa.mims.analysisengine.table.FilterCriteria;
import gov.epa.mims.analysisengine.table.FormattedCellRenderer;
import gov.epa.mims.analysisengine.table.OverallTableModel;
import gov.epa.mims.analysisengine.table.SaveToDialog;
import gov.epa.mims.analysisengine.table.SortCriteria;
import gov.epa.mims.analysisengine.tree.AnalysisOptions;
import gov.epa.mims.analysisengine.tree.Branch;
import gov.epa.mims.analysisengine.tree.DataSets;
import gov.epa.mims.analysisengine.tree.PageConstantsIfc;
import gov.epa.mims.analysisengine.tree.PageType;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumn;

/**
 * Class LoadConfigurationGUI - Dialog to preview the configuration in a file
 * before using it in current table's configuration; It is not an editor for
 * the file configuration.
 * This dialog pops up when the user wants to load an existing configuration
 * file to apply on the table currently being examined. It has several
 * features:
 * View to view the plot based on the current table data
 * View All to view all the plots in the configuration based on the current
 * table data
 * Import - import selected or all of the configurations into the current
 * configuration of the table.
 * Save - to save the configured plots based on the current table data to a
 * specific directory and in specific file format. If the plot is already
 * configured to be saved as a particular file and format it will be saved
 * under that filename in the chosen directory.
 *
 * @author  Krithiga Thangavelu, CEP, UNC CHAPEL HILL.
 * @version $Id: LoadConfigurationGUI.java,v 1.1 2006/10/30 17:26:12 parthee Exp $
 */
public class LoadConfigurationGUI extends javax.swing.JDialog
{
   
   /** Close button to close the GUI */
   private JButton bClose;
   
   /** Save button to save output of the configuration in the current table context */
   private JButton bSave;
   
   /** Import button to import the selected configuration into the current table  */
   private JButton bImport;
   
   /** View button to view selected plot in the configuration */
   private JButton bView;
   
   /** ConfigPanel in the  GUI */
   private JPanel jPanel1;
   
   /** List to display configurations in the file */
   private JList configList;
   
   /** Scrollpane containing the configList */
   private JScrollPane jScrollPane1;
   
   /** The panel containing all the components */
   private JPanel OverallPanel;
   
   /** The Configuration object from the configuration file being loaded */
   private AnalysisConfiguration input;
   
   /** The configuration object of the current table  */
   private AnalysisConfiguration dest;
   
   /** The Table containing the model in the sort filter table panel */
   private JTable table;
   
   /** Creates a new instance of LoadConfigurationGUI
    * @param input AnalysisConfiguration
    * @param dest  AnalysisConfiguration
    * @param table Table displaying the info from OverallTableModel
    * @param parent The Owner component of this dialog
    */
   public LoadConfigurationGUI(
   AnalysisConfiguration input,
   AnalysisConfiguration dest,
   JTable table, java.awt.Frame parent)
   {
      
      super(parent);
      this.input = input;
      this.dest = dest;
      this.table = table;
      initGUI();
      setLocation(ScreenUtils.getPointToCenter(this));
   }
   
   /**
    * Initializes the GUI.
    */
   public void initGUI()
   {
      try
      {
         OverallPanel = new JPanel();
         jScrollPane1 = new JScrollPane();
         //configList = new JList((Object[]) input.getKeys());
         String [] configNames = input.getConfigNames();
         int [] selectIndex = new int[configNames.length];
         for(int i=0; i< selectIndex.length; i++)
         {
            selectIndex[i] = i;
         }
         configList = new JList(configNames);
         configList.setSelectedIndices(selectIndex);
         jPanel1 = new JPanel();
         bView = new JButton();
         bImport = new JButton();
         bSave = new JButton();
         bClose = new JButton();
         this.setResizable(true);
         this.setTitle("Load Configuration Preview");
         this.setSize(new java.awt.Dimension(375, 230));
         this.getContentPane().add(OverallPanel);
         BoxLayout thisLayout = new BoxLayout(OverallPanel, 0);
         
         OverallPanel.setLayout(thisLayout);
         OverallPanel.setBorder(
         
         BorderFactory.createCompoundBorder(
         BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
         BorderFactory.createEmptyBorder(5, 5, 5, 5)));
         
         jScrollPane1.setPreferredSize(new java.awt.Dimension(177, 200));
         JPanel configPanel = new JPanel();
         
         configPanel.setLayout(new BoxLayout(configPanel, 1));
         configPanel.setBorder(
         BorderFactory.createTitledBorder(
         BorderFactory.createCompoundBorder(
         BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
         BorderFactory.createEmptyBorder(10, 10, 10, 10)),
         "Configurations"));
         
         configPanel.add(jScrollPane1);
         configList.setToolTipText(" Configurations in the file");
         jScrollPane1.add(configList);
         jScrollPane1.setViewportView(configList);
         OverallPanel.add(configPanel);
         FlowLayout  jPanel1Layout = new FlowLayout(FlowLayout.CENTER, 3, 6);
         jPanel1Layout.layoutContainer(jPanel1);
         jPanel1.setLayout(jPanel1Layout);
         jPanel1.setEnabled(true);
         jPanel1.setVisible(true);
         jPanel1.setPreferredSize(new java.awt.Dimension(120, 210));
         OverallPanel.add(jPanel1);
         
         bView.setText("    View    ");
         bView.setToolTipText("View all or the selected plots");
         bView.setPreferredSize(new java.awt.Dimension(100, 30));
         jPanel1.add(bView);
         bView.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               bViewMouseClicked(evt);
            }
         });
         
         bImport.setText("  Import   ");
         bImport.setToolTipText(
         "Import all or the selected configurations into the table");
         bImport.setPreferredSize(new java.awt.Dimension(100, 30));
         jPanel1.add(bImport);
         bImport.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               bImportMouseClicked(evt);
            }
         });
         
         bSave.setText("Save Plots");
         bSave.setToolTipText("Save all or the selected plots to a directory");
         bSave.setPreferredSize(new java.awt.Dimension(100, 30));
         jPanel1.add(bSave);
         bSave.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               bSaveMouseClicked(evt);
            }
         });
         
         bClose.setText("   Close    ");
         bClose.setToolTipText("Close this window");
         bClose.setPreferredSize(new java.awt.Dimension(100, 30));
         jPanel1.add(bClose);
         bClose.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               dispose();
               return;
            }
         });
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   
   protected void bViewMouseClicked(ActionEvent evt)
   {
      Object[]  values = configList.getSelectedValues();
      //System.out.println("values.length="+values.length);
      //for(int i=0; i<values.length; i++)
      //{
      //System.out.println("values["+i+"]="+values[i]);
      //}
      if(values.length == 0)
      {
         values = input.getConfigNames();
         //rearrange the array values
         values = reorder((String[])values);
      }
      StringBuffer error = new StringBuffer();
      for (int i = 0; i < values.length; i++)
      {
         try
         {
            showPlot((String) values[i]);
         }
         catch (Exception e)
         {
            error.append(values[i] + " not applicable to current table\n Error:"
            + e.getMessage() + "\n");
            e.printStackTrace();
         }
      }
      if (error.length() > 1)
      {
         new GUIUserInteractor().notify(this,"Configuration Error", error.toString(),
         UserInteractor.ERROR);
      }
   }
   
   private String[] reorder(String [] values)
   {
      Vector list = new Vector();
      for(int i=0; i< values.length; i++)
      {
         list.add(values[i]);
      }
      
      boolean format = list.contains(AnalysisConfiguration.TABLE_FORMAT);
      boolean filter = list.contains(AnalysisConfiguration.TABLE_FILTER_CRITERIA);
      boolean sort = list.contains(AnalysisConfiguration.TABLE_SORT_CRITERIA);
      int count =0;
      if(format)
      {
         list.remove(AnalysisConfiguration.TABLE_FORMAT);
         list.add(count++, AnalysisConfiguration.TABLE_FORMAT);
      }
      if(filter)
      {
         list.remove(AnalysisConfiguration.TABLE_FILTER_CRITERIA);
         list.add(count++, AnalysisConfiguration.TABLE_FILTER_CRITERIA);
      }
      if(sort)
      {
         list.remove(AnalysisConfiguration.TABLE_SORT_CRITERIA);
         list.add(count++, AnalysisConfiguration.TABLE_SORT_CRITERIA);
      }
      return (String[])list.toArray(values);
   }//reorder()
   
   
   /** Shows the plot of the configuration "plotname" in the current table context
    * @param plotname String
    * @return None else returns Exception if the configuration is not applicable on the table
    **/
   
   protected void showPlot(String plotname) throws Exception
   {
      String pageType = null;
      AnalysisOptions options;
      
      Data dat = input.getConfig(plotname);
      if (dat != null && dat.configType == Data.PLOT_TYPE)
      {
         DataSets dset = input.getDataSets(dat.info);
         Branch tempTree = dat.tree; // .clone());
         if (dset != null)
         {
            options = (AnalysisOptions) tempTree.getChild(0);
            pageType = setToDefaultScreenPageType(options);
            //dset.add(tempTree.getChild(0));
         }
         else
         {
            throw new Exception("Empty Data Set");
         }
         TreeDialog.createPlotWithoutGUI(tempTree, dset, null, null);
         if (pageType != null)
         {
            PageType pt = (PageType) options.getOption("PAGE_TYPE");
            pt.setTextString(pageType);
            options.addOption("PAGE_TYPE", pt);
         }
      }//if (dat != null && dat.configType == Data.PLOT_TYPE)
      else
      {
         if(dat!=null && dat.configType == Data.TABLE_TYPE)
         {
            if(dat.criteria==null)
            {
               new GUIUserInteractor().notify(this,"Table Configuration",
               plotname+" is empty", UserInteractor.NOTE);
               return;
            }
            else
            {
               previewTable(dat);
            }
         }
      }//else
   }
   
   protected void previewTable(Data dat)
   {
      OverallTableModel model = input.getModel();
      try
      {
         Hashtable formats = null;
         SortCriteria sCriteria = null;
         FilterCriteria fCriteria = null;
         formats = (Hashtable)(model.getColumnFormatInfo()).clone();
         sCriteria = model.getSortCriteria();
         fCriteria = model.getFilterCriteria();
         if(dat.criteria instanceof Hashtable)
         {
            formatTable((Hashtable)dat.criteria);
         }
         else
         {
            importIntoTable(dat.criteria);
         }
         this.setVisible(false);
         int result = new GUIUserInteractor().selectOption(this,"Table "+
         " Configuration Preview ", "View the table now. Do you want to keep "+
         "this configuration?", UserInteractor.YES_NO, UserInteractor.NO);
         
         if(result==UserInteractor.NO)
         {
            //model.reset();
            if(dat.criteria instanceof Hashtable)
               formatTable(formats);
            else
            {
               model.reset();
               importIntoTable(formats);
               importIntoTable(fCriteria);
               importIntoTable(sCriteria);
            }
         }
         this.setVisible(true);
      }
      catch(Exception e)
      {
         new GUIUserInteractor().notify(this,"Configuration Mismatch", "The "+
         "imported configuration is not applicable to the current table. "+
         e.getMessage(), UserInteractor.ERROR);
         e.printStackTrace();
      }
   }
   
   /** Sets the plot output to SCREEN
    * @param options AnalysisOptions
    * @return String PageType settings before modification
    **/
   protected String setToDefaultScreenPageType(AnalysisOptions options)
   {
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
   
   protected void bImportMouseClicked(ActionEvent evt)
   {
      Object[] values = configList.getSelectedValues();
      if(values.length == 0)
      {
         values = input.getConfigNames();
         values = reorder((String[])values);
      }
      if (values.length > 0)
      {
         for (int i = 0; i < values.length; i++)
         {
            try
            
            {
               Data dat = (Data)input.getConfig((String)values[i]);
               if(dat.configType ==Data.TABLE_TYPE)
               {
                  importIntoTable(dat.criteria);
               }
               else
               {
                  dest.storePlotConfig((String) values[i],
                  input.getConfig((String) values[i]), false);
               }
            }
            catch(Exception e)
            {
               new GUIUserInteractor().notify(this,"Configuration Mismatch",
               "Configuration "+ values[i]+ " not applicable to the current"+
               " table. "+e.getMessage(), UserInteractor.ERROR);
               e.printStackTrace();
            }
         }//for(values)
      }
   }
   
   
   /** imports into the table Criteria
    *  @param criteria Object can be SortCriteria or FilterCriteria
    *                    or Hashtable
    */
   protected void importIntoTable(Object criteria) throws Exception
   {
      
      OverallTableModel model = input.getModel();
      if(criteria == null) return;
      if(criteria instanceof Hashtable)
      {
         formatTable((Hashtable)criteria);
         
      }
      else
      {
         if(criteria instanceof SortCriteria)
         {
            model.sort((SortCriteria)criteria);
         }
         else
         {
            if(criteria instanceof FilterCriteria)
            {
               ((FilterCriteria)criteria).setTableModel(model);
               model.filterRows((FilterCriteria)criteria);
               model.filterColumns((FilterCriteria)criteria);
               formatTable(model.getColumnFormatInfo());
            }
            else
            {
               throw new Exception("Not an instance of Table Criteria ");
            }
         }
      }
   }
   
   /** Formats the table based on formats
    * @param formats a Hashtable of ColumnFormatInfo indexed by ColumnName
    *
    */
   protected void formatTable(Hashtable formats) throws Exception
   {
      Set keys = formats.keySet();
      Iterator iter = keys.iterator();
      while(iter.hasNext())
      {
         String key = (String)iter.next();
         int colIndex = input.getModel().getColumnNameIndex(key);
         //System.out.println("key=" + key + " colIndex = "+colIndex);
         ColumnFormatInfo formatInfo = (ColumnFormatInfo)formats.get(key);
         if(colIndex != -1 && formatInfo!=null)
         {
            TableColumn column = table.getColumnModel().getColumn(colIndex+1);
            column.setPreferredWidth(formatInfo.width);
            FormattedCellRenderer formattedRenderer = new
            FormattedCellRenderer(formatInfo.getFormat(),
            formatInfo.alignment);
            formattedRenderer.setFont(formatInfo.font);
            formattedRenderer.setForeground(formatInfo.foreground);
            formattedRenderer.setBackground(formatInfo.background);
            // Now set the new renderer to be used in the column.
            column.setCellRenderer(formattedRenderer);
            dest.getModel().setColumnFormatInfo(key, new ColumnFormatInfo(column));
         }
      }
      table.repaint();
   }
   
   protected void bSaveMouseClicked(ActionEvent evt)
   {
      Object[] values = configList.getSelectedValues();
      if(values.length == 0)
      {
         values = input.getConfigNames();
         values = reorder((String[])values);
      }
      SaveToDialog dialog = new SaveToDialog(this);
      dialog.setVisible(true);
      int returnVal = dialog.getRetVal();
      try
      {
         if (returnVal == SaveToDialog.CANCEL)
         {
            dialog.dispose();
            return;
         }
         if (returnVal == SaveToDialog.APPROVE)
         {
            String[] stringValues = new String[values.length];
            for(int i=0; i< values.length; i++)
            {
               stringValues[i]= (String)values[i];
               Data dat = (Data)input.getConfig(stringValues[i]);
               if(dat.configType ==Data.TABLE_TYPE)
               {
                  importIntoTable(dat.criteria);
               }
            }
            input.saveConfiguredPlots(dialog.getAbsolutePath(), dialog.getFileType(),
            stringValues);
            new GUIUserInteractor().notify(this,"Saving Plots", "Selected Plots "+
            " were saved successfully!!", UserInteractor.NOTE);
         }
      }
      
      catch (Exception  ex)
      {
         ex.printStackTrace();
         new GUIUserInteractor().notify(this,"Error saving plots", ex.getMessage(),
         UserInteractor.ERROR);
      }
      dialog.dispose();
      return;
   }
   
   /**
    * This static method creates a new instance of this class and shows
    * it inside a new JFrame, (unless it is already a JFrame).
    *
    * It is a convenience method for showing the GUI
    **/
   public static void showGUI()
   {
      try
      {
         LoadConfigurationGUI inst = new LoadConfigurationGUI(null, null,
         null, null);
         inst.setVisible(true);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
