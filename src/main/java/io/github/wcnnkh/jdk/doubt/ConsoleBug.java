package io.github.wcnnkh.jdk.doubt;

import java.io.Console;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * jdk版本 1.8.0_181
 * 
 * 我认为的bug,未进行双重校验判断.<br/>
 * 
 * 实际未验证出不安全，可能是因为sun.misc.SharedSecrets.getJavaIOAccess().console()本来就返回的是一个单例吧<br/>
 * 如果真的已经处理那么这里这样写让产生误导,做为jdk应该认真的实现双重校验锁
 * @author shuchaowen
 * @see System#console()
 *
 */
public class ConsoleBug {
	private static Set<Console> consoles = new HashSet<>();
	
	public static void main(String[] args) throws Exception {
		while(true) {
			start(100);
		}
	}
	
	public static void start(int threadSize) throws Exception {
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(threadSize);
		for(int i=0; i<threadSize; i++) {
			Thread thread = new TestRun(startLatch, endLatch);
			thread.setName(ConsoleBug.class.getName() + "#" + i);
			thread.start();
		}
		startLatch.countDown();//开始同步执行
		endLatch.await();//等待全部执行完
		if(consoles.size() != 1) {
			throw new IllegalStateException("console size:" + consoles.size());
		}
	}
	
	private static class TestRun extends Thread{
		private final CountDownLatch startLatch;
		private final CountDownLatch endLatch;
			
		public TestRun(CountDownLatch startLatch, CountDownLatch endLatch) {
			this.startLatch = startLatch;
			this.endLatch = endLatch;
		}
		
		@Override
		public void run() {
			try {
				startLatch.await();//等待一起执行
				Console console = System.console();
				synchronized (consoles) {
					consoles.add(console);
				}
			} catch (Exception e) {
				System.out.println("Error thread：" + getName());
				e.printStackTrace();
			}finally {
				endLatch.countDown();
			}
		}
	}
}
