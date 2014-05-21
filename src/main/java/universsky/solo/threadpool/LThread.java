/**
 * etao.test LThread.java 2014��4��19��
 */
package universsky.solo.threadpool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author �����¹⽣ 2014��4��19�� ����2:14:12 �̳߳�: �ȴ��������߳�ȫ��ִ����ϵĻ���ʵ��
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
	regist(this);// �߳̿�ʼʱע��
	System.out.println(Thread.currentThread().getName() + "��ʼ...");// ��ӡ��ʼ���
	// run target
	if (t != null) {
	    t.run();
	}
	unRegist(this);// �߳̽���ʱȡ��ע��
	System.out.println(Thread.currentThread().getName() + "����.");// ��ӡ�������
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
	return (runningThreads.size() > 0);// ͨ���ж�runningThreads�Ƿ�Ϊ�վ���֪���Ƿ����߳�δִ����
    }
}
