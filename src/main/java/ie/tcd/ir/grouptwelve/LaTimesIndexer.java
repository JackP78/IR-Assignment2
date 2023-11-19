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

public class LaTimesIndexer extends Indexer {

    public LaTimesIndexer(Analyzer analyzer, String corpusDir) {
        super(analyzer, corpusDir);
    }

    @Override
    void processSingleFile(File file, IndexWriter indexWriter) {
        // For each file in corpus
        System.out.println("parsing: " + file.getName());
        try {
            // parse the file using Jsoup
            Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");

            // select each DOC element which is each document
            Elements documents = doc.body().select("doc");
            for (Element document : documents) {
                // for each doc in the file create a Lucene document
                org.apache.lucene.document.Document LuceneDocument = new org.apache.lucene.document.Document();

                // Doc Number field
                Elements docNumber = document.select("docno");
                System.out.println("docNumber: " + docNumber.text());
                LuceneDocument.add(new StringField(Indexer.ID, docNumber.text(), Field.Store.YES));

                // Doc Headline (i.e. title) field
                Elements headline = document.select("headline");
                System.out.println("doctitle (headline): " + headline.text());
                LuceneDocument.add(new StringField(Indexer.TITLE, headline.text(), Field.Store.YES));

                // Doc main text field
                Elements text = document.select("text");
                System.out.println("text: " + text.text());
                LuceneDocument.add(new StringField(Indexer.BODY, text.text(), Field.Store.YES));

                // add the document to the lucene index
                indexWriter.addDocument(LuceneDocument);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
