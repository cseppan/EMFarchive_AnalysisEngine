package gov.epa.mims.analysisengine;

import java.lang.RuntimeException;
import java.lang.String;


/**
 * exception to be thrown by Analysis Engine objects
 *
 * @author Tommy E. Cathey
 * @version $Id: AnalysisException.java,v 1.1 2005/09/19 14:16:05 rhavaldar Exp $
 *
 **/
public class AnalysisException extends RuntimeException
{
   /******************************************************
    *
    * methods
    *
    *****************************************************/
   public AnalysisException()
   {
   }

   /**
    * Creates a new AnalysisException object.
    *
    * @param gripe DOCUMENT_ME
    ********************************************************/
   public AnalysisException(String gripe)
   {
      super(gripe);
   }
}