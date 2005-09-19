package gov.epa.mims.analysisengine.table;

import junit.framework.*;

public class TestSignificantDigitsFormat extends TestCase {

   /** the formatter on which all the tests will be conducted **/
   private SignificantDigitsFormat formatter = null;

   /**
    * this method checks whether the Format class creates the correct pattern
    * given the number of decimal places and the formatting option
    */
   public void testCreatePatternForStandardOption()
   {
      formatter = new SignificantDigitsFormat();
      formatter.setSelectedOption(SignificantDigitsFormat.STANDARD_FORMAT);

      // case where sig digits is less than number of decimal places
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      String pattern = formatter.toPattern();
System.out.println(pattern);
      String expectedPattern = "0.00000";
      assertEquals(pattern, expectedPattern);
   }

   public void testFormatForStandardOption()
   {
      formatter = new SignificantDigitsFormat();
      formatter.setSelectedOption(SignificantDigitsFormat.STANDARD_FORMAT);

      double number = 12.3456;
      // case where sig digits is less than number of decimal places
      // and the number is greater than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      formatter.applyPattern(formatter.toPattern());
      String formattedNumber = formatter.format(number);
      String expectedNumber = "12.00000";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.00123456;
      // case where sig digits is less than number of decimal places
      // and the number is less than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "0.00120";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 123.456;
      // case where sig digits is more than number of decimal places
      // and the number is greater than 1
      formatter.setNumberOfSignificantDigits(5);
      formatter.setNumberOfDecimalPlaces(1);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "123.5";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.078123;
      // case where sig digits is more than number of decimal places
      // and the number is less than 1
      formatter.setNumberOfSignificantDigits(5);
      formatter.setNumberOfDecimalPlaces(1);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "0.1";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

   }

   public void testFormatForCurrencyOption()
   {
      formatter = new SignificantDigitsFormat();
      formatter.setSelectedOption(SignificantDigitsFormat.CURRENCY_FORMAT);

      double number = 12.3456;
      // case where sig digits is less than number of decimal places
      // and the number is greater than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      formatter.applyPattern(formatter.toPattern());
      String formattedNumber = formatter.format(number);
      String expectedNumber = "$12.00000";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.00123456;
      // case where sig digits is less than number of decimal places
      // and the number is less than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "$0.00120";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 123.456;
      // case where sig digits is more than number of decimal places
      // and the number is greater than 1
      formatter.setNumberOfSignificantDigits(5);
      formatter.setNumberOfDecimalPlaces(1);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "$123.5";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.078123;
      // case where sig digits is more than number of decimal places
      // and the number is less than 1
      formatter.setNumberOfSignificantDigits(5);
      formatter.setNumberOfDecimalPlaces(1);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "$0.1";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

   }

   public void testFormatForPercentageOption()
   {
      formatter = new SignificantDigitsFormat();
      formatter.setSelectedOption(SignificantDigitsFormat.PERCENTAGE_FORMAT);

      // the percentage formatter multiplies by 100 and then adds % sign
      double number = 12.3456;
      // case where sig digits is less than number of decimal places
      // and the number is greater than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      formatter.applyPattern(formatter.toPattern());
      String formattedNumber = formatter.format(number);
      String expectedNumber = "1200.00000%";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.00123456;
      // case where sig digits is less than number of decimal places
      // and the number is less than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.setNumberOfDecimalPlaces(5);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "0.12000%";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 123.456;
      // case where sig digits is more than number of decimal places
      // and the number is greater than 1
      formatter.setNumberOfSignificantDigits(5);
      formatter.setNumberOfDecimalPlaces(1);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "12346.0%";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.078923;
      // case where sig digits is more than number of decimal places
      // and the number is less than 1
      formatter.setNumberOfSignificantDigits(5);
      formatter.setNumberOfDecimalPlaces(1);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "7.9%";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

   }

   public void testFormatForScientificOption()
   {
      formatter = new SignificantDigitsFormat();
      formatter.setSelectedOption(SignificantDigitsFormat.SCIENTIFIC_FORMAT);

      double number = 12.3456;
      // case where the number is greater than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.applyPattern(formatter.toPattern());
      String formattedNumber = formatter.format(number);
      String expectedNumber = "1.2E01";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.00123456;
      // case where the number is less than 1
      formatter.setNumberOfSignificantDigits(2);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "1.2E-03";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 123.4;
      // case where the number is greater than 1
      // but the number of significant digits is large
      formatter.setNumberOfSignificantDigits(5);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "1.2340E02";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

      number = 0.078123;
      // case where the number is less than 1
      // and number of significant digits is large
      formatter.setNumberOfSignificantDigits(5);
      formatter.applyPattern(formatter.toPattern());
      formattedNumber = formatter.format(number);
      expectedNumber = "7.8123E-02";
System.out.println(formattedNumber);
      assertEquals(formattedNumber, expectedNumber);

   }

   /**
    * retrieve this test suite
    *
    * @return this test suite
    ********************************************************/
   public static Test suite()
   {
      return new TestSuite(TestSignificantDigitsFormat.class);
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {
      junit.swingui.TestRunner.run(TestSignificantDigitsFormat.class);
   }//main()

}