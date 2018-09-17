package com.phd;


import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.*;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import org.jsoup.*;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.util.*;

public class GoogleScholar {

    private int maxConnections = 999999; //15;

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

    public Item getRecordsByAuthor(String author, String subject)
            throws IOException {
        String url = "http://scholar.google.com/scholar?start=0&num=1&hl=en&as_sdt=0";

        //Build  query
        if (author != null) { //author.length() > 0
            String subj = URLEncoder.encode(subject, "UTF-8");
            //url += "&q=as_subj%3D%22=" + subj; //first 440 docs
            url += "&q=" + subj;
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

    public Item getCitesByUrl(String url) throws IOException {

        Item item = new Item();
        item.url = url;
        item.errorMessage = "";

        Document doc = getDocument(url);
        if (doc == null) {
            System.out.println("document reading error: " + url);
            item.citationCount = -1;
            item.errorMessage = "Document reading error";
            return item;
        }
        System.out.println("DOC: " + doc);
        System.out.println("/n ================");

        Elements elements = doc.select("div.gs_r");

        System.out.println("element size: " + elements.size());
        if (elements.size() == 0) {
            item.citationCount = -1;
            item.errorMessage = "Document elements.size() == 0";
            return item;
        }
        for (Element element : elements) {
            System.out.println(">>>>element: " + element);

            Elements links = element.select(".gs_fl a[href]");
            System.out.println("????links size: " + links.size());


            int countNotFound = 0;
            for (Element link : links) {
                 System.out.println(">>>>>link: " + link);
                 if (link.attr("href").contains("cites=")){
                     System.out.println(">>>>>YES link: " + link.text());
                     item.citationCount = Integer.parseInt(link.text().replace("Cited by ",""));
                     return item;
                 } else {
                    countNotFound ++;
                }

            }
            // if we check every link but found nothing
            if (links.size() == countNotFound) {
                System.out.println(">>>>>NO FOUND countNotFound: " + countNotFound);
                item.citationCount = 0; //no cites
                item.errorMessage = "no cites";
                return item;
            }

        }

        item.citationCount = -1;
        item.errorMessage = "Cannot found cites= element";
        return item;
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

        List<CPProxy> res = ProxyUtil.CPProxyList;
        System.out.println("loading.......RES: \n" + res);

       Integer index = RandomUtil.getInstance().generateRandom(res.size()-1);

//        Random rand = new Random();
//        int index = rand.nextInt(res.size()-1) + 0;
        System.out.println("random CPProxy index:" + index);
        CPProxy cpProxy = res.get(index);


//        if (cpProxy != null) {
//            System.out.println("using CPProxy: \n");
//            System.out.println("address:" + cpProxy.adress);
//            System.out.println("port:" + cpProxy.port);
//
//            System.setProperty("https.proxyHost", cpProxy.adress);
//            System.setProperty("https.proxyPort", cpProxy.port);
//            // System.setProperty("http.defaultConnectTimeout", "10000");
//            // System.setProperty("http.defaultReadTimeout", "10000");
//        }

//         // works
//        Connection conn = Jsoup.connect(url);
//        addHeader(conn);
//        //conn.timeout(1000*10*1);
//        conn.header("Cookie", cookie);
//
//        Document doc = conn.get();


        Connection conn = Jsoup.connect(url).proxy(cpProxy.adress,Integer.valueOf(cpProxy.port));
        addHeader(conn);
        conn.header("Cookie", cookie);
        //Document doc = conn.get();


        Document doc = null;
        try {
            doc = conn.get();
            return doc;
        } catch (Exception e) {
            //log error
            System.out.println("connection error:" + e.getLocalizedMessage());
        }


//CPProxy attemp 2
//        URL urlConn = new URL(url);
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("78.40.136.195", 8080)); // or whatever your CPProxy is
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