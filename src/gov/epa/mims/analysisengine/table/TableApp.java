package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.GUIUserInteractor;
import gov.epa.mims.analysisengine.gui.OptionDialog;
import gov.epa.mims.analysisengine.gui.ScreenUtils;
import gov.epa.mims.analysisengine.gui.UserInteractor;
import gov.epa.mims.analysisengine.help.HelpMenu;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * <p>Title:TableApp.java </p>
 * <p>Description: A panel to hold the SortFilterTablePanel and have menu bar for
 * adding import, export,  ... </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: CEP, UNC-Chapel Hill </p>
 * @author Parthee Partheepan
 * @version $Id: TableApp.java,v 1.3 2005/09/21 14:22:48 parthee Exp $
 */


public class TableApp extends JFrame
{
   
   /** title for the frame */
   public final static String TITLE = "Analysis Engine Table Application";
   
   /** a panel to hold SortFilterTablePanel*/
   public JTabbedPaneWithCloseIcons mainTabbedPane;

   /** to store file names shown in the tab and create unique names to appear in
    * the mainTabbedPane
    */
   private FilesInTabbedPane filesInTabbedPane;

   /** gui to import the files
    */
   private FileImportGUI fileImportGUI;

   /** gui to export the files
    */
   private FileExportGUI fileExportGUI = null;

   static String version = "February 16, 2005";

   /** File History
    */
   private FileHistory history = null;

   /** a boolean to flag whether this TableApp is used as stand alone or call
    * from other application
    * In main() in this class it will be set true otherwise it will be always false
    */
   private static boolean standAlone = false;
 

   /** MenuItems that need to be enabled and disabled when files are open/closed
    */
   JMenuItem loadConfigMenuItem;
   JMenuItem saveConfiguredPlotsMenuItem;
   JMenuItem exportMenuItem;
   JMenuItem queryMenuItem;
   JMenuItem closeMenuItem;
  

   /** Creates a new instance of TableApp */
   public TableApp()
   {
      initialize();
      
      pack();
      System.out.println(TITLE+" Version "+this.version+" (C1)");
      setDefaultCloseOperation();
      setLocation(ScreenUtils.getPointToCenter(this));
      setVisible(true);
   }// TablePanel()

   /** Creates a new instance of TableApp
    *@param String [] fileNames array of fileNames to be imported
    *@param fileType This value = FileImportGUI.GENERIC_FILE
    *@param delimiter
    *@noOfColumnNameRows no of rows which contains column header informaton
    */
   public TableApp(String[] fileNames, String fileType, String delimiter,
      int noOfColumnNameRows)  
   {
      initialize();
      System.out.println(TITLE+" Version "+this.version+" (C2)");
      if (fileNames.length > 8)
      {
         showImportGUI(fileNames, fileType, delimiter, noOfColumnNameRows);
      }
      else
      {
         String[] tabNames = createTabNames(1, 40, fileNames);
         importFiles(fileNames, tabNames, fileType, delimiter, noOfColumnNameRows);
      }
      pack();
      setLocation(ScreenUtils.getPointToCenter(this));
      setDefaultCloseOperation();
      setVisible(true);
   }// TableApp();

   
   /** Creates a new instance of TableApp
    *@param String [] fileNames array of fileNames to be imported
    *@param fileType This value = FileImportGUI.GENERIC_FILE
    *@param delimiter
    *@noOfColumnNameRows no of rows which contains column header informaton
    */
   public TableApp(String[] fileNames, String fileType, String delimiter,
      int noOfColumnNameRows, int startPos, int endPos)
   {
      initialize();
      System.out.println(TITLE+" Version "+this.version+" (C3)");
      String[] tabNames = createTabNames(startPos, endPos, fileNames);
      importFiles(fileNames, tabNames, fileType, delimiter, noOfColumnNameRows);
      pack();
      setLocation(ScreenUtils.getPointToCenter(this));
      setDefaultCloseOperation();
      setVisible(true);
   }// TableApp();

   public TableApp(ResultSet rs, String tabName)
   {
     this();
     System.out.println(TITLE+" Version "+this.version+" (C4)");
     addNewTab(rs, tabName);
   }// TableApp();
   
   public TableApp(ResultSet rs,File configFile, String tabName)
   {
      this(rs,tabName);
      if(configFile != null)
      {
         TablePanel newPanel = (TablePanel)mainTabbedPane.getSelectedComponent();
         newPanel.tablePanel.loadConfigFile(configFile, true);
      }
   }

   

