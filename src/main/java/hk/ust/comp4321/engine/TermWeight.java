package hk.ust.comp4321.engine;

import java.io.IOException;

import hk.ust.comp4321.database.InvertedIndexTable;

/**
 * 
 * @author Paul Wong
 *
 */

public class TermWeight {
	
	// Constants and fields.
	// -------------------------------------------------------------------------	
	
	InvertedIndexTable invertedIndexTable;
	private int wordId;
	private int pageId;
	private final int totalPage = 300; //N
	
	// Constructors.
	// -------------------------------------------------------------------------

	/**
	 * This constructor encapsulates the WordId and PageId.
	 * 
	 * @param WordId        the word's id
	 * @param PageId        the page's id
	 */
	public TermWeight(int WordId, int PageId)
	{
		wordId = WordId;
		pageId = PageId;
	}
	
	/**
	 * This method converts base 10 log to base 2 log.
	 * 
	 * @param n        number needed to be log
	 */
	private static double log2(int n)
	{
	    return (Math.log(n) / Math.log(2));
	}
	
	/**
	 * This method finds the termWeight by (tf/max. tf) * log (N/df).
	 * 
	 * @return termWeight
	 */
	public double getTermWeight() throws IOException
	{
	  invertedIndexTable = InvertedIndexTable.getTable();
		int tfmax=0;
		int tf = invertedIndexTable.getIndexInfo(wordId, pageId).getPositionList().size(); //tf
		int numOfPage = invertedIndexTable.getIndexInfoList(wordId).size(); //df
		for(int i=0; i<numOfPage; i++) // max. tf
		{
			if (invertedIndexTable.getIndexInfoList(wordId).get(i).getPositionList().size() > tfmax)
			{
				tfmax = invertedIndexTable.getIndexInfoList(wordId).get(i).getPositionList().size();
			}
		}
		return (tf/tfmax)*log2(totalPage/numOfPage);
	}
}
