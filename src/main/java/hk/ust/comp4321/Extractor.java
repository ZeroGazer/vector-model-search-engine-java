import java.sql.Date;

import java.util.Vector;

import org.htmlparser.beans.StringBean;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.html.HTMLDocument;

import java.util.StringTokenizer;

import org.htmlparser.beans.LinkBean;

import sun.net.www.URLConnection;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author Paul Wong
 *
 */

public class Extractor {
	private String url;
	
	private int remaining_page;
	
	Extractor(String _url)
	{
		url = _url;
	}
	
	/**
	  * This method extracts the title from the given url and store in title:String.
	  * 
	  */
	public void ExtractTitle()
	{
		// need jsoup-1.8.1.jar, http://jsoup.org/download
		Document doc = Jsoup.connect(url).get();
		String title = doc.title();
	}
	
	/**
	  * This method extracts the last update date from the given url and store in date:Date.
	  * 
	  */
	public void ExtractLastUpdate()
	{
		URL Extracturl = new URL(url);
	    HttpURLConnection httpCon = (HttpURLConnection) Extracturl.openConnection();
	    long temp_date = httpCon.getLastModified();
	    Date date = new Date(temp_date);
	}
	
	/**
	  * This method extracts the size from the given url and store in size:int.
	  * 
	  */
	
	public void ExtractSize()
	{        
		StringBean sb = new StringBean();
		Vector<String> v_str = new Vector<String>();
        boolean links = false;
        sb.setLinks (links);
        sb.setURL (url);
        String temp = sb.getStrings();
        StringTokenizer st = new StringTokenizer(temp, " ");
        int size = st.countTokens();
	}
	
	/**
	  * This method counts the frequency of all the words from the given url.
	  * Not yet finish
	  */
	
	public void CountWordsFrequency() throws ParserException
	{
		// extract words in url and return them
		// use StringTokenizer to tokenize the result from StringBean
		StringBean sb = new StringBean ();
		Vector<String> v_str = new Vector<String>();
        boolean links = false;
        sb.setLinks (links);
        sb.setURL (url);
        String temp = sb.getStrings();
        // space as symbol to separate word by word
        StringTokenizer st = new StringTokenizer(temp, " ");
		while (a.hasMoreTokens()){
			if(!(forwardWordTable.hasWord(a.nextToken()))
				// create new id for the word on ForwardWordTable and InvertedWordTable
				// create new posting list
			else if 
				// check whether it is the first time of the word appear, 
				// if yes, create new posting list
				// else add frequency by 1 of that word of that page

		}	
	}
	
	/**
	  * This method extracts all the child links from the given url.
	  * Not yet finish
	  */
	
	public void ExtractLinks() throws ParserException
	{
	    Vector<String> v_link = new Vector<String>();
	    LinkBean lb = new LinkBean();
	    lb.setURL(url);
	    URL[] URL_array = lb.getLinks();
	    for(int i=0; i<URL_array.length; i++){
	    	// page_id exits
	    	if(ForwardPageTable.hasURL(URL_array[i].toString()))
	    		// create pairs of parent link and child link
	    	else
	    		// create page_id
	    		// create pairs of parent link and child link
	    }

	}
}
