
package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;

import com.sukianata.Producter;


/**
 * ClassName: TestMq 
 * @Description: TODO
 * @author sukianata
 * @date 2018/6/13
 */
public class TestQueueProducter {
	
	public static void main(String[] args) throws JMSException {
		Producter producter=new Producter();
		TestQueueProducter testMq=new TestQueueProducter();
		try {
			Thread.sleep(100);
			ExecutorService service = Executors.newFixedThreadPool(1024);
			for(int i = 1; i <= 10; i++) {
	            service.execute(new Thread(testMq.new ProductorMq(producter)));
	        }
			service.shutdown();
			while(!service.isTerminated()) {}
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
				producter.sendQueueMessage("Test-mq");
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
}
