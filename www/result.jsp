<%@ page import="hk.ust.comp4321.database.*, hk.ust.comp4321.engine.*" %>
<%@ page import="java.util.List, jdbm.helper.FastIterator" %>

<%  String query = request.getParameter("query");
    String[] queryArray= query.replace("\"", "").split(" ");
%>
<!DOCTYPE html>
<html>
  <title>Result of <% out.println(query); %></title>
  <body>
    <%
      InvertedPageTable invertedPageTable = InvertedPageTable.getTable();
      ForwardIndexTable forwardIndexTable = ForwardIndexTable.getTable();
      InvertedWordTable invertedWordTable = InvertedWordTable.getTable();

      PageRank pageRank = new PageRank(query);
      PageInfo pageInfo;
      int[] rankedId = pageRank.returnRankedList();
      double[] rankedScore = pageRank.returnTotalSimScore();
      Integer i, j, k, l;
      for(i = 0; i < rankedId.length && i < 50 && rankedScore[i] != 0 && new PhraseChecker(query, rankedId[i]).isConsecutive(); i++)
        {
          pageInfo = invertedPageTable.getPageInfo (rankedId[i]);

          // print score
          out.println("Score: " + String.format("%.10f", rankedScore[i]) + "<br />");

          // print title
          out.print("<a href=\"" + pageInfo.getUrl() + "\">");
          String[] titleArray = pageInfo.getTitle().split(" ");
          for(j = 0; j < titleArray.length; j++)
            {
              String word = titleArray[j];
              Boolean isKeyword = false;
              for(k = 0; k < queryArray.length; k++)
                if(queryArray[k].equals(word))
                  isKeyword = true;
              if(isKeyword)
                out.print("<span style=\"color: red\">" + word + "</span>" + " ");
              else
                out.print(word + " ");
            }
          out.print("</a><br />");

          // print url
          out.println("URL: <a href=\"" + pageInfo.getUrl() + "\">"+ pageInfo.getUrl() + "</a><br />");

          // print date
          out.println("Last modification date: " + pageInfo.getDate() + "<br />");

          // print page size
          out.println("Page size: " + pageInfo.getSize() + "byte" + "<br />");

          // print keywords
          List<DocInfo> docInfoList = forwardIndexTable.getDocInfoList (rankedId[i]);
          Integer[] wordArray = new Integer[5];
          for(j = 0; j < 5; j++)
            wordArray[j] = -1;
          Integer[] frequencyArray = new Integer[5];
          for(j = 0; j < 5; j++)
            {
              Integer maxFrequency = 0;
              for(k = 0; k < docInfoList.size(); k++)
              {
                Boolean newWord = true;
                for(l = 0; l < j; l++)
                  if(docInfoList.get (k).getId() == wordArray[l])
                    newWord = false;
                if(docInfoList.get (k).getFrequency() > maxFrequency && newWord)
                  {
                    maxFrequency = docInfoList.get (k).getFrequency();
                    wordArray[j] = docInfoList.get (k).getId();
                    frequencyArray[j] = maxFrequency;
                  }
              }
            }
          for(j = 0; j < 5 && wordArray[j] != null; j++)
             {
               String word = invertedWordTable.getWord(wordArray[j]);
               Boolean isKeyword = false;
               for(k = 0; k < queryArray.length; k++)
                 if(queryArray[k].equals(word))
                   isKeyword = true;
               if(isKeyword)
                 out.print("<span style=\"color: red\">" + word + "</span>" + " " + frequencyArray[j] + "; ");
               else
                 out.print(word + " " + frequencyArray[j] + "; ");
              }
          out.println("<br />");

          // print links
          out.println("Child links:" + "<br />");
          List<String> childLinks = pageInfo.getChildLinks();
          for(j = 0; j < childLinks.size(); j++)
            out.println("<a href=\"" + childLinks.get(j) + "\">"+ childLinks.get(j) + "</a><br />");
          out.println("<br />" + "----------------------------------------------------" + "<br /><br />");
        }
      if(rankedScore[0] == 0)
        out.println("<div style=\"color: black; font-size: 20px; text-align: center;\">" + "Your search - " + query + " - did not match any documents." + "</div>");
    %>
  </body>
</html>