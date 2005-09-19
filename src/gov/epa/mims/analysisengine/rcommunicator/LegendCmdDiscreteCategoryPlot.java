package gov.epa.mims.analysisengine.rcommunicator;

import gov.epa.mims.analysisengine.AnalysisEngineConstants;
import gov.epa.mims.analysisengine.tree.DataSetIfc;
import gov.epa.mims.analysisengine.tree.Legend;
import gov.epa.mims.analysisengine.tree.LineType;
import gov.epa.mims.analysisengine.tree.Plot;
import gov.epa.mims.analysisengine.tree.ScatterPlot;

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT_ME
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public class LegendCmdDiscreteCategoryPlot extends LegendCmdLTY
   implements java.io.Serializable
{
   /**
    * DOCUMENT_ME
    *
    * @param p DOCUMENT_ME
    *
    * @return DOCUMENT_ME
    ********************************************************/
   public LegendCmdDiscreteCategoryPlot(Legend l, LineType lt, String[] k, 
      DataSetIfc[] ds)
   {
      super(l, lt, k.length);
      processText(k, ds);
   }

   /**
    * DOCUMENT_ME
    *
    * @param k DOCUMENT_ME
    * @param ds DOCUMENT_ME
    ********************************************************/
   private void processText(String[] k, DataSetIfc[] ds)
   {
      if (k == null)
      {
         throw new IllegalArgumentException(getClass().getName() + "k=null");
      }

      if (ds == null)
      {
         throw new IllegalArgumentException(getClass().getName() + "ds=null");
      }

      if (k.length != ds.length)
      {
         throw new IllegalArgumentException(getClass().getName()
                                            + "k.length != ds.length");
      }

      String[] leg = new String[k.length];

      for (int i = 0; i < k.length; ++i)
      {
         leg[i] = Util.escapeQuote(ds[i].getName());
      }

      variableAdd("legend", Util.buildArrayCommand("c", leg));
   }
}
