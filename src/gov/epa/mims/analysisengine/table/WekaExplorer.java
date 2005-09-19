package gov.epa.mims.analysisengine.table;

import weka.gui.explorer.*;
import weka.core.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import gov.epa.mims.analysisengine.gui.*;

public class WekaExplorer extends Explorer 
{

 private Component parent;

 public WekaExplorer(Component parent, Instances data)
 {
   this.parent = parent;
   m_PreprocessPanel.setInstances(data);
 }

 public void showGUI()
 {
    try {
      final JFrame jf = new JFrame("Weka Explorer");
      jf.getContentPane().setLayout(new BorderLayout());
      jf.getContentPane().add(this, BorderLayout.CENTER);
      jf.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
           jf.dispose();
         }
      });
      jf.pack();
      jf.setSize(800, 600);
      jf.setVisible(true);
   }
   catch(Exception ie) 
   {
      new GUIUserInteractor().notify(this,"Error", "Error occured while running "+
         "Weka Explorer. "+ ie.getMessage(), UserInteractor.ERROR);
      return;
   }
 }

}

