package com.phd;

import com.sun.prism.impl.Disposer;
import com.sun.tools.javac.util.Name;


import java.io.IOException;

import java.net.URLEncoder;
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

    private static Pattern authorPattern = Pattern.compile("[a-zA-Z ]*");

    public String getRecordsByAuthor(String author, String subject)
            throws IOException {
        String url = "http://scholar.google.com/scholar?start=0&num=1&hl=en&as_sdt=0";

//        Matcher matcher = authorPattern.matcher(author);
//        if (!matcher.matches())
//            throw new IllegalArgumentException("Illegal author name");

        String subj = URLEncoder.encode(subject, "UTF-8");
        url += "&q=as_subj%3D%22=" + subj;
        System.out.println("subj: " + subj);

        String aut = URLEncoder.encode(author, "UTF-8");
        url += "%22+author%3D%22" + aut + "%22&btnG=";
        System.out.println("aut: " + aut);

        System.out.println("url: " + url);
        return getCitesByUrl(url);
    }

    public String getCitesByUrl(String url) throws IOException {


        Document doc = getDocument(url);
        //System.out.println(doc);

        Elements elements = doc.select("div.gs_r");

        System.out.println("element size: " + elements.size());
        for (Element element : elements) {
            //System.out.println("element: " + element);

            Elements links = element.select(".gs_fl a[href]");
            //System.out.println("links size: " + links.size());
            for (Element link : links) {
                 if (link.attr("href").contains("cites")){
                     //System.out.println("links: " + link.text());
                     return link.text().replace("Cited by ","");
                 }

            }
        }

        return "";
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
//  https://scholar.google.com/scholar?start=0&num=2&hl=en&as_sdt=0%2C5&q=as_subj%3D%22The+good%2C+the+bad%2C+and+the+ugly+of+silicon+debug%22+author%3A+%22Doug+Josephson%22&btnG=
