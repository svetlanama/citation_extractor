package com.phd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiProxy {

    public List<CPProxy> getProxyList() {

        List<CPProxy> list = new ArrayList<>();
        try {

            URL url = new URL("http://list.didsoft.com/get?email=vasileyko.alex@gmail.com&pass=unxgk7&pid=httppremium&https=yes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");


            while ((output = br.readLine()) != null) {
                CPProxy CPProxy = new CPProxy();

                CPProxy.adress = output.split(":")[0];
                CPProxy.port = output.split(":")[1].split("#")[0];
               // System.out.println("adress:" + CPProxy.adress);
               // System.out.println("port:" + CPProxy.port);
                list.add(CPProxy);
               // System.out.println(output);
            }

            conn.disconnect();


        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return list;
    }
}



// http://list.didsoft.com/get?email=vasileyko.alex@gmail.com&pass=unxgk7&pid=httppremium&https=yes