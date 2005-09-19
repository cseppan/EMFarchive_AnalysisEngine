package gov.epa.mims.analysisengine.rcommunicator;

import gov.epa.mims.analysisengine.AnalysisException;
import gov.epa.mims.analysisengine.AnalysisGeneratorIfc;
import gov.epa.mims.analysisengine.tree.Branch;
import gov.epa.mims.analysisengine.tree.Page;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;


/**
 * main class for generating R commands
 *
 * @author Tommy E. Cathey
 * @version $Id: RGenerator.java,v 1.2 2005/09/19 14:50:10 rhavaldar Exp $
 *
 **/
public class RGenerator implements Serializable, Cloneable, AnalysisGeneratorIfc
{
   /** DOCUMENT_ME */
   static final long serialVersionUID = 1;

   /** DOCUMENT_ME */
   private String pathToR;

   /** DOCUMENT_ME */
   private boolean echo;

   /**
    * Creates a new RGenerator object.
    *
    * @param pathToR DOCUMENT_ME
    ********************************************************/
   public RGenerator(String pathToR)
   {
      this.pathToR = pathToR;
      echo = false;
   }

   /**
    * DOCUMENT_ME
    *
    * @param flag DOCUMENT_ME
    ********************************************************/
   public void setEcho(boolean flag)
   {
      this.echo = flag;
   }

   /**
    * set logging
    *
    * @param f the Log File
    * @throws java.io.IOException DOCUMENT_ME
    **/
   public void setLog(File f) throws java.io.IOException
   {
      RCommunicator rCommunicator = RCommunicator.getInstance();
      rCommunicator.setLog(f);
   }

   /**
    * Top Level method for generating R Commands
    *
    * @param forest the root of all trees
    * @throws gov.epa.mims.analysisengine.AnalysisException on processing error
    **/
   public void execute(Branch forest)
                throws gov.epa.mims.analysisengine.AnalysisException
   {
      //
      // reset the R variable generator
      //
      Rvariable.reset();

      RCommunicator rCommunicator = RCommunicator.getInstance();
      rCommunicator.setPathToR(pathToR);

      // find all pages
      ArrayList allPages = new ArrayList();
      forest.getPages(allPages);

      for (int i = 0; i < allPages.size(); i++)
      {
         RCommandGenerator nv = new RCommandGenerator();

         Page currentPage = (Page) allPages.get(i);


         // generate page commands
         currentPage.accept(nv);

         ArrayList commands = nv.getCommands();

         if (echo)
         {
            for (int j = 0; j < commands.size(); j++)
            {
               System.out.println(commands.get(j));
            }
         }

         try
         {
            rCommunicator.issueCommands(commands, 1000);
         }
         catch (AnalysisException e)
         {
        	throw e;
            //e.getMessage();
           // e.printStackTrace();
         }
      }
   }

   /**
    * describe object in a String
    *
    * @return String describing object
    ******************************************************/
   public String toString()
   {
      return Util.toString(this);
   }
}
