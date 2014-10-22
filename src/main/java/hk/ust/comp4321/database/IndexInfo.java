package hk.ust.comp4321.database;

import java.io.Serializable;

/**
 * 
 * @author Alex Poon
 */
public class IndexInfo implements Serializable
{

  //Constants and fields.
  // -------------------------------------------------------------------------

  private int id;
  private int frequency;

  // Constructors.
  // -------------------------------------------------------------------------

  /**
   * This constrcutor encapsulates the index id and frequency of an index.
   * 
   * @param id        the page id
   * @param frequency the associated frequency
   */
  public IndexInfo(int id, int frequency)
  {
    this.id = id;
    this.frequency = frequency;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the index id of the index instance.
   * 
   * @return the id of this index
   */
  public int getId()
  {
    return this.id;
  }

  /**
   * This method returns the frequency of the index instance.
   * 
   * @return the frequency of the index instance 
   */
  public int getFrequency()
  {
    return this.frequency;
  }
}
