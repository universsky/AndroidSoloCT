/**
 * universsky.solo Init.java 2014��4��22��
 */
package universsky.solo.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import universsky.solo.io.FileUtil;
import universsky.solo.report.Record;
import universsky.solo.util.Const;
import universsky.solo.util.Util;
import universsky.solo.util.Util.MultiInstall;
import universsky.solo.util.Util.MultiUninstall;

/**
 * @author �����¹⽣ 2014��4��22�� ����5:35:20
 */
public class Init {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		init();
	}

	public static void init() throws InterruptedException {
		Init i = new Init();

		if (i.apkExists()) {
			initApk();
		}
		cleanSDCard_Logcat_OnMobile_OnLocal();
	}

	/**
	 * ж��,��װ���԰�,�����apk
	 * 
	 * @throws InterruptedException
	 */
	public static void initApk() throws InterruptedException {

		List<String> deviceList = Util.getDeviceList();

		for (String e : deviceList) {
			synchronized (e) {
				Util.print("----------Device Uninstall Information----------- ");
				// ����Ѿ���װ�˾ɵĲ��԰�����ж�ص�
				MultiUninstall r1 = new Util(e).new MultiUninstall(e,
						Const.etaoPackage);
				MultiUninstall r2 = new Util(e).new MultiUninstall(e,
						Const.soloPackage);
				Thread t1 = new Thread(r1);
				Thread t2 = new Thread(r2);
				t1.start();
				t2.start();
				t1.join();
				t2.join();
			}
		}
		for (String e : deviceList) {
			synchronized (e) {
				Util.print("----------Device Install Information----------- ");
				MultiInstall r3 = new Util(e).new MultiInstall(e, Const.etaoApk);
				MultiInstall r4 = new Util(e).new MultiInstall(e, Const.soloApk);
				Thread t3 = new Thread(r3);
				Thread t4 = new Thread(r4);
				t3.start();
				t4.start();
				t3.join();
				t4.join();
			}

		}
	}

	/**
	 * �жϲ��԰�,�����԰��Ƿ�ready
	 * 
	 * @return
	 */
	public boolean apkExists() {
		String suffix_etao = "etao_android.apk";
		String suffix_solo = "solo.apk";
		boolean etao_exists = false;
		boolean solo_exists = false;

		File f = new File(".");
		File[] files = f.listFiles();
		List<String> fileList = new ArrayList<String>(10);
		for (File ff : files) {
			fileList.add(ff.getAbsolutePath());
			System.out.println(ff.getAbsolutePath());
		}
		for (String fullName : fileList) {
			if (fullName.endsWith(suffix_etao))
				etao_exists = true;
			if (fullName.endsWith(suffix_solo))
				solo_exists = true;
		}

		if (etao_exists && solo_exists) {
			System.out.println("Apks ready ... ");
			return true;
		} else if (!etao_exists && solo_exists) {
			System.out
					.println("Apks not ready :  etao_android.apk does not exist!");
		} else if (etao_exists && !solo_exists) {
			System.out
					.println("Apks not ready :  solo_android.apk does not exist!");
		} else if (!etao_exists && !solo_exists) {
			System.out
					.println("Apks not ready :  solo_android.apk, etao_android.apk does not exist!");
		}
		return false;
	}

	public static void cleanSDCard_Logcat_OnMobile_OnLocal() {
		for (String deviceId : Util.getDeviceList()) {
			// ���mobile SDCard��ͼ��¼
			Record.deleteImgsOnSDCard(deviceId);
			// ���Local imgs
			FileUtil.delAllFile(Const.imgSavePath);
			// ���local logs
			FileUtil.delAllFile(Const.runtimeLogSavePath);
			FileUtil.delAllFile(Const.log4jLogSavePath);
			FileUtil.delAllFile(Const.perfsLogSavePath);
		}
	}

}
