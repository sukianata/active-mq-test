
package com.sukianata;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;



/**
 * ClassName: Consumer 
 * @Description: 消息消費者類
 * @author sukianata
 * @date 2018/6/19
 */
public class Consumer {
		
	//默認用戶名
	private static final String USERNAME=ActiveMQConnection.DEFAULT_USER;
	//默認登錄密碼
	private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
	//鏈接地址
	private static final String BROKEN_URL=ActiveMQConnection.DEFAULT_BROKER_URL;
	
	private transient Connection connection;
	
	private transient Session session;
	
	private static final ConnectionFactory CONNECTION_FACTORY;
	
	static{
		CONNECTION_FACTORY=new ActiveMQConnectionFactory(USERNAME, PASSWORD,BROKEN_URL);
	}
	
	/**
	 * Constructor
	 * @throws JMSException 
	 */
	public Consumer() throws JMSException {
		connection=CONNECTION_FACTORY.createConnection();
		connection.start();
		/**
		 * true 表示開啟事物
		 * 第二個參數表示設置為手動應答模式
		 */
		session=connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
	}
	public void getQueueMessage(String disname) throws JMSException{
		//創建一個消息隊列 disname 為隊列，名稱
		Queue queue = session.createQueue(disname);
		MessageConsumer consumer = null;
		//創建消費者
		consumer=session.createConsumer(queue);
		/*
		 * 另一種寫法如下：
		 * 	Destination destination=session.createQueue(disname);
		 *	MessageConsumer messageConsumer=session.createConsumer(destination);
		 */
		
		while (true) {
			//這裡採用同步應答的模式 會造成阻塞
			TextMessage msg=(TextMessage) consumer.receive();
			if (msg!=null) {
				msg.acknowledge();//確認消息被消費，若不確認，原消息會一直在隊列中，重新啟動消費者時會重複發送。（自己理解：會一直存到消息默認的過期時間）
				System.out.println(Thread.currentThread().getName()+": Consumer:我是消費者，我正在消費>>>>內容是：>>>>>"+msg.getText());
			}else{
				break;
			}
		}
	}
	public void getTopicMessage() throws JMSException{
		
		/*  
		 *  //Topic 和 Queue 是Destination的子類 兩種寫法都行
		 *  Topic topic =session.createTopic("TopicTest");
		 *  MessageConsumer messageConsumer=session.createDurableSubscriber(topic, "client1");
		 *  messageConsumer.setMessageListener(new MyMessageListener());//異步
		*/	
		Destination destination=session.createTopic("TopicTest");//這裡的topic名稱要與生產者保持一致才能接收到消息
		MessageConsumer messageConsumer=session.createConsumer(destination);//區別于上面createDurableSubscriber的形式，這種創建消費者的方法比較通用
		messageConsumer.setMessageListener(new  MyMessageListener());//異步應答，只需實現MessageListener類，并重寫onMessage方法就行
	}
	class MyMessageListener implements MessageListener{

		@Override
		public void onMessage(Message message) {
			TextMessage textMessage=(TextMessage) message;
			try {
				message.acknowledge();
				System.out.println("接收訂閱主題："+textMessage.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
