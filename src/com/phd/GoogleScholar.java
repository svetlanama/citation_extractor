package com.phd;

import com.sun.prism.impl.Disposer;
import com.sun.tools.javac.util.Name;


import java.io.IOException;

import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.*;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.util.*;
import java.util.prefs.*;
import java.util.regex.*;

public class GoogleScholar {

    private int maxConnections = 15;

    public static class TooManyConnectionsException extends IOException {
        private static final long serialVersionUID = 51944780478954114L;

        TooManyConnectionsException(String message) {
            super(message);
        }
    }

    private static void addHeader(Connection conn) {
        conn.header("User-Agent", "Mozilla");
        conn.header("Accept", "text/html,text/plain");
        conn.header("Accept-Language", "en-us,en");
        conn.header("Accept-Encoding", "gzip");
        conn.header("Accept-Charset", "utf-8");
    }

    public void clearCookies() {
        Preferences pref = Preferences.userRoot().node(
                GoogleScholar.class.getName());
        pref.remove("cookie");
    }

    private static Pattern citeidPattern = Pattern
            .compile("/scholar\\?cites=([\\d]*)\\&");
    private static Pattern infoidPattern = Pattern.compile("info:([\\w-]*):");
    private static Pattern britidPattern = Pattern
            .compile("direct.bl.uk/research/([0-9/A-Z]*)\\.html");
    private static Pattern doiPattern = Pattern.compile("id=doi:([^&]*)");
    private static Pattern yearPattern = Pattern
            .compile(" ([12][0-9][0-9][0-9])( |$)");
    private static Pattern authorPattern = Pattern.compile("[a-zA-Z ]*");

    public void getRecordsByAuthor(String author, String subject)
            throws IOException {
        String url = "http://scholar.google.com/scholar?start=0&num=100&hl=en&as_sdt=1,5";

        Matcher matcher = authorPattern.matcher(author);
        if (!matcher.matches())
            throw new IllegalArgumentException("Illegal author name");

        url += "&q=author:%22" + author.replace(' ', '+') + "%22";

        String subj = subject.replace(' ', '+');

        if (subj != null && subj.length() > 0)
            url += "&as_subj=" + subj;

        getRecordsByUrl(url);
    }

    public void getRecordsByUrl(String url) throws IOException {


        Document doc = getDocument(url);
        System.out.println(doc);

        // TODO: make sure that the CD counter is incremented properly in the
        // url list
        //return records;
    }

    public Document getDocument(String url) throws IOException {
        if (--maxConnections <= 0)
            throw new TooManyConnectionsException(
                    "Too many Google Scholar HTML requests");

        Preferences pref = Preferences.userRoot().node(
                GoogleScholar.class.getName());

        String cookie = pref.get("cookie", "");
        if (!cookie.contains("GSP")) {
            Connection conn = Jsoup
                    .connect("http://scholar.google.com/scholar_ncr");
            addHeader(conn);

            conn.get();

            Response resp = conn.response();
            cookie = "PREF=" + resp.cookie("PREF");
            cookie += "; GSP=" + resp.cookie("GSP") + ":CF=4";

            pref.put("cookie", cookie);
        }

        Connection conn = Jsoup.connect(url);
        addHeader(conn);
        conn.header("Cookie", cookie);

        Document doc = conn.get();

        return doc;
    }


}
