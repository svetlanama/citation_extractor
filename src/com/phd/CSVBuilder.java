package com.phd;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CSVBuilder {

   FileWriter writer = null;

    public void craeteCSV() throws IOException {
        writer = new FileWriter("/Users/svitlanamoiseyenko/REPOS/citation_extractor/citation.csv");
        CSVUtils.writeLine(writer, Arrays.asList("Total Citation", "No Citations", "paper file name", "author", "subject", "createdDate"));
//        writer.flush();
//        writer.close();
    }
   public void buildCSV(Integer totalCitation, String filename, String author, String subject, Integer createdDate) throws IOException {


        Integer noCitations = (totalCitation / (2018 - createdDate - 1));
        CSVUtils.writeLine(writer, Arrays.asList(totalCitation.toString(), noCitations.toString(), filename, author, subject, createdDate.toString()),',', '"');

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
