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
  private int InUseURL = 1;
  private Vector<String> v_link = new Vector<String>();
  private LinkBean lb = new LinkBean();
  
  // Constructors.
  // -------------------------------------------------------------------------
  
  public Crawler(String url)
  {
    this.originUrl = url;
    this.url = this.originUrl;
    //library http://downloads.sourceforge.net/htmlparser/htmlparser1_6_20060610.zip?modtime=1149940066&big_mirror=0 is needed
    v_link.addElement(this.url);
    do 
    {
    	lb.setURL(this.url);
    	URL[] URL_array = lb.getLinks();
    	for(int i=0; i<URL_array.length && v_link.size()<TotalNumOfPage; i++)
    		v_link.addElement(URL_array[i].toString());
    	InUseURL++;
    	// no exception handling if we can't crawle 30 pages
    	this.url = v_link.get(InUseURL);
    } while (v_link.size() < TotalNumOfPage);
    // Extract 30 pages
    for(int i = 0; i < v_link.size(); i++)
      try
        {
    		  Extractor extractor = new Extractor(this.url, v_link.get(i),
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
