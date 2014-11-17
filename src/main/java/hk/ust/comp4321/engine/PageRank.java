package hk.ust.comp4321.engine;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import hk.ust.comp4321.database.DocInfo;
import hk.ust.comp4321.database.ForwardIndexTable;
import hk.ust.comp4321.database.ForwardWordTable;
import hk.ust.comp4321.database.IndexInfo;
import hk.ust.comp4321.database.InvertedIndexTable;
import hk.ust.comp4321.database.InvertedPageTable;

/**
 * 
 * @author Paul Wong
 *
 */

public class PageRank {
	
	// Constants and fields.
	// -------------------------------------------------------------------------	
	
	String query;
	String[] divideQuery;
	ForwardWordTable forwardWordTable;
	ForwardIndexTable forwardIndexTable;
	InvertedIndexTable invertedIndexTable;
	InvertedPageTable invertedPageTable;
	private final int totalPage = 300;
	double[] cosSimScore = new double[totalPage];
	double[] titleSimScore = new double[totalPage];
	double[] totalSimScore = new double[totalPage];
	int[] rankedList = new int[totalPage];
	
	// Constructors.
	// -------------------------------------------------------------------------

	/**
	 * This constructor encapsulates the query.
	 * 
	 * @param query        the query
	 */
	private PageRank(String Query)
	{
		query = Query;
		query = query.replace("\"", "");
		divideQuery = query.split(" ");
		initializeAllScore();
		initializeRankedList();
	}
	
	/**
	 * This method initializes the cosSimScore array and titleSimScore array
	 * 
	 */
	private void initializeAllScore()
	{
		for(int i = 0; i < totalPage; i++)
			{
				cosSimScore[i] = 0;
				titleSimScore[i] = 0;
			}
	}
	
	/**
	 * This method initializes the rankedlist, -1 means not occupied, the place should be occupied by pageId.
	 * 
	 */
	private void initializeRankedList()
	{
		for(int i = 0; i < totalPage; i++)
			rankedList[i] = -1;
	}
	
	/**
	 * This method finds the length of query, lecture note 3, p.25.
	 * 
	 */
	private double getQueryLength()
	{
		String[] copyQuery = new String [divideQuery.length];
		for(int i=0; i<divideQuery.length; i++)
		{
			copyQuery[i] = divideQuery[i];
		}
		Vector<Integer> countLength = new Vector<Integer>();
		Vector<Integer> countedElement = new Vector<Integer>();
		for(int i=0; i<copyQuery.length; i++)
		{
			if(!countedElement.contains(i))		//skip counted element
			{
				countedElement.add(i);
				int counter = 1;
				for(int j=0; i<copyQuery.length; j++)
				{
					if (i!=j && copyQuery[i].equals(copyQuery[j]))
						{
							counter++;
							countedElement.add(j);
						}
				}
				countLength.add((int) Math.pow(counter,2));
			}
		}
		int squareQueryLength = 0;
		for(int i=0; i<countLength.size(); i++)
			squareQueryLength += countLength.get(i);
		return Math.sqrt(squareQueryLength);
	}
	
	/**
	 * This method finds the length of document(page) of the given corresponding pageId, lecture note 3, p.25.
	 * 
	 * @param pageId the document(page) id
	 * @return pageLength the document(page) length
	 */
	private double getPageLength(int pageId) throws IOException
	{
		List<DocInfo> docInfo = forwardIndexTable.getDocInfoList(pageId);
		double squareDocLength = 0;
		for(int i=0; i<docInfo.size();i++)
			squareDocLength += Math.pow(docInfo.get(i).getFrequency(), 2);
		return Math.sqrt(squareDocLength);
	}
	
