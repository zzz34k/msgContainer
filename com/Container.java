package com.sample;

import java.io.IOException;
import java.util.List;


public class Container {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: --groupName --configFile --brokerUrl");
            return;
        }
        
		//消息队列地址:ActiveMQ
        String url = "tcp://172.18.54.142:8616";
        if (args.length > 2) {
            url = args[2];
        }
        Group.groupName = args[0];
        Configuration.configFile = args[1];
        try {
            List<String> queues = Configuration.getAllQueues();
            for (String queue : queues) {
                QueueThread runable = new QueueThread();
                runable.setQueue(queue);
                runable.setUrl(url);
                Thread thread = new Thread(runable);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
