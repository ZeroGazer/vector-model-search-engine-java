package hk.ust.comp4321.crawler;

import java.io.*;

/**
 * 
 * @author Alex Poon
 *
 */
public class StopStem {

  // Constants and fields.
  // -------------------------------------------------------------------------

  private Porter porter;
  @SuppressWarnings("rawtypes")
  private java.util.HashSet stopWords;

  // Constructors.
  // -------------------------------------------------------------------------

  @SuppressWarnings("unchecked")
  public StopStem()  //Load the stop words from txt file to hashset;
  {
    super();
    String filePath = "stopwords.txt";
    File file = new File(filePath);
    BufferedReader reader = null;
    porter = new Porter();
    stopWords = new java.util.HashSet<String>();

    try
      {
       reader = new BufferedReader(new FileReader(file));
       String text = null;

       // repeat until all lines is read
       while ((text = reader.readLine()) != null)
         stopWords.add(text);
      }
    catch(IOException e)
      {
        System.out.println("File read exception");
      } 
  }

  // Instance methods.
  // -------------------------------------------------------------------------

  public boolean isStopWord(String str)
  {
    return stopWords.contains(str); 
  }

  public String stem(String str)
  {
    return porter.stripAffixes(str);
  }
}