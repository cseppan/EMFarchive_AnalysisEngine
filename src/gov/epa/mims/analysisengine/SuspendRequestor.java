package gov.epa.mims.analysisengine;

/**
 * class used to safely suspend threads
 *
 * @author Tommy E. Cathey
 * @version $Id: SuspendRequestor.java,v 1.1 2005/09/19 14:16:05 rhavaldar Exp $
 *
 **/
public class SuspendRequestor
{
   /** DOCUMENT_ME */
   private boolean suspendRequested;

   /**
    * DOCUMENT_ME
    *
    * @param b DOCUMENT_ME
    ********************************************************/
   public synchronized void set(boolean b)
   {
      suspendRequested = b;
      notifyAll();
   }

   /**
    * DOCUMENT_ME
    *
    * @throws InterruptedException DOCUMENT_ME
    ********************************************************/
   public synchronized void waitForResume() throws InterruptedException
   {
      while (suspendRequested)
      {
         wait();
      }
   }
}