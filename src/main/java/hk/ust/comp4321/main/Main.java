package hk.ust.comp4321.main

import java.io.PrintStream;

public class Main
{

  // Class methods.
  // -------------------------------------------------------------------------

  public static void main(String ars[])
  {
    try
      {
        // Setup output file
        PrintStream out = new PrintStream(new FileOutputStream(
                                      "spider_result.txt"));
    
        // Extract pages
        Crawler clawler = new Crawler("http://www.cse.ust.hk");

        // Output results
        InvertedPageTable invertedPageTable = InvertedPageTable.getTable();
        ForwardIndexTable forwardIndexTable = forwardIndexTable.getTable();
        InvertedWordTable InvertedWordTable = InvertedWordTable.getTable();
        FastIterator iter = invertedPageTable.keys();
        int pageId;
        PageInfo pageInfo = null;
        while((pageId = (int)iter.next()) != null)
          {
            // Display an individual page
            pageInfo = invertedPageTable.getPageInfo (pageId);
            out.println("Title: " + pageInfo.getTitle());
            out.println("URL: " + pageInfo.getUrl());
            out.printf("%1$s %2$tB %2$td, %2$tY" ,
                              "Last modification date: ", pageInfo.getTitle());
            out.println("Page size: " + pageInfo.getUrl() + "byte");
            List<DocInfo> docInfoList = forwardIndexTable.getDocInfoList(pageId);
            for(int i = 0; i < docInfoList.size(); i++)
              { 
                String word = InvertedWordTable.getWord 
                              (docInfoList.get (i).getId());
                out.print( + docInfoList.get (i).getFrequency() + "; ");
              }
            out.println("");
            //TODO: Child link
          }

        // Close file
        out.close();
      }
      catch (FileNotFoundException ex)
        {
          System.err.println(ex.toString());
        }
  }
}
