package ie.tcd.ir.grouptwelve;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class Main {

    public static String INDEX_DIRECTORY = "index/";

    public static String RESULTS_DIRECTORY = "results/";

    public static void main(String[] args)

    {
        Analyzer analyzer = new EnglishAnalyzer();

        Similarity bm25Similarity = new BM25Similarity();
        Similarity lmdSimilarity = new LMDirichletSimilarity();
        Similarity similarity = new MultiSimilarity(new Similarity[] { bm25Similarity, lmdSimilarity });

        Indexer fedReserveIndexer = new FederalReserveIndexer(analyzer, "./corpus/fr94");
        Indexer ftIndexer = new FinancialTimesIndexer(analyzer, "./corpus/ft");
        Indexer foriegnBroadcastIndexer = new ForiegnBroadcastInformationServiceIndexer(analyzer, "./corpus/fbis");
        Indexer laTimesIndexer = new LaTimesIndexer(analyzer, "./corpus/latimes");
        System.out.println("Done.");
        System.out.println("Querying ...");
        // now run the queries
        QueryEngine makeQueries = new QueryEngine(analyzer, similarity, "Standard");
        System.out.println("Done.");
    }
}