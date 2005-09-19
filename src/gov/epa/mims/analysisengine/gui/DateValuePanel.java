package gov.epa.mims.analysisengine.gui;

import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.text.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Vector;

/**
 * An editor that allows only dates in some format to be entered.
 *
 * @author Daniel Gatti
 * @version $Id: DateValuePanel.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */
public class DateValuePanel extends JPanel
{
   /** the label **/
   private String label = null;

   /** The JLabel that describes the value on this panel. */
   protected JLabel jLabel = null;

   /** The text field for entering the Date. */
   protected JTextField valueField = null;

   /** The unit label to describe the Date units. */
   private String unitLabel = null;

   /** the JLabel for the aboeve label **/
   private JLabel jUnitLabel = null;

   /** The lower bound if one is required **/
   private long lowBound;

   /** The upper bound if one is required **/
   private long upBound;
   
   private String preToolTipText = "";

//   /** a hashmap of format strings to SimpleDateFormat objects */
//   static private HashMap dateFormatters = new HashMap(20);
//
//   /** a vector date format strings */
//   static private Vector dateFormats = new Vector(20);
//
   /** A default date format to use when none is provided. */
   public static final String DEFAULT_DATE_FORMAT_STR = "MM/dd/yyyy";
   
   /** The DateFormat to use in validating dates in this class.  */
   protected SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT_STR);
