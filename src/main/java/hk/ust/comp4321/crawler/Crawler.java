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
  private InvertedPageTable invertedPageTable;
  private IDGenerator wordIdGenerator = new IDGenerator();
  private IDGenerator pageIdGenerator = new IDGenerator();
  private const int TotalNumOfPage = 30;
  private int counter = 1;
  
  // Constructors.
  // -------------------------------------------------------------------------
  
  public Crawler(String url)
  {
    this.url = url;
    
    // Extract 30 pages
    try
      {
    	  while(counter <= TotalNumOfPage)
    	  {
    		  Extractor extractor = new Extractor(this.url, this.wordIdGenerator,
                                          this.pageIdGenerator);
    		  counter++;
    		  this.url = invertedPageTable.getPageInfo(i).getUrl();
    	  }
      }
    catch (IOException ex)
      {
        System.err.println(ex.toString());
      }
  }
}
