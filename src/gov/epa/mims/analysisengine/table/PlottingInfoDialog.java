package gov.epa.mims.analysisengine.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import gov.epa.mims.analysisengine.gui.StringChooserPanel;
import gov.epa.mims.analysisengine.gui.StringValuePanel;
import gov.epa.mims.analysisengine.gui.StringEditableTablePanel;

/**
 * This Dialog comes up when the user clicks the "Plot" button
 *
 * @author Prashant Pai, CEP UNC
 * @version $Id: PlottingInfoDialog.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */

public class PlottingInfoDialog extends JDialog
{

  /** the plotting info object for this GUI **/
  protected PlottingInfo plottingInfo = null;

  /**
   *
   * @param plottingInfo
   */
  public PlottingInfoDialog(PlottingInfo plottingInfo)
  {
    this.plottingInfo = plottingInfo;
    setTitle("Select Plotting Options");
    initialize();
    pack();
  }

  private void initialize()
  {
    Container contentPane = this.getContentPane();

    contentPane.setLayout(new BorderLayout());

    JPanel plotTypePanel = new StringChooserPanel("Plot Type: ", false,
        gov.epa.mims.analysisengine.AnalysisEngineConstants.PLOT_NAMES);

    contentPane.add(plotTypePanel, BorderLayout.NORTH);

    JPanel columnSelectionPanel = new JPanel(new BorderLayout());

    JPanel dataColumnPanel = new JPanel();
    dataColumnPanel.add(new JLabel("Select Data Columns:"));
    dataColumnPanel.add(new JButton(new SelectAction()));
    columnSelectionPanel.add(dataColumnPanel, BorderLayout.NORTH);

    JPanel labelColumnPanel = new JPanel();
    labelColumnPanel.add(new JLabel("Select Label Columns:"));
    labelColumnPanel.add(new JButton(new SelectAction()));
    columnSelectionPanel.add(labelColumnPanel, BorderLayout.SOUTH);

    JPanel nameUnitsPanel = new JPanel(new BorderLayout());

    JPanel namePanel = new JPanel();
    namePanel.add(new JLabel("Names:"));
    JButton nameSelectButton = new JButton("Select");
    nameSelectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JDialog nameDialog = new JDialog();
        nameDialog.getContentPane().add(new StringEditableTablePanel("Name"));
        nameDialog.show();
      }//actionPerformed()
    });//addActionListener()
    namePanel.add(nameSelectButton);
    //namePanel.setPreferredSize(new Dimension(100,50));
    nameUnitsPanel.add(namePanel, BorderLayout.NORTH);

    StringValuePanel unitsPanel = new StringValuePanel("Units", false);
    nameUnitsPanel.add(unitsPanel, BorderLayout.SOUTH);

    contentPane.add(columnSelectionPanel, BorderLayout.CENTER);
    contentPane.add(namePanel, BorderLayout.SOUTH);
  }//initialize()

  public static void main(String[] args)
  {
  }

  /**
   * Select Multiple Columns
   */
  class SelectAction extends AbstractAction
  {
      public SelectAction()
      {
           super("Select");
      } // SortMultipleAction()

      public void actionPerformed(ActionEvent e)
      {
        JDialog columnSelectDialog = new JDialog();
        //String[][] columnHeaders = plottingInfo.getModel();
        ColumnSelectionPanel columnSelectPanel  = new ColumnSelectionPanel(new String[][]{});
        columnSelectDialog.getContentPane().add(columnSelectPanel);
      } // actionPerformed()
 } // class PlotAction

}

