package hk.ust.comp4321.database;

import jdbm.helper.FastIterator;
import jdbm.htree.HTree;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

/**
 * 
 * @author Alex Poon
 *
 */
public class ForwardIndexTable
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private static ForwardIndexTable forwardIndexTable = new ForwardIndexTable();
  private static RecordManager recman;
  private static HTree hashtable;

  // Constructors.
  // -------------------------------------------------------------------------

  private ForwardIndexTable()
  {
    // Create a RecordManager name "SearchEngineDatabase"
    recman = RecordManagerFactory.createRecordManager ("SearchEngineDatabase"); 
    // get the record id of the object named "ForwardIndexTable"
    long recid = recman.getNamedObject ("ForwardIndexTable");

    if (recid != 0)
      {
        // load the hash table named"ForwardIndexTable"from the RecordManager
        hashtable = HTree.load (recman, recid); 
      }
    else
      {
        // create a hash table in the RecordManager
        hashtable = HTree.createInstance (recman); 
        // set the name of the hash table to "ForwardIndexTable"
        recman.setNamedObject ( "ForwardIndexTable", hashtable.getRecid() ); 
      }
  }

  // Class methods.
  // -------------------------------------------------------------------------

  /**
   * The class has only one instance. This method returns that unique one
   * instance of forward index table.
   * 
   * @return the unique forward index table
   */
  public static ForwardIndexTable getIndexTable()
  {
    return ForwardIndexTable.forwardIndexTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the page id with the given url which has been inserted
   * in the database.
   * 
   * @param  url the absolute url of the page
   * @return     the page id of the given url
   */
  public Integer getPageID (String url)
  {
    return ForwardIndexTable.hashtable.get (url);
  }

  /**
   * This method inserts a url which is not in the database and its associated
   * page id.
   * 
   * @param url the url to be inserted
   * @param id  the associated id to be inserted
   */
  public void insertURL (String url, int id)
  {
    ForwardIndexTable.hashtable.put (url, id);
  }

  /**
   * This method checks a page url whether it has been already inserted in the
   * page table.
   * 
   * @param  url check the url whether it exists
   * @return     true if the url exists
   */
  public boolean hasURL (String url)
  {
    return (ForwardIndexTable.hashtable.get (url) != null);
  }
}
