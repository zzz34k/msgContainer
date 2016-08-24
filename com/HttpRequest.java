package com.sample;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpRequest {
    
    /**
     * http post
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String,String> params) {
        String host = url;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(host);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params.size()>0) {
            Iterator<String> it = params.keySet().iterator();
            while(it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                nvps.add(new BasicNameValuePair(key,value));
            }
        }
        
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            InputStream in = entity.getContent();
            BufferedInputStream bis = new BufferedInputStream(in);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            StringBuffer sb = new StringBuffer();
            while ((bytesRead = bis.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, bytesRead);
                sb.append(chunk);
            }
            EntityUtils.consume(entity);
            return sb.toString();
        }catch(IOException e){
            System.out.println(e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
