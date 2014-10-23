package hk.ust.comp4321.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.htmlparser.beans.LinkBean;
import org.htmlparser.util.ParserException;

import hk.ust.comp4321.database.ForwardIndexTable;
import hk.ust.comp4321.database.ForwardPageTable;
import hk.ust.comp4321.database.ForwardWordTable;
import hk.ust.comp4321.database.IDGenerator;
import hk.ust.comp4321.database.InvertedIndexTable;
import hk.ust.comp4321.database.InvertedPageTable;
import hk.ust.comp4321.database.InvertedWordTable;

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
  private static String originUrl = "http://www.cse.ust.hk";
  private InvertedPageTable invertedPageTable;
  private IDGenerator wordIdGenerator = new IDGenerator();
  private IDGenerator pageIdGenerator = new IDGenerator();
  private final int TotalNumOfPage = 30;
  private int InUseURL = 0;
  private int NumofPageExtracted = 1;
	private String[][] v_link = new String[30][2];
  private Vector<String> v_link = new Vector<String>();
  private LinkBean lb = new LinkBean();
  
  
  // Constructors.
  // -------------------------------------------------------------------------
  
  public boolean ExistLink(String url) 
	{
		for(int i=0; i<TotalNumOfPage; i++)
			if(v_link[i][1] == url)
				return true;
		return false;
	}
	
  public Crawler(String url)
  {
    this.originUrl = url;
    this.url = this.originUrl;
    // [x][0]: parent link, [x][1]:child link;
    v_link[0][0] = this.url;
		v_link[0][1] = this.url;
		do
		{
		 lb.setURL(this.url);
		 URL[] URL_array = lb.getLinks();
		 for(int i = 0; i <= URL_array.length && NumofPageExtracted<TotalNumOfPage; i++)
			 {
			 	if(!ExistLink(URL_array[i].toString()) && NumofPageExtracted<TotalNumOfPage)
			 	{
			 		v_link[NumofPageExtracted][0] = this.url;
			 		v_link[NumofPageExtracted][1] = URL_array[i].toString();
			 		NumofPage++;
			 	}
			 }
		 InUseURL++;
		 // no exception handling if we can't crawle 30 pages
		 this.url = v_link[InUseURL][1];
		 } while (NumofPageExtracted < TotalNumOfPage);

    // Extract 30 pages
    for(int i = 0; i < TotalNumOfPage; i++)
      try
        {
    		  Extractor extractor = new Extractor(v_link[i][0], v_link[i][1],
    		                                      this.wordIdGenerator,
    		                                      this.pageIdGenerator);
        }
      catch (IOException | ParserException ex)
        {
          System.err.println(ex.toString());
        }
    try
      {
        // Commit all tables
        ForwardIndexTable.getTable().terminate();
        ForwardPageTable.getTable().terminate();
        ForwardWordTable.getTable().terminate();
        InvertedIndexTable.getTable().terminate();
        InvertedPageTable.getTable().terminate();
        InvertedWordTable.getTable().terminate();
      }
    catch (IOException ex)
      {
        System.err.println(ex.toString());
      }
  }

  public static void main(String[] args)
  {
    Crawler clawler = new Crawler("http://www.cse.ust.hk");
  }
}
