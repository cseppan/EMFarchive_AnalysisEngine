
package gov.epa.mims.analysisengine.table;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * TestSuite that runs all the sample tests
 * @author Parthee Partheepan
 * @version $Id: TestAll.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $
 */

public class TestAll
{
   static final String sepr = File.separator;

   /** src installation dir. This string has to be changed according to the
    * installation dir
    */
   static String TESTHOME= null;

   /**
    * main method for JUnit test
    *
    * @param args not used
    */
   public static void main(String[] args)
   {
      TESTHOME = System.getProperty("TESTHOME");
      if(TESTHOME == null )
      {
         System.err.println("The TESTHOME variable is not set." +
         "Please set the src installation directory for TESTHOME.");
         System.exit(1);
      }

      File file = new File(TESTHOME);
      if(!file.isDirectory())
      {
         System.err.println(TESTHOME + " is not a directory. \nPlease specify " +
         "the src installation directory.");
         System.exit(1);
      }//if(!file.isDirectory())

      //System.setProperty("TESTHOME",TESTHOME);
      //junit.textui.TestRunner.run(suite());
      junit.swingui.TestRunner.run(TestAll.class);
   }


   /**
    * add test suites to the list of all test suites
    *
    * @return TestSuite
    */
   public static Test suite()
   {
      TestSuite suite = new TestSuite("All JUnit Tests");

         suite.addTest(TableTest.suite());
         suite.addTest(TestFileAdapter.suite());
         suite.addTest(FileScannerTest.suite());
         suite.addTest(FileParserTest.suite());
         suite.addTest(TestSignificantDigitsFormat.suite());
         return suite;

   }//suite

}//TestAll
