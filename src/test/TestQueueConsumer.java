
package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;

import com.sukianata.Consumer;


/**
 * ClassName: TestConsumer 
 * @Description: TODO
 * @author sukianata
 * @date 2018/6/13
 */
public class TestQueueConsumer {
	
	public static void main(String[] args) throws JMSException {
		Consumer consumer=new Consumer();
		TestQueueConsumer testConsumer=new TestQueueConsumer();
		ExecutorService service = Executors.newFixedThreadPool(1024);
		for(int i = 1; i <= 20; i++) {
            service.execute(new Thread(testConsumer.new ConsumerMQ(consumer)));
        }
		service.shutdown();
		while(true){
			if (service.isTerminated()) {
				System.out.println("Thread end");
				break;
			}
		}
	}
	private class ConsumerMQ implements Runnable{
		Consumer consumer;
		public ConsumerMQ(Consumer consumer) {
			this.consumer=consumer;
		}
		@Override
		public void run() {
				try {
					consumer.getQueueMessage("Test-mq");
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
	}
}
