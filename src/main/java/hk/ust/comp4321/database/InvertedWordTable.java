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
public class InvertedWordTable
{

  // Constants and fields.
  // -------------------------------------------------------------------------

  private static InvertedWordTable invertedWordTable = new InvertedWordTable();
  private static RecordManager recman;
  private static HTree hashtable;

  // Constructors.
  // -------------------------------------------------------------------------

  private InvertedWordTable()
  {
    try
      {
        // Create a RecordManager name "SearchEngineDatabase"
        recman = RecordManagerFactory.createRecordManager ("InvertedWordTable"); 
        // get the record id of the object named "InvertedWordTable"
        long recid = recman.getNamedObject ("InvertedWordTable");

        if (recid != 0)
          {
            // load the hash table named"InvertedWordTable"from the RecordManager
            hashtable = HTree.load (recman, recid); 
          }
        else
          {
            // create a hash table in the RecordManager
            hashtable = HTree.createInstance (recman); 
            // set the name of the hash table to "InvertedWordTable"
            recman.setNamedObject ("InvertedWordTable", hashtable.getRecid()); 
          }
      }
    catch(java.io.IOException ex)
      {
        System.err.println(ex.toString());
      }
  }

  // Class methods.
  // -------------------------------------------------------------------------

  /**
   * The class has only one instance. This method returns that unique one
   * instance of inverted word table.
   * 
   * @return the unique inverted word table
   */
  public static InvertedWordTable getTable()
  {
    return InvertedWordTable.invertedWordTable;
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  /**
   * This method returns the word with the given word id which has been
   * inserted in the database.
   * 
   * @param  id the word id of the word
   * @return    the word of the given word id
   * @throws IOException 
   */
  public String getWord (int id) throws IOException
  {
    return (String)InvertedWordTable.hashtable.get (id);
  }

  /**
   * This method inserts a word id which is not in the database and its
   * associated word.
   * 
   * @param id   the word id to be inserted
   * @param word the associated word to be inserted
   * @throws IOException 
   */
  public void insertWordId (int id, String word) throws IOException
  {
    InvertedWordTable.hashtable.put (id, word);
  }

  /**
   * This method returns an enumeration of the keys
   * @return an enumeration of the keys
   * @throws IOException
   */
  public FastIterator keys() throws IOException
  {
    return InvertedWordTable.hashtable.keys();
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
