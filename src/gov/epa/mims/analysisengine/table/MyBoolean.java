
package gov.epa.mims.analysisengine.table;

/*
 * MyBoolean.java
 * @author  Parthee R Parthepan
 * @version $Id: MyBoolean.java,v 1.1 2005/09/19 14:14:04 rhavaldar Exp $
 * Created on April 19, 2005, 11:37 AM
 */


public final class MyBoolean implements Comparable
{
   private Boolean myBoolValue;
   public MyBoolean(Boolean boolValue)
   {
      myBoolValue = boolValue;
   }
   
   public boolean getBooleanValue()
   {
      return myBoolValue.booleanValue();
   }
   
   public int compareTo(Object o)
   {
      if(o == null || !(o instanceof MyBoolean))
      {
         throw new IllegalArgumentException("The object is null or not intance of Boolean");
      }
      boolean other = ((MyBoolean)o).getBooleanValue();
      boolean my = myBoolValue.booleanValue();
      if((my && other) ||
      (!my && !other))
      {
         return 0;
      }
      else if(my &&!other)
      {
         return 1;
      }
      else //if(!my && other)
      {
         return  -1;
      }
   }
   
   public boolean equals(Object obj)
   {
      if (obj instanceof MyBoolean)
      {
         boolean my = myBoolValue.booleanValue();
         boolean other = ((MyBoolean)obj).getBooleanValue();
         
         return ((my && other) || (!my && !other))?true:false;
      }
      return false;
   }
   
   public int hashCode()
   {
      return myBoolValue.hashCode();
   }
   
   public String toString()
   {
      return myBoolValue.toString();
   }
   
}
