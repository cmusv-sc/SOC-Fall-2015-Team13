package search;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by bluebyte60 on 10/6/15.
 */
public class PostSearch {
    MySQL client;
    SimpleLucene lucene;
    final String selectSQL = "SELECT * FROM climate.Post;";
    public PostSearch()  {
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

    public void appendPost(long id, String content){
        lucene.appendPost(id, content);
    }

    public void deletePost(long id){
        lucene.deletePost(id);
    }

    private void prepareData() {
        ResultSet rs = client.select(selectSQL);
        try {
            while (rs.next()) {
                lucene.appendPost(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lucene.commit();
    }

    public static void main(String[] args) throws Exception {
        PostSearch LuceneSearch = new PostSearch();
        //demo basic search with title contains keyword "semantic || dependency"
        for (String id: LuceneSearch.basicSearch("apple", SearchMode.EXACTLY_MATCH, "content")) {
            System.out.println(id);
        }
    }

}
