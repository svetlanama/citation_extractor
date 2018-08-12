package com.phd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.pdfbox.text.PDFTextStripperByArea;

public class DocumentFileReader {

    CSVBuilder csvBuilder =null;

    public void listFilesForFolder(final File folder) throws IOException {
        csvBuilder = new CSVBuilder();
        csvBuilder.craeteCSV();

        for (final File fileEntry : folder.listFiles()) {
            System.out.println("fileEntry: " + fileEntry.getName());
            //TODO:  remove all DSSTORE and check for extension

            if (!fileEntry.getName().equals(".DS_Store")) {

                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    System.out.println(fileEntry.getName());
                    //readFile(fileEntry);
                    readPdfFile(fileEntry);
                }
            }
        }
        csvBuilder.closeCSV();
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


//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//            System.out.println("Text:" + text);
        }
        document.close();
    }

//    private void readFile(File inFile) {
//        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
//
//            String sCurrentLine;
//            int i = 0;
//            String subject = "";
//            String author = "";
//            while (((sCurrentLine = br.readLine()) != null) && (i<2))  {
//                //System.out.println(sCurrentLine);
//                System.out.println("i: " + i);
//                if (i == 0) {
//                    subject = sCurrentLine;
//                } else {
//                    author = sCurrentLine;
//                }
//
//                i++;
//            }
//           performSearchInGoogleScholar(inFile.getName(), author, subject);
//           //performSearchInGoogleScholar("Doug Josephson", "The good, the bad, and the ugly of silicon debug");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void performSearchInGoogleScholar(String filename, String author, String subject, Integer createdDate) throws IOException {
       GoogleScholar googleScholar = new GoogleScholar();
       System.out.println("\n\n Author: " + author + "\n Subject: " + subject);


       String cites = googleScholar.getRecordsByAuthor(author, subject);
       System.out.println("\n\n Author: " + author + "\n Subject: " + subject  + "\n Cites: " + cites);


        String authors = author.replace(" "," ");
        System.out.println("authors: " + authors);
        Integer totalCitation = Integer.parseInt(cites);
        csvBuilder.buildCSV(totalCitation, filename, authors, subject, createdDate);

    }

}

//https://scholar.google.com.ua/scholar?hl=ru&as_sdt=0%2C5&q=The+Good%2C+the+Bad%2C+and+the+Ugly+of+Silicon+Debug+&btnG=
