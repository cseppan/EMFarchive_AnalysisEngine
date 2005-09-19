package gov.epa.mims.analysisengine.table;

import gov.epa.mims.analysisengine.table.PlottingInfo;
import gov.epa.mims.analysisengine.tree.Branch;

/**  Data - Storage for configuration information
 * @author  Krithiga Thangavelu, CEP, UNC CHAPEL HILL.
 * @version $Id: Data.java,v 1.2 2005/09/19 14:50:03 rhavaldar Exp $  
 */

public class Data implements java.io.Serializable {

   static int TABLE_TYPE = 0;
   static int PLOT_TYPE = -1;
   public int configType ; 
   public Branch tree; // when configType == PLOT_TYPE
   public PlottingInfo info; //when configType == PLOT_TYPE
   public Object criteria; //when configType == TABLE_TYPE

}

