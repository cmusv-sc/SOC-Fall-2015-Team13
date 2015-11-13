package search;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by bluebyte60 on 10/6/15.
 */
public class UserSearch {
    MySQL client;
    SimpleLucene lucene;
    final String selectSQL = "select * from climate.User";
    public UserSearch()  {
        client = new MySQL("climate", "climate");
        try {
            lucene = new SimpleLucene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareData();
    }


    public List<String> basicSearch(String query, SearchMode mode, String field) throws Exception {
        return lucene.search(query, mode, field);
    }

    private void prepareData() {
        ResultSet rs = client.select(selectSQL);
        try {
            while (rs.next()) {
                lucene.append(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lucene.endOfAppend();
    }
//
//    public static void main(String[] args) throws Exception {
//        UserSearch LuceneSearch = new UserSearch();
//        //demo basic search with title contains keyword "semantic || dependency"
//        for (String id: LuceneSearch.basicSearch("chen", SearchMode.EXACTLY_MATCH, "lastName")) {
//            System.out.println(id);
//        }
//    }

}
