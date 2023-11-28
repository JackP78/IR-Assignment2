package ie.tcd.ir.grouptwelve;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

public class CustomAnalyzer {
    private PerFieldAnalyzerWrapper analyzer;

    CustomAnalyzer() {
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put("Body", new EnglishAnalyzer());
        analyzerPerField.put("Title", new EnglishAnalyzer());
        analyzerPerField.put("Date", new KeywordAnalyzer());
        analyzerPerField.put("Subject", new KeywordAnalyzer());
        this.analyzer = new PerFieldAnalyzerWrapper(new EnglishAnalyzer(), analyzerPerField);
    }

    public PerFieldAnalyzerWrapper getAnalyzer() {
        return analyzer;
    }
}
