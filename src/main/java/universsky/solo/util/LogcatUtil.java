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
//	 * 所有deviceId清logcat日志
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
//	 * 捕获各个deviceId上的logcat日志,并保存到本地
//	 * 
//	 * @param run_stamp
//	 * @param deviceId
//	 * @return
//	 */
//	public String logCat() {
//		/**
//		 * 在程序运行时修改log4j日志文件名
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
//			 * logcat介绍命令选项。 -s 默认设置过滤器 -f 输出到日志文件 -c 清除日志 -d 获取日志 -g 获取日志的大小 -v
//			 * 格式设置日志（见下面的格式打印格式） -v 格式 例 brief W/tag ( 876): message process W(
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
//					if (!line.isEmpty()) {// 去掉无效信息的空白行
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
//			// // 把logcat日志以deviceId为前缀的形式写到本地 "/" 很重要
//			// File logcatFile = new File(Const.logcatSavePath + "/" +
//			// Const.timestamp
//			// + "$" + deviceId + "$logcat.log");
//			// if (!logcatFile.exists())
//			// logcatFile.mkdirs();
//			// FileUtil.writeToFile(logcatFile, sb.toString());
//
//			// 返回各个DeviceId的运行日志,上传Httpd服务器
//			return new File("./logcat/" + Const.timestamp + "$" + deviceId
//					+ "$logcat.log").getAbsolutePath();
//		}
//	}
//
//	public void run() {
//		logCat();
//	}
// }
