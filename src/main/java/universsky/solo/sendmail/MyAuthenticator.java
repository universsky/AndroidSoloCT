/**
 * universsky.solo MyAuthenticator.java 2014年4月23日
 */
package universsky.solo.sendmail;

/**
 * @author 东海陈光剑
 * 2014年4月23日 下午1:53:32
 */
import javax.mail.*;

public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
	this.userName = username;
	this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication(userName, password);
    }
}
