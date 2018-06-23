package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sukianata.Consumer;
import com.sukianata.Producter;

/**
 * ClassName: TopicProducer 
 * @Description: TODO
 * @author sukianata
 * @date 2018/6/13
 */
public class TestTopic {
	public static void main(String[] args) {
		TestTopic topicTest= new TestTopic();
		try {
			Thread.sleep(100);
			ExecutorService service = Executors.newFixedThreadPool(1024);
			Consumer consumer =new Consumer();
			Producter producter=new Producter();
			
	        service.execute(new Thread(topicTest.new ProductorMq(producter)));
	          
	        service.execute(new Thread(topicTest.new ComsumerMq(consumer)));
	        
			service.shutdown();
			while(true){
				if (service.isTerminated()) {
					System.out.println("Thread end");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class ProductorMq implements Runnable{
		
		Producter producter;
		
		public ProductorMq(Producter producter) {
			this.producter=producter;
		}
		
		@Override
		public void run() {
				try {
					producter.sendTopicMessage();
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}
		
		
	}
	private class ComsumerMq implements Runnable{
		Consumer consumer;
		public ComsumerMq(Consumer consumer) {
			this.consumer=consumer;
		}
		@Override
		public void run() {
				try {
					consumer.getTopicMessage();
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
	}
}
