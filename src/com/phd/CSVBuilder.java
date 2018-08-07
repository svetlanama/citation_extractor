package com.phd;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CSVBuilder {

   FileWriter writer = null;

    public void craeteCSV() throws IOException {
        writer = new FileWriter("/Users/svitlanamoiseyenko/REPOS/citation_extractor/citation.csv");
        CSVUtils.writeLine(writer, Arrays.asList("citation", "author", "subject"));
//        writer.flush();
//        writer.close();
    }
   public void buildCSV(String citation, String author, String subject) throws IOException {

        CSVUtils.writeLine(writer, Arrays.asList(citation, author, subject),',', '"');

        //custom separator + quote
//        CSVUtils.writeLine(writer, Arrays.asList("aaa", "bb,b", "cc,c"), ',', '"');
//
//        //custom separator + quote
//        CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc,c"), '|', '\'');
//
//        //double-quotes
//        CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc\"c"));


//        writer.flush();
//        writer.close();
    }

    public void closeCSV() throws IOException {
        writer.close();
    }
}
