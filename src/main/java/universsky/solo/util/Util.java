/**
 * universsky.solo Util.java 2014��4��22��
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
 * @author �����¹⽣ 2014��4��22�� ����5:04:34
 */
public class Util {
	String deviceId;
	private static Logger log = Logger.getLogger(Util.class);

	public Util(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * �ڳ�������ʱ�޸�log4j��־�ļ���
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
						log.debug(Const.timestamp + "$" + deviceId + "$" + buff);// ��¼ִ����־
					}
				}
				br1.close();
				// �����ȴ�ʱ��
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
						log.debug(Const.timestamp + "$" + deviceId + "$" + buff);// ��¼ִ����־
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
	 * ��ȡ����ִ�н�ͼ
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
			while ((t = br.readLine()) != null) {// imgList�в�Ҫ����
				// print(t);
				// log.debug(t);
				if (!t.isEmpty())// ȥ���հ��ַ�
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
	 * ���ݰ�������ȡ�˰������е���������ʵ��
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
			// ������ Enumeration
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				File file = new File(url.getFile());
				// �Ѵ�Ŀ¼�µ������ļ��г�
				String[] classes = file.list();
				// ѭ�������飬����.classȥ�� ".class".length() = 6
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
	 * an instance of E). Eָ����д���Ǹ��ڲ��ࡣ
	 * ������ʾ��û�пɷ��ʵ��ڲ���E��ʵ�����������һ�����ʵ��ڲ���E��ʵ������x.new
	 * A()��x������E��ʵ���������������ʾ���Ҿ������ˣ����Ѿ���newʵ����������࣬Ϊʲô�������ء�
	 * 
	 * ���ǰٶȹȸ���һ��������ϡ�ԭ����д���ڲ����Ƕ�̬�ģ�Ҳ���ǿ�ͷ��public class��ͷ������������public static class
	 * main����Java�У����еľ�̬��������ֱ�ӵ��ö�̬������ֻ�н�ĳ���ڲ�������Ϊ��̬�࣬Ȼ����ܹ��ھ�̬���е��ø���ĳ�Ա�������Ա������
	 * �����ڲ��������䶯������£���򵥵Ľ���취�ǽ�public class��Ϊpublic static class.
	 * 
	 * ���������������⣬Ҳ��������ͬ����������Ѳ��ġ�
	 * 
	 * @author �����¹⽣ 2014��4��11�� ����11:50:12
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
						Util.print(devName + "�Ѱ�װ");
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
	// System.out.println("��ͼ��" + deviceId + activity.getActivityName() + "$"
	// + imgCount);
	// // ͼƬ����
	// Util.GenerateImage(activity, deviceId, imgCount, imgTimeChamp);
	// // ���
	// String timestamp = Const.timestamp;
	// String activityName = activity.getActivityName();
	// String imgName = activity.getActivityName() + "$" + imgCount + ".jpeg";
	//
	// /**
	// * ����RecordDao��������
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
	 * ��ͼƬת����Base64
	 * 
	 * @param imgFilePath
	 * @return
	 */
	public static String GetImageStr(String imgFilePath) {
		// ��ͼƬ�ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��
		byte[] data = null;
		// ��ȡͼƬ�ֽ�����
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ���ֽ�����Base64����
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		return encoder.encode(data);// ����Base64��������ֽ������ַ���
	}

	public static String date() {
		return (new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date());
	}

}
