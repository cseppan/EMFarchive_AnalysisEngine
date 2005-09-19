package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.GUIUserInteractor;
import gov.epa.mims.analysisengine.gui.UserInteractor;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;

import java_cup.runtime.Symbol;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Class FileHistory - It stores and loads from Files.List file information related to importing of File.
 * User can select files from a list shown using FileHistoryDialog and load them in the TableApp.
 *
 * @author  Krithiga Thangavelu, CEP, UNC CHAPEL HILL.
 * @version $Id: FileHistory.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 *
 */

public class FileHistory extends DefaultTableModel
{

/** The file from which file history was loaded */
private File file;

/** Column names */
Vector columnNames = new Vector(Arrays.asList(new String[]
{"File Type", "Directory", "File Name", "Delimiter",
 "Column Header Rows"}));

/** create an instance of FileHistory
 */

public FileHistory()
{
   initialize();
}

/** To prevent editing of cells
 */

public boolean isCellEditable(int row, int column)
{
 return false;
}

/** identifies the file from which the history has to be loaded
 *  @param None
 * @return None
 */

private void initialize()
{

   String filename = System.getProperty("RecentFiles");

   if(filename == null)
      filename = System.getProperty("user.dir")+File.separator+"Files.List";
   setColumnIdentifiers(columnNames);
   file = new File(filename);
   if(!file.exists()) {
      try {
      file.createNewFile();
      } catch(IOException e) {}
   } else
      loadHistory();
}


/** loads history from file
 *  @param None
 *  @return None
 */

private void loadHistory()
{
   Symbol[] lineTokens;
   Vector fileData = new Vector();

   try {
   FileScanner scanner = new FileScanner(new FileReader(file));
   while((lineTokens=scanner.getTokensPerLine(';', false))!=null)
   {
      FileInfo rowValues = getObjects(lineTokens);
      fileData.add(rowValues);
   }
   setDataVector(fileData, columnNames);
   } catch( Exception e) {
            new GUIUserInteractor().notify(null,"Error with Recent Files List",
          e.getMessage(), UserInteractor.ERROR);
   }
}

/**  extracts an array of objects from the lineTokens
 * @param lineTokens Symbol[]
 * @return FileInfo
 */
private FileInfo getObjects(Symbol[] lineTokens) throws Exception
{
    Object[] values = new Object[5];
    if(lineTokens.length==1) return null;
    if(lineTokens.length!=5) throw new Exception("Unexpected record type");
    for(int i=0; i<lineTokens.length; i++)
    if(lineTokens[i].sym != TokenConstants.NULL_LITERAL)
        values[i]=(lineTokens[i].value);
    return new FileInfo((String)values[2], (String)values[1],
      (String)values[3], ((Integer)values[4]).intValue(),
      (String)values[0]);
}


/**  adds to the set of recent files the file "fullname" with other information
 *   @param fileType String
 *   @param fullname String
 *   @param delim String
 *   @param numColHdrRows int
 *   @return None
 */

public void addToHistory(String filetype, String fullname,
      String delim, int numColHdrRows)
{
   Vector data = getDataVector();
   String filename = fullname.substring(fullname.lastIndexOf(File.separatorChar)+1, fullname.length());
        String path = fullname.substring(0, fullname.lastIndexOf(File.separatorChar));
   FileInfo info = new FileInfo(filename, path, delim, numColHdrRows, filetype);
   if(data.contains(info)) return;
   else {
      addRow(info);
   }
}

/** saves the history to the file from which it was originally
    loaded or to the Files.List in the current directory
    @param None
    @return None throws IOException if unsuccessful
*/

public void saveHistory() throws IOException
{
   FileOutputStream outputstream = new FileOutputStream(file);
   Vector data = getDataVector();
   for(int i=0; i< data.size(); i++) {
      FileInfo info = (FileInfo)data.get(i) ;
      info.print((OutputStream)outputstream);
   }
   outputstream.close();
}

}