   private void setDefaultCloseOperation()
   {
      if(!TableApp.standAlone)
      {
         setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      }
      else
      {
         setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      }
   }//setDefaultCloseOperation()
   

   /** a helper method to import files
    * @param fileNames != null
    * @param fileType != null
    * @param delimiter != null
    * @param noOfColumnNameRows != null
    * @param startpos used for creating tabnames
    * @param endpos used for creating tabnames
    */
   public void importDifferentFiles(String[] fileNames, String[] fileType,
      String[] delimiter, int[] noOfColumnNameRows, int startpos, int endpos)
      throws Exception
   {
      if(fileNames.length!=fileType.length ||
         fileNames.length!=delimiter.length ||
      fileNames.length!=noOfColumnNameRows.length)
      {
         throw new Exception("Invalid arguments");
      }
      String[] tabNames = createTabNames(startpos, endpos, fileNames);
      boolean allSuccessful = true; // a flag to check whether
      // all the file imported sucessfuly
      ArrayList importFileStatus = new ArrayList();
      ArrayList warningWindow = new ArrayList();
      // System.out.println("fileNames.length="+fileNames.length);
      for (int i = 0; i < fileNames.length; i++)
      {
         SpecialTableModel model = null;
/**         try //Why we are catching the error ??? RP
         {
 */
            try
            {
               model = FileImportGUI.createAModel(fileNames[i], fileType[i],
               delimiter[i], noOfColumnNameRows[i]);
               if (FileImportGUI.getLogMessages() != null)
               {
                  warningWindow.add(FileImportGUI.getLogMessages());
               }
               insertIntoTabbedPane(model, fileNames[i], tabNames[i], fileType[i]);
               history.addToHistory(fileType[i], fileNames[i], delimiter[i],
               noOfColumnNameRows[i]);
               importFileStatus.add("Success: " + fileNames[i]);
            }// try
            catch (Exception e)
            {
               allSuccessful = false;
               if (e.getMessage() == null)
               {
                  importFileStatus.add("FAILURE: " + fileNames[i]+"\n");
                  importFileStatus.add(get50Lines(fileNames[i]));
               }
               else
               {
                  importFileStatus.add("FAILURE: " + fileNames[i] +
                  ":\n" + e.getMessage());
                  importFileStatus.add(get50Lines(fileNames[i]));
               }
               System.out.println("Error reading File "+e.getMessage());
               e.printStackTrace();
            }// catch
/**         }//try
      
         catch(Error er)
         {
            //RP I am not sure why we are doing this????
            er.printStackTrace();
            System.out.println("Caught111\n");
         }
*/
      }//for(i fileNames)
      try
      {
         if (warningWindow.size() != 0)
         {
            TextDialog dialog = new TextDialog(this, "WARNING", "", false);
            dialog.setTextFromList("Warnings from the Table Loader",
            warningWindow);
            dialog.setModal(true);
            dialog.setVisible(true);
            warningWindow.clear();
         }
         if (!allSuccessful) // ie don't show the dialog if all the files are imported sucessfuly.
         {
            TextDialog dialog = new TextDialog(this, "Import File Status", "",
               false);
            dialog.setTextFromList(
               "The status of the import process is as follows:\n",
               importFileStatus);
          
            dialog.addComponentListener(new ComponentAdapter()
            {
               public void componentHidden(ComponentEvent e)
               {
                  if(fileImportGUI!=null)
                     fileImportGUI.setVisible(true);
               }
            });
           dialog.setModal(true);
           dialog.setVisible(true);
         }// if(!allSuccessful)
      } // try
      catch (Exception e)
      {
         DefaultUserInteractor.get().notifyOfException(this,
         "Failed to show import status dialog", e, UserInteractor.ERROR);
      }// catch
   }

