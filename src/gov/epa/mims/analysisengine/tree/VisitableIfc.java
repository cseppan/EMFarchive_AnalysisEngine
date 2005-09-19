package gov.epa.mims.analysisengine.tree;

/**
 * visitable interface functions
 *
 * @author Tommy E. Cathey
 * @version $Id: VisitableIfc.java,v 1.1 2005/09/19 14:14:14 rhavaldar Exp $
 *
 ***************************************************************/
public interface VisitableIfc
{
   /**
    * visit a Object
    *
    * @param v Object to be visited
    * @pre p != null
    ***************************************************************/
   void accept(VisitorIfc v);
}