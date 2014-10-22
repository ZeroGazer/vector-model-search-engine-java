package hk.ust.comp4321.database;

import java.io.IOException;

import jdbm.helper.FastIterator;
import jdbm.htree.HTree;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

/**
 * 
 * @author Alex Poon
 *
 */
public class ForwardPageTable
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private static ForwardPageTable forwardPageTable = new ForwardPageTable();
  private static RecordManager recman;
  private static HTree hashtable;

  // Constructors.
  // -------------------------------------------------------------------------

  private ForwardPageTable()
  {
    // Create a RecordManager name "SearchEngineDatabase"
    recman = RecordManagerFactory.createRecordManager ("SearchEngineDatabase"); 
    // get the record id of the object named "ForwardPageTable"
    long recid = recman.getNamedObject ("ForwardPageTable");

    if (recid != 0)
      {
        // load the hash table named"ForwardPageTable"from the RecordManager
        hashtable = HTree.load (recman, recid); 
      }
    else
      {
        // create a hash table in the RecordManager
        hashtable = HTree.createInstance (recman); 
        // set the name of the hash table to "ForwardPageTable"
        recman.setNamedObject ( "ForwardPageTable", hashtable.getRecid() ); 
      }
  }

  // Class methods.
  // -------------------------------------------------------------------------

  /**
   * The class has only one instance. This method returns that unique one
   * instance of forward page table.
   * 
   * @return the unique forward page table
   */
  public static ForwardPageTable getPageTable()
  {
    return ForwardPageTable.forwardPageTable;
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
    return ForwardPageTable.hashtable.get (url);
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
    ForwardPageTable.hashtable.put (url, id);
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
    return (ForwardPageTable.hashtable.get (url) != null);
  }

  /**
   * This method commits all changes since beginning of transaction and
   * terminates.
   * 
   * @throws IOException
   */
  public void terminate() throws IOException
  {
    recman.commit();
    recman.close();
  }
}
