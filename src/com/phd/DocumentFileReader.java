package com.phd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;


public class DocumentFileReader {



    public void listFilesForFolder(final File folder) throws IOException, InterruptedException {


        for (final File fileEntry : folder.listFiles()) {
            System.out.println("fileEntry: " + fileEntry.getName());
            //TODO:  remove all DSSTORE and check for extension

            if (!fileEntry.getName().equals(".DS_Store")) {

                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    System.out.println(fileEntry.getName());

                    Random rand = new Random();
                    int sec = rand.nextInt(50000) + 30000;
                    Thread.sleep(sec);

                    readPdfFile(fileEntry);
                }
            }
        }

        TaskManager.getInstance().stop();
    }

    private void readPdfFile(File inFile) throws IOException {
        PDDocument document = PDDocument.load(inFile);

        if (!document.isEncrypted()) {
            PDDocumentInformation info = document.getDocumentInformation();
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDMetadata metadata = catalog.getMetadata();

            System.out.println( "\n ======Info ======= \n" + metadata + "\n ============ \n");
            System.out.println( "\n Page Count= " + document.getNumberOfPages() );
            System.out.println( "Title= " + info.getTitle() );
            System.out.println( "Author= " + info.getAuthor() );
            System.out.println( "Subject=" + info.getSubject() );
            System.out.println( "Keywords=" + info.getKeywords() );
            System.out.println( "Creator=" + info.getCreator() );
            System.out.println( "CreationDate=" + info.getCreationDate().getWeekYear());
            System.out.println( "getMetadataKeys=" + info.getMetadataKeys() );

            String author = info.getAuthor();
            String subject = info.getTitle().trim();
            Integer createdDate = info.getCreationDate().getWeekYear();

            //Just to remove xome info before title
            if(subject.contains(": "))  {
                subject = subject.split(": ")[1];
            }

            if (subject.length() > 0){
                performSearchInGoogleScholar(inFile.getName(), author, subject, createdDate);
            }
        }
        document.close();
    }

    private void performSearchInGoogleScholar(String filename, String author, String subject, Integer createdDate) throws IOException {
       GoogleScholar googleScholar = new GoogleScholar();
       System.out.println("\n\n Author: " + author + "\n Subject: " + subject);


       Item item = googleScholar.getRecordsByAuthor(author, subject);
       System.out.println("\n\n Author: " + author + "\n Subject: " + subject  + "\n Cites: " + item.citationCount);

       // String cites = "10";
        String authors = author.replace(" "," ");
        System.out.println("authors: " + authors);
        //Integer totalCitation = Integer.parseInt(cites);


        // If everything is fine add to csv and move file to done
        if(item.citationCount > -1) {
            CSVBuilder.getInstance().buildCSV(item, filename, authors, subject, createdDate);
            FileUtil.moveToDone(filename);
        }
    }

}

//https://scholar.google.com.ua/scholar?hl=ru&as_sdt=0%2C5&q=The+Good%2C+the+Bad%2C+and+the+Ugly+of+Silicon+Debug+&btnG=
