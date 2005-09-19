package gov.epa.mims.analysisengine.tree;

import java.io.Serializable;

import java.util.NoSuchElementException;


/**
 * Data Set interface functions
 *
 * @author Tommy E. Cathey
 * @version $Id: LabeledDataSetIfc.java,v 1.2 2005/09/19 14:50:10 rhavaldar Exp $
 *
 **************************************************/
public interface LabeledDataSetIfc
   extends DataSetIfc, Serializable
{
   /**
    * get data label
    *
    * @param i index into data series
    * @return element label
    * @throws java.lang.Exception if series is not open
    * @throws java.util.NoSuchElementException if i is out of range 
    **************************************************/
   String getLabel(int i)
            throws java.lang.Exception, 
                   java.util.NoSuchElementException;
}