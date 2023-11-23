package ie.tcd.ir.grouptwelve;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class Main {

    public static String INDEX_DIRECTORY = "index/";

    public static String RESULTS_DIRECTORY = "results/";

    public static void main(String[] args)

    {
        Analyzer analyzer = new EnglishAnalyzer();
        Similarity similarity = new ClassicSimilarity();

        Indexer fedReserveIndexer = new FederalReserveIndexer(analyzer, "./corpus/fr94");
        Indexer ftIndexer = new FinancialTimesIndexer(analyzer, "./corpus/ft");
        Indexer foriegnBroadcastIndexer = new ForiegnBroadcastInformationServiceIndexer(analyzer,"./corpus/fbis");
        Indexer laTimesIndexer = new LaTimesIndexer(analyzer, "./corpus/latimes");
        // now run the queries
        QueryEngine makeQueries = new QueryEngine(analyzer, similarity, "Standard");
    }
}