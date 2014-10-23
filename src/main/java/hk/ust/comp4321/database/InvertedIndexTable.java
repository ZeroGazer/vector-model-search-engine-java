package hk.ust.comp4321.database;

import jdbm.helper.FastIterator;
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
                 createRecordManager ("InvertedIndexTable"); 
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
            recman.setNamedObject ("InvertedIndexTable", hashtable.getRecid()); 
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
  public static InvertedIndexTable getTable()
  {
    return InvertedIndexTable.invertedIndexTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the list of index info with the given word id.
   * 
   * @param wordId the word id containing the index info
   * @return the list of index info with the given word id
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<IndexInfo> getIndexInfoList (int wordId) throws IOException
  {
      return (List<IndexInfo>)InvertedIndexTable.hashtable.get (wordId);
  }

  /**
   * This method returns the index info with the given word id and page id. If
   * the index info does not exist, it returns null.
   * 
   * @param  wordId       the word id containing the index info
   * @return pageId       the page id of the index info
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public IndexInfo getIndexInfo (int wordId, int pageId) throws IOException
  {
    List<IndexInfo> indexInfoList = (List<IndexInfo>)InvertedIndexTable.
                                    hashtable.get (wordId);

    // Check if the list does not exist
    if(indexInfoList == null)
      return null;
    else
      {
        // Find the index info
        for(int i = 0; i < indexInfoList.size(); i++)
          if(indexInfoList.get (i).getId() == pageId)
            return indexInfoList.get (i);
        return null;
      }
  }

  /**
   * This method inserts a index info, if the index info exists, it will be
   * replaced.
   * 
   * @param  id            the word id to be inserted
   * @param  pageInfo      the associated index info to be inserted
   * @throws IOException
   */
  public void insertIndexInfo (int id, IndexInfo indexInfo) throws IOException
  {
    @SuppressWarnings("unchecked")
    List<IndexInfo> indexInfoList = (List<IndexInfo>)InvertedIndexTable.
                                                     hashtable.get (id);

    // Check if the list does not exist
    if(indexInfoList == null)
      indexInfoList = new ArrayList<IndexInfo>();

    // Check if the index info has already existed, if yes then remove it 
    for(int i = 0; i < indexInfoList.size(); i++)
      {
        if(indexInfoList.get (i).getId() == indexInfo.getId())
          {
            indexInfoList.remove (indexInfoList.get (i));
            break;
          }
      }

    // Add index info to the list
    indexInfoList.add (indexInfo);

    // Add the list to the database
    InvertedIndexTable.hashtable.remove (id);
    InvertedIndexTable.hashtable.put (id, indexInfoList);
  }

  /**
   * This method returns an enumeration of the keys
   * @return an enumeration of the keys
   * @throws IOException
   */
  public FastIterator keys() throws IOException
  {
    return InvertedIndexTable.hashtable.keys();
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
