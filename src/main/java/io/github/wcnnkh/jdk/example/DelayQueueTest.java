package io.github.wcnnkh.jdk.example;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 错误的使用delay queue造成的cpu过高问题
 * @author shuchaowen
 *
 */
public class DelayQueueTest {
	private static DelayQueue<Task> delayQueue = new DelayQueue<DelayQueueTest.Task>();

	public static void main(String[] args) throws InterruptedException {
		TaskRun run = new TaskRun();
		run.start();

		delayQueue.put(new Task("哈哈"));

		Thread.sleep(Integer.MAX_VALUE);
	}

	public static class TaskRun extends Thread {
		@Override
		public void run() {
			System.out.println("开始执行");
			while (!delayQueue.isEmpty()) {
				System.out.println("在循环呢");
				Object value = delayQueue.poll();
				if (value == null) {
					continue;
				}
				System.out.println(value);
			}
		}
	}

	private static class Task implements Delayed {
		private Object value;

		public Task(Object value) {
			this.value = value;
		}

		@Override
		public int compareTo(Delayed o) {
			return getDelay(TimeUnit.HOURS) > o.getDelay(TimeUnit.HOURS) ? 1 : -1;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return 1;
		}

		@Override
		public String toString() {
			return value.toString();
		}
	}
}
