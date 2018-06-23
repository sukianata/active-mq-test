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
 * @Description: �����Ͳ�����
 * @author sukianata
 * @date 2018/6/19
 */
public class Producter {
	
	//�q�{�Τ�W
	private static final String USERNAME=ActiveMQConnection.DEFAULT_USER;
	//�q�{�K�X
	private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
	//�q�{�챵
	private static final String BROKEN_URL=ActiveMQConnection.DEFAULT_BROKER_URL;
	
	private transient Connection connection;
	
	private transient Session session;
	
	
	//�챵�u�t
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
		/* �Ы�session�|��
		 * �q�L�ѼƳ]�m�G
		 * 	�O�_�}�Ҩƪ�
		 *  ����ñ���Ҧ�(�o�̳]�m���ݭn���O�̻ݭn��ʽT�{��������)
		 */
		session=connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
	}
	
	/**
	 * @Description: �q�Lqueue���Φ��o�G����
	 * @author sukianata
	 * @date 2018/6/19
	 */
	public void sendQueueMessage(String queueName){
		try {
			Destination destination=session.createQueue(queueName);//�����ت��a
			//Queue queue=session.createQueue(queueName);//�o�ؼg�k�K��Ϥ�
			MessageProducer messageProducer=session.createProducer(destination);
			for (int i = 0; i < 1000; i++) {
				int num=1;
				TextMessage msg=session.createTextMessage("�u�{�G"+Thread.currentThread().getName()+">>>>productor:10.195.11.158 >>>>>>>���b�Ͳ������Acount:"+num);
				System.out.println("�u�{�G"+Thread.currentThread().getName()+">>>>productor:10.195.11.158 >>>>>>>���b�Ͳ���"+(i+1)+"�������A�`��:"+num);
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
	 * @Description: �o�G������Topic
	 * @author sukianata
	 * @date 2018/6/23
	 */
	public void sendTopicMessage() throws JMSException{
		Destination destination=session.createTopic("TopicTest");//�����ت��a
		//Topic topic=session.createTopic("TopicTest"); //�o�ؼg�k�K��Ϥ�
		MessageProducer messageProducer=session.createProducer(destination);
		messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
		for (long i = 0; i < 100; i++) {
			int num =1;
			//�Ыؤ@������
			TextMessage msg=session.createTextMessage("�u�{�G"+Thread.currentThread().getName()+"���b�o�G>>>>�ɶ��G"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+">>>>ip:10.195.11.158 >>>>>>>count:"+num);
			System.out.println("�u�{�G"+Thread.currentThread().getName()+"���b�o�G>>>>�ɶ��G"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+">>>>ip:10.195.11.158 >>>>>>>count:"+num);
			messageProducer.send(msg);
			session.commit();
			num++;
		}
	}
}
