package com.phd;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.Arrays;

public class CSVBuilder {

   FileWriter writer = null;
   String relative = new File("/var/data").toURI().relativize(new File("output/citation_2004.csv").toURI()).getPath();


    public void createCSV() throws IOException {


        writer = new FileWriter(relative);
        CSVUtils.writeLine(writer, Arrays.asList("Total Citation", "No Citations", "paper file name", "author", "subject", "createdDate"));

        writer.flush();
        writer.close();
    }

   public void buildCSV(Integer totalCitation, String filename, String author, String subject, Integer createdDate) throws IOException {



        writer = new FileWriter(relative, true);

        int currentYear = Year.now().getValue();
        System.out.println("\n\n currentYear: " + currentYear);
        double noCitations = ((double)totalCitation / (double)(currentYear - createdDate - 1));
        System.out.println("\n\n num noCitations: " + noCitations);
        String strNoCitations = String.format("%.4f", noCitations);
        System.out.println("\n\n str noCitations: " + strNoCitations);

        CSVUtils.writeLine(writer, Arrays.asList(totalCitation.toString(), strNoCitations, filename, author, subject, createdDate.toString()),',', '"');

        writer.flush();
        writer.close();
    }

    public void closeCSV() throws IOException {
        writer.close();
    }
}
