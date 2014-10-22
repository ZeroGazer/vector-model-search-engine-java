package hk.ust.comp4321.crawler;

import java.io.IOException;

import hk.ust.comp4321.database.IDGenerator;

/**
 * 
 * @author Alex Poon
 *
 */
public class Crawler
{

  // Constants and fields.
  // -------------------------------------------------------------------------
  
  private String url;
  private IDGenerator wordIdGenerator = new IDGenerator();
  private IDGenerator pageIdGenerator = new IDGenerator();
  
  // Constructors.
  // -------------------------------------------------------------------------
  
  public Crawler(String url)
  {
    this.url = url;
    
    // Extract 30 pages
    try
      {
        Extractor extractor = new Extractor(this.url, this.wordIdGenerator,
                                          this.pageIdGenerator);
      }
    catch (IOException ex)
      {
        System.err.println(ex.toString());
      }
  }
}
