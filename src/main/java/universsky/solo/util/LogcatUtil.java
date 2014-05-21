///**
// * 
// */
//package universsky.solo.util;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//import org.apache.log4j.Appender;
//import org.apache.log4j.FileAppender;
//import org.apache.log4j.Logger;
//
//import universsky.solo.io.FileUtil;
//import universsky.solo.perf.PerfUtil;
//
///**
// * @author wb-chenguangjian
// * 
// */
//public class LogcatUtil implements Runnable {
//
//	private static Logger log = Logger.getLogger(LogcatUtil.class);
//	String deviceId;
//
//	public LogcatUtil(String deviceId) {
//		super();
//		this.deviceId = deviceId;
//	}
//
//	public static void main(String[] args) {
//		new LogcatUtil(Util.getDeviceList().get(0)).logCat();
//	}
//
//	/**
//	 * ����deviceId��logcat��־
//	 * 
//	 * @param deviceId
//	 */
//	public void logCatClean() {
//		// String logcatClean =
//		// "adb -s 096b3760 logcat -c | findstr com.taobao.etao";
//		try {
//			ArrayList<String> cmd = new ArrayList<String>();
//			cmd.add("adb");
//			cmd.add("-s");
//			cmd.add(deviceId);
//			cmd.add("logcat");
//			cmd.add("-c");
//			log.debug(cmd.toArray(new String[cmd.size()]));
//
//			Process process = Runtime.getRuntime().exec(
//					cmd.toArray(new String[cmd.size()]));
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(process.getInputStream()), 1024);
//			String line;
//			while ((line = bufferedReader.readLine()) != null) {
//				log.debug("runtime" + line + "\n");
//				print(line + "\n");
//			}
//		} catch (IOException e) {
//		}
//	}
//
//	private static void print(String string) {
//		System.out.println(string);
//
//	}
//
//	/**
//	 * �������deviceId�ϵ�logcat��־,�����浽����
//	 * 
//	 * @param run_stamp
//	 * @param deviceId
//	 * @return
//	 */
//	public String logCat() {
//		/**
//		 * �ڳ�������ʱ�޸�log4j��־�ļ���
//		 */
//
//		synchronized (log) {
//
//			Appender appender = log.getAppender("PERF");
//			boolean b = appender instanceof FileAppender;
//			FileAppender fappender = (FileAppender) appender;
//			String logFile = fappender.getFile();
//			log.debug(logFile);
//			fappender.setFile("./logcat/" + Const.timestamp + "$" + deviceId
//					+ "$logcat.log");
//			logFile = fappender.getFile();
//			log.debug(logFile);
//			fappender.activateOptions();
//			// adb -s 096b3760 logcat | findstr com.taobao.etao
//			StringBuffer sb = new StringBuffer();
//			// String cmd = "adb -s " + deviceId + " logcat ";// | grep
//			// log.debug(cmd); // com.taobao.etao";
//			// adb -s 096b3760 logcat -d -v threadtime -s
//			// ActivityManager,PackageName:I
//
//			/**
//			 * logcat��������ѡ� -s Ĭ�����ù����� -f �������־�ļ� -c �����־ -d ��ȡ��־ -g ��ȡ��־�Ĵ�С -v
//			 * ��ʽ������־��������ĸ�ʽ��ӡ��ʽ�� -v ��ʽ �� brief W/tag ( 876): message process W(
//			 * 876) message (tag) tag W/tag : message thread W( 876:0x37c)
//			 * message raw message time 09-08 05:40:26.729 W/tag ( 876): message
//			 * threadtime 09-08 05:40:26.729 876 892 W tag : message long [
//			 * 09-08 05:40:26.729 876:0x37c W/tag ] message
//			 */
//			try {
//				ArrayList<String> cmd = new ArrayList<String>();
//				cmd.add("adb");
//				cmd.add("-s");
//				cmd.add(deviceId);
//				cmd.add("logcat");
//				cmd.add("-d");
//				cmd.add("-v");
//				cmd.add("threadtime");
//				cmd.add("-s");
//				cmd.add("ActivityManager,PackageName,InputDispatcher,TaoApiRequest:I");
//				cmd.add("dalvikvm,callback,GestureDetector,FirewallPolicy:D");
//				cmd.add("System.err:W");
//				cmd.add("WifiP2pStateTracker:E");
//				// (Verbose,Info,Warn,
//				// Error,Fatal)
//
//				/**
//				 * D dalvikvm W System.err D callback I PackageName W
//				 * ActivityManager E WifiP2pStateTracker I TaoApiRequest
//				 * I/InputDispatcher D/GestureDetector D/FirewallPolicy
//				 */
//				log.debug(cmd.toArray(new String[cmd.size()]));
//
//				Process process = Runtime.getRuntime().exec(
//						cmd.toArray(new String[cmd.size()]));
//				BufferedReader bufferedReader = new BufferedReader(
//						new InputStreamReader(process.getInputStream(), "utf-8"),
//						1024);
//				String line = null;
//				while ((line = bufferedReader.readLine()) != null) {
//					if (!line.isEmpty()) {// ȥ����Ч��Ϣ�Ŀհ���
//						log.debug("logcat$" + Const.timestamp + "$" + deviceId
//								+ "$" + line);
//						sb.append(line + "\n");
//					}
//				}
//
//				bufferedReader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			// // ��logcat��־��deviceIdΪǰ׺����ʽд������ "/" ����Ҫ
//			// File logcatFile = new File(Const.logcatSavePath + "/" +
//			// Const.timestamp
//			// + "$" + deviceId + "$logcat.log");
//			// if (!logcatFile.exists())
//			// logcatFile.mkdirs();
//			// FileUtil.writeToFile(logcatFile, sb.toString());
//
//			// ���ظ���DeviceId��������־,�ϴ�Httpd������
//			return new File("./logcat/" + Const.timestamp + "$" + deviceId
//					+ "$logcat.log").getAbsolutePath();
//		}
//	}
//
//	public void run() {
//		logCat();
//	}
// }
