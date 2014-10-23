package hk.ust.comp4321.test;

import hk.ust.comp4321.database.DocInfo;
import hk.ust.comp4321.database.ForwardIndexTable;
import hk.ust.comp4321.database.ForwardPageTable;
import hk.ust.comp4321.database.ForwardWordTable;
import hk.ust.comp4321.database.InvertedIndexTable;
import hk.ust.comp4321.database.InvertedPageTable;
import hk.ust.comp4321.database.InvertedWordTable;
import hk.ust.comp4321.database.PageInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import jdbm.helper.FastIterator;

/**
 * 
 * @author Alex Poon
 *
 */
public class TestProgram
{

  // Class methods.
  // -------------------------------------------------------------------------

  public static void main(String args[]) throws IOException
  {
    try
      {
        // Setup output file
        PrintStream out = new PrintStream(new FileOutputStream(
                                      "spider_result.txt"));

        // Output results
        InvertedPageTable invertedPageTable = InvertedPageTable.getTable();
        ForwardIndexTable forwardIndexTable = ForwardIndexTable.getTable();
        InvertedWordTable invertedWordTable = InvertedWordTable.getTable();
        FastIterator iter = invertedPageTable.keys();
        Integer pageId;
        PageInfo pageInfo = null;
        while((pageId = (Integer)iter.next()) != null)
          {
            // Display an individual page
            pageInfo = invertedPageTable.getPageInfo (pageId);
            out.println("Title: " + pageInfo.getTitle());
            out.println("URL: " + pageInfo.getUrl());
            out.println("Last modification date: " + pageInfo.getDate());
            out.println("Page size: " + pageInfo.getSize() + "byte");
            List<DocInfo> docInfoList = forwardIndexTable.getDocInfoList (pageId);
            for(int i = 0; i < docInfoList.size(); i++)
              {
                String word = invertedWordTable.getWord 
                              (docInfoList.get (i).getId());

                out.print(word + " " + docInfoList.get (i).getFrequency()
                          + "; ");
              }
            out.println("");
            List<String> childLinks = pageInfo.getChildLinks();
            for(int i = 0; i < childLinks.size(); i++)
              out.println(childLinks.get(i));
            out.println("----------------------------------------------------");         
          }

        // Close file
        out.close();
      }
    catch (FileNotFoundException ex)
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
}
