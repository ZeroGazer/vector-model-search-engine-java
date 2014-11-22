package hk.ust.comp4321.crawler;

import hk.ust.comp4321.database.DocInfo;
import hk.ust.comp4321.database.ForwardIndexTable;
import hk.ust.comp4321.database.ForwardPageTable;
import hk.ust.comp4321.database.ForwardWordTable;
import hk.ust.comp4321.database.IDGenerator;
import hk.ust.comp4321.database.IndexInfo;
import hk.ust.comp4321.database.InvertedIndexTable;
import hk.ust.comp4321.database.InvertedPageTable;
import hk.ust.comp4321.database.InvertedWordTable;
import hk.ust.comp4321.database.PageInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.htmlparser.beans.LinkBean;
import org.htmlparser.beans.StringBean;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

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
	private StopStem stopStem;
	
	// Constructors.
  // -------------------------------------------------------------------------

	/**
	 * 
	 * @param url
	 * @param wordIdGenerator
	 * @param pageIdGenerator
	 * @throws IOException
	 * @throws ParserException 
	 */
	Extractor(String parentUrl, String url, IDGenerator wordIdGenerator,
	          IDGenerator pageIdGenerator) throws IOException, ParserException
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
		this.stopStem = new StopStem();

		// Save page
		this.pageId = this.pageIdGenerator.getId();
		PageInfo pageInfo = new PageInfo(this.url, this.extractTitle(),
		                                 this.extractSize(),
		                                 this.extractLastUpdate(),
		                                 parentUrl, this.extractLinks());
		
		this.forwardPageTable.insertURL (this.url, pageId);
    this.invertedPageTable.insertPageId (pageId, pageInfo);

		// Extract words
		this.countWordsFrequency();
	}

	//Instance methods.
  // -------------------------------------------------------------------------

	/**
	 * This method extracts the title from the given url
	 * 
	 * @return title of the webpage
	 * @throws IOException 
	 * @throws ParserException 
	 */
	private String extractTitle() throws IOException, ParserException
	{
	  Parser parser = new Parser(this.url);
	  HtmlPage htmlpage = new HtmlPage(parser);
	  parser.visitAllNodesWith(htmlpage);
	  return htmlpage.getTitle();
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
	  if(date_temp == 0)
	    date_temp = httpCon.getHeaderFieldDate ("Date", 0);
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
	  HttpURLConnection content = (HttpURLConnection)new URL (url)
	                            .openConnection();
	  long size = content.getContentLengthLong();
	  if (size != -1)
	    return content.getContentLength();
	  else
	    {
	      size = 0;
	      BufferedReader in = new BufferedReader(
	                          new InputStreamReader(content.getInputStream()));
	      String inputLine;

	      while ((inputLine = in.readLine()) != null)
	        size += inputLine.length();
	    }
	  content.disconnect();
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
    boolean links = false;
    sb.setLinks (links);
    sb.setURL (url);
    String temp = sb.getStrings();
    String[] words = temp.split("[\\W]+");
    int position = 0;

    // Non-word character as symbol to separate word by word
    for (int k = 0; k < words.length; k++)
		  {
		    position++;
		    String word = words[k];
		    if(stopStem.isStopWord(word.toLowerCase()))
		      continue;
		    int wordId;
			  if(!(this.forwardWordTable.hasWord (word)))
			    {
			      // create new id for the word on ForwardWordTable,
			      // InvertedWordTable and new posting list
			      wordId = this.wordIdGenerator.getId();
			      this.forwardWordTable.insertWord (word, wordId);
			      this.invertedWordTable.insertWordId (wordId, word);
			    }
			  else
			    {
				    // check whether it is the first time of the word appear, 
				    // if yes, create new posting list
				    // else add frequency by 1 of that word of that page
			      wordId = this.forwardWordTable.getWordID (word);
			    }
			  // Insert to forward index table
			  DocInfo docInfo = forwardIndexTable.getDocInfo (this.pageId, wordId);
			  if(docInfo == null)
			    forwardIndexTable.insertDocInfo(pageId, new DocInfo(wordId, 1));
			  else
			    forwardIndexTable.insertDocInfo(pageId, 
			                                    new DocInfo(wordId,
			                                                docInfo.getFrequency() 
			                                                + 1));
			  // Insert to inverted index table
			  IndexInfo indexInfo = invertedIndexTable.getIndexInfo(wordId, pageId);
			  List<Integer> positionList;
			  if(indexInfo == null)
			    {
			      positionList = new ArrayList<Integer>();
			      positionList.add(position);
			      indexInfo = new IndexInfo(pageId, positionList);
			      invertedIndexTable.insertIndexInfo(wordId, indexInfo);
			    }
			  else
			    {
			      indexInfo.getPositionList().add(position);
			      invertedIndexTable.insertIndexInfo(wordId, indexInfo);
			    }
		  }
	}

	/**
	  * This method extracts all the child links from the given url.
	  * Not yet finish
	  * @throws IOException 
	  */

	public List<String> extractLinks() throws ParserException, IOException
	{
	    List<String> v_link = new ArrayList<String>();
	    LinkBean lb = new LinkBean();
	    lb.setURL(url);
	    URL[] URL_array = lb.getLinks();
	    for(int i=0; i<URL_array.length; i++)
	    	v_link.add (URL_array[i].toString());
	    return v_link;
	}
}
