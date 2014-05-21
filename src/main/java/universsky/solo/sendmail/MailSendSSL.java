/**
 * universsky.solo MailSendSSL.java 2014��4��23��
 */
package universsky.solo.sendmail;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import universsky.solo.util.Const;

/**
 * @author �����¹⽣ 2014��4��23�� ����4:18:39
 */
public class MailSendSSL {

	private String userName = "***@****.com";
	private String passWord = "******";
	private String smtp = "smtp.xxx.com";
	private String sendEmail = "***@****.com";
	private Message msg;

	public static void main(String[] args) {
		String run_stamp = "20140421122151";
		String min = "1";
		String sec = "70";
		sendMailSSL(run_stamp, min, sec, Const.toNames);
	}

	public static void sendMailSSL(String run_stamp, String min, String sec,
			String[] toNames) {

		MailSendSSL m = new MailSendSSL();
		String subject = "[һ���������]����";
		String fromEmail = "***@****.com";

		String emailBody = "[һ���������]����:"
				+ "<a href= \"http://10.125.1.58:88/report.html?run_stamp="
				+ run_stamp + "&min=" + min + "&sec=" + sec
				+ "\">http://10.125.1.58:88/report.html?run_stamp= "
				+ run_stamp + "&min=" + min + "&sec=" + sec + "</a>";

		StringBuffer sb = new StringBuffer(emailBody);

		for (String toName : toNames) {
			m.sendHtmlEmail(subject, fromEmail, toName, toName, emailBody);
		}

	}

	/**
	 * 
	 * @param subject
	 *            �ʼ�����
	 * @param fromEmail
	 *            ����������
	 * @param toEmail
	 *            �ռ���email
	 * @param toName
	 *            �ռ�������
	 * @param emailBody
	 *            �ʼ�����
	 * @return
	 */
	public String sendHtmlEmail(String subject, String fromEmail,
			String toEmail, String toName, String emailBody) {

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", smtp);
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");

		// MailSSLSocketFactory sf = new MailSSLSocketFactory();
		// sf.setTrustAllHosts(true);
		// // sf.setTrustedHosts(new String[] { "my-server" });
		// props.put("mail.smtp.ssl.enable", "true");
		// // also use following for additional safety
		// //props.put("mail.smtp.ssl.checkserveridentity", "true");
		// props.put("mail.smtp.ssl.socketFactory", sf);
		//

		// �����û�������
		Session session = Session.getDefaultInstance(props,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, passWord);
					}
				});
		// -- Create a new message --
		msg = new MimeMessage(session);
		try {
			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress(sendEmail, fromEmail));// ���÷�����
			// ����������
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail, false));// �����ռ���
			msg.setSubject(subject);// ��������
			// ����ͨ��������
			// ��HTML��ʽ����
			msg.setContent(emailBody, "text/html;charset=utf-8");// �ʼ�����
			msg.setSentDate(new Date());
			Transport.send(msg);// �����ʼ�
			System.out.println("Message sent: " + toEmail);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Y";

	}
}
