package hk.ust.comp4321.database;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Alex Poon
 */
public class IndexInfo implements Serializable
{

  //Constants and fields.
  // -------------------------------------------------------------------------

  /**
   * 
   */
  private static final long serialVersionUID = -967762972744867492L;
  private int id;
  private List<Integer> positionList;

  // Constructors.
  // -------------------------------------------------------------------------

  /**
   * This constrcutor encapsulates the index id and frequency of an index.
   * 
   * @param id        the page id
   * @param frequency the associated frequency
   */
  public IndexInfo(int id, List<Integer> positionList)
  {
    this.id = id;
    this.positionList = positionList;
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
   * This method returns the list of word postion of the index instance.
   * 
   * @return the list of word postion of the index instance
   */
  public List<Integer> getPositionList()
  {
    return this.positionList;
  }
}
