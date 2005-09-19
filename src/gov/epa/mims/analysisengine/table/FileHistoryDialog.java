package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.ScreenUtils;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

/**  FileHistoryDialog - present the Recent Files list for
 *   loading and editing to the user.
 *
 *   @author  Krithiga Thangavelu, CEP, UNC CHAPEL HILL.
 *   @version $Id: FileHistoryDialog.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */

public class FileHistoryDialog extends javax.swing.JDialog {

       /** Close button for the panel */
   private JButton bClose;

       /** Clear selection button for the panel */
   private JButton bClear;

       /** Import selected files button for the panel */
   private JButton bImport;

        /** delete the selected files from history button */
   private JButton bDelete;

        /** panel to have the above operator buttons */
   private JPanel OperatorPanel;

        /** table displaying recent files */
   private JTable FilesTable;

        /** Scroll panel holding the recent files */
   private JScrollPane ScrollPanel;

        /** Panel containing the Scroll pane */
   private JPanel FilesDisplayPanel;

        /** outer panel enclosing operator panel and Files display panel */
   private JPanel OverallPanel;

        /** The History object */
   private TableSorter history;

        /** The TableApp to which the history belongs */
   private TableApp app;

   /** creates an instance of the FileHistoryDialog
     * @param app TableApp
     * @param history FileHistory
     */

   FileHistoryDialog(TableApp app, FileHistory history) {
   super(app);
   this.app = app;
   this.history = new TableSorter(history);
   initGUI();
   setLocation(ScreenUtils.getPointToCenter(this));

   }

   /** initializes GUI */

   private void initGUI() {

   OverallPanel = new JPanel();
   FilesDisplayPanel = new JPanel(new BorderLayout());
   ScrollPanel = new JScrollPane();
   FilesTable = new JTable() {
     public String getToolTipText(java.awt.event.MouseEvent event) {
        int row = rowAtPoint(event.getPoint());
        int col = columnAtPoint(event.getPoint());
        Object o = getValueAt(row,col);
        if( o == null )
            return null;
        if( o.toString().equals("") )
            return null;
        return o.toString();
      }
    };

    OperatorPanel = new JPanel();
    bDelete = new JButton();
    bImport = new JButton();
    bClear = new JButton();
    bClose = new JButton();

   this.setResizable(true);
   this.setSize(new java.awt.Dimension(445, 340));
   this.setTitle("Import Recent Files");

    BorderLayout thisLayout = new BorderLayout();
    this.getContentPane().setLayout(thisLayout);
    thisLayout.setHgap(5);
    thisLayout.setVgap(5);

    BoxLayout blayout = new BoxLayout(OverallPanel, 1);
    this.getContentPane().add(OverallPanel, BorderLayout.CENTER);

    //OverallPanel.setLayout(blayout);
    OverallPanel.setLayout(new BorderLayout());

    OverallPanel.setPreferredSize(new java.awt.Dimension(422,280));
    OverallPanel.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10));

    OverallPanel.add(FilesDisplayPanel, BorderLayout.CENTER);

    FilesDisplayPanel.setPreferredSize(new java.awt.Dimension(383,220));

    FilesDisplayPanel.add(ScrollPanel, BorderLayout.CENTER);

    ScrollPanel.setPreferredSize(new java.awt.Dimension(360,200));
    ScrollPanel.add(FilesTable);

    FilesTable.setModel(history);
    FilesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    history.setTableHeader(FilesTable.getTableHeader());
    FilesTable.addMouseListener(new MouseListener()
      {
         public void mouseClicked(MouseEvent e)
         {
            if(e.getClickCount()>1)
            {
               bImportActionPerformed(new ActionEvent(this, 0, ""));
               bCloseActionPerformed(new ActionEvent(this, 0, ""));
            }
         }

         public void mousePressed(MouseEvent e)
         {
         }

         public void mouseReleased(MouseEvent e)
         {
         }

         public void mouseExited(MouseEvent e)
         {
         }

         public void mouseEntered(MouseEvent e)
         {

         }
     });

   TableColumn column = null;
   for (int i = 0; i < 5; i++) {
      column = FilesTable.getColumnModel().getColumn(i);
      column.setPreferredWidth(125);
   }

   ScrollPanel.setViewportView(FilesTable);

   FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 5, 5);

   OperatorPanel.setPreferredSize(new java.awt.Dimension(422,40));
   OperatorPanel.setLayout(flow);
   OperatorPanel.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5));

   flow.layoutContainer(OperatorPanel);

   bImport.setSize(new java.awt.Dimension(90,40));
   bImport.setToolTipText("Import the selected files");
   bImport.setText("Import");
   bImport.setVisible(true);
   bImport.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent evt) {
      bImportActionPerformed(evt);
   }
   });
   OperatorPanel.add(bImport);

   bDelete.setText("Remove");
   bDelete.setSize(new java.awt.Dimension(90,40));
   bDelete.setToolTipText("Delete the selected files from history");
   bDelete.setVisible(true);
   bDelete.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent evt) {
      bDeleteActionPerformed(evt);
   }
 });
 OperatorPanel.add(bDelete);

 bClear.setSize(new java.awt.Dimension(90,40));
 bClear.setToolTipText("Clear the selection");
 bClear.setText("Clear");
 bClear.setVisible(true);
 bClear.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent evt) {
      bClearActionPerformed(evt);
   }
 });
 OperatorPanel.add(bClear);

 bClose.setSize(new java.awt.Dimension(90,40));
 bClose.setToolTipText("Close this window");
 bClose.setText("Close");
 bClose.setVisible(true);
 bClose.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent evt) {
      bCloseActionPerformed(evt);
   }
 });
 OperatorPanel.add(bClose);

 OverallPanel.add(OperatorPanel, BorderLayout.SOUTH);

}

protected void bDeleteActionPerformed(ActionEvent evt) {
  int[] indexs = FilesTable.getSelectedRows();
  for(int i=indexs.length-1; i>=0; i--) {
   history.remove(indexs[i]);
  }
}

protected void bImportActionPerformed(ActionEvent evt) {
 int[] indexs = FilesTable.getSelectedRows();
 String[] filename = new String[indexs.length];
 String[] delimiter = new String[indexs.length];
 String[] fileType = new String[indexs.length];
 int[] numColHdrRows = new int[indexs.length];

 for(int i=0; i< indexs.length; i++) {
   filename[i] = (String)history.getValueAt(indexs[i],
1)+File.separator+(String)history.getValueAt(indexs[i], 2);
   delimiter[i] = (String)history.getValueAt(indexs[i], 3);
   fileType[i]= (String)history.getValueAt(indexs[i], 0);
   numColHdrRows[i]=((Integer)history.getValueAt(indexs[i], 4)).intValue();
 }
 try {
   app.importDifferentFiles(filename, fileType , delimiter, numColHdrRows, 1, 40);
 } catch(Exception e) { }
}

protected void bClearActionPerformed(ActionEvent evt) {
  FilesTable.clearSelection();
}

protected void bCloseActionPerformed(ActionEvent evt) {
 dispose();
 return;
}

public static void showGUI(TableApp app, FileHistory history) {

  FileHistoryDialog hisDialog= new FileHistoryDialog(app, history);
  hisDialog.setVisible(true);

}

public static void main(String[] args) {

   FileHistoryDialog.showGUI(null, new FileHistory());

}

}
