/**  
 * @Title: Producter.java
 * @Description: TODO
 * @author sukianata
 * @date 2018/6/19
 */
package com.sukianata;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ClassName: Producter 
 * @Description: 消息生產者類
 * @author sukianata
 * @date 2018/6/19
 */
public class Producter {
	
	//默認用戶名
	private static final String USERNAME=ActiveMQConnection.DEFAULT_USER;
	//默認密碼
	private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
	//默認鏈接
	private static final String BROKEN_URL=ActiveMQConnection.DEFAULT_BROKER_URL;
	
	private transient Connection connection;
	
	private transient Session session;
	
	
	//鏈接工廠
	private static final ConnectionFactory CONNECTION_FACTORY;
	
	static{
		CONNECTION_FACTORY=new ActiveMQConnectionFactory(USERNAME, PASSWORD,BROKEN_URL);
	}
	
	/**
	 * Constructor
	 * @throws JMSException 
	 */
	public Producter() throws JMSException {
		connection=CONNECTION_FACTORY.createConnection();
		connection.start();
		/* 創建session會話
		 * 通過參數設置：
		 * 	是否開啟事物
		 *  消息簽收模式(這裡設置為需要消費者需要手動確認消息接收)
		 */
		session=connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
	}
	
	/**
	 * @Description: 通過queue的形式發佈消息
	 * @author sukianata
	 * @date 2018/6/19
	 */
	public void sendQueueMessage(String queueName){
		try {
			Destination destination=session.createQueue(queueName);//消息目的地
			//Queue queue=session.createQueue(queueName);//這種寫法便於區分
			MessageProducer messageProducer=session.createProducer(destination);
			for (int i = 0; i < 1000; i++) {
				int num=1;
				TextMessage msg=session.createTextMessage("線程："+Thread.currentThread().getName()+">>>>productor:10.195.11.158 >>>>>>>正在生產消息，count:"+num);
				System.out.println("線程："+Thread.currentThread().getName()+">>>>productor:10.195.11.158 >>>>>>>正在生產第"+(i+1)+"筆消息，總數:"+num);
				messageProducer.send(msg);
				session.commit();
				num++;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: 發佈消息到Topic
	 * @author sukianata
	 * @date 2018/6/23
	 */
	public void sendTopicMessage() throws JMSException{
		Destination destination=session.createTopic("TopicTest");//消息目的地
		//Topic topic=session.createTopic("TopicTest"); //這種寫法便於區分
		MessageProducer messageProducer=session.createProducer(destination);
		messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
		for (long i = 0; i < 100; i++) {
			int num =1;
			//創建一條消息
			TextMessage msg=session.createTextMessage("線程："+Thread.currentThread().getName()+"正在發佈>>>>時間："+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+">>>>ip:10.195.11.158 >>>>>>>count:"+num);
			System.out.println("線程："+Thread.currentThread().getName()+"正在發佈>>>>時間："+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+">>>>ip:10.195.11.158 >>>>>>>count:"+num);
			messageProducer.send(msg);
			session.commit();
			num++;
		}
	}
}
