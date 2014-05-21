/**
 * universsky.solo FileUtil.java 2014��4��25��
 */
package universsky.solo.io;

import java.io.BufferedReader;
/**
 * @author �����¹⽣
 * 2014��4��25�� ����10:21:50
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {
	public static void main(String args[]) {
		FileUtil t = new FileUtil();
		File f1 = new File("./logs");
		delAllFile(f1.getAbsolutePath());

		File f2 = new File("./imgs");
		delAllFile(f2.getAbsolutePath());
		// delFolder(f.getAbsolutePath());
	}

	/**
	 * ��txt�ı���д������
	 * 
	 * @param status
	 *            ��������־����Ŀ��ı�ʶ
	 * @param date
	 * @param content
	 *            д��ľ�������
	 * @return
	 */
	public static boolean writeToFile(File file, String content) {
		if (file.exists()) {
			file.delete();
		}
		byte[] b = content.getBytes();
		try {
			OutputStream writer = new FileOutputStream(file);
			writer.write(b);
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ȡָ���ļ��е����ݵ�String��
	 * 
	 * @param file
	 *            Ҫ��ȡ���ݵ��ļ�
	 * @return
	 * @throws IOException
	 */
	public static String readFormFile(File file) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader ipsr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(ipsr);
		String str = null;
		StringBuffer sb = new StringBuffer();
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * ɾ���ļ���
	 * 
	 * @param folderPath
	 *            �ļ�����������·��
	 */

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ�����ļ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ��ָ���ļ����������ļ�
	 * 
	 * @param path
	 *            �ļ�����������·��
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
				flag = true;
			}
		}
		return flag;
	}
}