	/**
	 * This method finds the cosine similarity between all the pages and the query, lecture note 3, p.25.
	 * 
	 */	
	private void findCosSim() throws IOException
	{
		// algorithm of lecture notes 4
		for(int i=0; i<divideQuery.length; i++)
		{
			Integer wordId = forwardWordTable.getWordID(divideQuery[i]);
			if (wordId != null)		// key exists
			{
				List<IndexInfo> postingList = invertedIndexTable.getIndexInfoList(wordId);
				for (int j = 0; j < postingList.size(); j++)
				{
					int pageId = postingList.get(j).getId();
					TermWeight termWeight = new TermWeight((int)wordId, pageId);
					cosSimScore[pageId] += termWeight.getTermWeight();
				}
			}
		}
		for(int i=0; i<totalPage; i++)
		{
			cosSimScore[i] = cosSimScore[i]/(getPageLength(i)*getQueryLength());
		}
	}
	
	/**
	 * This method finds the length of documents'(pages') title of the given corresponding pageId.
	 * 
	 * @param pageId the document(page) id
	 * @return titleLength the document(page) title's length
	 */	
	private double getTitleLength(int pageId) throws IOException
	{
		String title = invertedPageTable.getPageInfo(pageId).getTitle();
		String[] divideTitle = title.split(" ");
		Vector<Integer> countLength = new Vector<Integer>();
		Vector<Integer> countedElement = new Vector<Integer>();
		for(int i=0; i<divideTitle.length; i++)
		{
			if(!countedElement.contains(i))		//skip counted element
			{
				countedElement.add(i);
				int counter = 1;
				for(int j=0; i<divideTitle.length; j++)
				{
					if (i!=j && divideTitle[i].equals(divideTitle[j]))
						{
							counter++;
							countedElement.add(j);
						}
				}
				countLength.add((int) Math.pow(counter,2));
			}
		}
		int squareTitleLength = 0;
		for(int i=0; i<countLength.size(); i++)
			squareTitleLength += countLength.get(i);
		return Math.sqrt(squareTitleLength);
	}
	
	/**
	 * This method finds the cosine similarity between all the pages' title and the query.
	 * Use similar way with cosine similarity, but replace d by term frequency.
	 */	
	private void findTitleSim() throws IOException
	{
		for (int id=0; id<totalPage;id++)
		{
			String title = invertedPageTable.getPageInfo(id).getTitle();
			String[] divideTitle = title.split(" ");
			for(int i=0; i<divideTitle.length; i++)
			{
				for(int j=0; j<divideQuery.length; j++)
				{
					if(divideTitle[i].equals(divideQuery[j]))
						titleSimScore[i] += 1;
				}
			}
		}
		for(int i=0; i<totalPage; i++)
		{
			titleSimScore[i] = titleSimScore[i]/(getTitleLength(i)*getQueryLength());
		}
	}
	
	/**
	 * This method finds the total similarity between all the pages and the query.
	 * Use title with weight 0.7 and cosine similarity with weight 0.3 to find the total similarity. 0.7 and 0.3 can be changed.
	 */	
	private void findTotalSim() throws IOException
	{
		findCosSim();
		findTitleSim();
		final double weightOfTitle = 0.7;
		final double weightOfCosSim = 0.3;
		for(int i=0; i<totalPage; i++)
		{
			totalSimScore[i] = weightOfTitle*titleSimScore[i] + weightOfCosSim*cosSimScore[i];
		}
	}
	
	/**
	 * This method rank all the totalSimScore in decreasing order.
	 * Note that we can always find back the totalSimScore of rank=i by totalSimScore[rankedList[i]]. 
	 */	
	private void getRankedList() throws IOException
	{
		findTotalSim();
		for (int i=0; i<totalPage; i++)
		{
			int rank = totalPage-1;
			for(int j=0; j<totalPage; j++)
			{
				if(i!=j && totalSimScore[i] > totalSimScore[j])
					rank--;
			}
			while(rankedList[rank] != -1)	// The ranking is occupied
				rank--;
			rankedList[rank] = i;
		}
	}
}
