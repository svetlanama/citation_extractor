package com.phd;

import com.sun.prism.impl.Disposer;
import com.sun.tools.javac.util.Name;


import java.io.IOException;

import java.net.*;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.*;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.net.Proxy;

import java.io.*;
import java.util.*;
import java.util.prefs.*;
import java.util.regex.*;

public class GoogleScholar {

    private int maxConnections = 999; //15;

    public static class TooManyConnectionsException extends IOException {
        private static final long serialVersionUID = 51944780478954114L;

        TooManyConnectionsException(String message) {
            super(message);
        }
    }

    private static void addHeader(Connection conn) {
        conn.header("User-Agent", "Mozilla");
        //conn.header("User-Agent", "Safari");
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



        //Build  query
        if (author.length() > 0) {
            String subj = URLEncoder.encode(subject, "UTF-8");
            url += "&q=as_subj%3D%22=" + subj;
            System.out.println("subj: " + subj);

            String aut = URLEncoder.encode(author, "UTF-8");
            url += "%22+author%3D%22" + aut + "%22&btnG=";
        } else {
            String subj = URLEncoder.encode(subject, "UTF-8");
            url += "&q=" + subj;

            url += "%22&btnG=";
        }


        System.out.println("url: " + url);
        //return url;
        return getCitesByUrl(url);
    }

    public String getCitesByUrl(String url) throws IOException {


        Document doc = getDocument(url);
        System.out.println("DOC: " + doc);
        System.out.println("/n ================");

        Elements elements = doc.select("div.gs_r");

        System.out.println("element size: " + elements.size());
        if (elements.size() == 0) {
            return "-1";
        }
        for (Element element : elements) {
            //System.out.println("element: " + element);

            Elements links = element.select(".gs_fl a[href]");
            //System.out.println("links size: " + links.size());
            for (Element link : links) {
                 if (link.attr("href").contains("cites=")){
                     //System.out.println("links: " + link.text());
                     return link.text().replace("Cited by ","");
                 }

            }
        }

        return "-1";
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

//        System.setProperty("http.proxyHost", "192.168.5.1");
//        System.setProperty("http.proxyPort", "1080");
//        Connection conn = Jsoup.connect(url)

         // works
        Connection conn = Jsoup.connect(url);
        addHeader(conn);
        conn.header("Cookie", cookie);
        Document doc = conn.get();


        //additional attempt 0 - not working
//        Connection conn = Jsoup.connect(url)
//                .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2") //
//                .header("Content-Language", "en-US");
//        Document doc = conn.get();


        //Proxy attemp 1

        /*

        System.setProperty("http.proxyHost", "127.0.0.1");

        //set HTTP proxy port to 3128
        System.setProperty("http.proxyPort", "3128");

        Document doc = Jsoup //
                .connect(url) //
                .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2") //
                .header("Content-Language", "en-US") //
                .get();

*/

//Proxy attemp 2
//        URL urlConn = new URL(url);
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080)); // or whatever your proxy is
//        HttpURLConnection uc = (HttpURLConnection)urlConn.openConnection(proxy);
//
//        uc.connect();
//
//        String line = null;
//        StringBuffer tmp = new StringBuffer();
//        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
//        while ((line = in.readLine()) != null) {
//            tmp.append(line);
//        }
//
//        Document doc = Jsoup.parse(String.valueOf(tmp));

        return doc;
    }


}
//  https://scholar.google.com/scholar?start=0&num=2&hl=en&as_sdt=0%2C5&q=as_subj%3D%22The+good%2C+the+bad%2C+and+the+ugly+of+silicon+debug%22+author%3A+%22Doug+Josephson%22&btnG=

//no author
//https://scholar.google.com.ua/scholar?hl=ru&as_sdt=0%2C5&q=Is+Statistical+Timing+Statistically+Significant%3F&btnG=


//error
//https://scholar.google.com/scholar?start=0&num=1&hl=en&as_sdt=0&q=as_subj%3D%22=Panel%3A+Is+Statistical+Timing+Statistically+Significant%3F%22&btnG=