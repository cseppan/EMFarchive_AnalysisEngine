package gov.epa.mims.analysisengine;

import junit.framework.*;

/**
 * TestSuite that runs all the sample tests
 * 
 * @author Tommy E. Cathey
 * @version $Id: AllTests.java,v 1.1 2005/09/19 14:50:15 rhavaldar Exp $
 * 
 */
public class AllTests {
    /***************************************************************************
     * DOCUMENT_ME
     * 
     * @param args
     *            DOCUMENT_ME
     **************************************************************************/
    public static void main(String[] args) {
        System.setProperty("X", System.getProperty("X", "false"));
        System.setProperty("E", System.getProperty("E", "false"));
        System.setProperty("R", System.getProperty("R", "false"));
        System.setProperty("C", System.getProperty("C", "false"));
        System.setProperty("T", System.getProperty("T", "false"));
        System.setProperty("D", System.getProperty("D", "true"));

        junit.textui.TestRunner.run(suite());
    }

    /***************************************************************************
     * DOCUMENT_ME
     * 
     * @return DOCUMENT_ME
     **************************************************************************/
    public static Test suite() {
        TestSuite suite = new TestSuite("All JUnit Tests");
        suite.addTest(AllTests.suite());
        suite.addTest(AllTests.suite());

        return suite;
    }
}