package hk.ust.comp4321.crawler;

import hk.ust.comp4321.database.ForwardIndexTable;
import hk.ust.comp4321.database.ForwardPageTable;
import hk.ust.comp4321.database.ForwardWordTable;
import hk.ust.comp4321.database.IDGenerator;
import hk.ust.comp4321.database.InvertedIndexTable;
import hk.ust.comp4321.database.InvertedPageTable;
import hk.ust.comp4321.database.InvertedWordTable;
import hk.ust.comp4321.database.PageInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument;

import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.beans.LinkBean;
import org.htmlparser.beans.StringBean;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sun.net.www.URLConnection;

/**
 * 
 * @author Paul Wong
 *
 */
public class Extractor
{

  // Constants and fields.
  // -------------------------------------------------------------------------

	private String url;
	private int pageId;
	private IDGenerator pageIdGenerator;
	private IDGenerator wordIdGenerator;
	private ForwardIndexTable forwardIndexTable;
	private ForwardPageTable forwardPageTable;
	private ForwardWordTable forwardWordTable;
	private InvertedIndexTable invertedIndexTable;
	private InvertedPageTable invertedPageTable;
	private InvertedWordTable invertedWordTable;
	
	// Constructors.
  // -------------------------------------------------------------------------

	/**
	 * 
	 * @param url
	 * @param wordIdGenerator
	 * @param pageIdGenerator
	 * @throws IOException
	 */
	Extractor(String url, IDGenerator wordIdGenerator,
	          IDGenerator pageIdGenerator) throws IOException
	{
	  // Initialize all variables
	  this.forwardIndexTable = ForwardIndexTable.getTable();
	  this.forwardPageTable = ForwardPageTable.getTable();
	  this.forwardWordTable = ForwardWordTable.getTable();
	  this.invertedIndexTable = InvertedIndexTable.getTable();
	  this.invertedPageTable = InvertedPageTable.getTable();
	  this.invertedWordTable = InvertedWordTable.getTable();
		this.url = url;
		this.wordIdGenerator = wordIdGenerator;
		this.pageIdGenerator = pageIdGenerator;
		
		// Save page
		this.pageId = this.pageIdGenerator.getId();
		PageInfo pageInfo = new PageInfo(this.url, this.extractTitle(),
		                                 this.extractSize(),
		                                 this.extractLastUpdate());
		this.forwardPageTable.insertURL(this.url, pageId);
    this.invertedPageTable.insertPageId(pageId, pageInfo);

		// Extract words
		//countWordsFrequency();

		// Commit all tables
		this.forwardIndexTable.terminate();
    this.forwardPageTable.terminate();
    this.forwardWordTable.terminate();
    this.invertedIndexTable.terminate();
    this.invertedPageTable.terminate();
    this.invertedWordTable.terminate();
	}

	//Instance methods.
  // -------------------------------------------------------------------------

	/**
	 * This method extracts the title from the given url
	 * 
	 * @return title of the webpage
	 * @throws IOException 
	 */
	private String extractTitle() throws IOException
	{
		return Jsoup.connect (url).get().title();
	}

	/**
	  * This method extracts the last update date from the given url
	  * 
	  * @return the last modification date
	 * @throws IOException 
	  */
	private Date extractLastUpdate() throws IOException
	{
		URL extractUrl = new URL(url);
	  HttpURLConnection httpCon = (HttpURLConnection)extractUrl.openConnection();
	  long date_temp = httpCon.getLastModified();
	  httpCon.disconnect();
	  return new Date(date_temp);
	}

	/**
	  * This method extracts the size from the given url
	  * 
	  * @return the size of the page
	 * @throws IOException 
	  */
	private long extractSize() throws IOException
	{    
	   URL extractUrl = new URL(url);
	   HttpURLConnection httpCon = (HttpURLConnection)extractUrl.openConnection();
	   long size = httpCon.getContentLengthLong();
	   httpCon.disconnect();
	   return size;
	}

	/**
	  * This method counts the frequency of all the words from the given url.
	  * Not yet finish
	 * @throws IOException 
	  */
	public void countWordsFrequency() throws ParserException, IOException
	{
		// Extract words in url and return them
		// Use StringTokenizer to tokenize the result from StringBean
		StringBean sb = new StringBean();
		Vector<String> v_str = new Vector<String>();
    boolean links = false;
    sb.setLinks (links);
    sb.setURL (url);
    String temp = sb.getStrings();

    // Space as symbol to separate word by word
    StringTokenizer st = new StringTokenizer(temp, " ");
		while (st.hasMoreTokens())
		  {
		    String word = st.nextToken();
			  if(!(this.forwardWordTable.hasWord(word)))
			    {
			      // create new id for the word on ForwardWordTable,
			      // InvertedWordTable and new posting list
			      int wordId = this.wordIdGenerator.getId();
			      this.forwardWordTable.insertWord(word, wordId);
			      this.invertedWordTable.insertWordId(wordId, word);
			    }
			  else
			    {
				    // check whether it is the first time of the word appear, 
				    // if yes, create new posting list
				    // else add frequency by 1 of that word of that page

			    }
		  }
	}

	/**
	  * This method extracts all the child links from the given url.
	  * Not yet finish
	 * @throws IOException 
	  */

	public void ExtractLinks() throws ParserException, IOException
	{
	    Vector<String> v_link = new Vector<String>();
	    LinkBean lb = new LinkBean();
	    lb.setURL(url);
	    URL[] URL_array = lb.getLinks();
	    for(int i=0; i<URL_array.length; i++){
	    	// page_id exits
	    	if(this.forwardPageTable.hasURL(URL_array[i].toString()))
	    	  {
	    		  // create pairs of parent link and child link
	    	  }
	    	else
	    	  {
	    		  // create page_id
	    		  // create pairs of parent link and child link
	    	  }
	    }
	}
}
