<%@ page import="hk.ust.comp4321.database.*, hk.ust.comp4321.engine.*" %>
<%@ page import="java.util.List, jdbm.helper.FastIterator" %>

<!DOCTYPE html>
<html>
  <body>
    <%
      String query = request.getParameter("query");

      InvertedPageTable invertedPageTable = InvertedPageTable.getTable();
      ForwardIndexTable forwardIndexTable = ForwardIndexTable.getTable();
      InvertedWordTable invertedWordTable = InvertedWordTable.getTable();

      PageRank pageRank = new PageRank(query);
      PageInfo pageInfo;
      int[] rankedId = pageRank.returnRankedList();
      double[] rankedScore = pageRank.returnTotalSimScore();
  
      Integer i, j;
      for(i = 0; i < rankedId.length && i < 50; i++)
        {
          out.println("Score: " + rankedScore[i] + "<br />");
          pageInfo = invertedPageTable.getPageInfo (rankedId[i]);
          out.println("Title: " + pageInfo.getTitle() + "<br />");
          out.println("URL: " + pageInfo.getUrl() + "<br />");
          out.println("Last modification date: " + pageInfo.getDate() + "<br />");
          out.println("Page size: " + pageInfo.getSize() + "byte" + "<br />");
          List<DocInfo> docInfoList = forwardIndexTable.getDocInfoList (rankedId[i]);
          for(j = 0; j < docInfoList.size(); j++)
            {
              String word = invertedWordTable.getWord (docInfoList.get (j).getId());
              out.print(word + " " + docInfoList.get (j).getFrequency() + "; ");
            }
          out.println("<br />");
          List<String> childLinks = pageInfo.getChildLinks();
          for(j = 0; j < childLinks.size(); j++)
            out.println(childLinks.get(j));
          out.println("<br />" + "----------------------------------------------------" + "<br /><br />");
        }
    %>
  </body>
</html>