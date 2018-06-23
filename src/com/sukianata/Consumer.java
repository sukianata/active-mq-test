
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
 * @Description: �������O����
 * @author sukianata
 * @date 2018/6/19
 */
public class Consumer {
		
	//�q�{�Τ�W
	private static final String USERNAME=ActiveMQConnection.DEFAULT_USER;
	//�q�{�n���K�X
	private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
	//�챵�a�}
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
		 * true ��ܶ}�Ҩƪ�
		 * �ĤG�ӰѼƪ�ܳ]�m����������Ҧ�
		 */
		session=connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
	}
	public void getQueueMessage(String disname) throws JMSException{
		//�Ыؤ@�Ӯ������C disname �����C�A�W��
		Queue queue = session.createQueue(disname);
		MessageConsumer consumer = null;
		//�Ыخ��O��
		consumer=session.createConsumer(queue);
		/*
		 * �t�@�ؼg�k�p�U�G
		 * 	Destination destination=session.createQueue(disname);
		 *	MessageConsumer messageConsumer=session.createConsumer(destination);
		 */
		
		while (true) {
			//�o�̱ĥΦP�B�������Ҧ� �|�y������
			TextMessage msg=(TextMessage) consumer.receive();
			if (msg!=null) {
				msg.acknowledge();//�T�{�����Q���O�A�Y���T�{�A������|�@���b���C���A���s�Ұʮ��O�̮ɷ|���Ƶo�e�C�]�ۤv�z�ѡG�|�@���s������q�{���L���ɶ��^
				System.out.println(Thread.currentThread().getName()+": Consumer:�ڬO���O�̡A�ڥ��b���O>>>>���e�O�G>>>>>"+msg.getText());
			}else{
				break;
			}
		}
	}
	public void getTopicMessage() throws JMSException{
		
		/*  
		 *  //Topic �M Queue �ODestination���l�� ��ؼg�k����
		 *  Topic topic =session.createTopic("TopicTest");
		 *  MessageConsumer messageConsumer=session.createDurableSubscriber(topic, "client1");
		 *  messageConsumer.setMessageListener(new MyMessageListener());//���B
		*/	
		Destination destination=session.createTopic("TopicTest");//�o�̪�topic�W�٭n�P�Ͳ��̫O���@�P�~�౵�������
		MessageConsumer messageConsumer=session.createConsumer(destination);//�ϧO�_�W��createDurableSubscriber���Φ��A�o�سЫخ��O�̪���k����q��
		messageConsumer.setMessageListener(new  MyMessageListener());//���B�����A�u�ݹ�{MessageListener���A�}���gonMessage��k�N��
	}
	class MyMessageListener implements MessageListener{

		@Override
		public void onMessage(Message message) {
			TextMessage textMessage=(TextMessage) message;
			try {
				message.acknowledge();
				System.out.println("�����q�\�D�D�G"+textMessage.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
