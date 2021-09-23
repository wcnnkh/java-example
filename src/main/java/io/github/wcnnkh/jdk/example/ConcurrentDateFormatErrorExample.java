package io.github.wcnnkh.jdk.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * SimpleDateFormat是线程不安全的
 * 
 * @author shuchaowen
 *
 */
public class ConcurrentDateFormatErrorExample extends Thread {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static int count = 100;
	private static CountDownLatch countDownLatch = new CountDownLatch(count);

	public static void main(String[] args) {
		for(int i = 0; i<count; i++) {
			new ConcurrentDateFormatErrorExample().start();
			countDownLatch.countDown();
		}
	}

	@Override
	public void run() {
		Date date = new Date();
		date.setTime(new Random().nextLong());
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		String value2 = dateFormat.format(date);
		String print = value + ","  + value2;
		if(value.equals(value2)) {
			System.out.println(print);
		}else {
			System.err.println(print);
		}
	}
}
