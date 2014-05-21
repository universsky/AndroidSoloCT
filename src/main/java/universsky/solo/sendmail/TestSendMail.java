/**
 * universsky.solo TestSendMail.java 2014年4月23日
 */
package universsky.solo.sendmail;

/**
 * @author 东海陈光剑 2014年4月23日 下午3:16:46
 */
public class TestSendMail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendMail("20140421122151");
	}

	public static void sendMail(String run_stamp) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.xxxxx.com");
		/**
		 * 高级设置里必须更改端口为：SMTP服务器端口 465 要求安全连接打钩 POP3服务器端口 993 要求安全连接打钩
		 */
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("*****@****");
		mailInfo.setPassword("*******");// 您的邮箱密码
		mailInfo.setFromAddress("*****@****");
		mailInfo.setToAddress("*****@****");
		mailInfo.setSubject("适配测试报告  http://10.125.1.58:88/report.html");
		mailInfo.setContent("适配测试报告:"
				+ "<a href= \"http://10.125.1.58:88/report.html?run_stamp="
				+ run_stamp
				+ "\">http://10.125.1.58:88/report.html?run_stamp= "
				+ run_stamp + "</a>");
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		// sms.sendTextMail(mailInfo);// 发送文体格式
		SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
	}
}
