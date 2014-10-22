package hk.ust.comp4321.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        recman.setNamedObject ("ForwardIndexTable", hashtable.getRecid()); 
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
  public static ForwardIndexTable getTable()
  {
    return ForwardIndexTable.forwardIndexTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the list of doc info with the given page id.
   * 
   * @param pageId the page id containing the doc info
   * @return list of doc info with the given page id
   * @throws IOException
   */
  public List<DocInfo> getDocInfoList (int pageId) throws IOException
  {
      return (List<DocInfo>)ForwardIndexTable.hashtable.get (pageId);
  }

  /**
   * This method returns the doc info with the given page id and word id. If
   * the doc info does not exist, it returns null.
   * 
   * @param  pageId       the page id containing the doc info
   * @param  wordId       the word id of the doc info
   * @return doc info with the given page id and word id
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public DocInfo getDocInfo (int pageId, int wordId) throws IOException
  {
    List<DocInfo> docInfoList = (List<DocInfo>)ForwardIndexTable.
                                    hashtable.get (pageId);

    // Check if the list does not exist
    if(docInfoList == null)
      return null;
    else
      {
        // Find the doc info
        for(int i = 0; i < docInfoList.size(); i++)
          if(docInfoList.get (i).getId() = wordId)
            return docInfo = docInfoList.get (i);
        return null;
      }
  }

  /**
   * This method inserts a doc info, if the doc info exists, it will be
   * replaced.
   * 
   * @param  id            the doc id to be inserted
   * @param  pageInfo      the associated doc info to be inserted
   * @throws IOException
   */
  public void insertDocInfo (int id, DocInfo docInfo) throws IOException
  {
    @SuppressWarnings("unchecked")
    List<DocInfo> docInfoList = (List<DocInfo>)ForwardIndexTable.
                                                     hashtable.get (id);

    // Check if the list does not exist
    if(docInfoList == null)
      docInfoList = new ArrayList<DocInfo>();

    // Check if the doc info has already existed, if yes then remove it 
    for(int i = 0; i < docInfoList.size(); i++)
      {
        if(docInfoList.get (i).getId() = docInfo.getId())
          {
            indexInfoList.remove (docInfoList.get (i));
            break;
          }
      }

    // Add doc info to the list
    docInfoList.add (docInfo);

    // Add the list to the database
    ForwardIndexTable.hashtable.remove (id);
    ForwardIndexTable.hashtable.put (id, docInfoList);
  }

  /**
   * This method returns an enumeration of the keys
   * @return an enumeration of the keys
   * @throws IOException
   */
  public FastIterator keys() throws IOException
  {
    return ForwardIndexTable.hashtable.keys();
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
