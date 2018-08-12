package com.phd;
import java.io.File;
import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("hello");

        DocumentFileReader fr = new DocumentFileReader();

        String base = "/var/data";
        String relative = new File(base).toURI().relativize(new File("input").toURI()).getPath();

        final File folder = new File(relative);
        fr.listFilesForFolder(folder);
    }
}
