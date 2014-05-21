/**
 * universsky.solo FileUtil.java 2014年4月25日
 */
package universsky.solo.io;

import java.io.BufferedReader;
/**
 * @author 东海陈光剑
 * 2014年4月25日 上午10:21:50
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
	 * 向txt文本中写入内容
	 * 
	 * @param status
	 *            区分是日志还是目标的标识
	 * @param date
	 * @param content
	 *            写入的具体内容
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
	 * 读取指定文件中到内容到String中
	 * 
	 * @param file
	 *            要读取内容的文件
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
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 */

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
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
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}
