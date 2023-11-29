package ie.tcd.ir.grouptwelve;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


class QueryEngine {

    private ArrayList<QueryItem> queries = new ArrayList<QueryItem>();
    private ArrayList<QueryResult> results = new ArrayList<QueryResult>();

    protected static Logger logger = LogManager.getLogger(QueryEngine.class);

    public QueryEngine() {
        try {
            ParseFile();
        } catch (Exception e) {
            logger.error("Issue with QueryEngine.");
            logger.error("Exception: ", e);
        }
    }

    public void SaveResultsFile(String fileName) throws IOException {
        Files.createDirectories(Paths.get(Main.RESULTS_DIRECTORY));
        String filePath = Main.RESULTS_DIRECTORY + fileName + ".test";
        logger.info("Writing file " + filePath);

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
            logger.debug("query: " + queryId);
            Elements queryTitle = topic.select("title");
            logger.debug("title: " + queryTitle.text());
            Elements description = topic.select("desc");
            logger.debug("description: " + description.text());
            Elements narrative = topic.select("narr");
            logger.debug("narrative: " + narrative.text());

            // Update narrative to remove sentences that contain "not relevent"
            String updatedNarrative = updateNarrative(narrative.text());

            QueryItem query = new QueryItem(Integer.parseInt(queryId), queryTitle.text(), description.text(), updatedNarrative);
            this.queries.add(query);
        }

        logger.info("Num queries processed " + this.queries.size());
    }

    public void ExecuteQueries(Analyzer analyzer, Similarity similarity, String similarityStrategy) {
        results = new ArrayList<QueryResult>();
        try {
            Directory directory = FSDirectory.open(Paths.get(Main.INDEX_DIRECTORY));

            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(reader);

            indexSearcher.setSimilarity(similarity);

            for (QueryItem thisQuery: this.queries) {
                try {
                    logger.debug("Query ID " + thisQuery.getId() + " parsed");

                    // Get separate topic terms
                    String narrativeTerm = stripPunctuation(thisQuery.getNarrative());
                    String descriptionTerm = stripPunctuation(thisQuery.getDescription());
                    String titleTerm = stripPunctuation(thisQuery.getDescription());
                    String[] terms = new String[]{titleTerm, descriptionTerm, descriptionTerm, narrativeTerm, narrativeTerm};

                    // Build query
                    Query query = MultiFieldQueryParser.parse(terms,
                            new String[]{Indexer.TITLE, Indexer.SUMMARY, Indexer.BODY, Indexer.SUMMARY, Indexer.BODY},
                            analyzer);

                    // Get results from query
                    ScoreDoc[] hits = indexSearcher.search(query, 1000).scoreDocs;

                    if (hits.length == 0) {
                        logger.warn("No results for query " + thisQuery.getId());
                    }
                    for (int i = 0; i < hits.length; i++) {
                        Document hitDoc = indexSearcher.doc(hits[i].doc);
                        QueryResult searchResult = new QueryResult(thisQuery.getId(),hitDoc.get("ID"), i+1, hits[i].score, similarityStrategy);
                        this.results.add(searchResult);
                    }
                }
                catch (Exception e) {
                    logger.error("Error running query " + thisQuery.getId());
                    logger.error("Exception: ", e);
                }
            }

            reader.close();
            directory.close();
            SaveResultsFile(similarityStrategy);
        }
        catch(Exception c) {
            logger.error("Exception: ", c);
        }
    }

    // get rid of those hasty question marks
    private String stripPunctuation(String line) {
        return line.replaceAll("\\/", "");
    }

        // Removes sentences that contain "not relevant" from Narrative
    public static String updateNarrative(String narrative) {
        String input = narrative.substring(10);

        String[] sentences = input.split("(?<=[a-z])\\.\\s+");

        StringBuilder result = new StringBuilder();
        for (String sentence : sentences) {
            // Check if the sentence contains "not relevant"
            if (!sentence.contains("not relevant")) {
                result.append(sentence);
            }
        }
        return "Narrative:" + "." + result.toString();
    }

}