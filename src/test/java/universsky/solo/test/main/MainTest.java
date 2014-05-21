/**
 * universsky.solo Main.java 2014��4��22��
 */
package universsky.solo.test.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.junit.Test;

import universsky.solo.main.Init;
import universsky.solo.perf.PerfUtil;
import universsky.solo.report.Record;
import universsky.solo.sendmail.MailSendSSL;
import universsky.solo.util.Const;
import universsky.solo.util.Util;

/**
 * @author �����¹⽣ 2014��4��22�� ����5:04:58
 */
public class MainTest {
	private static final Logger log = Logger.getLogger(MainTest.class);

	@Test
	public void testMain() throws InterruptedException {
		Init.init(); // ж�ذ�װapks ��ս�ͼĿ¼ ���logcat��־��¼
		long start = System.currentTimeMillis();
		letsGo();
		long end = System.currentTimeMillis();
		/**
		 * ���Ա��洦������
		 */
		// �ϴ�ÿ��deviceId��imglist, logcat
		for (String deviceId : Util.getDeviceList()) {
			Record.record(deviceId);
		}
		String sec = (end - start) / 1000 + "";
		String min = (end - start) / 1000 / 60 + "";
		// �����ʼ���
		MailSendSSL.sendMailSSL(Const.timestamp, min, sec, Const.toNames);
		// �ϴ��������м�¼run_stamp$log4j.log
		// ����ڳ�������ʱ�޸�log4j��־�ļ���?
		Record.recordLog4jAndPerfLog();
		String cmd = "cmd  /c  start  http://10.125.1.58:88/report.html?run_stamp="
				+ Const.timestamp + "?min=" + min + "?sec=" + sec;
		System.out.println(cmd);
		Util.excuteCmd(cmd);// & ���� DOS��֧��,���� ? �ָ���
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Init.init(); // ж�ذ�װapks ��ս�ͼĿ¼ ���logcat��־��¼
		long start = System.currentTimeMillis();
		letsGo();
		long end = System.currentTimeMillis();
		/**
		 * ���Ա��洦������
		 */
		// �ϴ�ÿ��deviceId��logcat
		for (String deviceId : Util.getDeviceList()) {
			Record.record(deviceId);
		}
		String sec = (end - start) / 1000 + "";
		String min = (end - start) / 1000 / 60 + "";
		// �����ʼ���
		MailSendSSL.sendMailSSL(Const.timestamp, min, sec, Const.toNames);
		// �ϴ��������м�¼run_stamp$log4j.log
		// ����ڳ�������ʱ�޸�log4j��־�ļ���?
		Record.recordLog4jAndPerfLog(); // �@�ط��ǲ�׌�ĵ�?log4j���ڱ��i��?
		String cmd = "cmd  /c  start  http://10.125.1.58:88/report.html?run_stamp="
				+ Const.timestamp + "?min=" + min + "?sec=" + sec;
		System.out.println(cmd);
		Util.excuteCmd(cmd);// & ���� DOS��֧��,���� ? �ָ���
	}

	private static void letsGo() {
		List<String> deviceList = Util.getDeviceList();
		List<Thread> threadList = new ArrayList<Thread>(deviceList.size());
		// Thread�б�
		for (String deviceId : deviceList) {
			Util u = new Util(deviceId);
			threadList.add(new Thread(u.new MExcuteCmd()));
		}
		for (String deviceId : deviceList) {
			Thread perf = new Thread(new PerfUtil(deviceId));
			perf.setPriority(Thread.MIN_PRIORITY);
			threadList.add(perf);
		}
		// start �����߳�
		for (Thread thread : threadList) {
			thread.start();
			log.debug(thread);
		}

		// join�ȴ��������߳�ִ����
		for (Thread thread : threadList) {
			try {
				thread.join();
				log.debug(thread);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
