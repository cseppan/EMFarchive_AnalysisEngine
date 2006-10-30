package gov.epa.mims.analysisengine.tree;

import java.io.Serializable;
import java.util.Date;


/**
 * Data Set interface functions
 *
 * @author Tommy E. Cathey
 * @version $Id: DateDataSetIfc.java,v 1.3 2006/10/30 21:43:50 parthee Exp $
 *
 **************************************************/
public interface DateDataSetIfc
   extends DataSetIfc, Serializable
{
   /**
    * get the Date for the i-th element
    *
    * @param i index into data series
    * @return element Date
    * @throws java.lang.Exception if series is not open
    * @throws java.util.NoSuchElementException if i is out of range 
    **************************************************/
   Date getDate(int i)
         throws java.lang.Exception, 
                java.util.NoSuchElementException;
}