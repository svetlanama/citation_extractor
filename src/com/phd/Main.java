package com.phd;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;



public class Main {



    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("hello");

        DocumentFileReader fr = new DocumentFileReader();

        String base = "/var/data";
        String relative = new File(base).toURI().relativize(new File("input").toURI()).getPath();



       // ProxyUtil poxyUtil = new ProxyUtil();
        ProxyUtil.generateProxyList(new proxyCallback(){
            @Override
            public void onSuccess() throws IOException, InterruptedException {
                // no errors
                System.out.println("Done");
                final File folder = new File(relative);
                fr.listFilesForFolder(folder);

            }

            @Override
            public void onError(String err) {
                // error happen
                System.out.println(err);
            }
        });

       // System.out.println("....poxyUtil: " + ProxyUtil.CPProxyList);





//
//
//        ApiProxy apiProxy = new ApiProxy();
//        List<CPProxy> res = apiProxy.getProxyList();
//        System.out.println("res: " + res);

//        String relativeProxy = new File(base).toURI().relativize(new File("proxy").toURI()).getPath();
//        final File folderProxy = new File(relativeProxy);
        //fr.readProxyList(folderProxy);
    }
}
