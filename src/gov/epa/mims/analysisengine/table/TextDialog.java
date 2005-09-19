package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.gui.DefaultUserInteractor;
import gov.epa.mims.analysisengine.gui.UserInteractor;
import gov.epa.mims.analysisengine.gui.ScreenUtils;

import java.awt.event.*;
import java.awt.*;
import java.beans.*;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.html.*;

/**
 * A GUI for showing text or HTML in an editor pane
 *
 * @author Alison Eyth
 * @version $Id: TextDialog.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 *
 */
public class TextDialog extends EditingDialog
{
   /** whether the text is editable */
   private boolean editable;

   /** whether text has been changed */
   private boolean changed;

   private TextArea textArea = new TextArea();

   private Component myCaller = null;

   /**
    * Constructor.
    * @author Alison Eyth
    *
    *******************************************************************/
   public TextDialog(Component myCaller, String title, String text, boolean editable)
   throws Exception
   {
      super(title, "OK", false);
      // Create reference from PropertyTypeGUI to the PropertyType
      // it refers to
      this.editable = editable;
      this.myCaller = myCaller;
      textArea.setEditable(editable);
      textArea.setBackground(Color.white);
      changed = false;
      super.jbInit();
      jbInit();
   }

   public void setText(String newText)
   {
      textArea.setText(newText);
   }

   public void setTextFromList(String prefix, ArrayList list)
   {
      String newText = prefix + "\n";
      for (int i = 0; i < list.size(); i++)
      {
        Object curr = list.get(i);
        newText = newText + curr.toString() + "\n";
      }
      textArea.setText(newText);
   }

   /**
    * Update the GUI after something has changed in the corresponding object.
    *
    * @param int updateCode A code indicate what type of change occurred
    * @param Object arg An object to operate on
    *
    * @author Alison Eyth
    */
   public void update(int updateCode, Object arg)
   {
      //System.out.println("Description GUI updateCode = "+updateCode);
      switch(updateCode)
      {
      }
   }


   JScrollPane jScrollPane1 = new JScrollPane(
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

   class KeyTyped implements java.awt.event.KeyListener
   {
      public void keyReleased(KeyEvent e) {
         if (!changed && editable)
         {
            setHasChanged();
         }
      }
      public void keyPressed(KeyEvent e) {
         if (!changed && editable)
         {
            setHasChanged();
         }
      }
      public void keyTyped(KeyEvent e) {
         if (!changed && editable)
         {
            setHasChanged();
         }
      }
   }

   KeyTyped keyListener = new KeyTyped();

   protected void jbInit() throws Exception
   {
      this.getContentPane().add(textArea, BorderLayout.CENTER);
      this.setSize(500, 200);
      //editorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      this.pack();
      setLocation(ScreenUtils.getPointToCenter(this));
   }

   /**
    * Called by EditingDialog when the Accept / OK button is pressed
    *
    * @returns boolean whether it is OK to close the EditingDialog
    */
   protected boolean acceptChanges()
   {
      if (myCaller != null) myCaller.repaint();
      return true;
   }

   /**
    * Called by EditingDialog when the Help button is pressed
    *
    */
   protected void getHelp()
   {
   }

   /**
    * Called by EditingDialog when the Close/Cancel button is pressed
    *
    */
   protected void discardChanges()
   {
      if (myCaller != null)
      {
         myCaller.repaint();
         if (myCaller instanceof Window) ((Window)myCaller).toFront();
      }
   }
}

