package hk.ust.comp4321.database;

import jdbm.htree.HTree;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Alex Poon
 *
 */
public class InvertedIndexTable
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private static InvertedIndexTable invertedIndexTable = new 
                                                         InvertedIndexTable();
  private static RecordManager recman;
  private static HTree hashtable;

  // Constructors.
  // -------------------------------------------------------------------------

  private InvertedIndexTable()
  {
    try
      {
        // Create a RecordManager name "SearchEngineDatabase"
        recman = RecordManagerFactory.
                 createRecordManager ("SearchEngineDatabase"); 
        // get the record id of the object named "InvertedIndexTable"
        long recid = recman.getNamedObject ("InvertedIndexTable");

        if (recid != 0)
          {
            // load the hash table named"InvertedIndexTable" from
            // the RecordManager
            hashtable = HTree.load (recman, recid); 
          }
        else
          {
            // create a hash table in the RecordManager
            hashtable = HTree.createInstance (recman); 
            // set the name of the hash table to "InvertedIndexTable"
            recman.setNamedObject ( "InvertedIndexTable", hashtable.getRecid() ); 
          }
      }
    catch (IOException ex)
      {
        System.err.println(ex.toString());
      }
  }

  // Class methods.
  // -------------------------------------------------------------------------

  /**
   * The class has only one instance. This method returns that unique one
   * instance of inverted index table.
   * 
   * @return the unique inverted index table
   */
  public static InvertedIndexTable getInvertedIndexTable()
  {
    return InvertedIndexTable.invertedIndexTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the list of doc id and frequency with the given index
   * id which has been inserted in the database.
   * 
   * @param  id           the index id of the index
   * @return              the list of index info of the given index id
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<IndexInfo> getIndexInfo (int id) throws IOException
  {
    return (List<IndexInfo>)InvertedIndexTable.hashtable.get (id);
  }

  /**
   * This method inserts a page id which is not in the database and its
   * associated page info.
   * 
   * @param  id            the page id to be inserted
   * @param  pageInfo      the associated page info to be inserted
   * @throws IOException
   */
  public void insertIndexInfo (int id, IndexInfo indexInfo) throws IOException
  {
    @SuppressWarnings("unchecked")
    List<IndexInfo> indexInfoList = (List<IndexInfo>)InvertedIndexTable.
                                                     hashtable.get(id);
    if(indexInfoList == null)
      indexInfoList = new ArrayList<IndexInfo>();
    indexInfoList.add(indexInfo);
    InvertedIndexTable.hashtable.put (id, indexInfoList);
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
