package com.phd;
import java.io.File;

public class Main {



    public static void main(String[] args) {
	// write your code here
        System.out.println("hello");

        DocumentFileReader fr = new DocumentFileReader();

        final File folder = new File("/Users/svitlanamoiseyenko/REPOS/citation_extractor/data");
        fr.listFilesForFolder(folder);
    }
}