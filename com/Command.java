package com.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Command {

	//命令根目录
    private String root = "d:/root";
    
    /**
     * 
     * @param root
     */
    public void setRoot(String root) {
        this.root = root;
    }
    
    /**
     * 
     * @param queue
     * @param cmd
     * @param file
     */
    public void execute(String queue, String cmd, String file) {
        Runtime rt = Runtime.getRuntime();
        String output = "d:/pgc/log/" + queue  + "_" + Calendar.getInstance().getTimeInMillis() + ".log";
        try {
            System.out.println(new Date() + "\t" + cmd);
            final Process pr = rt.exec("cmd /c " + cmd + " --file=" + file + " > " + output, null, new File(this.root));
            pr.waitFor ();
            
			//如果使用Kafka收集日志信息可以使用下面的代码
            /*MessageProducer producer = new MessageProducer();
            producer.setTopic(queue.replace("coverage.", ""));
            FileReader fr = new FileReader(new File(output));
            BufferedReader bf = new BufferedReader(fr);
            String lineSting = null;
            int index = 0;
            while((lineSting = bf.readLine()) != null) {
                producer.produce(queue + "_" + (index++), lineSting);
            }
            System.out.println("Message send done");
            producer.close();
            bf.close();
            fr.close();*/
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
