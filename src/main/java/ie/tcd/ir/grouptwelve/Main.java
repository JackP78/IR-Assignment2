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
        // index the federal reserver corpus which is in ./corpus/fr94
        Indexer createIndexes = new FederalReserveIndexer(new EnglishAnalyzer(), "./corpus/fr94");
    }
}