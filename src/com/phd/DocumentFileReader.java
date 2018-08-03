package com.phd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class DocumentFileReader {

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
                readFile(fileEntry);
            }
        }
    }

    private void readFile(File inFile) {
//        File inFile = new File("inputText.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {

            String sCurrentLine;
            int i = 0;
            while (((sCurrentLine = br.readLine()) != null) && (i<2))  {
                System.out.println(sCurrentLine);
                i++;
            }
            performSearchInGoogleScholar("The Good, the Bad, and the Ugly of Silicon Debug ", "Doug Josephson ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performSearchInGoogleScholar(String name, String author) {


    }

}
//https://github.com/mmaroti/shared/blob/master/org/reflocator/cites4/GoogleScholar.java
//https://scholar.google.com.ua/scholar?hl=ru&as_sdt=0%2C5&q=The+Good%2C+the+Bad%2C+and+the+Ugly+of+Silicon+Debug+&btnG=