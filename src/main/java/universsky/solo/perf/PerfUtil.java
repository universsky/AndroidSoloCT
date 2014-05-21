package universsky.solo.perf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import universsky.solo.util.Const;
import universsky.solo.util.Util;

/**
 * @author wb-chenguangjian
 * 
 */
public class PerfUtil implements Runnable {
	private static Logger log = Logger.getLogger(PerfUtil.class);
	String deviceId;

	public PerfUtil(String deviceId) {
		super();
		this.deviceId = deviceId;
	}

	static {
		/**
		 * �ڳ�������ʱ�޸�log4j��־�ļ���
		 */
		Appender appender = log.getAppender("PERF");
		boolean b = appender instanceof FileAppender;
		FileAppender fappender = (FileAppender) appender;
		String logFile = fappender.getFile();
		log.debug(logFile);
		fappender.setFile("./perfs/" + Const.timestamp + "$cpu_usage.log");
		logFile = fappender.getFile();
		log.debug(logFile);
		fappender.activateOptions();
	}

	public static void main(String[] args) {
		new PerfUtil(Util.getDeviceList().get(0)).getCpuUsage();
	}

	public synchronized void getCpuUsage() {

		Process process = null;
		String buff = "";
		try {
			String cmd = "adb -s " + deviceId + " shell top -d 2 -n 100";// |
			// pipe
			// grep
			// com.taobao.etao";
			// //
			// ��������2s
			// grep
			// ?
			// root
			// ����ʱ��100s
			// (����ʵ������ʱ��)
			process = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "utf-8"));
			while ((buff = br.readLine()) != null) {
				if (!buff.isEmpty() && buff.contains("com.taobao.etao")) {
					log.debug("cpu_usage$" + Const.timestamp + "$" + deviceId
							+ "$" + buff);
					System.out.println(buff);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		getCpuUsage();
	}

}
