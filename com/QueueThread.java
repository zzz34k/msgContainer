package com.sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.util.ByteSequence;

public class QueueThread implements Runnable {
    
	//默认地址
    private String url = "tcp://172.18.54.142:8616";
    
	//默认地址
    //private String notifyUrl = "http://172.18.54.130/pgmgr/index.php?r=api/notify";
    
    private String queue = "";
    
    public void setUrl(String url) {
        this.url = url;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);    
            
            Connection connection = connectionFactory.createConnection();    
            connection.start();  
            
            final Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);    
            Destination destination = session.createQueue(this.queue);  
              
            MessageConsumer consumer = session.createConsumer(destination);  
            
            System.out.println(new Date() + "\t" + this.queue  + "\tWaiting for messages...");
            while(true) {
                Message msg = consumer.receive();
                if (!this.process(msg)) {
                    continue;
                }
            }
            //session.close();    
            //connection.close();  
        }catch(JMSException  e) {
            System.out.println("exception message:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param msg
     * @return
     * @throws JMSException
     */
    private boolean  process(Message msg) throws JMSException {
        if ( msg instanceof  ActiveMQBytesMessage ) {
            ActiveMQBytesMessage message = (ActiveMQBytesMessage) msg;   
            ByteSequence bs = message.getMessage().getContent();  
            String body = new String(bs.getData());  
            
            return this.msgHanlder(body, msg);
        } else if (msg instanceof TextMessage) {
            TextMessage txt = (TextMessage)msg;
            return this.msgHanlder(txt.getText(), msg);
        } else {
            System.out.println(new Date() + "\t" + this.queue  + "\tUnexpected message type: "+msg.getClass());
            return true;
        }
    }
    
    /**
     * 
     * @param body
     * @param msg
     * @return
     * @throws JMSException
     */
    private boolean msgHanlder(String body, Message msg) throws JMSException {
        System.out.println(new Date() + "\t" + this.queue  + "\t" + body);
        if (body != null) {
            Command cmd = new Command();
            String line = Configuration.getValueByKey(this.queue);
            String[] str = line.split(";");
            cmd.setRoot(str[0]);
            
            String file = "D:\\pgc\\param\\" + this.queue + "_" + Calendar.getInstance().getTimeInMillis(); 
            try {
                FileWriter fw = new FileWriter(new File(file));
                fw.write(body);
                fw.close();
                cmd.execute(this.queue, str[1], file);
                msg.acknowledge();
                //Map<String,String> params = new HashMap<String,String>();
                //params.put("group", Group.groupName);
                //params.put("type", this.queue);
                //System.out.println(HttpRequest.post(this.notifyUrl, params));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
