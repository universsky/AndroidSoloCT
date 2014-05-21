/**
 * etao.test LThread.java 2014年4月19日
 */
package universsky.solo.threadpool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 东海陈光剑 2014年4月19日 上午2:14:12 线程池: 等待所有子线程全部执行完毕的机制实现
 */
public class LThread extends Thread {
    private static List<Thread> runningThreads = new ArrayList<Thread>(5);
    Runnable t;

    public LThread() {
    }

    public LThread(Runnable t) {
	this.t = t;
    }

    @Override
    public void run() {
	regist(this);// 线程开始时注册
	System.out.println(Thread.currentThread().getName() + "开始...");// 打印开始标记
	// run target
	if (t != null) {
	    t.run();
	}
	unRegist(this);// 线程结束时取消注册
	System.out.println(Thread.currentThread().getName() + "结束.");// 打印结束标记
    }

    public void regist(Thread t) {
	synchronized (runningThreads) {
	    runningThreads.add(t);
	}
    }

    public void unRegist(Thread t) {
	synchronized (runningThreads) {
	    runningThreads.remove(t);
	}
    }

    public static boolean hasThreadRunning() {
	return (runningThreads.size() > 0);// 通过判断runningThreads是否为空就能知道是否还有线程未执行完
    }
}
