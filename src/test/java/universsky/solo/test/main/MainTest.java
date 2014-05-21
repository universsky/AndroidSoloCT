/**
 * universsky.solo Main.java 2014年4月22日
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
 * @author 东海陈光剑 2014年4月22日 下午5:04:58
 */
public class MainTest {
	private static final Logger log = Logger.getLogger(MainTest.class);

	@Test
	public void testMain() throws InterruptedException {
		Init.init(); // 卸载安装apks 清空截图目录 清空logcat日志记录
		long start = System.currentTimeMillis();
		letsGo();
		long end = System.currentTimeMillis();
		/**
		 * 测试报告处理生成
		 */
		// 上传每个deviceId的imglist, logcat
		for (String deviceId : Util.getDeviceList()) {
			Record.record(deviceId);
		}
		String sec = (end - start) / 1000 + "";
		String min = (end - start) / 1000 / 60 + "";
		// 发送邮件组
		MailSendSSL.sendMailSSL(Const.timestamp, min, sec, Const.toNames);
		// 上传本次运行记录run_stamp$log4j.log
		// 如何在程序运行时修改log4j日志文件名?
		Record.recordLog4jAndPerfLog();
		String cmd = "cmd  /c  start  http://10.125.1.58:88/report.html?run_stamp="
				+ Const.timestamp + "?min=" + min + "?sec=" + sec;
		System.out.println(cmd);
		Util.excuteCmd(cmd);// & 符号 DOS不支持,改用 ? 分隔符
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Init.init(); // 卸载安装apks 清空截图目录 清空logcat日志记录
		long start = System.currentTimeMillis();
		letsGo();
		long end = System.currentTimeMillis();
		/**
		 * 测试报告处理生成
		 */
		// 上传每个deviceId的logcat
		for (String deviceId : Util.getDeviceList()) {
			Record.record(deviceId);
		}
		String sec = (end - start) / 1000 + "";
		String min = (end - start) / 1000 / 60 + "";
		// 发送邮件组
		MailSendSSL.sendMailSSL(Const.timestamp, min, sec, Const.toNames);
		// 上传本次运行记录run_stamp$log4j.log
		// 如何在程序运行时修改log4j日志文件名?
		Record.recordLog4jAndPerfLog(); // 這地方是不讓改的?log4j正在被鎖定?
		String cmd = "cmd  /c  start  http://10.125.1.58:88/report.html?run_stamp="
				+ Const.timestamp + "?min=" + min + "?sec=" + sec;
		System.out.println(cmd);
		Util.excuteCmd(cmd);// & 符号 DOS不支持,改用 ? 分隔符
	}

	private static void letsGo() {
		List<String> deviceList = Util.getDeviceList();
		List<Thread> threadList = new ArrayList<Thread>(deviceList.size());
		// Thread列表
		for (String deviceId : deviceList) {
			Util u = new Util(deviceId);
			threadList.add(new Thread(u.new MExcuteCmd()));
		}
		for (String deviceId : deviceList) {
			Thread perf = new Thread(new PerfUtil(deviceId));
			perf.setPriority(Thread.MIN_PRIORITY);
			threadList.add(perf);
		}
		// start 启动线程
		for (Thread thread : threadList) {
			thread.start();
			log.debug(thread);
		}

		// join等待所有子线程执行完
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
