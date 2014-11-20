package main.java.hk.ust.comp4321.crawler;

import main.java.hk.ust.comp4321.database.ForwardIndexTable;
import main.java.hk.ust.comp4321.database.InvertedIndexTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import main.java.hk.ust.comp4321.database.ForwardWordTable;

/**
 * 
 * @author Paul Wong
 *
 */

public class PhraseChecker {
	
	// Constants and fields.
	// -------------------------------------------------------------------------	
	
	String query;
	String[] divideQuery;
	int	pageId;
	Vector<Integer> quoMarkPosition = new Vector<Integer>();
	Vector<String> subQuery = new Vector<String>();
	Vector<Boolean> phraseMatch = new Vector<Boolean>();	// use for store value of each pair of "" 
	InvertedIndexTable invertedIndexTable;
	ForwardIndexTable forwardIndexTable;
	ForwardWordTable forwardWordTable;
	
	// Constructors.
	// -------------------------------------------------------------------------

	/**
	 * This constructor encapsulates the query.
	 * 
	 * @param Query        the query which is needed to be check
	 * @param PageId       the pagedId which is needed to be check
	 */
	
	public PhraseChecker(String Query, int PageId)
	{
		query = Query;
		pageId = PageId;
	}
	
	/**
	 * This method finds the cartesian product of a list of list, the cartesian product store at result
	 * 
	 * @param input        the list of list that we want to find its cartesian product.
	 * @param current      temp used to store a cartesian product in each iteration, use the size of the cartesian product size (=list of list size)
	 * @param k			   the outer position, start at 0.
	 * @param result	   the cartesian product result.
	 */
	
	private static void cartesianProduct(List<List<Integer>> input, int[] current, int k, List<List<Integer>> result) {

	    if(k == input.size()) {
	    	List<Integer> output = new ArrayList<Integer>();
	        for(int i = 0; i < k; i++) {
	            output.add(current[i]);
	        }
	        result.add(output);
	    } else {            
	        for(int j = 0; j < input.get(k).size(); j++) {
	            current[k] = input.get(k).get(j);
	            cartesianProduct(input, current, k + 1, result);
	        }       
	    }
	}
	
	/**
	 * This method finds whether a page contains all the phrase (phrase is quoted inside "")
	 * 
	 * @return boolean 		true if the pages contains all the phrase, otherwise false
	 */
	public boolean isConsecutive () throws IOException
	{
		if(query.contains("\""))
		{
			for (int i = -1; (i = query.indexOf("\"", i + 1)) != -1; ) 		//find all quotation mark position
			{
			    quoMarkPosition.add(i);
			}
			if(quoMarkPosition.size()%2 != 0)			// if odd, delete last position
			{
				quoMarkPosition.remove(quoMarkPosition.size()-1);
			}
			for (int i = 0; i< quoMarkPosition.size(); i = i+2)		//get all string inside quotation mark
			{
				subQuery.add(query.substring(quoMarkPosition.get(i)+1, quoMarkPosition.get(i+1)));
			}
			for (int i=0; i<subQuery.size(); i++)	// subQuery.size() equal to the number of pair of "", use for loop to check for each pair
			{
				divideQuery = subQuery.get(i).split(" ");		//split the subquery into words
				List<List<Integer>> wordPositionList = new ArrayList<List<Integer>>();
				for (int k=0; k<divideQuery.length; k++)
				{
					Integer wordId = forwardWordTable.getWordID(divideQuery[k]);
					if (wordId == null)		// words don't exist in any page			
						return false;
					wordPositionList.add(invertedIndexTable.getIndexInfo(wordId, pageId).getPositionList()); // create list of posting list
				}
				List<List<Integer>> cartesianProductList = new ArrayList<List<Integer>>();
				cartesianProduct(wordPositionList, new int[wordPositionList.size()], 0, cartesianProductList); // get caresianProductList
				for(int m=0; m<cartesianProductList.size(); m++)
				{
					for(int n=0; n<cartesianProductList.get(m).size(); n++)
					{
						if(cartesianProductList.get(m).get(n)!= cartesianProductList.get(m).get(n+1)-1)	//not phrase, check next CP
							break;
						else if (n == cartesianProductList.get(m).size() - 1)	//it is a phrase, store true value
							phraseMatch.add(true);
					}
					if(phraseMatch.size() == i+1)	//found a phrase match, can goto check next phrase
						break;
				}
			}
			if(phraseMatch.size()==subQuery.size())	//each pair phrase match, return true
				return true;
			else 
				return false;
			
		}
		else	//no need to do phrase match
			return true;
	}
}
