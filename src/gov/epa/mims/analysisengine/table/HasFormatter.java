package gov.epa.mims.analysisengine.table;

import java.text.Format;

/**
 * <p>Description: This is used a a common interface for things that use
 * formatters. The UnitsCellRenderer and the FormattedCellRenderer use this.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UNC - CEP</p>
 * @author Daniel Gatti
 * @version $Id: HasFormatter.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 */
public interface HasFormatter
{
   /** Return the formatter used in this object.*/
   public Format getFormat();
} // intrface HasFormatter

