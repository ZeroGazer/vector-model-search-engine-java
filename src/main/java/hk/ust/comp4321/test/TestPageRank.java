package hk.ust.comp4321.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import hk.ust.comp4321.database.DocInfo;
import hk.ust.comp4321.database.ForwardIndexTable;
import hk.ust.comp4321.database.InvertedPageTable;
import hk.ust.comp4321.database.InvertedWordTable;
import hk.ust.comp4321.database.PageInfo;
import hk.ust.comp4321.engine.PageRank;
import hk.ust.comp4321.engine.PhraseChecker;

public class TestPageRank
{
  private static InvertedPageTable invertedPageTable = InvertedPageTable.getTable();
  private static ForwardIndexTable forwardIndexTable = ForwardIndexTable.getTable();
  private static InvertedWordTable invertedWordTable = InvertedWordTable.getTable();

  public static void main(String[] args) throws IOException
  {
    PrintStream out = new PrintStream(new FileOutputStream(
      "test.txt"));
    PageRank pageRank = new PageRank("\"literature listings\"");
    double[] score = pageRank.returnTotalSimScore();
    int[] rank = pageRank.returnRankedList();
    for(int i = 0; i < rank.length && i < 50 && score[i] != 0 && new PhraseChecker("\"literature listings\"", rank[i]).isConsecutive(); i++)
      {
        out.println("Score: " + score[i]);
        PageInfo pageInfo = invertedPageTable .getPageInfo (rank[i]);
        out.println("Title: " + pageInfo.getTitle());
        out.println("URL: " + pageInfo.getUrl());
        out.println("Last modification date: " + pageInfo.getDate());
        out.println("Page size: " + pageInfo.getSize() + "byte");
        List<DocInfo> docInfoList = forwardIndexTable.getDocInfoList (rank[i]);
        for(int j = 0; j < docInfoList.size(); j++)
          {
            String word = invertedWordTable .getWord (docInfoList.get (j).getId());
            out.print(word + " " + docInfoList.get (j).getFrequency() + "; ");
          }
        out.println("");
        List<String> childLinks = pageInfo.getChildLinks();
        for(int j = 0; j < childLinks.size(); j++)
          out.println(childLinks.get(j));
        out.println("----------------------------------------------------");
      }
    out.close();
  }
}
