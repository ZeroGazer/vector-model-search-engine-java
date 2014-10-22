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
public class InvertedPageTable
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private static InvertedPageTable invertedPageTable = new InvertedPageTable();
  private static RecordManager recman;
  private static HTree hashtable;

  // Constructors.
  // -------------------------------------------------------------------------

  private InvertedPageTable()
  {
    // Create a RecordManager name "SearchEngineDatabase"
    recman = RecordManagerFactory.createRecordManager ("SearchEngineDatabase"); 
    // get the record id of the object named "InvertedPageTable"
    long recid = recman.getNamedObject ("InvertedPageTable");

    if (recid != 0)
      {
        // load the hash table named"InvertedPageTable"from the RecordManager
        hashtable = HTree.load (recman, recid); 
      }
    else
      {
        // create a hash table in the RecordManager
        hashtable = HTree.createInstance (recman); 
        // set the name of the hash table to "InvertedPageTable"
        recman.setNamedObject ("InvertedPageTable", hashtable.getRecid()); 
      }
  }

  // Class methods.
  // -------------------------------------------------------------------------

  /**
   * The class has only one instance. This method returns that unique one
   * instance of inverted page table.
   * 
   * @return the unique inverted page table
   */
  public static InvertedPageTable getTable()
  {
    return InvertedPageTable.invertedPageTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the page info with the given page id which has been
   * inserted in the database.
   * 
   * @param  id the page id of the page
   * @return    the page info of the given page id
   */
  public PageInfo getPageInfo (int id)
  {
    return InvertedPageTable.hashtable.get (id);
  }

  /**
   * This method inserts a page id which is not in the database and its
   * associated page info.
   * 
   * @param id       the page id to be inserted
   * @param pageInfo the associated page info to be inserted
   */
  public void insertPageId (int id, PageInfo pageInfo)
  {
    PageInfo pageInfo = InvertedPageTable.hashtable.get (id);
    
    // If the page id has already existed, then remove it
    if(pageInfo != null)
      InvertedPageTable.hashtable.remove (id);
    
    // Insert pageInfo
    InvertedPageTable.hashtable.put (id, pageInfo);
  }

  /**
   * This method returns an enumeration of the keys
   * @return an enumeration of the keys
   * @throws IOException
   */
  public FastIterator keys() throws IOException
  {
    return InvertedPageTable.hashtable.keys();
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
