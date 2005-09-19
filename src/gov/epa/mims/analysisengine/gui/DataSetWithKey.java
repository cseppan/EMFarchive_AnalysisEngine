package gov.epa.mims.analysisengine.gui;

import gov.epa.mims.analysisengine.tree.DataSetIfc;
import gov.epa.mims.analysisengine.tree.DataSets;


/**
 * <p>Title: </p>
 * <p>Description: Stores the data set and key together</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DataSetWithKey
{
   Object key;
   DataSetIfc dataSet;

   public DataSetWithKey(Object key, DataSetIfc dataSet)
   {
     this.key = key;
     this.dataSet = dataSet;
   }

   public String toString()
   {
      return dataSet.getName();
   }

   public String getName()
   {
      return dataSet.getName();
   }
}

