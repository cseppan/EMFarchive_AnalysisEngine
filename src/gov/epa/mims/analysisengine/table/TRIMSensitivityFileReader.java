package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.table.io.FileParser;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * TRIMSensitivityFileReader.java
 * This class is customized to read TRIM Sensitivity file
 * Created on April 1, 2004, 12:31 PM
 * @author  Parthee Partheepan, CEP, UNC-CHAPEL HILL
 * @version $Id: TRIMSensitivityFileReader.java,v 1.3 2006/10/30 17:26:13 parthee Exp $
 */

public class TRIMSensitivityFileReader extends  FileParser
{
   
   public static final int  NO_OF_COLUMN_HEADER_ROWS = 1;
   public static final String DELIMITER  = ";";
   
   /** Creates a new instance of TRIMSensitivityFileReader */
   public TRIMSensitivityFileReader(String fileName, boolean ignoreMultDelims) throws Exception
   {
      super(fileName, DELIMITER,NO_OF_COLUMN_HEADER_ROWS, ignoreMultDelims);
   }
   
}

