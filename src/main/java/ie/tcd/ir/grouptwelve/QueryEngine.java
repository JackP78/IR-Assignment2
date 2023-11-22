package ie.tcd.ir.grouptwelve;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


class QueryEngine {

    private String similarityStrategy;

    private ArrayList<QueryItem> queries = new ArrayList<QueryItem>();
    private ArrayList<QueryResult> results = new ArrayList<QueryResult>();

    public QueryEngine(Analyzer analyzer, Similarity similarity, String type) {
        try {
            ParseFile();
            this.similarityStrategy = type;
            ExecuteQueries(analyzer, similarity);
            SaveResultsFile();
        } catch (Exception e) {
            System.out.println("Issue with QueryEngine.");
            e.printStackTrace();
        }
    }

    public void SaveResultsFile() throws IOException {
        Files.createDirectories(Paths.get(Main.RESULTS_DIRECTORY));
        String filePath = Main.RESULTS_DIRECTORY + this.similarityStrategy + ".test";
        System.out.println("Writing file " + filePath);

        FileWriter fileWriter = new FileWriter(filePath);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (QueryResult result: this.results) {
            printWriter.println(result.toString());
        }
        printWriter.close();
        fileWriter.close();
    }

    public void ParseFile() throws IOException {
        // File opening Setup
        InputStream fstream = QueryEngine.class.getClassLoader().getResourceAsStream("topics.xml");
        org.jsoup.nodes.Document doc = Jsoup.parse(fstream, "UTF-8", "http://example.com/");

        Elements topics = doc.body().select("top");
        for (Element topic : topics) {
            Elements queryNumber = topic.select("num");
            String queryId = queryNumber.text().trim().substring(8);
            System.out.println("query: " + queryId);
            Elements queryTitle = topic.select("title");
            System.out.println("title: " + queryTitle.text());
            Elements description = topic.select("desc");
            System.out.println("description: " + description.text());
            Elements narrative = topic.select("narr");
            System.out.println("description: " + narrative.text());
            QueryItem query = new QueryItem(Integer.parseInt(queryId), queryTitle.text(), description.text(), narrative.text());
            this.queries.add(query);
        }

        System.out.println("Num queries processed " + this.queries.size());
    }

    public void ExecuteQueries(Analyzer analyzer, Similarity similarity) throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(Main.INDEX_DIRECTORY));
		
		DirectoryReader reader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(reader);

        indexSearcher.setSimilarity(similarity);
        System.out.println(indexSearcher.getSimilarity());

		for (QueryItem thisQuery: this.queries) {
            try {
                System.out.println("Query ID " + thisQuery.getId() + " parsed");
                String narrativeTerm = stripPunctuation(thisQuery.getNarrative());
                String descriptionTerm = stripPunctuation(thisQuery.getDescription());
                String titleTerm = stripPunctuation(thisQuery.getDescription());
                String[] terms = new String[]{narrativeTerm, descriptionTerm, titleTerm};
                Query query = MultiFieldQueryParser.parse(terms,
                        new String[]{Indexer.TITLE, Indexer.SUMMARY, Indexer.BODY},
                        analyzer);
                ScoreDoc[] hits = indexSearcher.search(query, 50).scoreDocs;
                if (hits.length == 0) {
                    System.out.println("No results for query " + thisQuery.getId());
                }
                for (int i = 0; i < hits.length; i++) {
                    Document hitDoc = indexSearcher.doc(hits[i].doc);
                    QueryResult searchResult = new QueryResult(thisQuery.getId(),hitDoc.get("ID"), i+1, hits[i].score, this.similarityStrategy);
                    this.results.add(searchResult);
                }
            }
            catch (Exception e) {
                System.out.println("Error running query " + thisQuery.getId());
                e.printStackTrace();
            }

        }

		reader.close();
		directory.close();
    }

    // get rid of those hasty question marks
    private String stripPunctuation(String line) {
        return line.replaceAll("\\/", "");
    }

}