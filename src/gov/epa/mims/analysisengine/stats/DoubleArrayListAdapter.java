
package gov.epa.mims.analysisengine.stats;

import cern.colt.list.DoubleArrayList;
/**
 * <p>Description: This class converts different data structures to 
 * DoubleArrayListAdapter
 * Currently double [], Double [], DataSetIfc, are supported </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UNC-CH, Carolina Environmental Program</p>
 * @author Parthee Partheepan
 * @version $Id: DoubleArrayListAdapter.java,v 1.1 2005/09/19 14:16:05 rhavaldar Exp $
 */
public class DoubleArrayListAdapter
{
   
   /** Convert a Double array into a DoubleArrayList
    * @param data Double [] 
    * @return DoubleArrayList 
    * @throws NullPointerException if data[] is null
    */
   public static DoubleArrayList getDoubleArrayList(Double [] data)
   {  
      if(data != null)
      {
         double [] dataArray = new double[data.length];
         for(int i=0; i< data.length; i++)
         {
            dataArray[i] = data[i].doubleValue();
         }
         DoubleArrayList list = new DoubleArrayList(dataArray);
         return list;
      }//if(data != null)
      else
      {
         throw new NullPointerException("Data array is not initialized");
      }
   }//getDoubleArrayList(Double []);
   
    /** Convert a double array into a DoubleArrayList
    * @param data double [] 
    * @return DoubleArrayList 
    * @throws NullPointerException if data[] is null
    */
   public static DoubleArrayList getDoubleArrayList(double [] data)
   {  
      if(data != null)
      {
         return new DoubleArrayList(data);
      }
      else
      {
         throw new NullPointerException("Data array is not initialized");
      }
   }//getDoubleArrayList(double []);
   
   /** Convert the DoubleArrayList into Double []
    * @param DoubleArrayList
    * @return Double []
    */
   public static Double[] getDoubles(DoubleArrayList list)
   {
      if(list !=null)
      {
         int size = list.size();
         Double [] doubles = new Double[size];
         for(int i=0; i<size; i++)
         {
            doubles[i] = new Double(list.getQuick(i));
         }
         return doubles;
      }//if(list !=null)
      else
      {
         throw new NullPointerException("DoubleArrayList is not initialized");
      }//else
   }//getDoubles(DoubleArrayList list)
   
   /** Convert the DoubleArrayList into Double []
   * @param DoubleArrayList
   * @return Double []
   */
   public static double[] getPrimitiveDoubles(DoubleArrayList list)
   {
      if(list !=null)
      {
         int size = list.size();
         double [] doubles = new double[size];
         for(int i=0; i<size; i++)
         {
            doubles[i] = list.getQuick(i);
         }
         return doubles;
      }//if(list !=null)
      else
      {
         throw new NullPointerException("DoubleArrayList is not initialized");
      }//else
   }//getPrimitiveDoubles(DoubleArrayList list)
}
