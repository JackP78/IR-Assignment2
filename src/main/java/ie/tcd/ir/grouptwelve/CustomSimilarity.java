package ie.tcd.ir.grouptwelve;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.PerFieldSimilarityWrapper;
import org.apache.lucene.search.similarities.Similarity;

public class CustomSimilarity extends PerFieldSimilarityWrapper {
    @Override
    public Similarity get(String fieldName) {
        if (fieldName.equals("Summary")) {
            return new LMJelinekMercerSimilarity((float) 0.7);
        } else {
            return new BM25Similarity();
        }
    }
}