//
//   // Set up a list of date formats that we can support.
//   static
//   {
//      dateFormats.add("yyyy/MM/dd");
//      dateFormats.add("yyyyMMdd");
//      dateFormats.add("MM/dd/yyyy");
//      dateFormats.add("MMddyyyy");
//      dateFormats.add("yyyyDDD");
//      dateFormats.add("yyyy/MM/dd HH:mm:ss");
//      dateFormats.add("yyyyMMdd HH:mm:ss");
//      dateFormats.add("MM/dd/yyyy HH:mm:ss");
//      dateFormats.add("MMddyyyy HH:mm:ss");
//      dateFormats.add("yyyyDDD HH:mm:ss");
//      dateFormats.add("MM/dd/yyyy HH:mm:ss zzz");
//      dateFormats.add("HH:mm:ss");
//      dateFormats.add("HH:mm");
//      dateFormats.add("HH");
//
//      // TBD: need to handle time zones eventually
//      for (int f = 0; f < dateFormats.size(); f++)
//      {
//         String thisFormat = (String)dateFormats.get(f);
//         SimpleDateFormat sdf = new SimpleDateFormat(thisFormat);
//         sdf.setLenient(false);
//         dateFormatters.put(thisFormat, sdf);
//      }
//   } // static


   /**
    * @param label String that is the text to place next to the text field.
    * @param unitLabel A String type that specifies the unit of the parameter to be displayed
    * @param labelOnTop boolean that is true if the label should be on top.
    * @param lowerBound long that is the lowest Date that we will accept.
    * @param upperBound long that is the highet Date that we will accept.
    * @param dateFormatString String that is one of the strings in the static dateFormats HashMap.
    */
   public DateValuePanel(String label, String unitLabel, boolean labelOnTop, long lowerBound,
                         long upperBound, String dateFormatString)
   {
      initialize(label, labelOnTop, lowerBound, upperBound, dateFormatString);
      this.unitLabel = unitLabel;
      this.setToolTipText(unitLabel);
      //jUnitLabel = new JLabel(unitLabel);
      //add(jUnitLabel);
   } // DateValuePanel()


   /**
    * @param label String that is the text to place next to the text field.
    * @param labelOnTop boolean that is true if the label should be on top.
    * @param lowerBound long that is the lowest Date that we will accept.
    * @param upperBound long that is the highet Date that we will accept.
    */
   public DateValuePanel(String label, boolean labelOnTop, long lowerBound,
                         long upperBound, String dateFormatString)
   {
      initialize(label, labelOnTop, lowerBound, upperBound, dateFormatString);
      this.unitLabel = unitLabel;
      //jUnitLabel = new JLabel(unitLabel);
      //add(jUnitLabel);
   } // DateValuePanel()


   /**
    * Constructor.
    * @param label
    * @param labelOnTop
    * @param String dateFormatString
    */
   public DateValuePanel(String label, boolean labelOnTop, String dateFormatString)
   {
      this(label, labelOnTop, Long.MIN_VALUE, Long.MAX_VALUE, dateFormatString);
   } // DateValuePanel()


   /**
    * Constructor.
    * @param label
    * @param labelOnTop
    * @param String dateFormatString
    */
   public DateValuePanel(String label, boolean labelOnTop)
   {
      this(label, labelOnTop, Long.MIN_VALUE, Long.MAX_VALUE, DEFAULT_DATE_FORMAT_STR);
   } // DateValuePanel()


   /**
    * Constructor.
    * @param label
    * @param units String that is the units label to use.
    * @param labelOnTop
    */
   public DateValuePanel(String label, String units, boolean labelOnTop)
   {
      this(label, units, labelOnTop, Long.MIN_VALUE, Long.MAX_VALUE, DEFAULT_DATE_FORMAT_STR);
   } // DateValuePanel()


   /**
    * A private method which contains the common piece of code for the above two
    * constructors.
    */
   private void initialize(String label, boolean labelOnTop, long lowerBound,
                           long upperBound, String dateFormatString)
   {
      this.label = label;
      dateFormat.applyPattern(dateFormatString.trim());
//      if (dateFormat == null)
//      {
//         DefaultUserInteractor.get().notify("Invalid Date Format",
//                     "A date formatter was not found for the format '" +
//                     dateFormatString + "'.", UserInteractor.ERROR);
//      }

      this.lowBound = lowerBound;
      this.upBound = upperBound;
      setAlignmentX(JPanel.CENTER_ALIGNMENT);
      setAlignmentY(JPanel.CENTER_ALIGNMENT);
      jLabel = new JLabel(label);
      valueField = new JTextField(5);
      valueField.setMaximumSize(new Dimension(200, 20));

      InputVerifier dateVerifier = new InputVerifier()
      {
         public boolean verify(JComponent input)
         {
            Date enteredValue;
            try
            {
               String entText = ((JTextField)input).getText();
               if (entText.trim().length() == 0)
               {
                  return true;
               }
               enteredValue = dateFormat.parse(entText);
            }
            catch(Exception e)
            {
               DefaultUserInteractor.get().notify(DateValuePanel.this, "Error verifying input value",
                     "Please enter a valid date in the format (" +
                     dateFormat.toPattern() + ").", UserInteractor.ERROR);
               return false;
            }
            // Check the bounds.
            if (enteredValue.getTime() < lowBound || enteredValue.getTime() > upBound)
            {
               String message = null;
               if (upBound == Long.MAX_VALUE)
                  message = "Please enter a date > " + dateFormat.format(new Date(lowBound));
               else
                  message = "Please enter a date between " + dateFormat.format(new Date(lowBound))
                  + " and " + dateFormat.format(new Date(upBound));

               DefaultUserInteractor.get().notify(DateValuePanel.this,"Error verifying input value",
                     message, UserInteractor.ERROR);
               return false;
            }
            else
               return true;
         }//verify()
      };
      valueField.setInputVerifier(dateVerifier);

      // Set up the X or Y axis as the layout type.
      int layoutType = BoxLayout.X_AXIS;
      if (labelOnTop)
         layoutType = BoxLayout.Y_AXIS;

      setLayout(new BoxLayout(this, layoutType));
      add(jLabel);
      if (labelOnTop)
         add(Box.createVerticalStrut(5));
      else
         add(Box.createHorizontalStrut(5));
      add(valueField);

      setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
   } // initialize()


   /**
    * return the value in the textfield - NaN if empty
    * @return the verified value in the textfield
    */
   public Date getValue()
   {
      if (valueField.getText().trim().length() == 0)
         return null;

      Date retval = null;
      try
      {
         retval = dateFormat.parse(valueField.getText());
      }
      catch (ParseException pe)
      {
         DefaultUserInteractor.get().notify(DateValuePanel.this,"Error verifying input value",
               pe.getMessage(), UserInteractor.ERROR);
      }

      return retval;
   } //getValue()


   /**
    * Set the double value in this panel.
    * @param val the value to be set
    */
   public void setValue(Date val)
   {
      if (val == null)
      {
         valueField.setText("");
      }
      else
      {
         valueField.setText(dateFormat.format(val));
      }
   } //setValue()


   /**
    * Set a new date formatter for this panel.
    */
   public void setDateFormatter(SimpleDateFormat newFormat)
   {
      dateFormat = newFormat;
//      if (jUnitLabel != null && newFormat != null)
//        jUnitLabel.setText(newFormat.toPattern());
      if(newFormat != null)
      {
        String pattern = newFormat.toPattern();
        String toolTip = preToolTipText + "("+ pattern+ ")";
        valueField.setToolTipText(toolTip);
        jLabel.setToolTipText(toolTip);
      }
   }


   /**
    * Set the usability of the textfield within the panel.
    * @param enable to enable the textfield or not
    */
   public void setEnabled(boolean enable)
   {
      jLabel.setEnabled(enable);
      valueField.setEnabled(enable);
      if (jUnitLabel != null)
      {
         jUnitLabel.setEnabled(enable);
      }

   } //setEnabled()


   /**
    * Set a new label to display next to the text field.
    * @param str String that is the new text to display.
    */
   public void setLabel(String str)
   {
      jLabel.setText(str);
   }


   /**
    * Set the tool tip to appear over all of the components in
    * the panel.
    */
   public void setToolTipText(String newText)
   {
      preToolTipText = newText;
      String toolTip = preToolTipText + "("+  dateFormat.toPattern()+ " )";
      //this.setToolTipText(toolTip);
      jLabel.setToolTipText(toolTip);
      valueField.setToolTipText(toolTip);
      if (jUnitLabel != null)
      {
         jUnitLabel.setToolTipText(toolTip);
      }
   }
} // class DateValuePanel