   public static String get50Lines(String fileName)
   {
      StringBuffer lines = new StringBuffer();
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(fileName));
         String str = new String("The first 50 Lines of the file "+ fileName);
         lines.append(str);
         lines.append("\n");
         for(int i=0; i<str.length(); i++)
            lines.append("=");
         lines.append("\n\n");
         String line;
         int count=0;
         while((line=reader.readLine())!=null && count<50)
         {
            lines.append(line);
            lines.append("\n");
            count++;
         }
         reader.close();
      }
      catch(IOException ie)
      {
         lines.append("Error reading file "+ ie.getMessage());
      }
      return lines.toString();
   }

   /** a helper method to import files
    * @param fileNames != null
    * @param fileType
    * @param delimiter
    */
   private void importFiles(String[] fileNames, String[] tabNames,
      String fileType, String delimiter, int noOfColumnNameRows)
   {
      boolean allSuccessful = true; // a flag to check whether all the file imported sucessfuly
      ArrayList importFileStatus = new ArrayList();
      ArrayList warningWindow = new ArrayList();

      // System.out.println("fileNames.length="+fileNames.length);
      for (int i = 0; i < fileNames.length; i++)
      {
         SpecialTableModel model = null;
         try
         {
            try
            {
               model = FileImportGUI.createAModel(fileNames[i], fileType,
               delimiter, noOfColumnNameRows);
               if (FileImportGUI.getLogMessages() != null)
               {
                  warningWindow.add(FileImportGUI.getLogMessages());
               }
               insertIntoTabbedPane(model, fileNames[i], tabNames[i], fileType);
               history.addToHistory(fileType, fileNames[i], delimiter,
               noOfColumnNameRows);
               importFileStatus.add("Success: " + fileNames[i]);
            } // try
            catch (Exception e)
            {
               allSuccessful = false;
               if (e.getMessage() == null)
               {
                  importFileStatus.add("FAILURE: " + fileNames[i]+"\n");
                  importFileStatus.add(get50Lines(fileNames[i]));
               }
               else
               {
                  importFileStatus.add(
                  "FAILURE: " + fileNames[i] + ":\n" + e.getMessage());
                  importFileStatus.add(get50Lines(fileNames[i]));
               }
               System.out.println("Error reading the file "+ e.getMessage());
               e.printStackTrace();
            }// catch
         }
         catch(Error er)
         {
            System.out.println("Caught\n");
         }
      }
      try
      {
         if (warningWindow.size() != 0)
         {
            TextDialog dialog = new TextDialog(this, "WARNING", "", false);
            dialog.setTextFromList("Warnings from the Table Loader",
               warningWindow);
            dialog.setModal(true);
            dialog.setVisible(true);
            warningWindow.clear();
         }
         if (!allSuccessful) // ie don't show the dialog if all
            //the files are imported sucessfuly.
         {
            TextDialog dialog = new TextDialog(this, "Import File Status", "",
               false);
            dialog.setTextFromList(
            "The status of the import process is as follows:",
            importFileStatus);

            dialog.setSize(800, 400);
            dialog.addComponentListener(new ComponentAdapter()
            {
               public void componentHidden(ComponentEvent e)
               {
                if(fileImportGUI!=null)
                    fileImportGUI.setVisible(true);
               }
            });
            dialog.setModal(true);
            dialog.setVisible(true);
         }// if(!allSuccessful)
      } // try
      catch (Exception e)
      {
         DefaultUserInteractor.get().notifyOfException(this,
            "Failed to show import status dialog", e, UserInteractor.ERROR);
      }// catch
   }// importFiles()

   /** a helper method to insert a table into a tabbed pane
    */
   public void insertIntoTabbedPane(SpecialTableModel model, String fileName,
      String tabName, String fileType)
   {
      if (model == null)
      {
         return;
      }

      // System.out.println("In side insertTabbedPane()"+fileName);
      if (fileName == null)
      {
         fileName = tabName;
      }
      try
      {
         filesInTabbedPane.addFileName(fileName, tabName);
      }
      catch(Exception e)
      {
         DefaultUserInteractor.get().notify(this,"Error",e.getMessage(),UserInteractor.ERROR);
      }
      
      int noOfTabs = filesInTabbedPane.getTabCount()-1;
      // System.out.println("noOfTabs="+noOfTabs);
      String uniqueName = filesInTabbedPane.getUniqueName(noOfTabs);
      TablePanel panel = new TablePanel(this, model, fileName, fileType);

      mainTabbedPane.addTabToRight(uniqueName, null, panel, fileName);
      mainTabbedPane.setSelectedIndex(noOfTabs);
      if(closeMenuItem.isEnabled()==false)
      {
         closeMenuItem.setEnabled(true);
         exportMenuItem.setEnabled(true);
         loadConfigMenuItem.setEnabled(true);
         saveConfiguredPlotsMenuItem.setEnabled(true);
      }
   }// insertIntoTabbedPane()

   /** a helper method to initialize gui components */
   private void initialize()
   {
      // menu panel
      JPanel menuPanel = createMenuPanel();
      setTitle(TITLE);
      // JPanel componentsPanel = createATabPanel(null);//new JPanel();
      mainTabbedPane = new JTabbedPaneWithCloseIcons();
      filesInTabbedPane = new FilesInTabbedPane();
      mainTabbedPane.addMouseListener(new MouseAdapter()
      {
         public void mouseClicked(MouseEvent e)
         {
            int index = mainTabbedPane.getSelectedIndex();
            if (index != -1)
            {
               String tabTitle = mainTabbedPane.removeTab(e);
               if(tabTitle != null)
               {
                  filesInTabbedPane.remove(tabTitle);
                  if(filesInTabbedPane.getTabCount()==0)
                  {
                     closeMenuItem.setEnabled(false);
                     exportMenuItem.setEnabled(false);
                     loadConfigMenuItem.setEnabled(false);
                     saveConfiguredPlotsMenuItem.setEnabled(false);
                  }
               }
            }// if(index != -1)
         }
      });
    

/*      // Set the component to show the popup menu
      mainTabbedPane.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent evt)
        {
            //if (evt.isPopupTrigger()) {
                menu.show(evt.getComponent(), evt.getX(), evt.getY());
           // }
        }
      });
 */

      mainTabbedPane.setBorder(BorderFactory.createLoweredBevelBorder());
      JPanel componentsPanel = new JPanel();
    
      componentsPanel.setBorder(BorderFactory.createEtchedBorder());
      componentsPanel.setLayout(new BorderLayout());
      componentsPanel.add(mainTabbedPane);
      
      JPanel mainPanel = new JPanel();
 
      mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      mainPanel.setPreferredSize(new Dimension(700, 600));
      menuPanel.setMaximumSize(new Dimension(4000, 25));
      mainPanel.add(menuPanel);
      mainPanel.add(componentsPanel);
      Container container = getContentPane();
      container.add(mainPanel);
      this.addWindowListener(
         new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            dispose();
         }
      }
      );
   }// initialize()

   /** a helper method to bring up the import gui and then call importFiles()
    *
    */
   public void showImportGUI(String[] fileNames, String fileType,
      String delimiter, int noOfColumnNameRows)
   {
      if (fileImportGUI == null)
      {
         fileImportGUI = new FileImportGUI((JFrame)this, fileNames, fileType);
      }
      else
      {
         fileImportGUI.removeAllRows();
         fileImportGUI.setVisible(true);
      }
      if (fileImportGUI.isAllSet())
      {
         String[] selFileNames = fileImportGUI.getSelectedFiles();
         int startIndex = fileImportGUI.getStartTabNameIndex();
         int endIndex = fileImportGUI.getEndTabNameIndex();
         String[] tabNames = createTabNames(startIndex, endIndex, selFileNames);
         String selFileType = fileImportGUI.getSelectedFileType();
         // check whether filetype is generic file and dave output file which
         // require additional information
         delimiter = fileImportGUI.getDelimiter();
         //noOfColumnNameRows = fileImportGUI.getNoOfColumnHeaders();
         noOfColumnNameRows = 0; //dummy setting
         if(selFileType.equals(FileImportGUI.TAB_DELIMITED_FILE))
         {
            delimiter ="\t";
         }
         if (selFileNames != null)
         {
            importFiles(selFileNames, tabNames, selFileType, delimiter,
            noOfColumnNameRows);
         }// if()
      }// if(fileImportGUI.isAllSet())
   }// showImportGUI()

   
   /** helper method to create tab name
    * @pre endIndex >=1
    */
   private String[] createTabNames(int startIndex, int endIndex,
      String[] selectedFiles)
   {
      String[] tabNames = null;
      if (selectedFiles != null)
      {
         // switch the indices
         if (startIndex > endIndex)
         {
            int tempStartIndex = startIndex;
            startIndex = endIndex;
            endIndex = tempStartIndex;
         }// if

         startIndex--; // deducting 1 since user values starts from 1
         //but index starts from 0;
         tabNames = new String[selectedFiles.length];
         for (int i = 0; i < selectedFiles.length; i++)
         {
            File file = new File(selectedFiles[i]);
            String fileNameWithExt = file.getName();
            int wholeLength = fileNameWithExt.length();
            String fileNameWOExt = fileNameWithExt.substring(0, wholeLength - 4);
            int correctedEndIndex = -1;

            if (startIndex > fileNameWOExt.length())
            {
               startIndex = FileImportGUI.START_TAB_NAME_INDEX - 1; // set to 0
            }// if

            if (endIndex > fileNameWOExt.length())
            {
               correctedEndIndex = fileNameWOExt.length();
            } // if
            else
            {
               correctedEndIndex = endIndex;
            }// else
            String tabName = fileNameWOExt.substring(startIndex,
            correctedEndIndex);
            tabNames[i] = tabName;
         }// for(i)
         // check for same fileNames
         checkForDuplicateTabNames(tabNames);
      }// if(selectedFiles != null)
      return tabNames;
   }// createTabNames()

   

   /** a helper method to check whether there exist a duplicate file name and if

    * so add an integer to the name *.

    */

   private void checkForDuplicateTabNames(String[] tabNames)

   {

      int length = tabNames.length;

      int start = 1;

      int suffix = 2;

      boolean duplicate = false;

      

      for (int i = 0; i < length; i++)

      {

         String aName = tabNames[i];

         

         for (int j = start; j < length; j++)

         {

            String compareName = tabNames[j];

            

            if (aName.equalsIgnoreCase(compareName))

            {

               tabNames[j] = compareName + suffix;

               suffix++;

               duplicate = true;

            }// if

         }// for(j)

         if (duplicate)

         {

            tabNames[i] = aName + 1;

            duplicate = false;

         }// if(duplicate)

         

         suffix = 2;

         start = start + 2;

      }// for(i)

   }// checkForDuplicateTabNames()

   

   /* a helper method to create a menu bar for the gui

    */

   private JPanel createMenuPanel()

   {

      JPanel menuPanel = new JPanel(new BorderLayout());

      

      menuPanel.setBorder(BorderFactory.createEtchedBorder());

      JMenuBar menuBar = new JMenuBar();

      menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));

      

      JMenu fileMenu = new JMenu("File");

      fileMenu.setMnemonic('F');

      menuBar.add(fileMenu);

      

      JMenuItem importMenuItem = createImportMenuItem();

      importMenuItem.setAccelerator(

      KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));

      fileMenu.add(importMenuItem);

      

      JMenuItem recentfilesMenuItem = new JMenuItem("Recent Files");

      fileMenu.add(recentfilesMenuItem);

      history = new FileHistory();

      recentfilesMenuItem.setAccelerator(

      KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));

      recentfilesMenuItem.addActionListener( new ActionListener()

      {

         public void actionPerformed(ActionEvent e)

         {

            if(history.getRowCount()>0)

               FileHistoryDialog.showGUI(TableApp.this, history);

            else

               new GUIUserInteractor().notify(TableApp.this,"Recent Files",

               "No file history is currently available. ", UserInteractor.WARNING);

         }

      });

      

      queryMenuItem = createQueryMenuItem();

      queryMenuItem.setAccelerator(

         KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.ALT_MASK));

      fileMenu.add(queryMenuItem);

      

      loadConfigMenuItem = new JMenuItem("Load Configuration");
      loadConfigMenuItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
      fileMenu.add(loadConfigMenuItem);
      loadConfigMenuItem.setEnabled(false);
      loadConfigMenuItem.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            if (mainTabbedPane.getTabCount() == 0)
            {
               return;
            }
            JFileChooser chooser = new JFileChooser();
            do
            {
               int returnVal = chooser.showOpenDialog(TableApp.this);
               try
               {
                  if (returnVal == JFileChooser.CANCEL_OPTION)
                  {
                     return;
                  }
                  if (returnVal == JFileChooser.APPROVE_OPTION)
                  {
                     TablePanel panel = ((TablePanel) mainTabbedPane.
                     getSelectedComponent());
                     (panel.tablePanel).showLoadConfigGUI(
                     chooser.getSelectedFile());
                     return;
                  }
               }
               catch (Exception  ex)
               {
                  new GUIUserInteractor().notify(TableApp.this,"Error loading file",
                  ex.getMessage(), UserInteractor.ERROR);
               }
            }while (true);
         }
      });
      saveConfiguredPlotsMenuItem = new JMenuItem("Save Configuration");
      loadConfigMenuItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
      fileMenu.add(saveConfiguredPlotsMenuItem);
      saveConfiguredPlotsMenuItem.setEnabled(false);
      saveConfiguredPlotsMenuItem.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            if (mainTabbedPane.getTabCount() == 0)
            {
               new GUIUserInteractor().notify(TableApp.this,"Save Configuration", "No "+
               " table is currently being analyzed", UserInteractor.WARNING);
               return;
            }
            TablePanel panel = ((TablePanel) mainTabbedPane.
            getSelectedComponent());

            (panel.tablePanel).showSaveConfigGUI();
            return;
         }
      });
      exportMenuItem = createExportMenuItem();
      exportMenuItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
      fileMenu.add(exportMenuItem);
      exportMenuItem.setEnabled(false);

      closeMenuItem = createCloseMenuItem();
      closeMenuItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
      fileMenu.add(closeMenuItem);
      closeMenuItem.setEnabled(false);

      JMenuItem quitMenuItem = new JMenuItem("Quit");
      quitMenuItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
      fileMenu.add(quitMenuItem);
      quitMenuItem.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            try
            {
               history.saveHistory();
            }
            catch(java.io.IOException ie)
            {
               new GUIUserInteractor().notify(TableApp.this,"Error",
                  "Error saving the file history. "+ ie.getMessage(),
               UserInteractor.ERROR);
            }
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            if(standAlone)
            {
               dispose();
               System.exit(0);
            }
            else
            {
               setVisible(false);
               dispose();
            }
         }

      });

      

      JMenu editMenu = new JMenu("Edit");

      editMenu.setMnemonic('E');

      menuBar.add(editMenu);

      

      JMenuItem editTabNameItem = new JMenuItem("Rename Tabs");

      editMenu.add(editTabNameItem);

      editTabNameItem.addActionListener(new ActionListener()

      {

         public void actionPerformed(ActionEvent e)

         {

            String [] tabNames = filesInTabbedPane.getAllTabUniqueNames();

            if(tabNames==null || tabNames.length==0)

            {

               new GUIUserInteractor().notify(TableApp.this,"Rename Tabs",

               "No tabs are currently open for renaming", UserInteractor.NOTE);

               return;

            }

            

            TabNameGUI tabNameGUI = new TabNameGUI(TableApp.this,tabNames);

            if(tabNameGUI.getResult()== JOptionPane.CANCEL_OPTION)

            {

               return;

            }

            //OK_OPTION

            tabNames = tabNameGUI.getAllTabNames();

            filesInTabbedPane.setAllTabUniqueNames(tabNames);

            for(int i=0; i<tabNames.length; i++)

            {

               //System.out.println("tabNames["+i+"]="+tabNames[i]);

               mainTabbedPane.setTitleAt(i, tabNames[i]);

            }//for(i)

         }

      });

      JMenu helpMenu = HelpMenu.createStandardMenu(HelpMenu.TABLE_OF_CONTENTS, HelpMenu.TABLEAPP_WINDOW,this);

      //JMenu helpMenu = new JMenu("Help");

      //helpMenu.setMnemonic('H');

      menuBar.add(helpMenu);

      //      JMenuItem userGuideMenuItem = new JMenuItem("User Guide");

      //

      //      userGuideMenuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));

      //      helpMenu.add(userGuideMenuItem);

      //      userGuideMenuItem.addActionListener(new ActionListener()

      //      {

      //         public void actionPerformed(ActionEvent e)

      //         {

      //            JOptionPane.showMessageDialog(TableApp.this, "User Guide is not ready yet.");

      //         }

      //      });

      

      JMenuItem aboutMenuItem = new JMenuItem("About");

      helpMenu.add(aboutMenuItem);

      aboutMenuItem.addActionListener(

      new ActionListener()

      {

         public void actionPerformed(ActionEvent e)

         {

            JOptionPane.showMessageDialog(TableApp.this,

            "This version is dated " + version,

            "About the Analysis Engine",

            JOptionPane.INFORMATION_MESSAGE);

         }

      });

      menuPanel.add(menuBar, BorderLayout.WEST);

      return menuPanel;

      

   }// createMenuPanel();

   

   /** a helper method to create importMenuItem

    */

   private JMenuItem createImportMenuItem()

   {

      JMenuItem importMenu = new JMenuItem("Import");

      

      // create a file chooser

      importMenu.addActionListener(new ActionListener()

      {

         public void actionPerformed(ActionEvent e)

         {

            int noOfColumnNameRows = -1;

            

            showImportGUI(null, null, null, noOfColumnNameRows);

         }// actionPerformed()

      });

      

      return importMenu;

   }// createImportMenuItem()

   

   /** a helper method to create exportMenuItem

    */

   private JMenuItem createExportMenuItem()

   {

      JMenuItem exportMenu = new JMenuItem("Export");

      

      exportMenu.addActionListener(new ActionListener()

      {

         public void actionPerformed(ActionEvent e)

         {
            int size = mainTabbedPane.getTabCount();      
           // System.out.println("size="+size);
            // if nothing in the tabbed pane then nothing to import!
            if (size == 0)
            {
               return;
            }// if(size == 0)
            //            fileExportGUI = new FileExportGUI(TableApp.this, allTablePanelModels,
            //                 mainTabbedPane.getSelectedIndex());
            fileExportGUI = new FileExportGUI(TableApp.this,
            filesInTabbedPane.getAllTabUniqueNames(),
            mainTabbedPane.getSelectedIndex());
         }// actionPerformed()
      });
      // create a file chooser
      return exportMenu;
   }// createExportMenuItem()
   
   /** a helper method to create importQueryItem
    */
   private JMenuItem createQueryMenuItem()

   {

      JMenuItem queryMenu = new JMenuItem("Database Query");

      // create a file chooser

      queryMenu.addActionListener(new ActionListener()

      {

         public void actionPerformed(ActionEvent e)

         {

            showDBQueryViewer();

         }// actionPerformed()

      });

      return queryMenu;

   }// createQueryMenuItem()

   

   

   private void showDBQueryViewer()

   {

      DBQueryViewer viewer = new DBQueryViewer(TableApp.this);

      if(viewer.getResult() == OptionDialog.OK_RESULT)

      {

         String dbName = viewer.getDBName();

         String query = viewer.getQuery();

         String tabName = viewer.getTabName();

         try

         {

            Connection con = DBManager.openConnection(dbName);

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            addNewTab(rs, tabName);

         }

         catch(Exception e)

         {

            DefaultUserInteractor.get().notifyOfException(TableApp.this,"Error", e, UserInteractor.ERROR);

         }

         

      }

      

   }

   /** a helper method to create closeMenuItem

    */

   private JMenuItem createCloseMenuItem()

   {
      JMenuItem closeMenu = new JMenuItem("Close");
      closeMenu.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            int index = mainTabbedPane.getSelectedIndex();
            if (index >= 0)
            {
               String tabTitle = mainTabbedPane.getTitleAt(index);
               mainTabbedPane.remove(index);
               filesInTabbedPane.remove(tabTitle);
               if(filesInTabbedPane.getTabCount()==0)
               {
                  closeMenuItem.setEnabled(false);
                  exportMenuItem.setEnabled(false);
                  loadConfigMenuItem.setEnabled(false);
                  saveConfiguredPlotsMenuItem.setEnabled(false);
               }
            }
         }
      });
      return closeMenu;
   } // createCloseMenuItem()


   

  public TablePanelModel getTablePanelModel(String tabName)
   {
      TablePanel panel = (TablePanel)mainTabbedPane.getComponentAt(
      mainTabbedPane.indexOfTab(tabName));
      return panel.getTablePanelModel();
   }

   
   //   private boolean quitConfirmed(JFrame frame) {
   //        String s1 = "Quit";
   //        String s2 = "Cancel";
   //        Object[] options = {s1, s2};
   //        int n = JOptionPane.showOptionDialog(frame,
   //                "Windows are still open.\nDo you really want to quit?",
   //                "Quit Confirmation",
   //                JOptionPane.YES_NO_OPTION,
   //                JOptionPane.QUESTION_MESSAGE,
   //                null,
   //                options,
   //                s1);
   //        if (n == JOptionPane.YES_OPTION) {
   //            return true;
   //        } else {
   //            return false;
   //        }
   
   
   // public static int TEMP = 0;
   // public static void createApp(String [] fileNames, String fileType, String delimiter)
   // {
   // final String [] fns = fileNames;
   // final String ft = fileType;
   // final String delim = delimiter;
   //
   // javax.swing.SwingUtilities.invokeLater(new Runnable()

  // {
   // public void run()
   // {
   // final javax.swing.JFrame f = new javax.swing.JFrame("Sort Filter Table Application ");
   // final java.awt.Container contentPane = f.getContentPane();
   // TableApp panel = new TableApp(fns, ft, delim, false);
   // contentPane.add(panel);
   // contentPane.setLayout(new javax.swing.BoxLayout(contentPane, javax.swing.BoxLayout.Y_AXIS));
   // f.pack();
   // Point point = ScreenUtils.getPointToCenter(f);
   // f.setLocation(point);
   // //f.setLocation((int)point.getX()/2, (int)point.getY()/2);
   // //f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
   // f.setVisible(true);
   // }
   // });
   // }
   //
   
   public static boolean savePlots(String fileName, String fileType,
      String delimiter, int noOfColHdrRows, int startPos, int endPos,
      String configFile, String outputDir, int plotFmt, boolean ignoreTableConfig)
   {
      OverallTableModel model;
      try
      {
         try
         {
            model = new OverallTableModel(FileImportGUI.createAModel(
               fileName, fileType, delimiter, noOfColHdrRows));
         }
         catch(Exception e)
         {
            System.out.println("Error reading the file \n"+ e.getMessage());
            return true;
         }
         AnalysisConfiguration aconfig = new AnalysisConfiguration(model);
         aconfig.loadConfiguration(new File(configFile), !ignoreTableConfig);
         aconfig.saveConfiguredPlots(outputDir, plotFmt,aconfig.getConfigNames());
      }
      catch(Exception e)
      {
         System.out.println("Error during plot generation\n");
         System.out.println(e.getMessage()+ "\nBringing up the GUI\n");
         return false;
      }
      return true;
   }//savePlots

   

   /** Add a new tab and show the resultset in a sort filter table panel 
    *@param rs A ResultSet 
    *@param tabName A String name of the tab
    *@pre rs != null and not empty
    */
   public void addNewTab(ResultSet rs,String tabName){
      ResultSetTableModel rsModel = new ResultSetTableModel(rs);
      insertIntoTabbedPane(rsModel, tabName, tabName, "db");
   }
   
   /** Add a new tab and show the resultset in a sort filter table panel 
    *@param rs A ResultSet 
    *@param tabName A String name of the tab
    *@pre rs != null and not empty
    */
   public void addNewTab(ResultSet rs,File configFile,String tabName)
   {
      ResultSetTableModel rsModel = new ResultSetTableModel(rs);
      insertIntoTabbedPane(rsModel, tabName, tabName, "db");
      if(configFile != null)
      {
         TablePanel newPanel = (TablePanel)mainTabbedPane.getSelectedComponent();
         newPanel.tablePanel.loadConfigFile(configFile, true);
      }
   }
   /**
    * @param args the command line arguments
    */
   public static void main(String[] arg)
   {
      final String[] args = arg;
      TableApp frame = null;
      TableApp.standAlone = true;
      if (args.length == 0)
      {
         frame = new TableApp();
      }
      else
      {
         try
         {
            FileAdapter fileAdapter = new FileAdapter(args);
            String[] fileNames = fileAdapter.fileNames;
            //creates plots from the command line
            if(fileAdapter.configFile!=null && fileAdapter.outputDir!=null)
            {
               if(TableApp.savePlots(
                  fileNames[0],
                  fileAdapter.fileType,
                  fileAdapter.delimiter,
                  fileAdapter.noOfColumnHeader,
                  fileAdapter.startPos,   
                  fileAdapter.endPos,
                  fileAdapter.configFile,
                  fileAdapter.outputDir,
                  fileAdapter.plotFmt,
                  fileAdapter.ignoreTableConfig
                  ))
               {
                  System.exit(0);
               }
            }//if(fileAdapter.configFile!=null && fileAdapter.outputDir!=null
            frame = new TableApp(
               fileNames,
               fileAdapter.fileType,
               fileAdapter.delimiter,
               fileAdapter.noOfColumnHeader,
               fileAdapter.startPos,
               fileAdapter.endPos
            );
            if(fileAdapter.configFile!=null && fileAdapter.outputDir == null)
            {
               try
               {
                  TablePanel panel = (TablePanel)frame.mainTabbedPane.getSelectedComponent();
                  (panel.tablePanel).loadConfigFile(
                     new File(fileAdapter.configFile),
                     !fileAdapter.ignoreTableConfig
                   );
                  if(fileAdapter.showPlots)
                  {
                     (panel.tablePanel).showPlots();
                  }
               }//try
               catch(Exception e)
               {
                  System.out.println("Error while loading TableApp"+ e.getMessage());
                  System.exit(0);
               }
            }//if(fileAdapter.configFile!=null && fileAdapter.outputDir == null)
         }//try
         catch (Exception e)
         {
            System.err.println(e.getMessage());
            FileAdapter.printUsage();
            //e.printStackTrace();
            System.exit(1);
         }//catch
      }// else
   }// main()

   

   public void dispose()
   {
      try
      {
         history.saveHistory();
      }
      catch(java.io.IOException ie)
      {
         new GUIUserInteractor().notify(TableApp.this,"Error",
         "Error saving the file history. "+
         ie.getMessage(), UserInteractor.ERROR);
      }
   }
   
   protected void finalize()
   {
      try
      {
         history.saveHistory();
      }
      catch(java.io.IOException ie)
      {
         new GUIUserInteractor().notify(TableApp.this,"Error",
         "Error saving the file history. "+
        ie.getMessage(), UserInteractor.ERROR);
     }
   }
}



