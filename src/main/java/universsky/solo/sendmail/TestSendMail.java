/**
 * universsky.solo TestSendMail.java 2014��4��23��
 */
package universsky.solo.sendmail;

/**
 * @author �����¹⽣ 2014��4��23�� ����3:16:46
 */
public class TestSendMail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendMail("20140421122151");
	}

	public static void sendMail(String run_stamp) {
		// �������Ҫ�������ʼ�
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.xxxxx.com");
		/**
		 * �߼������������Ķ˿�Ϊ��SMTP�������˿� 465 Ҫ��ȫ���Ӵ� POP3�������˿� 993 Ҫ��ȫ���Ӵ�
		 */
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("*****@****");
		mailInfo.setPassword("*******");// ������������
		mailInfo.setFromAddress("*****@****");
		mailInfo.setToAddress("*****@****");
		mailInfo.setSubject("������Ա���  http://10.125.1.58:88/report.html");
		mailInfo.setContent("������Ա���:"
				+ "<a href= \"http://10.125.1.58:88/report.html?run_stamp="
				+ run_stamp
				+ "\">http://10.125.1.58:88/report.html?run_stamp= "
				+ run_stamp + "</a>");
		// �������Ҫ�������ʼ�
		SimpleMailSender sms = new SimpleMailSender();
		// sms.sendTextMail(mailInfo);// ���������ʽ
		SimpleMailSender.sendHtmlMail(mailInfo);// ����html��ʽ
	}
}
