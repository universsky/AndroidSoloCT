/**
 * universsky.solo LogDao.java 2014��4��25��
 */
package universsky.solo.dao;

/**
 * @author �����¹⽣ 2014��4��25�� ����1:04:08
 */
public class LogDao {

    String run_stamp, deviceId;

    public LogDao(String run_stamp, String deviceId) {
	super();
	this.run_stamp = run_stamp;
	this.deviceId = deviceId;
    }

    public String getRun_stamp() {
	return run_stamp;
    }

    public void setRun_stamp(String run_stamp) {
	this.run_stamp = run_stamp;
    }

    public String getDeviceId() {
	return deviceId;
    }

    public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
    }

}
