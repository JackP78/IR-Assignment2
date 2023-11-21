package ie.tcd.ir.grouptwelve;
import java.io.*;
import java.util.ArrayList;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

abstract public class Indexer
{

    protected Analyzer analyzer;
    protected File corpusDirectory;

    protected static String ID = "ID";
    protected static String TITLE = "Title";
    protected static String AUTHOR = "Author";
    protected static String DATE = "Date";
    protected static String BODY = "Body";
    protected static String SUMMARY = "Summary";
    protected IndexWriter indexWriter;
    private int filesProcessed = 0;
    protected static int totalfilesProcessed = 0;


    public Indexer(Analyzer analyzer, String corpusDirectory) {
        try {
            this.analyzer = analyzer;
            this.corpusDirectory = new File(corpusDirectory);
            ParseFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void processFiles(File dirPath, IndexWriter indexWriter) {
        File filesList[] = dirPath.listFiles();
        for(File file : filesList) {
            if(file.isFile()) {
                processSingleFile(file, indexWriter);
                filesProcessed++;
                totalfilesProcessed++;
            } else {
                processFiles(file, indexWriter);
            }
        }
    }

    abstract void processSingleFile(File file, IndexWriter indexWriter);

    public void ParseFile() throws IOException {
        System.out.println("Start Processing " + this.corpusDirectory.getAbsolutePath());
        boolean exists = this.corpusDirectory.exists();
        assert (exists == true);

        Directory directory = FSDirectory.open(Paths.get(Main.INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        if (totalfilesProcessed == 0) {
            totalfilesProcessed = 1;
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        indexWriter = new IndexWriter(directory, config);

        if (this.corpusDirectory.isDirectory()) {
            this.processFiles(this.corpusDirectory, indexWriter);
        }
        else {
            this.processSingleFile(corpusDirectory, indexWriter);
        }

        indexWriter.close();
        directory.close();
        System.out.println("Finished processing " + this.filesProcessed + " files in " + this.corpusDirectory.getAbsolutePath());
    }
}
