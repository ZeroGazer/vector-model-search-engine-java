package hk.ust.comp4321.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.htmlparser.beans.LinkBean;
import org.htmlparser.util.ParserException;

import hk.ust.comp4321.database.IDGenerator;
import hk.ust.comp4321.database.InvertedPageTable;

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
  private final int TotalNumOfPage = 30;
  private int InUseURL = 1;
  private Vector<String> v_link = new Vector<String>();
  private LinkBean lb = new LinkBean();
  
  // Constructors.
  // -------------------------------------------------------------------------
  
  public Crawler(String url)
  {
    this.url = url;
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
    try
      {
    		  Extractor extractor = new Extractor(this.url, this.wordIdGenerator,
                                          this.pageIdGenerator);
      }
    catch (IOException | ParserException ex)
      {
        System.err.println(ex.toString());
      }
  }
}
