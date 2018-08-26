package com.phd;

import java.io.IOException;
import java.util.List;

public class ProxyUtil {

    static List<CPProxy> CPProxyList;

    public static void generateProxyList(proxyCallback callback) throws IOException, InterruptedException {

        ApiProxy apiProxy = new ApiProxy();
        List<CPProxy> res = apiProxy.getProxyList();
        System.out.println("res: " + res);
        System.out.println("res size: " + res.size());

        CPProxyList = res;
        callback.onSuccess();
    }

}