package gov.epa.mims.analysisengine;

import gov.epa.mims.analysisengine.tree.Branch;


/**
 * interface for analysis generators
 *
 * @author Tommy E. Cathey
 * @version $Id: AnalysisGeneratorIfc.java,v 1.1 2005/09/19 14:16:05 rhavaldar Exp $
 *
 **/
public interface AnalysisGeneratorIfc
{
   /**
    * DOCUMENT_ME
    *
    * @param p DOCUMENT_ME
    ********************************************************/
   public void execute(Branch p);
}