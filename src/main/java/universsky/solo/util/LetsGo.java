/**
 * LetsGo.java etao.autotest.util etao.test 
 * ����10:20:14
 */
package universsky.solo.util;

import java.util.List;

import universsky.solo.util.Util.MultiInstall;
import universsky.solo.util.Util.MultiUninstall;

/**
 * @author �����¹⽣ 2014��4��11�� ����10:20:14
 */
public class LetsGo {
	/**
	 * ETAO = 1 ; TAOBAO = 2; ALIPAY = 3
	 */
	private final static int FLAG = 1; // ����FLAG

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// genApks();
		LetsGo.letsGo();
	}

	/**
	 * ������Ե�һ����ж�ذ�װ
	 */
	@SuppressWarnings("unused")
	public static void letsGo() {

		List<String> deviceList = Util.getDeviceList();

		Util.print("----------Device Uninstall Information----------- ");
		for (String e : deviceList) {
			// ����Ѿ���װ�˾ɵĲ��԰�����ж�ص�
			uninstall(e, Const.soloPackage);
			uninstall(e, Const.etaoPackage);

		}

		Util.print("----------Device Install Information----------- ");
		for (String e : deviceList) {
			// ��װ���β��԰�

			install(e, Const.ETAO.ApkPath);
			install(e, Const.ETAO.testApkPath);

		}

	}

	private static void install(String e, String apkpath) {
		Util u = new Util(e);
		MultiInstall t = u.new MultiInstall(e, apkpath);
		new Thread(t).start();
	}

	private static void uninstall(String e, String packageName) {
		Util u = new Util(e);
		MultiUninstall t = u.new MultiUninstall(e, packageName);
		new Thread(t).start();
	}

}
