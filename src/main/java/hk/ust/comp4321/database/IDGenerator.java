package hk.ust.comp4321.database;

/**
 * 
 * @author Alex Poon
 *
 */
public class IDGenerator
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private int counter;

  // Constructors.
  // -------------------------------------------------------------------------

  public IDGenerator()
  {
    counter = 0;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns an unique id for a specific data
   * @return an unique id
   */
  public int getId()
  {
    return counter++;
  }
}
