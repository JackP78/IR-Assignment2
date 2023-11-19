package ie.tcd.ir.grouptwelve;

import org.apache.lucene.analysis.Analyzer;

import java.io.*;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class FederalReserveIndexer extends Indexer {

    public FederalReserveIndexer(Analyzer analyzer, String corpusDirectory) {
        super(analyzer, corpusDirectory);
    }

    @Override
    void processSingleFile(File file, IndexWriter indexWriter) {
        // this method is called for each file in the corpus
        System.out.println("parsing: "+file.getName());
        try {
            // parse the file using Jsoup
            Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");

            // select each DOC element which is each document
            Elements documents = doc.body().select("doc");
            for (Element document : documents) {
                // for each doc in the file create a Lucene document
                org.apache.lucene.document.Document LuceneDocument = new org.apache.lucene.document.Document();

                // select the relevant fields that will be made into Lucene fields
                Elements docNumber = document.select("docno");
                System.out.println("docNumber: " + docNumber.text());
                LuceneDocument.add(new StringField(Indexer.ID,docNumber.text(), Field.Store.YES));

                Elements docTitle = document.select("doctitle");
                System.out.println("doctitle: " + docTitle.text());
                LuceneDocument.add(new TextField(Indexer.TITLE,docTitle.text(), Field.Store.YES));

                Elements summary = document.select("summary");
                System.out.println("summary: " + summary.text());
                LuceneDocument.add(new TextField(Indexer.SUMMARY,summary.text(), Field.Store.YES));

                Elements text = document.select("text");
                System.out.println("text: " + text.text());
                LuceneDocument.add(new TextField(Indexer.BODY,docTitle.text(), Field.Store.YES));

                // add the document to the lucene index
                indexWriter.addDocument(LuceneDocument);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
