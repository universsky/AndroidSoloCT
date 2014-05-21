/**
 * universsky.solo Record.java 2014年4月23日
 */
package universsky.solo.report;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import universsky.solo.dao.LogDao;
import universsky.solo.dao.RecordDao;
import universsky.solo.util.Const;
import universsky.solo.util.Util;

/**
 * @author 东海陈光剑 2014年4月23日 上午1:02:26
 */
public class Record {

	/*************************************
	 * recordLog4jAndPerfLog
	 * 
	 ************************************/
	public static void recordLog4jAndPerfLog() {
		FileUpload fu = new FileUpload();
		// 这里的分隔符 / 很重要
		// 這裡的 靜態改變log4j的日誌名字的方法,在運行時是無法做到的
		File runtimelog = new File(Const.runtimeLogSavePath + "/"
				+ Const.timestamp + "$runtime.log");
		File perflog = new File(Const.perfsLogSavePath + "/" + Const.timestamp
				+ "$cpu_usage.log");

		try {
			fu.send(Const.uploadServerUrl, runtimelog.getAbsolutePath());
			fu.send(Const.uploadServerUrl, perflog.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/***********************************************
	 * 截图,记录操作过程
	 * 
	 * @param activity
	 * @return
	 * @throws InterruptedException
	 * 
	 ************************************************/

	public static void record(String deviceId) {
		FileUpload fu = new FileUpload();
		// // logcat.log入库
		// LogDao logDao = new LogDao(Const.timestamp, deviceId);
		// insertLog(logDao);
		// 上传log到服务器
		String logName = Const.runtimeLogSavePath + "/" + Const.timestamp + "$"
				+ deviceId + "$runtime.log";
		try {
			fu.send(Const.uploadServerUrl, logName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> imgList = Util.getImgListOnDevice(deviceId);
		System.out.println(imgList);
		for (String imgName : imgList) {

			/**
			 * 把手机上的图片pull到PC本地
			 */
			String imgName1 = imgName;
			String imgName2 = imgName.replace("device_id", deviceId);// 注入多线程主线程启动时间run_stamp
			imgName2 = imgName2.replace("run_stamp", Const.timestamp);
			// "/" 这里的分隔符很重要
			String pullCmd = "adb -s " + deviceId
					+ " pull /sdcard/Robotium-Screenshots/" + imgName1 + " "
					+ Const.imgSavePath + "/" + imgName2;
			System.out.println(pullCmd);
			Util.excuteCmd(pullCmd);

			/**
			 * 图片信息写入数据库 %替换为deviceId
			 */
			// 20140422101239$%$20140422101239$com.etao.mobile.feedstream.FeedStreamActivity@422593f0$0.jpg
			String[] splitImgName2 = imgName2.split("[$]");
			// List<String> dao = Arrays.asList(splitImgName2);
			// for (String d : splitImgName2) {
			// System.out.println(d);
			// }
			RecordDao dao = new RecordDao();
			// "INSERT INTO ct_pic(run_stamp,device_id,img_name,url,img_timestamp)"
			Const.timestamp = splitImgName2[0];
			String device_id = splitImgName2[1];
			String img_timestamp = splitImgName2[2];
			String url = splitImgName2[3];
			String img_name = splitImgName2[3] + "$" + splitImgName2[4];

			dao.setTimestamp(Const.timestamp);
			dao.setDeviceId(device_id);
			dao.setImgTimeChamp(img_timestamp);
			dao.setActivityName(url);
			dao.setImgName(img_name);

			insertImg(dao);
			/**
			 * 上传图片到服务器
			 */
			String fullPathImgName2 = Const.imgSavePath + "/" + imgName2;
			try {
				fu.send(Const.uploadServerUrl, fullPathImgName2);
			} catch (IOException e) {
				e.printStackTrace();
			}

			/**
			 * 删除SDCard上的截图, 这里考虑到pull图片,直接删除,会导致IO失败
			 */
			// deleteImgsOnSDCard(deviceId);
			// 返回运行时间戳

		}

	}

	/**
	 * insertLog
	 * 
	 * @param dao
	 */
	public static void insertLog(LogDao dao) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			System.out
					.println("Success loading Mysql Driver  com.mysql.jdbc.Driver  !");
		} catch (Exception e) {
			System.out
					.print("Error loading Mysql Driver  com.mysql.jdbc.Driver  !");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection(Const.mysqlUrl,
					Const.mysqlUser, Const.mysqlPassword);

			System.out
					.println("Success connect Mysql server!" + Const.mysqlUrl);
			Statement stmt = connect.createStatement();
			String insertCmd = "INSERT INTO ct_log(run_stamp,device_id)"
					+ " VALUES (" + "'" + dao.getRun_stamp() + "','"
					+ dao.getDeviceId() + "')";
			System.out.println(insertCmd);
			boolean rs = stmt.execute(insertCmd);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * insertImg
	 * 
	 * @param dao
	 */
	public static void insertImg(RecordDao dao) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			System.out
					.println("Success loading Mysql Driver  com.mysql.jdbc.Driver  !");
		} catch (Exception e) {
			System.out
					.print("Error loading Mysql Driver  com.mysql.jdbc.Driver  !");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection(Const.mysqlUrl,
					Const.mysqlUser, Const.mysqlPassword);

			System.out
					.println("Success connect Mysql server!" + Const.mysqlUrl);
			Statement stmt = connect.createStatement();
			String insertCmd = "INSERT INTO ct_pic(run_stamp,device_id,img_name,url,img_timestamp)"
					+ " VALUES ("
					+ "'"
					+ dao.getTimestamp()
					+ "',"
					+ "'"
					+ dao.getDeviceId()
					+ "',"
					+ "'"
					+ dao.getImgName()
					+ "',"
					+ "'"
					+ dao.getActivityName()
					+ "','"
					+ dao.getImgTimeChamp() + "')";
			System.out.println(insertCmd);
			boolean rs = stmt.execute(insertCmd);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param deviceId
	 */
	public static void deleteImgsOnSDCard(String deviceId) {
		String delCmd = "adb -s " + deviceId
				+ " shell rm /sdcard/Robotium-Screenshots/*";
		Util.excuteCmd(delCmd);
	}
}
