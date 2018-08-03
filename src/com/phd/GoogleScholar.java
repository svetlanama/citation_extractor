package com.phd;

import com.sun.prism.impl.Disposer;
import com.sun.tools.javac.util.Name;

import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import javax.swing.text.Element;
import java.io.IOException;
import java.sql.Connection;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.jsoup.Connection.Response;

public class GoogleScholar {

    private int maxConnections = 15;

    public static class TooManyConnectionsException extends IOException {
        private static final long serialVersionUID = 51944780478954114L;

        TooManyConnectionsException(String message) {
            super(message);
        }
    }

//    private static void addHeader(Connection conn) {
//        conn.header("User-Agent", "Mozilla");
//        conn.header("Accept", "text/html,text/plain");
//        conn.header("Accept-Language", "en-us,en");
//        conn.header("Accept-Encoding", "gzip");
//        conn.header("Accept-Charset", "utf-8");
//    }

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

        if (subject != null && subject.length() > 0)
            url += "&as_subj=" + subject;

        //  getRecordsByUrl(url);
    }
//
//    public Name.Table getRecordsByUrl(String url) throws IOException {
//        MemoryTable records = new MemoryTable("scholar", new String[] {
//                "title", "url", "year", "doi", "origin", "cites", "citeid",
//                "infoid", "britid" });
//
//        Document doc = getDocument(url);
//
//        outer: for (;;) {
//            Elements elements = doc.select("div.gs_r");
//            for (Element element : elements) {
//                Disposer.Record record = records.createRecord();
//
//                Elements links = element.select(".gs_rt a[href]");
//                if (links.size() >= 2)
//                    throw new IllegalArgumentException(
//                            "Too many article links in scholar record");
//                else if (links.size() == 1) {
//                    record.setValue("url", links.first().attr("href"));
//                    record.setValue("title", links.first().text());
//                } else {
//                    String title = element.select(".gs_rt").text();
//                    if (!title.startsWith("[CITATION]"))
//                        throw new IllegalArgumentException(
//                                "Unexpected title format for scholar record");
//
//                    title = title.substring(10).trim();
//                    record.setValue("title", title);
//                }
//
//                links = element.select("span.gs_a");
//                if (links.size() != 1)
//                    throw new IllegalArgumentException(
//                            "No summary line in scholar record");
//                else {
//                    String origin = links.first().text();
//                    record.setValue("origin", origin);
//
//                    Matcher matcher = yearPattern.matcher(origin);
//                    if (matcher.find())
//                        record.setValue("year", matcher.group(1));
//                }
//
//                record.setValue("cites", "0");
//
//                links = element.select(".gs_fl a[href]");
//                for (Element link : links) {
//                    String text = link.text();
//
//                    if (text.startsWith("Cited by ")) {
//                        Matcher matcher = citeidPattern.matcher(link
//                                .attr("href"));
//                        if (!matcher.find())
//                            throw new IllegalArgumentException(
//                                    "Cites url does not contain the cites field");
//
//                        record.setValue("citeid", matcher.group(1));
//                        record.setValue("cites", text.substring(9));
//                    }
//
//                    else if (text.startsWith("Find it")) {
//                        Matcher matcher = doiPattern.matcher(link.attr("href"));
//                        if (matcher.find())
//                            record.setValue("doi", matcher.group(1));
//                    }
//
//                    else if (text.equals("Import into BibTeX")) {
//                        Matcher matcher = infoidPattern.matcher(link
//                                .attr("href"));
//                        if (!matcher.find())
//                            throw new IllegalArgumentException(
//                                    "BibTex url does not contain the info field");
//
//                        record.setValue("infoid", matcher.group(1));
//                    }
//
//                    else if (text.equals("BL Direct")) {
//                        Matcher matcher = britidPattern.matcher(link
//                                .attr("href"));
//                        if (!matcher.find())
//                            throw new IllegalArgumentException(
//                                    "BL Direct url is not well formatted");
//
//                        record.setValue("britid", matcher.group(1));
//                    }
//                }
//
//                records.addRecord(record);
//            }
//
//            elements = doc.select("div.n a[href]");
//            for (Element element : elements) {
//                String text = element.text();
//
//                if (text.equals("Next")) {
//                    String href = element.attr("href");
//                    if (!href.startsWith("/scholar?start"))
//                        throw new IOException("Unexpected format of next link");
//
//                    href = "http://scholar.google.com" + href;
//                    doc = getDocument(href);
//
//                    continue outer;
//                }
//            }
//
//            // exit if no more Next links
//            break;
//        }
//
//        // TODO: make sure that the CD counter is incremented properly in the
//        // url list
//        return records;
//    }

}
