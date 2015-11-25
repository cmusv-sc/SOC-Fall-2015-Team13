package search;

import models.User;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class SimpleLucene {
    // 0. Specify the analyzer for tokenizing text.
    // The same analyzer should be used for indexing and searching
    StandardAnalyzer analyzer = new StandardAnalyzer();
    // 1. create the index
    Directory index = new RAMDirectory();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    IndexWriter w;

    public SimpleLucene() throws IOException {
        w = new IndexWriter(index, config);
    }


    public void append(ResultSet rs) {
        try {
            addUser(w, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendPost(long id, String content) {
        try {
            Document doc = new Document();
            doc.add(new TextField("content", content, Field.Store.YES));
            doc.add(new TextField("id", String.valueOf(id), Field.Store.YES));
            w.addDocument(doc);
            w.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePost(long id){
        try {
            w.deleteDocuments(new Term("id", String.valueOf(id)));
            w.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendPost(ResultSet rs) {
        try {
            addPostDoc(w, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            w.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPostDoc(IndexWriter w, ResultSet rs) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("content", rs.getString("content"), Field.Store.YES));
        doc.add(new TextField("id", val(rs.getString("id")), Field.Store.YES));
        w.addDocument(doc);
    }

    public void appendUser(long id, User user) {
        try {
            Document doc = new Document();
            doc.add(new TextField("id", val(String.valueOf(id)), Field.Store.YES));
            doc.add(new TextField("firstName", val(user.getFirstName()), Field.Store.YES));
            doc.add(new TextField("lastName", val(user.getLastName()), Field.Store.YES));
            doc.add(new TextField("researchFields", val(user.getResearchFields()), Field.Store.YES));
            doc.add(new TextField("default", val(user.getResearchFields()) + " "
                    + val(user.getResearchFields()) + " "
                    + val(user.getLastName()), Field.Store.YES));
            w.addDocument(doc);
            w.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(long id){
        try {
            w.deleteDocuments(new Term("id", String.valueOf(id)));
            w.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUser(IndexWriter w, ResultSet rs) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("id", val(rs.getString("id")), Field.Store.YES));
        doc.add(new TextField("firstName", val(rs.getString("firstName")), Field.Store.YES));
        doc.add(new TextField("lastName", val(rs.getString("lastName")), Field.Store.YES));
        doc.add(new TextField("researchFields", val(rs.getString("researchFields")), Field.Store.YES));
        doc.add(new TextField("default", val(rs.getString("researchFields")) + " "
                + val(rs.getString("firstName")) + " "
                + val(rs.getString("lastName")), Field.Store.YES));
        w.addDocument(doc);
    }

    private String val(String s) {
        if (s == null) return "";
        else return s;
    }

    public List<String> search(String keyword, SearchMode mode, String field) throws ParseException, IOException {
        List<String> result = new ArrayList<>();
        // 1. get query string according to mode
        String querystr = getQuery(mode, keyword);
        // 2. Set field to query
        Query q;
        if (mode == SearchMode.EXACTLY_MATCH) q = new QueryParser(field, analyzer).parse(querystr);
        else q = new FuzzyQuery(new Term(field, keyword), (int) FuzzyQuery.defaultMinSimilarity, 0);
        // 3. search
        int hitsPerPage = 100000;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        // 4. display results

        for (ScoreDoc hit : hits) {
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            result.add(d.get("id"));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        return result;
    }

    private String getQuery(SearchMode mode, String query) {
        String[] tokon = query.split(" ");
        String r = "";
        for (int i = 0; i < tokon.length; i++) {
            if (mode == SearchMode.EXACTLY_MATCH) {
                if (i != tokon.length - 1)
                    r += tokon[i] + " || ";
                else
                    r += tokon[i];
            } else {
                if (i != tokon.length - 1)
                    r += tokon[i] + "~ || ";
                else
                    r += tokon[i] + "~";
            }
        }
        return r;

    }
}

