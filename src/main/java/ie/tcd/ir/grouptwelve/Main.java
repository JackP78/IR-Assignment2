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

    public static void main( String[] args )

    {
        Analyzer analyzer= new EnglishAnalyzer();
        Similarity similarity = new ClassicSimilarity();

        // index the federal reserver corpus which is in ./corpus/fr94
        Indexer fedReserveIndexer = new FederalReserveIndexer(new EnglishAnalyzer(), "./corpus/fr94");
        // index the financial times corpus which is in ./corpus/ft
        Indexer ftIndexer = new FinancialTimesIndexer(new EnglishAnalyzer(), "./corpus/ft");
        // index the federal reserver corpus which is in ./corpus/fr94
        Indexer foriegnBroadcastIndexer = new ForiegnBroadcastInformationServiceIndexer(new EnglishAnalyzer(), "./corpus/fbis");

        // now run the queries
        QueryEngine makeQueries = new QueryEngine(analyzer, similarity, "Standard");
    }
}