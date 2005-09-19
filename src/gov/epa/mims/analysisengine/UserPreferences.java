
package gov.epa.mims.analysisengine;

import java.util.Properties;
import java.util.MissingResourceException;
import java.io.FileInputStream;
import java.io.File;
/*
 * UserPreferences.java
 * Please define the constants for each preference described in the user pref text file
 * in this class. This will be used as key in the relevant class to access the preference.
 * Created on May 17, 2005, 12:01 PM
 * @author  Parthee R Partheepan
 */
public class UserPreferences extends Properties
{
   /**
    * access the user preferences
    */
   public static UserPreferences USER_PREFERENCES = null;
   
   
   /**
    *formatting double
    */
   //keys
   public static final String FORMAT_DOUBLE_SIGNIFICANT_DIGITS="format.double.significant_digits";
   public static final String FORMAT_DOUBLE_DECIMAL_PLACES = "format.double.decimal_places";
   public static final String FORMAT_OPTION = "format.double.option";
   
   
   static
   {
      USER_PREFERENCES = new UserPreferences();
      try
      {
         String fileName = System.getProperty("USER_PREFERENCES");
         if(fileName != null && fileName.trim().length()>=0)
         {
            fileName = fileName.trim();
            File file = new File(fileName);
            if(file.exists() && file.isFile())
            {
               
               FileInputStream inStream = new FileInputStream(file);
              
               USER_PREFERENCES.load(inStream);
            }
         }
      }
      catch(Exception e)
      {
         //do nothing
      }
   }
}
