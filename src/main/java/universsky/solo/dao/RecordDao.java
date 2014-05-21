/**
 * etao.test RecordDao.java 2014年4月20日
 */
package universsky.solo.dao;

/**
 * @author 东海陈光剑 2014年4月20日 下午11:09:50
 */
public class RecordDao {
    // "INSERT INTO ct_pic(run_stamp,device_id,img_name,url,img_timestamp)"
    String timestamp, deviceId, imgName, activityName, imgTimeChamp;

    public RecordDao() {
	super();
    }

    /**
     * 
     * @param timestamp
     * @param deviceId
     * @param imgName
     * @param activityName
     * @param imgTimeChamp
     */
    public RecordDao(String timestamp, String deviceId, String imgName,
	    String activityName, String imgTimeChamp) {
	super();
	this.timestamp = timestamp;
	this.deviceId = deviceId;
	this.imgName = imgName;
	this.activityName = activityName;
	this.imgTimeChamp = imgTimeChamp;
    }

    public String getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
    }

    public String getDeviceId() {
	return deviceId;
    }

    public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
    }

    public String getImgName() {
	return imgName;
    }

    public void setImgName(String imgName) {
	this.imgName = imgName;
    }

    public String getActivityName() {
	return activityName;
    }

    public void setActivityName(String activityName) {
	this.activityName = activityName;
    }

    public String getImgTimeChamp() {
	return imgTimeChamp;
    }

    public void setImgTimeChamp(String imgTimeChamp) {
	this.imgTimeChamp = imgTimeChamp;
    }

}
