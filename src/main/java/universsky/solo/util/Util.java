/**
 * universsky.solo Util.java 2014年4月22日
 */
package universsky.solo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import universsky.solo.io.FileUtil;

/**
 * @author 东海陈光剑 2014年4月22日 下午5:04:34
 */
public class Util {
	String deviceId;
	private static Logger log = Logger.getLogger(Util.class);

	public Util(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 在程序运行时修改log4j日志文件名
	 */
	static {
		// log.info(log.getAllAppenders());
		// log.info(log.getRootLogger());
		Appender appender = log.getAppender("XFILE");
		// log.info(appender.getName());
		boolean b = appender instanceof FileAppender;
		// log.info(b);
		FileAppender fappender = (FileAppender) appender;
		String logFile = fappender.getFile();
		// log.info(logFile);
		fappender.setFile(Const.runtimeLogSavePath + "/" + Const.timestamp
				+ "$runtime.log");
		// fappender.activateOptions();
		// log.info("------------------");
		logFile = fappender.getFile();
		// log.info(logFile);
		fappender.activateOptions();
	}

	public class MExcuteCmd implements Runnable {

		public void run() {
			System.out.println(deviceId);
			Process process1 = null;
			Process process2 = null;
			String buff = "";
			try {
				String cmd0 = "adb -s " + deviceId + " shell logcat -c";
				Runtime.getRuntime().exec(cmd0);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String cmd1 = "adb -s "
						+ deviceId
						+ " shell am instrument -e perf true -r -w com.taobao.etao.test/android.test.InstrumentationTestRunner";
				process1 = Runtime.getRuntime().exec(cmd1);

				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						process1.getInputStream(), "utf-8"));
				while ((buff = br1.readLine()) != null) {
					if (!buff.isEmpty()) {
						Util.print(buff);
						log.debug(Const.timestamp + "$" + deviceId + "$" + buff);// 记录执行日志
					}
				}
				br1.close();
				// 启动等待时间
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String cmd2 = "adb -s " + deviceId
						+ " shell logcat -d -v threadtime";
				process2 = Runtime.getRuntime().exec(cmd2);
				BufferedReader br2 = new BufferedReader(new InputStreamReader(
						process2.getInputStream(), "utf-8"));
				while ((buff = br2.readLine()) != null) {
					if (!buff.isEmpty() && buff.contains("com.taobao")) {
						Util.print(buff);
						log.debug(Const.timestamp + "$" + deviceId + "$" + buff);// 记录执行日志
					}
				}
				br2.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * @param packageName
	 */
	public static void uninstall(String deviceName, String packageName) {
		Util.print(deviceName + " Uninstalling " + packageName + "...");
		Process process = null;
		String buff = "";
		try {
			/**
			 * adb -s HC34WW907981 uninstall com.taobao.taobao.sword.test
			 */
			process = Runtime.getRuntime().exec(
					"adb -s " + deviceName + " uninstall " + packageName);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "utf-8"));
			while ((buff = br.readLine()) != null) {
				// Util.print(buff);
				// log.debug(buff);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getDeviceList() {
		Process process = null;
		String strBuff = "";
		String t = "";
		String command = "adb devices";

		try {
			process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "utf-8"));
			while ((t = br.readLine()) != null) {
				strBuff += t + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * List of devices attached
		 * \nCoolpad5890-a1a1314a\tdevice\nHC34WW907981\tdevice\n\n
		 */
		// print(strBuff);
		// log.debug(strBuff);

		List<String> deviceList = new ArrayList<String>(5);
		String regex = "[\\n](.*)[\\t]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(strBuff);
		while (matcher.find()) {
			String deviceId = strBuff.substring(matcher.start(), matcher.end());
			deviceId = deviceId.replaceAll("\\s*", "");
			deviceList.add(deviceId);
		}
		return deviceList;
	}

	/**
	 * 获取本次执行截图
	 * 
	 * @param buff
	 */
	// adb -s 4d004077b4369049 shell ls /sdcard/Robotium-Screenshots/
	public static List<String> getImgListOnDevice(String deviceId) {
		Process process = null;
		String command = "adb -s " + deviceId
				+ " shell ls /sdcard/Robotium-Screenshots/";
		List<String> imgList = new ArrayList<String>(20);
		try {
			process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "utf-8"));
			String t;
			while ((t = br.readLine()) != null) {// imgList中不要换行
				// print(t);
				// log.debug(t);
				if (!t.isEmpty())// 去掉空白字符
					imgList.add(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < imgList.size(); i++) {
			if ("\\n".equals(imgList.get(i)))
				imgList.remove(i);
		}
		return imgList;
	}

	public static void print(String buff) {
		System.out.println(buff);
	}

	/*********************
	 * 根据包名来获取此包下所有的类名及其实例
	 * 
	 * @param packagePath
	 * @return
	 **********************/
	public static List<String> getClassInPackage(String packagePath) {
		List<String> classList = new ArrayList(10);
		String packageName = packagePath;
		String packageDirName = packageName.replace(".", "/");
		Enumeration<URL> dirs = null;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			// 迭代此 Enumeration
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				File file = new File(url.getFile());
				// 把此目录下的所有文件列出
				String[] classes = file.list();
				// 循环此数组，并把.class去掉 ".class".length() = 6
				for (String className : classes) {
					className = className.substring(0, className.length() - 6);
					classList.add(className);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classList;
	}

	/**
	 * No enclosing instance of type E is accessible. Must qualify the
	 * allocation with an enclosing instance of type E(e.g. x.new A() where x is
	 * an instance of E). E指代我写的那个内部类。
	 * 根据提示，没有可访问的内部类E的实例，必须分配一个合适的内部类E的实例（如x.new
	 * A()，x必须是E的实例。）看着这句提示，我就纳闷了，我已经用new实例化了这个类，为什么还不行呢。
	 * 
	 * 于是百度谷歌了一下相关资料。原来我写的内部类是动态的，也就是开头以public class开头。而主程序是public static class
	 * main。在Java中，类中的静态方法不能直接调用动态方法。只有将某个内部类修饰为静态类，然后才能够在静态类中调用该类的成员变量与成员方法。
	 * 所以在不做其他变动的情况下，最简单的解决办法是将public class改为public static class.
	 * 
	 * 在这里记下这个问题，也方面遇到同样问题的朋友查阅。
	 * 
	 * @author 东海陈光剑 2014年4月11日 上午11:50:12
	 */
	public class MultiInstall implements Runnable {
		String devName, apkAddress;

		/**
		 * 
		 * @param devName
		 * @param apkAddress
		 */
		public MultiInstall(String devName, String apkAddress) {
			super();
			this.devName = devName;
			this.apkAddress = apkAddress;
		}

		public void run() {
			Util.print(devName + " Installing " + apkAddress + "...");
			Process process = null;
			String buff = "";
			try {

				/**
				 * adb -s HC34WW907981 install
				 * D:/Android/test/taobao_android.sword.signed.apk
				 */
				process = Runtime.getRuntime().exec(
						"adb -s " + devName + " install " + apkAddress);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						process.getInputStream(), "utf-8"));
				while ((buff = br.readLine()) != null) {
					// Util.print(buff);
					// log.debug(buff);
					if (buff.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
						Util.print(devName + "已安装");
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void excuteCmd(String cmd) {
		Process process = null;
		String buff = "";
		try {
			process = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "utf-8"));
			while ((buff = br.readLine()) != null) {
				// Util.print(buff);
				// log.debug(buff);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public class MultiUninstall implements Runnable {
		String deviceName, packageName;

		/**
		 * 
		 * @param deviceName
		 * @param packageName
		 */
		public MultiUninstall(String deviceName, String packageName) {
			super();
			this.deviceName = deviceName;
			this.packageName = packageName;
		}

		public void run() {
			Util.print(deviceName + " Uninstalling " + packageName + "...");
			Process process = null;
			String buff = "";
			try {
				/**
				 * adb -s HC34WW907981 uninstall com.taobao.taobao.sword.test
				 */
				process = Runtime.getRuntime().exec(
						"adb -s " + deviceName + " uninstall " + packageName);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						process.getInputStream(), "utf-8"));
				while ((buff = br.readLine()) != null) {
					Util.print(buff);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// public static void record(IAndroidDriver driver, String deviceId,
	// int imgCount) throws InterruptedException {
	//
	// Thread.sleep(1000);
	// String imgTimeChamp = (new SimpleDateFormat("yyyyMMddhhmmss"))
	// .format(new Date());
	// IAndroidActivity activity = driver.getCurrentActivity();
	// System.out.println(activity.getActivityName() + " | Device ID: "
	// + deviceId + "$" + imgCount);
	// // System.out.println(activity.captureScreen());
	// System.out.println("截图：" + deviceId + activity.getActivityName() + "$"
	// + imgCount);
	// // 图片保存
	// Util.GenerateImage(activity, deviceId, imgCount, imgTimeChamp);
	// // 入库
	// String timestamp = Const.timestamp;
	// String activityName = activity.getActivityName();
	// String imgName = activity.getActivityName() + "$" + imgCount + ".jpeg";
	//
	// /**
	// * 设置RecordDao对象属性
	// */
	// RecordDao dao = new RecordDao();
	// dao.setTimestamp(timestamp);
	// dao.setActivityName(activityName);
	// dao.setDeviceId(deviceId);
	// dao.setImgName(imgName);
	// dao.setImgTimeChamp(imgTimeChamp);
	// Util.insert(dao);
	// // Utils.insert(timestamp, activityName, imgName, deviceId,
	// // imgTimeChamp);
	//
	// }

	/**
	 * 将图片转换成Base64
	 * 
	 * @param imgFilePath
	 * @return
	 */
	public static String GetImageStr(String imgFilePath) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static String date() {
		return (new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date());
	}

}
