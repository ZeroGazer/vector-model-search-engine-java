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
public class ForwardWordTable
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private static ForwardWordTable forwardWordTable = new ForwardWordTable();
  private static RecordManager recman;
  private static HTree hashtable;

  // Constructors.
  // -------------------------------------------------------------------------

  private ForwardWordTable()
  {
    // Create a RecordManager name "SearchEngineDatabase"
    recman = RecordManagerFactory.createRecordManager ("SearchEngineDatabase"); 
    // get the record id of the object named "ForwardWordTable"
    long recid = recman.getNamedObject ("ForwardWordTable");

    if (recid != 0)
      {
        // load the hash table named"ForwardWordTable"from the RecordManager
        hashtable = HTree.load (recman, recid); 
      }
    else
      {
        // create a hash table in the RecordManager
        hashtable = HTree.createInstance (recman); 
        // set the name of the hash table to "ForwardWordTable"
        recman.setNamedObject ( "ForwardWordTable", hashtable.getRecid() ); 
      }
  }

  // Class methods.
  // -------------------------------------------------------------------------

  /**
   * The class has only one instance. This method returns that unique one
   * instance of forward word table.
   * 
   * @return the unique forward word table
   */
  public static ForwardWordTable getTable()
  {
    return ForwardWordTable.forwardWordTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the word id with the given word which has been
   * inserted in the database.
   * 
   * @param  word the word
   * @return      the word id of the given word
   */
  public Integer getWordID (String word)
  {
    return ForwardWordTable.hashtable.get (word);
  }

  /**
   * This method inserts a word which is not in the database and its associated
   * word id.
   * 
   * @param word the word to be inserted
   * @param id   the associated id to be inserted
   */
  public void insertWord (String word, int id)
  {
    ForwardWordTable.hashtable.put (word, id);
  }

  /**
   * This method checks a word whether it has been already inserted in the word
   * table.
   * 
   * @param  word check the word whether it exists
   * @return      true if the word exists
   */
  public boolean hasWord (String word)
  {
    return (WordTable.hashtable.get (word) == null);
  }

  /**
   * This method returns an enumeration of the keys
   * @return an enumeration of the keys
   * @throws IOException
   */
  public FastIterator keys() throws IOException
  {
    return ForwardWordTable.hashtable.keys();
  }

  /**
   * This method returns an enumeration of the keys
   * @return an enumeration of the keys
   * @throws IOException
   */
  public FastIterator keys() throws IOException
  {
    return ForwardWordWord.hashtable.keys();
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
