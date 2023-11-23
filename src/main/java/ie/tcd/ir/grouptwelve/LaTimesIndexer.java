package ie.tcd.ir.grouptwelve;

import org.apache.lucene.analysis.Analyzer;

import java.io.*;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.apache.lucene.document.TextField;

public class LaTimesIndexer extends Indexer {

    public LaTimesIndexer(Analyzer analyzer, String corpusDir) {
        super(analyzer, corpusDir);
    }

    @Override
    void processSingleFile(File file, IndexWriter indexWriter) {
        // For each file in corpus
        logger.debug("parsing: " + file.getName());
        try {
            // parse the file using Jsoup
            Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");

            // select each DOC element which is each document
            Elements documents = doc.body().select("doc");
            for (Element document : documents) {
                // for each doc in the file create a Lucene document
                org.apache.lucene.document.Document LuceneDocument = new org.apache.lucene.document.Document();

                indexID(document, LuceneDocument, "docno");

                indexOneField(document, LuceneDocument, "headline", Indexer.TITLE);

                indexOneField(document, LuceneDocument, "text", Indexer.BODY);

                // add the document to the lucene index
                indexWriter.addDocument(LuceneDocument);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }
}
