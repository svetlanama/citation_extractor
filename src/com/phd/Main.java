package com.phd;
import java.io.File;
import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException {
	// write your code here
        System.out.println("hello");

        DocumentFileReader fr = new DocumentFileReader();

        final File folder = new File("/Users/svitlanamoiseyenko/REPOS/citation_extractor/data");
        fr.listFilesForFolder(folder);
    }
}
