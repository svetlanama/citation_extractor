package com.phd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;


public class DocumentFileReader {

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            System.out.println("fileEntry: " + fileEntry.getName());
            //TODO:  remove all DSSTORE and check for extension

            if (!fileEntry.getName().equals(".DS_Store")) {

                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    System.out.println(fileEntry.getName());
                    readFile(fileEntry);
                }
            }
        }
    }

    private void readFile(File inFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {

            String sCurrentLine;
            int i = 0;
            String subject = "";
            String author = "";
            while (((sCurrentLine = br.readLine()) != null) && (i<2))  {
                //System.out.println(sCurrentLine);
                System.out.println("i: " + i);
                if (i == 0) {
                    subject = sCurrentLine;
                } else {
                    author = sCurrentLine;
                }

                i++;
            }
           performSearchInGoogleScholar(author, subject);
           //performSearchInGoogleScholar("Doug Josephson", "The good, the bad, and the ugly of silicon debug");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performSearchInGoogleScholar(String author, String subject) throws IOException {
       GoogleScholar googleScholar = new GoogleScholar();
       String cites = googleScholar.getRecordsByAuthor(author, subject);
       System.out.println("\n\n Author: " + author + "\n Subject: " + subject  + "\n Cites: " + cites);

    }

}
//https://github.com/mmaroti/shared/blob/master/org/reflocator/cites4/GoogleScholar.java
//https://scholar.google.com.ua/scholar?hl=ru&as_sdt=0%2C5&q=The+Good%2C+the+Bad%2C+and+the+Ugly+of+Silicon+Debug+&btnG=