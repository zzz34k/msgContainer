package com.sample;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class Configuration {

    public static String configFile = "";
    
    public static List<String> getAllQueues() throws IOException {
        List<String> queues = new ArrayList<String>();
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(Configuration.configFile));
        pps.load(in);
        Enumeration en = pps.propertyNames(); 
        
        while(en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            queues.add(strKey);
        }
        return queues;
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public static String getValueByKey(String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(Configuration.configFile));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
            
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
